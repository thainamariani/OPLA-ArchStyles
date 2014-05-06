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
import operators.AddClass;
import pojo.Layer;

/**
 *
 * @author Thainá
 */
public class MainTest {

    public static void main(String[] args) throws Exception {
        //criação da arquitetura
        ArchitectureBuilder builder = new ArchitectureBuilder();
        Architecture architecture = builder.create("C:/Users/Thainá/workspace/AGM/agm.uml");

        LayerIdentification layerIdentification = new LayerIdentification(architecture);

        //valores abaixo (camadas, sufixos, prefixos) serão indicados pelo usuário (GUI)
        //int qtdeCamadas = 3;
        List<Layer> camadas = new ArrayList<>();

        Layer layer1 = new Layer();
        layer1.setNumero(1);
        Map sp1 = new HashMap();
        sp1.put("mgr", "suffix");
        layer1.setSp(sp1);
        camadas.add(layer1);

        Layer layer2 = new Layer();
        layer2.setNumero(2);
        Map sp2 = new HashMap();
        sp2.put("ctrl", "suffix");
        layer2.setSp(sp2);
        camadas.add(layer2);

        Layer layer3 = new Layer();
        layer3.setNumero(3);
        Map sp3 = new HashMap();
        sp3.put("gUi", "suffix");
        layer3.setSp(sp3);
        camadas.add(layer3);

        if (layerIdentification.isCorrect(camadas)) {
            //chama os operadores

            //MoveMethod moveMethod = new MoveMethod();
            //moveMethod.doMutation((Double) parameters.get("probability"), architecture, "layer", LayerIdentification.getLISTLAYERS());
            //MoveAttribute moveAttribute = new MoveAttribute();
            //moveAttribute.doMutation((Double) parameters.get("probability"), architecture, "layer", LayerIdentification.getLISTLAYERS());
            AddClass addClass = new AddClass();
            addClass.doMutation(1, architecture, "layer", LayerIdentification.getLISTLAYERS());
        }
    }
}
