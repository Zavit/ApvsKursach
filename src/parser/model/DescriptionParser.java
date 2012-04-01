package parser.model;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DescriptionParser
{

    public static Model parse(String filename) throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder builder = factory.newDocumentBuilder();

        //       SchemaFactory schemaFactory = SchemaFactory.newInstance("gfg");
        //      Schema schema = schemaFactory.newSchema();
        //      Validator validator = schema.newValidator();
        //      validator.
        Document description = builder.parse(new FileInputStream(new File(filename)));
        List<Element> listElements = formListElements(description);
        List<ExternalContact> listExtContacts = formListExternalContact(description);
        Chain chain = formMapChains(description);
        return new Model(listElements, listExtContacts, chain);
    }

    private static List<Element> formListElements(Document document)
    {
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
        return listElement;
    }

    private static List<ExternalContact> formListExternalContact(Document document)
    {
        NodeList contacts = document.getElementsByTagName("external_contact");
        List<ExternalContact> listContacts = new LinkedList<ExternalContact>();
        for (int i = 0; i < contacts.getLength(); i++)
        {
            Node contact = contacts.item(i);
            NamedNodeMap map = contact.getAttributes();
            String type = map.getNamedItem("type").getTextContent();
            int chain = Integer.parseInt(map.getNamedItem("chain").getTextContent());
            int numberExtContact = Integer.parseInt(map.getNamedItem("numberContact").getTextContent());
            listContacts.add(new ExternalContact(type, chain, numberExtContact));
        }
        return listContacts;
    }

    private static Chain formMapChains(Document document)
    {
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
        return chainObj;
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
    private static class Element
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
    private static class Contact
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
    private static class ExternalContact extends Contact
    {
        private int chainNumber;

        private ExternalContact(String type, int numberContact, int chainNumber)
        {
            super(type, numberContact);
            this.chainNumber = chainNumber;
        }

        public int getChainNumber()
        {
            return chainNumber;
        }

        @Override
        public String toString()
        {
            return super.toString() + ",[chainNumber = " + chainNumber + "]";
        }
    }
    private static class ElementContact extends Contact
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
    private static class Chain
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
}
