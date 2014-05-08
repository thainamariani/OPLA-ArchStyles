/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import arquitetura.builders.ArchitectureBuilder;
import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.relationship.AssociationRelationship;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.BeforeClass;

/**
 *
 * @author Thainá
 */
public class RelationshipUtilTest {

    private static Architecture architectureTest;
    private static Class class3;
    private static Class class4;
    private static Class class5;
    private static Class class6;
    private static Class class7;
    private static Class class8;
    private static Class class9;
    private static Class class10;
    private static Class class11;
    private static Class class12;
    private static Class class13;
    private static Class class14;
    private static Class class15;
    private static Class class16;

    public RelationshipUtilTest() {
    }

    @BeforeClass
    public static void before() throws Exception {
        //instância archteste
        ArchitectureBuilder builder = new ArchitectureBuilder();
        architectureTest = builder.create("C:/Users/Thainá/Documents/NetBeansProjects/OPLA-ArchStyles/test/models/archtest1/model.uml");
        class3 = architectureTest.findClassByName("Class3L1").get(0);
        class4 = architectureTest.findClassByName("Class4L1").get(0);
        class5 = architectureTest.findClassByName("Class5L1").get(0);
        class6 = architectureTest.findClassByName("Class6L1").get(0);
        class7 = architectureTest.findClassByName("Class7L1").get(0);
        class8 = architectureTest.findClassByName("Class8L1").get(0);
        class9 = architectureTest.findClassByName("Class9L1").get(0);
        class10 = architectureTest.findClassByName("Class10L1").get(0);
        class11 = architectureTest.findClassByName("Class11L1").get(0);
        class12 = architectureTest.findClassByName("Class12L1").get(0);
        class13 = architectureTest.findClassByName("Class13L1").get(0);
        class14 = architectureTest.findClassByName("Class14L1").get(0);
        class15 = architectureTest.findClassByName("Class15L1").get(0);
        class16 = architectureTest.findClassByName("Class16L1").get(0);
    }

    @Test
    public void testGetUsedElementFromRelationship() {
        //abstract
        Assert.assertEquals(class4, RelationshipUtil.getUsedElementFromRelationship(class3.getRelationships().iterator().next()));

        //association uni
        Assert.assertEquals(class5, RelationshipUtil.getUsedElementFromRelationship(class5.getRelationships().iterator().next()));

        //association bi
        Assert.assertNull(RelationshipUtil.getUsedElementFromRelationship(class7.getRelationships().iterator().next()));

        //dependency
        Assert.assertEquals(class10, RelationshipUtil.getUsedElementFromRelationship(class9.getRelationships().iterator().next()));

        //generalization
        Assert.assertEquals(class11, RelationshipUtil.getUsedElementFromRelationship(class11.getRelationships().iterator().next()));

        //realization
        Assert.assertEquals(class14, RelationshipUtil.getUsedElementFromRelationship(class13.getRelationships().iterator().next()));

        //usage
        Assert.assertEquals(class15, RelationshipUtil.getUsedElementFromRelationship(class15.getRelationships().iterator().next()));
    }

    @Test
    public void testGetClientElementFromRelationship() {
        //abstract
        Assert.assertEquals(class3, RelationshipUtil.getClientElementFromRelationship(class3.getRelationships().iterator().next()));

        //association uni
        Assert.assertEquals(class6, RelationshipUtil.getClientElementFromRelationship(class5.getRelationships().iterator().next()));

        //association bi
        Assert.assertNull(RelationshipUtil.getClientElementFromRelationship(class7.getRelationships().iterator().next()));

        //dependency
        Assert.assertEquals(class9, RelationshipUtil.getClientElementFromRelationship(class9.getRelationships().iterator().next()));

        //generalization
        Assert.assertEquals(class12, RelationshipUtil.getClientElementFromRelationship(class11.getRelationships().iterator().next()));

        //realization
        Assert.assertEquals(class13, RelationshipUtil.getClientElementFromRelationship(class13.getRelationships().iterator().next()));

        //usage
        Assert.assertEquals(class16, RelationshipUtil.getClientElementFromRelationship(class15.getRelationships().iterator().next()));
    }

    @Test
    public void testVerifyAssociationRelationship() {
        Assert.assertTrue(RelationshipUtil.verifyAssociationRelationship((AssociationRelationship) class5.getRelationships().iterator().next()));
        Assert.assertFalse(RelationshipUtil.verifyAssociationRelationship((AssociationRelationship) class8.getRelationships().iterator().next()));
    }

}
