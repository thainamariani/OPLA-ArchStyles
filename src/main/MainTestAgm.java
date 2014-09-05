/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import arquitetura.builders.ArchitectureBuilder;
import arquitetura.representation.Architecture;
import identification.LayerIdentification;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import operators.AddClass;
import operators.AddPackage;
import operators.FeatureDriven;
import operators.MoveAttribute;
import operators.MoveMethod;
import operators.MoveOperation;
import pojo.Layer;
import util.ArchitectureRepository;

/**
 *
 * @author Thainá
 */
public class MainTestAgm {

    public static void main(String[] args) {
        try {
            ArchitectureBuilder builder = new ArchitectureBuilder();
            Architecture architecture = builder.create("/home/thaina/NetBeansProjects/OPLA-ArchStyles/test/models/archtest8/model.uml");
            //Architecture architecture = builder.create("/home/thaina/NetBeansProjects/OPLA-ArchStyles/agm/agm.uml");
            LayerIdentification layerIdentification = new LayerIdentification(architecture);
            //List<Layer> camadas = new ArrayList<>();

//            Layer layer1 = new Layer();
//            layer1.setNumero(1);
//            List<String> sufixos = new ArrayList<>();
//            List<String> prefixos = new ArrayList<>();
//            sufixos.add("Mgr");
//            layer1.setSufixos(sufixos);
//            layer1.setPrefixos(prefixos);
//            camadas.add(layer1);
//
//            Layer layer2 = new Layer();
//            layer2.setNumero(2);
//            List<String> sufixos2 = new ArrayList<>();
//            List<String> prefixos2 = new ArrayList<>();
//            sufixos2.add("Ctrl");
//            layer2.setSufixos(sufixos2);
//            layer2.setPrefixos(prefixos2);
//            camadas.add(layer2);
//
//            Layer layer3 = new Layer();
//            layer3.setNumero(3);
//            List<String> sufixos3 = new ArrayList<>();
//            List<String> prefixos3 = new ArrayList<>();
//            sufixos3.add("Gui");
//            layer3.setSufixos(sufixos3);
//            layer3.setPrefixos(prefixos3);
//            camadas.add(layer3);
            List<Layer> camadas = new ArrayList<>();
            Layer layer1 = new Layer();
            layer1.setNumero(1);
            List<String> sufixos = new ArrayList<>();
            List<String> prefixos = new ArrayList<>();
            sufixos.add("L1");
            layer1.setSufixos(sufixos);
            layer1.setPrefixos(prefixos);
            camadas.add(layer1);

            Layer layer2 = new Layer();
            layer2.setNumero(2);
            List<String> sufixos2 = new ArrayList<>();
            List<String> prefixos2 = new ArrayList<>();
            sufixos2.add("L2");
            layer2.setSufixos(sufixos2);
            layer2.setPrefixos(prefixos2);
            camadas.add(layer2);

            Layer layer3 = new Layer();
            layer3.setNumero(3);
            List<String> sufixos3 = new ArrayList<>();
            List<String> prefixos3 = new ArrayList<>();
            sufixos3.add("L3");
            layer3.setSufixos(sufixos3);
            layer3.setPrefixos(prefixos3);
            camadas.add(layer3);
            
            ArchitectureRepository.setCurrentArchitecture(architecture);
            for (int i = 0; i < 5000; i++) {
                System.out.println("Execution " + i);
                layerIdentification.setArchitecture(architecture);
                if (layerIdentification.isCorrect(camadas)) {

                    MoveAttribute moveAttribute = new MoveAttribute();
                    moveAttribute.doMutation(1, architecture, "layer", LayerIdentification.getLISTLAYERS());
                    LayerIdentification.clearPackagesFromLayers();
                    LayerIdentification.addPackagesToLayers(architecture);

                    MoveMethod moveMethod = new MoveMethod();
                    moveMethod.doMutation(1, architecture, "layer", LayerIdentification.getLISTLAYERS());
                    LayerIdentification.clearPackagesFromLayers();
                    LayerIdentification.addPackagesToLayers(architecture);

                    AddClass addClass = new AddClass();
                    addClass.doMutation(1, architecture, "layer", LayerIdentification.getLISTLAYERS());
                    LayerIdentification.clearPackagesFromLayers();
                    LayerIdentification.addPackagesToLayers(architecture);

                    AddPackage addPackage = new AddPackage();
                    addPackage.doMutation(1, architecture, "layer", LayerIdentification.getLISTLAYERS());
                    LayerIdentification.clearPackagesFromLayers();
                    LayerIdentification.addPackagesToLayers(architecture);

                    FeatureDriven featureDriven = new FeatureDriven();
                    featureDriven.doMutation(1, architecture, "layer", LayerIdentification.getLISTLAYERS());
                    LayerIdentification.clearPackagesFromLayers();
                    LayerIdentification.addPackagesToLayers(architecture);

                    MoveOperation moveOperation = new MoveOperation();
                    moveOperation.doMutation(1, architecture, "layer", LayerIdentification.getLISTLAYERS());
                    LayerIdentification.clearPackagesFromLayers();
                    LayerIdentification.addPackagesToLayers(architecture);
                   

                } else {
                    ArchitectureRepository.saveArchitecture("featuredriven", "archtest8");
                    System.out.println("Arquitetura não correta");
                    break;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(MainTestAgm.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
