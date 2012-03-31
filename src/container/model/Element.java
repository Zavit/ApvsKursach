package container.model;

public class Element
{
    private String function;
    private String type;
    private int    infoInput;
    private int    addressInput;
    private int    controlInput;

    public Element(String function, String type, int infoInput, int addressInput, int controlInput)
    {
        this.function = function;
        this.type = type;
        this.infoInput = infoInput;
        this.addressInput = addressInput;
        this.controlInput = controlInput;
    }

    public String getFunction()
    {
        return function;
    }

    public String getType()
    {
        return type;
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

    @Override
    public String toString()
    {
        return getClass().getName()+"[function=" + function + ", type=" + type + ", infoInput=" + infoInput + ", addressInput=" + addressInput + ", controlInput=" + controlInput + "]";
    }
    
}
