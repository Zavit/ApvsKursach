package parser.model;

import java.io.File;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;

public class DomParserHelper
{
    public static Document parse(String xmlFile,
                                 String xmlSchema)
    {
        Document document = null;
        try
        {
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(new File(xmlSchema));
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setSchema(schema);
            factory.setNamespaceAware(true);
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

            DocumentBuilder parser = factory.newDocumentBuilder();
            XMLErrorHandler error = new XMLErrorHandler();

            parser.setErrorHandler(error);
            document = parser.parse(new File(xmlFile));
            if (error.getSaxParseException() != null)
            {
                return null;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return document;
    }
}
