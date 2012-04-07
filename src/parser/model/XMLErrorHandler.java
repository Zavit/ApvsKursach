package parser.model;

import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public  class XMLErrorHandler extends DefaultHandler
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