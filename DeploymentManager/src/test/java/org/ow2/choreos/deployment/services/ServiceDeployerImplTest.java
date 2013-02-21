package org.ow2.choreos.deployment.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.ow2.choreos.chef.Knife;
import org.ow2.choreos.chef.KnifeCookbook;
import org.ow2.choreos.chef.KnifeException;
import org.ow2.choreos.deployment.nodes.ConfigNotAppliedException;
import org.ow2.choreos.deployment.nodes.NodePoolManager;
import org.ow2.choreos.deployment.nodes.datamodel.Config;
import org.ow2.choreos.deployment.nodes.datamodel.Node;
import org.ow2.choreos.deployment.services.datamodel.ArtifactType;
import org.ow2.choreos.deployment.services.datamodel.Service;
import org.ow2.choreos.deployment.services.datamodel.ServiceInstance;
import org.ow2.choreos.deployment.services.datamodel.ServiceSpec;
import org.ow2.choreos.utils.LogConfigurator;

public class ServiceDeployerImplTest {

	private NodePoolManager npm; 
	private ServiceDeployer serviceDeployer;
	
	private Node selectedNode;
	private ServiceSpec serviceSpec;
	
	@Before
	public void setUp() throws ConfigNotAppliedException, KnifeException {
	
		LogConfigurator.configLog();
		setUpNPM();
		setUpServiceDeployer();
	}
	
	private void setUpNPM() throws ConfigNotAppliedException {
		
		selectedNode = new Node();
		selectedNode.setId("1");
		selectedNode.setIp("192.168.56.102");
		selectedNode.setHostname("CHOREOS-NODE");
		
		List<Node> selectedNodes = new ArrayList<Node>();
		selectedNodes .add(selectedNode);
		
		npm = mock(NodePoolManager.class);
		when(npm.applyConfig(any(Config.class), any(Integer.class))).thenReturn(selectedNodes);
	}
	
	private void setUpServiceDeployer() throws KnifeException {
		
		serviceSpec = new ServiceSpec();
		serviceSpec.setName("Airline");
		serviceSpec.setDeployableUri("http://choreos.eu/services/airline.jar");
		serviceSpec.setArtifactType(ArtifactType.COMMAND_LINE);
		serviceSpec.setEndpointName("airline");
		serviceSpec.setPort(8042);
		
		Knife knife = mock(Knife.class);
		KnifeCookbook knifeCookbbok = mock(KnifeCookbook.class);
		when(knife.cookbook()).thenReturn(knifeCookbbok);
		String cookbookUploadResult = "Cookbook 'uploaded' by mock";
		when(knifeCookbbok.upload(any(String.class), any(String.class))).thenReturn(cookbookUploadResult);
		
		serviceDeployer = new ServiceDeployerImpl(npm, knife);
	}
	
	//@Test
	public void shouldReturnAValidService() throws ConfigNotAppliedException, ServiceNotDeployedException {

		final String EXPECTED_URI = "http://" + selectedNode.getIp() + ":"
				+ serviceSpec.getPort() + "/" + serviceSpec.getEndpointName()
				+ "/";
		
		Service service = serviceDeployer.deploy(serviceSpec);
		
		ServiceInstance instance = service.getInstances().get(0);
		
		assertEquals(selectedNode.getHostname(), instance.getHost());
		assertEquals(selectedNode.getIp(), instance.getIp());
		assertEquals(selectedNode.getId(), instance.getNodeId());
		assertEquals(EXPECTED_URI, instance.getUri());
		
		verify(npm).applyConfig(any(Config.class), any(Integer.class));
	}
}
