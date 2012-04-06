package runner;




import parser.model.DescriptionParser;

import parser.model.DescriptionParser.Model;
import constants.Paths;


public class Runner
{
    public static void main(String[] args) throws Exception
    {
        Model model = DescriptionParser.parse(Paths.XML_PATH,Paths.XML_SCHEMA_PATH);    
        
   System.out.println(model);
    }
}
