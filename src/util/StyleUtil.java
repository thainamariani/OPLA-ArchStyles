/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

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

    //falta testar
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
}
