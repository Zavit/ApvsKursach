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

        // Modelling modelling = new Modelling(TactCalc.tactCalc(model.getListElements()), model);
        //modelling.runModelling();

        Element element = new Element("decoder", "dec", new Input(3, 0, 3), 8, 40);
        element.codeSignal(1041);
        ModellingElement mod = new ModellingElement(element);
        System.out.println(mod.runModellingOfElement());
    }
}
