package container.model;

public class ExternalContact
{
    // I-O-E
    private String type;
    private int    chainNumber;
    private int numberContact;

    public ExternalContact(String type, int chainNumber, int numberContact)
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
