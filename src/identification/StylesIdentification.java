/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package identification;

import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Package;
import arquitetura.representation.relationship.Relationship;
import java.util.Set;

/**
 *
 * @author Thainá
 */
public class StylesIdentification {

    private Architecture architecture;

    public StylesIdentification(Architecture architecture) {
        this.architecture = architecture;
    }

    public void identify() {
        Set<Package> packages = architecture.getAllPackages();
        for (Package p : packages) {
            Set<Class> classes = p.getAllClasses();
            for (Class c : classes) {
                Set<Relationship> relationshipsClass = c.getRelationships();
                for (Relationship r : relationshipsClass) {
                    System.out.println("Nome do relacionamento" + r.getName());
                    System.out.println("Tipo do relacionamento:" + r.getType());
                }

            }

            /*Set<Interface> interfaces = p.getAllInterfaces();
             Set<Relationship> relationships = p.getRelationships();
             for (Relationship r : relationships) {
             System.out.println(r.getName());
             } */
        }

    }

    //método provisório
    private String getSuffix(Package comp) {
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

    public Architecture getArchitecture() {
        return architecture;
    }

    public void setArchitecture(Architecture architecture) {
        this.architecture = architecture;
    }

}
