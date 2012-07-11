package org.ow2.choreos.npm.selector;

import java.util.List;

import org.jclouds.compute.RunNodesException;
import org.ow2.choreos.npm.cloudprovider.CloudProvider;
import org.ow2.choreos.npm.datamodel.Config;
import org.ow2.choreos.npm.datamodel.Node;


public class SimpleRoundRobinSelector implements NodeSelector {

	private List<Node> availableNodes;
	private CloudProvider cloudProvider;
	private int lastUsedNode = -1;

	public List<Node> getAvailableNodes() {
		return availableNodes;
	}

	public SimpleRoundRobinSelector(CloudProvider provider,
			int minimalInstantiatedNodeAmmount) {

		availableNodes = provider.getNodes();
		cloudProvider = provider;

		createInstantiatedNodePool(minimalInstantiatedNodeAmmount, provider);

	}

	public void createInstantiatedNodePool(int minimalInstantiatedNodeAmmount,
			CloudProvider provider) {

		for (int i = availableNodes.size(); i < minimalInstantiatedNodeAmmount; i++) {
			Node createdNode = createNewNode(provider);
			availableNodes.add(createdNode);
		}
	}

	private Node createNewNode(CloudProvider provider) {
		Node node = new Node();
		Node responseNode = null;
		try {
			responseNode = provider.createNode(node);
		} catch (RunNodesException e) {
			System.out.println("Could not create new node at "
					+ provider.getproviderName());
			e.printStackTrace();
		}

		return responseNode;
	}

	public void changeNodePoolSize(int newSize) {
		int currentSize = availableNodes.size();
		System.out.println(newSize + "  " + currentSize);
		if (newSize > currentSize){
			createInstantiatedNodePool(newSize, cloudProvider);
		}
		else {
			int nodesToKill = currentSize - newSize;
			System.out.println("Nodes to kill: " + nodesToKill);
			for(int i = 0; i < nodesToKill; i++){
				System.out.println("Killing node...");
				destroyLastNode();
			}
		}

	}

	private void destroyLastNode() {
		int lastNodeIndex = availableNodes.size()-1;
		cloudProvider.destroyNode(availableNodes.get(lastNodeIndex).getId());
	}

	public Node selectNode(Config config) {

		System.out.println("nodes in selector: " + availableNodes.size());
		System.out.println("Pointer: " + lastUsedNode);
		Node node = availableNodes.get(getNextIndex());

		return node;
	}

	public int getNextIndex() {
		lastUsedNode++;

		if (lastUsedNode == availableNodes.size()) {
			lastUsedNode = 0;
		}

		return lastUsedNode;
	}

}