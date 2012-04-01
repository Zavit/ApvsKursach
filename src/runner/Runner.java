package runner;


import parser.model.DescriptionParser;
import parser.model.DescriptionParser.Model;


public class Runner
{
    public static void main(String[] args) throws Exception
    {
        Model model = DescriptionParser.parse("resources/model.xml");    
        System.out.println(model);
    }
}
