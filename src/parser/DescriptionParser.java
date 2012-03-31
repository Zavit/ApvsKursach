package parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import container.model.Chain;
import container.model.Element;
import container.model.ExternalContact;
import container.model.Model;

public class DescriptionParser
{
    public static void main(String[] args) throws Exception
    {

    }

    public Model parse() throws ParserConfigurationException, FileNotFoundException, SAXException, IOException
    {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder builder = factory.newDocumentBuilder();
        Document description = builder.parse(new FileInputStream(new File("resources/model.xml")));
        List<Element> listElements = formListElements(description);
        List<ExternalContact> listExtContacts = formListExternalContact(description);
        return null;
    }

    private List<Element> formListElements(Document document)
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

    private List<ExternalContact> formListExternalContact(Document document)
    {
        NodeList contacts = document.getElementsByTagName("external_contact");
        List<ExternalContact> listContacts = new LinkedList<ExternalContact>();
        for (int i = 0; i < contacts.getLength(); i++)
        {
            Node contact = contacts.item(i);
            NamedNodeMap map = contact.getAttributes();
            String type = map.getNamedItem("type").getTextContent();
            int chain = Integer.parseInt(map.getNamedItem("chain").getTextContent());
            String name = map.getNamedItem("name").getTextContent();
            listContacts.add(new ExternalContact(type, chain, name));
        }
        return listContacts;
    }

    private Chain formListChains(Document document)
    {
        NodeList chains = document.getElementsByTagName("chain");
        Chain chainObj = new Chain();
        for (int i = 0; i < chains.getLength(); i++)
        {
            Node chain = chains.item(i);

            NamedNodeMap mapNumber = chain.getAttributes();

            int number = Integer.parseInt(mapNumber.getNamedItem("number").getTextContent());

            NodeList contacts = chain.getChildNodes();
            for (int j = 0; j < contacts.getLength(); j++)
            {
                Node contact = contacts.item(j);
                NamedNodeMap map = contact.getAttributes();
                String type = map.getNamedItem("type").getTextContent();
                String numberElement = mapNumber.getNamedItem("numberElement").getTextContent();
                String numberContact = mapNumber.getNamedItem("numberContact").getTextContent();
            }
        }
        return chainObj;
    }
}
