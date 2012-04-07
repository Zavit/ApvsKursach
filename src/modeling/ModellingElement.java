package modeling;

import java.io.File;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import parser.model.DomParserHelper;
import parser.model.Model.Element;
import parser.model.Model.Input;
import parser.model.XMLErrorHandler;
import constants.Paths;
import constants.XMLLibProperties;

public class ModellingElement
{
    private Element element;

    public ModellingElement(Element element)
    {
        this.element = element;
    }

    public int runModellingOfElement(String libXml,
                                     String libXmlSchema)
    {
        int codedsignal = element.getCodedSignal();
        Document document = DomParserHelper.parse(libXml, libXmlSchema);
        if(document == null)
        {
            return -1;
        }
        try
        {
            NodeList nodes = document.getElementsByTagName(element.getFunction());
            for (int i = 0; i < nodes.getLength(); i++)
            {
                Node node = nodes.item(i);
                NamedNodeMap attr = node.getAttributes();

                if (element.getInput().equals(getInputByAttr(attr)))
                {
                    NodeList signals = node.getChildNodes();
                    for (int j = 0; j < signals.getLength(); j++)
                    {
                        Node signal = signals.item(j);
                        if (signal.getNodeType() == Node.ELEMENT_NODE && signal.hasAttributes())
                        {
                            NamedNodeMap entry = signal.getAttributes();
                            int code = Integer.parseInt(entry.getNamedItem(XMLLibProperties.KEY).getTextContent());
                            if (code == codedsignal)
                            {
                                return Integer.parseInt(entry.getNamedItem(XMLLibProperties.VALUE).getTextContent());
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -2;
        }

        return -1;
    }

    public Input getInputByAttr(NamedNodeMap attr) throws Exception
    {
        int infoInput = Integer.parseInt(attr.getNamedItem(XMLLibProperties.INFO_INPUT).getTextContent());
        int addressInput = Integer.parseInt(attr.getNamedItem(XMLLibProperties.ADDRESS_INPUT).getTextContent());
        int controlInput = Integer.parseInt(attr.getNamedItem(XMLLibProperties.CONTROL_INPUT).getTextContent());
        return new Input(infoInput, addressInput, controlInput);
    }

}
