package container.model;

public class ExternalContact
{
    // I-O-E
    private String type;
    private int    chainNumber;
    private String name;

    public ExternalContact(String type, int chainNumber, String name)
    {
        this.type = type;
        this.chainNumber = chainNumber;
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public String getType()
    {
        return type;
    }

    public int getChainNumber()
    {
        return chainNumber;
    }

}
