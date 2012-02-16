package br.usp.ime.choreos.nodepoolmanager.cloudprovider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.cxf.common.util.StringUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jclouds.aws.ec2.compute.AWSEC2ComputeService;
import org.jclouds.aws.ec2.compute.AWSEC2TemplateOptions;
import org.jclouds.aws.ec2.reference.AWSEC2Constants;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.ComputeServiceContextFactory;
import org.jclouds.compute.RunNodesException;
import org.jclouds.compute.domain.ComputeMetadata;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.NodeState;
import org.jclouds.compute.domain.Template;
import org.jclouds.compute.domain.TemplateBuilder;
import org.jclouds.domain.JsonBall;
import org.jclouds.ec2.domain.InstanceType;

import br.usp.ime.choreos.nodepoolmanager.Configuration;
import br.usp.ime.choreos.nodepoolmanager.Node;
import br.usp.ime.choreos.nodepoolmanager.NodeNotFoundException;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.inject.Module;

public class AWSCloudProvider implements CloudProvider {

	private static String DEFAULT_IMAGE = "us-east-1/ami-ccf405a5";
	
	private ComputeService getClient(String imageId) {
		String provider = Configuration.get("DEFAULT_PROVIDER");
		if (provider == null || provider.isEmpty()) {
			provider = "aws-ec2";
		}

		return getClient(imageId, provider);
	}

	private ComputeService getClient(String imageId, String provider) {
		// If we specify the image, it doesn't download info about all others
		Properties overrides = new Properties();
		overrides.setProperty(AWSEC2Constants.PROPERTY_EC2_AMI_QUERY,
				"image-id=" + imageId);

		// get a context with ec2 that offers the portable ComputeService api
		ComputeServiceContext context = new ComputeServiceContextFactory()
				.createContext(provider,
						Configuration.get("AMAZON_ACCESS_KEY_ID"),
						Configuration.get("AMAZON_SECRET_KEY"),
						ImmutableSet.<Module> of(), overrides);

		return context.getComputeService();
	}

	public Node createNode(Node node) throws RunNodesException {
		System.out.println("Creating node...");

		String imageId = node.getImage();
		if (StringUtils.isEmpty(imageId)) {
			imageId = DEFAULT_IMAGE; // TODO put default image in a
												// configuration file
		}
		String image = imageId.substring(imageId.indexOf('/') + 1);

		ComputeService client = getClient(image);
		Set<? extends NodeMetadata> createdNodes = client.createNodesInGroup(
				"default", 1, getTemplate(client, imageId));
		NodeMetadata cloudNode = Iterables.get(createdNodes, 0);

		setNodeProperties(node, cloudNode);

//		if (!(client.getContext() instanceof Proxy)) {
//			//initializeNode(node);
//		}

		client.getContext().close();

		// create node in chef database
		// org.jclouds.chef.domain.Node chefNode = new
		// org.jclouds.chef.domain.Node(node.getId(),
		// ImmutableSet.of("role[petals]"));
		// chefNode.getAutomatic().put("cloud", new
		// JsonBall("{\"public_host_name\":\"" + node.getHostname() + "\"}"));
		// chefNode.getAutomatic().put("ec2", new JsonBall("{'ami_id': '" +
		// node.getImage() + "'}"));
		// chefNode.getAutomatic().put("os", new JsonBall(node.getSo()));
		// ChefHelper.getChefContext().getApi().createNode(chefNode);

		System.out.println("Node created");
		return node;
	}



	public Node getNode(String nodeId) throws NodeNotFoundException {
		ComputeService client = getClient("");

		Node node = new Node();

		try {
			NodeMetadata cloudNode = client.getNodeMetadata(nodeId);
			setNodeProperties(node, cloudNode);
		} catch (Exception e) {
			throw new NodeNotFoundException();
		}

		return node;
	}

	public List<Node> getNodes() {
		List<Node> nodeList = new ArrayList<Node>();
		Node node;

		// for (String nodeString :
		// ChefHelper.getChefContext().getApi().listNodes()) {
		// org.jclouds.chef.domain.Node chefNode =
		// ChefHelper.getChefContext().getApi().getNode(nodeString);
		// node = new Node();
		//
		// try {
		// setNodeProperties(node, chefNode);
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }
		// if (node.getState() != 1) {
		// nodeList.add(node);
		// }
		// }

		ComputeService client = getClient("");
		Set<? extends ComputeMetadata> cloudNodes = client.listNodes();
		for (ComputeMetadata computeMetadata : cloudNodes) {
			NodeMetadata cloudNode = client.getNodeMetadata(computeMetadata
					.getId());
			node = new Node();

			setNodeProperties(node, cloudNode);
			if (node.getState() != 1) {
				nodeList.add(node);
			}
		}

		client.getContext().close();

		return nodeList;
	}

	public void destroyNode(String id) {
		ComputeService client = getClient("");
		client.destroyNode(id);

		client.getContext().close();
	}

	private void setNodeProperties(Node node, NodeMetadata cloudNode) {
		setNodeIp(node, cloudNode);
		node.setHostname(cloudNode.getName());
		node.setSo(cloudNode.getOperatingSystem().getName());
		node.setId(cloudNode.getId());
		node.setImage(cloudNode.getImageId());
		node.setState(cloudNode.getState().ordinal());
	}

	private void setNodeIp(Node node, NodeMetadata cloudNode) {
		Iterator<String> publicAddresses = cloudNode.getPublicAddresses()
				.iterator();

		if (publicAddresses != null && publicAddresses.hasNext()) {
			node.setIp(publicAddresses.next());
		}
	}

	private void setNodeProperties(Node node,
			org.jclouds.chef.domain.Node chefNode) throws JSONException {
		JSONObject cloudJSON = new JSONObject(chefNode.getAutomatic()
				.get("cloud").toString());
		JSONObject ec2JSON = new JSONObject(chefNode.getAutomatic().get("ec2")
				.toString());

		setNodeIp(node, chefNode);
		node.setHostname(cloudJSON.getString("public_hostname"));
		node.setSo(chefNode.getAutomatic().get("os").toString());
		node.setId(chefNode.getName());
		node.setImage(ec2JSON.getString("ami_id"));
		// node.setState(chefNode.getState().ordinal());
	}

	private void setNodeIp(Node node, org.jclouds.chef.domain.Node chefNode)
			throws JSONException {
		JsonBall cloud = chefNode.getAutomatic().get("cloud");
		JSONObject j = new JSONObject(cloud.toString());
		node.setIp(j.getString("public_ipv4"));
	}

	private Template getTemplate(ComputeService client, String imageId) {
		TemplateBuilder builder = client.templateBuilder().imageId(imageId);
		if (client instanceof AWSEC2ComputeService) {
			builder.hardwareId(InstanceType.M1_SMALL);
			Template template = builder.build();
			AWSEC2TemplateOptions options = template.getOptions().as(
					AWSEC2TemplateOptions.class);
			options.securityGroups("default");
			options.keyPair(Configuration.get("AMAZON_KEY_PAIR"));
			return template;
		}

		return builder.build();
	}

	public Node createOrUseExistingNode(Node node) throws RunNodesException {
		for (Node n : getNodes()) {
			if (n.getImage().equals(node.getImage())
					&& NodeState.RUNNING.ordinal() == n.getState()) {
				return n;
			}
		}
		return createNode(node);
	}
}