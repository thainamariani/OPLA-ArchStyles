/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.identification;

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
import java.util.Set;
import project.pojo.Layer;
import project.pojo.Style;
import project.util.ElementUtil;
import project.util.OperatorUtil;
import project.util.RelationshipUtil;
import project.util.StyleUtil;

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

    public boolean isCorrect(List<Layer> layers) {
        boolean isCorrect = false;
        if (repeatSuffixPrefix(layers)) {
            boolean identify = false;
            identify = checkSuffixPrefix(layers);
            if (identify) {
                isCorrect = identify(layers);
            }
        }
        return isCorrect;
    }

    @Override
    //retorna se está OK (ou seja, se não repete)
    public boolean repeatSuffixPrefix(List<? extends Style> camadas) {
        boolean existSomeone = true;
        boolean exist = true;

        for (int i = 0; i < camadas.size(); i++) {
            Layer layer = (Layer) camadas.get(i);
            for (String sufixo : layer.getSufixos()) {
                for (int j = i + 1; j < camadas.size(); j++) {
                    Layer layer2 = (Layer) camadas.get(j);
                    for (String sufixo2 : layer2.getSufixos()) {
                        if ((sufixo.equalsIgnoreCase(sufixo2))) {
                            System.out.println("O sufixo " + sufixo + " está repetido.");
                            exist = false;
                        }
                    }
                }
            }

            for (String prefixo : layer.getPrefixos()) {
                for (int j = i + 1; j < camadas.size(); j++) {
                    Layer layer2 = (Layer) camadas.get(j);
                    for (String prefixo2 : layer2.getPrefixos()) {
                        if (prefixo.equalsIgnoreCase(prefixo2)) {
                            System.out.println("O prefixo " + prefixo + " está repetido.");
                            exist = false;
                        }
                    }
                }
            }
            if (exist == false) {
                existSomeone = false;
            }
        }
//        for (Layer layer : camadas) {
//            for (Map.Entry<String, String> entry : layer.getSp().entrySet()) {
//                for (Layer layer2 : camadas) {
//                    for (Map.Entry<String, String> entry2 : layer2.getSp().entrySet()) {
//                        if ((entry != entry2) && (entry.getValue().toLowerCase().equals(entry2.getValue().toLowerCase())) && (entry.getKey().toLowerCase().equals(entry2.getKey().toLowerCase()))) {
//                            System.out.println(entry.getValue() + " " + entry.getKey() + " está repetido.");
//                            exist = false;
//                        }
//                    }
//                }
//                if (exist == false) {
//                    existSomeone = false;
//                }
//            }
//        }
        return existSomeone;
    }

    //verifica se é sufixo ou prefixo
    @Override
    public boolean checkSuffixPrefix(List<? extends Style> camadas) {
        List<Layer> layers = (List<Layer>) camadas;
        boolean existSomeone = true;
        boolean exist = true;
        for (Layer layer : layers) {
            for (String sufixo : layer.getSufixos()) {
                exist = verifySuffix(sufixo);
            }
            for (String prefixo : layer.getPrefixos()) {
                exist = verifyPrefix(prefixo);
            }

            if (exist == false) {
                existSomeone = false;
            }
        }
        return existSomeone;
    }

//verifica se o sufixo existe nos pacotes
    @Override
    public boolean verifySuffix(String suffix) {
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
    public boolean verifyPrefix(String prefix) {
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
    @Override
    public boolean checkStyle(List<? extends Style> camadas) {
        boolean isCorrect = true;
        boolean isCorrectRelationship = true;
        //percorre cada camada
        List<Layer> layers = (List<Layer>) camadas;
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
                            if (RelationshipUtil.verifyAssociationRelationship((AssociationRelationship) r)) {
                                used = RelationshipUtil.getUsedElementFromRelationship((AssociationRelationship) r);
                                client = RelationshipUtil.getClientElementFromRelationship((AssociationRelationship) r);
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
                        if (isCorrectRelationship == false) {
                            isCorrect = false;
                        }
                    }
                }
            }
        }
        return isCorrect;
    }

    public boolean checkAssociationClassRelationship(AssociationClassRelationship association, List<Package> layerPackages) {
        Package packageElement1 = ElementUtil.getPackage(association.getAssociationClass(), architecture);
        Package packageElement2 = ElementUtil.getPackage(association.getMemebersEnd().get(0).getType(), architecture);
        Package packageElement3 = null;
        if (association.getMemebersEnd().size() > 1) {
            packageElement3 = ElementUtil.getPackage(association.getMemebersEnd().get(1).getType(), architecture);
            if ((!layerPackages.contains(packageElement1)) || (!layerPackages.contains(packageElement2)) || (!layerPackages.contains(packageElement3))) {
                System.out.println("Elementos relacionados com a classe associativa " + association.getAssociationClass() + " devem estar na mesma camada");
                return false;
            }
        } else {
            if ((!layerPackages.contains(packageElement1)) || (!layerPackages.contains(packageElement2))) {
                System.out.println("Elementos relacionados com a classe associativa " + association.getAssociationClass() + " devem estar na mesma camada");
                return false;
            }
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
    public boolean identify(List<? extends Style> camadas) {
        setLISTLAYERS((List<Layer>) camadas);

        clearPackagesFromLayers();
        addPackagesToLayers(architecture);

        Set<Interface> interfacesArch = architecture.getInterfaces();
        Set<Class> classesArch = architecture.getClasses();
        boolean isCorrect = false;

        int sizePackages = 0;
        for (Style camada : camadas) {
            sizePackages += camada.getPackages().size();
        }
        if ((interfacesArch.isEmpty()) && (classesArch.isEmpty()) && (sizePackages == architecture.getAllPackages().size())) {
            isCorrect = checkStyle(camadas);
        } else {
            System.out.println("Erro ao verificar as camadas. Verifique se existem pacotes sem os sufixos ou prefixos informados, ou ainda, elementos na arquitetura que não pertençam a nenhum pacote.");
        }
        return isCorrect;
    }

    public static Layer getLayerByNumber(int number) {
        for (Layer layer : LISTLAYERS) {
            if (layer.getNumero() == number) {
                return layer;
            }
        }
        return null;
    }

    public static void clearPackagesFromLayers() {
        for (Layer layer : LISTLAYERS) {
            layer.getPackages().clear();
        }
    }

    public static void addPackagesToLayers(Architecture architecture) {
        Set<arquitetura.representation.Package> allPackages = architecture.getAllPackages();
        List<arquitetura.representation.Package> packages = new ArrayList<>();
        packages.addAll(allPackages);
        //código que exclui os nestedPackages da lista de pacotes (eles serão verificados adiante)
        for (arquitetura.representation.Package pacotes : allPackages) {
            for (arquitetura.representation.Package np : pacotes.nestedPackages) {
                if (allPackages.contains(np)) {
                    packages.remove(np);
                }
            }
        }

        List<Layer> layers = (List<Layer>) LISTLAYERS;
        for (Layer layer : layers) {
            List<Package> layerPackages = new ArrayList<>();
            for (String sufixo : layer.getSufixos()) {
                for (Package p : packages) {
                    String packageName = p.getName().toLowerCase();
                    if (packageName.endsWith(sufixo.toLowerCase())) {
                        layerPackages.add(p);
                        Set<Package> nestedPackages = p.getNestedPackages();
                        for (Package np : nestedPackages) {
                            layerPackages.add(np);
                        }
                    }
                }
            }
            for (String prefixo : layer.getPrefixos()) {
                for (Package p : packages) {
                    String packageName = p.getName().toLowerCase();
                    if (packageName.startsWith(prefixo.toLowerCase())) {
                        layerPackages.add(p);
                        Set<Package> nestedPackages = p.getNestedPackages();
                        for (Package np : nestedPackages) {
                            layerPackages.add(np);
                        }
                    }
                }
            }
            layer.setPackages(layerPackages);
        }
    }

    public void isCorrectLayerCommunication(List<Layer> layers) {
        for (Layer layer : layers) {
            List<Package> packages = layer.getPackages();
            List<Relationship> relationshipsLayer = new ArrayList<>();
            for (Package pac : packages) {
                for (Class classe : pac.getAllClasses()) {
                    relationshipsLayer.addAll(classe.getRelationships());
                }
                for (Interface itf : pac.getAllInterfaces()) {
                    relationshipsLayer.addAll(itf.getRelationships());
                }
            }

            for (Relationship relationship : relationshipsLayer) {
                Element client = RelationshipUtil.getClientElementFromRelationship(relationship);
                Element used = RelationshipUtil.getUsedElementFromRelationship(relationship);

                if ((client != null) && (used != null)) {
                    Package packageClient = ElementUtil.getPackage(client, architecture);
                    Package packageUsed = ElementUtil.getPackage(used, architecture);

                    int layerUsed = OperatorUtil.findPackageLayer(layers, packageUsed).getNumero();
                    int layerClient = OperatorUtil.findPackageLayer(layers, packageClient).getNumero();

                    if (layerUsed != layerClient) {
                        System.out.println("Camada " + layerClient + " usa " + layerUsed);
                        break;
                }
                }

            }
        }
    }
}
