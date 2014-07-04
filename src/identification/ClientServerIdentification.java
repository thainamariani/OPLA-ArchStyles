/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package identification;

import arquitetura.representation.Architecture;
import arquitetura.representation.Interface;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import pojo.Client;
import pojo.Layer;
import pojo.Server;
import pojo.Style;

/**
 *
 * @author Thainá
 */
public class ClientServerIdentification extends StylesIdentification {

    public static List<Layer> LISTCLIENTS = null;
    public static List<Layer> LISTSERVERS = null;

    public ClientServerIdentification(Architecture architecture) {
        super(architecture);
    }

    public static List<Layer> getLISTCLIENTS() {
        return LISTCLIENTS;
    }

    public static void setLISTCLIENTS(List<Layer> LISTCLIENTS) {
        ClientServerIdentification.LISTCLIENTS = LISTCLIENTS;
    }

    public static List<Layer> getLISTSERVERS() {
        return LISTSERVERS;
    }

    public static void setLISTSERVERS(List<Layer> LISTSERVERS) {
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

    public boolean checkStyle(List<? extends Style> list) {
        return false;
    }

}
