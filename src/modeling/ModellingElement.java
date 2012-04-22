package modeling;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import parser.model.DomParserHelper;
import parser.model.Model.Element;
import parser.model.Model.Input;

import constants.Function;
import constants.Paths;
import constants.XMLLibProperties;

public class ModellingElement
{
    private Element            element;
    private DomParserHelper    helper           = new DomParserHelper();
    private LogicError         logicErr         = null;
    private Map<String, State> mapTriggersState = new HashMap<String, State>();

    public ModellingElement(Element element)
    {
        this.element = element;
    }
    public void setElement(Element element)
    {
        this.element = element;
    }

    public int runModellingOfElement()
    {
        int codedsignal = element.getCodedSignal();

        Document document = helper.parse(Paths.XML_LIBRARY_PATH, Paths.XML_LIBRARY_SCHEMA_PATH);
        if (document == null)
        {
            return -1;
        }
        try
        {
            String function = element.getFunction();
            NodeList nodes = document.getElementsByTagName(function);
            for (int i = 0; i < nodes.getLength(); i++)
            {
                Node node = nodes.item(i);
                NamedNodeMap attr = node.getAttributes();

                if (element.getInput().equals(getInputByAttr(attr)) && element.getOutput() == Integer.parseInt(attr.getNamedItem(XMLLibProperties.OUTPUT).getTextContent()))
                {
                    if (function.equals(Function.CONJUNCTION) || function.equals(Function.CONJUNCTION_INVERS) || function.equals(Function.DISJUNCTION) || function.equals(Function.DISJUNCTION_INVERS))
                    {
                        return new Logic(element.getInput(), function, element.getCodedSignal()).getOutputSignal();
                    }
                    if (function.indexOf(Function.TRIGGER) != -1)
                    {
                        return new Trigger(function, element.getCodedSignal(), node.getChildNodes()).getOutputSignal();
                    }
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
        }
        logicErr = new LogicError(element);
        return -2;
    }

    public LogicError getLogicErr()
    {
        return logicErr;
    }

    public Input getInputByAttr(NamedNodeMap attr) throws Exception
    {
        int infoInput = Integer.parseInt(attr.getNamedItem(XMLLibProperties.INFO_INPUT).getTextContent());
        int addressInput = Integer.parseInt(attr.getNamedItem(XMLLibProperties.ADDRESS_INPUT).getTextContent());
        int controlInput = Integer.parseInt(attr.getNamedItem(XMLLibProperties.CONTROL_INPUT).getTextContent());
        return new Input(infoInput, addressInput, controlInput);
    }

    public DomParserHelper getHelper()
    {
        return helper;
    }

    private static class Logic
    {
        private Input  input;
        private String function;
        private int    inputSignal;

        private Logic(Input input, String function, int inputSignal)
        {
            this.input = input;
            this.function = function;
            this.inputSignal = inputSignal;
        }

        private int getOutputSignal()
        {
            if (function.equals(Function.CONJUNCTION) || function.equals(Function.CONJUNCTION_INVERS))
            {
                for (int mask = 3, i = 0; i < input.getInfoInput(); mask <<= 2, i++)
                {
                    if ((mask & inputSignal) == 0)
                    {
                        return function.equals(Function.CONJUNCTION) ? 0 : 1;
                    }
                }
                for (int mask = 2, i = 0; i < input.getInfoInput(); mask <<= 2, i++)
                {
                    if ((mask & inputSignal) != 0)
                    {
                        return 2;
                    }
                }
                return function.equals(Function.CONJUNCTION) ? 1 : 0;
            }
            else if (function.equals(Function.DISJUNCTION) || function.equals(Function.DISJUNCTION_INVERS))
            {
                for (int mask = 1, i = 0; i < input.getInfoInput(); mask <<= 2, i++)
                {

                    if ((mask & inputSignal) != 0)
                    {
                        return function.equals(Function.DISJUNCTION) ? 1 : 0;
                    }
                }
                for (int mask = 2, i = 0; i < input.getInfoInput(); mask <<= 2, i++)
                {
                    if ((mask & inputSignal) != 0)
                    {
                        return 2;
                    }
                }
                return function.equals(Function.DISJUNCTION) ? 0 : 1;
            }
            return -1;
        }

    }
    private class Trigger
    {

        private String   function;
        private int      inputSignal;
        private NodeList nodes;

        private Trigger(String function, int inputSignal, NodeList nodes)
        {
            this.function = function;
            this.inputSignal = inputSignal;
            this.nodes = nodes;
        }

        private int getOutputSignal()
        {
            State state = mapTriggersState.get(function);
            if (state == null)
            {
                for (int i = 0; i < nodes.getLength(); i++)
                {
                    Node entry = nodes.item(i);
                    if (entry.getNodeType() == Node.ELEMENT_NODE && entry.hasAttributes())
                    {
                        NamedNodeMap entryAttr = entry.getAttributes();
                        int code = Integer.parseInt(entryAttr.getNamedItem(XMLLibProperties.KEY).getTextContent());
                        int currentState = 2;

                        if (code == inputSignal && Integer.parseInt(entryAttr.getNamedItem(XMLLibProperties.CURRENT_STATE).getTextContent()) == currentState)

                        {
                            mapTriggersState.put(function, new State(Integer.parseInt(entryAttr.getNamedItem(XMLLibProperties.NEXT_STATE).getTextContent())));
                            return Integer.parseInt(entryAttr.getNamedItem(XMLLibProperties.VALUE).getTextContent());//закодированные выходы!!!
                        }
                    }
                }

            }
            else
            {
                int currentState = state.getNextState();
                for (int i = 0; i < nodes.getLength(); i++)
                {
                    Node entry = nodes.item(i);
                    if (entry.getNodeType() == Node.ELEMENT_NODE && entry.hasAttributes())
                    {
                        NamedNodeMap entryAttr = entry.getAttributes();
                        int code = Integer.parseInt(entryAttr.getNamedItem(XMLLibProperties.KEY).getTextContent());

                        if (code == inputSignal && Integer.parseInt(entryAttr.getNamedItem(XMLLibProperties.CURRENT_STATE).getTextContent()) == currentState)
                        {
                            mapTriggersState.put(function, new State(Integer.parseInt(entryAttr.getNamedItem(XMLLibProperties.NEXT_STATE).getTextContent())));
                            return Integer.parseInt(entryAttr.getNamedItem(XMLLibProperties.VALUE).getTextContent());//закодированные выходы!!!
                        }
                    }
                }
            }
            return -1;
        }
    }
    public static class LogicError
    {
        private Element element;

        private LogicError(Element element)
        {
            this.element = element;
        }

        @Override
        public String toString()
        {
            return element == null ? "NOTHING" : "The element " + element + " wasn't found in library!!";
        }
    }
    private static class State
    {
        private int nextState;

        private State(int nextState)
        {
            this.nextState = nextState;
        }

        private int getNextState()
        {
            return nextState;
        }
    }

}
