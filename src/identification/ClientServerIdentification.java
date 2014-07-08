/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package identification;

import arquitetura.representation.Architecture;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.relationship.AssociationClassRelationship;
import arquitetura.representation.relationship.AssociationEnd;
import arquitetura.representation.relationship.AssociationRelationship;
import arquitetura.representation.relationship.Relationship;
import static identification.LayerIdentification.setLISTLAYERS;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import pojo.Client;
import pojo.Layer;
import pojo.Server;
import pojo.Style;
import util.ElementUtil;
import util.RelationshipUtil;
import util.StyleUtil;

/**
 *
 * @author Thainá
 */
public class ClientServerIdentification extends StylesIdentification {

    public static List<Client> LISTCLIENTS = new ArrayList<>();
    public static List<Server> LISTSERVERS = new ArrayList<>();

    public ClientServerIdentification(Architecture architecture) {
        super(architecture);
    }

    public static List<Client> getLISTCLIENTS() {
        return LISTCLIENTS;
    }

    public static void setLISTCLIENTS(List<Client> LISTCLIENTS) {
        ClientServerIdentification.LISTCLIENTS = LISTCLIENTS;
    }

    public static List<Server> getLISTSERVERS() {
        return LISTSERVERS;
    }

    public static void setLISTSERVERS(List<Server> LISTSERVERS) {
        ClientServerIdentification.LISTSERVERS = LISTSERVERS;
    }

    public boolean isCorrect(List<? extends Style> clientsservers) {
        boolean isCorrect = false;
        if (repeatSuffixPrefix(clientsservers)) {
            boolean identify = false;
            identify = checkSuffixPrefix(clientsservers);
            if (identify) {
                isCorrect = identify(clientsservers);
            }
        }
        return isCorrect;
    }

    //retorna se está OK (ou seja, se não repete)
    @Override
    public boolean repeatSuffixPrefix(List<? extends Style> clientsservers) {

        boolean existSomeone = true;
        boolean exist = true;

        for (int i = 0; i < clientsservers.size(); i++) {
            //inclui clientes e servidores
            Style clientserver = null;
            if (clientsservers.get(i) instanceof Client) {
                clientserver = (Client) clientsservers.get(i);
            } else {
                clientserver = (Server) clientsservers.get(i);
            }
            for (String sufixo : clientserver.getSufixos()) {
                for (int j = i + 1; j < clientsservers.size(); j++) {
                    Style clientserver2 = null;
                    if (clientsservers.get(j) instanceof Client) {
                        clientserver2 = (Client) clientsservers.get(j);
                    } else {
                        clientserver2 = (Server) clientsservers.get(j);
                    }
                    for (String sufixo2 : clientserver2.getSufixos()) {
                        if ((sufixo.equalsIgnoreCase(sufixo2))) {
                            System.out.println("O sufixo " + sufixo + " está repetido.");
                            exist = false;
                        }
                    }
                }
            }

            for (String prefixo : clientserver.getPrefixos()) {
                for (int j = i + 1; j < clientsservers.size(); j++) {
                    //inclui clientes e servidores
                    Style clientserver2 = null;
                    if (clientsservers.get(j) instanceof Client) {
                        clientserver2 = (Client) clientsservers.get(j);
                    } else {
                        clientserver2 = (Server) clientsservers.get(j);
                    }
                    for (String prefixo2 : clientserver2.getPrefixos()) {
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
        return existSomeone;
    }

    //verifica se é sufixo ou prefixo
    @Override
    public boolean checkSuffixPrefix(List<? extends Style> clientsservers) {
        boolean existSomeone = true;
        boolean exist = true;
        for (Style clientserver : clientsservers) {
            for (String sufixo : clientserver.getSufixos()) {
                exist = verifySuffix(sufixo);
            }
            for (String prefixo : clientserver.getPrefixos()) {
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
        Set<arquitetura.representation.Package> allpackages = architecture.getAllPackages();
        for (arquitetura.representation.Package p : allpackages) {
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
        Set<arquitetura.representation.Package> allpackages = architecture.getAllPackages();
        for (arquitetura.representation.Package p : allpackages) {
            if (p.getName().toLowerCase().startsWith(prefix.toLowerCase())) {
                return true;
            }
        }
        System.out.println("Prefixo " + prefix + " não existe. Insira novamente");
        return false;
    }

    @Override
    public boolean identify(List<? extends Style> clientsservers) {
        Set<arquitetura.representation.Package> packages = architecture.getAllPackages();
        int sizePackages = 0;
        for (Style clientserver : clientsservers) {
            List<arquitetura.representation.Package> csPackages = new ArrayList<>();
            for (String sufixo : clientserver.getSufixos()) {
                for (arquitetura.representation.Package p : packages) {
                    String packageName = p.getName().toLowerCase();
                    if (packageName.endsWith(sufixo.toLowerCase())) {
                        csPackages.add(p);
                        Set<arquitetura.representation.Package> nestedPackages = p.getNestedPackages();
                        for (arquitetura.representation.Package np : nestedPackages) {
                            csPackages.add(np);
                            sizePackages++;
                        }
                        sizePackages++;
                    }
                }
            }
            for (String prefixo : clientserver.getPrefixos()) {
                for (arquitetura.representation.Package p : packages) {
                    String packageName = p.getName().toLowerCase();
                    if (packageName.startsWith(prefixo.toLowerCase())) {
                        csPackages.add(p);
                        Set<arquitetura.representation.Package> nestedPackages = p.getNestedPackages();
                        for (arquitetura.representation.Package np : nestedPackages) {
                            csPackages.add(np);
                            sizePackages++;
                        }
                        sizePackages++;
                    }
                }
            }
            clientserver.setPackages(csPackages);
        }

        Set<Interface> interfacesArch = architecture.getInterfaces();
        Set<arquitetura.representation.Class> classesArch = architecture.getClasses();
        boolean isCorrect = false;
        if ((interfacesArch.isEmpty()) && (classesArch.isEmpty()) && (sizePackages == packages.size())) {
            isCorrect = checkStyle(clientsservers);
        } else {
            System.out.println("Erro ao verificar os clientes e servidores. Verifique se existem pacotes sem os sufixos ou prefixos informados, ou ainda, elementos na arquitetura que não pertençam a nenhum pacote.");
        }
        return isCorrect;
    }

    @Override
    public boolean checkStyle(List<? extends Style> clientsservers) {
        boolean isCorrect = true;
        boolean isCorrectRelationship = true;

        for (Style clientserver : clientsservers) {
            List<arquitetura.representation.Package> csPackages = clientserver.getPackages();
            for (arquitetura.representation.Package csPackage : csPackages) {
                Set<Element> elements = ElementUtil.selectPackageClassesInterfaces(csPackage);
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
                                isCorrectRelationship = checkUnidirectionalRelationship(used, client, clientserver, clientsservers, csPackages, csPackage, r);
                            } else {
                                isCorrectRelationship = checkBidirectionalRelationship((AssociationRelationship) r, csPackages, clientserver, clientsservers);
                            }
                        } else if (r instanceof AssociationClassRelationship) {
                            isCorrectRelationship = checkAssociationClassRelationship((AssociationClassRelationship) r, csPackages, clientserver, clientsservers);
                        } else {
                            used = RelationshipUtil.getUsedElementFromRelationship(r);
                            client = RelationshipUtil.getClientElementFromRelationship(r);
                            isCorrectRelationship = checkUnidirectionalRelationship(used, client, clientserver, clientsservers, csPackages, csPackage, r);
                        }
                    }
                    if (isCorrectRelationship == false) {
                        isCorrect = false;
                    }
                }
            }
        }
        setClientsServers(clientsservers);
        return isCorrect;
    }

    public boolean checkAssociationClassRelationship(AssociationClassRelationship association, List<arquitetura.representation.Package> csPackages, Style cs, List<? extends Style> clientsservers) {
        arquitetura.representation.Package packageElement1 = ElementUtil.getPackage(association.getAssociationClass(), architecture);
        arquitetura.representation.Package packageElement2 = ElementUtil.getPackage(association.getMemebersEnd().get(0).getType(), architecture);
        arquitetura.representation.Package packageElement3 = ElementUtil.getPackage(association.getMemebersEnd().get(1).getType(), architecture);
        if ((cs instanceof Client) && !csPackages.contains(packageElement1) && !csPackages.contains(packageElement2) && !csPackages.contains(packageElement3)) {
            System.out.println("Elementos relacionados com a classe associativa " + association.getAssociationClass() + " não podem estar em diferentes cliente");
            return false;
        } else {
            Style pac1 = StyleUtil.returnClientServer(packageElement1, (List<Style>) clientsservers);
            Style pac2 = StyleUtil.returnClientServer(packageElement2, (List<Style>) clientsservers);
            Style pac3 = StyleUtil.returnClientServer(packageElement2, (List<Style>) clientsservers);
            if (pac1 instanceof Client || pac2 instanceof Client || pac3 instanceof Client) {
                System.out.println("Elementos relacionados com a classe associativa " + association.getAssociationClass() + " não podem estar entre clientes e servidores");
                return false;
            }
        }
        return true;
    }

    public boolean checkBidirectionalRelationship(AssociationRelationship association, List<arquitetura.representation.Package> csPackages, Style cs, List<? extends Style> clientsservers) {
        List<AssociationEnd> participants = association.getParticipants();
        arquitetura.representation.Package packageElement1 = ElementUtil.getPackage(participants.get(0).getCLSClass(), architecture);
        arquitetura.representation.Package packageElement2 = ElementUtil.getPackage(participants.get(1).getCLSClass(), architecture);
        if ((cs instanceof Client) && !csPackages.contains(packageElement1) && !csPackages.contains(packageElement2)) {
            System.out.println("Elementos " + participants.get(0).getCLSClass() + " e " + participants.get(1).getCLSClass() + " não podem estar entre diferentes clientes");
            return false;
        } else {
            Style pac1 = StyleUtil.returnClientServer(packageElement1, (List<Style>) clientsservers);
            Style pac2 = StyleUtil.returnClientServer(packageElement2, (List<Style>) clientsservers);
            if (pac1 instanceof Client || pac2 instanceof Client) {
                System.out.println("Elementos " + participants.get(0).getCLSClass() + " e " + participants.get(1).getCLSClass() + " não podem estar entre clientes e servidores");
                return false;
            }
        }
        return true;
    }

    public boolean checkUnidirectionalRelationship(Element used, Element client, Style clientserver, List<? extends Style> clientsservers, List<arquitetura.representation.Package> csPackages, arquitetura.representation.Package csPackage, Relationship r) {
        arquitetura.representation.Package usedPackage = ElementUtil.getPackage(used, architecture);
        arquitetura.representation.Package clientPackage = ElementUtil.getPackage(client, architecture);

        //TODO: GUI: mudar mensagens quando tiver a GUI
        if (clientserver instanceof Client) {
            if (usedPackage.equals(csPackage) && !csPackages.contains(clientPackage)) {
                System.out.println("Relacionamento " + r.getName() + " entre o elemento " + client + " pertencente ao pacote " + clientPackage + " e o elemento " + used + " pertencente ao pacote " + usedPackage + " é inválido para o estilo arquitetural cliente/servidor.");
                return false;
            }
        }
        return true;
    }

    public void setClientsServers(List<? extends Style> clientsservers) {
        List<Client> clients = new ArrayList<>();
        List<Server> servers = new ArrayList<>();
        for (Style clientserver : clientsservers) {
            if (clientserver instanceof Client) {
                clients.add((Client) clientserver);
            } else {
                servers.add((Server) clientserver);
            }
        }
        setLISTCLIENTS(clients);
        setLISTSERVERS(servers);
    }

}