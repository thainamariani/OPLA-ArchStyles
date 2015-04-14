package project.util;

import arquitetura.exceptions.ConcernNotFoundException;
import arquitetura.representation.Concern;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.Method;
import arquitetura.representation.ParameterMethod;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.collections4.CollectionUtils;

public class MethodUtil {

    public static Set<Method> getMethodsFromElement(Element element) {
        Set<Method> iMethods;
        if (element instanceof arquitetura.representation.Class) {
            arquitetura.representation.Class iClass = (arquitetura.representation.Class) element;
            iMethods = iClass.getAllMethods();
        } else if (element instanceof Interface) {
            Interface iInterface = (Interface) element;
            iMethods = iInterface.getOperations();
        } else {
            return null;
        }
        return iMethods;
    }

    public static List<Method> getAllMethodsFromElement(Element element) {
        List<Method> iMethods = new ArrayList<>();
        if (element instanceof arquitetura.representation.Class) {
            arquitetura.representation.Class iClass = (arquitetura.representation.Class) element;
            iMethods.addAll(iClass.getAllMethods());
        } else if (element instanceof Interface) {
            Interface iInterface = (Interface) element;
            iMethods.addAll(iInterface.getOperations());
        }
        List<Element> parents = ElementUtil.getAllExtendedElements(element);
        for (Element parent : parents) {
            if (parent.getClass().equals(element.getClass())) {
                Set<Method> parentMethods = getMethodsFromElement(parent);
                for (Method parentMethod : parentMethods) {
                    if (!iMethods.contains(parentMethod)) {
                        iMethods.add(parentMethod);
                    }
                }
            }
        }
        return iMethods;
    }

    public static List<Method> getAllMethodsFromSetOfElements(List<Element> elements) {
        MethodArrayList methods = new MethodArrayList();
        for (Element element : elements) {
            MethodArrayList elementMethods = new MethodArrayList(getAllMethodsFromElement(element));
            for (Method elementMethod : elementMethods) {
                if (!methods.contains(elementMethod)) {
                    methods.add(cloneMethod(elementMethod));
                } else {
                    Method tempMethod = methods.get(methods.indexOf(elementMethod));
                    mergeMethodsToMethodA(tempMethod, elementMethod);
                }
            }
        }
        return methods;
    }

    public static List<Method> getAllMethodsFromSetOfElementsByConcern(List<Element> elements, Concern concern) {
        MethodArrayList methods = new MethodArrayList();
        for (Element element : elements) {
            MethodArrayList elementMethods = new MethodArrayList(getAllMethodsFromElement(element));
            for (Method elementMethod : elementMethods) {
                if (concern == null
                        ? (element.getOwnConcerns().isEmpty() && elementMethod.getOwnConcerns().isEmpty())
                        : (element.getOwnConcerns().contains(concern) || elementMethod.getOwnConcerns().contains(concern))) {
                    if (!methods.contains(elementMethod)) {
                        methods.add(cloneMethod(elementMethod));
                    } else {
                        Method tempMethod = methods.get(methods.indexOf(elementMethod));
                        mergeMethodsToMethodA(tempMethod, elementMethod);
                    }
                }
            }
        }
        return methods;
    }

    public static List<Method> createMethodsFromSetOfElements(List<Element> elements) {
        MethodArrayList methods = new MethodArrayList();
        for (Element element : elements) {
            MethodArrayList methodsFromElement = new MethodArrayList(getAllMethodsFromElement(element));
            methodFor:
            for (Method elementMethod : methodsFromElement) {
                Method clonedMethod = cloneMethod(elementMethod);
                int count = 1;
                String name = clonedMethod.getName();
                while (methods.containsSameName(clonedMethod)) {
                    if (methods.contains(clonedMethod)) {
                        Method method = methods.get(methods.indexOf(clonedMethod));
                        mergeMethodsToMethodA(method, clonedMethod);
                        continue methodFor;
                    }
                    count++;
                    clonedMethod.setName(name + Integer.toString(count));
                }
                methods.add(clonedMethod);
            }
        }
        return methods;
    }

    public static List<Method> createMethodsFromSetOfElementsByConcern(List<Element> elements, Concern concern) {
        MethodArrayList methods = new MethodArrayList();
        MethodArrayList methodsFromElements = new MethodArrayList(getAllMethodsFromSetOfElementsByConcern(elements, concern));
        methodFor:
        for (Method elementMethod : methodsFromElements) {
            Method clonedMethod = cloneMethod(elementMethod);
            int count = 1;
            String name = clonedMethod.getName();
            while (methods.containsSameName(clonedMethod)) {
                if (methods.contains(clonedMethod)) {
                    Method method = methods.get(methods.indexOf(clonedMethod));
                    mergeMethodsToMethodA(method, clonedMethod);
                    continue methodFor;
                }
                count++;
                clonedMethod.setName(name + Integer.toString(count));
            }
            methods.add(clonedMethod);
        }
        return methods;
    }

    public static Method cloneMethod(Method method) {
        Method newMethod = new Method(method.getName(), method.getReturnType(), "", method.isAbstract(), UUID.randomUUID().toString());
        newMethod.getParameters().addAll(method.getParameters());
        for (Concern concern : method.getOwnConcerns()) {
            try {
                newMethod.addConcern(concern.getName());
            } catch (ConcernNotFoundException ex) {
                Logger.getLogger(MethodUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        newMethod.setNamespace(method.getNamespace());
        return newMethod;
    }

    public static Set<Method> cloneMethods(Set<Method> methodsToBeCloned) {
        Set<Method> methods = new HashSet<>();
        for (Method method : methodsToBeCloned) {
            methods.add(cloneMethod(method));
        }
        return methods;
    }

    public static Method mergeMethodsToNewOne(Method methodA, Method methodB) {
        Method newMethod = cloneMethod(methodA);

        mergeMethodsToMethodA(newMethod, methodB);

        return newMethod;
    }

    public static void mergeMethodsToMethodA(Method methodA, Method methodB) {
        for (ParameterMethod bParameter : methodB.getParameters()) {
            ParameterMethod clonedParameter = ParameterMethodUtil.cloneParameter(bParameter);
            List<ParameterMethod> aParameters = methodA.getParameters();
            if (aParameters.contains(clonedParameter)) {
                ParameterMethod aParameter = aParameters.get(aParameters.indexOf(clonedParameter));
                if (!aParameter.getType().equals(clonedParameter.getType())
                        || !aParameter.getDirection().equals(clonedParameter.getDirection())) {
                    int count = 1;
                    String name = clonedParameter.getName();
                    do {
                        count++;
                        clonedParameter.setName(name + Integer.toString(count));
                    } while (aParameters.contains(clonedParameter));
                    aParameters.add(clonedParameter);
                }
            } else {
                aParameters.add(clonedParameter);
            }
        }

        ArrayList<Concern> concerns = new ArrayList<>(methodA.getOwnConcerns());
        for (Concern concern : concerns) {
            methodA.removeConcern(concern.getName());
        }
        for (Concern concern : CollectionUtils.union(concerns, methodB.getOwnConcerns())) {
            try {
                methodA.addConcern(concern.getName());
            } catch (ConcernNotFoundException ex) {
                Logger.getLogger(MethodUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private MethodUtil() {
    }
}
