/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.util;

import arquitetura.representation.Package;
import arquitetura.representation.Class;
import arquitetura.representation.Interface;
import arquitetura.representation.Method;
import arquitetura.representation.Attribute;
import arquitetura.representation.Concern;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Thainá
 */
public class ParametersRepository {

    public static Package sourcePackage = null;
    public static Class sourceClass = null;
    public static Package targetPackage = null;
    public static Class targetClass = null;
    public static Method moveMethod = null;
    public static Attribute moveAttribute = null;
    public static Interface sourceInterface = null;
    public static Interface targetInterface = null;
    public static Concern selectedConcern = null;
    public static List<arquitetura.representation.Package> modularizationPackages = new ArrayList<Package>();

    private ParametersRepository() {
    }

    public static Package getSourcePackage() {
        return sourcePackage;
    }

    public static void setSourcePackage(Package sourcePackage) {
        ParametersRepository.sourcePackage = sourcePackage;
    }

    public static Class getSourceClass() {
        return sourceClass;
    }

    public static void setSourceClass(Class sourceClass) {
        ParametersRepository.sourceClass = sourceClass;
    }

    public static Package getTargetPackage() {
        return targetPackage;
    }

    public static void setTargetPackage(Package targetPackage) {
        ParametersRepository.targetPackage = targetPackage;
    }

    public static Class getTargetClass() {
        return targetClass;
    }

    public static void setTargetClass(Class targetClass) {
        ParametersRepository.targetClass = targetClass;
    }

    public static Method getMoveMethod() {
        return moveMethod;
    }

    public static void setMoveMethod(Method moveMethod) {
        ParametersRepository.moveMethod = moveMethod;
    }

    public static Attribute getMoveAttribute() {
        return moveAttribute;
    }

    public static void setMoveAttribute(Attribute moveAttribute) {
        ParametersRepository.moveAttribute = moveAttribute;
    }

    public static Interface getSourceInterface() {
        return sourceInterface;
    }

    public static void setSourceInterface(Interface sourceInterface) {
        ParametersRepository.sourceInterface = sourceInterface;
    }

    public static Interface getTargetInterface() {
        return targetInterface;
    }

    public static void setTargetInterface(Interface targetInterface) {
        ParametersRepository.targetInterface = targetInterface;
    }

    public static Concern getSelectedConcern() {
        return selectedConcern;
    }

    public static void setSelectedConcern(Concern selectedConcern) {
        ParametersRepository.selectedConcern = selectedConcern;
    }

    public static List<Package> getModularizationPackages() {
        return modularizationPackages;
    }

    public static void setModularizationPackages(List<Package> modularizationPackages) {
        ParametersRepository.modularizationPackages = modularizationPackages;
    }

}
