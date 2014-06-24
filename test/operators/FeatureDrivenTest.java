/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operators;

import arquitetura.builders.ArchitectureBuilder;
import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Concern;
import identification.LayerIdentification;
import java.util.ArrayList;
import java.util.List;
import jmetal.problems.OPLA;
import jmetal.util.PseudoRandom;
import org.apache.log4j.lf5.util.ResourceUtils;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import pojo.Layer;
import util.OperatorUtil;

/**
 *
 * @author Thainá
 */
public class FeatureDrivenTest {

    boolean suffix = false;

    public FeatureDrivenTest() {
    }

    @Test
    public void testDoMutation() throws Exception {
    }

    @Test
    public void testDoMutationLayer() {

        try {
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
                final List<arquitetura.representation.Package> allComponents = new ArrayList<arquitetura.representation.Package>(architecture.getAllPackages());
                if (!allComponents.isEmpty()) {
                    //final arquitetura.representation.Package selectedComp = OperatorUtil.randomObject(allComponents);
                    arquitetura.representation.Package selectedComp = architecture.findPackageByName("Package1L1");
                    List<Concern> concernsSelectedComp = new ArrayList<Concern>(selectedComp.getAllConcerns());
                    Concern concernPlay = new Concern("play");
                    Concern concernBrickles = new Concern("brickles");
                    Assert.assertTrue(concernsSelectedComp.contains(concernPlay));
                    Assert.assertTrue(concernsSelectedComp.contains(concernBrickles));

                    if (concernsSelectedComp.size() > 1) {
                        //selecionado manualmente para testes
                        final Concern selectedConcern = concernPlay;
                        int cont0 = 0;
                        int cont1 = 0;
                        int cont2 = 0;
                        arquitetura.representation.Package package1l3 = architecture.findPackageByName("Package1L3");
                        arquitetura.representation.Package package2l3 = architecture.findPackageByName("Package2L3");
                        for (Layer layer : LayerIdentification.getLISTLAYERS()) {
                            arquitetura.representation.Package newComp = null;
                            List<arquitetura.representation.Package> packagesLayerAssignedOnlyToConcern = new ArrayList<arquitetura.representation.Package>(OperatorUtil.searchComponentsAssignedToConcern(selectedConcern, layer.getPackages()));
                            if (packagesLayerAssignedOnlyToConcern.isEmpty()) {
                                String name = getSuffixPrefix(layer);
                                if (suffix) {
                                    newComp = architecture.createPackage("Package" + OPLA.contComp_ + name);
                                } else {
                                    newComp = architecture.createPackage(name + "Package" + OPLA.contComp_);
                                }
                                cont0++;
                                //OperatorUtil.modularizeConcernInComponent(allComponents, newComp, selectedConcern, architecture);
                            } else {
                                if (packagesLayerAssignedOnlyToConcern.size() == 1) {
                                    Assert.assertTrue(packagesLayerAssignedOnlyToConcern.get(0).getName().equals("Package2L1"));
                                    cont1++;
                                    //OperatorUtil.modularizeConcernInComponent(allComponents, packagesLayerAssignedOnlyToConcern.get(0), selectedConcern, architecture);
                                } else {
                                    cont2++;
                                    Assert.assertTrue(packagesLayerAssignedOnlyToConcern.contains(package1l3));
                                    Assert.assertTrue(packagesLayerAssignedOnlyToConcern.contains(package2l3));
                                    //OperatorUtil.modularizeConcernInComponent(allComponents, OperatorUtil.randomObject(packagesLayerAssignedOnlyToConcern), selectedConcern, architecture);
                                }
                            }
                            packagesLayerAssignedOnlyToConcern.clear();
                        }
                        Assert.assertTrue(cont0 == 1);
                        Assert.assertTrue(cont1 == 1);
                        Assert.assertTrue(cont2 == 1);
                    }
                    concernsSelectedComp.clear();
                    allComponents.clear();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDoMutationClientServer() {
    }

    @Test
    public void testGetSuffixPrefix() {
    }

    //métodos adicionais
    public String getSuffixPrefix(Layer layerSelect) {
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

        return name;
    }

}
