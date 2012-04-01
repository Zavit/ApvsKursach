package container.model;


import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Chain
{
    private Map<Integer, List<Contact>> mapChains = new HashMap<Integer, List<Contact>>();

    public Chain()
    {
        
    }
    public void registerChain(Integer number,List<Contact> contact)
    {
      mapChains.put(number, contact);  
    }
    public int getChainsCount()
    {
        return mapChains.size();
    }
    public List<Contact> getListContacts(int chain)
    {
        return Collections.unmodifiableList(mapChains.get(chain));
    }
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(getClass().getName()).append("\n");
        for(Entry<Integer, List<Contact>> entry: mapChains.entrySet())
        {
            builder.append(entry.getKey()).append("=").append(entry.getValue()).append("\n");
        }
        return builder.toString();
    }
}
