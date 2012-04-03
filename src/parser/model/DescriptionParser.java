package parser.model;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

public class DescriptionParser
{

    public static Model parse(String xmlFile, String xmlSchema) throws Exception
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
        NodeList elements = document.getElementsByTagName("element");
        List<Element> listElement = new LinkedList<Element>();
        for (int i = 0; i < elements.getLength(); i++)
        {
            Node element = elements.item(i);
            NamedNodeMap map = element.getAttributes();
            String type = map.getNamedItem("type").getTextContent();
            String function = map.getNamedItem("function").getTextContent();
            int infoInput = Integer.parseInt(map.getNamedItem("infoInput").getTextContent());
            int addressInput = Integer.parseInt(map.getNamedItem("addressInput").getTextContent());
            int controlInput = Integer.parseInt(map.getNamedItem("controlInput").getTextContent());
            listElement.add(new Element(function, type, infoInput, addressInput, controlInput));
        }
        //external
        NodeList ext_contacts = document.getElementsByTagName("external_contact");
        List<ExternalContact> listContacts = new LinkedList<ExternalContact>();
        for (int i = 0; i < ext_contacts.getLength(); i++)
        {
            Node contact = ext_contacts.item(i);
            NamedNodeMap map = contact.getAttributes();
            String type = map.getNamedItem("type").getTextContent();
            int chain = Integer.parseInt(map.getNamedItem("chain").getTextContent());
            int value = map.getNamedItem("value") != null ? Integer.parseInt(map.getNamedItem("value").getTextContent()) : -1;
            int numberExtContact = Integer.parseInt(map.getNamedItem("numberContact").getTextContent());
            listContacts.add(new ExternalContact(type, chain, numberExtContact, value));
        }
        //chains
        NodeList chains = document.getElementsByTagName("chain");
        Chain chainObj = new Chain();
        for (int i = 0; i < chains.getLength(); i++)
        {
            List<ElementContact> contactList = new LinkedList<ElementContact>();
            Node chain = chains.item(i);
            NamedNodeMap mapNumber = chain.getAttributes();
            int chainNumber = Integer.parseInt(mapNumber.getNamedItem("number").getTextContent());
            NodeList contacts = chain.getChildNodes();
            for (int j = 0; j < contacts.getLength(); j++)
            {
                Node contact = contacts.item(j);
                if (contact.getNodeType() == Node.ELEMENT_NODE)
                {
                    NamedNodeMap map = contact.getAttributes();
                    String type = map.getNamedItem("type").getTextContent();
                    int numberElement = Integer.parseInt(map.getNamedItem("numberElement").getTextContent());
                    int numberContact = Integer.parseInt(map.getNamedItem("numberContact").getTextContent());
                    contactList.add(new ElementContact(type, numberContact, numberElement));
                }
            }
            chainObj.registerChain(chainNumber, contactList);
        }
        return new Model(listElement,listContacts,chainObj);
    }
    public static class Model
    {
        private List<Element>         listElements;
        private List<ExternalContact> listExtContacts;
        private Chain                 chain;

        private Model(List<Element> listElements, List<ExternalContact> listExtContacts, Chain chain)
        {
            this.listElements = listElements;
            this.listExtContacts = listExtContacts;
            this.chain = chain;
        }

        public Chain getChain()
        {
            return chain;
        }

        public List<Element> getListElements()
        {
            return listElements;
        }

        public List<ExternalContact> getListExtContacts()
        {
            return listExtContacts;
        }

        @Override
        public String toString()
        {
            StringBuilder builder = new StringBuilder();
            builder.append(getClass().getName()).append("\n");
            for (Element element : listElements)
            {
                builder.append(element).append("\n");
            }
            for (ExternalContact extContact : listExtContacts)
            {
                builder.append(extContact).append("\n");
            }
            return builder.append(chain).toString();
        }
    }
    public static class Element
    {
        private String function;
        private String type;
        private int    infoInput;
        private int    addressInput;
        private int    controlInput;

        private Element(String function, String type, int infoInput, int addressInput, int controlInput)
        {
            this.function = function;
            this.type = type;
            this.infoInput = infoInput;
            this.addressInput = addressInput;
            this.controlInput = controlInput;
        }

        public String getFunction()
        {
            return function;
        }

        public String getType()
        {
            return type;
        }

        public int getInfoInput()
        {
            return infoInput;
        }

        public int getAddressInput()
        {
            return addressInput;
        }

        public int getControlInput()
        {
            return controlInput;
        }

        public int getGeneralInput()
        {
            return (infoInput + addressInput + controlInput);
        }

        @Override
        public String toString()
        {
            return getClass().getName() + "[function=" + function + ", type=" + type + ", infoInput=" + infoInput + ", addressInput=" + addressInput + ", controlInput=" + controlInput + "]";
        }
    }
    public static class Contact
    {
        private String type;
        private int    numberContact;

        private Contact(String type, int numberContact)
        {
            this.type = type;
            this.numberContact = numberContact;
        }

        public String getType()
        {
            return type;
        }

        public int getNumberContact()
        {
            return numberContact;
        }

        @Override
        public String toString()
        {
            return getClass().getName() + "[type=" + type + "], [numberContact=" + numberContact + "]";
        }

    }
    public static class ExternalContact extends Contact
    {
        private int chainNumber;
        private int value;

        private ExternalContact(String type, int numberContact, int chainNumber, int value)
        {
            super(type, numberContact);
            this.chainNumber = chainNumber;
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }

        public int getChainNumber()
        {
            return chainNumber;
        }

        @Override
        public String toString()
        {
            return super.toString() + ",[chainNumber = " + chainNumber + "]"+",[value = " + value + "]";
        }
    }
    public static class ElementContact extends Contact
    {
        private int numberElement;

        private ElementContact(String type, int numberContact, int numberElement)
        {
            super(type, numberContact);
            this.numberElement = numberElement;
        }

        public int getElementNumber()
        {
            return numberElement;
        }

        public String toString()
        {
            return super.toString() + ",[numberElement =" + numberElement + "]";
        }
    }
    public static class Chain
    {
        private Map<Integer, List<ElementContact>> mapChains = new HashMap<Integer, List<ElementContact>>();

        private Chain()
        {
        }

        public void registerChain(Integer number,
                                  List<ElementContact> contact)
        {
            mapChains.put(number, contact);
        }

        public int getChainsCount()
        {
            return mapChains.size();
        }

        public List<ElementContact> getListContacts(int chain)
        {
            return Collections.unmodifiableList(mapChains.get(chain));
        }

        @Override
        public String toString()
        {
            StringBuilder builder = new StringBuilder();
            builder.append(getClass().getName()).append("\n");
            for (Entry<Integer, List<ElementContact>> entry : mapChains.entrySet())
            {
                builder.append(entry.getKey()).append("=").append(entry.getValue()).append("\n");
            }
            return builder.toString();
        }
    }
    private static class XMLErrorHandler extends DefaultHandler
    {
        private boolean           hasValidationError = false;
        private boolean           hasFattalError     = false;
        private SAXParseException saxParseException  = null;

        public void error(SAXParseException exception)
        {
            hasValidationError = true;
            saxParseException = exception;
        }

        public void fatalError(SAXParseException exception)
        {
            hasFattalError = true;
            saxParseException = exception;
        }

        public SAXParseException getSaxParseException()
        {
            return saxParseException;
        }
    }

}
