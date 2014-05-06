/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operators;

import arquitetura.representation.Architecture;
import arquitetura.representation.Attribute;
import arquitetura.representation.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jmetal.problems.OPLA;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import static operators.PLAFeatureMutationConstraints.LOGGER;
import pojo.Layer;
import pojo.Style;
import util.ArchitectureRepository;
import util.OperatorUtil;
import static util.OperatorUtil.isOptional;
import static util.OperatorUtil.isVarPoint;
import static util.OperatorUtil.isVariant;
import static util.OperatorUtil.randomObject;
import static util.OperatorUtil.removeClassesInPatternStructureFromArray;
import static util.OperatorUtil.searchForGeneralizations;

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
            } else {
                //doMutationClientServer(probability, architecture, (List<ClientServer>) styles);
            }
        }
    }

    @Override
    public void doMutationLayer(double probability, Architecture architecture, List<Layer> layers) {
        LOGGER.info("Executando AddClassMutation ");
        if (PseudoRandom.randDouble() < probability) {
            //TODO: Experimento: adicionar duas linhas abaixo
            //if (solution.getDecisionVariables()[0].getVariableType() == java.lang.Class.forName(Architecture.ARCHITECTURE_TYPE)) {
            //Architecture arch = ((Architecture) solution.getDecisionVariables()[0]);
            arquitetura.representation.Package sourceComp = randomObject(new ArrayList<arquitetura.representation.Package>(architecture.getAllPackages()));
            List<arquitetura.representation.Class> ClassesComp = new ArrayList<arquitetura.representation.Class>(sourceComp.getAllClasses());
            removeClassesInPatternStructureFromArray(ClassesComp);
            if (ClassesComp.size() > 0) {
                arquitetura.representation.Class sourceClass = randomObject(ClassesComp);
                Layer layerSourcePackage = OperatorUtil.findPackageLayer(layers, sourceComp);
                arquitetura.representation.Package targetPackage = OperatorUtil.randomObject(layerSourcePackage.getPackages());
                
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
                                OperatorUtil.moveAttributeToNewClass(architecture, sourceClass, AttributesClass, targetPackage.createClass("Class" + OPLA.contClass_++, false));
                            } catch (Exception ex) {
                                Logger.getLogger(AddClass.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            AttributesClass.clear();
                        }
                    } else { //method
                        List<Method> MethodsClass = new ArrayList<Method>(sourceClass.getAllMethods());
                        if (MethodsClass.size() >= 1) {
                            try {
                                OperatorUtil.moveMethodToNewClass(architecture, sourceClass, MethodsClass, targetPackage.createClass("Class" + OPLA.contClass_++, false));
                            } catch (Exception ex) {
                                Logger.getLogger(AddClass.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            MethodsClass.clear();
                        }
                    }
                }
            }
            ClassesComp.clear();
            //TODO: Experimento: apagar duas linhas abaixo
            ArchitectureRepository.setCurrentArchitecture(architecture);
            ArchitectureRepository.generateArchitecture("testAddClass");
        }
    }

    @Override
    public void doMutationClientServer(double probability, Architecture architecture, List<Style> list) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
