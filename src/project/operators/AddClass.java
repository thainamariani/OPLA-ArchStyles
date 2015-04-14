/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.operators;

import arquitetura.representation.Architecture;
import arquitetura.representation.Attribute;
import arquitetura.representation.Method;
import arquitetura.representation.relationship.AssociationEnd;
import arquitetura.representation.relationship.AssociationRelationship;
import project.aspect.AspectManipulation;
import project.identification.ClientServerIdentification;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jmetal.problems.OPLA;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import project.mutation.PLAFeatureMutationConstraints;
import project.pojo.Client;
import project.pojo.Layer;
import project.pojo.Server;
import project.pojo.Style;
import project.util.OperatorUtil;
import static project.util.OperatorUtil.isOptional;
import static project.util.OperatorUtil.isVarPoint;
import static project.util.OperatorUtil.isVariant;
import static project.util.OperatorUtil.randomObject;
import static project.util.OperatorUtil.removeClassesInPatternStructureFromArray;
import static project.util.OperatorUtil.searchForGeneralizations;
import project.util.ParametersRepository;
import project.util.StyleUtil;

/**
 *
 * @author Thainá
 */
public class AddClass implements OperatorConstraints {

    @Override
    public void doMutation(double probability, Architecture architecture, String style, List<? extends Style> styles) throws JMException {
        if (PseudoRandom.randDouble() < probability) {
            if (style.equals("layer")) {
                doMutationLayer(probability, architecture, (List<Layer>) styles);
            } else if (style.equals("clientserver")) {
                doMutationClientServer(probability, architecture, (List<Style>) styles);
            } else if (style.equals("aspect")) {
                doMutationAspect(probability, architecture);
            }
        }
    }

    @Override
    public void doMutationLayer(double probability, Architecture architecture, List<Layer> layers) {
        if (PseudoRandom.randDouble() < probability) {
            //TODO: Experimento: adicionar duas linhas abaixo
            //if (solution.getDecisionVariables()[0].getVariableType() == java.lang.Class.forName(Architecture.ARCHITECTURE_TYPE)) {
            //Architecture arch = ((Architecture) solution.getDecisionVariables()[0]);
            arquitetura.representation.Package sourceComp = randomObject(new ArrayList<arquitetura.representation.Package>(architecture.getAllPackages()));
            List<arquitetura.representation.Class> ClassesComp = AspectManipulation.returnClassesWithoutAspect(sourceComp);//new ArrayList<arquitetura.representation.Class>(sourceComp.getAllClasses());
            removeClassesInPatternStructureFromArray(ClassesComp);
            if (ClassesComp.size() > 0) {
                arquitetura.representation.Class sourceClass = randomObject(ClassesComp);
                Layer layerSourcePackage = OperatorUtil.findPackageLayer(layers, sourceComp);
                arquitetura.representation.Package targetPackage = OperatorUtil.randomObject(layerSourcePackage.getPackages());
                mutation(sourceClass, architecture, sourceComp, targetPackage);
            }
            ClassesComp.clear();
        }
    }

    @Override
    public void doMutationClientServer(double probability, Architecture architecture, List<Style> list) {
        final arquitetura.representation.Package sourceComp = OperatorUtil.randomObject(new ArrayList<arquitetura.representation.Package>(architecture.getAllPackages()));
        List<arquitetura.representation.Class> ClassesComp = AspectManipulation.returnClassesWithoutAspect(sourceComp);//new ArrayList<arquitetura.representation.Class>(sourceComp.getAllClasses());
        OperatorUtil.removeClassesInPatternStructureFromArray(ClassesComp);
        if (ClassesComp.size() > 0) {
            final arquitetura.representation.Class sourceClass = OperatorUtil.randomObject(ClassesComp);
            Style csSourcePackage = StyleUtil.returnClientServer(sourceComp, list);
            arquitetura.representation.Package targetPackage = null;
            //restricao
            if (csSourcePackage instanceof Client) {
                targetPackage = OperatorUtil.randomObject(csSourcePackage.getPackages());
            } else {
                Server targetServer = OperatorUtil.randomObject(ClientServerIdentification.getLISTSERVERS());
                targetPackage = OperatorUtil.randomObject(targetServer.getPackages());
            }
            mutation(sourceClass, architecture, sourceComp, targetPackage);
        }
    }

    private void doMutationAspect(double probability, Architecture architecture) {
        final arquitetura.representation.Package sourceComp = OperatorUtil.randomObject(new ArrayList<arquitetura.representation.Package>(architecture.getAllPackages()));
        List<arquitetura.representation.Class> ClassesComp = AspectManipulation.returnClassesWithoutAspect(sourceComp);
        OperatorUtil.removeClassesInPatternStructureFromArray(ClassesComp);
        if (ClassesComp.size() > 0) {
            final arquitetura.representation.Class sourceClass = OperatorUtil.randomObject(ClassesComp);
            final arquitetura.representation.Package targetPackage = OperatorUtil.randomObject(new ArrayList<arquitetura.representation.Package>(architecture.getAllPackages()));
            mutation(sourceClass, architecture, targetPackage, sourceComp);
        }
        ClassesComp.clear();
    }

    public void mutation(arquitetura.representation.Class sourceClass, Architecture architecture, arquitetura.representation.Package sourceComp, arquitetura.representation.Package targetPackage) {
        if ((sourceClass != null) && (!searchForGeneralizations(sourceClass))
                && (sourceClass.getAllAttributes().size() > 1)
                && (sourceClass.getAllMethods().size() > 1)
                && (!isVarPoint(architecture, sourceClass))
                && (!isVariant(architecture, sourceClass))
                && (!isOptional(architecture, sourceClass))) {
            if (PseudoRandom.randInt(0, 1) == 0) { //attribute
                List<Attribute> AttributesClass = new ArrayList<Attribute>(sourceClass.getAllAttributes());
                if (AttributesClass.size() >= 1) {
                    try {
                        Attribute targetAttribute = randomObject(AttributesClass);
                        arquitetura.representation.Class newClass = targetPackage.createClass("Class" + OPLA.contClass_++, false);
                        //add para adicionar a um log
                        ParametersRepository.setTargetPackage(targetPackage);
                        ParametersRepository.setTargetClass(newClass);
                        ParametersRepository.setSourcePackage(sourceComp);
                        ParametersRepository.setSourceClass(sourceClass);
                        ParametersRepository.setMoveAttribute(targetAttribute);
                        //--

                        PLAFeatureMutationConstraints.LOGGER.info("------------------------------------------------------------------------------");
                        PLAFeatureMutationConstraints.LOGGER.info("Executado Operador Add Class");
                        PLAFeatureMutationConstraints.LOGGER.info("Target Package: " + targetPackage);
                        PLAFeatureMutationConstraints.LOGGER.info("New Class: " + newClass);
                        PLAFeatureMutationConstraints.LOGGER.info("Source Package: " + sourceComp);
                        PLAFeatureMutationConstraints.LOGGER.info("Source Class: " + sourceClass);
                        PLAFeatureMutationConstraints.LOGGER.info("Attribute: " + targetAttribute);

                        OperatorUtil.moveAttributeToNewClass(architecture, sourceClass, targetAttribute, newClass);
                    } catch (Exception ex) {
                        Logger.getLogger(AddClass.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    AttributesClass.clear();
                }
            } else { //method
                List<Method> MethodsClass = new ArrayList<Method>(sourceClass.getAllMethods());
                if (MethodsClass.size() >= 1) {
                    try {
                        arquitetura.representation.Class newClass = targetPackage.createClass("Class" + OPLA.contClass_++, false);
                        Method targetMethod = randomObject(MethodsClass);
                        //add para adicionar a um log
                        ParametersRepository.setTargetPackage(targetPackage);
                        ParametersRepository.setTargetClass(newClass);
                        ParametersRepository.setSourcePackage(sourceComp);
                        ParametersRepository.setSourceClass(sourceClass);
                        ParametersRepository.setMoveMethod(targetMethod);

                        PLAFeatureMutationConstraints.LOGGER.info("------------------------------------------------------------------------------");
                        PLAFeatureMutationConstraints.LOGGER.info("Executado Operador Add Class");
                        PLAFeatureMutationConstraints.LOGGER.info("Target Package: " + targetPackage);
                        PLAFeatureMutationConstraints.LOGGER.info("New Class: " + newClass);
                        PLAFeatureMutationConstraints.LOGGER.info("Source Package: " + sourceComp);
                        PLAFeatureMutationConstraints.LOGGER.info("Source Class: " + sourceClass);
                        PLAFeatureMutationConstraints.LOGGER.info("Method: " + targetMethod);

                        boolean isJoinpoint = false;
                        AspectManipulation aspectManipulation = new AspectManipulation();
                        if (AspectManipulation.isJoinPoint(sourceClass, targetMethod, architecture)) {
                            aspectManipulation.getInformationPointcut(architecture, sourceClass, newClass, targetMethod);
                            isJoinpoint = true;
                        }
                        if (OperatorUtil.moveMethodToNewClass(architecture, sourceClass, targetMethod, newClass)) {
                            if (isJoinpoint) {
                                aspectManipulation.updatePoincut(architecture);
                            } else {
                                aspectManipulation.updatePointcutEnd(targetMethod, newClass);
                            }
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(AddClass.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    MethodsClass.clear();
                }
            }
        }
    }
}
