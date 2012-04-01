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
        Document description = builder.parse(new FileInputStream(new File(filename)));
        List<Element> listElements = formListElements(description);
        List<ExternalContact> listExtContacts = formListExternalContact(description);
        Chain chain = formListChains(description);
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

    private static Chain formListChains(Document document)
    {
        NodeList chains = document.getElementsByTagName("chain");
        Chain chainObj = new Chain();
        for (int i = 0; i < chains.getLength(); i++)
        {
            List<Contact> contactList = new LinkedList<Contact>();
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
                    contactList.add(new Contact(type, chainNumber, numberContact, numberElement));
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

        @Override
        public String toString()
        {
            return getClass().getName()+"[function=" + function + ", type=" + type + ", infoInput=" + infoInput + ", addressInput=" + addressInput + ", controlInput=" + controlInput + "]";
        }
        
    }
    private static class ExternalContact
    {
        // I-O-E
        private String type;
        private int    chainNumber;
        private int numberContact;

        private ExternalContact(String type, int chainNumber, int numberContact)
        {
            this.type = type;
            this.chainNumber = chainNumber;
            this.numberContact = numberContact;
        }

        public int getNumberContact()
        {
            return numberContact;
        }

        public String getType()
        {
            return type;
        }

        public int getChainNumber()
        {
            return chainNumber;
        }

        @Override
        public String toString()
        {
            return  getClass().getName()+"[type=" + type + ", chainNumber=" + chainNumber + ", numberContact=" + numberContact + "]";
        }

        
    }
    private static class Contact extends ExternalContact
    {
        private int numberElement;

        private Contact(String type, int chainNumber, int numberContact, int numberElement)
        {
            super(type, chainNumber, numberContact);
            this.numberElement = numberElement;
        }

        public int getElementNumber()
        {
            return numberElement;
        }
        public String toString()
        {
            return super.toString()+", numberElement ="+numberElement+"]";
        }
    }
    private static class Chain
    {
        private Map<Integer, List<Contact>> mapChains = new HashMap<Integer, List<Contact>>();

        private Chain()
        {
            
        }
        public void registerChain(Integer number,List<Contact> contact)
        {
          mapChains.put(number, contact);  
        }
        public int getChainsCount()
        {
            return mapChains.size();
        }
        public List<Contact> getListContacts(int chain)
        {
            return Collections.unmodifiableList(mapChains.get(chain));
        }
        @Override
        public String toString()
        {
            StringBuilder builder = new StringBuilder();
            builder.append(getClass().getName()).append("\n");
            for(Entry<Integer, List<Contact>> entry: mapChains.entrySet())
            {
                builder.append(entry.getKey()).append("=").append(entry.getValue()).append("\n");
            }
            return builder.toString();
        }
    }

}
