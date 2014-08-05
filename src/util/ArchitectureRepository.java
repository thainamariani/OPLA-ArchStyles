/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import arquitetura.io.ReaderConfig;
import arquitetura.representation.Architecture;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import jmetal.util.Configuration;
import main.GenerateArchitecture;

/**
 *
 * @author Thainá
 */
public class ArchitectureRepository {

    public static Architecture CURRENT_ARCHITECTURE = null;

    public static final String AGM = "/home/thaina/NetBeansProjects/OPLA-ArchStyles/agm/agm.uml";
    public static final String BANKING = "/home/thaina/NetBeansProjects/OPLA-ArchStyles/banking/banking.uml";

    public static String getPlaPath(String name) {
        switch (name) {
            case "agm":
                return AGM;
            case "banking":
                System.out.println("PLA Name: " +name);
                return BANKING;
            default:
                return null;
        }
    }

    public static File getOrCreateDirectory(String directoryName) {
        File directory = new File(directoryName);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                System.out.println("Não foi possível criar o diretório " + directoryName);
                System.exit(0);
            }
        }
        return directory;
    }

    private ArchitectureRepository() {
    }

    public static Architecture getCurrentArchitecture() {
        return CURRENT_ARCHITECTURE;
    }

    public static void setCurrentArchitecture(Architecture architecture) {
        CURRENT_ARCHITECTURE = architecture;
    }

    public static void generateArchitecture(String name) {
        GenerateArchitecture generateArchitecture = new GenerateArchitecture();
        generateArchitecture.generate(CURRENT_ARCHITECTURE, name);
    }

    //salva a arquitetura gerada em um arquivo e um log
    public static void saveArchitecture(String operator, String archname) throws IOException {
        int i = 0;
        File directory = new File("test/models/" + archname + "/experiment/" + operator + "/" + operator + i);
        while (directory.exists()) {
            i++;
            directory = new File("test/models/" + archname + "/experiment/" + operator + "/" + operator + i);
        }
        if (!directory.mkdirs()) {
            System.out.println("Não foi poss�vel criar o diret�rio do resultado");
            System.exit(0);
        }

        File file = new File(directory + "/" + operator + i + ".txt");

        if (!file.createNewFile()) {
            System.out.println("Não foi poss�vel criar o arquivo do resultado");
            System.exit(0);
        }

        ReaderConfig.setDirExportTarget("test/models/" + archname + "/experiment/" + operator + "/" + operator + i + "/");
        ArchitectureRepository.generateArchitecture(operator + i);

        verifyOperator(operator, file.getPath());
    }

    private static void verifyOperator(String operator, String file) {
        if (operator.equals("movemethod")) {
            printMoveMethod(file);
        } else if (operator.equals("moveattribute")) {
            printMoveAttribute(file);
        } else if (operator.equals("addclass")) {
            printAddClass(file);
        } else if (operator.equals("moveoperation")) {
            printMoveOperation(file);
        } else if (operator.equals("addpackage")) {
            printAddPackage(file);
        } else if (operator.equals("featuredriven")) {
            printFeatureDriven(file);
        }
    }

    public static void printMoveMethod(String path) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);

            bw.write("Pacote origem: " + ParametersRepository.getSourcePackage());
            bw.newLine();
            bw.write("Classe origem: " + ParametersRepository.getSourceClass());
            bw.newLine();
            bw.write("Pacote destino: " + ParametersRepository.getTargetPackage());
            bw.newLine();
            bw.write("Classe destino: " + ParametersRepository.getTargetClass());
            bw.newLine();
            bw.write("Método movido: " + ParametersRepository.getMoveMethod());

            bw.close();
        } catch (IOException e) {
            Configuration.logger_.severe("Error acceding to the file");
            e.printStackTrace();
        }
    }

    public static void printMoveAttribute(String path) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);

            bw.write("Pacote origem: " + ParametersRepository.getSourcePackage());
            bw.newLine();
            bw.write("Classe origem: " + ParametersRepository.getSourceClass());
            bw.newLine();
            bw.write("Pacote destino: " + ParametersRepository.getTargetPackage());
            bw.newLine();
            bw.write("Classe destino: " + ParametersRepository.getTargetClass());
            bw.newLine();
            bw.write("Atributo movido: " + ParametersRepository.getMoveAttribute());

            bw.close();
        } catch (IOException e) {
            Configuration.logger_.severe("Error acceding to the file");
            e.printStackTrace();
        }
    }

    public static void printAddClass(String path) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);

            bw.write("Pacote origem: " + ParametersRepository.getSourcePackage());
            bw.newLine();
            bw.write("Classe origem: " + ParametersRepository.getSourceClass());
            bw.newLine();
            bw.write("Pacote destino: " + ParametersRepository.getTargetPackage());
            bw.newLine();
            bw.write("Nova Classe: " + ParametersRepository.getTargetClass());
            bw.newLine();
            bw.write("Método movido: " + ParametersRepository.getMoveMethod());
            bw.newLine();
            bw.write("Atributo movido: " + ParametersRepository.getMoveAttribute());

            bw.close();
        } catch (IOException e) {
            Configuration.logger_.severe("Error acceding to the file");
            e.printStackTrace();
        }
    }

    public static void printMoveOperation(String path) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);

            bw.write("Pacote origem: " + ParametersRepository.getSourcePackage());
            bw.newLine();
            bw.write("Interface origem: " + ParametersRepository.getSourceInterface());
            bw.newLine();
            bw.write("Pacote destino: " + ParametersRepository.getTargetPackage());
            bw.newLine();
            bw.write("Interface destino: " + ParametersRepository.getTargetInterface());
            bw.newLine();
            bw.write("Método movido: " + ParametersRepository.getMoveMethod());

            bw.close();
        } catch (IOException e) {
            Configuration.logger_.severe("Error acceding to the file");
            e.printStackTrace();
        }
    }

    public static void printAddPackage(String path) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);

            bw.write("Pacote origem: " + ParametersRepository.getSourcePackage());
            bw.newLine();
            bw.write("Interface origem: " + ParametersRepository.getSourceInterface());
            bw.newLine();
            bw.write("Novo Pacote: " + ParametersRepository.getTargetPackage());
            bw.newLine();
            bw.write("Nova Interface: " + ParametersRepository.getTargetInterface());
            bw.newLine();
            bw.write("Método movido: " + ParametersRepository.getMoveMethod());

            bw.close();
        } catch (IOException e) {
            Configuration.logger_.severe("Error acceding to the file");
            e.printStackTrace();
        }
    }

    private static void printFeatureDriven(String path) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);

            bw.write("Feature selecionada: " + ParametersRepository.getSelectedConcern());
            bw.newLine();

            for (arquitetura.representation.Package pac : ParametersRepository.getModularizationPackages()) {
                bw.write("Pacote: " + pac.getName());
                bw.newLine();
            }

            bw.close();
        } catch (IOException e) {
            Configuration.logger_.severe("Error acceding to the file");
            e.printStackTrace();
        }
    }
}
