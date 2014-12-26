package results;

import arquitetura.builders.ArchitectureBuilder;
import arquitetura.io.ReaderConfig;
import arquitetura.representation.Architecture;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.qualityIndicator.util.MetricsUtil;
import jmetal.util.comparators.EqualSolutions;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class GeraTudoAKAGodClass {

    public static int EXECUTIONS = 30;

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public static void main(String[] args) throws Exception {

        HashMap<String, String[]> experiments = new HashMap();
        //NOVOS VALORES
        //MobileMedia_50_15050_0.9_allComponents
        //banking_50_5050_0.9_allComponents
        //BeT-clientserver_100_30100_0.9_clientserver

//        experiments.put("MobileMedia", new String[]{
//            "MobileMedia_50_15050_0.9_allComponents",
//            "MobileMedia_100_10100_1.0_layer",});
//
//        experiments.put("agm", new String[]{
//            "agm_100_30100_0.9_layer",
//            "agm_50_15050_0.9_allComponents",});
//        
//        experiments.put("MobileMedia", new String[]{
//            "MobileMedia_100_10100_1.0_layer",
//            "MobileMedia_50_15050_0.9_allComponents",});
//        
//        experiments.put("banking", new String[]{
//            "banking_100_10100_0.9_clientserver",
//            "banking_50_5050_0.9_allComponents",});
//
        experiments.put("BeT_layer", new String[]{
            "BeT_100_10100_0.9_layer",
            "BeT_50_5050_1.0_allComponents"});
//        experiments.put("BeT", new String[]{
//            "BeT_50_5050_1.0_allComponents",
//            "BeT_100_10100_0.9_layer",
//            "BeT-clientserver_100_30100_0.9_clientserver"});
//        experiments.put("BeT_clientserver", new String[]{
//            "BeT_50_5050_1.0_allComponents",
//            "BeT-clientserver_100_30100_0.9_clientserver"});
//        experiments.put("agm_allComponents", new String[]{
//            "agm_100_10100_0.9_allComponents",
//            "agm_100_10100_1.0_allComponents",
//            "agm_100_30100_0.9_allComponents",
//            "agm_100_30100_1.0_allComponents",
//            "agm_100_90100_0.9_allComponents",
//            "agm_100_90100_1.0_allComponents",
//            "agm_200_180200_0.9_allComponents",
//            "agm_200_180200_1.0_allComponents",
//            "agm_200_20200_0.9_allComponents",
//            "agm_200_20200_1.0_allComponents",
//            "agm_200_60200_0.9_allComponents",
//            "agm_200_60200_1.0_allComponents",
//            "agm_50_15050_0.9_allComponents",
//            "agm_50_15050_1.0_allComponents",
//            "agm_50_45050_0.9_allComponents",
//            "agm_50_45050_1.0_allComponents",
//            "agm_50_5050_0.9_allComponents",
//            "agm_50_5050_1.0_allComponents",});
//
//        experiments.put("agm_layer", new String[]{
//            "agm_100_10100_0.9_layer",
//            "agm_100_10100_1.0_layer",
//            "agm_100_30100_0.9_layer",
//            "agm_100_30100_1.0_layer",
//            "agm_100_90100_0.9_layer",
//            "agm_100_90100_1.0_layer",
//            "agm_200_180200_0.9_layer",
//            "agm_200_180200_1.0_layer",
//            "agm_200_20200_0.9_layer",
//            "agm_200_20200_1.0_layer",
//            "agm_200_60200_0.9_layer",
//            "agm_200_60200_1.0_layer",
//            "agm_50_15050_0.9_layer",
//            "agm_50_15050_1.0_layer",
//            "agm_50_45050_0.9_layer",
//            "agm_50_45050_1.0_layer",
//            "agm_50_5050_0.9_layer",
//            "agm_50_5050_1.0_layer",});
//
//        experiments.put("MobileMedia_layer", new String[]{
//            "MobileMedia_100_10100_0.9_layer",
//            "MobileMedia_100_10100_1.0_layer",
//            "MobileMedia_100_30100_0.9_layer",
//            "MobileMedia_100_30100_1.0_layer",
//            "MobileMedia_100_90100_0.9_layer",
//            "MobileMedia_100_90100_1.0_layer",
//            "MobileMedia_200_180200_0.9_layer",
//            "MobileMedia_200_180200_1.0_layer",
//            "MobileMedia_200_20200_0.9_layer",
//            "MobileMedia_200_20200_1.0_layer",
//            "MobileMedia_200_60200_0.9_layer",
//            "MobileMedia_200_60200_1.0_layer",
//            "MobileMedia_50_15050_0.9_layer",
//            "MobileMedia_50_15050_1.0_layer",
//            "MobileMedia_50_45050_0.9_layer",
//            "MobileMedia_50_45050_1.0_layer",
//            "MobileMedia_50_5050_0.9_layer",
//            "MobileMedia_50_5050_1.0_layer",});
//
//        experiments.put("MobileMedia_allComponents", new String[]{
//            "MobileMedia_100_10100_0.9_allComponents",
//            "MobileMedia_100_10100_1.0_allComponents",
//            "MobileMedia_100_30100_0.9_allComponents",
//            "MobileMedia_100_30100_1.0_allComponents",
//            "MobileMedia_100_90100_0.9_allComponents",
//            "MobileMedia_100_90100_1.0_allComponents",
//            "MobileMedia_200_180200_0.9_allComponents",
//            "MobileMedia_200_180200_1.0_allComponents",
//            "MobileMedia_200_20200_0.9_allComponents",
//            "MobileMedia_200_20200_1.0_allComponents",
//            "MobileMedia_200_60200_0.9_allComponents",
//            "MobileMedia_200_60200_1.0_allComponents",
//            "MobileMedia_50_15050_0.9_allComponents",
//            "MobileMedia_50_15050_1.0_allComponents",
//            "MobileMedia_50_45050_0.9_allComponents",
//            "MobileMedia_50_45050_1.0_allComponents",
//            "MobileMedia_50_5050_0.9_allComponents",
//            "MobileMedia_50_5050_1.0_allComponents",});
//
//        experiments.put("banking_clientserver", new String[]{
//            "banking_100_10100_0.9_clientserver",
//            "banking_100_10100_1.0_clientserver",
//            "banking_100_30100_0.9_clientserver",
//            "banking_100_30100_1.0_clientserver",
//            "banking_100_90100_0.9_clientserver",
//            "banking_100_90100_1.0_clientserver",
//            "banking_200_180200_0.9_clientserver",
//            "banking_200_180200_1.0_clientserver",
//            "banking_200_20200_0.9_clientserver",
//            "banking_200_20200_1.0_clientserver",
//            "banking_200_60200_0.9_clientserver",
//            "banking_200_60200_1.0_clientserver",
//            "banking_50_15050_0.9_clientserver",
//            "banking_50_15050_1.0_clientserver",
//            "banking_50_45050_0.9_clientserver",
//            "banking_50_45050_1.0_clientserver",
//            "banking_50_5050_0.9_clientserver",
//            "banking_50_5050_1.0_clientserver",});
//
//        experiments.put("banking_allComponents", new String[]{
//            "banking_100_10100_0.9_allComponents",
//            "banking_100_10100_1.0_allComponents",
//            "banking_100_30100_0.9_allComponents",
//            "banking_100_30100_1.0_allComponents",
//            "banking_100_90100_0.9_allComponents",
//            "banking_100_90100_1.0_allComponents",
//            "banking_200_180200_0.9_allComponents",
//            "banking_200_180200_1.0_allComponents",
//            "banking_200_20200_0.9_allComponents",
//            "banking_200_20200_1.0_allComponents",
//            "banking_200_60200_0.9_allComponents",
//            "banking_200_60200_1.0_allComponents",
//            "banking_50_15050_0.9_allComponents",
//            "banking_50_15050_1.0_allComponents",
//            "banking_50_45050_0.9_allComponents",
//            "banking_50_45050_1.0_allComponents",
//            "banking_50_5050_0.9_allComponents",
//            "banking_50_5050_1.0_allComponents",});
//
//        experiments.put("BeT_layer", new String[]{
//            "BeT_100_10100_0.9_layer",
//            "BeT_100_10100_1.0_layer",
//            "BeT_100_30100_0.9_layer",
//            "BeT_100_30100_1.0_layer",
//            "BeT_100_90100_0.9_layer",
//            "BeT_100_90100_1.0_layer",
//            "BeT_200_180200_0.9_layer",
//            "BeT_200_180200_1.0_layer",
//            "BeT_200_20200_0.9_layer",
//            "BeT_200_20200_1.0_layer",
//            "BeT_200_60200_0.9_layer",
//            "BeT_200_60200_1.0_layer",
//            "BeT_50_15050_0.9_layer",
//            "BeT_50_15050_1.0_layer",
//            "BeT_50_45050_0.9_layer",
//            "BeT_50_45050_1.0_layer",
//            "BeT_50_5050_0.9_layer",
//            "BeT_50_5050_1.0_layer",});
//
//        experiments.put("BeT_allComponents", new String[]{
//            "BeT_100_10100_0.9_allComponents",
//            "BeT_100_10100_1.0_allComponents",
//            "BeT_100_30100_0.9_allComponents",
//            "BeT_100_30100_1.0_allComponents",
//            "BeT_100_90100_0.9_allComponents",
//            "BeT_100_90100_1.0_allComponents",
//            "BeT_200_180200_0.9_allComponents",
//            "BeT_200_180200_1.0_allComponents",
//            "BeT_200_20200_0.9_allComponents",
//            "BeT_200_20200_1.0_allComponents",
//            "BeT_200_60200_0.9_allComponents",
//            "BeT_200_60200_1.0_allComponents",
//            "BeT_50_15050_0.9_allComponents",
//            "BeT_50_15050_1.0_allComponents",
//            "BeT_50_45050_0.9_allComponents",
//            "BeT_50_45050_1.0_allComponents",
//            "BeT_50_5050_0.9_allComponents",
//            "BeT_50_5050_1.0_allComponents",});
//
//        experiments.put("BeT_clientserver", new String[]{
//            "BeT_100_10100_0.9_clientserver",
//            "BeT_100_10100_1.0_clientserver",
//            "BeT_100_30100_0.9_clientserver",
//            "BeT_100_30100_1.0_clientserver",
//            "BeT_100_90100_0.9_clientserver",
//            "BeT_100_90100_1.0_clientserver",
//            "BeT_200_180200_0.9_clientserver",
//            "BeT_200_180200_1.0_clientserver",
//            "BeT_200_20200_0.9_clientserver",
//            "BeT_200_20200_1.0_clientserver",
//            "BeT_200_60200_0.9_clientserver",
//            "BeT_200_60200_1.0_clientserver",
//            "BeT_50_15050_0.9_clientserver",
//            "BeT_50_15050_1.0_clientserver",
//            "BeT_50_45050_0.9_clientserver",
//            "BeT_50_45050_1.0_clientserver",
//            "BeT_50_5050_0.9_clientserver",
//            "BeT_50_5050_1.0_clientserver",});
        MetricsUtil mu = new MetricsUtil();

        for (Map.Entry<String, String[]> entry : experiments.entrySet()) {
            String pla = entry.getKey();
            String[] plaJustName = pla.split("_");
            String[] contexts = entry.getValue();

            String directoryPath = "experiment/" + pla + "/";

            try (FileWriter funAll = new FileWriter(directoryPath + "FUN_All_" + plaJustName[0] + ".txt")) {
                for (String contexto : contexts) {
                    double[][] front = mu.readFront(directoryPath + contexto + "/" + "FUN_All_" + plaJustName[0] + ".txt");
                    for (double[] solucao : front) {
                        funAll.write(solucao[0] + " " + solucao[1] + "\n");
                    }
                }
            }
            normalizaHypervolume(directoryPath, plaJustName[0], contexts);
            executaHypervolume(directoryPath, plaJustName[0], contexts);
            runFriedman(directoryPath, contexts);
            runKruskal(directoryPath, contexts);
            runWilcoxon(directoryPath, contexts);
            executeEuclideanDistance(directoryPath, plaJustName[0], contexts);
            executeParetoStats(directoryPath, plaJustName[0], contexts);
            executeArchitectureStats(directoryPath, pla, contexts);
        }
    }

    private static void normalizaHypervolume(String directoryPath, String pla, String[] contexts) throws IOException {
        MetricsUtil mu = new MetricsUtil();

        SolutionSet solutionSet = new SolutionSet();

        for (String contexto : contexts) {
            for (int i = 0; i < EXECUTIONS; i++) {
                SolutionSet execution = mu.readNonDominatedSolutionSet(directoryPath + contexto + "/FUN_" + pla + "_" + i + ".txt");
                solutionSet = solutionSet.union(execution);
            }
        }

        double[] max = mu.getMaximumValues(solutionSet.writeObjectivesToMatrix(), 2);
        double[] min = mu.getMinimumValues(solutionSet.writeObjectivesToMatrix(), 2);
        try (FileWriter funAll = new FileWriter(directoryPath + "FUN_All_N_" + pla + ".txt")) {
            for (String contexto : contexts) {
                try (FileWriter fileWriter = new FileWriter(directoryPath + contexto + "/HYPERVOLUME_N.txt")) {
                    for (int i = 0; i < EXECUTIONS; i++) {
                        SolutionSet execution = mu.readNonDominatedSolutionSet(directoryPath + contexto + "/FUN_" + pla + "_" + i + ".txt");
                        for (Iterator<Solution> it = execution.iterator(); it.hasNext();) {
                            Solution solution = it.next();
                            for (int j = 0; j < 2; j++) {
                                double objective = solution.getObjective(j);
                                objective = (objective - min[j]) / (max[j] - min[j]);
                                fileWriter.append(objective + " ");
                                funAll.write(objective + " ");
                            }
                            fileWriter.append("\n");
                            funAll.write("\n");
                        }
                        if (i != 29) {
                            fileWriter.append("\n");
                        }
                    }
                }
                try (FileWriter funAllContext = new FileWriter(directoryPath + contexto + "/FUN_All_N_" + pla + ".txt")) {
                    SolutionSet execution = mu.readNonDominatedSolutionSet(directoryPath + contexto + "/FUN_All_" + pla + ".txt");
                    for (Iterator<Solution> it = execution.iterator(); it.hasNext();) {
                        Solution solution = it.next();
                        for (int j = 0; j < 2; j++) {
                            double objective = solution.getObjective(j);
                            objective = (objective - min[j]) / (max[j] - min[j]);
                            funAllContext.append(objective + " ");
                        }
                        funAllContext.write("\n");
                    }
                }
            }
            //atual - menor / maior - menor
        }
    }

    private static void executeParetoStats(String directoryPath, String pla, String[] contexts) {
        MetricsUtil mu = new MetricsUtil();
        EqualSolutions comparator = new EqualSolutions();
        SolutionSet truePareto = removeDominadas(mu.readNonDominatedSolutionSet(directoryPath + "FUN_All_" + pla + ".txt"));
        try (FileWriter fileWriter = new FileWriter(directoryPath + "PARETO.txt")) {
            fileWriter.write("True Pareto:\t" + truePareto.size() + "\n");
            for (String context : contexts) {
                SolutionSet knownPareto = mu.readNonDominatedSolutionSet(directoryPath + context + "/FUN_All_" + pla + ".txt");
                int count = 0;
                knownFor:
                for (Iterator<Solution> knownIterator = knownPareto.iterator(); knownIterator.hasNext();) {
                    Solution knownSolution = knownIterator.next();
                    for (Iterator<Solution> trueIterator = truePareto.iterator(); trueIterator.hasNext();) {
                        Solution trueSolution = trueIterator.next();
                        if (comparator.compare(trueSolution, knownSolution) == 0) {
                            count++;
                            continue knownFor;
                        }
                    }
                }
                fileWriter.write(context + "\t " + knownPareto.size() + " (" + count + ")\n");
            }
        } catch (IOException ex) {
            Logger.getLogger(GeraTudoAKAGodClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void executeEuclideanDistance(String directoryPath, String pla, String[] contexts) throws IOException {
        MetricsUtil mu = new MetricsUtil();
        SolutionSet ss = mu.readNonDominatedSolutionSet(directoryPath + "FUN_All_N_" + pla + ".txt");
        ss = removeDominadas(ss);
//        ss.printObjectivesToFile(directoryPath + "FUN_All_N_" + pla + ".txt");

        double[] min = mu.getMinimumValues(ss.writeObjectivesToMatrix(), 2);
        try (FileWriter todosEds = new FileWriter(directoryPath + "ALL_ED_" + pla + ".txt")) {
            try (FileWriter menoresEds = new FileWriter(directoryPath + "MIN_ED_" + pla + ".txt")) {
                todosEds.write("--- " + min[0] + " " + min[1] + " ---" + "\n");
                for (String contexto : contexts) {
                    List<Integer> melhoresSolucoesPorContexto = new ArrayList<>();
                    double menorDistancia = Integer.MAX_VALUE;

//                    double[][] front = new double[quantidadeSolucoes][numObjetivos];
//                    for(solucoes)for(objetivos)solucoes[solucaoI][numObjJ] = valor do banco;
                    double[][] front = mu.readFront(directoryPath + contexto + "/" + "FUN_All_N_" + pla + ".txt");
                    for (int i = 0; i < front.length; i++) {
                        double distanciaEuclidiana = mu.distance(min, front[i]);
                        todosEds.write(distanciaEuclidiana + "\n");
                        if (distanciaEuclidiana < menorDistancia) {
                            menorDistancia = distanciaEuclidiana;
                            melhoresSolucoesPorContexto.clear();
                        }
                        if (distanciaEuclidiana == menorDistancia) {
                            melhoresSolucoesPorContexto.add(i);
                        }
                    }

                    for (Integer melhorSolucao : melhoresSolucoesPorContexto) {
                        menoresEds.write(contexto + ": " + melhorSolucao + " - ED: " + menorDistancia + "\n");
                    }
                }
            }
        }
    }

    private static void executaHypervolume(String directoryPath, String pla, String[] contexts) throws IOException, InterruptedException {
        MetricsUtil mu = new MetricsUtil();
        double[] referencePoint = Hypervolume.printReferencePoint(mu.readFront(directoryPath + "FUN_All_N_" + pla + ".txt"), directoryPath + "/HYPERVOLUME_REFERENCE.txt", 2);

        try (FileWriter sh = new FileWriter(directoryPath + "run_hypervolume.sh")) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("#!/bin/sh\n");
            for (String context : contexts) {
                stringBuilder.append("\n");
                stringBuilder.append("#-----------------------------------------\n");
                stringBuilder.append("system=").append(directoryPath).append(context).append("\n");
                stringBuilder.append("reference=\"");
                for (double d : referencePoint) {
                    stringBuilder.append(Double.toString(d)).append(" ");
                }
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                stringBuilder.append("\"\n");
                stringBuilder.append("\n");
                stringBuilder.append("echo \"$system\"\n");
                stringBuilder.append("FILES=./$system/HYPERVOLUME_N.txt\n");
                stringBuilder.append("for f in $FILES\n");
                stringBuilder.append("do\n");
                stringBuilder.append("\techo \"Processing $f\"\n");
                stringBuilder.append("\t./experiment/hv-1.3-src/hv -r \"$reference\" $f >> ./$system/HYPERVOLUME_RESULT.txt\n");
                stringBuilder.append("done\n");
                stringBuilder.append("echo \"\\n\"\n");
            }
            sh.write(stringBuilder.toString());
        }

        for (String context : contexts) {
            Hypervolume.clearFile(directoryPath + context + "/HYPERVOLUME_RESULT.txt");
        }

        ProcessBuilder processBuilder = new ProcessBuilder("sh", "./" + directoryPath + "run_hypervolume.sh");
        Process process = processBuilder.start();
        process.waitFor();

        try (FileWriter hypervolumesFile = new FileWriter(directoryPath + "HYPERVOLUMES.txt")) {
            hypervolumesFile.append("#\t / Context\t / Mean\t / Max\t / Std. Dev.\t / Time\t / Std. Dev.\n");
            int count = 1;
            for (String context : contexts) {
                DescriptiveStatistics hypervolumes = new DescriptiveStatistics();
                DescriptiveStatistics execTimes = new DescriptiveStatistics();
                Scanner scanner = new Scanner(new File(directoryPath + context + "/HYPERVOLUME_RESULT.txt"));
                Scanner scannerTime = new Scanner(new File(directoryPath + context + "/TIME_" + pla));
                scannerTime.nextLine();
                scannerTime.nextLine();
                while (scanner.hasNextDouble()) {
                    double hypervolume = scanner.nextDouble();
                    hypervolumes.addValue(hypervolume);
                    execTimes.addValue(scannerTime.nextLong());
                }

                hypervolumesFile.append(count++ + "\t" + context + "\t" + String.valueOf(hypervolumes.getMean()).replace(".", ",") + "\t" + String.valueOf(hypervolumes.getMax()).replace(".", ",") + "\t" + String.valueOf(hypervolumes.getStandardDeviation()).replace(".", ",") + "\t" + String.valueOf(execTimes.getMean()).replace(".", ",") + "\t" + String.valueOf(execTimes.getStandardDeviation()).replace(".", ",") + "\n");
            }
        }
    }

    public static void runFriedman(String directoryPath, String[] contexts) throws IOException, InterruptedException {
        try (FileWriter friedman = new FileWriter(directoryPath + "friedman_script.txt")) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String context : contexts) {
                stringBuilder.append(context).append("<- c(");
                Scanner scanner = new Scanner(new FileInputStream(directoryPath + context + "/HYPERVOLUME_RESULT.txt"));
                while (scanner.hasNextLine()) {
                    String value = scanner.nextLine().trim();
                    if (!value.isEmpty()) {
                        stringBuilder.append(value).append(", ");
                    }
                }
                stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
                stringBuilder.append(")\n");
                stringBuilder.append("\n");
            }

            stringBuilder.append("require(pgirmess)\n");
            stringBuilder.append("AR1 <-cbind(");

            StringBuilder contextNames = new StringBuilder();
            for (String context : contexts) {
                contextNames.append(context).append(", ");
            }
            contextNames.delete(contextNames.length() - 2, contextNames.length());

            stringBuilder.append(contextNames.toString()).append(")\n");
            stringBuilder.append("result<-friedman.test(AR1)\n");
            stringBuilder.append("\n");
            stringBuilder.append("m<-data.frame(result$statistic,result$p.value)\n");
            stringBuilder.append("write.csv2(m,file=\"./").append(directoryPath).append("friedman.csv\")\n");
            stringBuilder.append("\n");
            stringBuilder.append("pos_teste<-friedmanmc(AR1)\n");
            stringBuilder.append("write.csv2(pos_teste,file=\"./").append(directoryPath).append("friedman-compara.csv\")\n");
            stringBuilder.append("png(file=\"./").append(directoryPath).append("friedman-boxplot.png\", width=400, height=400)\n");
            stringBuilder.append("boxplot(").append(contextNames.toString());

            contextNames = new StringBuilder();
            for (String context : contexts) {
                contextNames.append("\"").append(context).append("\", ");
            }
            contextNames.delete(contextNames.length() - 2, contextNames.length());

            stringBuilder.append(", names=c(").append(contextNames.toString()).append("))");

            friedman.write(stringBuilder.toString());
        }

        ProcessBuilder processBuilder = new ProcessBuilder("R", "--no-save");
        Process process = processBuilder.redirectInput(new File("./" + directoryPath + "friedman_script.txt")).start();
        process.waitFor();
    }

    private static void runKruskal(String directoryPath, String[] contexts) throws IOException, InterruptedException {
        try (FileWriter kruskal = new FileWriter(directoryPath + "kruskal_script.txt")) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ARRAY").append("<- c(");
            for (String context : contexts) {
                Scanner scanner = new Scanner(new FileInputStream(directoryPath + context + "/HYPERVOLUME_RESULT.txt"));
                while (scanner.hasNextLine()) {
                    String value = scanner.nextLine().trim();
                    if (!value.isEmpty()) {
                        stringBuilder.append(value).append(", ");
                    }
                }
            }
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
            stringBuilder.append(")\n");
            stringBuilder.append("\n");

            stringBuilder.append("require(pgirmess)\n");

            StringBuilder contextNames = new StringBuilder();
            for (String context : contexts) {
                contextNames.append("\"").append(context).append("\", ");
            }
            contextNames.delete(contextNames.length() - 2, contextNames.length());

            stringBuilder.append("categs<-as.factor(rep(c(").append(contextNames.toString()).append("),each=").append(EXECUTIONS).append("));");
            stringBuilder.append("\n");
            stringBuilder.append("result<-kruskal.test(ARRAY,categs)\n");
            stringBuilder.append("\n");
            stringBuilder.append("m<-data.frame(result$statistic,result$p.value)\n");
            stringBuilder.append("write.csv2(m,file=\"./").append(directoryPath).append("kruskal.csv\")\n");
            stringBuilder.append("\n");
            stringBuilder.append("pos_teste<-kruskalmc(ARRAY,categs)\n");
            stringBuilder.append("write.csv2(pos_teste,file=\"./").append(directoryPath).append("kruskal-compara.csv\")\n");

            kruskal.write(stringBuilder.toString());
        }

        ProcessBuilder processBuilder = new ProcessBuilder("R", "--no-save");
        Process process = processBuilder.redirectInput(new File("./" + directoryPath + "kruskal_script.txt")).start();
        process.waitFor();
    }

    private static void runWilcoxon(String directoryPath, String[] contexts) throws IOException, InterruptedException {
        try (FileWriter wilcoxon = new FileWriter(directoryPath + "wilcoxon_script.txt")) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String context : contexts) {
                stringBuilder.append(context).append("<- c(");
                Scanner scanner = new Scanner(new FileInputStream(directoryPath + context + "/HYPERVOLUME_RESULT.txt"));
                while (scanner.hasNextLine()) {
                    String value = scanner.nextLine().trim();
                    if (!value.isEmpty()) {
                        stringBuilder.append(value).append(", ");
                    }
                }
                stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
                stringBuilder.append(")\n");
                stringBuilder.append("\n");
            }

            StringBuilder contextNames = new StringBuilder();
            for (String context : contexts) {
                contextNames.append(context).append(", ");
            }
            contextNames.delete(contextNames.length() - 2, contextNames.length());

            stringBuilder.append("wilcox.test(").append(contextNames.toString()).append(")\n");
            stringBuilder.append("png(file=\"./").append(directoryPath).append("wilcoxon-boxplot.png\", width=500, height=500)\n");
            stringBuilder.append("boxplot(").append(contextNames.toString());

            contextNames = new StringBuilder();
            for (String context : contexts) {
                contextNames.append("\"").append(context).append("\", ");
            }
            contextNames.delete(contextNames.length() - 2, contextNames.length());

            stringBuilder.append(", names=c(").append(contextNames.toString()).append("))");

            wilcoxon.write(stringBuilder.toString());
        }

        ProcessBuilder processBuilder = new ProcessBuilder("R", "--no-save");
        Process process = processBuilder.redirectInput(new File("./" + directoryPath + "wilcoxon_script.txt")).start();
        process.waitFor();
    }

    public static SolutionSet removeDominadas(SolutionSet result) {
        boolean dominador, dominado;
        double valor1 = 0;
        double valor2 = 0;

        for (int i = 0; i < (result.size() - 1); i++) {
            for (int j = (i + 1); j < result.size(); j++) {
                dominador = true;
                dominado = true;

                for (int k = 0; k < result.get(i).numberOfObjectives(); k++) {
                    valor1 = result.get(i).getObjective(k);
                    valor2 = result.get(j).getObjective(k);

                    if (valor1 > valor2 || dominador == false) {
                        dominador = false;
                    } else if (valor1 <= valor2) {
                        dominador = true;
                    }

                    if (valor2 > valor1 || dominado == false) {
                        dominado = false;
                    } else if (valor2 < valor1) {
                        dominado = true;
                    }
                }

                if (dominador) {
                    result.remove(j);
                    j -= 1;
                } else if (dominado) {
                    result.remove(i);
                    j = i;
                }
            }
        }

        return result;
    }

    private static void executeArchitectureStats(String directoryPath, String pla, String[] contexts) throws Exception {
        ArchitectureBuilder architectureBuilder = new ArchitectureBuilder();

//        String originalDirectory = pla + "/Papyrus/";
//        ReaderConfig.setPathToProfileSMarty(originalDirectory + "smarty.profile.uml");
//        ReaderConfig.setPathToProfileConcerns(originalDirectory + "concerns.profile.uml");
//        ReaderConfig.setPathProfileRelationship(originalDirectory + "relationships.profile.uml");
//        ReaderConfig.setPathToProfilePatterns(originalDirectory + "patterns.profile.uml");
//
//        Architecture original = architectureBuilder.create(ArchitectureRepository.getPlaPath(pla));
        try (FileWriter statistics = new FileWriter(directoryPath + "/ELEMENTS_STATISTICS.txt")) {
            statistics.write("\\toprule\n");
            statistics.write("Experimento & Elementos / Relacionamentos & Elementos / Pacotes \\\\ \\midrule\n");

            for (String context : contexts) {
                DescriptiveStatistics classStatistics = new DescriptiveStatistics();
                DescriptiveStatistics interfaceStatistics = new DescriptiveStatistics();
                DescriptiveStatistics packageStatistics = new DescriptiveStatistics();
                DescriptiveStatistics dependencyStatistics = new DescriptiveStatistics();
                DescriptiveStatistics usageStatistics = new DescriptiveStatistics();
                DescriptiveStatistics associationStatistics = new DescriptiveStatistics();

                File outputFolder = new File(directoryPath + context + "/output/");

                String resourcesDirectory = outputFolder.getAbsolutePath() + "/resources/";
                ReaderConfig.setPathToProfileSMarty(resourcesDirectory + "smarty.profile.uml");
                ReaderConfig.setPathToProfileConcerns(resourcesDirectory + "concerns.profile.uml");
                ReaderConfig.setPathProfileRelationship(resourcesDirectory + "relationships.profile.uml");
                ReaderConfig.setPathToProfilePatterns(resourcesDirectory + "patterns.profile.uml");

                File[] files = outputFolder.listFiles();
                for (File file : files) {
                    if (file.getName().endsWith(".uml")) {
                        try {
                            Architecture architecture = architectureBuilder.create(file.getAbsolutePath());

                            classStatistics.addValue(architecture.getAllClasses().size());
                            interfaceStatistics.addValue(architecture.getAllInterfaces().size());
                            packageStatistics.addValue(architecture.getAllPackages().size());
                            dependencyStatistics.addValue(architecture.getRelationshipHolder().getAllDependencies().size());
                            usageStatistics.addValue(architecture.getRelationshipHolder().getAllUsage().size());
                            associationStatistics.addValue(architecture.getRelationshipHolder().getAllAssociations().size());
                        } catch (Exception ex) {
//                        ex.printStackTrace();
                        }
                    }
                }

                double elementos = classStatistics.getMean() + interfaceStatistics.getMean();
                double relacionamentos = dependencyStatistics.getMean() + usageStatistics.getMean() + associationStatistics.getMean();

                statistics.write(context);
                statistics.write(" & ");
                statistics.write("" + (elementos / relacionamentos));
                statistics.write(" & ");
                statistics.write("" + (elementos / packageStatistics.getMean()));
                statistics.write(" \\\\ \\hline\n");

            }
        }
    }
}
