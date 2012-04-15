package parser.model;

import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLErrorHandler extends DefaultHandler
{
    private SAXParseException saxParseException = null;

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
    @Override
    public String toString()
    {
      return saxParseException.toString()+" Column - "+saxParseException.getColumnNumber()+" Line "+saxParseException.getLineNumber(); 
    }

    public static class ErrorHandler
    {
        private String error;

        public ErrorHandler(String error)
        {

            this.error = error;
        }

        @Override
        public String toString()
        {
            return error;
        }
    }
}