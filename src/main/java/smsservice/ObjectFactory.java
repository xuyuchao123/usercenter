
package smsservice;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the smsservice package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _SMSSend_QNAME = new QName("com.shasteel.erp.sms", "SMSSend");
    private final static QName _SMSSendResponse_QNAME = new QName("com.shasteel.erp.sms", "SMSSendResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: smsservice
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SMSSendResponse }
     * 
     */
    public SMSSendResponse createSMSSendResponse() {
        return new SMSSendResponse();
    }

    /**
     * Create an instance of {@link SMSSend }
     * 
     */
    public SMSSend createSMSSend() {
        return new SMSSend();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SMSSend }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "com.shasteel.erp.sms", name = "SMSSend")
    public JAXBElement<SMSSend> createSMSSend(SMSSend value) {
        return new JAXBElement<SMSSend>(_SMSSend_QNAME, SMSSend.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SMSSendResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "com.shasteel.erp.sms", name = "SMSSendResponse")
    public JAXBElement<SMSSendResponse> createSMSSendResponse(SMSSendResponse value) {
        return new JAXBElement<SMSSendResponse>(_SMSSendResponse_QNAME, SMSSendResponse.class, null, value);
    }

}
