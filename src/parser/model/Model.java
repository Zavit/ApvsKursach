package parser.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Model
{
    private List<Element>         listElements;
    private List<ExternalContact> listExtContacts;
    private Chain                 chain;

    public Model(List<Element> listElements, List<ExternalContact> listExtContacts, Chain chain)
    {
        this.listElements = listElements;
        this.listExtContacts = listExtContacts;
        this.chain = chain;
    }

    public Chain getChain()
    {
        return chain;
    }

    public int getChainCount()
    {
        return chain.getChainsCount();
    }

    public int getExternalContactCount()
    {
        return listExtContacts.size() - 1;
    }

    public List<Element> getListElements()
    {
        return listElements;
    }

    public List<ExternalContact> getListExtContacts()
    {
        return listExtContacts;
    }

    public static class Element
    {
        private String function;
        private Input  input;
        private int    output;
        private int    delay;
        private int    codedSignal;

        public Element(String function, Input input, int output, int delay)
        {
            this.function = function;
            this.input = input;
            this.output = output;
            this.delay = delay;
        }

        public String getFunction()
        {
            return function;
        }

        public int getDelay()
        {
            return delay;
        }

        public Input getInput()
        {
            return input;
        }

        public int getOutput()
        {
            return output;
        }

        public void codeSignal(int data)
        {
            codedSignal = data;
        }

        public int getCodedSignal()
        {
            return codedSignal;
        }
    }
    public static class Input
    {
        private int infoInput;
        private int addressInput;
        private int controlInput;

        public Input(int infoInput, int addressInput, int controlInput)
        {
            this.infoInput = infoInput;
            this.addressInput = addressInput;
            this.controlInput = controlInput;
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

        public int generalInputCount()
        {
            return (infoInput + addressInput + controlInput);
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + addressInput;
            result = prime * result + controlInput;
            result = prime * result + infoInput;
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Input other = (Input) obj;
            if (addressInput != other.addressInput)
                return false;
            if (controlInput != other.controlInput)
                return false;
            if (infoInput != other.infoInput)
                return false;
            return true;
        }

    }
    public static class Contact
    {
        private String type;
        private int    numberContact;

        public Contact(String type, int numberContact)
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
    }
    public static class ExternalContact extends Contact
    {
        private int chainNumber;
        private int value;

        public ExternalContact(String type, int numberContact, int chainNumber, int value)
        {
            super(type, numberContact);
            this.chainNumber = chainNumber;
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }

        public int getChainNumber()
        {
            return chainNumber;
        }
    }
    public static class ElementContact extends Contact
    {
        private int numberElement;

        public ElementContact(String type, int numberContact, int numberElement)
        {
            super(type, numberContact);
            this.numberElement = numberElement;
        }

        public int getElementNumber()
        {
            return numberElement;
        }
    }
    public static class Chain
    {
        private Map<Integer, List<ElementContact>> mapChains = new HashMap<Integer, List<ElementContact>>();

        public Chain()
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
    }
}