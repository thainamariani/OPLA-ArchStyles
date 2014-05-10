/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operators;

import arquitetura.helpers.UtilResources;
import arquitetura.representation.Architecture;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.Method;
import arquitetura.representation.Package;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import pojo.Layer;
import pojo.Style;
import util.OperatorUtil;
import static util.OperatorUtil.checkSameLayer;
import static util.OperatorUtil.randomObject;
import static util.OperatorUtil.removeInterfacesInPatternStructureFromArray;
import util.ParametersRepository;
import util.StyleUtil;

/**
 *
 * @author Thainá
 */
public class MoveOperation implements OperatorConstraints {

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
    public void doMutationLayer(double probability, Architecture architecture, List<Layer> list) {
        try {
            arquitetura.representation.Package sourceComp = OperatorUtil.randomObject(new ArrayList<arquitetura.representation.Package>(architecture.getAllPackages()));
            List<Interface> InterfacesSourceComp = new ArrayList<Interface>();
            List<Interface> InterfacesTargetComp = new ArrayList<Interface>();
            Layer layer = OperatorUtil.findPackageLayer(list, sourceComp);

            if (InterfacesSourceComp.size() >= 1) {
                //Seleciona uma interface aleatória (interface destino)
                InterfacesSourceComp.addAll(sourceComp.getAllInterfaces());
                removeInterfacesInPatternStructureFromArray(InterfacesSourceComp);
                Interface sourceInterface = randomObject(InterfacesSourceComp);

                //seleciona as camadas dos implementadores
                Set<Layer> layersImplementors = new HashSet<>();
                for (Element element : sourceInterface.getImplementors()) {
                    Package pac = architecture.findPackageByName(UtilResources.extractPackageName(element.getNamespace()));
                    layersImplementors.add(OperatorUtil.findPackageLayer(list, pac));
                }

                arquitetura.representation.Package targetComp = null;

                //if - seleciona o targetPackage da mesma camada que a sourceInterface
                //else - seleciona o targetPackage da mesma camada ou da inferior (caso haja) 
                if (layersImplementors.size() >= 2) {
                    targetComp = OperatorUtil.randomObject(layer.getPackages());
                } else if (layersImplementors.size() == 1) {
                    List<Package> packages = new ArrayList<>();
                    packages.addAll(layer.getPackages());
                    if (layersImplementors.iterator().next().getNumero() != 1) {
                        packages.addAll(StyleUtil.returnPackagesLayerNumber(layersImplementors.iterator().next().getNumero() - 1, list));
                    }
                    targetComp = OperatorUtil.randomObject(packages);
                } else {
                    System.out.println("AQUI 0");
                    targetComp = OperatorUtil.randomObject((List<Package>) architecture.getAllPackages());
                }

                InterfacesTargetComp.addAll(targetComp.getAllInterfaces());

                if (InterfacesTargetComp.size() >= 1) {
                    Interface targetInterface = randomObject(InterfacesTargetComp);

                    if (targetInterface != sourceInterface) {
                        List<Method> OpsInterface = new ArrayList<Method>();
                        OpsInterface.addAll(sourceInterface.getOperations());
                        if (OpsInterface.size() >= 1) {
                            Method operation = randomObject(OpsInterface);
                            ParametersRepository.setSourcePackage(sourceComp);
                            ParametersRepository.setTargetPackage(targetComp);
                            ParametersRepository.setSourceInterface(sourceInterface);
                            ParametersRepository.setTargetInterface(targetInterface);
                            ParametersRepository.setMoveMethod(operation);

                            sourceInterface.moveOperationToInterface(operation, targetInterface);
                            for (Element implementor : sourceInterface.getImplementors()) {
                                if (implementor instanceof arquitetura.representation.Package) {
                                    architecture.addImplementedInterface(targetInterface, (arquitetura.representation.Package) implementor);
                                }
                                if (implementor instanceof arquitetura.representation.Class) {
                                    architecture.addImplementedInterface(targetInterface, (arquitetura.representation.Class) implementor);
                                }
                            }
                            OpsInterface.clear();
                        }
                    }
                }
            }
            InterfacesTargetComp.clear();
            InterfacesSourceComp.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doMutationClientServer(double probability, Architecture architecture, List<Style> list) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
