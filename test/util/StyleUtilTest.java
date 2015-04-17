/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import arquitetura.builders.ArchitectureBuilder;
import arquitetura.io.ReaderConfig;
import arquitetura.representation.Architecture;
import arquitetura.representation.Method;
import arquitetura.representation.relationship.AssociationRelationship;
import arquitetura.representation.relationship.Relationship;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.Test;
import project.aspect.AspectManipulation;

/**
 *
 * @author thaina
 */
//Não está funcionando porque a arquitetura de teste foi modificada
public class StyleUtilTest {

    public StyleUtilTest() {
    }
//
//    @Test
//    public void testReturnPointcuts() throws Exception {
//        System.out.println("returnPointcuts");
//
//        ReaderConfig.setPathToProfileSMarty("test/models/aspect/smarty.profile.uml");
//        ReaderConfig.setPathToProfileConcerns("test/models/aspect/concerns.profile.uml");
//        ReaderConfig.setPathProfileRelationship("test/models/aspect/relationships.profile.uml");
//        ReaderConfig.setPathToProfileAspect("test/models/aspect/aspect.profile.uml");
//        ArchitectureBuilder builder = new ArchitectureBuilder();
//        Architecture architecture = builder.create("test/models/aspect/model.uml");
//
//        List<AssociationRelationship> pointcuts = AspectManipulation.returnPointcuts(architecture);
//        Assert.assertTrue(pointcuts.size() == 4);
//    }
//
//    @Test
//    public void isJoinPoint() throws Exception {
//        ReaderConfig.setPathToProfileSMarty("test/models/aspect/smarty.profile.uml");
//        ReaderConfig.setPathToProfileConcerns("test/models/aspect/concerns.profile.uml");
//        ReaderConfig.setPathProfileRelationship("test/models/aspect/relationships.profile.uml");
//        ReaderConfig.setPathToProfileAspect("test/models/aspect/aspect.profile.uml");
//        ArchitectureBuilder builder = new ArchitectureBuilder();
//        Architecture architecture = builder.create("test/models/aspect/model.uml");
//
//        Class class1 = architecture.findClassByName("Class1").get(0);
//
//        Set<Method> methodsClass1 = class1.getAllMethods();
//        for (Method methodClass1 : methodsClass1) {
//            boolean joinPoint = AspectManipulation.isJoinPoint(class1, methodClass1, architecture);
//            if (methodClass1.getName().equalsIgnoreCase("op1Class1")) {
//                Assert.assertFalse(joinPoint);
//            } else if (methodClass1.getName().equalsIgnoreCase("op2Class1")) {
//                Assert.assertTrue(joinPoint);
//            }
//        }
//
//        Class class2 = architecture.findClassByName("Class2").get(0);
//
//        Set<Method> methodsClass2 = class2.getAllMethods();
//        for (Method methodClass2 : methodsClass2) {
//            boolean joinPoint = AspectManipulation.isJoinPoint(class2, methodClass2, architecture);
//            if (methodClass2.getName().equalsIgnoreCase("op1Class2")) {
//                Assert.assertTrue(joinPoint);
//            } else if (methodClass2.getName().equalsIgnoreCase("op2Class2")) {
//                Assert.assertFalse(joinPoint);
//            }
//        }
//
//        Class class3 = architecture.findClassByName("Class3").get(0);
//
//        Set<Method> methodsClass3 = class3.getAllMethods();
//        for (Method methodClass3 : methodsClass3) {
//            boolean joinPoint = AspectManipulation.isJoinPoint(class3, methodClass3, architecture);
//            Assert.assertTrue(joinPoint);
//        }
//
//        Interface class4 = architecture.findInterfaceByName("Interface1");
//
//        Set<Method> methodsClass4 = class4.getOperations();
//        for (Method methodClass4 : methodsClass4) {
//            boolean joinPoint = AspectManipulation.isJoinPoint(class4, methodClass4, architecture);
//            if (methodClass4.getName().equalsIgnoreCase("op1Class4")) {
//                Assert.assertTrue(joinPoint);
//            } else if (methodClass4.getName().equalsIgnoreCase("op2Class4")) {
//                Assert.assertFalse(joinPoint);
//            }
//        }
//
//        Class class5 = architecture.findClassByName("Class5").get(0);
//        Set<Method> methodsClass5 = class5.getAllMethods();
//        for (Method methodClass5 : methodsClass5) {
//            boolean joinPoint = AspectManipulation.isJoinPoint(class5, methodClass5, architecture);
//            Assert.assertFalse(joinPoint);
//        }
//
//        Class class6 = architecture.findClassByName("Class6").get(0);
//        Set<Method> methodsClass6 = class6.getAllMethods();
//        for (Method methodClass6 : methodsClass6) {
//            boolean joinPoint = AspectManipulation.isJoinPoint(class6, methodClass6, architecture);
//            Assert.assertFalse(joinPoint);
//        }
//    }
//

    @Test
    public void testGetMethod() throws Exception {
        ReaderConfig.setPathToProfileSMarty("test/models/aspect/smarty.profile.uml");
        ReaderConfig.setPathToProfileConcerns("test/models/aspect/concerns.profile.uml");
        ReaderConfig.setPathProfileRelationship("test/models/aspect/relationships.profile.uml");
        ReaderConfig.setPathToProfileAspect("test/models/aspect/aspect.profile.uml");
        ArchitectureBuilder builder = new ArchitectureBuilder();
        Architecture architecture = builder.create("test/models/aspect/model.uml");

        arquitetura.representation.Class aspect = architecture.findClassByName("Aspect").get(0);
        Set<Relationship> relationships = aspect.getRelationships();
        for (Relationship relationship : relationships) {
            if (relationship instanceof AssociationRelationship) {
                List<Method> advices = new ArrayList<>();
                AssociationRelationship association = (AssociationRelationship) relationship;
                if (association.getParticipants().get(0).getCLSClass().getName().equals(aspect.getName())) {
                    advices.addAll(AspectManipulation.getMethods(association.getParticipants().get(0)));
                    System.out.println("Advices com: " + association.getParticipants().get(1).getCLSClass().getName());
                } else {
                    advices.addAll(AspectManipulation.getMethods(association.getParticipants().get(1)));
                    System.out.println("Advices com: " + association.getParticipants().get(0).getCLSClass().getName());
                }
                for (Method advice : advices) {
                    System.out.println(advice.getName());
                }
            }
        }
    }
//
//    @Test
//    public void testReturnPointcut() throws Exception {
//        boolean testInterface1 = false;
//        boolean testClass1 = false;
//
//        ReaderConfig.setPathToProfileSMarty("test/models/aspect/smarty.profile.uml");
//        ReaderConfig.setPathToProfileConcerns("test/models/aspect/concerns.profile.uml");
//        ReaderConfig.setPathProfileRelationship("test/models/aspect/relationships.profile.uml");
//        ReaderConfig.setPathToProfileAspect("test/models/aspect/aspect.profile.uml");
//        ArchitectureBuilder builder = new ArchitectureBuilder();
//        Architecture architecture = builder.create("test/models/aspect/model.uml");
//
//        Interface interface1 = architecture.findInterfaceByName("Interface1");
//        for (Method method : interface1.getOperations()) {
//            if (method.getName().equals("op1Class4")) {
//                AssociationRelationship pointcut = AspectManipulation.returnPointcut(method, interface1);
//                testInterface1 = pointcut.getName().equals("I1");
//            }
//        }
//        Assert.assertTrue(testInterface1);
//
//        Class class1 = architecture.findClassByName("Class1").get(0);
//        for (Method method : class1.getAllMethods()) {
//            if (method.getName().equals("op2Class1")) {
//                AssociationRelationship pointcut = AspectManipulation.returnPointcut(method, class1);
//                testClass1 = pointcut.getName().equals("C1");
//            }
//        }
//        Assert.assertTrue(testClass1);
//
//        Class class5 = architecture.findClassByName("Class5").get(0);
//        Method method = class5.getAllMethods().iterator().next();
//        AssociationRelationship pointcut = AspectManipulation.returnPointcut(method, class5);
//        Assert.assertEquals(null, pointcut);
//
//    }
//
//    @Test
//    public void testReturnPointcutsTargetClass() throws Exception {
//        ReaderConfig.setPathToProfileSMarty("test/models/aspect/smarty.profile.uml");
//        ReaderConfig.setPathToProfileConcerns("test/models/aspect/concerns.profile.uml");
//        ReaderConfig.setPathProfileRelationship("test/models/aspect/relationships.profile.uml");
//        ReaderConfig.setPathToProfileAspect("test/models/aspect/aspect.profile.uml");
//        ArchitectureBuilder builder = new ArchitectureBuilder();
//        Architecture architecture = builder.create("test/models/aspect/model.uml");
//
//        Class aspect = architecture.findClassByName("Aspect").get(0);
//        Class class1 = architecture.findClassByName("Class1").get(0);
//        List<AssociationRelationship> pointcuts = AspectManipulation.returnPointcutsTargetElement(aspect, class1);
//        Assert.assertTrue(pointcuts.size() == 1);
//        Assert.assertTrue(pointcuts.get(0).getName().equals("C1"));
//
//        Class class5 = architecture.findClassByName("Class5").get(0);
//        List<AssociationRelationship> pointcuts1 = AspectManipulation.returnPointcutsTargetElement(aspect, class5);
//        Assert.assertTrue(pointcuts1.isEmpty());
//
//        Class aspect2 = architecture.findClassByName("Aspect2").get(0);
//        List<AssociationRelationship> pointcuts2 = AspectManipulation.returnPointcutsTargetElement(aspect2, class5);
//        Assert.assertTrue(pointcuts2.size() == 1);
//        Assert.assertTrue(pointcuts2.get(0).getName().equals("C5"));
//    }
}
