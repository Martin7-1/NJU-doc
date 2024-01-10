
package cn.edu.nju.software.soa.login;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>loginResponse complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="loginResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://login.soa.software.nju.edu.cn/schema/}LoginResponse" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "loginResponse", propOrder = {
    "_return"
})
public class LoginResponse {

    @XmlElement(name = "return")
    protected cn.edu.nju.software.soa.login.schema.LoginResponse _return;

    /**
     * 获取return属性的值。
     * 
     * @return
     *     possible object is
     *     {@link cn.edu.nju.software.soa.login.schema.LoginResponse }
     *     
     */
    public cn.edu.nju.software.soa.login.schema.LoginResponse getReturn() {
        return _return;
    }

    /**
     * 设置return属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link cn.edu.nju.software.soa.login.schema.LoginResponse }
     *     
     */
    public void setReturn(cn.edu.nju.software.soa.login.schema.LoginResponse value) {
        this._return = value;
    }

}
