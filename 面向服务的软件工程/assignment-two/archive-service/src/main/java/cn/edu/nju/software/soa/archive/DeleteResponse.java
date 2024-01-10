
package cn.edu.nju.software.soa.archive;

import cn.edu.nju.software.soa.common.enums.StatusCode;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

import java.math.BigInteger;


/**
 * <p>DeleteResponse complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="DeleteResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="code" type="{http://www.w3.org/2001/XMLSchema}integer"/&gt;
 *         &lt;element name="message" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="data" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DeleteResponse", propOrder = {
        "code",
        "message",
        "data"
})
public class DeleteResponse {

    @XmlElement(required = true)
    protected BigInteger code;
    @XmlElement(required = true)
    protected String message;
    protected boolean data;

    public DeleteResponse() {

    }

    public DeleteResponse(StatusCode statusCode, boolean data) {
        this.code = BigInteger.valueOf(statusCode.getCode());
        this.message = statusCode.getMsg();
        this.data = data;
    }

    public DeleteResponse(StatusCode statusCode) {
        this(statusCode, false);
    }

    public DeleteResponse(int code, String message) {
        this.code = BigInteger.valueOf(code);
        this.message = message;
    }

    /**
     * 获取code属性的值。
     *
     * @return possible object is
     * {@link BigInteger }
     */
    public BigInteger getCode() {
        return code;
    }

    /**
     * 设置code属性的值。
     *
     * @param value allowed object is
     *              {@link BigInteger }
     */
    public void setCode(BigInteger value) {
        this.code = value;
    }

    /**
     * 获取message属性的值。
     *
     * @return possible object is
     * {@link String }
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置message属性的值。
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setMessage(String value) {
        this.message = value;
    }

    /**
     * 获取data属性的值。
     */
    public boolean isData() {
        return data;
    }

    /**
     * 设置data属性的值。
     */
    public void setData(boolean value) {
        this.data = value;
    }

}
