package container.model;

import java.util.List;

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

    public List<Element> getListElements()
    {
        return listElements;
    }

    public List<ExternalContact> getListExtContacts()
    {
        return listExtContacts;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(getClass().getName()).append("\n");
        for (Element element : listElements)
        {
            builder.append(element).append("\n");
        }
        for (ExternalContact extContact : listExtContacts)
        {
            builder.append(extContact).append("\n");
        }
        return builder.append(chain).toString();
    }
}
