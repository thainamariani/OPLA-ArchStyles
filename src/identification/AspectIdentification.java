/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package identification;

import arquitetura.builders.ArchitectureBuilder;
import arquitetura.representation.Architecture;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.Method;
import arquitetura.representation.relationship.AssociationEnd;
import arquitetura.representation.relationship.AssociationRelationship;
import aspect.AspectManipulation;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thaina
 */
public class AspectIdentification {

    public AspectIdentification() {
    }

    public void verifyAspectArchitecture(Architecture architecture) {
        List<AssociationRelationship> pointcuts = AspectManipulation.returnPointcuts(architecture);
        for (AssociationRelationship pointcut : pointcuts) {
            AssociationEnd end0 = pointcut.getParticipants().get(0);
            AssociationEnd end1 = pointcut.getParticipants().get(1);

            Element element0 = end0.getCLSClass();
            Element element1 = end1.getCLSClass();

            List<Method> methodsElement0 = new ArrayList<>();
            List<Method> methodsElement1 = new ArrayList<>();

            if (element0 instanceof arquitetura.representation.Class) {
                methodsElement0.addAll(((arquitetura.representation.Class) element0).getAllMethods());
            } else {
                methodsElement0.addAll(((arquitetura.representation.Interface) element0).getOperations());
            }

            if (element1 instanceof arquitetura.representation.Class) {
                methodsElement1.addAll(((arquitetura.representation.Class) element1).getAllMethods());
            } else {
                methodsElement1.addAll(((arquitetura.representation.Interface) element1).getOperations());
            }
            System.out.println("");
            System.out.println("");
            System.out.println("Association End0: " + end0.getName());
            System.out.println("Element 0: " + element0.getName());
            for (Method mElement0 : methodsElement0) {
                System.out.println(mElement0.getName());
            }
            System.out.println("");
            System.out.println("");
            System.out.println("Association End1: " + end1.getName());
            System.out.println("Element 1: " + element1.getName());
            for (Method mElement1 : methodsElement1) {
                System.out.println(mElement1.getName());
            }
        }
    }

    public static boolean isCorrectAspect(String plaPath) {
        ArchitectureBuilder builder = new ArchitectureBuilder();
        boolean correct = true;
        try {
            Architecture architecture = builder.create(plaPath);
            List<AssociationRelationship> pointcuts = AspectManipulation.returnPointcuts(architecture);
            if (pointcuts.isEmpty()) {
                System.out.println("A arquitetura não possui pointcuts. Por favor, adicione-os ou selecione o procedimento sem aspectos");
                return false;
            }

//            for (arquitetura.representation.Class classe : architecture.getAllClasses()) {
//                if (classe.isAspect()) {
//                    if (AspectManipulation.returnPointcutsByElement(classe).isEmpty()) {
//                        System.out.println("O aspecto " + classe.getName() + " não possui pointcuts.");
//                        correct = false;
//                    }
//                }
//            }

            correct = checkPointcutsElements(pointcuts, correct);
            if (correct) {
                correct = analyzeEnds(pointcuts, correct);
            }
        } catch (Exception ex) {
            Logger.getLogger(AspectIdentification.class.getName()).log(Level.SEVERE, null, ex);
        }
        return correct;
    }

    private static boolean analyzeEnds(List<AssociationRelationship> pointcuts, boolean correct) {
        for (AssociationRelationship pointcut : pointcuts) {
            arquitetura.representation.Class aspect = (arquitetura.representation.Class) AspectManipulation.returnAspect(pointcut);
            AssociationEnd aspectEnd = null;
            AssociationEnd elementEnd = null;
            Element element = null;

            if (pointcut.getParticipants().get(0).getCLSClass().equals(aspect)) {
                element = pointcut.getParticipants().get(1).getCLSClass();
                elementEnd = pointcut.getParticipants().get(1);
                aspectEnd = pointcut.getParticipants().get(0);
            } else {
                element = pointcut.getParticipants().get(0).getCLSClass();
                elementEnd = pointcut.getParticipants().get(0);
                aspectEnd = pointcut.getParticipants().get(1);
            }

            if (!aspectEnd.getName().equalsIgnoreCase("all")) {
                List<String> methodsEnd = getMethods(aspectEnd);
                if (methodsEnd.isEmpty()) {
                    correct = false;
                    System.out.println("Extremidade " + aspectEnd.getName() + " pertencente ao pointcut " + pointcut.getName() + " possui adendos inválidos. Insira novamente.");
                    System.out.println("Deve-se inserir 'all' para todos os adendos ou o nome dos adendos separados por vírgula (sem espaço)");
                }

                for (String methodEnd : methodsEnd) {
                    boolean achou = false;
                    for (Method adendo : aspect.getAllMethods()) {
                        if (adendo.getName().equalsIgnoreCase(methodEnd)) {
                            achou = true;
                            break;
                        }
                    }
                    if (!achou) {
                        correct = false;
                        System.out.println("Extremidade " + aspectEnd.getName() + " pertencente ao pointcut " + pointcut.getName() + " possui adendos inválidos. Insira novamente.");
                        System.out.println("Deve-se inserir 'all' para todos os adendos ou o nome dos adendos separados por vírgula (sem espaço)");
                    }
                }
            }

            if (!elementEnd.getName().equalsIgnoreCase("all")) {
                List<String> joinpointsEnd = getMethods(elementEnd);
                if (joinpointsEnd.isEmpty()) {
                    System.out.println("Extremidade " + elementEnd.getName() + " pertencente ao pointcut " + pointcut.getName() + " possui joinpoints inválidos. Insira novamente.");
                    System.out.println("Deve-se inserir 'all' para todos os joinpoints ou o nome dos joinpoints separados por vírgula (sem espaço)");
                    correct = false;
                }

                List<Method> allJoinpoints = new ArrayList<>();
                if (element instanceof Interface) {
                    allJoinpoints.addAll(((Interface) element).getOperations());
                } else {
                    allJoinpoints.addAll(((arquitetura.representation.Class) element).getAllMethods());
                }

                for (String joinpointEnd : joinpointsEnd) {
                    boolean achou = false;
                    for (Method joinpoint : allJoinpoints) {
                        if (joinpoint.getName().equalsIgnoreCase(joinpointEnd)) {
                            achou = true;
                            break;
                        }
                    }
                    if (!achou) {
                        System.out.println("Extremidade " + elementEnd.getName() + " pertencente ao pointcut " + pointcut.getName() + " possui joinpoints inválidos. Insira novamente.");
                        System.out.println("Deve-se inserir 'all' para todos os joinpoints ou o nome dos joinpoints separados por vírgula (sem espaço)");
                        correct = false;
                    }
                }
            }
        }
        return correct;
    }

    private static boolean checkPointcutsElements(List<AssociationRelationship> pointcuts, boolean correct) {
        for (AssociationRelationship pointcut : pointcuts) {
            Element aspect = null;
            AssociationEnd associationEnd0 = pointcut.getParticipants().get(0);
            AssociationEnd associationEnd1 = pointcut.getParticipants().get(1);
            if ((!associationEnd0.getCLSClass().isAspect()) && (!associationEnd1.getCLSClass().isAspect())) {
                System.out.println("Pointcut " + pointcut.getName() + " não está relacionado com um aspecto.");
                correct = false;
            } else {
                aspect = AspectManipulation.returnAspect(pointcut);
                if (!(associationEnd0.getCLSClass().isAspect() && associationEnd1.getCLSClass().isAspect())) {
                    if (associationEnd0.getCLSClass().isAspect()) {
                        if (associationEnd0.isNavigable()) {
                            System.out.println("Extremidade da associação " + pointcut.getName() + " do aspecto " + aspect.getName() + " não pode ter navegabilidade");
                            correct = false;
                        }
                        if (!associationEnd1.isNavigable()) {
                            System.out.println("Extremidade da associação " + pointcut.getName() + " do elemento " + associationEnd1.getCLSClass().getName() + " deve ter navegabilidade");
                            correct = false;
                        }
                    } else {
                        if (associationEnd1.isNavigable()) {
                            System.out.println("Extremidade da associação" + pointcut.getName() + "do aspecto" + aspect.getName() + "não pode ter navegabilidade");
                            correct = false;
                        }
                        if (!associationEnd0.isNavigable()) {
                            System.out.println("Extremidade da associação" + pointcut.getName() + "do elemento" + associationEnd0.getCLSClass().getName() + "deve ter navegabilidade");
                            correct = false;
                        }
                    }
                }
            }
        }
        return correct;
    }

    private static List<String> getMethods(AssociationEnd elementEnd) {
        List<String> methods = new ArrayList();
        String[] split = new String[100];
        if (elementEnd.getName().contains(",")) {
            split = elementEnd.getName().split(",");
        } else {
            split[0] = elementEnd.getName();
        }

        for (int i = 0; i < split.length; i++) {
            String method = split[i];
            if (method != null) {
                methods.add(method);
            } else {
                break;
            }
        }
        return methods;
    }
}
