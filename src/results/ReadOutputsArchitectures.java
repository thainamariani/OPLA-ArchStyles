/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package results;

import arquitetura.builders.ArchitectureBuilder;
import arquitetura.io.ReaderConfig;
import arquitetura.representation.Architecture;
import arquitetura.representation.Attribute;
import arquitetura.representation.Class;
import arquitetura.representation.Concern;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.Method;
import arquitetura.representation.Package;
import arquitetura.representation.RelationshipsHolder;
import arquitetura.representation.relationship.AssociationClassRelationship;
import arquitetura.representation.relationship.AssociationEnd;
import arquitetura.representation.relationship.AssociationRelationship;
import arquitetura.representation.relationship.DependencyRelationship;
import arquitetura.representation.relationship.Relationship;
import arquitetura.representation.relationship.UsageRelationship;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import pojo.Layer;
import static results.OutputIdentificationClientServer.getInvalidsInterfaces;
import util.ElementUtil;
import util.OperatorUtil;
import util.RelationshipUtil;

/**
 *
 * @author thaina
 */
public class ReadOutputsArchitectures {

    public static void main(String[] args) {
        List<String> plas = new ArrayList<>();
        //plas.add("agm");
        plas.add("mobilemedia");
//        plas.add("bet");
//        plas.add("banking");
//        plas.add("betserver");

        for (String pla : plas) {
            List<File> solutions = new ArrayList<>();
            //menor ED
            if (pla.equals("agm")) {
//                solutions.add(new File("agm/agm.uml"));
//                //menor ED
//                solutions.add(new File("experiment/agm/agm_50_15050_0.9_allComponents/output/VAR_All_agm4.uml"));
//                solutions.add(new File("experiment/agm/agm_100_30100_0.9_layer/output/VAR_All_agm1.uml"));
//                //maior ED
                solutions.add(new File("experiment/agm/agm_50_15050_0.9_allComponents/output/VAR_All_agm2.uml"));
                solutions.add(new File("experiment/agm/agm_100_30100_0.9_layer/output/VAR_All_agm0.uml"));
                //solutions.add(new File("experiment/agm/agm_50_15050_0.9_allComponents/output/VAR_All_agm12.uml"));
                //solutions.add(new File("experiment/agm/agm_100_30100_0.9_layer/output/VAR_All_agm2.uml"));
               
                
            } else if (pla.equals("mobilemedia")) {
                solutions.add(new File("mobilemedia/MobileMedia.uml"));
                solutions.add(new File("experiment/MobileMedia/MobileMedia_50_15050_0.9_allComponents/output/VAR_All_MobileMedia0.uml"));
                solutions.add(new File("experiment/MobileMedia/MobileMedia_100_10100_1.0_layer/output/VAR_All_MobileMedia8.uml"));
            } else if (pla.equals("bet")) {
                //solutions.add(new File("BeT/BeT.uml"));
//                solutions.add(new File("experiment/BeT/BeT_50_5050_1.0_allComponents/output/VAR_All_BeT4.uml"));
                solutions.add(new File("experiment/BeT/BeT_100_10100_0.9_layer/output/VAR_All_BeT6.uml"));
            } else if (pla.equals("banking")) {
                solutions.add(new File("banking/banking.uml"));
                solutions.add(new File("experiment/banking/banking_50_5050_0.9_allComponents/output/VAR_All_banking0.uml"));
                solutions.add(new File("experiment/banking/banking_100_10100_0.9_clientserver/output/VAR_All_banking1.uml"));
            } else if (pla.equals("betserver")) {
                solutions.add(new File("BeT-clientserver/BeT.uml"));
                solutions.add(new File("experiment/BeT/BeT_50_5050_1.0_allComponents/output/VAR_All_BeT2.uml"));
                solutions.add(new File("experiment/BeT/BeT-clientserver_100_30100_0.9_clientserver/output/VAR_All_BeT25.uml"));
            }
            //maior ED
//            if (pla.equals("agm")) {
//                solutions.add(new File("agm/agm.uml"));
//                solutions.add(new File("experiment/agm/agm_200_30000_1.0_allComponents/output/VAR_All_agm0.uml"));
//                solutions.add(new File("experiment/agm/agm_200_30000_1.0_layer/output/VAR_All_agm23.uml"));
//            } else if (pla.equals("mobilemedia")) {
//                solutions.add(new File("mobilemedia/MobileMedia.uml"));
//                solutions.add(new File("experiment/MobileMedia/MobileMedia_50_30000_0.9_allComponents/output/VAR_All_MobileMedia1.uml"));
//                solutions.add(new File("experiment/MobileMedia/MobileMedia_50_30000_1.0_layer/output/VAR_All_MobileMedia0.uml"));
//            } else {
//                solutions.add(new File("BeT/BeT.uml"));
//                solutions.add(new File("experiment/BeT/BeT_50_30000_0.9_allComponents/output/VAR_All_BeT1.uml"));
//                solutions.add(new File("experiment/BeT/BeT_50_30000_1.0_layer/output/VAR_All_BeT0.uml"));
//            }

            for (File solution : solutions) {

                try {
                    String path = solution.getParent();
                    if (path.contains("output") || pla.equals("betserver") || pla.equals("mobilemedia") || pla.equals("bet")) {
                        path += "/resources";
                    }

                    ReaderConfig.setPathToProfileSMarty(path + "/smarty.profile.uml");
                    ReaderConfig.setPathToProfileConcerns(path + "/concerns.profile.uml");
                    ReaderConfig.setPathProfileRelationship(path + "/relationships.profile.uml");
                    ReaderConfig.setPathToProfilePatterns(path + "/patterns.profile.uml");
                    ReaderConfig.setDirTarget("banking1/manipulation/");
                    ReaderConfig.setDirExportTarget("banking1/");

                    ArchitectureBuilder builder = new ArchitectureBuilder();
                    Architecture architecture = builder.create(solution.getAbsolutePath());

                    System.out.println("PLA: " + pla);
                    System.out.println("Solution: " + solution.getPath());

//                    getElements(architecture, pla);
//                    getConcernsforClientServer(architecture, pla);
                    getConcernsforLayer(architecture, pla);
//                   getInvalidsInterfaces(architecture);
//                    OutputIdentificationLayer.getInterfacesImplementors(architecture);
                    //replaceUsageforDependency(architecture);
                    //getDependents(architecture);
//                  getRelationshipsBetweenPackages(architecture);
                } catch (Exception ex) {
                    Logger.getLogger(ReadOutputsArchitectures.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static void getRelationshipsBetweenPackages(Architecture architecture) {

        Set<Package> allPackages = architecture.getAllPackages();
        System.out.println("PACOTES");
        for (Package pac : allPackages) {
            System.out.println(pac.getName());
        }
        RelationshipsHolder relationshipsHolder = architecture.getRelationshipHolder();
        Set<Relationship> allRelationships = relationshipsHolder.getAllRelationships();

        for (Relationship relationship : allRelationships) {

            if (relationship instanceof AssociationClassRelationship) {

                AssociationClassRelationship association = (AssociationClassRelationship) relationship;
                Package packageElement1 = ElementUtil.getPackage(association.getAssociationClass(), architecture);
                Package packageElement2 = ElementUtil.getPackage(association.getMemebersEnd().get(0).getType(), architecture);
                Package packageElement3 = null;
                if (association.getMemebersEnd().size() > 1) {
                    packageElement3 = ElementUtil.getPackage(association.getMemebersEnd().get(1).getType(), architecture);
                }
                System.out.println("Classe associativa: ");
                System.out.println(packageElement1);
                System.out.println(packageElement2);
                System.out.println(packageElement3);

            } else if (relationship instanceof AssociationRelationship) {
                List<AssociationEnd> participants = ((AssociationRelationship) relationship).getParticipants();
                Package packageElement1 = ElementUtil.getPackage(participants.get(0).getCLSClass(), architecture);
                Package packageElement2 = ElementUtil.getPackage(participants.get(1).getCLSClass(), architecture);
                if (packageElement1 != packageElement2) {
                    System.out.println(packageElement1 + " usa " + packageElement2);
                    System.out.println(packageElement2 + " usa " + packageElement1);
                }

            } else {
                Element client = RelationshipUtil.getClientElementFromRelationship(relationship);
                Element used = RelationshipUtil.getUsedElementFromRelationship(relationship);

                Package packageClient = ElementUtil.getPackage(client, architecture);
                Package packageUsed = ElementUtil.getPackage(used, architecture);

                if (packageClient != packageUsed) {
                    System.out.println(packageClient + " usa " + packageUsed);
                }
            }
        }
    }

    public static void getDependents(Architecture architecture) {
        List<arquitetura.representation.Interface> allInterfaces = new ArrayList<arquitetura.representation.Interface>(architecture.getAllInterfaces());
        for (arquitetura.representation.Interface i : allInterfaces) {
            System.out.println("\n\n\n" + i.getName());
            for (Element e : i.getDependents()) {
                if (e instanceof Class) {
                    Class c = (Class) e;
                    System.out.println("\nDependente: " + e.getName());
                    for (Method method : c.getAllMethods()) {
                        System.out.println("Method: " + method.getName());
                    }
                } else if (e instanceof Package) {
                    System.out.println("PACOTE");
                }

                for (Element impl : i.getImplementors()) {
                    if (impl == e) {
                        System.out.println("Dependência e Implementador");
                    }
                }
            }
        }

    }

    public static void getConcernsforLayer(Architecture architecture, String pla) {

        Set<Package> allPackages = architecture.getAllPackages();
        System.out.println("Quantidade de Pacotes:" + allPackages.size());
        System.out.println("Quantidade de Concerns: " + architecture.getAllConcerns().size());

        List<Concern> guiConcerns = new ArrayList<>();
        List<Concern> ctrlConcerns = new ArrayList<>();
        List<Concern> mgrConcerns = new ArrayList<>();
        List<Concern> otherConcerns = new ArrayList<>();

        for (Package pac : allPackages) {
            //System.out.println(pac.getName());
            //System.out.println("");
            List<Concern> ownConcerns = new ArrayList<>();
            for (arquitetura.representation.Class classe : pac.getAllClasses()) {
                ownConcerns.addAll(classe.getOwnConcerns());
                for (Method method : classe.getAllMethods()) {
                    ownConcerns.addAll(method.getOwnConcerns());
                }
                for (Attribute attribute : classe.getAllAttributes()) {
                    ownConcerns.addAll(attribute.getOwnConcerns());
                }
            }

            for (arquitetura.representation.Interface interfaces : pac.getAllInterfaces()) {
                ownConcerns.addAll(interfaces.getOwnConcerns());
                for (Method operation : interfaces.getOperations()) {
                    ownConcerns.addAll(operation.getOwnConcerns());
                }
            }

            if (pac.getName().toUpperCase().endsWith("GUI")) {
                guiConcerns.addAll(ownConcerns);
            } else if (pac.getName().toUpperCase().endsWith("CTRL")) {
                ctrlConcerns.addAll(ownConcerns);
            } else if (pac.getName().toUpperCase().endsWith("MGR")) {
                mgrConcerns.addAll(ownConcerns);
            } else {
                otherConcerns.addAll(ownConcerns);
            }
        }

        List<Concern> foi = new ArrayList<>();
        int contTotal = 0;
        System.out.println("-------------------------------------");
        System.out.println("Concerns Camada 3 - GUI:  ");
        for (int i = 0; i < guiConcerns.size(); i++) {
            if (!foi.contains(guiConcerns.get(i))) {
                int cont = 1;
                for (int j = i + 1; j < guiConcerns.size(); j++) {
                    if (guiConcerns.get(i).equals(guiConcerns.get(j))) {
                        cont++;
                    }
                }
                System.out.println(guiConcerns.get(i) + " " + cont);
                foi.add(guiConcerns.get(i));
                contTotal++;
            }
        }

        System.out.println("Quantidade: " + contTotal);
        System.out.println("-------------------------------------");

        if (pla.equals("agm") || pla.equals("mobilemedia")) {
            System.out.println("Concerns Camada 2 - CTRL:  ");
            contTotal = 0;
            foi = new ArrayList<>();
            for (int i = 0; i < ctrlConcerns.size(); i++) {
                if (!foi.contains(ctrlConcerns.get(i))) {
                    int cont = 1;
                    for (int j = i + 1; j < ctrlConcerns.size(); j++) {
                        if (ctrlConcerns.get(i).equals(ctrlConcerns.get(j))) {
                            cont++;
                        }
                    }
                    System.out.println(ctrlConcerns.get(i) + " " + cont);
                    foi.add(ctrlConcerns.get(i));
                    contTotal++;
                }
            }
            System.out.println("Quantidade: " + contTotal);
            System.out.println("-------------------------------------");

            System.out.println("Concerns Camada 1 - MGR:  ");
            contTotal = 0;
            foi = new ArrayList<>();
            for (int i = 0; i < mgrConcerns.size(); i++) {
                if (!foi.contains(mgrConcerns.get(i))) {
                    int cont = 1;
                    for (int j = i + 1; j < mgrConcerns.size(); j++) {
                        if (mgrConcerns.get(i).equals(mgrConcerns.get(j))) {
                            cont++;
                        }
                    }
                    System.out.println(mgrConcerns.get(i) + " " + cont);
                    foi.add(mgrConcerns.get(i));
                    contTotal++;
                }
            }
            System.out.println("Quantidade: " + contTotal);
            System.out.println("-------------------------------------");
        } else {
            contTotal = 0;
            foi = new ArrayList<>();
            List<Concern> mgrCtrlConcerns = new ArrayList<>();
            mgrCtrlConcerns.addAll(mgrConcerns);
            mgrCtrlConcerns.addAll(ctrlConcerns);

            for (int i = 0; i < mgrCtrlConcerns.size(); i++) {
                if (!foi.contains(mgrCtrlConcerns.get(i))) {
                    int cont = 1;
                    for (int j = i + 1; j < mgrCtrlConcerns.size(); j++) {
                        if (mgrCtrlConcerns.get(i).equals(mgrCtrlConcerns.get(j))) {
                            cont++;
                        }
                    }
                    System.out.println(mgrCtrlConcerns.get(i) + " " + cont);
                    foi.add(mgrCtrlConcerns.get(i));
                    contTotal++;
                }
            }
            System.out.println("Quantidade: " + contTotal);
            System.out.println("-------------------------------------");
        }

        System.out.println("Concerns sem camadas:  ");
        for (Concern otherConcern : otherConcerns) {
            System.out.println(otherConcern.getName());
        }
        System.out.println("Quantidade: " + otherConcerns.size());
        System.out.println("-------------------------------------");

    }

    public static void getConcernsforClientServer(Architecture architecture, String pla) {
        Set<Package> allPackages = architecture.getAllPackages();
        System.out.println("Quantidade de Pacotes:" + allPackages.size());
        System.out.println("Quantidade de Interfaces:" + architecture.getAllInterfaces().size());
        System.out.println("Quantidade de Classes: " + architecture.getAllClasses().size());
        System.out.println("Quantidade de Variabilidades: " + architecture.getAllVariabilities().size());
        System.out.println("Quantidade de Concerns: " + architecture.getAllConcerns().size());

        if (pla.equalsIgnoreCase("banking")) {
            List<Concern> server1Concerns = new ArrayList<>();
            List<Concern> server2Concerns = new ArrayList<>();
            List<Concern> client1Concerns = new ArrayList<>();
            List<Concern> otherConcerns = new ArrayList<>();

            for (Package pac : allPackages) {
//                System.out.println(pac.getName());
//                System.out.println("");
                List<Concern> ownConcerns = new ArrayList<>();
                for (arquitetura.representation.Class classe : pac.getAllClasses()) {
                    ownConcerns.addAll(classe.getOwnConcerns());
                    for (Method method : classe.getAllMethods()) {
                        ownConcerns.addAll(method.getOwnConcerns());
                    }
                    for (Attribute attribute : classe.getAllAttributes()) {
                        ownConcerns.addAll(attribute.getOwnConcerns());
                    }
                }

                for (arquitetura.representation.Interface interfaces : pac.getAllInterfaces()) {
                    ownConcerns.addAll(interfaces.getOwnConcerns());
                    for (Method operation : interfaces.getOperations()) {
                        ownConcerns.addAll(operation.getOwnConcerns());
                    }
                }

                if (pac.getName().toUpperCase().endsWith("SERVER1")) {
                    server1Concerns.addAll(ownConcerns);
                } else if (pac.getName().toUpperCase().endsWith("SERVER2")) {
                    server2Concerns.addAll(ownConcerns);
                } else if (pac.getName().toUpperCase().endsWith("CLIENT1")) {
                    client1Concerns.addAll(ownConcerns);
                } else {
                    otherConcerns.addAll(ownConcerns);
                }
            }

            List<Concern> foi = new ArrayList<>();
            int contTotal = 0;
            System.out.println("-------------------------------------");
            System.out.println("Concerns Server1:  ");
            for (int i = 0; i < server1Concerns.size(); i++) {
                if (!foi.contains(server1Concerns.get(i))) {
                    int cont = 1;
                    for (int j = i + 1; j < server1Concerns.size(); j++) {
                        if (server1Concerns.get(i).equals(server1Concerns.get(j))) {
                            cont++;
                        }
                    }
                    System.out.println(server1Concerns.get(i) + " " + cont);
                    foi.add(server1Concerns.get(i));
                    contTotal++;
                }
            }

            System.out.println("Quantidade: " + contTotal);
            System.out.println("-------------------------------------");

            System.out.println("Concerns Server2:   ");
            contTotal = 0;
            foi = new ArrayList<>();
            for (int i = 0; i < server2Concerns.size(); i++) {
                if (!foi.contains(server2Concerns.get(i))) {
                    int cont = 1;
                    for (int j = i + 1; j < server2Concerns.size(); j++) {
                        if (server2Concerns.get(i).equals(server2Concerns.get(j))) {
                            cont++;
                        }
                    }
                    System.out.println(server2Concerns.get(i) + " " + cont);
                    foi.add(server2Concerns.get(i));
                    contTotal++;
                }
            }
            System.out.println("Quantidade: " + contTotal);
            System.out.println("-------------------------------------");

            System.out.println("Concerns Client1:  ");
            System.out.println("Size: " + client1Concerns.size());
            contTotal = 0;
            foi = new ArrayList<>();
            for (int i = 0; i < client1Concerns.size(); i++) {
                if (!foi.contains(client1Concerns.get(i))) {
                    int cont = 1;
                    for (int j = i + 1; j < client1Concerns.size(); j++) {
                        if (client1Concerns.get(i).equals(client1Concerns.get(j))) {
                            cont++;
                        }
                    }
                    System.out.println(client1Concerns.get(i) + " " + cont);
                    foi.add(client1Concerns.get(i));
                    contTotal++;
                }
            }
            System.out.println("Quantidade: " + contTotal);
            System.out.println("-------------------------------------");

        } else if (pla.equalsIgnoreCase("betserver")) {
            List<Concern> guiConcerns = new ArrayList<>();
            List<Concern> betConcerns = new ArrayList<>();
            List<Concern> clientOnibusConcerns = new ArrayList<>();
            List<Concern> servidorOnibusConcerns = new ArrayList<>();
            List<Concern> otherConcerns = new ArrayList<>();

            for (Package pac : allPackages) {
//                System.out.println(pac.getName());
//                System.out.println("");
                List<Concern> ownConcerns = new ArrayList<>();
                for (arquitetura.representation.Class classe : pac.getAllClasses()) {
                    ownConcerns.addAll(classe.getOwnConcerns());
                    for (Method method : classe.getAllMethods()) {
                        ownConcerns.addAll(method.getOwnConcerns());
                    }
                    for (Attribute attribute : classe.getAllAttributes()) {
                        ownConcerns.addAll(attribute.getOwnConcerns());
                    }
                }

                for (arquitetura.representation.Interface interfaces : pac.getAllInterfaces()) {
                    ownConcerns.addAll(interfaces.getOwnConcerns());
                    for (Method operation : interfaces.getOperations()) {
                        ownConcerns.addAll(operation.getOwnConcerns());
                    }
                }

                if (pac.getName().toUpperCase().endsWith("SERVIDORONIBUS") || pac.getName().equalsIgnoreCase("ValidadorServidorCtrl")) {
                    servidorOnibusConcerns.addAll(ownConcerns);
                } else if (pac.getName().toUpperCase().endsWith("CLIENTEONIBUS") || pac.getName().equalsIgnoreCase("ValidadorOnibusCtrl") || pac.getName().equalsIgnoreCase("ValidadorMgr")) {
                    clientOnibusConcerns.addAll(ownConcerns);
                } else if (pac.getName().toUpperCase().endsWith("GUI")) {
                    guiConcerns.addAll(ownConcerns);
                } else if (pac.getName().toUpperCase().endsWith("CTRL")) {
                    betConcerns.addAll(ownConcerns);
                } else if (pac.getName().toUpperCase().endsWith("MGR")) {
                    betConcerns.addAll(ownConcerns);
                } else {
                    otherConcerns.addAll(ownConcerns);
                }
            }

            List<Concern> foi = new ArrayList<>();
            int contTotal = 0;
            System.out.println("-------------------------------------");
            System.out.println("Concerns Server BET:  ");
            for (int i = 0; i < betConcerns.size(); i++) {
                if (!foi.contains(betConcerns.get(i))) {
                    int cont = 1;
                    for (int j = i + 1; j < betConcerns.size(); j++) {
                        if (betConcerns.get(i).equals(betConcerns.get(j))) {
                            cont++;
                        }
                    }
                    System.out.println(betConcerns.get(i) + " " + cont);
                    foi.add(betConcerns.get(i));
                    contTotal++;
                }
            }

            System.out.println("Quantidade: " + contTotal);
            System.out.println("-------------------------------------");

            System.out.println("Concerns Server ServidorOnibus:   ");
            contTotal = 0;
            foi = new ArrayList<>();
            for (int i = 0; i < servidorOnibusConcerns.size(); i++) {
                if (!foi.contains(servidorOnibusConcerns.get(i))) {
                    int cont = 1;
                    for (int j = i + 1; j < servidorOnibusConcerns.size(); j++) {
                        if (servidorOnibusConcerns.get(i).equals(servidorOnibusConcerns.get(j))) {
                            cont++;
                        }
                    }
                    System.out.println(servidorOnibusConcerns.get(i) + " " + cont);
                    foi.add(servidorOnibusConcerns.get(i));
                    contTotal++;
                }
            }
            System.out.println("Quantidade: " + contTotal);
            System.out.println("-------------------------------------");

            System.out.println("Client ClienteOnibus:  ");
            contTotal = 0;
            foi = new ArrayList<>();
            for (int i = 0; i < clientOnibusConcerns.size(); i++) {
                if (!foi.contains(clientOnibusConcerns.get(i))) {
                    int cont = 1;
                    for (int j = i + 1; j < clientOnibusConcerns.size(); j++) {
                        if (clientOnibusConcerns.get(i).equals(clientOnibusConcerns.get(j))) {
                            cont++;
                        }
                    }
                    System.out.println(clientOnibusConcerns.get(i) + " " + cont);
                    foi.add(clientOnibusConcerns.get(i));
                    contTotal++;
                }
            }
            System.out.println("Quantidade: " + contTotal);
            System.out.println("-------------------------------------");

            System.out.println("Client GUI:  ");
            contTotal = 0;
            foi = new ArrayList<>();
            for (int i = 0; i < guiConcerns.size(); i++) {
                if (!foi.contains(guiConcerns.get(i))) {
                    int cont = 1;
                    for (int j = i + 1; j < guiConcerns.size(); j++) {
                        if (guiConcerns.get(i).equals(guiConcerns.get(j))) {
                            cont++;
                        }
                    }
                    System.out.println(guiConcerns.get(i) + " " + cont);
                    foi.add(guiConcerns.get(i));
                    contTotal++;
                }
            }
            System.out.println("Quantidade: " + contTotal);
            System.out.println("-------------------------------------");
        }
    }

    public static void replaceUsageforDependency(Architecture architecture) throws IOException {
        RelationshipsHolder relationshipHolder = architecture.getRelationshipHolder();
        List<UsageRelationship> allUsage = relationshipHolder.getAllUsage();
        int cont = 0;
        for (UsageRelationship usage : allUsage) {
            Element client = usage.getClient();
            Element supplier = usage.getSupplier();
            DependencyRelationship dependency = new DependencyRelationship(supplier, client, "Dependency" + cont++);
            architecture.addRelationship(dependency);
            architecture.removeRelationship(usage);
        }

        System.out.println(cont + " dependências adicionadas");
        architecture.save(architecture, "", "");
    }

    public static void getElements(Architecture architecture, String pla) {

        if (pla.equals("betserver")) {
            Set<arquitetura.representation.Package> allPackages = architecture.getAllPackages();
            List<arquitetura.representation.Package> guiPackages = new ArrayList<>();
            List<arquitetura.representation.Package> betPackages = new ArrayList<>();
            List<arquitetura.representation.Package> clienteOnibusPackages = new ArrayList<>();
            List<arquitetura.representation.Package> servidorOnibusPackages = new ArrayList<>();

            for (arquitetura.representation.Package pac : allPackages) {
                if (pac.getName().toUpperCase().endsWith("GUI")) {
                    guiPackages.add(pac);
                } else if (pac.getName().toUpperCase().endsWith("CTRL")) {
                    betPackages.add(pac);
                } else if (pac.getName().toUpperCase().endsWith("MGR")) {
                    betPackages.add(pac);
                } else if (pac.getName().toUpperCase().endsWith("CLIENTEONIBUS")) {
                    clienteOnibusPackages.add(pac);
                } else if (pac.getName().toUpperCase().endsWith("SERVIDORONIBUS")) {
                    servidorOnibusPackages.add(pac);
                }
            }

            System.out.println("PACOTES: ");
            System.out.println("GUI: " + guiPackages.size());
            System.out.println("BET: " + betPackages.size());
            System.out.println("CLIENTE ONIBUS: " + clienteOnibusPackages.size());
            System.out.println("SERVIDOR ONIBUS: " + servidorOnibusPackages.size());

            List<arquitetura.representation.Interface> guiInterfaces = new ArrayList<>();
            List<arquitetura.representation.Class> guiClasses = new ArrayList<>();
            List<Concern> guiAllConcerns = new ArrayList<>();
            List<Concern> guiConcerns = new ArrayList<>();
            for (arquitetura.representation.Package guiPackage : guiPackages) {
                guiInterfaces.addAll(guiPackage.getAllInterfaces());
                guiClasses.addAll(guiPackage.getAllClasses());
                guiAllConcerns.addAll(guiPackage.getAllConcerns());
            }

            for (Concern c : guiAllConcerns) {
                if (!guiConcerns.contains(c)) {
                    guiConcerns.add(c);
                }
            }

            System.out.println("GUI INTERFACES:" + guiInterfaces.size());
            System.out.println("GUI CLASSES:" + guiClasses.size());
            System.out.println("GUI CONCERNS:" + guiConcerns.size());

            List<arquitetura.representation.Interface> betInterfaces = new ArrayList<>();
            List<arquitetura.representation.Class> betClasses = new ArrayList<>();
            List<Concern> betAllConcerns = new ArrayList<>();
            List<Concern> betConcerns = new ArrayList<>();
            for (arquitetura.representation.Package betPackage : betPackages) {
                betInterfaces.addAll(betPackage.getAllInterfaces());
                betClasses.addAll(betPackage.getAllClasses());
                betAllConcerns.addAll(betPackage.getAllConcerns());
            }

            for (Concern c : betAllConcerns) {
                if (!betConcerns.contains(c)) {
                    betConcerns.add(c);
                }
            }

            System.out.println("BET INTERFACES:" + betInterfaces.size());
            System.out.println("BET CLASSES:" + betClasses.size());
            System.out.println("BET CONCERNS:" + betConcerns.size());

            List<arquitetura.representation.Interface> clienteOnibusInterfaces = new ArrayList<>();
            List<arquitetura.representation.Class> clienteOnibusClasses = new ArrayList<>();
            List<Concern> clienteOnibusAllConcerns = new ArrayList<>();
            List<Concern> clienteOnibusConcerns = new ArrayList<>();
            for (arquitetura.representation.Package clienteOnibusPackage : clienteOnibusPackages) {
                clienteOnibusInterfaces.addAll(clienteOnibusPackage.getAllInterfaces());
                clienteOnibusClasses.addAll(clienteOnibusPackage.getAllClasses());
                clienteOnibusAllConcerns.addAll(clienteOnibusPackage.getAllConcerns());
            }

            for (Concern c : clienteOnibusAllConcerns) {
                if (!clienteOnibusConcerns.contains(c)) {
                    clienteOnibusConcerns.add(c);
                }
            }

            System.out.println("CLIENTE ONIBUS INTERFACES:" + clienteOnibusInterfaces.size());
            System.out.println("CLIENTE ONIBUS CLASSES:" + clienteOnibusClasses.size());
            System.out.println("CLIENTE ONIBUS CONCERNS:" + clienteOnibusConcerns.size());
            System.out.println("----------------------------------------------------");
            System.out.println(" ");

            List<arquitetura.representation.Interface> servidorOnibusInterfaces = new ArrayList<>();
            List<arquitetura.representation.Class> servidorOnibusClasses = new ArrayList<>();
            List<Concern> servidorOnibusAllConcerns = new ArrayList<>();
            List<Concern> servidorOnibusConcerns = new ArrayList<>();
            for (arquitetura.representation.Package servidorOnibusPackage : servidorOnibusPackages) {
                servidorOnibusInterfaces.addAll(servidorOnibusPackage.getAllInterfaces());
                servidorOnibusClasses.addAll(servidorOnibusPackage.getAllClasses());
                servidorOnibusAllConcerns.addAll(servidorOnibusPackage.getAllConcerns());
            }

            for (Concern c : servidorOnibusAllConcerns) {
                if (!servidorOnibusConcerns.contains(c)) {
                    servidorOnibusConcerns.add(c);
                }
            }

            System.out.println("SERVIDOR ONIBUS INTERFACES:" + servidorOnibusInterfaces.size());
            System.out.println("SERVIDOR ONIBUS CLASSES:" + servidorOnibusClasses.size());
            System.out.println("SERVIDOR ONIBUS CONCERNS:" + servidorOnibusConcerns.size());
            System.out.println("----------------------------------------------------");
            System.out.println(" ");

            List<Concern> onlyGuiConcern = new ArrayList<>();
            List<Concern> onlyBetConcern = new ArrayList<>();
            List<Concern> onlyClienteOnibusConcern = new ArrayList<>();
            List<Concern> onlyServidorOnibusConcern = new ArrayList<>();

            for (Concern c : architecture.getAllConcerns()) {
                boolean containsGui = false;
                boolean containsBet = false;
                boolean containsClienteOnibus = false;
                boolean containsServidorOnibus = false;

                int contAll = 0;
                if (guiConcerns.contains(c)) {
                    containsGui = true;
                    contAll++;
                }

                if (betConcerns.contains(c)) {
                    containsBet = true;
                    contAll++;
                }

                if (clienteOnibusConcerns.contains(c)) {
                    containsClienteOnibus = true;
                    contAll++;
                }

                if (servidorOnibusConcerns.contains(c)) {
                    containsServidorOnibus = true;
                    contAll++;
                }

                if (contAll == 1) {
                    if (containsGui) {
                        onlyGuiConcern.add(c);
                    } else if (containsBet) {
                        onlyBetConcern.add(c);
                    } else if (containsClienteOnibus) {
                        onlyClienteOnibusConcern.add(c);
                    } else if (containsServidorOnibus) {
                        onlyServidorOnibusConcern.add(c);
                    }

                }
            }

            System.out.println("CONCERNS EXCLUSIVOS GUI: " + onlyGuiConcern.size());
            System.out.println("CONCERNS EXCLUSIVOS BET: " + onlyBetConcern.size());
            System.out.println("CONCERNS EXCLUSIVOS CLIENTE ONIBUS: " + onlyClienteOnibusConcern.size());
            System.out.println("CONCERNS EXCLUSIVOS SERVIDOR ONIBUS: " + onlyServidorOnibusConcern.size());

        } else if (pla.equals("banking")) {
            Set<arquitetura.representation.Package> allPackages = architecture.getAllPackages();
            List<arquitetura.representation.Package> server1Packages = new ArrayList<>();
            List<arquitetura.representation.Package> server2Packages = new ArrayList<>();
            List<arquitetura.representation.Package> client1Packages = new ArrayList<>();

            for (arquitetura.representation.Package pac : allPackages) {
                if (pac.getName().toUpperCase().endsWith("SERVER1")) {
                    server1Packages.add(pac);
                } else if (pac.getName().toUpperCase().endsWith("SERVER2")) {
                    server2Packages.add(pac);
                } else if (pac.getName().toUpperCase().endsWith("CLIENT1")) {
                    client1Packages.add(pac);
                }
            }

            System.out.println("PACOTES: ");
            System.out.println("SERVER1: " + server1Packages.size());
            System.out.println("SERVER2: " + server2Packages.size());
            System.out.println("CLIENT1: " + client1Packages.size());

            List<arquitetura.representation.Interface> server1Interfaces = new ArrayList<>();
            List<arquitetura.representation.Class> server1Classes = new ArrayList<>();
            List<Concern> server1AllConcerns = new ArrayList<>();
            List<Concern> server1Concerns = new ArrayList<>();
            for (arquitetura.representation.Package server1Package : server1Packages) {
                server1Interfaces.addAll(server1Package.getAllInterfaces());
                server1Classes.addAll(server1Package.getAllClasses());
                server1AllConcerns.addAll(server1Package.getAllConcerns());
            }

            for (Concern c : server1AllConcerns) {
                if (!server1Concerns.contains(c)) {
                    server1Concerns.add(c);
                }
            }

            System.out.println("SERVER1 INTERFACES:" + server1Interfaces.size());
            System.out.println("SERVER1 CLASSES:" + server1Classes.size());
            System.out.println("SERVER1 CONCERNS:" + server1Concerns.size());

            List<arquitetura.representation.Interface> server2Interfaces = new ArrayList<>();
            List<arquitetura.representation.Class> server2Classes = new ArrayList<>();
            List<Concern> server2AllConcerns = new ArrayList<>();
            List<Concern> server2Concerns = new ArrayList<>();
            for (arquitetura.representation.Package server2Package : server2Packages) {
                server2Interfaces.addAll(server2Package.getAllInterfaces());
                server2Classes.addAll(server2Package.getAllClasses());
                server2AllConcerns.addAll(server2Package.getAllConcerns());
            }

            for (Concern c : server2AllConcerns) {
                if (!server2Concerns.contains(c)) {
                    server2Concerns.add(c);
                }
            }

            System.out.println("SERVER2 INTERFACES:" + server2Interfaces.size());
            System.out.println("SERVER2 CLASSES:" + server2Classes.size());
            System.out.println("SERVER2 CONCERNS:" + server2Concerns.size());

            List<arquitetura.representation.Interface> client1Interfaces = new ArrayList<>();
            List<arquitetura.representation.Class> client1Classes = new ArrayList<>();
            List<Concern> client1AllConcerns = new ArrayList<>();
            List<Concern> client1Concerns = new ArrayList<>();
            for (arquitetura.representation.Package client1Package : client1Packages) {
                client1Interfaces.addAll(client1Package.getAllInterfaces());
                client1Classes.addAll(client1Package.getAllClasses());
                client1AllConcerns.addAll(client1Package.getAllConcerns());
            }

            for (Concern c : client1AllConcerns) {
                if (!client1Concerns.contains(c)) {
                    client1Concerns.add(c);
                }
            }

            System.out.println("CLIENT1 ONIBUS INTERFACES:" + client1Interfaces.size());
            System.out.println("CLIENT1 ONIBUS CLASSES:" + client1Classes.size());
            System.out.println("CLIENT1 ONIBUS CONCERNS:" + client1Concerns.size());
            System.out.println("----------------------------------------------------");
            System.out.println(" ");

            List<Concern> onlyServer1Concern = new ArrayList<>();
            List<Concern> onlyServer2Concern = new ArrayList<>();
            List<Concern> onlyClient1Concern = new ArrayList<>();

            for (Concern c : architecture.getAllConcerns()) {
                boolean containsServer1 = false;
                boolean containsServer2 = false;
                boolean containsClient1 = false;

                int contAll = 0;
                if (server1Concerns.contains(c)) {
                    containsServer1 = true;
                    contAll++;
                }

                if (server2Concerns.contains(c)) {
                    containsServer2 = true;
                    contAll++;
                }

                if (client1Concerns.contains(c)) {
                    containsClient1 = true;
                    contAll++;
                }

                if (contAll == 1) {
                    if (containsServer1) {
                        onlyServer1Concern.add(c);
                    } else if (containsServer2) {
                        onlyServer2Concern.add(c);
                    } else if (containsClient1) {
                        onlyClient1Concern.add(c);
                    }
                }
            }

            System.out.println("CONCERNS EXCLUSIVOS SERVER1: " + onlyServer1Concern.size());
            System.out.println("CONCERNS EXCLUSIVOS SERVER2: " + onlyServer2Concern.size());
            System.out.println("CONCERNS EXCLUSIVOS CLIENT1: " + onlyClient1Concern.size());

        } else {

            Set<arquitetura.representation.Package> allPackages = architecture.getAllPackages();
            List<arquitetura.representation.Package> guiPackages = new ArrayList<>();
            List<arquitetura.representation.Package> betPackages = new ArrayList<>();
            List<arquitetura.representation.Package> mgrPackages = new ArrayList<>();
            List<arquitetura.representation.Package> ctrlPackages = new ArrayList<>();

            for (arquitetura.representation.Package pac : allPackages) {
                if (pac.getName().toUpperCase().endsWith("GUI")) {
                    guiPackages.add(pac);
                } else if (pac.getName().toUpperCase().endsWith("CTRL")) {
                    ctrlPackages.add(pac);
                } else if (pac.getName().toUpperCase().endsWith("MGR")) {
                    mgrPackages.add(pac);
                }
            }

            System.out.println("PACOTES: ");
            System.out.println("GUI: " + guiPackages.size());
            System.out.println("CTRL: " + ctrlPackages.size());
            System.out.println("MGR: " + mgrPackages.size());

            List<arquitetura.representation.Interface> guiInterfaces = new ArrayList<>();
            List<arquitetura.representation.Class> guiClasses = new ArrayList<>();
            List<Concern> guiAllConcerns = new ArrayList<>();
            List<Concern> guiConcerns = new ArrayList<>();
            for (arquitetura.representation.Package guiPackage : guiPackages) {
                guiInterfaces.addAll(guiPackage.getAllInterfaces());
                guiClasses.addAll(guiPackage.getAllClasses());
                guiAllConcerns.addAll(guiPackage.getAllConcerns());
            }

            for (Concern c : guiAllConcerns) {
                if (!guiConcerns.contains(c)) {
                    guiConcerns.add(c);
                }
            }

            System.out.println("GUI INTERFACES:" + guiInterfaces.size());
            System.out.println("GUI CLASSES:" + guiClasses.size());
            System.out.println("GUI CONCERNS:" + guiConcerns.size());

            List<arquitetura.representation.Interface> ctrlInterfaces = new ArrayList<>();
            List<arquitetura.representation.Class> ctrlClasses = new ArrayList<>();
            List<Concern> ctrlAllConcerns = new ArrayList<>();
            List<Concern> ctrlConcerns = new ArrayList<>();
            for (arquitetura.representation.Package ctrlPackage : ctrlPackages) {
                ctrlInterfaces.addAll(ctrlPackage.getAllInterfaces());
                ctrlClasses.addAll(ctrlPackage.getAllClasses());
                ctrlAllConcerns.addAll(ctrlPackage.getAllConcerns());
            }

            for (Concern c : ctrlAllConcerns) {
                if (!ctrlConcerns.contains(c)) {
                    ctrlConcerns.add(c);
                }
            }

            System.out.println("CTRL INTERFACES:" + ctrlInterfaces.size());
            System.out.println("CTRL CLASSES:" + ctrlClasses.size());
            System.out.println("CTRL CONCERNS:" + ctrlConcerns.size());

            List<arquitetura.representation.Interface> mgrInterfaces = new ArrayList<>();
            List<arquitetura.representation.Class> mgrClasses = new ArrayList<>();
            List<Concern> mgrAllConcerns = new ArrayList<>();
            List<Concern> mgrConcerns = new ArrayList<>();
            for (arquitetura.representation.Package mgrPackage : mgrPackages) {
                mgrInterfaces.addAll(mgrPackage.getAllInterfaces());
                mgrClasses.addAll(mgrPackage.getAllClasses());
                mgrAllConcerns.addAll(mgrPackage.getAllConcerns());
            }

            for (Concern c : mgrAllConcerns) {
                if (!mgrConcerns.contains(c)) {
                    mgrConcerns.add(c);
                }
            }

            System.out.println("MGR INTERFACES:" + mgrInterfaces.size());
            System.out.println("MGR CLASSES:" + mgrClasses.size());
            System.out.println("MGR CONCERNS:" + mgrConcerns.size());
            System.out.println("----------------------------------------------------");
            System.out.println(" ");

            List<Concern> betConcerns = new ArrayList<>();
            if (pla.equals("bet")) {
                List<Concern> betAllConcerns = new ArrayList<>();
                betAllConcerns.addAll(ctrlConcerns);
                betAllConcerns.addAll(mgrConcerns);

                for (Concern c : betAllConcerns) {
                    if (!betConcerns.contains(c)) {
                        betConcerns.add(c);
                    }
                }

                System.out.println("BET CONCERNS: " + betConcerns.size());
            }

            List<Concern> onlyGuiConcern = new ArrayList<>();
            List<Concern> onlyCtrlConcern = new ArrayList<>();
            List<Concern> onlyMgrConcern = new ArrayList<>();
            List<Concern> onlyBetConcern = new ArrayList<>();

            for (Concern c : architecture.getAllConcerns()) {
                boolean containsGui = false;
                boolean containsCtrl = false;
                boolean containsMgr = false;
                boolean containsBet = false;
                int contAll = 0;
                if (guiConcerns.contains(c)) {
                    containsGui = true;
                    contAll++;
                }

                if (!pla.equals("bet")) {

                    if (ctrlConcerns.contains(c)) {
                        containsCtrl = true;
                        contAll++;
                    }
                    if (mgrConcerns.contains(c)) {
                        containsMgr = true;
                        contAll++;
                    }
                } else {
                    if (betConcerns.contains(c)) {
                        containsBet = true;
                        contAll++;
                    }
                }

                if (contAll == 1) {
                    if (containsGui) {
                        onlyGuiConcern.add(c);
                    } else if (containsCtrl) {
                        onlyCtrlConcern.add(c);
                    } else if (containsMgr) {
                        onlyMgrConcern.add(c);
                    } else if (containsBet) {
                        onlyBetConcern.add(c);
                    }

                }
            }

            System.out.println(
                    "CONCERNS EXCLUSIVOS GUI: " + onlyGuiConcern.size());
            if (pla.equals("bet")) {
                System.out.println("CONCERNS EXCLUSIVOS BET: " + onlyBetConcern.size());
            } else {
                System.out.println(
                        "CONCERNS EXCLUSIVOS CTRL: " + onlyCtrlConcern.size());
                System.out.println(
                        "CONCERNS EXCLUSIVOS MGR: " + onlyMgrConcern.size());
            }
        }
    }
}
