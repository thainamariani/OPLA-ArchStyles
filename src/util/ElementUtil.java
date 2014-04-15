package util;

import arquitetura.helpers.UtilResources;
import arquitetura.representation.Architecture;
import arquitetura.representation.Concern;
import arquitetura.representation.Element;
import arquitetura.representation.Class;
import arquitetura.representation.Interface;
import arquitetura.representation.Package;
import arquitetura.representation.Method;
import arquitetura.representation.relationship.AssociationEnd;
import arquitetura.representation.relationship.AssociationRelationship;
import arquitetura.representation.relationship.Relationship;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;

public class ElementUtil {

    public static List<Relationship> getRelationships(Element element) {
        ArrayList<Relationship> relationships = new ArrayList<>();
        if (element instanceof arquitetura.representation.Class) {
            relationships.addAll(((arquitetura.representation.Class) element).getRelationships());
        } else if (element instanceof Interface) {
            relationships.addAll(((Interface) element).getRelationships());
        }
        return relationships;
    }

    public static List<Relationship> getRelationships(List<? extends Element> elements) {
        ArrayList<Relationship> relationships = new ArrayList<>();
        for (Element element : elements) {
            Set<Relationship> tempRelationships;
            if (element instanceof arquitetura.representation.Class) {
                tempRelationships = ((arquitetura.representation.Class) element).getRelationships();
            } else {
                tempRelationships = ((Interface) element).getRelationships();
            }
            for (Relationship relationship : tempRelationships) {
                if (!relationships.contains(relationship)) {
                    relationships.add(relationship);
                }
            }
        }
        return relationships;
    }

    public static boolean isTypeOf(Element child, Element parent) {
        boolean isType = false;
        for (Relationship relationship : ElementUtil.getRelationships(child)) {
            Element tempParent = RelationshipUtil.getImplementedInterface(relationship);
            if (tempParent == null) {
                tempParent = RelationshipUtil.getExtendedElement(relationship);
                if (tempParent == null) {
                    continue;
                }
            }
            if (!tempParent.equals(child)) {
                if (tempParent.equals(parent)) {
                    isType = true;
                } else {
                    isType = isTypeOf(tempParent, parent);
                }
            }
        }
        return isType;
    }

    public static List<Interface> getAllSuperInterfaces(Element child) {
        List<Interface> implementedInterfaces = new ArrayList<>();
        for (Relationship relationship : ElementUtil.getRelationships(child)) {
            Interface tempInterface = RelationshipUtil.getImplementedInterface(relationship);
            if (tempInterface != null && !tempInterface.equals(child)) {
                implementedInterfaces.add(tempInterface);
                List<Interface> parentInterfaces = getAllSuperInterfaces(tempInterface);
                for (Interface parentInterface : parentInterfaces) {
                    if (!implementedInterfaces.contains(parentInterface)) {
                        implementedInterfaces.add(parentInterface);
                    }
                }
                List<Element> parentSuperTypes = getAllExtendedElements(tempInterface);
                for (Element parentSuperType : parentSuperTypes) {
                    if (parentSuperType instanceof Interface && !implementedInterfaces.contains(parentSuperType)) {
                        implementedInterfaces.add((Interface) parentSuperType);
                    }
                }
            }
        }
        List<Element> allExtendedElements = getAllExtendedElements(child);
        for (Element extendedElement : allExtendedElements) {
            List<Interface> parentInterfaces = getAllSuperInterfaces(extendedElement);
            for (Interface parentInterface : parentInterfaces) {
                if (!implementedInterfaces.contains(parentInterface)) {
                    implementedInterfaces.add(parentInterface);
                }
            }
            if (child instanceof Interface && extendedElement instanceof Interface) {
                if (!implementedInterfaces.contains(extendedElement)) {
                    implementedInterfaces.add((Interface) extendedElement);
                }
            }
        }
        return implementedInterfaces;
    }

    public static List<Interface> getAllCommonInterfaces(List<Element> participants) {
        List<Interface> interfaces = new ArrayList<>();
        boolean first = true;
        for (Element participant : participants) {
            List<Interface> elementInterfaces = ElementUtil.getAllSuperInterfaces(participant);
            if (first) {
                interfaces.addAll(elementInterfaces);
                if (participant instanceof Interface) {
                    interfaces.add((Interface) participant);
                }
                first = false;
            } else {
                elementInterfaces = new ArrayList<>(elementInterfaces);
                if (participant instanceof Interface) {
                    elementInterfaces.add((Interface) participant);
                }
                interfaces = new ArrayList<>(CollectionUtils.intersection(interfaces, elementInterfaces));
            }
        }
        return interfaces;
    }

    public static List<Interface> getAllCommonSuperInterfaces(List<Element> participants) {
        List<Interface> interfaces = new ArrayList<>();
        for (Element participant : participants) {
            List<Interface> elementInterfaces = ElementUtil.getAllSuperInterfaces(participant);
            if (interfaces.isEmpty()) {
                interfaces.addAll(elementInterfaces);
            } else {
                interfaces = new ArrayList<>(CollectionUtils.intersection(interfaces, elementInterfaces));
            }
        }
        return interfaces;
    }

    public static List<Element> getAllExtendedElements(Element child) {
        List<Element> extendedElements = new ArrayList<>();
        for (Relationship relationship : ElementUtil.getRelationships(child)) {
            Element tempParent = RelationshipUtil.getExtendedElement(relationship);
            if (tempParent != null && !tempParent.equals(child)) {
                extendedElements.add(tempParent);
                List<Element> parentSuperTypes = getAllExtendedElements(tempParent);
                for (Element parentSuperType : parentSuperTypes) {
                    if (!extendedElements.contains(parentSuperType)) {
                        extendedElements.add(parentSuperType);
                    }
                }
            }
        }
        return extendedElements;
    }

    public static List<Element> getAllSubElements(Element parent) {
        List<Element> subElements = new ArrayList<>();
        for (Relationship relationship : ElementUtil.getRelationships(parent)) {
            Element tempChild = RelationshipUtil.getSubElement(relationship);
            if (tempChild != null && !tempChild.equals(parent)) {
                subElements.add(tempChild);
                List<Element> subElementSubTypes = getAllSubElements(tempChild);
                for (Element subElementSubType : subElementSubTypes) {
                    if (!subElements.contains(subElementSubType)) {
                        subElements.add(subElementSubType);
                    }
                }
            }
        }
        return subElements;
    }

    public static boolean isClassOrInterface(Element element) {
        return (element instanceof arquitetura.representation.Class || element instanceof Interface);
    }

    public static Set<Concern> getOwnAndMethodsCommonConcerns(List<Element> elements) {
        Set<Concern> commonConcerns = new HashSet<>();
        commonConcerns.addAll(getOwnAndMethodsConcerns(elements.get(0)));
        for (Element participant : elements) {
            commonConcerns = new HashSet<>(CollectionUtils.intersection(commonConcerns, getOwnAndMethodsConcerns(participant)));
        }
        return commonConcerns;
    }

    public static Set<Concern> getOwnAndMethodsCommonConcernsOfAtLeastTwoElements(List<Element> elements) {
        Set<Concern> commonConcerns = new HashSet<>();
        for (Element iElement : elements) {
            concernLoop:
            for (Concern concern : getOwnAndMethodsConcerns(iElement)) {
                if (!commonConcerns.contains(concern)) {
                    for (Element jElement : elements) {
                        if (!jElement.equals(iElement)) {
                            if (getOwnAndMethodsConcerns(jElement).contains(concern)) {
                                commonConcerns.add(concern);
                                continue concernLoop;
                            }
                        }
                    }
                }
            }
        }
        return commonConcerns;
    }

    public static Set<Concern> getOwnAndMethodsConcerns(List<Element> elements) {
        Set<Concern> commonConcerns = new HashSet<>();
        for (Element participant : elements) {
            commonConcerns.addAll(getOwnAndMethodsConcerns(participant));
        }
        return commonConcerns;
    }

    public static Set<Concern> getOwnAndMethodsConcerns(Element element) {
        Set<Concern> commonConcerns = new HashSet<>();
        commonConcerns.addAll(element.getOwnConcerns());
        for (Method method : MethodUtil.getAllMethodsFromElement(element)) {
            commonConcerns.addAll(method.getOwnConcerns());
        }
        return commonConcerns;
    }

    public static Set<Element> getAllAggregatedElements(Element element) {
        Set<Element> aggregatedElements = new HashSet<>();
        for (Relationship relationship : ElementUtil.getRelationships(element)) {
            if (relationship instanceof AssociationRelationship) {
                AssociationRelationship association = (AssociationRelationship) relationship;
                for (AssociationEnd end : association.getParticipants()) {
                    if (end.isAggregation() && !end.getCLSClass().equals(element)) {
                        aggregatedElements.add(end.getCLSClass());
                    }
                }
            }
        }
        return aggregatedElements;
    }

    public static HashMap<Concern, List<Element>> groupElementsByConcern(List<Element> elements) {
        HashMap<Concern, List<Element>> groupedElements = new HashMap<>();
        Set<Concern> ownAndMethodsCommonConcerns = getOwnAndMethodsConcerns(elements);
        for (Concern concern : ownAndMethodsCommonConcerns) {
            List<Element> concernElements = new ArrayList<>();
            for (Element element : elements) {
                Set<Concern> elementConcerns = getOwnAndMethodsConcerns(element);
                if (elementConcerns.contains(concern)) {
                    concernElements.add(element);
                }
            }
            groupedElements.put(concern, concernElements);
        }
        ArrayList<Element> nullList = ElementUtil.getElementsWithNoOwnConcernsAndWithAtLeastOneMethodWithNoConcerns(elements);
        if (!nullList.isEmpty()) {
            groupedElements.put(null, nullList);
        }
        return groupedElements;
    }

    public static ArrayList<Element> getElementsWithNoOwnConcernsAndWithAtLeastOneMethodWithNoConcerns(Iterable<Element> elements) {
        ArrayList<Element> nullArrayList = new ArrayList<>();
        elementLoop:
        for (Element element : elements) {
            if (element.getOwnConcerns().isEmpty()) {
                for (Method method : MethodUtil.getAllMethodsFromElement(element)) {
                    if (method.getOwnConcerns().isEmpty()) {
                        nullArrayList.add(element);
                        continue elementLoop;
                    }
                }
            }
        }
        return nullArrayList;
    }

    public static String getNameSpace(List<Element> elements) {
        HashMap<String, Integer> namespaceList = new HashMap<>();
        for (Element element : elements) {
            Integer namespaceCount = namespaceList.get(element.getNamespace());
            namespaceList.put(element.getNamespace(), namespaceCount == null ? 1 : namespaceCount + 1);
        }

        Integer max = -1;
        String namespace = "";
        for (Map.Entry<String, Integer> entry : namespaceList.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            if (value > max) {
                max = value;
                namespace = key;
            }
        }
        return namespace;
    }

    public static void implementInterface(List<Element> elements, Interface anInterface) {
        Collections.sort(elements, SubElementsComparator.getDescendingOrderer());
        for (Element participant : elements) {
            implementInterface(participant, anInterface);
        }
    }

    public static void implementInterface(Element child, Interface anInterface) {
        if (!ElementUtil.isTypeOf(child, anInterface)) {
            if (child instanceof arquitetura.representation.Class) {
                RelationshipUtil.createNewRealizationRelationship("implements", child, anInterface);
            }
        }

        if (ElementUtil.isTypeOf(child, anInterface) && child instanceof arquitetura.representation.Class) {
            arquitetura.representation.Class childClass = (arquitetura.representation.Class) child;
            if (!childClass.isAbstract()) {
                MethodArrayList childMethods = new MethodArrayList(new ArrayList<>(childClass.getAllMethods()));
                for (Method interfaceMethod : anInterface.getOperations()) {
                    if (!childMethods.containsSameSignature(interfaceMethod)) {
                        childClass.addExternalMethod(MethodUtil.cloneMethod(interfaceMethod));
                    }
                }
            }
        }
    }

    public static void extendClass(Element child, arquitetura.representation.Class aClass) {
        if (!ElementUtil.isTypeOf(child, aClass)) {
            if (child instanceof arquitetura.representation.Class) {
                RelationshipUtil.createNewGeneralizationRelationship(child, aClass);
            }
        }
        if (child instanceof arquitetura.representation.Class) {
            arquitetura.representation.Class childClass = (arquitetura.representation.Class) child;
            if (!childClass.isAbstract()) {
                MethodArrayList childMethods = new MethodArrayList(new ArrayList<>(childClass.getAllMethods()));
                for (Method classMethod : aClass.getAllMethods()) {
                    if (!childMethods.containsSameSignature(classMethod)) {
                        Method cloneMethod = MethodUtil.cloneMethod(classMethod);
                        cloneMethod.setAbstract(false);
                        childClass.addExternalMethod(cloneMethod);
                    }
                }
            }
        }
    }

    public static void verifyAndRemoveRequiredInterface(Element client, Element supplier) {
        if (client instanceof arquitetura.representation.Class && supplier instanceof Interface) {
            for (Relationship relationship : ElementUtil.getRelationships(client)) {
                Element usedElementFromRelationship = RelationshipUtil.getUsedElementFromRelationship(relationship);
                if (supplier.equals(usedElementFromRelationship)) {
                    return;
                }
            }
            ((arquitetura.representation.Class) client).removeRequiredInterface((Interface) supplier);
        }
    }

    public static void verifyAndRemoveImplementedInterface(Element client, Element supplier) {
        if (client instanceof arquitetura.representation.Class && supplier instanceof Interface) {
            if (!getAllSuperInterfaces(client).contains(supplier)) {
                ((arquitetura.representation.Class) client).removeImplementedInterface((Interface) supplier);
            }
        }
    }

    public static void addRequiredInterface(Element client, Element supplier) {
        if (client instanceof arquitetura.representation.Class && supplier instanceof Interface) {
            ((arquitetura.representation.Class) client).addRequiredInterface((Interface) supplier);
        }
    }

    public static void addImplementedInterface(Element client, Element supplier) {
        if (client instanceof arquitetura.representation.Class && supplier instanceof Interface) {
            ((arquitetura.representation.Class) client).addImplementedInterface((Interface) supplier);
        }
    }

    public static List<Element> getAllSuperElements(Element element) {
        return new ArrayList<>(CollectionUtils.union(getAllExtendedElements(element), getAllSuperInterfaces(element)));
    }

    //Métodos Thainá
    //cria uma lista com todos as classes e interfaces existentes no pacote, e também com o próprio pacote
    public static Set<Element> selectPackageClassesInterfaces(Package p) {
        Set<Element> elements = new HashSet<>();
        for (Class c : p.getAllClasses()) {
            elements.add(c);
        }
        for (Interface i : p.getAllInterfaces()) {
            elements.add(i);
        }
        for (Package pn : p.getNestedPackages()) {
            for (Class cn : pn.getAllClasses()) {
                elements.add(cn);
            }
            for (Interface in : pn.getAllInterfaces()) {
                elements.add(in);
            }
            elements.add(pn);
        }
        elements.add(p);
        return elements;
    }

    public static Set<Relationship> getRelationshipByElement(Element element) {
        Set<Relationship> relationships = new HashSet<>();
        if (element instanceof Class) {
            relationships = ((Class) element).getRelationships();
        } else if (element instanceof Interface) {
            relationships = ((Interface) element).getRelationships();
        } else {
            relationships = ((Package) element).getRelationships();
        }
        return relationships;
    }

    public static Package getPackage(Element element, Architecture architecture) {
        Package pckage = null;
        if ((element instanceof Class) || (element instanceof Interface)) {
            pckage = architecture.findPackageByName(UtilResources.extractPackageName(element.getNamespace()));
        } else if (element instanceof Package) {
            pckage = (Package) element;
        }
        return pckage;
    }

    private ElementUtil() {
    }

}
