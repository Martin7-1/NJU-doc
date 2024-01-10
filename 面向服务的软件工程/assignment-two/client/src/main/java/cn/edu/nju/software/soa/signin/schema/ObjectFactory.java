
package cn.edu.nju.software.soa.signin.schema;

import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the cn.edu.nju.software.soa.signin.schema package. 
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


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: cn.edu.nju.software.soa.signin.schema
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SigninResponse }
     * 
     */
    public SigninResponse createSigninResponse() {
        return new SigninResponse();
    }

    /**
     * Create an instance of {@link SigninRequest }
     * 
     */
    public SigninRequest createSigninRequest() {
        return new SigninRequest();
    }

}
