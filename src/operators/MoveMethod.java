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
import pojo.Layer;
import pojo.Style;
import util.ArchitectureRepository;
import util.OperatorUtil;
import static util.OperatorUtil.randomObject;

/**
 *
 * @author Thainá
 */
public class MoveMethod implements OperatorConstraints {

    @Override
    public void doMutation(double probability, Architecture architecture, String style, List<? extends Style> styles) throws JMException {
        if (PseudoRandom.randDouble() < probability) {
            if (style.equals("layer")) {
                doMutationLayer(probability, architecture, (List<Layer>) styles);
            } else {
                //doMutationClientServer(probability, architecture, (List<ClientServer>) styles);
            }
        }
    }

    @Override
    public void doMutationLayer(double probability, Architecture architecture, List<Layer> layers) {
        //TODO: Experimento: mudar para solution ao realizar os testes na MOA4PLA
        //final Architecture arch = ((Architecture) solution.getDecisionVariables()[0]);
        final Package sourceComp = OperatorUtil.randomObject(new ArrayList<Package>(architecture.getAllPackages()));
        List<Class> ClassesComp = new ArrayList<Class>(sourceComp.getAllClasses());
        OperatorUtil.removeClassesInPatternStructureFromArray(ClassesComp);
        if (ClassesComp.size() > 0) {
            final Class sourceClass = OperatorUtil.randomObject(ClassesComp);
            Layer layerSourcePackage = OperatorUtil.findPackageLayer(layers, sourceComp);
            final Package targetPackage = OperatorUtil.randomObject(layerSourcePackage.getPackages());
            final Class targetClass = OperatorUtil.randomObject(new ArrayList<Class>(targetPackage.getAllClasses()));
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
        //TODO: Experimento: apagar duas linhas abaixo
        ArchitectureRepository.setCurrentArchitecture(architecture);
        ArchitectureRepository.generateArchitecture("testMoveMethod");
    }

    @Override
    public void doMutationClientServer(double probability, Architecture architecture, List<Style> list) {
    }

}
