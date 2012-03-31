package container.model;

public class Contact extends ExternalContact
{
    private int elementNumber;

    public Contact(String type, int chainNumber, String name, int elementNumber)
    {
        super(type, chainNumber, name);
        this.elementNumber = elementNumber;
    }

    public int getElementNumber()
    {
        return elementNumber;
    }
}
