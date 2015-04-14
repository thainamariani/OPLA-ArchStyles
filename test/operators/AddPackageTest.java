/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operators;

import arquitetura.builders.ArchitectureBuilder;
import arquitetura.helpers.UtilResources;
import arquitetura.representation.Architecture;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import project.identification.LayerIdentification;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jmetal.util.PseudoRandom;
import junit.framework.Assert;
import org.junit.Test;
import project.pojo.Layer;
import project.util.OperatorUtil;
import project.util.StyleUtil;

/**
 *
 * @author Thainá
 */
public class AddPackageTest {

    public AddPackageTest() {
    }

    @Test
    public void testDoMutationLayer() throws Exception {

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

        //popula as camadas
        layerIdentification.isCorrect(camadas);

        //EXEMPLO 1
        arquitetura.representation.Package sourceComp = architecture.findPackageByName("Package2L2");
        Layer layer = OperatorUtil.findPackageLayer(camadas, sourceComp);
        Interface sourceInterface = architecture.findInterfaceByName("Interface1L2");

        //seleciona as camadas dos implementadores
        Set<Layer> layersImplementors = new HashSet<>();
        for (Element element : sourceInterface.getImplementors()) {
            arquitetura.representation.Package pac = architecture.findPackageByName(UtilResources.extractPackageName(element.getNamespace()));
            layersImplementors.add(OperatorUtil.findPackageLayer(camadas, pac));
        }

        //lista que contém as possiveis camadas para criar o novo pacote
        List<Layer> layersSelect = new ArrayList<>();

        //if - se não houver implementadores adiciona todas as camadas
        if (layersImplementors.isEmpty()) {
            for (Layer l : camadas) {
                layersSelect.add(l);
            }
        } else {
            //add a camada atual
            layersSelect.add(layer);

            //if - todos os impl estiverem na mesma camada add a camada inferior também
            if (layersImplementors.size() == 1) {
                if (layersImplementors.iterator().next().getNumero() != 1) {
                    layersSelect.add(StyleUtil.returnLayerNumber(layersImplementors.iterator().next().getNumero() - 1, camadas));
                }
            }
        }

        Layer layerSelect = OperatorUtil.randomObject(layersSelect);

        //posiveis sufixos ou prefixos (0 = sufixo, 1 = prefixo);
        boolean suffix;
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

        //testes
        Assert.assertTrue(suffix);
        Assert.assertTrue(result == 0);
        Assert.assertTrue(layersSelect.contains(layer2));
        Assert.assertFalse(layersSelect.contains(layer1));
        Assert.assertFalse(layersSelect.contains(layer3));

    }

    @Test
    public void testDoMutationLayer2() throws Exception {

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

        //popula as camadas
        layerIdentification.isCorrect(camadas);

        //EXEMPLO 1
        arquitetura.representation.Package sourceComp = architecture.findPackageByName("Package2L2");
        Layer layer = OperatorUtil.findPackageLayer(camadas, sourceComp);
        Interface sourceInterface = architecture.findInterfaceByName("Interface2L2");

        //seleciona as camadas dos implementadores
        Set<Layer> layersImplementors = new HashSet<>();
        for (Element element : sourceInterface.getImplementors()) {
            arquitetura.representation.Package pac = architecture.findPackageByName(UtilResources.extractPackageName(element.getNamespace()));
            layersImplementors.add(OperatorUtil.findPackageLayer(camadas, pac));
        }

        //lista que contém as possiveis camadas para criar o novo pacote
        List<Layer> layersSelect = new ArrayList<>();

        //if - se não houver implementadores adiciona todas as camadas
        if (layersImplementors.isEmpty()) {
            for (Layer l : camadas) {
                layersSelect.add(l);
            }
        } else {
            //add a camada atual
            layersSelect.add(layer);

            //if - todos os impl estiverem na mesma camada add a camada inferior também
            if (layersImplementors.size() == 1) {
                if (layersImplementors.iterator().next().getNumero() != 1) {
                    layersSelect.add(StyleUtil.returnLayerNumber(layersImplementors.iterator().next().getNumero() - 1, camadas));
                }
            }
        }

        Layer layerSelect = OperatorUtil.randomObject(layersSelect);

        //posiveis sufixos ou prefixos (0 = sufixo, 1 = prefixo);
        boolean suffix;
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

        //testes
        Assert.assertTrue(suffix);
        Assert.assertTrue(result == 0);
        Assert.assertTrue(layersSelect.contains(layer2));
        Assert.assertTrue(layersSelect.contains(layer1));
        Assert.assertFalse(layersSelect.contains(layer3));

    }

    @Test
    public void testDoMutationLayer3() throws Exception {

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

        //popula as camadas
        layerIdentification.isCorrect(camadas);

        //EXEMPLO 1
        arquitetura.representation.Package sourceComp = architecture.findPackageByName("Package1L1");
        Layer layer = OperatorUtil.findPackageLayer(camadas, sourceComp);
        Interface sourceInterface = architecture.findInterfaceByName("Interface2L1");

        //seleciona as camadas dos implementadores
        Set<Layer> layersImplementors = new HashSet<>();
        for (Element element : sourceInterface.getImplementors()) {
            arquitetura.representation.Package pac = architecture.findPackageByName(UtilResources.extractPackageName(element.getNamespace()));
            layersImplementors.add(OperatorUtil.findPackageLayer(camadas, pac));
        }

        //lista que contém as possiveis camadas para criar o novo pacote
        List<Layer> layersSelect = new ArrayList<>();

        //if - se não houver implementadores adiciona todas as camadas
        if (layersImplementors.isEmpty()) {
            for (Layer l : camadas) {
                layersSelect.add(l);
            }
        } else {
            //add a camada atual
            layersSelect.add(layer);

            //if - todos os impl estiverem na mesma camada add a camada inferior também
            if (layersImplementors.size() == 1) {
                if (layersImplementors.iterator().next().getNumero() != 1) {
                    layersSelect.add(StyleUtil.returnLayerNumber(layersImplementors.iterator().next().getNumero() - 1, camadas));
                }
            }
        }

        Layer layerSelect = OperatorUtil.randomObject(layersSelect);

        //posiveis sufixos ou prefixos (0 = sufixo, 1 = prefixo);
        boolean suffix;
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

        //testes
        Assert.assertTrue(suffix);
        Assert.assertTrue(result == 0);
        Assert.assertTrue(layersSelect.contains(layer2));
        Assert.assertTrue(layersSelect.contains(layer1));
        Assert.assertTrue(layersSelect.contains(layer3));

    }

}
