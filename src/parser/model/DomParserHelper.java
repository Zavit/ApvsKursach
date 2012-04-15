package parser.model;

import java.io.File;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;

import parser.model.XMLErrorHandler.ErrorHandler;

public class DomParserHelper
{
    private XMLErrorHandler errorHandler  = null;
    private ErrorHandler           error = null;

    public Document parse(String xmlFile,
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
            errorHandler = new XMLErrorHandler();

            parser.setErrorHandler(errorHandler);
            document = parser.parse(new File(xmlFile));
            if (errorHandler.getSaxParseException() != null)
            {
                return null;
            }
        }
        catch (Exception e)
        {
            error = new ErrorHandler(e.getMessage());
            return null;
        }
        return document;
    }

    public XMLErrorHandler getErrorHandler()
    {
        return errorHandler;
    }

    public ErrorHandler getErrors()
    {
        return error;
    }
    @Override
    public String toString()
    {
        if(error != null)
        {
            return error.toString();
        }
        else if(errorHandler !=null)
        {
            return errorHandler.toString();
        }
        return "NONE MISTAKES";
            
        
    } 
    
}
