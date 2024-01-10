package cn.edu.nju.software.service.impl;

import cn.edu.nju.software.entity.StudentInfo;
import cn.edu.nju.software.service.ArchiveManagementService;
import cn.edu.nju.software.utils.ValidationUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * 档案管理服务
 */
public class ArchiveManagementServiceImpl implements ArchiveManagementService {

    private static final String RESOURCES_PATH = "/archive-management/";
    private static final String XML_FILE = RESOURCES_PATH + "StudentInfo.xml";
    private static final String SCHEMA_FILE = RESOURCES_PATH + "StudentInfo.xsd";

    @Override
    public List<StudentInfo> getStudentInfos() {
        String documentPath = "src/main/resources" + XML_FILE;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(documentPath);
            return readContentFromDocument(document);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        // 读取实例并验证
        System.out.println(ValidationUtils.validate(SCHEMA_FILE, XML_FILE));
    }

    private List<StudentInfo> readContentFromDocument(Document document) {
        List<StudentInfo> contentList = new ArrayList<>();
        NodeList nodelist = document.getElementsByTagName("am:studentInfo");
        for (int i = 0; i < nodelist.getLength(); i++) {
            Node node = nodelist.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                StudentInfo studentInfo = new StudentInfo();
                studentInfo.setId(element.getElementsByTagName("id").item(0).getTextContent());
                studentInfo.setName(element.getElementsByTagName("name").item(0).getTextContent());
                studentInfo.setAge(Integer.parseInt(element.getElementsByTagName("age").item(0).getTextContent()));
                studentInfo.setMajor(element.getElementsByTagName("major").item(0).getTextContent());
                studentInfo.setGender(element.getElementsByTagName("gender").item(0).getTextContent());
                studentInfo.setEnrollYear(Integer.parseInt(element.getElementsByTagName("enrollYear").item(0).getTextContent()));
                contentList.add(studentInfo);
            }
        }

        return contentList;
    }
}
