package org.ow2.choreos.ee.nodes.cloudprovider;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.jclouds.ContextBuilder;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.RunNodesException;
import org.jclouds.compute.domain.ComputeMetadata;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.Template;
import org.jclouds.compute.domain.TemplateBuilder;
import org.jclouds.compute.options.TemplateOptions;
import org.jclouds.ec2.domain.InstanceType;
import org.ow2.choreos.invoker.Invoker;
import org.ow2.choreos.invoker.InvokerException;
import org.ow2.choreos.invoker.InvokerFactory;
import org.ow2.choreos.nodes.NodeNotCreatedException;
import org.ow2.choreos.nodes.NodeNotDestroyed;
import org.ow2.choreos.nodes.NodeNotFoundException;
import org.ow2.choreos.nodes.datamodel.CloudNode;
import org.ow2.choreos.nodes.datamodel.NodeSpec;
import org.ow2.choreos.nodes.datamodel.ResourceImpact;

import com.google.common.collect.Iterables;

public abstract class JCloudsCloudProvider implements CloudProvider {

	private static final String CREATION_TASK_NAME = "NODE_CREATION";
	private static final String DELETION_TASK_NAME = "NODE_DELETION";

	protected String identity, credential, provider;
	protected Properties properties;

	private Logger logger = Logger.getLogger(JCloudsCloudProvider.class);

	protected abstract String getDefaultImageId();

	protected abstract String getHardwareId(NodeSpec nodeSpec);

	protected abstract String getUserName();

	protected abstract String getUserPrivateKey();

	protected abstract void configureTemplateOptions(
			TemplateOptions templateOptions);

	protected abstract String getNodeIp(NodeMetadata nodeMetadata);

	@Override
	public CloudNode createNode(NodeSpec nodeSpec)
			throws NodeNotCreatedException {
		logger.debug("Creating node...");
		CreateNodeTask task = new CreateNodeTask(nodeSpec);
		InvokerFactory<CloudNode> factory = new InvokerFactory<CloudNode>();
		Invoker<CloudNode> invoker = factory.geNewInvokerInstance(
				CREATION_TASK_NAME, task);
		try {
			CloudNode node = invoker.invoke();
			return node;
		} catch (InvokerException e) {
			logger.error("Could not create CloudNode", e.getCause());
			throw new NodeNotCreatedException();
		}
	}

	protected ComputeService getComputeService() {
		ContextBuilder builder = ContextBuilder.newBuilder(provider)
				.credentials(identity, credential).overrides(properties);
		ComputeService compute = null;
		compute = builder.buildView(ComputeServiceContext.class)
				.getComputeService();
		return compute;
	}

	protected CloudNode getCloudNodeFromMetadata(NodeMetadata nodeMetadata) {
		CloudNode node = new CloudNode();
		node.setIp(getNodeIp(nodeMetadata));
		node.setHostname(nodeMetadata.getName());
		node.setSo(nodeMetadata.getOperatingSystem().getName());
		node.setId(nodeMetadata.getId());
		node.setImage(nodeMetadata.getImageId());
		node.setState(nodeMetadata.getStatus().ordinal());
		node.setUser(getUserName());
		node.setPrivateKey(getUserPrivateKey());
		return node;
	}

	protected Template getTemplate(ComputeService client, NodeSpec nodeSpec) {

		String imageId = nodeSpec.getImage();
		if (imageId == null || imageId.isEmpty()) {
			imageId = getDefaultImageId();
		}
		String hardwareId = getHardwareId(nodeSpec);
		logger.info("Creating Template with image ID: " + imageId
				+ "; hardware ID: " + hardwareId);

		TemplateBuilder builder = client.templateBuilder().imageId(imageId);
		builder.hardwareId(hardwareId);
		Template template = null;
		try {
			template = builder.build();
		} catch (Exception e) {
			logger.error("Could not build template because: " + e.getMessage());
		}
		configureTemplateOptions(template.getOptions());
		return template;
	}

	@SuppressWarnings("unused")
	private String getInstanceTypeFromResourceImpact(
			ResourceImpact resourceImpact) {

		String defaultImage = InstanceType.M1_SMALL;

		if (resourceImpact != null && resourceImpact.getRAM() != null) {
			switch (resourceImpact.getRAM()) {
			case SMALL:
				return InstanceType.M1_SMALL;
			case MEDIUM:
				return InstanceType.M1_MEDIUM;
			case LARGE:
				return InstanceType.M1_LARGE;
			default:
				return defaultImage;
			}
		}
		return defaultImage;
	}

	@Override
	public CloudNode getNode(String nodeId) throws NodeNotFoundException {
		ComputeService client = getComputeService();
		try {
			NodeMetadata nodeMetadata = client.getNodeMetadata(nodeId);
			CloudNode node = getCloudNodeFromMetadata(nodeMetadata);
			client.getContext().close();
			return node;
		} catch (Exception e) {
			throw new NodeNotFoundException(nodeId);
		}
	}

	@Override
	public List<CloudNode> getNodes() {
		List<CloudNode> nodeList = new ArrayList<CloudNode>();
		ComputeService client = getComputeService();
		Set<? extends ComputeMetadata> cloudNodes = client.listNodes();
		for (ComputeMetadata computeMetadata : cloudNodes) {
			NodeMetadata nodeMetadata = client.getNodeMetadata(computeMetadata
					.getId());
			CloudNode node = getCloudNodeFromMetadata(nodeMetadata);
			if (node.getState() != 1 && node.hasIp()) {
				nodeList.add(node);
			}
		}
		client.getContext().close();
		return nodeList;
	}

	@Override
	public void destroyNode(String nodeId) throws NodeNotDestroyed {
		DestroyNodeTask task = new DestroyNodeTask(nodeId);
		InvokerFactory<Void> factory = new InvokerFactory<Void>();
		Invoker<Void> invoker = factory.geNewInvokerInstance(
				DELETION_TASK_NAME, task);
		try {
			invoker.invoke();
		} catch (InvokerException e) {
			throw new NodeNotDestroyed(nodeId);
		}
	}

	@Override
	public CloudNode createOrUseExistingNode(NodeSpec nodeSpec)
			throws NodeNotCreatedException {
		List<CloudNode> nodes = this.getNodes();
		if (nodes.size() > 0)
			return nodes.get(0);
		else
			return createNode(nodeSpec);
	}

	private class CreateNodeTask implements Callable<CloudNode> {

		NodeSpec nodeSpec;

		public CreateNodeTask(NodeSpec nodeSpec) {
			this.nodeSpec = nodeSpec;
		}

		@Override
		public CloudNode call() throws Exception {
			ComputeService computeService = getComputeService();
			try {
				Template template = getTemplate(computeService, nodeSpec);
				Set<? extends NodeMetadata> createdNodes = computeService
						.createNodesInGroup("default", 1, template);
				NodeMetadata nodeMetadata = Iterables.get(createdNodes, 0);
				CloudNode node = getCloudNodeFromMetadata(nodeMetadata);
				if (!node.hasIp()) {
					throw new NodeNotCreatedException(
							"Could not retrieve IP from just created node.");
				}
				logger.debug(node + " created");
				computeService.getContext().close();
				return node;
			} catch (RunNodesException e) {
				logger.error("Node creation failed: " + e.getMessage());
				throw new NodeNotCreatedException();
			} catch (org.jclouds.rest.AuthorizationException e) {
				logger.error("Authorization failed. Provided user doesn't have authorization to create a new node.");
				throw new NodeNotCreatedException();
			} catch (IllegalStateException e) {
				logger.error(e);
				throw new NodeNotCreatedException();
			}
		}

	}

	private class DestroyNodeTask implements Callable<Void> {

		String nodeId;

		public DestroyNodeTask(String nodeId) {
			this.nodeId = nodeId;
		}

		@Override
		public Void call() {
			ComputeService client = getComputeService();
			client.destroyNode(nodeId);
			client.getContext().close();
			return null;
		}

	}

}
