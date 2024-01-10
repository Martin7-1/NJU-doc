package cn.edu.nju.software.handler;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayDeque;
import java.util.Deque;

public class FailedGradeHandler extends DefaultHandler {

    private static final String TOTAL_GRADE = "totalGrade";

    private DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    private TransformerFactory tf = TransformerFactory.newInstance();

    private StringWriter writer = new StringWriter();
    private Deque<String> elementStack = new ArrayDeque<>();
    private boolean isTotalGrade;
    private double currentTotalGrade;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        // 判断当前 element 是否是 totalGrade
        isTotalGrade = TOTAL_GRADE.equals(qName);
        elementStack.push(qName);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        isTotalGrade = false;
        if ("studentGrades".equals(elementStack.peek()) && (currentTotalGrade <= 60)) {
            printElement(qName);
        }
        elementStack.pop();
    }

    private void printElement(String name) throws SAXException {
        try {
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.transform(new DOMSource(dbf.newDocumentBuilder().parse(new InputSource(new StringReader(writer.toString()))).getElementsByTagName(name).item(0)), new StreamResult(System.out));
        } catch (ParserConfigurationException | TransformerException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (isTotalGrade) {
            currentTotalGrade = Double.parseDouble(new String(ch, start, length));
        }
        super.characters(ch, start, length);
        writer.write(ch, start, length);
    }

    public static void main(String[] args) {
        try {
            FailedGradeHandler filter = new FailedGradeHandler();
            SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
            saxParser.parse(FailedGradeHandler.class.getResourceAsStream("/educational-admin/SortedCourseGrades.xml"), filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
