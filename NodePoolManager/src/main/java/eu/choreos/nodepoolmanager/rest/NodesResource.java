package eu.choreos.nodepoolmanager.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;

import eu.choreos.nodepoolmanager.Controller;
import eu.choreos.nodepoolmanager.NodeNotFoundException;
import eu.choreos.nodepoolmanager.cloudprovider.AWSCloudProvider;
import eu.choreos.nodepoolmanager.cloudprovider.CloudProvider;
import eu.choreos.nodepoolmanager.datamodel.Node;
import eu.choreos.nodepoolmanager.datamodel.NodeRestRepresentation;


@Path("nodes")
public class NodesResource {

	private Logger logger = Logger.getLogger(NodesResource.class);
    private Controller controller = new Controller(new AWSCloudProvider());
    
    @GET
    public List<NodeRestRepresentation> getNodes() {

    	logger.debug("Request to get nodes");
    	
    	List<NodeRestRepresentation> restNodeList = new ArrayList<NodeRestRepresentation>();
    	for (Node node: controller.getNodes()){
    		restNodeList.add(node.getRestRepresentation());
    	}
        return restNodeList;
    }

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    public Response createNode(NodeRestRepresentation node, @Context UriInfo uriInfo) throws URISyntaxException {

    	logger.debug("Request to create node");
    	
    	Node newNode = new Node();
    	controller.createNode(newNode);
    	
    	logger.info(newNode + " created");
		
    	UriBuilder uriBuilder = uriInfo.getBaseUriBuilder();
    	// TODO should be getId instead nodeIp
    	// and the node should be in the body
		uriBuilder = uriBuilder.path(NodesResource.class).path(newNode.getIp());
		URI uri = uriBuilder.build();
    	return Response.created(uri).build();
    }
    
    @GET
    @Path("{node_id:.+}")
    public Response getNode(@PathParam("node_id") String id) {
        
    	logger.debug("Request to get nodes");
    	
    	Response response;
        CloudProvider infrastructure = new AWSCloudProvider();

        try {
            NodeRestRepresentation node = infrastructure.getNode(id).getRestRepresentation();
            response = Response.ok(node).build();
        } catch (NodeNotFoundException e) {
            response = Response.status(Status.NOT_FOUND).build();
        }

        return response;
    }

    @DELETE
    @Path("{node_id:.+}")
    public Response deleteNode(@PathParam("node_id") String id) {
    	
    	logger.debug("Request to delete node");
    	
    	CloudProvider infrastructure = new AWSCloudProvider();
        infrastructure.destroyNode(id);

        logger.info("Node " + id + " deleted");
        
        return Response.status(Status.OK).build();
    }
    
    /**
     * Runs chef-client on nodes.
     * Concurrency problems with "knife add run_list".
     * @return
     */
    @POST
    @Path("upgrade")
    public Response upgradeNodes() {

    	logger.debug("Request to upgrade");
    	
    	Response response;
        try {
            controller.upgradeNodes();
            response = Response.status(Status.OK).build();
        } catch (Exception e) {
            response = Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }

        logger.info("Nodes upgraded");
        
        return response;
    }
   
}