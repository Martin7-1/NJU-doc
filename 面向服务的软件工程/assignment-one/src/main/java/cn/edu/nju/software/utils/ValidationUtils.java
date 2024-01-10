package cn.edu.nju.software.utils;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

public class ValidationUtils {

    private ValidationUtils() throws InstantiationException {
        throw new InstantiationException("can't instantiate this class");
    }

    public static boolean validate(String schemaFile, String xmlFile) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(ValidationUtils.class.getResourceAsStream(schemaFile)));

            // 创建Validator并执行validation
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(ValidationUtils.class.getResourceAsStream(xmlFile)));

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
