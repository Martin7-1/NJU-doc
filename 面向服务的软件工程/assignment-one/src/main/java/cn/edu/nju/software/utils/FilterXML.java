package cn.edu.nju.software.utils;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.ArrayDeque;
import java.util.Deque;

public class FilterXML {

    private static final String INPUT_FILE = "input.xml";
    private static final String OUTPUT_FILE = "output.xml";

    public static void main(String[] args) throws Exception {
        File inputFile = new File(INPUT_FILE);
        File outputFile = new File(OUTPUT_FILE);

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();

        ContentFilter contentHandler = new ContentFilter();
        saxParser.parse(inputFile, contentHandler);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8");
        StreamResult result = new StreamResult(writer);

        Source source = new SAXSource(contentHandler.getXMLReader(), contentHandler.getInputSource());
        transformer.transform(source, result);

        System.out.println("Output to file saved!");
    }

    private static class ContentFilter extends DefaultHandler {
        private boolean inTotalGradeTag;
        private int totalGrade;
        private StringBuilder characters = new StringBuilder();
        private Deque<String> elementStack = new ArrayDeque<>();

        public XMLReader getXMLReader() {
            return null;
        }

        public InputSource getInputSource() {
            return new InputSource(new StringReader(this.characters.toString()));
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes)
                throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            this.elementStack.push(qName);
            if ("totalGrade".equals(qName)) {
                this.inTotalGradeTag = true;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            this.elementStack.pop();
            if ("totalGrade".equals(qName)) {
                this.inTotalGradeTag = false;
            }
        }

        @Override
        public void characters(char ch[], int start, int length) throws SAXException {
            super.characters(ch, start, length);
            String string = new String(ch, start, length);
            if (this.inTotalGradeTag) {
                try {
                    this.totalGrade = Integer.parseInt(string);
                    if (this.totalGrade < 60) {
                        // print the entire student record with grades less than 60
                        for (String s : this.elementStack) {
                            startElement("", s, s, null);
                        }
                        characters(ch, start, length);
                        while (!"student".equals(this.elementStack.peek())) {
                            endElement("", this.elementStack.pop(), this.elementStack.pop());
                        }
                        endElement("", "student", "student");
                    }
                } catch (NumberFormatException e) {
                    throw new SAXException(e.getMessage(), e);
                }
            }
            this.characters.append(string);
        }
    }
}
