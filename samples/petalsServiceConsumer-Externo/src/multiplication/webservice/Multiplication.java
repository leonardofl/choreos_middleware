
package multiplication.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebService(name = "Multiplication", targetNamespace = "http://webservice.multiplication/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface Multiplication {


    /**
     * 
     * @param term1
     * @param term2
     * @return
     *     returns int
     */
    @WebMethod
    @WebResult(name = "product", targetNamespace = "")
    @RequestWrapper(localName = "operationMultiply", targetNamespace = "http://webservice.multiplication/", className = "multiplication.webservice.OperationMultiply")
    @ResponseWrapper(localName = "operationMultiplyResponse", targetNamespace = "http://webservice.multiplication/", className = "multiplication.webservice.OperationMultiplyResponse")
    public int operationMultiply(
        @WebParam(name = "term1", targetNamespace = "")
        int term1,
        @WebParam(name = "term2", targetNamespace = "")
        int term2);

}