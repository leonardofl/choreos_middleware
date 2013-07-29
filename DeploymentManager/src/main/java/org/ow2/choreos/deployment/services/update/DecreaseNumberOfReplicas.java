package org.ow2.choreos.deployment.services.update;

import org.ow2.choreos.deployment.services.preparer.ServiceUndeploymentPreparer;
import org.ow2.choreos.deployment.services.preparer.ServiceUndeploymentPreparerFactory;
import org.ow2.choreos.services.datamodel.DeployableService;
import org.ow2.choreos.services.datamodel.DeployableServiceSpec;

public class DecreaseNumberOfReplicas extends BaseAction {

    private static final String NAME = "Decrease number of instances";

    private DeployableService currentService;
    private DeployableServiceSpec newSpec;

    public DecreaseNumberOfReplicas(DeployableService currentService, DeployableServiceSpec newSpec) {
	this.currentService = currentService;
	this.newSpec = newSpec;
    }

    @Override
    public void applyUpdate() throws UpdateActionFailedException {
	int decreaseAmount = currentService.getSpec().getNumberOfInstances() - newSpec.getNumberOfInstances();

	ServiceUndeploymentPreparer undeploymentPreparer = ServiceUndeploymentPreparerFactory.getNewInstance(
		currentService, decreaseAmount);

	try {
	    undeploymentPreparer.prepareUndeployment();
	} catch (Exception e) {
	    throw new UpdateActionFailedException();
	}
    }

    @Override
    public String getName() {
	return NAME;
    }

}
