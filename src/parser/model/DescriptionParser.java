package parser.model;

import java.io.File;

import java.util.LinkedList;
import java.util.List;


import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import parser.model.Model.Chain;
import parser.model.Model.Element;
import parser.model.Model.ElementContact;
import parser.model.Model.ExternalContact;
import parser.model.Model.Input;

import constants.XMLProperties;

public class DescriptionParser
{

    public static Model parse(String xmlFile,
                              String xmlSchema) throws Exception
    {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(new File(xmlSchema));
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setSchema(schema);
        factory.setNamespaceAware(true);
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        DocumentBuilder builder = factory.newDocumentBuilder();

        XMLErrorHandler error = new XMLErrorHandler();

        builder.setErrorHandler(error);
        Document description = builder.parse(new File(xmlFile));

        if (error.getSaxParseException() != null)
        {
            System.out.println(error.getSaxParseException().getMessage() + " line " + error.getSaxParseException().getLineNumber() + " column - " + error.getSaxParseException().getColumnNumber());
            return null;
        }
        //////////////////////////MODEL FORM //////////////////////////
        return formModel(description);
    }

    private static Model formModel(Document document)
    {
        //elements
        NodeList elements = document.getElementsByTagName(XMLProperties.ELEMENT);
        List<Element> listElement = new LinkedList<Element>();
        for (int i = 0; i < elements.getLength(); i++)
        {
            Node element = elements.item(i);
            NamedNodeMap map = element.getAttributes();
            String type = map.getNamedItem(XMLProperties.TYPE).getTextContent();
            String function = map.getNamedItem(XMLProperties.FUNCTION).getTextContent();
            int infoInput = Integer.parseInt(map.getNamedItem(XMLProperties.INFO_INPUT).getTextContent());
            int addressInput = Integer.parseInt(map.getNamedItem(XMLProperties.ADDRESS_INPUT).getTextContent());
            int controlInput = Integer.parseInt(map.getNamedItem(XMLProperties.CONTROL_INPUT).getTextContent());
            int output = Integer.parseInt(map.getNamedItem(XMLProperties.OUTPUT).getTextContent());
            int delay = Integer.parseInt(map.getNamedItem(XMLProperties.DELAY).getTextContent());
            listElement.add(new Element(function, type, new Input(infoInput, addressInput, controlInput),output, delay));
        }
        //external
        NodeList ext_contacts = document.getElementsByTagName(XMLProperties.EXTERNAL_CONTACT);
        List<ExternalContact> listContacts = new LinkedList<ExternalContact>();
        for (int i = 0; i < ext_contacts.getLength(); i++)
        {
            Node contact = ext_contacts.item(i);
            NamedNodeMap map = contact.getAttributes();
            String type = map.getNamedItem(XMLProperties.TYPE).getTextContent();
            int chain = Integer.parseInt(map.getNamedItem(XMLProperties.CHAIN).getTextContent());
            int value = map.getNamedItem(XMLProperties.VALUE) != null ? Integer.parseInt(map.getNamedItem(XMLProperties.VALUE).getTextContent()) : -1;
            int numberExtContact = Integer.parseInt(map.getNamedItem(XMLProperties.NUMBER_CONTACT).getTextContent());
            listContacts.add(new ExternalContact(type, chain, numberExtContact, value));
        }
        //chains
        NodeList chains = document.getElementsByTagName(XMLProperties.CHAIN);
        Chain chainObj = new Chain();
        for (int i = 0; i < chains.getLength(); i++)
        {
            List<ElementContact> contactList = new LinkedList<ElementContact>();
            Node chain = chains.item(i);
            NamedNodeMap mapNumber = chain.getAttributes();
            int chainNumber = Integer.parseInt(mapNumber.getNamedItem(XMLProperties.NUMBER).getTextContent());
            NodeList contacts = chain.getChildNodes();
            for (int j = 0; j < contacts.getLength(); j++)
            {
                Node contact = contacts.item(j);
                if (contact.getNodeType() == Node.ELEMENT_NODE)
                {
                    NamedNodeMap map = contact.getAttributes();
                    String type = map.getNamedItem(XMLProperties.TYPE).getTextContent();
                    int numberElement = Integer.parseInt(map.getNamedItem(XMLProperties.NUMBER_ELEMENT).getTextContent());
                    int numberContact = Integer.parseInt(map.getNamedItem(XMLProperties.NUMBER_CONTACT).getTextContent());
                    contactList.add(new ElementContact(type, numberContact, numberElement));
                }
            }
            chainObj.registerChain(chainNumber, contactList);
        }
        return new Model(listElement, listContacts, chainObj);
    }
    private static class XMLErrorHandler extends DefaultHandler
    {
        private SAXParseException saxParseException  = null;

        public void error(SAXParseException exception)
        {
            saxParseException = exception;
        }

        public void fatalError(SAXParseException exception)
        {
            saxParseException = exception;
        }

        public SAXParseException getSaxParseException()
        {
            return saxParseException;
        }
    }
 


}
