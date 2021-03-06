/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.ow2.choreos.nodes.client;

import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.ow2.choreos.nodes.NodeNotCreatedException;
import org.ow2.choreos.nodes.NodeNotDestroyed;
import org.ow2.choreos.nodes.NodeNotFoundException;
import org.ow2.choreos.nodes.NodePoolManager;
import org.ow2.choreos.nodes.datamodel.CloudNode;
import org.ow2.choreos.nodes.datamodel.NodeSpec;

/**
 * Access Node Pool Manager functionalities through the REST API.
 * 
 * The user of NPMClient does not need to worry with the REST communication.
 * 
 * @author leonardo
 * 
 */
public class NodesClient implements NodePoolManager {

    private String host;
    private String cloudAccount;

    /**
     * 
     * @param host
     *            ex: 'http://localhost:9100/enactmentengine'
     * @param cloudAccount
     *            ex: MY_AWS_ACCOUNT
     * 
     */
    public NodesClient(String host, String cloudAccount) {
        this.host = host;
        this.cloudAccount = cloudAccount;
    }

    private WebClient setupClient() {

        WebClient client = WebClient.create(host);

        // remove time out
        // not proud of it!
        HTTPConduit http = (HTTPConduit) WebClient.getConfig(client).getConduit();
        HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
        httpClientPolicy.setConnectionTimeout(0);// indefined
        httpClientPolicy.setReceiveTimeout(0);// indefined
        http.setClient(httpClientPolicy);

        return client;
    }

    @Override
    public CloudNode createNode(NodeSpec nodeSpec) throws NodeNotCreatedException {

        WebClient client = setupClient();
        client.path(cloudAccount).path("nodes");
        client.type(MediaType.APPLICATION_XML);
        CloudNode node = null;

        try {
            node = client.post(nodeSpec, CloudNode.class);
        } catch (WebApplicationException e) {
            throw new NodeNotCreatedException();
        }

        return node;
    }

    @Override
    public CloudNode getNode(String nodeId) throws NodeNotFoundException {

        WebClient client = setupClient();
        client.path(cloudAccount).path("nodes").path(nodeId);
        CloudNode node = null;

        try {
            node = client.get(CloudNode.class);
        } catch (WebApplicationException e) {
            throw new NodeNotFoundException(nodeId);
        }

        return node;
    }

    @Override
    public List<CloudNode> getNodes() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void destroyNode(String nodeId) throws NodeNotDestroyed, NodeNotFoundException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void destroyNodes() throws NodeNotDestroyed {
        WebClient client = setupClient();
        client.path(cloudAccount).path("nodes");
        Response response = client.delete();
        if (response.getStatus() != 200) {
            throw new NodeNotDestroyed("?");
        }
    }

}
