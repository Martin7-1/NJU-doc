package cn.edu.nju.software.utils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.File;

public class DocumentUtils {

    public DocumentUtils() throws InstantiationException {
        throw new InstantiationException("can't instantiate this class");
    }

    public static <T> void toXML(T object, String filePath, Class<T> clazz) {
        try {
            JAXBContext context = JAXBContext.newInstance(clazz);

            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(object, new File(filePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
