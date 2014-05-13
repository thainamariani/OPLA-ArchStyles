/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import arquitetura.builders.ArchitectureBuilder;
import arquitetura.representation.Architecture;
import identification.LayerIdentification;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.BeforeClass;
import pojo.Layer;

/**
 *
 * @author Thainá
 */
public class OperatorUtilTest {

    private static LayerIdentification layerIdentificationTest;
    private static Architecture architectureTest;
    private static List<Layer> camadas;

    public OperatorUtilTest() {
    }

    @BeforeClass
    public static void before() throws Exception {
        //instância archteste
        ArchitectureBuilder builder = new ArchitectureBuilder();
        architectureTest = builder.create("C:/Users/Thainá/Documents/NetBeansProjects/OPLA-ArchStyles/test/models/archtest1/model.uml");
        layerIdentificationTest = new LayerIdentification(architectureTest);
        camadas = new ArrayList<>();
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

        //popular as camadas com os pacotes
        layerIdentificationTest.isCorrect(camadas);

    }

    @Test
    public void testFindPackageLayer() {
        arquitetura.representation.Package sourcePackage = architectureTest.findPackageByName("Package1L1");
        Layer layerSourcePackage = OperatorUtil.findPackageLayer(camadas, sourcePackage);
        Assert.assertEquals(1, layerSourcePackage.getNumero());
        Assert.assertTrue(layerSourcePackage.getPackages().contains(architectureTest.findPackageByName("Package1L1")));
        Assert.assertTrue(layerSourcePackage.getPackages().contains(architectureTest.findPackageByName("Package2L1")));
        Assert.assertTrue(layerSourcePackage.getPackages().contains(architectureTest.findPackageByName("Package3L1")));
        Assert.assertFalse(layerSourcePackage.getPackages().contains(architectureTest.findPackageByName("Package1L2")));

    }

}
