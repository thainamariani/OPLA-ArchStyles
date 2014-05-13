/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operators;

import arquitetura.exceptions.ConcernNotFoundException;
import arquitetura.helpers.UtilResources;
import arquitetura.representation.Architecture;
import arquitetura.representation.Concern;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import jmetal.problems.OPLA;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import pojo.Layer;
import pojo.Style;
import util.OperatorUtil;
import static util.OperatorUtil.getSuffix;
import static util.OperatorUtil.randomObject;
import util.ParametersRepository;
import util.StyleUtil;

/**
 *
 * @author Thainá
 */
public class AddPackage implements OperatorConstraints {

    private boolean suffix;

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
            arquitetura.representation.Package sourceComp = randomObject(new ArrayList<arquitetura.representation.Package>(architecture.getAllPackages()));
            List<Interface> InterfacesSourceComp = new ArrayList<Interface>();
            Layer layer = OperatorUtil.findPackageLayer(list, sourceComp);
            InterfacesSourceComp.addAll(sourceComp.getAllInterfaces());

            if (InterfacesSourceComp.size() >= 1) {
                OperatorUtil.removeInterfacesInPatternStructureFromArray(InterfacesSourceComp);
                Interface sourceInterface = randomObject(InterfacesSourceComp);

                //add log
                ParametersRepository.setSourcePackage(sourceComp);
                ParametersRepository.setSourceInterface(sourceInterface);
                //--

                getSuffixPrefix(sourceInterface, architecture, list, layer);

                List<Method> OpsInterface = new ArrayList<Method>();
                OpsInterface.addAll(sourceInterface.getOperations());
                if (OpsInterface.size() >= 1) {
                    Method op = randomObject(OpsInterface);

                    arquitetura.representation.Package newComp = architecture.createPackage("Package" + OPLA.contComp_ + getSuffix(sourceComp));
                    OPLA.contComp_++;
                    Interface newInterface = newComp.createInterface("Interface" + OPLA.contInt_++);

                    sourceInterface.moveOperationToInterface(op, newInterface);

                    for (Element implementor : sourceInterface.getImplementors()) {
                        if (implementor instanceof arquitetura.representation.Package) {
                            architecture.addImplementedInterface(newInterface, (arquitetura.representation.Package) implementor);
                        }
                        if (implementor instanceof arquitetura.representation.Class) {
                            architecture.addImplementedInterface(newInterface, (arquitetura.representation.Class) implementor);
                        }
                    }
                    for (Concern con : op.getOwnConcerns()) {
                        try {
                            newInterface.addConcern(con.getName());
                        } catch (ConcernNotFoundException ex) {
                            Logger.getLogger(AddPackage.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                OpsInterface.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doMutationClientServer(double probability, Architecture architecture, List<Style> list) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void getSuffixPrefix(Interface sourceInterface, Architecture architecture, List<Layer> list, Layer layer) {
        //seleciona as camadas dos implementadores
        Set<Layer> layersImplementors = new HashSet<>();
        for (Element element : sourceInterface.getImplementors()) {
            arquitetura.representation.Package pac = architecture.findPackageByName(UtilResources.extractPackageName(element.getNamespace()));
            layersImplementors.add(OperatorUtil.findPackageLayer(list, pac));
        }

        //lista que contém as possiveis camadas para criar o novo pacote
        List<Layer> layersSelect = new ArrayList<>();

        //if - se não houver implementadores adiciona todas as camadas
        if (layersImplementors.isEmpty()) {
            for (Layer l : list) {
                layersSelect.add(l);
            }
        } else {
            //add a camada atual
            layersSelect.add(layer);

            //if - todos os impl estiverem na mesma camada add a camada inferior também
            if (layersImplementors.size() == 1) {
                if (layersImplementors.iterator().next().getNumero() != 1) {
                    layersSelect.add(StyleUtil.returnLayerNumber(layersImplementors.iterator().next().getNumero() - 1, list));
                }
            }
        }

        Layer layerSelect = OperatorUtil.randomObject(layersSelect);

        //posiveis sufixos ou prefixos (0 = sufixo, 1 = prefixo);
        String name = "";
        int result = PseudoRandom.randInt(0, 1);
        if (layerSelect.getSufixos().isEmpty()) {
            result = 1;
        } else if (layerSelect.getPrefixos().isEmpty()) {
            result = 0;
        }

        if (result == 0) {
            suffix = true;
            name = OperatorUtil.randomObject(layerSelect.getSufixos());
        } else {
            suffix = false;
            name = OperatorUtil.randomObject(layerSelect.getPrefixos());
        }

        System.out.println("Source Interface:" +sourceInterface);
        System.out.println("LayerSelect: " + layerSelect.getNumero());
        System.out.println("Sufixo/Prefixo:" + name);

    }

}
