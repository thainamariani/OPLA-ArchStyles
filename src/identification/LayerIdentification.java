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
import java.util.List;
import java.util.Set;
import pojo.Layer;

/**
 *
 * @author Thainá
 */
public class LayerIdentification extends StylesIdentification {

    public LayerIdentification(Architecture architecture) {
        super(architecture);
    }

    @Override
    public boolean checkSuffixPrefix(String type, String sp) {
        if (type.equals("suffix")) {
            return verifySuffix(sp);
        } else {
            return verifyPrefix(sp);
        }
    }

    @Override
    public boolean verifySuffix(String suffix) {
        Set<Package> allpackages = architecture.getAllPackages();
        for (Package p : allpackages) {
            if (p.getName().toLowerCase().endsWith(suffix.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean verifyPrefix(String prefix) {
        Set<Package> allpackages = architecture.getAllPackages();
        for (Package p : allpackages) {
            if (p.getName().toLowerCase().startsWith(prefix.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void checkStyle(List<Layer> camadas) {
        
    }

    @Override
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

            break;
        }

    }
}
