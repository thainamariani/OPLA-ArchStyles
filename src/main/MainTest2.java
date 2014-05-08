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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import operators.MoveAttribute;
import operators.MoveMethod;
import pojo.Layer;
import util.ArchitectureRepository;

/**
 *
 * @author Thainá
 */
public class MainTest2 {

    public static void main(String[] args) throws Exception {
        ArchitectureBuilder builder = new ArchitectureBuilder();
        Architecture architecture = builder.create("C:/Users/Thainá/Documents/NetBeansProjects/OPLA-ArchStyles/test/models/archtest2/model.uml");
        LayerIdentification layerIdentification = new LayerIdentification(architecture);
        List<Layer> camadas = new ArrayList<>();
        Layer layer1 = new Layer();
        layer1.setNumero(1);
        Map sp1 = new HashMap();
        sp1.put("L1", "suffix");
        layer1.setSp(sp1);
        camadas.add(layer1);

        Layer layer2 = new Layer();
        layer2.setNumero(2);
        Map sp2 = new HashMap();
        sp2.put("L2", "suffix");
        layer2.setSp(sp2);
        camadas.add(layer2);

        Layer layer3 = new Layer();
        layer3.setNumero(3);
        Map sp3 = new HashMap();
        sp3.put("L3", "suffix");
        layer3.setSp(sp3);
        camadas.add(layer3);

        if (layerIdentification.isCorrect(camadas)) {

            //MoveMethod moveMethod = new MoveMethod();
            //moveMethod.doMutation(1, architecture, "layer", LayerIdentification.getLISTLAYERS());
            MoveAttribute moveAttribute = new MoveAttribute();
            moveAttribute.doMutation(1, architecture, "layer", LayerIdentification.getLISTLAYERS());
            ArchitectureRepository.setCurrentArchitecture(architecture);
            ArchitectureRepository.saveArchitecture("moveattribute", "archtest2");
        }
    }
}
