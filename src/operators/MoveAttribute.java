/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operators;

import arquitetura.representation.Architecture;
import arquitetura.representation.Attribute;
import identification.ClientServerIdentification;
import java.util.ArrayList;
import java.util.List;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import mutation.PLAFeatureMutationConstraints;
import pojo.Client;
import pojo.Layer;
import pojo.Server;
import pojo.Style;
import util.OperatorUtil;
import static util.OperatorUtil.isOptional;
import static util.OperatorUtil.isVarPoint;
import static util.OperatorUtil.isVariant;
import static util.OperatorUtil.randomObject;
import static util.OperatorUtil.searchForGeneralizations;
import util.ParametersRepository;
import util.StyleUtil;

/**
 *
 * @author Thainá
 */
public class MoveAttribute implements OperatorConstraints {

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
        try {
            if (PseudoRandom.randDouble() < probability) {
                //TODO: Experimento: Acrescentar linha abaixo (if e else)
                //if (solution.getDecisionVariables()[0].getVariableType() == java.lang.Class.forName(Architecture.ARCHITECTURE_TYPE)) {
                //Architecture arch = ((Architecture) solution.getDecisionVariables()[0]);
                final arquitetura.representation.Package sourceComp = OperatorUtil.randomObject(new ArrayList<arquitetura.representation.Package>(architecture.getAllPackages()));
                List<arquitetura.representation.Class> ClassesComp = new ArrayList<arquitetura.representation.Class>(sourceComp.getAllClasses());
                if (ClassesComp.size() > 0) {
                    final arquitetura.representation.Class sourceClass = OperatorUtil.randomObject(ClassesComp);
                    Layer layerSourcePackage = OperatorUtil.findPackageLayer(layers, sourceComp);
                    final arquitetura.representation.Package targetPackage = OperatorUtil.randomObject(layerSourcePackage.getPackages());
                    mutation(sourceClass, architecture, sourceComp, targetPackage);
                }
                ClassesComp.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void doMutationClientServer(double probability, Architecture architecture, List<Style> list) {
        final arquitetura.representation.Package sourceComp = OperatorUtil.randomObject(new ArrayList<arquitetura.representation.Package>(architecture.getAllPackages()));
        List<arquitetura.representation.Class> ClassesComp = new ArrayList<arquitetura.representation.Class>(sourceComp.getAllClasses());
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

    public void mutation(arquitetura.representation.Class sourceClass, Architecture architecture, arquitetura.representation.Package sourceComp, arquitetura.representation.Package targetPackage) {
        final arquitetura.representation.Class targetClass = OperatorUtil.randomObject(new ArrayList<arquitetura.representation.Class>(targetPackage.getAllClasses()));
        if ((sourceClass != null) && (!searchForGeneralizations(sourceClass))
                && (sourceClass.getAllAttributes().size() > 1)
                && (sourceClass.getAllMethods().size() > 1)
                && (!isVarPoint(architecture, sourceClass))
                && (!isVariant(architecture, sourceClass))
                && (!isOptional(architecture, sourceClass))) {
            if ((targetClass != null) && (!(targetClass.equals(sourceClass)))) {
                List<Attribute> attributesClass = new ArrayList<Attribute>(sourceClass.getAllAttributes());
                if (attributesClass.size() >= 1) {
                    Attribute attribute = randomObject(attributesClass);
                    //add para adicionar a um log
                    ParametersRepository.setTargetPackage(targetPackage);
                    ParametersRepository.setTargetClass(targetClass);
                    ParametersRepository.setSourcePackage(sourceComp);
                    ParametersRepository.setSourceClass(sourceClass);
                    ParametersRepository.setMoveAttribute(attribute);
                    //--
                    PLAFeatureMutationConstraints.LOGGER.info("------------------------------------------------------------------------------");
                    PLAFeatureMutationConstraints.LOGGER.info("Operador Move Attribute");
                    PLAFeatureMutationConstraints.LOGGER.info("Target Package: " + targetPackage);
                    PLAFeatureMutationConstraints.LOGGER.info("Target Class: " + targetClass);
                    PLAFeatureMutationConstraints.LOGGER.info("Source Package: " + sourceComp);
                    PLAFeatureMutationConstraints.LOGGER.info("Source Class: " + sourceClass);
                    PLAFeatureMutationConstraints.LOGGER.info("Attribute: " + attribute);

                    if (sourceClass.moveAttributeToClass(attribute, targetClass)) {
                        OperatorUtil.createAssociation(architecture, targetClass, sourceClass);
                    }
                }

                attributesClass.clear();
            }
        }
    }
}
