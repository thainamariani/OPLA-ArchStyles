/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

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
import pojo.Layer;
import pojo.Style;

/**
 *
 * @author Thainá
 */
public class StyleUtil {

    public static List<arquitetura.representation.Package> returnPackagesLayerNumber(int numLayer, List<? extends Style> layers) {
        for (Style layer : layers) {
            if (((Layer) layer).getNumero() == numLayer) {
                return layer.getPackages();
            }
        }
        return null;
    }

    public static Layer returnLayerNumber(int numLayer, List<Layer> layers) {
        for (Layer layer : layers) {
            if (layer.getNumero() == numLayer) {
                return layer;
            }
        }
        return null;
    }

    public static Style returnClientServer(arquitetura.representation.Package pacote, List<Style> clientsservers) {
        for (Style clientserver : clientsservers) {
            for (arquitetura.representation.Package pac : clientserver.getPackages()) {
                if (pac.equals(pacote)) {
                    return clientserver;
                }
            }
        }
        return null;
    }

    public static List<arquitetura.representation.Class> returnClassesWithoutAspect(arquitetura.representation.Package sourcePackage) {
        List<Class> allClasses = new ArrayList<Class>(sourcePackage.getAllClasses());
        List<Class> returnClasses = new ArrayList<Class>();
        for (Class classe : allClasses) {
            if (!classe.isAspect()) {
                returnClasses.add(classe);
            }
        }
        return returnClasses;
    }

    public void updatePointcut(Architecture architecture, Class aspect, Element sourceElement, Element targetElement, Method joinpoint) throws ConcernNotFoundException {
        boolean create = true;
        AssociationRelationship originalPointcut = returnPointcut(joinpoint, sourceElement);
        List<Method> sourceElementAdvices = new ArrayList<>();
        List<Method> sourceElementJoinpoints = new ArrayList<>();
        AssociationEnd sourceElementAdvicesEnd = getElementAssociationEnd(originalPointcut, aspect);
        AssociationEnd sourceElementJoinpointsEnd = getElementAssociationEnd(originalPointcut, sourceElement);
        sourceElementAdvices.addAll(getMethods(sourceElementAdvicesEnd));
        sourceElementJoinpoints.addAll(getMethods(sourceElementJoinpointsEnd));

        if (sourceElementJoinpoints.size() == 1) {
            architecture.removeRelationship(originalPointcut);
        } else {
            sourceElementJoinpoints.remove(joinpoint);
            updateEnds(sourceElementJoinpointsEnd, sourceElementJoinpoints);
        }

        List<AssociationRelationship> targetPointcuts = returnPointcutsTargetElement(aspect, targetElement);
        for (AssociationRelationship targetPointcut : targetPointcuts) {
            List<Method> advicesTargetPointcut = new ArrayList<>();
            List<Method> joinpointsTargetPoincut = new ArrayList<>();
            AssociationEnd targetElementJoinpointsEnd = getElementAssociationEnd(targetPointcut, targetElement);
            AssociationEnd targetElementAdvicesEnd = getElementAssociationEnd(targetPointcut, aspect);
            joinpointsTargetPoincut.addAll(getMethods(targetElementJoinpointsEnd));
            advicesTargetPointcut.addAll(getMethods(targetElementAdvicesEnd));

            if ((advicesTargetPointcut.size() == sourceElementAdvices.size()) && (advicesTargetPointcut.containsAll(sourceElementAdvices))) {
                joinpointsTargetPoincut.add(joinpoint);
                updateEnds(targetElementJoinpointsEnd, joinpointsTargetPoincut);
                create = false;
                break;
            }
        }

        if (create) {
            AssociationRelationship pointcut = new AssociationRelationship(aspect, targetElement);
            pointcut.addPoincut("pointcut");
            architecture.addRelationship(pointcut);
            AssociationEnd targetElementJoinpointsEnd = getElementAssociationEnd(pointcut, targetElement);
            AssociationEnd targetElementAdvicesEnd = getElementAssociationEnd(pointcut, aspect);
            List<Method> joinpoints = new ArrayList<>();
            joinpoints.add(joinpoint);
            updateEnds(targetElementJoinpointsEnd, joinpoints);
            updateEnds(targetElementAdvicesEnd, sourceElementAdvices);
        }

    }

    public AssociationEnd getElementAssociationEnd(AssociationRelationship association, Element element) {
        if (association.getParticipants().get(0).getCLSClass().equals(element)) {
            return association.getParticipants().get(0);
        } else {
            association.getParticipants().get(1);
        }
        return null;
    }

    //retorna todos os relacionamentos pointcut da arquitetura
    public static List<AssociationRelationship> returnPointcuts(Architecture architecture) {
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

    public static List<Method> returnMethodsByString(String name, Element element) {
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
            String[] split = name.split(",");
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

    public static List<Method> getMethods(AssociationEnd associationEnd) {
        List<Method> methods = new ArrayList<>();
        String name = associationEnd.getName();
        Element element = associationEnd.getCLSClass();
        methods.addAll(returnMethodsByString(name, element));
        return methods;
    }

    public static void updateEnds(AssociationEnd associationEnd, List<Method> joinpoints) {
        String endName = "";
        List<Method> allMethods = new ArrayList<>();
        if (associationEnd.getCLSClass() instanceof Class) {
            allMethods.addAll(((Class) associationEnd.getCLSClass()).getAllMethods());
        } else {
            allMethods.addAll(((Interface) associationEnd.getCLSClass()).getOperations());
        }
        if (allMethods.containsAll(joinpoints)) {
            endName = "all";
        } else {
            for (Method joinpoint : joinpoints) {
                endName += joinpoint.getName() + ",";
            }
        }
        associationEnd.setName(endName);
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

    //metodo para verificar se a classe/interface destino possui um pointcut com a classe de origem
    public static List<AssociationRelationship> returnPointcutsTargetElement(Class aspect, Element targetElement) {
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
            if ((relationship instanceof AssociationRelationship)) {
                AssociationRelationship association = (AssociationRelationship) relationship;
                if (association.isPoincut() && (association.getParticipants().get(0).getCLSClass().equals(aspect) || association.getParticipants().get(1).getCLSClass().equals(aspect))) {
                    pointcuts.add(association);
                }
            }
        }
        return pointcuts;
    }
}
