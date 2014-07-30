/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import arquitetura.exceptions.ConcernNotFoundException;
import arquitetura.helpers.UtilResources;
import arquitetura.representation.Architecture;
import arquitetura.representation.Attribute;
import arquitetura.representation.Concern;
import arquitetura.representation.Element;
import arquitetura.representation.Class;
import arquitetura.representation.Interface;
import arquitetura.representation.Method;
import arquitetura.representation.Variability;
import arquitetura.representation.Variant;
import arquitetura.representation.VariationPoint;
import arquitetura.representation.Package;
import arquitetura.representation.relationship.AssociationRelationship;
import arquitetura.representation.relationship.GeneralizationRelationship;
import arquitetura.representation.relationship.RealizationRelationship;
import arquitetura.representation.relationship.Relationship;
import identification.ClientServerIdentification;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import jmetal.problems.OPLA;
import jmetal.util.PseudoRandom;
import pojo.Client;
import pojo.Layer;
import pojo.Server;
import pojo.Style;

/**
 *
 * @author Thainá
 */
public class OperatorUtil {

    //add por thaina
    public static Layer findPackageLayer(List<Layer> layers, Package sourcePackage) {
        for (Layer layer : layers) {
            for (Package pac : layer.getPackages()) {
                if (pac == sourcePackage) {
                    return layer;
                }
            }
        }
        return null;
    }

    //--------------------------------------------------------------------------
    //método para verificar se algum dos relacionamentos recebidos é generalização
    public static boolean searchForGeneralizations(Class cls) {
        for (Relationship relationship : cls.getRelationships()) {
            if (relationship instanceof GeneralizationRelationship) {
                if (((GeneralizationRelationship) relationship).getChild().equals(cls) || ((GeneralizationRelationship) relationship).getParent().equals(cls)) {
                    return true;
                }
            }
        }
        return false;
    }

    //Add por Édipo
    public static void createAssociation(Architecture arch, Class targetClass, Class sourceClass) {
        arch.addRelationship(new AssociationRelationship(targetClass, sourceClass));
    }

    public static void removeClassesInPatternStructureFromArray(List<Class> ClassesComp) {
        for (int i = 0; i < ClassesComp.size(); i++) {
            Class klass = ClassesComp.get(i);
            if (klass.getPatternsOperations().hasPatternApplied()) {
                ClassesComp.remove(i);
                i--;
            }
        }
    }

    public static void removeInterfacesInPatternStructureFromArray(List<Interface> InterfacesSourceComp) {
        for (int i = 0; i < InterfacesSourceComp.size(); i++) {
            Interface anInterface = InterfacesSourceComp.get(i);
            if (anInterface.getPatternsOperations().hasPatternApplied()) {
                InterfacesSourceComp.remove(i);
                i--;
            }
        }
    }

    public static void moveMethodToNewClass(Architecture arch, Class sourceClass, Method targetMethod, Class newClass) throws Exception {
        sourceClass.moveMethodToClass(targetMethod, newClass);
        //if (targetMethod.isAbstract()) targetMethod.setAbstract(false);
        for (Concern con : targetMethod.getOwnConcerns()) {
            newClass.addConcern(con.getName());
        }
        createAssociation(arch, newClass, sourceClass);
    }

    //	public static void moveMethodAllComponents(Architecture arch, Class sourceClass, List<Method> MethodsClass, Class newClass) throws JMException {
    //		Method targetMethod = randomObject (MethodsClass);
    //		sourceClass.moveMethodToClass(targetMethod, newClass);
    //		//if (targetMethod.isAbstract()) targetMethod.setAbstract(false);
    //		for (Concern con: targetMethod.getOwnConcerns()){
    //			try {
    //				newClass.addConcern(con.getName());
    //			} catch (ConcernNotFoundException e) {
    //				e.printStackTrace();
    //			}
    //		}
    //		AssociationRelationship newRelationship = new AssociationRelationship(newClass, sourceClass);
    //		arch.getAllRelationships().add(newRelationship);
    //	}
    public static void moveAttributeToNewClass(Architecture arch, Class sourceClass, Attribute targetAttribute, Class newClass) throws Exception {
        sourceClass.moveAttributeToClass(targetAttribute, newClass);
        for (Concern con : targetAttribute.getOwnConcerns()) {
            newClass.addConcern(con.getName());
        }
        createAssociation(arch, newClass, sourceClass);

    }

    public static List<Package> searchComponentsAssignedToConcern(Concern concern, List<Package> allComponents) {
        final List<Package> allComponentsAssignedToConcern = new ArrayList<Package>();
        for (Package component : allComponents) {
            final Set<Concern> numberOfConcernsForPackage = getNumberOfConcernsFor(component);
            if (numberOfConcernsForPackage.size() == 1 && (numberOfConcernsForPackage.contains(concern))) {
                allComponentsAssignedToConcern.add(component);
            }
        }
        return allComponentsAssignedToConcern;
    }

    public static Set<Concern> getNumberOfConcernsFor(Package pkg) {
        final Set<Concern> listOfOwnedConcern = new HashSet<Concern>();

        for (Class klass : pkg.getAllClasses()) {
            listOfOwnedConcern.addAll(klass.getOwnConcerns());
        }
        for (Interface inte : pkg.getAllInterfaces()) {
            listOfOwnedConcern.addAll(inte.getOwnConcerns());
        }

        return listOfOwnedConcern;
    }

    public static void modularizeConcernInComponent(List<? extends Style> styles, Style style, Package targetComponent, Concern concern, Architecture arch) {
        try {
            List<Package> allComponents = new ArrayList<>();
            if (style instanceof Server) {
                for (Server servidor : ClientServerIdentification.getLISTSERVERS()) {
                    allComponents.addAll(servidor.getPackages());
                }
            } else {
                allComponents = style.getPackages();
            }

            Iterator<Package> itrComp = allComponents.iterator();
            while (itrComp.hasNext()) {
                Package comp = itrComp.next();
                if (!comp.equals(targetComponent)) {
                    //&& checkSameLayer(comp, targetComponent)) {
                    final Set<Interface> allInterfaces = new HashSet<Interface>(comp.getAllInterfaces());
                    //allInterfaces.addAll(comp.getImplementedInterfaces());
                    Iterator<Interface> itrInterface = allInterfaces.iterator();
                    while (itrInterface.hasNext()) {
                        Interface interfaceComp = itrInterface.next();
                        if (interfaceComp.getOwnConcerns().size() == 1 && interfaceComp.containsConcern(concern)) {
                            moveInterfaceToComponent((List<Style>) styles, style, interfaceComp, targetComponent, comp, arch, concern); // EDIPO TESTADO
                        } else if (!interfaceComp.getPatternsOperations().hasPatternApplied()) {
                            List<Method> operationsInterfaceComp = new ArrayList<Method>(interfaceComp.getOperations());
                            Iterator<Method> itrOperation = operationsInterfaceComp.iterator();
                            while (itrOperation.hasNext()) {
                                Method operation = itrOperation.next();
                                if (operation.getOwnConcerns().size() == 1 && operation.containsConcern(concern)) {
                                    moveOperationToComponent((List<Style>) styles, style, operation, interfaceComp, targetComponent, comp, arch, concern);
                                }
                            }
                        }
                    }

                    allInterfaces.clear();
                    final List<Class> allClasses = new ArrayList<Class>(comp.getAllClasses());
                    Iterator<Class> ItrClass = allClasses.iterator();
                    while (ItrClass.hasNext()) {
                        Class classComp = ItrClass.next();
                        if (comp.getAllClasses().contains(classComp)) {
                            if ((classComp.getOwnConcerns().size() == 1) && (classComp.containsConcern(concern))) {
                                if (!searchForGeneralizations(classComp)) //realiza a muta����o em classe que n��oo est��o numa hierarquia de heran��a
                                {
                                    moveClassToComponent(classComp, targetComponent, comp, arch, concern);
                                } else {
                                    moveHierarchyToComponent(classComp, targetComponent, comp, arch, concern); //realiza a muta����o em classes est��o numa hierarquia de herarquia
                                }
                            } else {
                                if (!searchForGeneralizations(classComp)) {
                                    if (!isVarPointOfConcern(arch, classComp, concern) && !isVariantOfConcern(arch, classComp, concern)) {
                                        final List<Attribute> attributesClassComp = new ArrayList<Attribute>(classComp.getAllAttributes());
                                        Iterator<Attribute> irtAttribute = attributesClassComp.iterator();
                                        while (irtAttribute.hasNext()) {
                                            Attribute attribute = irtAttribute.next();
                                            if (attribute.getOwnConcerns().size() == 1 && attribute.containsConcern(concern)) {
                                                moveAttributeToComponent(attribute, classComp, targetComponent, comp, arch, concern);
                                            }
                                        }
                                        attributesClassComp.clear();
                                        if (!classComp.getPatternsOperations().hasPatternApplied()) {
                                            final List<Method> methodsClassComp = new ArrayList<Method>(classComp.getAllMethods());
                                            Iterator<Method> irtMethod = methodsClassComp.iterator();
                                            while (irtMethod.hasNext()) {
                                                Method method = irtMethod.next();
                                                if (method.getOwnConcerns().size() == 1 && method.containsConcern(concern)) {
                                                    moveMethodToComponent(method, classComp, targetComponent, comp, arch, concern);
                                                }
                                            }
                                            methodsClassComp.clear();
                                        }
                                    }
                                }
                            }
                        }
                    }
                    allClasses.clear();
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static void moveClassToComponent(Class classComp, Package targetComp, Package sourceComp, Architecture architecture, Concern concern) {
        sourceComp.moveClassToPackage(classComp, targetComp);
    }

    public static void moveAttributeToComponent(Attribute attribute, Class classComp, Package targetComp, Package sourceComp, Architecture architecture, Concern concern) throws ConcernNotFoundException {
        final Class targetClass = findOrCreateClassWithConcern(targetComp, concern);
        classComp.moveAttributeToClass(attribute, targetClass);
        createAssociation(architecture, targetClass, classComp);
    }

    public static void moveMethodToComponent(Method method, Class classComp, Package targetComp, Package sourceComp, Architecture architecture, Concern concern) throws ConcernNotFoundException {
        final Class targetClass = findOrCreateClassWithConcern(targetComp, concern);
        classComp.moveMethodToClass(method, targetClass);
        createAssociation(architecture, targetClass, classComp);
    }

    //add por Édipo
    public static Class findOrCreateClassWithConcern(Package targetComp, Concern concern) throws ConcernNotFoundException {
        Class targetClass = null;
        for (Class cls : targetComp.getAllClasses()) {
            if (cls.containsConcern(concern)) {
                targetClass = cls;
            }
        }

        if (targetClass == null) {
            targetClass = targetComp.createClass("Class" + OPLA.contClass_++, false);
            targetClass.addConcern(concern.getName());
        }
        return targetClass;
    }

    public static void moveInterfaceToComponent(List<Style> styles, Style style, Interface interfaceComp, Package targetComp, Package sourceComp, Architecture architecture, Concern concernSelected) {
        if (!sourceComp.moveInterfaceToPackage(interfaceComp, targetComp)) {
            architecture.moveElementToPackage(interfaceComp, targetComp);
        }

        for (Element implementor : interfaceComp.getImplementors()) {
            if (implementor instanceof Package) {
                if (targetComp.getAllClasses().size() == 1) {
                    final Class klass = targetComp.getAllClasses().iterator().next();
                    for (Concern concern : klass.getOwnConcerns()) {
                        if (interfaceComp.containsConcern(concern)) {
                            architecture.removeImplementedInterface(interfaceComp, sourceComp);
                            addExternalInterface(targetComp, architecture, interfaceComp);
                            addImplementedInterface(targetComp, architecture, interfaceComp, klass);
                        }
                    }
                    return;
                } else if (targetComp.getAllClasses().size() > 1) {
                    final List<Class> targetClasses = allClassesWithConcerns(concernSelected, targetComp.getAllClasses());
                    final Class klass = randonClass(targetClasses);
                    architecture.removeImplementedInterface(interfaceComp, sourceComp);
                    addExternalInterface(targetComp, architecture, interfaceComp);
                    addImplementedInterface(targetComp, architecture, interfaceComp, klass);
                    return;
                } else {
                    //Busca na arquitetura como um todo
                    //Alterado: busca na mesma camada e na camada superior
                    List<arquitetura.representation.Package> packages = new ArrayList<>();
                    if (style instanceof Layer) {
                        packages.addAll(style.getPackages());
                        if (((Layer) style).getNumero() < styles.size()) {
                            packages.addAll(StyleUtil.returnPackagesLayerNumber(((Layer) style).getNumero() + 1, styles));
                        }
                    } else if (style instanceof Server) {
                        packages.addAll(architecture.getAllPackages());
                    } else if (style instanceof Client) {
                        packages.addAll(style.getPackages());
                    }

                    final List<Class> targetClasses = new ArrayList<>();
                    for (Package pac : packages) {
                        targetClasses.addAll(allClassesWithConcerns(concernSelected, pac.getAllClasses()));
                    }
                    Class klass = null;
                    if (!targetClasses.isEmpty()) {
                        klass = randonClass(targetClasses);
                        architecture.removeImplementedInterface(interfaceComp, sourceComp);
                        addExternalInterface(targetComp, architecture, interfaceComp);
                        addImplementedInterface(targetComp, architecture, interfaceComp, klass);
                    }
                }
            }
        }
    }

    public static Class randonClass(List<Class> targetClasses) {
        Collections.shuffle(targetClasses);
        Class randonKlass = targetClasses.get(0);
        return randonKlass;
    }

    /**
     * Retorna todas as classes que tiverem algum dos concerns presentes na lista ownConcerns.
     *
     * @param ownConcerns
     * @param allClasses
     * @return
     */
    public static List<Class> allClassesWithConcerns(Concern c, Set<Class> allClasses) {
        List<Class> klasses = new ArrayList<Class>();
        for (Class klass : allClasses) {
            for (Concern concernKlass : klass.getOwnConcerns()) {
                if (concernKlass.getName().equalsIgnoreCase(c.getName())) {
                    klasses.add(klass);
                }
            }
        }
        return klasses;
    }

    public static void moveOperationToComponent(List<Style> styles, Style style, Method operation, Interface sourceInterface, Package targetComp, Package sourceComp, Architecture architecture, Concern concern) throws ConcernNotFoundException {
        Interface targetInterface = null;
        targetInterface = searchForInterfaceWithConcern(concern, targetComp);

        if (targetInterface == null) {
            targetInterface = targetComp.createInterface("Interface" + OPLA.contInt_++);
            sourceInterface.moveOperationToInterface(operation, targetInterface);
            targetInterface.addConcern(concern.getName());
        } else {
            sourceInterface.moveOperationToInterface(operation, targetInterface);
        }

        addRelationship(styles, style, sourceInterface, targetComp, sourceComp, architecture, concern, targetInterface);
    }

    public static void addRelationship(List<Style> styles, Style style, Interface sourceInterface, Package targetComp, Package sourceComp, Architecture architecture, Concern concern, Interface targetInterface) {
        for (Element implementor : sourceInterface.getImplementors()) {
            // Se quem estiver implementando a interface que teve a operacao movida for um pacote.
            if (implementor instanceof Package) {
                /**
                 * Verifica se o pacote tem somente um classe, recupera a mesma e verifica se a interface destino (targetInterface) possui algum interesse da classe recuperada. Caso tiver, remove implemented interface (sourceInterface) de sourceComp. Adiciona a interface tergetInterface em seu pacote ou na arquitetura Verifica se já existe um relacionamento de realização entre targetInterface e klass, caso não tiver adiciona targetInterface como sendo implemenda por klass.
                 */
                if (targetComp.getAllClasses().size() == 1) {
                    final Class klass = targetComp.getAllClasses().iterator().next();
                    for (Concern c : klass.getOwnConcerns()) {
                        if (targetInterface.containsConcern(c)) {
                            architecture.removeImplementedInterface(sourceInterface, sourceComp);
                            addExternalInterface(targetComp, architecture, targetInterface);
                            addImplementedInterface(targetComp, architecture, targetInterface, klass);
                            return;
                        }
                    }

                    /**
                     * Caso o pacote destino tiver mais de uma classe. Busca dentre essas classes todas com o interesse em questão (concern), e seleciona um aleatoriamente. Remove implemented interface (sourceInterface) de sourceComp. Adiciona a interface tergetInterface em seu pacote ou na arquitetura Verifica se já existe um relacionamento de realização entre targetInterface e klass, caso não tiver adiciona targetInterface como sendo implemenda por klass.
                     */
                } else if (targetComp.getAllClasses().size() > 1) {
                    final List<Class> targetClasses = allClassesWithConcerns(concern, targetComp.getAllClasses());
                    final Class klass = randonClass(targetClasses);
                    architecture.removeImplementedInterface(sourceInterface, sourceComp);
                    addExternalInterface(targetComp, architecture, targetInterface);
                    addImplementedInterface(targetComp, architecture, targetInterface, klass);
                    return;
                } else {
                    /**
                     * Caso o pacote for vazio, faz um busca nas classes da arquitetura como um todo.
                     */
                    //Alterado: busca na mesma camada e na camada superior
                    List<arquitetura.representation.Package> packages = new ArrayList<>();
                    if (style instanceof Layer) {
                        packages.addAll(style.getPackages());
                        if (((Layer) style).getNumero() < styles.size()) {
                            packages.addAll(StyleUtil.returnPackagesLayerNumber(((Layer) style).getNumero() + 1, styles));
                        }
                    } else if (style instanceof Server) {
                        packages.addAll(architecture.getAllPackages());
                    } else if (style instanceof Client) {
                        packages.addAll(style.getPackages());
                    }

                    final List<Class> targetClasses = new ArrayList<>();
                    for (Package pac : packages) {
                        targetClasses.addAll(allClassesWithConcerns(concern, pac.getAllClasses()));
                    }
                    Class klass = null;
                    //final List<Class> targetClasses = allClassesWithConcerns(concern, architecture.getAllClasses());
                    if (!targetClasses.isEmpty()) {
                        klass = randonClass(targetClasses);
                        architecture.removeImplementedInterface(sourceInterface, sourceComp);
                        addExternalInterface(targetComp, architecture, targetInterface);
                        addImplementedInterface(targetComp, architecture, targetInterface, klass);
                    }
                }
            }

            /**
             * Recupera quem estava implementando a interface que teve a operacao movida e cria uma realizacao entre a interface que recebeu a operacao (targetInterface) e quem tava implementando a interface que teve a operacao movida (sourceInterface).
             *
             */
            if (implementor instanceof Class) {
                architecture.removeImplementedInterface(sourceInterface, sourceComp);
                addExternalInterface(targetComp, architecture, targetInterface);
                addImplementedInterface(targetComp, architecture, targetInterface, (Class) implementor);
            }
        }
    }

    public static void addImplementedInterface(Package targetComp, Architecture architecture, Interface targetInterface, Class klass) {
        if (!packageTargetHasRealization(targetComp, targetInterface)) {
            architecture.addImplementedInterface(targetInterface, klass);
        }
    }

    public static void addExternalInterface(Package targetComp, Architecture architecture, Interface targetInterface) {
        final String packageNameInterface = UtilResources.extractPackageName(targetInterface.getNamespace().trim());
        if (packageNameInterface.equalsIgnoreCase("model")) {
            architecture.addExternalInterface(targetInterface);
        } else {
            targetComp.addExternalInterface(targetInterface);
        }
    }

    public static boolean packageTargetHasRealization(Package targetComp, Interface targetInterface) {
        for (Relationship r : targetComp.getRelationships()) {
            if (r instanceof RealizationRelationship) {
                final RealizationRelationship realization = (RealizationRelationship) r;
                if (realization.getSupplier().equals(targetInterface)) {
                    return true;
                }
            }
        }

        return false;
    }

//	//Édipo
    //	public static void addConcernToNewInterface(Concern concern, Interface targetInterface, Interface sourceInterface) {
    //		Set<Concern> interfaceConcerns = sourceInterface.getOwnConcerns();
    //		try {
    //			for(Concern c : interfaceConcerns)
    //				targetInterface.addConcern(c.getName());
    //		} catch (ConcernNotFoundException e) {
    //			e.printStackTrace();
    //		}
    //
    //		for(Method operation : sourceInterface.getOperations()){
    //			Set<Concern> operationConcerns = operation.getOwnConcerns();
    //			for(Method o : targetInterface.getOperations()){
    //				for(Concern c : operationConcerns){
    //					try {
    //						o.addConcern(c.getName());
    //					} catch (ConcernNotFoundException e) {
    //						e.printStackTrace();
    //					}
    //				}
    //			}
    //
    //		}
    //	}
    //Édipo Método
    public static Interface searchForInterfaceWithConcern(Concern concern, Package targetComp) {
        for (Interface itf : targetComp.getImplementedInterfaces()) {
            if (itf.containsConcern(concern)) {
                return itf;
            }
        }

        for (Interface itf : targetComp.getAllInterfaces()) {
            if (itf.containsConcern(concern)) {
                return itf;
            }
        }

        return null;
    }

    //-------------------------------------------------------------------------------------------------
    public static <T> T randomObject(List<T> allObjects) {
        int numObjects = allObjects.size();
        int key;
        T object;
        if (numObjects == 0) {
            object = null;
        } else {
            key = PseudoRandom.randInt(0, numObjects - 1);
            object = allObjects.get(key);
        }
        return object;
    }

    //-------------------------------------------------------------------------------------------------
    //Thelma: método adicionado para verificar se os componentes nos quais as mutacoes serao realizadas estao na mesma camada da arquitetura
//    public static boolean checkSameLayer(Package source, Package target) {
//        boolean sameLayer = false;
//        if ((source.getName().endsWith("Mgr") && target.getName().endsWith("Mgr"))
//                || (source.getName().endsWith("Ctrl") && target.getName().endsWith("Ctrl"))
//                || (source.getName().endsWith("GUI") && target.getName().endsWith("GUI"))) {
//            sameLayer = true;
//        }
//        return sameLayer;
//    }

    //-------------------------------------------------------------------------------------------------
    //Thelma: método adicionado para retornar o sufixo do nome do componente
    public static String getSuffix(Package comp) {
        String suffix;
        if (comp.getName().endsWith("Mgr")) {
            suffix = "Mgr";
        } else if (comp.getName().endsWith("Ctrl")) {
            suffix = "Ctrl";
        } else if (comp.getName().endsWith("GUI")) {
            suffix = "GUI";
        } else {
            suffix = "";
        }
        return suffix;
    }

    //-------------------------------------------------------------------------------------------------
    //Thelma: método adicionado para verificar se a classe tem uma variabilidade relativa ao concern
    public static boolean isVarPointOfConcern(Architecture arch, Class cls, Concern concern) {
        boolean isVariationPointConcern = false;
        Collection<Variability> variabilities = arch.getAllVariabilities();
        for (Variability variability : variabilities) {
            VariationPoint varPoint = variability.getVariationPoint();
            if (varPoint != null) {
                Class classVP = (Class) varPoint.getVariationPointElement();
                if (classVP.equals(cls) && variability.getName().equals(concern.getName())) {
                    isVariationPointConcern = true;
                }
            }
        }
        return isVariationPointConcern;
    }

    //-------------------------------------------------------------------------------------------------
    //Thelma: método adicionado para verificar se a classe é variant de uma variabilidade relativa ao concern
    public static boolean isVariantOfConcern(Architecture arch, Class cls, Concern concern) {
        boolean isVariantConcern = false;
        Collection<Variability> variabilities = arch.getAllVariabilities();
        for (Variability variability : variabilities) {
            VariationPoint varPoint = variability.getVariationPoint();
            if (varPoint != null) {
                for (Variant variant : varPoint.getVariants()) {
                    if (variant.getVariantElement().equals(cls) && variability.getName().equals(concern.getName())) {
                        isVariantConcern = true;
                    }
                }
            } else {
                if (cls.getVariantType() != null) {
                    if (cls.getVariantType().equalsIgnoreCase("optional")) {
                        isVariantConcern = true;
                    }
                }
            }
        }
        variabilities.clear();
        return isVariantConcern;
    }

    /**
     * metodo que move a hierarquia de classes para um outro componente que esta modularizando o interesse concern
     *
     *
     * @param classComp - Classe selecionada
     * @param targetComp - Pacote destino
     * @param sourceComp - Pacote de origem
     * @param architecture - arquiteutra
     * @param concern - interesse sendo modularizado
     */
    public static void moveHierarchyToComponent(Class classComp, Package targetComp, Package sourceComp, Architecture architecture, Concern concern) {
        architecture.forGeneralization().moveGeneralizationToPackage(getGeneralizationRelationshipForClass(classComp), targetComp);
    }

    //EDIPO Identifica quem é o parent para a classComp
    /**
     * Dado um {@link Element} retorna a {@link GeneralizationRelationship} no qual o mesmo pertence.
     *
     * @param element
     * @return {@link GeneralizationRelationship}
     */
    public static GeneralizationRelationship getGeneralizationRelationshipForClass(Element element) {
        for (Relationship r : ((Class) element).getRelationships()) {
            if (r instanceof GeneralizationRelationship) {
                GeneralizationRelationship g = (GeneralizationRelationship) r;
                if (g.getParent().equals(element) || (g.getChild().equals(element))) {
                    return g;
                }
            }
        }
        return null;
    }

    // verificar se a classe é variant de uma variabilidade
    public static boolean isOptional(Architecture arch, Class cls) {
        boolean isOptional = false;
        if (cls.getVariantType() != null) {
            if (cls.getVariantType().toString().equalsIgnoreCase("optional")) {
                return true;
            }
        }
        return isOptional;
    }
    //-------------------------------------------------------------------------------------------------
    // verificar se a classe é variant de uma variabilidade

    public static boolean isVariant(Architecture arch, Class cls) {
        boolean isVariant = false;
        Collection<Variability> variabilities = arch.getAllVariabilities();
        for (Variability variability : variabilities) {
            VariationPoint varPoint = variability.getVariationPoint();
            if (varPoint != null) {
                for (Variant variant : varPoint.getVariants()) {
                    if (variant.getVariantElement().equals(cls)) {
                        isVariant = true;
                    }
                }
            }
        }
        return isVariant;
    }

    public static boolean isVarPoint(Architecture arch, Class cls) {
        boolean isVariationPoint = false;
        Collection<Variability> variabilities = arch.getAllVariabilities();
        for (Variability variability : variabilities) {
            VariationPoint varPoint = variability.getVariationPoint();
            if (varPoint != null) {
                Class classVP = (Class) varPoint.getVariationPointElement();
                if (classVP.equals(cls)) {
                    isVariationPoint = true;
                }
            }
        }
        return isVariationPoint;
    }

    // Thelma - Dez2013 método adicionado
    // verify if the architecture contains a valid PLA design, i.e., if there is not any interface without relationships in the architecture.
    public static boolean isValidSolution(Architecture solution) {
        boolean isValid = true;
        List<Interface> allInterfaces = new ArrayList<Interface>(solution.getAllInterfaces());
        if (!allInterfaces.isEmpty()) {
            for (Interface itf : allInterfaces) {
                if ((itf.getImplementors().isEmpty()) && (itf.getDependents().isEmpty()) && (!itf.getOperations().isEmpty())) {
                    return false;
                }
            }
        }
        return isValid;
    }

}
