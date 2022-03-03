import org.w3c.dom.*;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;


public class Lesson4XML {

    public static void main(String[] args) throws IOException, XPathExpressionException, ParserConfigurationException, SAXException {
        String inputFile = "../JobResult_UCSDExt.xml";
        xpathParser(inputFile);
        domParser(inputFile);
        saxParser(inputFile);
    }


    private static void xpathParser(String inputFile) throws XPathExpressionException, ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(new File(inputFile));
        //
        XPath xPath = XPathFactory.newInstance().newXPath();
        System.out.println("Results of XML Parsing using XPATH Parser:");
        XPathExpression xPathExpression = xPath.compile("/jobresult/serial/text()");
        Object  xPathResult = xPathExpression.evaluate(document, XPathConstants.NODESET);
        NodeList nodeList = (NodeList) xPathResult;

        for (int i = 0; i < nodeList.getLength(); i++) {
            System.out.printf("serial: %s%n", nodeList.item(i).getNodeValue());
        }

        XPathExpression visibleString = xPath.compile("/jobresult/data/visible-string/text()");
        Object  dataResult = visibleString.evaluate(document, XPathConstants.NODESET);
        NodeList dataNodeList = (NodeList) dataResult;

        for (int i = 0; i < dataNodeList.getLength(); i++) {
            System.out.printf("visible-string: %s%n", dataNodeList.item(i).getNodeValue());
        }

        XPathExpression unsignedExpression = xPath.compile("/jobresult/data/structure/unsigned/text()");
        Object  unsignedResult = unsignedExpression.evaluate(document, XPathConstants.NODESET);
        NodeList unsignedNodeList = (NodeList) unsignedResult;

        for (int i = 0; i < unsignedNodeList.getLength(); i++) {
            System.out.printf("unsigned: %s%n", unsignedNodeList.item(i).getNodeValue());
        }
    }

    private static void saxParser(String inputFile) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        saxParserFactory.setNamespaceAware(true);
        SAXParser saxParser = saxParserFactory.newSAXParser();
        System.out.println("Results of XML Parsing using SAX Parser:");

        DefaultHandler handler = new DefaultHandler() {
            StringBuilder elementValue = new StringBuilder();

            public void startElement(String uri, String lName, String qName, Attributes attr) throws SAXException {
                elementValue = new StringBuilder();
            }

            public void endElement(String uri, String localName, String qName) {

                switch (qName) {
                    case "serial":
                        System.out.printf("serial : %s%n", elementValue.toString());
                    case "visible-string":
                        System.out.printf("visible-string : %s%n", elementValue.toString());
                    case "unsigned":
                        System.out.printf("unsigned : %s%n", elementValue.toString());
                }

            }
            public void characters(char[] ch, int start, int length) {
                if (elementValue == null) {
                    elementValue = new StringBuilder();
                } else {
                    elementValue.append(ch, start, length);
                }
            }


        };

        saxParser.parse(new File(inputFile),handler);
    }

    private static void domParser(String inputFile) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(new File(inputFile));
        //
        System.out.println("Results of XML Parsing using DOM Parser:");
        Element elementRoot = document.getDocumentElement();
        NodeList children = elementRoot.getChildNodes();
        for (int i = 0; i < children.getLength(); i++){
            Node child = children.item(i);
            if (child instanceof Element){
                Element childElement = (Element) child;
                if (childElement.getTagName().equals("serial")){
                    Text textNode = (Text) childElement.getFirstChild();
                    System.out.printf("serial: %s%n", textNode.getData().trim());
                }
            }
        }

        NodeList nList = document.getElementsByTagName("data");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            Element eElement = (Element) nNode;
            NodeList visibleStringeNodeList = eElement.getElementsByTagName("visible-string");

            if (visibleStringeNodeList.getLength()> 0) {
                System.out.printf("visible-string : %s%n", eElement.getElementsByTagName("visible-string").item(0).getTextContent());
            }

            NodeList structureNodeList = eElement.getElementsByTagName("structure");
            if (structureNodeList.getLength() > 0) {
                Node structureNode = nList.item(temp);
                Element structurElement = (Element) structureNode;
                System.out.printf("unsigned : %s%n", structurElement.getElementsByTagName("unsigned").item(0).getTextContent());
            }
        }
    }
}