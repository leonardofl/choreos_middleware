package org.ow2.choreos.ee.services.preparer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.ow2.choreos.ee.nodes.selector.NodeSelector;
import org.ow2.choreos.ee.nodes.selector.NodeSelectorFactory;
import org.ow2.choreos.nodes.datamodel.CloudNode;
import org.ow2.choreos.selectors.NotSelectedException;
import org.ow2.choreos.services.datamodel.DeployableService;
import org.ow2.choreos.services.datamodel.DeployableServiceSpec;

public class ServiceDeploymentPreparer {

    private DeployableService service;
    private DeployableServiceSpec spec;
    private String serviceSpecName;
    private Set<CloudNode> nodes;

    private Logger logger = Logger.getLogger(ServiceDeploymentPreparer.class);

    public ServiceDeploymentPreparer(DeployableService service) {
        this.service = service;
        this.spec = service.getSpec();
        this.serviceSpecName = spec.getName();
    }

    public void prepareDeployment() throws PrepareDeploymentFailedException {
        selectNodes();
        service.addSelectedNodes(nodes);
        prepareInstances();
    }

    private void selectNodes() throws PrepareDeploymentFailedException {
        NodeSelector selector = NodeSelectorFactory.getFactoryInstance().getNodeSelectorInstance();
        int numberOfNewInstances = getNumberOfNewInstances();
        if (numberOfNewInstances > 0) {
            try {
                List<CloudNode> nodesList = selector.select(spec, numberOfNewInstances);
                nodes = new HashSet<CloudNode>(nodesList);
                logger.info("Selected nodes to " + serviceSpecName + ": " + nodes);
            } catch (NotSelectedException e) {
                failDeployment();
            }
            if (nodes == null || nodes.isEmpty()) {
                failDeployment();
            }
        } else {
            nodes = new HashSet<CloudNode>();
        }
    }

    private int getNumberOfNewInstances() {
        int selectedNodesLen = service.getSelectedNodes() == null ? 0 : service.getSelectedNodes().size();
        int numberOfNewInstances = spec.getNumberOfInstances() - selectedNodesLen;
        return numberOfNewInstances;
    }

    private void failDeployment() throws PrepareDeploymentFailedException {
        throw new PrepareDeploymentFailedException(serviceSpecName);
    }

    private void prepareInstances() {
        for (CloudNode node : nodes) {
            try {
                InstanceDeploymentPreparer instanceDeploymentPreparer = new InstanceDeploymentPreparer(spec,
                        service, node);
                instanceDeploymentPreparer.prepareDeployment();
            } catch (PrepareDeploymentFailedException e) {
                logger.error(e.getMessage());
            }
        }
    }

}
