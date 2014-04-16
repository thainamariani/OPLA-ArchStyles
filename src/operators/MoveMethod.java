/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operators;

import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Method;
import arquitetura.representation.Package;
import java.util.ArrayList;
import java.util.List;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import util.OperatorUtil;
import static util.OperatorUtil.createAssociation;
import static util.OperatorUtil.randomObject;

/**
 *
 * @author Thainá
 */
public class MoveMethod implements OperatorConstraints {

    @Override
    public void doMutation(double probability, Architecture architecture) throws JMException {
        if (PseudoRandom.randDouble() < probability) {
            //TODO: mudar para solution ao realizar os testes na MOA4PLA
            //final Architecture arch = ((Architecture) solution.getDecisionVariables()[0]);
            final Package sourceComp = OperatorUtil.randomObject(new ArrayList<Package>(architecture.getAllPackages()));
            List<Class> ClassesComp = new ArrayList<Class>(sourceComp.getAllClasses());
            OperatorUtil.removeClassesInPatternStructureFromArray(ClassesComp);
            if (ClassesComp.size() > 1) {
                final Class targetClass = OperatorUtil.randomObject(ClassesComp);
                final Class sourceClass = OperatorUtil.randomObject(ClassesComp);
                if ((sourceClass != null) && (!OperatorUtil.searchForGeneralizations(sourceClass))
                        && (sourceClass.getAllAttributes().size() > 1)
                        && (sourceClass.getAllMethods().size() > 1)
                        && (!OperatorUtil.isVarPoint(architecture, sourceClass))
                        && (!OperatorUtil.isVariant(architecture, sourceClass))
                        && (!OperatorUtil.isOptional(architecture, sourceClass))) {
                    if ((targetClass != null) && (!(targetClass.equals(sourceClass)))) {
                        final List<Method> MethodsClass = new ArrayList<Method>(sourceClass.getAllMethods());
                        if (MethodsClass.size() >= 1) {
                            if (sourceClass.moveMethodToClass(randomObject(MethodsClass), targetClass)) {
                                OperatorUtil.createAssociation(architecture, targetClass, sourceClass);
                            }
                        }

                        MethodsClass.clear();
                    }
                }
            }
            ClassesComp.clear();
        }
    }

    @Override
    public void doMutationLayer(double probability, Architecture architecture, String scopeLevels) {
    }

    @Override
    public void doMutationClientServer(double probability, Architecture architecture, String scopeLevels) {
    }

}
