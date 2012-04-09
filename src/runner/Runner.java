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

        Element element = new Element("decoder", new Input(3, 0, 3), 8, 40);
        element.codeSignal(1041);
        ModellingElement mod = new ModellingElement(element);
        System.out.println(mod.runModellingOfElement());
    }
}
