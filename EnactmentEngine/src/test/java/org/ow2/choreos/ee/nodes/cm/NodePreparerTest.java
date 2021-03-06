package org.ow2.choreos.ee.nodes.cm;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.ow2.choreos.ee.config.QoSManagementConfiguration;
import org.ow2.choreos.nodes.datamodel.CloudNode;
import org.ow2.choreos.utils.SshCommandFailed;
import org.ow2.choreos.utils.SshNotConnected;
import org.ow2.choreos.utils.SshUtil;
import org.ow2.choreos.utils.SshWaiter;

import com.jcraft.jsch.JSchException;

public class NodePreparerTest {

    private CloudNode node;
    private SshUtil ssh;
    private SshWaiter waiter;

    private final String INSTANCE_ID = "123456";

    @Before
    public void setUp() throws Exception {
	QoSManagementConfiguration.set(QoSManagementConfiguration.QOS_MGMT, "False");
	setNode();
	setSsh();
    }

    private void setNode() {
	node = new CloudNode();
	node.setIp("192.168.56.101");
	node.setUser("ubuntu");
	node.setPrivateKey("ubuntu.pem");
    }

    private void setSsh() throws SshNotConnected, JSchException, SshCommandFailed {
	ssh = mock(SshUtil.class);
	when(ssh.runCommand(anyString())).thenReturn(INSTANCE_ID);
	waiter = mock(SshWaiter.class);
	when(waiter.waitSsh(anyString(), anyString(), anyString(), anyInt())).thenReturn(ssh);
    }

    @Test
    public void shouldRunPrepareScript() throws Exception {
	final String packageUri = "http://choreos.eu/airline.jar";
	final String cookbookTemplateName = "jar";
	final String expectedCommand = ". chef-solo/prepare_deployment.sh " + packageUri + " " + cookbookTemplateName
		+ " no-qos";
	NodePreparer preparer = new NodePreparer(node);
	preparer.sshWaiter = waiter;
	String instanceId = preparer.prepareNodeForDeployment(packageUri, cookbookTemplateName);
	assertEquals(INSTANCE_ID, instanceId);
	verify(ssh).runCommand(expectedCommand);
    }

    @Test
    public void shouldRunUndeploymentPrepareScript() throws Exception {
	final String instanceId = "1111111111-11111111-111111111-11111111";
	final String expectedCommand = ". chef-solo/prepare_undeployment.sh " + instanceId;
	NodePreparer preparer = new NodePreparer(node);
	preparer.sshWaiter = waiter;
	preparer.prepareNodeForUndeployment(instanceId);
	verify(ssh).runCommand(expectedCommand);
    }

}
