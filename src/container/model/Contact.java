package container.model;

public class Contact extends ExternalContact
{
    private int numberElement;

    public Contact(String type, int chainNumber, int numberContact, int numberElement)
    {
        super(type, chainNumber, numberContact);
        this.numberElement = numberElement;
    }

    public int getElementNumber()
    {
        return numberElement;
    }
    public String toString()
    {
        return super.toString()+", numberElement ="+numberElement+"]";
    }
}
