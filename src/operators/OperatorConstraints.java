/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operators;

import arquitetura.representation.Architecture;
import java.util.List;
import jmetal.util.JMException;
import pojo.Layer;
import pojo.Style;

/**
 *
 * @author Thainá
 */
public interface OperatorConstraints {

    public void doMutation(double probability, Architecture architecture, String style, List<? extends Style> styles) throws JMException;

    public void doMutationLayer(double probability, Architecture architecture, List<Layer> list);

    public void doMutationClientServer(double probability, Architecture architecture, List<Style> list);
}
