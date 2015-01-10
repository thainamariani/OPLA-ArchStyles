/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aspect;

import arquitetura.exceptions.ConcernNotFoundException;
import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.Method;
import arquitetura.representation.RelationshipsHolder;
import arquitetura.representation.relationship.AssociationEnd;
import arquitetura.representation.relationship.AssociationRelationship;
import arquitetura.representation.relationship.Relationship;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.ElementUtil;

/**
 *
 * @author thaina
 */
public class AspectManipulation {

    private List<Method> sourceElementJoinpoints;
    private List<Method> sourceElementAdvices;
    private AssociationRelationship originalPointcut;
    private Method joinpoint;
    private AssociationEnd sourceElementJoinpointsEnd;
    private AssociationEnd targetElementJoinpointsEnd;
    private List<Method> joinpointsTargetPoincut;
    private Class aspect;
    private Element targetElement;
    private boolean create = true;

    public AspectManipulation() {
    }

    //verifica se o método é um join point
    public static boolean isJoinPoint(Element sourceElement, Method movedMethod, Architecture architecture) {
        List<Method> joinPoints = new ArrayList<>();
        List<Relationship> relationshipsElement = ElementUtil.getRelationships(sourceElement);
        for (Relationship relationship : relationshipsElement) {
            if (relationship instanceof AssociationRelationship) {
                AssociationRelationship association = (AssociationRelationship) relationship;
                if (association.isPoincut()) {
                    List<AssociationEnd> participants = association.getParticipants();
                    if (participants.get(0).getCLSClass().equals(sourceElement)) {
                        joinPoints.addAll(getMethods(participants.get(0)));
                    } else {
                        joinPoints.addAll(getMethods(participants.get(1)));
                    }
                }
            }
        }
        for (Method joinpoint : joinPoints) {
            if (joinpoint.equals(movedMethod)) {
                return true;
            }
        }
        return false;
    }

    public void updatePoincut(Architecture architecture) {
        try {
            if (sourceElementJoinpoints.size() == 1) {
                architecture.removeRelationship(originalPointcut);
            } else {
                sourceElementJoinpoints.remove(joinpoint);
                updateEnds(sourceElementJoinpointsEnd, sourceElementJoinpoints);
            }

            if (create) {
                AssociationRelationship pointcut = new AssociationRelationship(aspect, targetElement);
                pointcut.addPoincut("pointcut");
                architecture.addPointcut(pointcut);
                targetElementJoinpointsEnd = getElementAssociationEnd(pointcut, targetElement);
                AssociationEnd targetElementAdvicesEnd = getElementAssociationEnd(pointcut, aspect);
                targetElementJoinpointsEnd.setNavigable(true);
                List<Method> joinpoints = new ArrayList<>();
                joinpoints.add(joinpoint);
                updateEnds(targetElementJoinpointsEnd, joinpoints);
                updateEnds(targetElementAdvicesEnd, sourceElementAdvices);
            } else {
                updateEnds(targetElementJoinpointsEnd, joinpointsTargetPoincut);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void updateEnds(AssociationEnd associationEnd, List<Method> joinpoints) {
        String endName = "";
        List<Method> allMethods = new ArrayList<>();
        if (associationEnd.getCLSClass() instanceof Class) {
            allMethods.addAll(((Class) associationEnd.getCLSClass()).getAllMethods());
        } else {
            allMethods.addAll(((Interface) associationEnd.getCLSClass()).getOperations());
        }
        if (allMethods.containsAll(joinpoints) && joinpoints.containsAll(allMethods)) {
            endName = "all";
        } else {
            for (Method joinpoint : joinpoints) {
                endName += joinpoint.getName() + ",";
            }
        }
        if (endName.endsWith(",")) {
            endName = endName.substring(0, endName.length() - 1);
        }
        associationEnd.setName(endName);
    }

    public static Element returnAspect(AssociationRelationship pointcut) {
        if (pointcut.getParticipants().get(0).getCLSClass().isAspect()) {
            return pointcut.getParticipants().get(0).getCLSClass();
        } else {
            return pointcut.getParticipants().get(1).getCLSClass();
        }
    }

    public static AssociationEnd getElementAssociationEnd(AssociationRelationship association, Element element) {
        if (association.getParticipants().get(0).getCLSClass().equals(element)) {
            return association.getParticipants().get(0);
        } else {
            return association.getParticipants().get(1);
        }
    }

    public static List getMethods(AssociationEnd associationEnd) {
        List<Method> methods = new ArrayList<>();
        String name = associationEnd.getName();
        Element element = associationEnd.getCLSClass();
        methods.addAll(returnMethodsByString(name, element));
        return methods;
    }

    public static AssociationRelationship returnPointcut(Method joinpoint, Element joinpointElement) {
        List<Relationship> relationshipsElement = ElementUtil.getRelationships(joinpointElement);
        for (Relationship relationship : relationshipsElement) {
            List<Method> joinPoints = new ArrayList<>();
            if (relationship instanceof AssociationRelationship) {
                AssociationRelationship association = (AssociationRelationship) relationship;
                if (association.isPoincut()) {
                    List<AssociationEnd> participants = association.getParticipants();
                    if (participants.get(0).getCLSClass().equals(joinpointElement)) {
                        joinPoints.addAll(getMethods(participants.get(0)));
                    } else {
                        joinPoints.addAll(getMethods(participants.get(1)));
                    }
                }
                for (Method joinpointfound : joinPoints) {
                    if (joinpoint == joinpointfound) {
                        return (AssociationRelationship) relationship;
                    }
                }
            }
        }
        return null;
    }

    public static List returnMethodsByString(String name, Element element) {
        List<Method> methods = new ArrayList<>();
        if (name.equalsIgnoreCase("all")) {
            if (element instanceof Class) {
                Class classe = (Class) element;
                methods.addAll(classe.getAllMethods());
            } else {
                Interface inter = (Interface) element;
                methods.addAll(inter.getOperations());
            }
        } else {
            String[] split = new String[100];
            if (name.contains(",")) {
                split = name.split(",");
            } else {
                split[0] = name;
            }
            for (int i = 0; i < split.length; i++) {
                String operation = split[i];
                if (element instanceof Class) {
                    Class classe = (Class) element;
                    Set<Method> allMethods = classe.getAllMethods();
                    for (Method method : allMethods) {
                        if (method.getName().equalsIgnoreCase(operation)) {
                            methods.add(method);
                        }
                    }
                } else {
                    Interface inter = (Interface) element;
                    Set<Method> operations = inter.getOperations();
                    for (Method operation1 : operations) {
                        if (operation1.getName().equalsIgnoreCase(operation)) {
                            methods.add(operation1);
                        }
                    }
                }
            }
        }
        return methods;
    }

    public static List<Class> returnClassesWithoutAspect(arquitetura.representation.Package sourcePackage) {
        List<arquitetura.representation.Class> allClasses = new ArrayList<>(sourcePackage.getAllClasses());
        List<arquitetura.representation.Class> returnClasses = new ArrayList<>();
        for (arquitetura.representation.Class classe : allClasses) {
            if (!classe.isAspect()) {
                returnClasses.add(classe);
            }
        }
        return returnClasses;
    }

    public void getInformationPointcut(Architecture architecture, Element sourceElement, Element targetElement, Method joinpoint) {
        try {
            this.targetElement = targetElement;
            this.joinpoint = joinpoint;
            originalPointcut = returnPointcut(joinpoint, sourceElement);
            aspect = (Class) returnAspect(originalPointcut);
            sourceElementAdvices = new ArrayList<>();
            sourceElementJoinpoints = new ArrayList<>();
            AssociationEnd sourceElementAdvicesEnd = getElementAssociationEnd(originalPointcut, aspect);
            sourceElementJoinpointsEnd = getElementAssociationEnd(originalPointcut, sourceElement);
            sourceElementAdvices.addAll(getMethods(sourceElementAdvicesEnd));
            sourceElementJoinpoints.addAll(getMethods(sourceElementJoinpointsEnd));
//            if (sourceElementJoinpoints.size() == 1) {
//                architecture.removeRelationship(originalPointcut);
//            } else {
//                sourceElementJoinpoints.remove(joinpoint);
//                updateEnds(sourceElementJoinpointsEnd, sourceElementJoinpoints);
//            }
            List<AssociationRelationship> targetPointcuts = returnPointcutsTargetElement(aspect, targetElement);
            for (AssociationRelationship targetPointcut : targetPointcuts) {
                List<Method> advicesTargetPointcut = new ArrayList<>();
                joinpointsTargetPoincut = new ArrayList<>();
                targetElementJoinpointsEnd = getElementAssociationEnd(targetPointcut, targetElement);
                AssociationEnd targetElementAdvicesEnd = getElementAssociationEnd(targetPointcut, aspect);
                joinpointsTargetPoincut.addAll(getMethods(targetElementJoinpointsEnd));
                advicesTargetPointcut.addAll(getMethods(targetElementAdvicesEnd));
                if ((advicesTargetPointcut.size() == sourceElementAdvices.size()) && (advicesTargetPointcut.containsAll(sourceElementAdvices))) {
                    joinpointsTargetPoincut.add(joinpoint);
                    //updateEnds(targetElementJoinpointsEnd, joinpointsTargetPoincut);
                    create = false;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //retorna todos os relacionamentos pointcut da arquitetura
    public static List returnPointcuts(Architecture architecture) {
        List<AssociationRelationship> pointcuts = new ArrayList<>();
        RelationshipsHolder relationshipHolder = architecture.getRelationshipHolder();
        List<AssociationRelationship> allAssociations = relationshipHolder.getAllAssociations();
        for (AssociationRelationship association : allAssociations) {
            if (association.isPoincut()) {
                pointcuts.add(association);
            }
        }
        return pointcuts;
    }

    //metodo para verificar se a classe/interface destino possui um pointcut com a classe de origem
    public static List returnPointcutsTargetElement(Class aspect, Element targetElement) {
        List<Relationship> relationships = new ArrayList<>();
        List<AssociationRelationship> pointcuts = new ArrayList<>();
        if (targetElement instanceof Interface) {
            Interface targetInterface = (Interface) targetElement;
            relationships.addAll(targetInterface.getRelationships());
        } else if (targetElement instanceof Class) {
            Class targetClass = (Class) targetElement;
            relationships.addAll(targetClass.getRelationships());
        }
        for (Relationship relationship : relationships) {
            if (relationship instanceof AssociationRelationship) {
                AssociationRelationship association = (AssociationRelationship) relationship;
                if (association.isPoincut() && (association.getParticipants().get(0).getCLSClass().equals(aspect) || association.getParticipants().get(1).getCLSClass().equals(aspect))) {
                    pointcuts.add(association);
                }
            }
        }
        return pointcuts;
    }

}
