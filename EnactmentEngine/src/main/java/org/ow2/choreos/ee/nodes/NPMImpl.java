/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.ow2.choreos.ee.nodes;

import java.util.List;

import org.apache.log4j.Logger;
import org.ow2.choreos.ee.config.CloudConfiguration;
import org.ow2.choreos.ee.config.EEConfiguration;
import org.ow2.choreos.ee.nodes.cloudprovider.CloudProvider;
import org.ow2.choreos.ee.nodes.cloudprovider.CloudProviderFactory;
import org.ow2.choreos.ee.nodes.cloudprovider.FixedCloudProvider;
import org.ow2.choreos.nodes.NodeNotCreatedException;
import org.ow2.choreos.nodes.NodeNotDestroyed;
import org.ow2.choreos.nodes.NodeNotFoundException;
import org.ow2.choreos.nodes.NodePoolManager;
import org.ow2.choreos.nodes.datamodel.CloudNode;
import org.ow2.choreos.nodes.datamodel.NodeSpec;

/**
 * 
 * @author leonardo, furtado
 * 
 */
public class NPMImpl implements NodePoolManager {

    private static final Logger logger = Logger.getLogger(NPMImpl.class);

    private CloudProvider cloudProvider;
    private NodeRegistry nodeRegistry;
    private boolean useThePool;
    private Reservoir reservoir;
    private CloudConfiguration cloudConfiguration;

    public NPMImpl(CloudConfiguration cloudConfiguration) {
        this.cloudConfiguration = cloudConfiguration;
        cloudProvider = CloudProviderFactory.getFactoryInstance().getCloudProviderInstance(cloudConfiguration);
        nodeRegistry = NodeRegistry.getInstance();
        loadPool();
    }

    private void loadPool() {
        useThePool = Boolean.parseBoolean(EEConfiguration.get("RESERVOIR"));
        if (useThePool) {
            ReservoirFactory factory = new ReservoirFactory();
            reservoir = factory.getReservoir(cloudConfiguration);
        }
    }

    public NPMImpl(CloudConfiguration cloudConfiguration, Reservoir pool) {
        cloudProvider = CloudProviderFactory.getFactoryInstance().getCloudProviderInstance(cloudConfiguration);
        nodeRegistry = NodeRegistry.getInstance();
        reservoir = pool;
    }

    @Override
    public CloudNode createNode(NodeSpec nodeSpec) throws NodeNotCreatedException {
        CloudNode node = new CloudNode();
        NodeCreatorFactory factory = new NodeCreatorFactory();
        NodeCreator nodeCreator = factory.getNewNodeCreator(cloudConfiguration);
        try {
            node = nodeCreator.createBootstrappedNode(nodeSpec);
        } catch (NodeNotCreatedException e1) {
            // if node creation has failed, let's retrieve a node from the pool
            // since waiting for a new node would take too much time!
            if (useThePool) {
                try {
                    logger.warn("Node creation failed, let's retrieve a node from the pool");
                    node = reservoir.retriveNode();
                } catch (NodeNotCreatedException e2) {
                    throw new NodeNotCreatedException();
                }
            } else {
                throw new NodeNotCreatedException();
            }
        }
        nodeRegistry.putNode(node);
        if (useThePool) {
            // we want the pool to be always filled
            // whenever requests are coming
            reservoir.fillPool();
        }
        return node;
    }

    @Override
    public List<CloudNode> getNodes() {
        if (this.cloudProvider.getCloudProviderName() == FixedCloudProvider.FIXED_CLOUD_PROVIDER) {
            return this.cloudProvider.getNodes();
        } else {
            return nodeRegistry.getNodes();
        }
    }

    @Override
    public CloudNode getNode(String nodeId) throws NodeNotFoundException {
        if (this.cloudProvider.getCloudProviderName() == FixedCloudProvider.FIXED_CLOUD_PROVIDER) {
            return this.cloudProvider.getNode(nodeId);
        } else {
            return nodeRegistry.getNode(nodeId);
        }
    }

    @Override
    public void destroyNode(String nodeId) throws NodeNotDestroyed, NodeNotFoundException {
        if (nodeRegistry.getNode(nodeId) == null)
            throw new NodeNotFoundException(nodeId);
        cloudProvider.destroyNode(nodeId);
        nodeRegistry.deleteNode(nodeId);
        logger.info("Node " + nodeId + " destroyed");
    }

    @Override
    public void destroyNodes() throws NodeNotDestroyed {
        if (useThePool) {
            try {
                reservoir.emptyPool();
            } catch (NodeNotDestroyed e) {
                logger.error("Could not destroy all the idle nodes");
                throw e;
            }
        }
        NodesDestroyer destroyer = new NodesDestroyer(cloudConfiguration, this.getNodes());
        try {
            List<CloudNode> destroyedNodes = destroyer.destroyNodes();
            nodeRegistry.deleteNodes(destroyedNodes);
        } catch (NodeNotDestroyed e) {
            logger.error("Could not destroy all the nodes");
            nodeRegistry.deleteNodes(destroyer.getDestroyedNodes());
            throw e;
        }
    }

}
