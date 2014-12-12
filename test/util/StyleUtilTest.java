/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import arquitetura.builders.ArchitectureBuilder;
import arquitetura.io.ReaderConfig;
import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.Method;
import arquitetura.representation.Package;
import arquitetura.representation.relationship.AssociationRelationship;
import arquitetura.representation.relationship.Relationship;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import pojo.Layer;
import pojo.Style;

/**
 *
 * @author thaina
 */
public class StyleUtilTest {

    public StyleUtilTest() {
    }

    @Test
    public void testReturnPointcuts() throws Exception {
        System.out.println("returnPointcuts");

        ReaderConfig.setPathToProfileSMarty("/home/thaina/NetBeansProjects/OPLA-ArchStyles/test/models/aspect/smarty.profile.uml");
        ReaderConfig.setPathToProfileConcerns("/home/thaina/NetBeansProjects/OPLA-ArchStyles/test/models/aspect/concerns.profile.uml");
        ReaderConfig.setPathProfileRelationship("/home/thaina/NetBeansProjects/OPLA-ArchStyles/test/models/aspect/relationships.profile.uml");
        ReaderConfig.setPathToProfileAspect("/home/thaina/NetBeansProjects/OPLA-ArchStyles/test/models/aspect/aspect.profile.uml");
        ArchitectureBuilder builder = new ArchitectureBuilder();
        Architecture architecture = builder.create("/home/thaina/NetBeansProjects/OPLA-ArchStyles/test/models/aspect/model.uml");

        List<AssociationRelationship> pointcuts = StyleUtil.returnPointcuts(architecture);
        Assert.assertTrue(pointcuts.size() == 4);
    }

    @Test
    public void isJoinPoint() throws Exception {
        ReaderConfig.setPathToProfileSMarty("/home/thaina/NetBeansProjects/OPLA-ArchStyles/test/models/aspect/smarty.profile.uml");
        ReaderConfig.setPathToProfileConcerns("/home/thaina/NetBeansProjects/OPLA-ArchStyles/test/models/aspect/concerns.profile.uml");
        ReaderConfig.setPathProfileRelationship("/home/thaina/NetBeansProjects/OPLA-ArchStyles/test/models/aspect/relationships.profile.uml");
        ReaderConfig.setPathToProfileAspect("/home/thaina/NetBeansProjects/OPLA-ArchStyles/test/models/aspect/aspect.profile.uml");
        ArchitectureBuilder builder = new ArchitectureBuilder();
        Architecture architecture = builder.create("/home/thaina/NetBeansProjects/OPLA-ArchStyles/test/models/aspect/model.uml");

        Class class1 = architecture.findClassByName("Class1").get(0);

        Set<Method> methodsClass1 = class1.getAllMethods();
        for (Method methodClass1 : methodsClass1) {
            boolean joinPoint = StyleUtil.isJoinPoint(class1, methodClass1, architecture);
            if (methodClass1.getName().equalsIgnoreCase("op1Class1")) {
                Assert.assertFalse(joinPoint);
            } else if (methodClass1.getName().equalsIgnoreCase("op2Class1")) {
                Assert.assertTrue(joinPoint);
            }
        }

        Class class2 = architecture.findClassByName("Class2").get(0);

        Set<Method> methodsClass2 = class2.getAllMethods();
        for (Method methodClass2 : methodsClass2) {
            boolean joinPoint = StyleUtil.isJoinPoint(class2, methodClass2, architecture);
            if (methodClass2.getName().equalsIgnoreCase("op1Class2")) {
                Assert.assertTrue(joinPoint);
            } else if (methodClass2.getName().equalsIgnoreCase("op2Class2")) {
                Assert.assertFalse(joinPoint);
            }
        }

        Class class3 = architecture.findClassByName("Class3").get(0);

        Set<Method> methodsClass3 = class3.getAllMethods();
        for (Method methodClass3 : methodsClass3) {
            boolean joinPoint = StyleUtil.isJoinPoint(class3, methodClass3, architecture);
            Assert.assertTrue(joinPoint);
        }

        Interface class4 = architecture.findInterfaceByName("Interface1");

        Set<Method> methodsClass4 = class4.getOperations();
        for (Method methodClass4 : methodsClass4) {
            boolean joinPoint = StyleUtil.isJoinPoint(class4, methodClass4, architecture);
            if (methodClass4.getName().equalsIgnoreCase("op1Class4")) {
                Assert.assertTrue(joinPoint);
            } else if (methodClass4.getName().equalsIgnoreCase("op2Class4")) {
                Assert.assertFalse(joinPoint);
            }
        }

        Class class5 = architecture.findClassByName("Class5").get(0);
        Set<Method> methodsClass5 = class5.getAllMethods();
        for (Method methodClass5 : methodsClass5) {
            boolean joinPoint = StyleUtil.isJoinPoint(class5, methodClass5, architecture);
            Assert.assertFalse(joinPoint);
        }

        Class class6 = architecture.findClassByName("Class6").get(0);
        Set<Method> methodsClass6 = class6.getAllMethods();
        for (Method methodClass6 : methodsClass6) {
            boolean joinPoint = StyleUtil.isJoinPoint(class6, methodClass6, architecture);
            Assert.assertFalse(joinPoint);
        }
    }

    @Test
    public void testGetMethod() throws Exception {
        ReaderConfig.setPathToProfileSMarty("/home/thaina/NetBeansProjects/OPLA-ArchStyles/test/models/aspect/smarty.profile.uml");
        ReaderConfig.setPathToProfileConcerns("/home/thaina/NetBeansProjects/OPLA-ArchStyles/test/models/aspect/concerns.profile.uml");
        ReaderConfig.setPathProfileRelationship("/home/thaina/NetBeansProjects/OPLA-ArchStyles/test/models/aspect/relationships.profile.uml");
        ReaderConfig.setPathToProfileAspect("/home/thaina/NetBeansProjects/OPLA-ArchStyles/test/models/aspect/aspect.profile.uml");
        ArchitectureBuilder builder = new ArchitectureBuilder();
        Architecture architecture = builder.create("/home/thaina/NetBeansProjects/OPLA-ArchStyles/test/models/aspect/model.uml");

        Class aspect = architecture.findClassByName("Aspect").get(0);
        Set<Relationship> relationships = aspect.getRelationships();
        for (Relationship relationship : relationships) {
            if (relationship instanceof AssociationRelationship) {
                List<Method> advices = new ArrayList<>();
                AssociationRelationship association = (AssociationRelationship) relationship;
                if (association.getParticipants().get(0).getCLSClass().getName().equals(aspect.getName())) {
                    advices.addAll(StyleUtil.getMethods(association.getParticipants().get(0)));
                    System.out.println("Advices com: " + association.getParticipants().get(1).getCLSClass().getName());
                } else {
                    advices.addAll(StyleUtil.getMethods(association.getParticipants().get(1)));
                    System.out.println("Advices com: " + association.getParticipants().get(0).getCLSClass().getName());
                }
                for (Method advice : advices) {
                    System.out.println(advice.getName());
                }
            }
        }
    }
}
