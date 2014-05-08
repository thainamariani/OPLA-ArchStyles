/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package identification;

import arquitetura.builders.ArchitectureBuilder;
import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Element;
import arquitetura.representation.relationship.AssociationClassRelationship;
import arquitetura.representation.relationship.AssociationEnd;
import arquitetura.representation.relationship.AssociationRelationship;
import arquitetura.representation.relationship.Relationship;
import arquitetura.representation.Package;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;
import org.junit.BeforeClass;
import pojo.Layer;
import util.ElementUtil;
import util.RelationshipUtil;

/**
 *
 * @author Thainá
 */
public class LayerIdentification2Test {

    private static LayerIdentification layerIdentificationTest;
    private static Architecture architectureTest;

    public LayerIdentification2Test() {
    }

    @BeforeClass
    public static void before() throws Exception {
        //instância archteste
        ArchitectureBuilder builder = new ArchitectureBuilder();
        architectureTest = builder.create("C:/Users/Thainá/Documents/NetBeansProjects/OPLA-ArchStyles/test/models/archtest1/model.uml");
        layerIdentificationTest = new LayerIdentification(architectureTest);
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

        //popular as camadas com os pacotes
        layerIdentificationTest.isCorrect(camadas);

    }

    @Test
    public void testCheckAssociationClassRelationship() {
        boolean association1 = false;
        boolean association2 = true;
        for (Layer layer : LayerIdentification.getLISTLAYERS()) {
            for (arquitetura.representation.Package pack : layer.getPackages()) {
                Set<Element> elements = ElementUtil.selectPackageClassesInterfaces(pack);
                for (Element element : elements) {
                    Set<Relationship> relationships = ElementUtil.getRelationshipByElement(element);
                    for (Relationship relationship : relationships) {
                        if (relationship instanceof AssociationClassRelationship) {
                            if (((AssociationClassRelationship) relationship).getAssociationClass().getName().equalsIgnoreCase("AssociationClass1L1")) {
                                association1 = layerIdentificationTest.checkAssociationClassRelationship(((AssociationClassRelationship) relationship), layer.getPackages());
                            } else if (((AssociationClassRelationship) relationship).getAssociationClass().getName().equalsIgnoreCase("AssociationClass2L1")) {
                                association2 = layerIdentificationTest.checkAssociationClassRelationship(((AssociationClassRelationship) relationship), layer.getPackages());
                            }
                        }
                    }
                }
            }
        }
        Assert.assertTrue(association1);
        Assert.assertFalse(association2);
    }

    @Test
    public void testCheckBidirectionalRelationship() {
        boolean associationSameLayer = false;
        boolean associationDiferentLayer = true;
        for (Layer layer : LayerIdentification.getLISTLAYERS()) {
            for (arquitetura.representation.Package pack : layer.getPackages()) {
                Set<Element> elements = ElementUtil.selectPackageClassesInterfaces(pack);
                for (Element element : elements) {
                    Set<Relationship> relationships = ElementUtil.getRelationshipByElement(element);
                    for (Relationship relationship : relationships) {
                        if (relationship instanceof AssociationRelationship) {
                            List<AssociationEnd> participants = ((AssociationRelationship) relationship).getParticipants();
                            if (((participants.get(0).getName().equalsIgnoreCase("class3L2")) && ((participants.get(1).getName().equals("class4L2")))) || ((participants.get(0).getName().equals("Class4L2")) && ((participants.get(1).getName().equals("Class3L2"))))) {
                                associationSameLayer = layerIdentificationTest.checkBidirectionalRelationship((AssociationRelationship) relationship, layer.getPackages());
                            } else if (((participants.get(0).getName().equalsIgnoreCase("class3L2")) && ((participants.get(1).getName().equals("class1L1")))) || ((participants.get(0).getName().equals("Class1L1")) && ((participants.get(1).getName().equals("Class3L2"))))) {
                                associationDiferentLayer = layerIdentificationTest.checkBidirectionalRelationship((AssociationRelationship) relationship, layer.getPackages());
                            }
                        }
                    }
                }
            }
        }
        Assert.assertTrue(associationSameLayer);
        Assert.assertFalse(associationDiferentLayer);
    }

    @Test
    public void testCheckUnidirectionalRelationship() {
        boolean associationuni3 = false;
        boolean associationuni2 = false;
        boolean associationuni1 = true;

        Layer layer = LayerIdentification.getLayerByNumber(2);
        Class class1 = architectureTest.findClassByName("Class1L2").get(0);
        Package pack = ElementUtil.getPackage(class1, architectureTest);

        for (Relationship relationship : class1.getRelationships()) {
            if (relationship instanceof AssociationRelationship) {
                Element used = RelationshipUtil.getUsedElementFromRelationship((AssociationRelationship) relationship);
                Element client = RelationshipUtil.getClientElementFromRelationship((AssociationRelationship) relationship);
                if (((AssociationRelationship) relationship).getName().equalsIgnoreCase("associationuni1")) {
                    associationuni1 = layerIdentificationTest.checkUnidirectionalRelationship(used, client, layer, LayerIdentification.getLISTLAYERS(), layer.getPackages(), pack, relationship);
                } else if (((AssociationRelationship) relationship).getName().equalsIgnoreCase("associationuni2")) {
                    associationuni2 = layerIdentificationTest.checkUnidirectionalRelationship(used, client, layer, LayerIdentification.getLISTLAYERS(), layer.getPackages(), pack, relationship);
                } else if (((AssociationRelationship) relationship).getName().equalsIgnoreCase("associationuni3")) {
                    associationuni3 = layerIdentificationTest.checkUnidirectionalRelationship(used, client, layer, LayerIdentification.getLISTLAYERS(), layer.getPackages(), pack, relationship);
                }
            }
        }

        Assert.assertFalse(associationuni1);
        Assert.assertTrue(associationuni2);
        Assert.assertTrue(associationuni3);
    }

}
