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
import identification.LayerIdentification;
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;
import pojo.Layer;
import util.OperatorUtil;
import util.StyleUtil;

/**
 *
 * @author Thainá
 */
public class MoveOperationTest {

    public MoveOperationTest() {
    }

    @Test
    public void testDoMutationLayer() throws Exception {
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
            //EXEMPLO 1
            arquitetura.representation.Package sourceComp = architecture.findPackageByName("Package2L2");
            Layer layer = OperatorUtil.findPackageLayer(camadas, sourceComp);
            Interface sourceInterface = architecture.findInterfaceByName("Interface1L2");
            Set<Layer> layersImplementors = new HashSet<>();
            for (Element element : sourceInterface.getImplementors()) {
                arquitetura.representation.Package pac = architecture.findPackageByName(UtilResources.extractPackageName(element.getNamespace()));
                layersImplementors.add(OperatorUtil.findPackageLayer(camadas, pac));
            }
            Assert.assertTrue(layersImplementors.size() == 2);

            arquitetura.representation.Package targetComp = null;
            List<arquitetura.representation.Package> packages = new ArrayList<>();
            //if - seleciona o targetPackage da mesma camada que a sourceInterface
            //else - seleciona o targetPackage da mesma camada ou da inferior (caso haja) 
            if (layersImplementors.size() >= 2) {
                targetComp = OperatorUtil.randomObject(layer.getPackages());
            } else if (layersImplementors.size() == 1) {
                packages.addAll(layer.getPackages());
                if (layersImplementors.iterator().next().getNumero() != 1) {
                    packages.addAll(StyleUtil.returnPackagesLayerNumber(layersImplementors.iterator().next().getNumero() - 1, camadas));
                }
                targetComp = OperatorUtil.randomObject(packages);
            }

            Assert.assertTrue(layer.getPackages().contains(architecture.findPackageByName("Package1L2")));
            Assert.assertTrue(layer.getPackages().contains(architecture.findPackageByName("Package2L2")));
            Assert.assertFalse(layer.getPackages().contains(architecture.findPackageByName("Package1L1")));
            Assert.assertFalse(layer.getPackages().contains(architecture.findPackageByName("Package2L1")));
            Assert.assertFalse(layer.getPackages().contains(architecture.findPackageByName("Package1L3")));
            Assert.assertFalse(layer.getPackages().contains(architecture.findPackageByName("Package2L3")));
            Assert.assertTrue(layer.getPackages().size() == 2);

            //EXEMPLO 2 
            sourceComp = architecture.findPackageByName("Package2L2");
            layer = OperatorUtil.findPackageLayer(camadas, sourceComp);
            sourceInterface = architecture.findInterfaceByName("Interface2L2");
            layersImplementors = new HashSet<>();
            for (Element element : sourceInterface.getImplementors()) {
                arquitetura.representation.Package pac = architecture.findPackageByName(UtilResources.extractPackageName(element.getNamespace()));
                layersImplementors.add(OperatorUtil.findPackageLayer(camadas, pac));
            }
            Assert.assertTrue(layersImplementors.size() == 1);

            //if - seleciona o targetPackage da mesma camada que a sourceInterface
            //else - seleciona o targetPackage da mesma camada ou da inferior (caso haja) 
            if (layersImplementors.size() >= 2) {
                targetComp = OperatorUtil.randomObject(layer.getPackages());
            } else if (layersImplementors.size() == 1) {
                packages.addAll(layer.getPackages());
                if (layersImplementors.iterator().next().getNumero() != 1) {
                    packages.addAll(StyleUtil.returnPackagesLayerNumber(layersImplementors.iterator().next().getNumero() - 1, camadas));
                }
                targetComp = OperatorUtil.randomObject(packages);
            }

            Assert.assertTrue(packages.contains(architecture.findPackageByName("Package1L2")));
            Assert.assertTrue(packages.contains(architecture.findPackageByName("Package2L2")));
            Assert.assertTrue(packages.contains(architecture.findPackageByName("Package1L1")));
            Assert.assertTrue(packages.contains(architecture.findPackageByName("Package2L1")));
            Assert.assertFalse(packages.contains(architecture.findPackageByName("Package1L3")));
            Assert.assertFalse(packages.contains(architecture.findPackageByName("Package2L3")));
            Assert.assertTrue(packages.size() == 4);

        }
    }

}
