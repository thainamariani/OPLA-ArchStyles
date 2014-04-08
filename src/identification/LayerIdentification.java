/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package identification;

import arquitetura.helpers.UtilResources;
import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Element;
import arquitetura.representation.Package;
import arquitetura.representation.relationship.AssociationRelationship;
import arquitetura.representation.relationship.Relationship;
import java.util.List;
import java.util.Set;
import pojo.Layer;
import util.ElementUtil;
import util.RelationshipUtil;

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
        Set<Package> packages = architecture.getAllPackages();
        for (Package p : packages) {
            System.out.println("--------------------------------------------------------------");
            System.out.println("Pacote:" + p.getName());
            Set<Class> classes = p.getAllClasses();
            for (Class c : classes) {
                Set<Relationship> relationshipsClass = c.getRelationships();
                for (Relationship r : relationshipsClass) {
                    if (!(r instanceof AssociationRelationship)) {
                        System.out.println("Tipo do relacionamento:" + r.getType());
                        Element used = RelationshipUtil.getUsedElementFromRelationship(r);
                        Element client = RelationshipUtil.getClientElementFromRelationship(r);
                        if (UtilResources.extractPackageName(client.getNamespace()).equals(p.getName())) {
                            System.out.println("Classe " + client.getName() + "possui um relacionamento inválido para o estilo em camadas (layer 0)");

                        }
                    }
                }

            }
        }
    }

    @Override
    public void identify() {
    }
}
