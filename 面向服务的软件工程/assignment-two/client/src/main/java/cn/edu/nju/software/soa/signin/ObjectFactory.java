
package cn.edu.nju.software.soa.signin;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import cn.edu.nju.software.soa.signin.schema.SigninRequest;
import cn.edu.nju.software.soa.signin.schema.SigninResponse;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the cn.edu.nju.software.soa.signin package. 
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

    private final static QName _SigninResponse_QNAME = new QName("http://signin.soa.software.nju.edu.cn/", "signinResponse");
    private final static QName _Signin_QNAME = new QName("http://signin.soa.software.nju.edu.cn/", "signin");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: cn.edu.nju.software.soa.signin
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SigninResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://signin.soa.software.nju.edu.cn/", name = "signinResponse")
    public JAXBElement<SigninResponse> createSigninResponse(SigninResponse value) {
        return new JAXBElement<SigninResponse>(_SigninResponse_QNAME, SigninResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SigninRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://signin.soa.software.nju.edu.cn/", name = "signin")
    public JAXBElement<SigninRequest> createSignin(SigninRequest value) {
        return new JAXBElement<SigninRequest>(_Signin_QNAME, SigninRequest.class, null, value);
    }

}
