package container.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}
