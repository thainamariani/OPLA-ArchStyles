/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package identification;

import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.Package;
import arquitetura.representation.relationship.AssociationClassRelationship;
import arquitetura.representation.relationship.AssociationEnd;
import arquitetura.representation.relationship.AssociationRelationship;
import arquitetura.representation.relationship.Relationship;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import pojo.Layer;
import pojo.Style;
import util.ElementUtil;
import util.RelationshipUtil;
import util.StyleUtil;

/**
 *
 * @author Thainá
 */
public class LayerIdentification extends StylesIdentification {

    public static List<Layer> LISTLAYERS = null;

    public LayerIdentification(Architecture architecture) {
        super(architecture);
    }

    public static List<Layer> getLISTLAYERS() {
        return LISTLAYERS;
    }

    public static void setLISTLAYERS(List<Layer> LISTLAYERS) {
        LayerIdentification.LISTLAYERS = LISTLAYERS;
    }

    @Override
    public boolean repeatSuffixPrefix(List<Layer> camadas) {
        boolean existSomeone = true;
        boolean exist = true;
        for (Layer layer : camadas) {
            for (Map.Entry<String, String> entry : layer.getSp().entrySet()) {
                for (Layer layer2 : camadas) {
                    for (Map.Entry<String, String> entry2 : layer2.getSp().entrySet()) {
                        if ((entry != entry2) && (entry.getValue().toLowerCase().equals(entry2.getValue().toLowerCase())) && (entry.getKey().toLowerCase().equals(entry2.getKey().toLowerCase()))) {
                            System.out.println(entry.getValue() + " " + entry.getKey() + " está repetido.");
                            exist = false;
                        }
                    }
                }
                if (exist == false) {
                    existSomeone = false;
                }
            }
        }
        return existSomeone;
    }

    //verifica se é sufixo ou prefixo
    @Override
    public boolean checkSuffixPrefix(List<Layer> camadas
    ) {
        List<Layer> layers = (List<Layer>) camadas;
        boolean existSomeone = true;
        boolean exist = true;
        for (Layer layer : layers) {
            for (Map.Entry<String, String> entry : layer.getSp().entrySet()) {
                if (entry.getValue().equals("suffix")) {
                    exist = verifySuffix(entry.getKey());
                } else {
                    exist = verifyPrefix(entry.getKey());
                }

                if (exist == false) {
                    existSomeone = false;
                }
            }
        }
        return existSomeone;
    }

//verifica se o sufixo existe nos pacotes
    @Override
    public boolean verifySuffix(String suffix
    ) {
        Set<Package> allpackages = architecture.getAllPackages();
        for (Package p : allpackages) {
            if (p.getName().toLowerCase().endsWith(suffix.toLowerCase())) {
                return true;
            }
        }
        System.out.println("Sufixo " + suffix + " não existe. Insira novamente");
        return false;
    }

    //verifica se o prefixo existe nos pacotes
    @Override
    public boolean verifyPrefix(String prefix
    ) {
        Set<Package> allpackages = architecture.getAllPackages();
        for (Package p : allpackages) {
            if (p.getName().toLowerCase().startsWith(prefix.toLowerCase())) {
                return true;
            }
        }
        System.out.println("Prefixo " + prefix + " não existe. Insira novamente");
        return false;
    }

    //método que verifica se o relacionamento existente entre as camadas está OK para o estilo arquitetural;
    //TODO: elaborar para association e testar com todos os relacionamentos..
    @Override
    public boolean checkStyle(List<Layer> layers) {
        boolean isCorrect = true;
        boolean isCorrectRelationship = true;
        //percorre cada camada
        for (Layer layer : layers) {
            List<Package> layerPackages = layer.getPackages();
            //percorre cada pacote da camada
            for (Package layerPackage : layerPackages) {
                Set<Element> elements = ElementUtil.selectPackageClassesInterfaces(layerPackage);
                //percorre cada elemento (classe, interface, pacote) do pacote
                for (Element element : elements) {
                    Set<Relationship> relationships = ElementUtil.getRelationshipByElement(element);
                    //percorre cada relacionamento do elemento
                    for (Relationship r : relationships) {
                        Element used = null;
                        Element client = null;
                        if (r instanceof AssociationRelationship) {
                            if (RelationshipUtil.VerifyAssociationRelationship((AssociationRelationship) r)) {
                                used = RelationshipUtil.getUsedElementFromAssociationRelationship((AssociationRelationship) r);
                                client = RelationshipUtil.getClientElementFromAssociationRelationship((AssociationRelationship) r);
                                isCorrectRelationship = checkUnidirectionalRelationship(used, client, layer, layers, layerPackages, layerPackage, r);
                            } else {
                                isCorrectRelationship = checkBidirectionalRelationship((AssociationRelationship) r, layerPackages);
                            }
                        } else if (r instanceof AssociationClassRelationship) {
                            isCorrectRelationship = checkAssociationClassRelationship((AssociationClassRelationship) r, layerPackages);
                        } else {
                            used = RelationshipUtil.getUsedElementFromRelationship(r);
                            client = RelationshipUtil.getClientElementFromRelationship(r);
                            isCorrectRelationship = checkUnidirectionalRelationship(used, client, layer, layers, layerPackages, layerPackage, r);
                        }
                    }
                    if (isCorrectRelationship == false) {
                        isCorrect = false;
                    }
                }
            }
        }
        setLISTLAYERS(layers);
        return isCorrect;
    }

    public boolean checkAssociationClassRelationship(AssociationClassRelationship association, List<Package> layerPackages) {
        Package packageElement1 = ElementUtil.getPackage(association.getAssociationClass(), architecture);
        Package packageElement2 = ElementUtil.getPackage(association.getMemebersEnd().get(0).getType(), architecture);
        Package packageElement3 = ElementUtil.getPackage(association.getMemebersEnd().get(1).getType(), architecture);
        if ((!layerPackages.contains(packageElement1)) || (!layerPackages.contains(packageElement2)) || (!layerPackages.contains(packageElement3))) {
            System.out.println("Elementos relacionados com a classe associativa" + association.getAssociationClass() + " devem estar na mesma camada");
            return false;
        }
        return true;
    }

    public boolean checkBidirectionalRelationship(AssociationRelationship association, List<Package> layerPackages) {
        List<AssociationEnd> participants = association.getParticipants();
        Package packageElement1 = ElementUtil.getPackage(participants.get(0).getCLSClass(), architecture);
        Package packageElement2 = ElementUtil.getPackage(participants.get(1).getCLSClass(), architecture);
        if ((!layerPackages.contains(packageElement1)) || (!layerPackages.contains(packageElement2))) {
            System.out.println("Elementos " + participants.get(0).getCLSClass() + " e " + participants.get(1).getCLSClass() + " devem estar na mesma camada");
            return false;
        }
        return true;
    }

    public boolean checkUnidirectionalRelationship(Element used, Element client, Layer layer, List<Layer> layers, List<Package> layerPackages, Package layerPackage, Relationship r) {
        Package usedPackage = ElementUtil.getPackage(used, architecture);
        Package clientPackage = ElementUtil.getPackage(client, architecture);
        List<Package> lowerLayerPackages = StyleUtil.returnPackagesLayerNumber(layer.getNumero() - 1, layers);
        List<Package> upperLayerPackages = StyleUtil.returnPackagesLayerNumber(layer.getNumero() + 1, layers);

        //TODO: GUI: mudar mensagens quando tiver a GUI
        if (layer.getNumero() == 1) {
            if ((clientPackage.equals(layerPackage) && !layerPackages.contains(usedPackage)) || (usedPackage.equals(layerPackage) && !upperLayerPackages.contains(clientPackage) && !layerPackages.contains(clientPackage))) {
                System.out.println("Relacionamento " + r.getName() + " entre o elemento " + client + " pertencente ao pacote " + clientPackage + " e o elemento " + used + " pertencente ao pacote " + usedPackage + " é inválido para o estilo arquitetural em camadas.");
                return false;
            }
        } else if (layer.getNumero() == layers.size()) {
            if ((usedPackage.equals(layerPackage) && !layerPackages.contains(clientPackage)) || (clientPackage.equals(layerPackage) && !lowerLayerPackages.contains(usedPackage) && !layerPackages.contains(usedPackage))) {
                System.out.println("Relacionamento " + r.getName() + " entre o elemento " + client + " pertencente ao pacote " + clientPackage + " e o elemento " + used + " pertencente ao pacote " + usedPackage + " é inválido para o estilo arquitetural em camadas.");
                return false;
            }
        } else {
            if ((usedPackage.equals(layerPackage) && !layerPackages.contains(clientPackage) && !upperLayerPackages.contains(clientPackage)) || (clientPackage.equals(layerPackage) && !lowerLayerPackages.contains(usedPackage) && !layerPackages.contains(usedPackage))) {
                System.out.println("Relacionamento " + r.getName() + " entre o elemento " + client + " pertencente ao pacote " + clientPackage + " e o elemento " + used + " pertencente ao pacote " + usedPackage + " é inválido para o estilo arquitetural em camadas.");
                return false;
            }
        }
        return true;
    }

    //método que identifica quais pacotes são de qual camada com base nos sufixos e prefixos,
    //..retorna false se forem encontrados pacotes sem camadas (sem os sufixos ou prefixos informados)
    @Override
    public boolean identify(List<Layer> layers) {
        Set<Package> packages = architecture.getAllPackages();
        int sizePackages = 0;
        for (Layer layer : layers) {
            List<Package> layerPackages = new ArrayList<>();
            for (Map.Entry<String, String> entry : layer.getSp().entrySet()) {
                String tipo = entry.getValue().toLowerCase();
                String name = entry.getKey().toLowerCase();
                for (Package p : packages) {
                    String packageName = p.getName().toLowerCase();
                    if (((tipo.equals("suffix")) && (packageName.endsWith(name))) || ((tipo.equals("prefix")) && (packageName.startsWith(name)))) {
                        layerPackages.add(p);
                        Set<Package> nestedPackages = p.getNestedPackages();
                        for (Package np : nestedPackages) {
                            layerPackages.add(np);
                            sizePackages++;
                        }
                        sizePackages++;
                    }
                }
            }
            layer.setPackages(layerPackages);
        }

        Set<Interface> interfacesArch = architecture.getInterfaces();
        Set<Class> classesArch = architecture.getClasses();
        boolean isCorrect = false;
        if ((interfacesArch.isEmpty()) && (classesArch.isEmpty()) && (sizePackages == packages.size())) {
            isCorrect = checkStyle(layers);
        } else {
            System.out.println("Erro ao verificar as camadas. Verifique se existem pacotes sem os sufixos ou prefixos informados, ou ainda, elementos na arquitetura que não pertençam a nenhum pacote.");
        }
        return isCorrect;
    }
}
