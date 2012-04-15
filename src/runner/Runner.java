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
        Model model = DescriptionParser.parse();

        Element element = new Element("disjunction", new Input(2,0, 0), 1, 25);
        element.codeSignal(9);
        ModellingElement mod = new ModellingElement(element);
        System.out.println(mod.runModellingOfElement());
    }
}
