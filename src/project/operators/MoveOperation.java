/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.operators;

import arquitetura.helpers.UtilResources;
import arquitetura.representation.Architecture;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.Method;
import arquitetura.representation.Package;
import arquitetura.representation.relationship.AssociationEnd;
import arquitetura.representation.relationship.AssociationRelationship;
import project.aspect.AspectManipulation;
import project.identification.ClientServerIdentification;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import project.mutation.PLAFeatureMutationConstraints;
import project.pojo.Client;
import project.pojo.Layer;
import project.pojo.Style;
import project.util.OperatorUtil;
import static project.util.OperatorUtil.randomObject;
import static project.util.OperatorUtil.removeInterfacesInPatternStructureFromArray;
import project.util.ParametersRepository;
import project.util.StyleUtil;

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
            } else if (style.equals("clientserver")) {
                doMutationClientServer(probability, architecture, (List<Style>) styles);
            } else if (style.equals("aspect")) {
                doMutationAspect(probability, architecture);
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
            InterfacesSourceComp.addAll(sourceComp.getAllInterfaces());

            if (InterfacesSourceComp.size() >= 1) {
                removeInterfacesInPatternStructureFromArray(InterfacesSourceComp);
                //Seleciona uma interface aleatória (interface destino)
                Interface sourceInterface = randomObject(InterfacesSourceComp);

                ParametersRepository.setSourcePackage(sourceComp);
                ParametersRepository.setSourceInterface(sourceInterface);

                //seleciona as camadas dos implementadores
                Set<Layer> layersImplementors = new HashSet<>();
                for (Element element : sourceInterface.getImplementors()) {
                    Package pac;
                    if (element instanceof arquitetura.representation.Package) {
                        pac = (arquitetura.representation.Package) element;
                    } else {
                        pac = architecture.findPackageByName(UtilResources.extractPackageName(element.getNamespace()));
                    }
                    layersImplementors.add(OperatorUtil.findPackageLayer(list, pac));
                }

                //adiciona os pointcuts como implementadores (se tiver aspectos)
                //nao testado
                for (AssociationRelationship pointcut : AspectManipulation.returnPointcutsByElement(sourceInterface)) {
                    Element aspect = AspectManipulation.returnAspect(pointcut);
                    Package packageAspect = architecture.findPackageByName(UtilResources.extractPackageName(aspect.getNamespace()));
                    layersImplementors.add(OperatorUtil.findPackageLayer(list, packageAspect));
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
                    targetComp = OperatorUtil.randomObject(new ArrayList<Package>(architecture.getAllPackages()));
                }

                InterfacesTargetComp.addAll(targetComp.getAllInterfaces());
                mutation(InterfacesTargetComp, sourceInterface, targetComp, architecture);

            }
            InterfacesTargetComp.clear();
            InterfacesSourceComp.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doMutationClientServer(double probability, Architecture architecture, List<Style> list) {
        try {
            arquitetura.representation.Package sourceComp = OperatorUtil.randomObject(new ArrayList<arquitetura.representation.Package>(architecture.getAllPackages()));
            List<Interface> InterfacesSourceComp = new ArrayList<Interface>();
            List<Interface> InterfacesTargetComp = new ArrayList<Interface>();
            InterfacesSourceComp.addAll(sourceComp.getAllInterfaces());

            if (InterfacesSourceComp.size() >= 1) {
                removeInterfacesInPatternStructureFromArray(InterfacesSourceComp);
                //Seleciona uma interface aleatória (interface original)
                Interface sourceInterface = randomObject(InterfacesSourceComp);

                ParametersRepository.setSourcePackage(sourceComp);
                ParametersRepository.setSourceInterface(sourceInterface);

                //seleciona os clientes ou servidores dos implementadores
                Set<Style> clientsServersImplementors = new HashSet<>();
                for (Element element : sourceInterface.getImplementors()) {
                    Package pac;
                    if (element instanceof arquitetura.representation.Package) {
                        pac = (arquitetura.representation.Package) element;
                    } else {
                        pac = architecture.findPackageByName(UtilResources.extractPackageName(element.getNamespace()));
                    }
                    clientsServersImplementors.add(StyleUtil.returnClientServer(pac, list));
                }

                //adiciona os pointcuts como implementadores (se tiver aspectos)
                //nao testado
                for (AssociationRelationship pointcut : AspectManipulation.returnPointcutsByElement(sourceInterface)) {
                    Element aspect = AspectManipulation.returnAspect(pointcut);
                    Package packageAspect = architecture.findPackageByName(UtilResources.extractPackageName(aspect.getNamespace()));
                    clientsServersImplementors.add(StyleUtil.returnClientServer(packageAspect, list));
                }

                //cria lista de possíveis targets
                List<Style> targetClientServer = new ArrayList<>();
                //adiciona todos os servidores
                targetClientServer.addAll(ClientServerIdentification.getLISTSERVERS());

                //se todos os implementadores estiveram em um único cliente, adiciona este cliente a lista
                //se não houver implementadores adiciona todos os clientes a lista (a lista conterá todos os pacotes)
                if ((clientsServersImplementors.size() == 1) && (clientsServersImplementors.iterator().next() instanceof Client)) {
                    targetClientServer.add(clientsServersImplementors.iterator().next());
                } else if (clientsServersImplementors.isEmpty()) {
                    targetClientServer.addAll(ClientServerIdentification.getLISTCLIENTS());
                }

                Style selectClientServer = OperatorUtil.randomObject(targetClientServer);
                arquitetura.representation.Package targetComp = OperatorUtil.randomObject(selectClientServer.getPackages());

                InterfacesTargetComp.addAll(targetComp.getAllInterfaces());
                mutation(InterfacesTargetComp, sourceInterface, targetComp, architecture);

            }
            InterfacesTargetComp.clear();
            InterfacesSourceComp.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mutation(List<Interface> InterfacesTargetComp, Interface sourceInterface, arquitetura.representation.Package targetComp, Architecture architecture) {
        if (InterfacesTargetComp.size() >= 1) {
            Interface targetInterface = randomObject(InterfacesTargetComp);
            if (targetInterface != sourceInterface) {
                List<Method> OpsInterface = new ArrayList<Method>();
                OpsInterface.addAll(sourceInterface.getOperations());
                if (OpsInterface.size() >= 1) {
                    Method operation = randomObject(OpsInterface);
                    ParametersRepository.setTargetPackage(targetComp);
                    ParametersRepository.setTargetInterface(targetInterface);
                    ParametersRepository.setMoveMethod(operation);

                    PLAFeatureMutationConstraints.LOGGER.info("------------------------------------------------------------------------------");
                    PLAFeatureMutationConstraints.LOGGER.info("Executado Operador Move Operation");
                    PLAFeatureMutationConstraints.LOGGER.info("Target Package: " + targetComp);
                    PLAFeatureMutationConstraints.LOGGER.info("Target Interface: " + targetInterface);
                    PLAFeatureMutationConstraints.LOGGER.info("Source Interface: " + sourceInterface);
                    for (Element element : sourceInterface.getImplementors()) {
                        PLAFeatureMutationConstraints.LOGGER.info("Implementor: " + element.getName());
                    }

                    boolean isJoinpoint = false;
                    AspectManipulation aspectManipulation = new AspectManipulation();
                    if (AspectManipulation.isJoinPoint(sourceInterface, operation, architecture)) {
                        aspectManipulation.getInformationPointcut(architecture, sourceInterface, targetInterface, operation);
                        isJoinpoint = true;
                    }

                    if (sourceInterface.moveOperationToInterface(operation, targetInterface)) {
                        if (isJoinpoint) {
                            aspectManipulation.updatePoincut(architecture);
                        } else {
                            aspectManipulation.updatePointcutEnd(operation, targetInterface);
                        }
                    }
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

    private void doMutationAspect(double probability, Architecture architecture) {
        try {
            Package sourceComp = randomObject(new ArrayList<Package>(architecture.getAllPackages()));
            Package targetComp = randomObject(new ArrayList<Package>(architecture.getAllPackages()));

            List<Interface> InterfacesSourceComp = new ArrayList<Interface>();
            List<Interface> InterfacesTargetComp = new ArrayList<Interface>();

            InterfacesSourceComp.addAll(sourceComp.getAllInterfaces());
            removeInterfacesInPatternStructureFromArray(InterfacesSourceComp);

            InterfacesTargetComp.addAll(targetComp.getAllInterfaces());

            if (InterfacesSourceComp.size() >= 1) {
                Interface sourceInterface = randomObject(InterfacesSourceComp);
                mutation(InterfacesTargetComp, sourceInterface, targetComp, architecture);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
