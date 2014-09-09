/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package identification;

import arquitetura.builders.ArchitectureBuilder;
import arquitetura.io.ReaderConfig;
import arquitetura.representation.Architecture;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import pojo.Layer;

/**
 *
 * @author thaina
 */
public class OutputIdentificationLayer {

    public static void main(String[] args) {
        List<Layer> camadas = new ArrayList<>();
        Layer layer1 = new Layer();
        layer1.setNumero(1);
        List<String> sufixos = new ArrayList<>();
        List<String> prefixos = new ArrayList<>();
        sufixos.add("Mgr");
        layer1.setSufixos(sufixos);
        layer1.setPrefixos(prefixos);
        camadas.add(layer1);

        Layer layer2 = new Layer();
        layer2.setNumero(2);
        List<String> sufixos2 = new ArrayList<>();
        List<String> prefixos2 = new ArrayList<>();
        sufixos2.add("Ctrl");
        layer2.setSufixos(sufixos2);
        layer2.setPrefixos(prefixos2);
        camadas.add(layer2);

        Layer layer3 = new Layer();
        layer3.setNumero(3);
        List<String> sufixos3 = new ArrayList<>();
        List<String> prefixos3 = new ArrayList<>();
        sufixos3.add("Gui");
        layer3.setSufixos(sufixos3);
        layer3.setPrefixos(prefixos3);
        camadas.add(layer3);

        int contCorreta = 0;
        int contIncorreta = 0;
        int contProblema = 0;
        File directory = new File("/media/thaina/Acer/Users/Thainá/Documents/Experimentos");
        if (directory.exists()) {
            String[] list = directory.list();
            for (int i = 0; i < list.length; i++) {
                File subdirectory = new File(directory + "/" + list[i]);
                String[] split = list[i].split("_");
                if (split[0].equals("agm")) {
                    String[] configs = subdirectory.list();
                    for (int j = 0; j < configs.length; j++) {
                        File subsubdirectory = new File(subdirectory + "/" + configs[j]);
                        if (subsubdirectory.toString().endsWith("layer")) {
                            File output = new File(subsubdirectory + "/output");
                            String[] outputs = output.list();
                            for (int k = 0; k < outputs.length; k++) {
                                if (outputs[k].endsWith("uml") && outputs[k].startsWith("VAR")) {
                                    ReaderConfig.setPathToProfileSMarty(output + "/resources/smarty.profile.uml");
                                    ReaderConfig.setPathToProfileConcerns(output + "/resources/concerns.profile.uml");
                                    ReaderConfig.setPathProfileRelationship(output + "/resources/relationships.profile.uml");
                                    ReaderConfig.setPathToProfilePatterns(output + "/resources/patterns.profile.uml");
                                    try {
                                        ArchitectureBuilder builder = new ArchitectureBuilder();
                                        Architecture architecture = builder.create(output.getAbsolutePath() + "/" + outputs[k]);
                                        LayerIdentification layerIdentification = new LayerIdentification(architecture);
                                        if (layerIdentification.isCorrect(camadas)) {
                                            //System.out.println("Experimento " + subsubdirectory + " Solução " + outputs[k] + " está correta");
                                            contCorreta++;
                                        } else {
                                            //System.out.println("Experimento " + subsubdirectory + " Solução " + outputs[k] + " não está correta");
                                            contIncorreta++;
                                        }

                                    } catch (Exception ex) {
                                        contProblema++;
                                        //System.out.println("PROBLEMA: " + outputs[k]);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            System.out.println("Diretório não existe");
        }

        System.out.println("Corretas: " + contCorreta);
        System.out.println("Incorretas: " + contIncorreta);
        System.out.println("Com problema: " + contProblema);
    }
}
