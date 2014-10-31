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
import arquitetura.representation.Concern;
import arquitetura.representation.Element;
import arquitetura.representation.Method;
import arquitetura.representation.Package;
import arquitetura.representation.RelationshipsHolder;
import arquitetura.representation.relationship.DependencyRelationship;
import arquitetura.representation.relationship.UsageRelationship;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.OperatorUtil;

/**
 *
 * @author thaina
 */
public class ReadOutputsArchitectures {

    public static void main(String[] args) {
        List<String> plas = new ArrayList<>();
        //plas.add("agm");
        //plas.add("mobilemedia");
        plas.add("bet");

        for (String pla : plas) {
            List<File> solutions = new ArrayList<>();
            //menor ED
            if (pla.equals("agm")) {
                //solutions.add(new File("agm/agm.uml"));
                //solutions.add(new File("experiment/agm/agm_200_30000_1.0_allComponents/output/VAR_All_agm16.uml"));
                //solutions.add(new File("experiment/agm/agm_200_30000_1.0_layer/output/VAR_All_agm4.uml"));
            } else if (pla.equals("mobilemedia")) {
                //solutions.add(new File("mobilemediaAnterior/MobileMedia.uml"));
                //solutions.add(new File("experiment/MobileMedia/MobileMedia_50_30000_0.9_allComponents/output/VAR_All_MobileMedia7.uml"));
                //solutions.add(new File("experiment/MobileMedia/MobileMedia_50_30000_1.0_layer/output/VAR_All_MobileMedia5.uml"));
            } else {
                solutions.add(new File("BeTAnterior/BeT.uml"));
                //solutions.add(new File("experiment/BeT/BeT_50_30000_0.9_allComponents/output/VAR_All_BeT3.uml"));
                //solutions.add(new File("experiment/BeT/BeT_50_30000_1.0_layer/output/VAR_All_BeT10.uml"));
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
                    if (path.contains("output")) {
                        path += "/resources";
                    }

                    ReaderConfig.setPathToProfileSMarty(path + "/smarty.profile.uml");
                    ReaderConfig.setPathToProfileConcerns(path + "/concerns.profile.uml");
                    ReaderConfig.setPathProfileRelationship(path + "/relationships.profile.uml");
                    ReaderConfig.setPathToProfilePatterns(path + "/patterns.profile.uml");
                    ReaderConfig.setDirTarget("BeT/manipulation/");
                    ReaderConfig.setDirExportTarget("BeT/");

                    ArchitectureBuilder builder = new ArchitectureBuilder();
                    Architecture architecture = builder.create(solution.getAbsolutePath());

                    System.out.println("PLA: " + pla);
                    System.out.println("Solution: " + solution.getPath());
                    //getConcernsforLayer(architecture, pla);
                    //getInvalidsInterfaces(architecture);
                    replaceUsageforDependency(architecture);
                } catch (Exception ex) {
                    Logger.getLogger(ReadOutputsArchitectures.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }
    }

    public static void getInvalidsInterfaces(Architecture architecture) {
        boolean validSolution = OperatorUtil.isValidSolution(architecture);
        System.out.println("Valida? " + validSolution);
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
            System.out.println(pac.getName());
            System.out.println("");
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

    public static void getElementsforLayer(Architecture architecture, String pla) {

        Set<arquitetura.representation.Package> allPackages = architecture.getAllPackages();
        List<arquitetura.representation.Package> guiPackages = new ArrayList<>();
        List<arquitetura.representation.Package> ctrlPackages = new ArrayList<>();
        List<arquitetura.representation.Package> mgrPackages = new ArrayList<>();

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
