
package cn.edu.nju.software.soa.archive;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import cn.edu.nju.software.soa.archive.schema.AddRequest;
import cn.edu.nju.software.soa.archive.schema.AddResponse;
import cn.edu.nju.software.soa.archive.schema.DeleteRequest;
import cn.edu.nju.software.soa.archive.schema.DeleteResponse;
import cn.edu.nju.software.soa.archive.schema.QueryRequest;
import cn.edu.nju.software.soa.archive.schema.QueryResponse;
import cn.edu.nju.software.soa.archive.schema.UpdateRequest;
import cn.edu.nju.software.soa.archive.schema.UpdateResponse;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the cn.edu.nju.software.soa.archive package. 
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

    private final static QName _Update_QNAME = new QName("http://archive.soa.software.nju.edu.cn/", "update");
    private final static QName _UpdateResponse_QNAME = new QName("http://archive.soa.software.nju.edu.cn/", "updateResponse");
    private final static QName _Delete_QNAME = new QName("http://archive.soa.software.nju.edu.cn/", "delete");
    private final static QName _Add_QNAME = new QName("http://archive.soa.software.nju.edu.cn/", "add");
    private final static QName _Query_QNAME = new QName("http://archive.soa.software.nju.edu.cn/", "query");
    private final static QName _AddResponse_QNAME = new QName("http://archive.soa.software.nju.edu.cn/", "addResponse");
    private final static QName _QueryResponse_QNAME = new QName("http://archive.soa.software.nju.edu.cn/", "queryResponse");
    private final static QName _DeleteResponse_QNAME = new QName("http://archive.soa.software.nju.edu.cn/", "deleteResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: cn.edu.nju.software.soa.archive
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://archive.soa.software.nju.edu.cn/", name = "update")
    public JAXBElement<UpdateRequest> createUpdate(UpdateRequest value) {
        return new JAXBElement<UpdateRequest>(_Update_QNAME, UpdateRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://archive.soa.software.nju.edu.cn/", name = "updateResponse")
    public JAXBElement<UpdateResponse> createUpdateResponse(UpdateResponse value) {
        return new JAXBElement<UpdateResponse>(_UpdateResponse_QNAME, UpdateResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://archive.soa.software.nju.edu.cn/", name = "delete")
    public JAXBElement<DeleteRequest> createDelete(DeleteRequest value) {
        return new JAXBElement<DeleteRequest>(_Delete_QNAME, DeleteRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://archive.soa.software.nju.edu.cn/", name = "add")
    public JAXBElement<AddRequest> createAdd(AddRequest value) {
        return new JAXBElement<AddRequest>(_Add_QNAME, AddRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://archive.soa.software.nju.edu.cn/", name = "query")
    public JAXBElement<QueryRequest> createQuery(QueryRequest value) {
        return new JAXBElement<QueryRequest>(_Query_QNAME, QueryRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://archive.soa.software.nju.edu.cn/", name = "addResponse")
    public JAXBElement<AddResponse> createAddResponse(AddResponse value) {
        return new JAXBElement<AddResponse>(_AddResponse_QNAME, AddResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://archive.soa.software.nju.edu.cn/", name = "queryResponse")
    public JAXBElement<QueryResponse> createQueryResponse(QueryResponse value) {
        return new JAXBElement<QueryResponse>(_QueryResponse_QNAME, QueryResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://archive.soa.software.nju.edu.cn/", name = "deleteResponse")
    public JAXBElement<DeleteResponse> createDeleteResponse(DeleteResponse value) {
        return new JAXBElement<DeleteResponse>(_DeleteResponse_QNAME, DeleteResponse.class, null, value);
    }

}
