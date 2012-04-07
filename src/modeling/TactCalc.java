package modeling;

import java.util.List;

import parser.model.DescriptionParser.Element;

public class TactCalc
{
    public static int tactCalc(List<Element> elements)
    {
        int [] delay = new int[elements.size()];
        for(int i=0;i<elements.size();i++)
        {
            delay[i] = elements.get(i).getDelay();
        }
        return tactCalcHelper(delay);
    }
    private static int tactCalcHelper(int [] delays)
    {
        return 1;
    }
    
}
