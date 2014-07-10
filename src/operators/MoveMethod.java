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
import identification.ClientServerIdentification;
import java.util.ArrayList;
import java.util.List;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import pojo.Client;
import pojo.Layer;
import pojo.Server;
import pojo.Style;
import util.OperatorUtil;
import static util.OperatorUtil.randomObject;
import util.ParametersRepository;
import util.StyleUtil;

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
                doMutationClientServer(probability, architecture, (List<Style>) styles);
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

            mutation(sourceClass, architecture, targetPackage, sourceComp);
        }
        ClassesComp.clear();
    }

    @Override
    public void doMutationClientServer(double probability, Architecture architecture, List<Style> list) {
        //TODO: Experimento: mudar para solution ao realizar os testes na MOA4PLA
        //final Architecture arch = ((Architecture) solution.getDecisionVariables()[0]);
        final Package sourceComp = OperatorUtil.randomObject(new ArrayList<Package>(architecture.getAllPackages()));
        List<Class> ClassesComp = new ArrayList<Class>(sourceComp.getAllClasses());
        OperatorUtil.removeClassesInPatternStructureFromArray(ClassesComp);
        if (ClassesComp.size() > 0) {
            final Class sourceClass = OperatorUtil.randomObject(ClassesComp);
            Style csSourcePackage = StyleUtil.returnClientServer(sourceComp, list);
            Package targetPackage = null;
            //restricao
            if (csSourcePackage instanceof Client) {
                targetPackage = OperatorUtil.randomObject(csSourcePackage.getPackages());
            } else {
                Server targetServer = OperatorUtil.randomObject(ClientServerIdentification.getLISTSERVERS());
                targetPackage = OperatorUtil.randomObject(targetServer.getPackages());
            }
            mutation(sourceClass, architecture, targetPackage, sourceComp);
        }
        ClassesComp.clear();
    }

    public void mutation(arquitetura.representation.Class sourceClass, Architecture architecture, arquitetura.representation.Package targetPackage, arquitetura.representation.Package sourceComp) {
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
                    Method method = randomObject(MethodsClass);
                    //add para adicionar a um log
                    ParametersRepository.setTargetPackage(targetPackage);
                    ParametersRepository.setTargetClass(targetClass);
                    ParametersRepository.setSourcePackage(sourceComp);
                    ParametersRepository.setSourceClass(sourceClass);
                    ParametersRepository.setMoveMethod(method);
                    //--
                    if (sourceClass.moveMethodToClass(method, targetClass)) {
                        OperatorUtil.createAssociation(architecture, targetClass, sourceClass);
                    }
                }
                MethodsClass.clear();
            }
        }
    }

}
