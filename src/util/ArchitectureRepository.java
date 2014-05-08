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
import org.apache.log4j.lf5.util.ResourceUtils;

/**
 *
 * @author Thainá
 */
public class ArchitectureRepository {
    
    public static Architecture CURRENT_ARCHITECTURE = null;
    
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
        
        ReaderConfig.setDirExportTarget("test/models/archtest2/experiment/" + operator + "/" + operator + i + "/");
        ArchitectureRepository.generateArchitecture(operator + i);
        
        verifyOperator(operator, file.getPath());
    }
    
    private static void verifyOperator(String operator, String file) {
        if (operator.equals("movemethod")) {
            printMoveMethod(file);
        } else if (operator.equals("moveattribute")) {
            printMoveAttribute(file);
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
}
