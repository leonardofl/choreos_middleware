/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.ow2.choreos.deployment.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.ow2.choreos.chef.Knife;
import org.ow2.choreos.chef.KnifeCookbook;
import org.ow2.choreos.chef.KnifeException;
import org.ow2.choreos.nodes.NodePoolManager;
import org.ow2.choreos.nodes.PrepareDeploymentFailedException;
import org.ow2.choreos.nodes.datamodel.DeploymentRequest;
import org.ow2.choreos.nodes.datamodel.Node;
import org.ow2.choreos.services.ServiceNotDeployedException;
import org.ow2.choreos.services.ServicesManager;
import org.ow2.choreos.services.datamodel.DeployableService;
import org.ow2.choreos.services.datamodel.DeployableServiceSpec;
import org.ow2.choreos.services.datamodel.PackageType;
import org.ow2.choreos.services.datamodel.ServiceInstance;
import org.ow2.choreos.utils.LogConfigurator;

public class ServiceDeployerImplTest {

    private NodePoolManager npm;
    private ServicesManager servicesManager;

    private Node selectedNode;
    private DeployableServiceSpec serviceSpec;

    @Before
    public void setUp() throws PrepareDeploymentFailedException, KnifeException {

	LogConfigurator.configLog();
	setUpNPM();
	setUpServiceDeployer();
    }

    private void setUpNPM() throws PrepareDeploymentFailedException {

	selectedNode = new Node();
	selectedNode.setId("1");
	selectedNode.setIp("192.168.56.102");
	selectedNode.setHostname("CHOREOS-NODE");

	List<Node> selectedNodes = new ArrayList<Node>();
	selectedNodes.add(selectedNode);

	npm = mock(NodePoolManager.class);
	when(npm.prepareDeployment(any(DeploymentRequest.class))).thenReturn(selectedNodes);
    }

    private void setUpServiceDeployer() throws KnifeException {

	serviceSpec = new DeployableServiceSpec();
	serviceSpec.setPackageUri("http://choreos.eu/services/airline.jar");
	serviceSpec.setPackageType(PackageType.COMMAND_LINE);
	serviceSpec.setEndpointName("airline");
	serviceSpec.setPort(8042);

	Knife knife = mock(Knife.class);
	KnifeCookbook knifeCookbbok = mock(KnifeCookbook.class);
	when(knife.cookbook()).thenReturn(knifeCookbbok);
	String cookbookUploadResult = "Cookbook 'uploaded' by mock";
	when(knifeCookbbok.upload(any(String.class), any(String.class))).thenReturn(cookbookUploadResult);

	servicesManager = new ServicesManagerImpl(npm, knife);
    }

    @Test
    public void shouldReturnAValidService() throws PrepareDeploymentFailedException, ServiceNotDeployedException {

	final String EXPECTED_URI = "http://" + selectedNode.getIp() + ":" + serviceSpec.getPort() + "/"
		+ serviceSpec.getEndpointName() + "/";

	DeployableService service = servicesManager.createService(serviceSpec);

	ServiceInstance instance = service.getInstances().get(0);

	assertEquals(selectedNode.getHostname(), instance.getNode().getHostname());
	assertEquals(selectedNode.getIp(), instance.getNode().getIp());
	assertEquals(selectedNode.getId(), instance.getNode().getId());
	assertEquals(EXPECTED_URI, instance.getNativeUri());

	verify(npm).prepareDeployment(any(DeploymentRequest.class));
    }

}
