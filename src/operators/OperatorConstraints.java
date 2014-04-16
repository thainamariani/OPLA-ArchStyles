/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operators;

import arquitetura.representation.Architecture;
import jmetal.util.JMException;

/**
 *
 * @author Thainá
 */
public interface OperatorConstraints {

    public void doMutation(double probability, Architecture architecture) throws JMException ;

    public void doMutationLayer(double probability, Architecture architecture, String scopeLevels);

    public void doMutationClientServer(double probability, Architecture architecture, String scopeLevels);

}
