package org.ow2.choreos.experiments.travelagency.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

/**
 * This class was generated by the JAX-WS RI. JAX-WS RI 2.1.6 Generated source
 * version: 2.1
 * 
 */
@WebServiceClient(name = "TravelAgencyServiceService", targetNamespace = "http://choreos.ow2.org/", wsdlLocation = TravelAgencyServiceService.TRAVELAGENCY_WSDL)
public class TravelAgencyServiceService extends Service {

    static final String TRAVELAGENCY_WSDL = "http://10.0.0.8:8080/11d96c72-fcb6-4bbc-92a4-28c66d14ae08/travelagency?wsdl";
    private final static Logger logger = Logger.getLogger(TravelAgencyServiceService.class.getName());

    static {
	URL url = null;
	try {
	    URL baseUrl;
	    baseUrl = TravelAgencyServiceService.class.getResource(".");
	    url = new URL(baseUrl, TRAVELAGENCY_WSDL);
	} catch (MalformedURLException e) {
	    logger.warning("Failed to create URL for the wsdl Location: 'http://10.0.0.8:8080/11d96c72-fcb6-4bbc-92a4-28c66d14ae08/travelagency?wsdl', retrying as a local file");
	    logger.warning(e.getMessage());
	}
    }

    public TravelAgencyServiceService(URL wsdlLocation, QName serviceName) {
	super(wsdlLocation, serviceName);
    }

    /**
     * 
     * @return returns TravelAgencyService
     */
    @WebEndpoint(name = "TravelAgencyServicePort")
    public TravelAgencyService getTravelAgencyServicePort() {
	return super
		.getPort(new QName("http://choreos.ow2.org/", "TravelAgencyServicePort"), TravelAgencyService.class);
    }

    /**
     * 
     * @param features
     *            A list of {@link javax.xml.ws.WebServiceFeature} to configure
     *            on the proxy. Supported features not in the
     *            <code>features</code> parameter will have their default
     *            values.
     * @return returns TravelAgencyService
     */
    @WebEndpoint(name = "TravelAgencyServicePort")
    public TravelAgencyService getTravelAgencyServicePort(WebServiceFeature... features) {
	return super.getPort(new QName("http://choreos.ow2.org/", "TravelAgencyServicePort"),
		TravelAgencyService.class, features);
    }

}
