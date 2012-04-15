package parser.model;

import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import parser.model.Model.Chain;
import parser.model.Model.Element;
import parser.model.Model.ElementContact;
import parser.model.Model.ExternalContact;
import parser.model.Model.Input;

import constants.Paths;
import constants.XMLProperties;

public class DescriptionParser
{
    private DomParserHelper helper = new DomParserHelper();

    public DescriptionParser()
    {

    }

    public Model parse() throws Exception
    {   
        Document description = helper.parse(Paths.XML_MODEL_PATH, Paths.XML_MODEL_SCHEMA_PATH);
        return (description == null) ? null : formModel(description);
    }

    public DomParserHelper getHelper()
    {
        return helper;
    }

    private Model formModel(Document document)
    {
        //elements
        NodeList elements = document.getElementsByTagName(XMLProperties.ELEMENT);
        List<Element> listElement = new LinkedList<Element>();
        for (int i = 0; i < elements.getLength(); i++)
        {
            Node element = elements.item(i);
            NamedNodeMap map = element.getAttributes();
            String function = map.getNamedItem(XMLProperties.FUNCTION).getTextContent();
            int infoInput = Integer.parseInt(map.getNamedItem(XMLProperties.INFO_INPUT).getTextContent());
            int addressInput = Integer.parseInt(map.getNamedItem(XMLProperties.ADDRESS_INPUT).getTextContent());
            int controlInput = Integer.parseInt(map.getNamedItem(XMLProperties.CONTROL_INPUT).getTextContent());
            int output = Integer.parseInt(map.getNamedItem(XMLProperties.OUTPUT).getTextContent());
            int delay = Integer.parseInt(map.getNamedItem(XMLProperties.DELAY).getTextContent());
            listElement.add(new Element(function, new Input(infoInput, addressInput, controlInput), output, delay));
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
}
