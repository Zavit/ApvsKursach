package runner;

import modeling.ModellingElement;

import parser.model.DescriptionParser;
import parser.model.Model;
import parser.model.Model.Element;
import parser.model.Model.Input;
import constants.Paths;

public class Runner
{
    public static void main(String[] args) throws Exception
    {
        DescriptionParser parser = new DescriptionParser();
        Model model = parser.parse();
        System.out.println(model);
        if (model == null)
        {
            System.out.println(parser.getHelper());

        }
        Element element = new Element("triggerRS", new Input(2, 0, 0), 2, 25);
        element.codeSignal(1);
        ModellingElement mod = new ModellingElement(element);
        int result = mod.runModellingOfElement();
System.out.println(result);
        //        if (result == -1)
//        {
//            System.out.println(mod.getHelper());
//
//        }
//        else if (result == -2)
//        {
//            System.out.println(mod.getLogicErr());
//        }
//        else
//        {
//            System.out.println(result);
//
//        }
    }
}
