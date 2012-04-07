package tests;

import static org.junit.Assert.*;

import modeling.ModellingElement;

import org.junit.Test;

import parser.model.Model.Element;
import parser.model.Model.Input;

public class ModellingElementTest
{

    @Test
    public void testModelling()
    {
        int expected = 21829;
        Element element = new Element("decoder", "dec", new Input(3, 0, 3), 8, 40);
        element.codeSignal(1041);
        ModellingElement mod = new ModellingElement(element);
        assertTrue(expected == mod.runModellingOfElement());
    }

}
