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
import operators.AddPackage;
import pojo.Layer;
import util.ArchitectureRepository;

/**
 *
 * @author Thainá
 */
public class MainTest {

    public static void main(String[] args) throws Exception {
        ArchitectureBuilder builder = new ArchitectureBuilder();
        Architecture architecture = builder.create("C:/Users/Thainá/Documents/NetBeansProjects/OPLA-ArchStyles/test/models/archtest2/model.uml");
        LayerIdentification layerIdentification = new LayerIdentification(architecture);
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

        if (layerIdentification.isCorrect(camadas)) {
            //MoveMethod moveMethod = new MoveMethod();
            //moveMethod.doMutation(1, architecture, "layer", LayerIdentification.getLISTLAYERS());
            //MoveAttribute moveAttribute = new MoveAttribute();
            //moveAttribute.doMutation(1, architecture, "layer", LayerIdentification.getLISTLAYERS());
            //AddClass addClass = new AddClass();
            //addClass.doMutation(1, architecture, "layer", LayerIdentification.getLISTLAYERS());
            //MoveOperation moveOperation = new MoveOperation();
            //moveOperation.doMutation(1, architecture, "layer", LayerIdentification.getLISTLAYERS());
            AddPackage addPackage = new AddPackage();
            addPackage.doMutation(1, architecture, "layer", LayerIdentification.getLISTLAYERS());

            ArchitectureRepository.setCurrentArchitecture(architecture);
            ArchitectureRepository.saveArchitecture("addpackage", "archtest2");
        }
    }
}
