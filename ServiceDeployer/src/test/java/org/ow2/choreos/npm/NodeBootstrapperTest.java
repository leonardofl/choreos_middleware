package org.ow2.choreos.npm;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.ow2.choreos.npm.cloudprovider.CloudProvider;
import org.ow2.choreos.npm.cloudprovider.FixedCloudProvider;
import org.ow2.choreos.npm.cm.NodeBootstrapper;
import org.ow2.choreos.npm.datamodel.Node;
import org.ow2.choreos.tests.IntegrationTest;
import org.ow2.choreos.utils.LogConfigurator;


/**
 * Before run this test, restore your VM to a snapshot before the bootstrap
 * 
 * If the machine is already bootstrapped the test must still pass,
 * but it will not properly test the system
 * 
 * @author leonardo
 *
 */
@Category(IntegrationTest.class)
public class NodeBootstrapperTest {
    
	@Before
	public void setUp() {
		LogConfigurator.configLog();
	}
	
	/**
	 * Beware, this test will leave the node bootstrapped
	 * 
	 * @throws Exception
	 */
	@Test
	public void shouldLeaveNodeBootstraped() throws Exception {

		CloudProvider cp = new FixedCloudProvider();
		Node node = cp.createOrUseExistingNode(new Node());
		System.out.println(node);

		NodeBootstrapper bootstrapper = new NodeBootstrapper(node);
		if (!bootstrapper.isNodeAlreadyBootstrapped()) {
			System.out.println("Going to bootstrap the node");
			bootstrapper.bootstrapNode();
			System.out.println("Checking if bootstrap was OK");
			assertTrue(bootstrapper.isNodeAlreadyBootstrapped());
		} else {
			System.out.println("Node was already bootstrapped");
		}
	}
        
    
}