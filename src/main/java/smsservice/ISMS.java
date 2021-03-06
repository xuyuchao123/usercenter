
package smsservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "ISMS", targetNamespace = "com.shasteel.erp.sms")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface ISMS {


    /**
     * 
     * @param arg1
     * @param arg0
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "SMSSend")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "SMSSend", targetNamespace = "com.shasteel.erp.sms", className = "smsservice.SMSSend")
    @ResponseWrapper(localName = "SMSSendResponse", targetNamespace = "com.shasteel.erp.sms", className = "smsservice.SMSSendResponse")
    public String smsSend(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1);

}
