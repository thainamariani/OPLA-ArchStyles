/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operators;

import arquitetura.representation.Architecture;
import arquitetura.representation.Attribute;
import java.util.ArrayList;
import java.util.List;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import pojo.Layer;
import pojo.Style;
import util.ArchitectureRepository;
import util.OperatorUtil;
import static util.OperatorUtil.isOptional;
import static util.OperatorUtil.isVarPoint;
import static util.OperatorUtil.isVariant;
import static util.OperatorUtil.randomObject;
import static util.OperatorUtil.searchForGeneralizations;

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
                //doMutationClientServer(probability, architecture, (List<ClientServer>) styles);
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
                    final arquitetura.representation.Class targetClass = OperatorUtil.randomObject(new ArrayList<arquitetura.representation.Class>(targetPackage.getAllClasses()));
                    for(arquitetura.representation.Package p: layerSourcePackage.getPackages()){
                        System.out.println("Package: "+p.getName());
                    }
                    System.out.println("Source Class: " +sourceClass);
                    System.out.println("Target Package:" +targetPackage);
                    System.out.println("Target Class: " +targetClass);
                    if ((sourceClass != null) && (!searchForGeneralizations(sourceClass))
                            && (sourceClass.getAllAttributes().size() > 1)
                            && (sourceClass.getAllMethods().size() > 1)
                            && (!isVarPoint(architecture, sourceClass))
                            && (!isVariant(architecture, sourceClass))
                            && (!isOptional(architecture, sourceClass))) {
                        if ((targetClass != null) && (!(targetClass.equals(sourceClass)))) {
                            List<Attribute> attributesClass = new ArrayList<Attribute>(sourceClass.getAllAttributes());
                            if (attributesClass.size() >= 1) {
                                System.out.println("AQUI");
                                if (sourceClass.moveAttributeToClass(randomObject(attributesClass), targetClass)) {
                                    OperatorUtil.createAssociation(architecture, targetClass, sourceClass);
                                }
                            }

                            attributesClass.clear();
                        }
                    }
                }
                ClassesComp.clear();
                //TODO: Experimento: apagar duas linhas abaixo
                ArchitectureRepository.setCurrentArchitecture(architecture);
                ArchitectureRepository.generateArchitecture("testMoveAttribute");
//                } else {
//                    Configuration.logger_.log(Level.SEVERE, "MoveAttributeMutation.doMutation: invalid type. " + "{0}", solution.getDecisionVariables()[0].getVariableType());
//                    java.lang.Class<String> cls = java.lang.String.class;
//                    String name = cls.getName();
//                    throw new JMException("Exception in " + name + ".doMutation()");
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void doMutationClientServer(double probability, Architecture architecture, List<Style> list) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
