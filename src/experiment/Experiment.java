package experiment;

import arquitetura.io.ReaderConfig;
import identification.ClientServerIdentification;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import jmetal.core.Algorithm;
import jmetal.core.SolutionSet;
import jmetal.metaheuristics.nsgaII.NSGAII;
import jmetal.operators.crossover.Crossover;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.Mutation;
import jmetal.operators.mutation.PLAFeatureMutation;
import jmetal.operators.selection.Selection;
import jmetal.operators.selection.SelectionFactory;
import jmetal.problems.OPLA;
import jmetal.util.JMException;
import mutation.MutationFactory;
import mutation.PLAFeatureMutationConstraints;
import pojo.Style;
import util.ArchitectureRepository;

public class Experiment {

    public static int populationSize_;
    public static int maxEvaluations_;
    public static double mutationProbability_;
    public static double crossoverProbability_;

//--  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public static void main(String[] args) throws FileNotFoundException, IOException, JMException, ClassNotFoundException, Exception {

        //args = new String[]{"1", "1", "0", ArchitectureRepository.BET, "layer", "teste"};
        if (args.length < 6) {
            System.out.println("You need to inform the following parameters:");
            System.out.println("\t1 - Population Size (Integer);"
                    + "\n\t2 - Max Evaluations (Integer);"
                    + "\n\t3 - Mutation Probability (Double);"
                    + "\n\t4 - PLA path;"
                    + "\n\t5 - Architectural Style;"
                    + "\n\t6 - Context"); //nostyle, layer, clientserver
            System.exit(0);
        }

        int runsNumber = 10; //30; //10
        if (args[0] == null || args[0].trim().equals("")) {
            System.out.println("Missing population size argument.");
            System.exit(1);
        }
        try {
            populationSize_ = Integer.valueOf(args[0]); //100;
        } catch (NumberFormatException ex) {
            System.out.println("Population size argument not integer.");
            System.exit(1);
        }
        if (args[1] == null || args[1].trim().equals("")) {
            System.out.println("Missing max evaluations argument.");
            System.exit(1);
        }
        try {
            maxEvaluations_ = Integer.valueOf(args[1]); //300 geraçõeshttp://loggr.net/
        } catch (NumberFormatException ex) {
            System.out.println("Max evaluations argument not integer.");
            System.exit(1);
        }
        crossoverProbability_ = 0.0;
        if (args[2] == null || args[2].trim().equals("")) {
            System.out.println("Missing mutation probability argument.");
            System.exit(1);
        }
        try {
            mutationProbability_ = Double.valueOf(args[2]);
        } catch (NumberFormatException ex) {
            System.out.println("Mutation probability argument not double.");
            System.exit(1);
        }

        HashMap parameters; // Operator parameters
        parameters = new HashMap();
        parameters.put("probability", mutationProbability_);

        if (args[3] == null || args[3].trim().equals("")) {
            System.out.println("Missing PLA Path argument.");
            System.exit(1);
        }
        String pla = args[3];

        if (args[4] == null || args[4].trim().equals("")) {
            System.out.println("Missing architectural style argument.");
            System.exit(1);
        }
        String style = args[4];

        if (args[5] == null || args[5].trim().equals("")) {
            System.out.println("Missing context argument.");
            System.exit(1);
        }
        String context = args[5];

        boolean shouldPrintVariables = false;

        String plaName = getPlaName(pla);

        File directory = ArchitectureRepository.getOrCreateDirectory("experiment/" + plaName + "/" + context + "/");
        ArchitectureRepository.getOrCreateDirectory("experiment/" + plaName + "/" + context + "/manipulation");
        ArchitectureRepository.getOrCreateDirectory("experiment/" + plaName + "/" + context + "/output");

        ReaderConfig.setDirTarget("experiment/" + plaName + "/" + context + "/manipulation/");
        ReaderConfig.setDirExportTarget("experiment/" + plaName + "/" + context + "/output/");

        String plaDirectory = getPlaDirectory(pla);
        ReaderConfig.setPathToTemplateModelsDirectory(plaDirectory + "/");
        if (!plaName.equalsIgnoreCase("agm") && !plaName.equalsIgnoreCase("banking")) {
            System.out.println("MM, BET, BetClientServer");
            ReaderConfig.setPathToProfileSMarty(plaDirectory + "/resources/smarty.profile.uml");
            ReaderConfig.setPathToProfileConcerns(plaDirectory + "/resources/concerns.profile.uml");
            ReaderConfig.setPathProfileRelationship(plaDirectory + "/resources/relationships.profile.uml");
            ReaderConfig.setPathToProfilePatterns(plaDirectory + "/resources/patterns.profile.uml");
        } else {
            System.out.println("AGM ou Banking");
            ReaderConfig.setPathToProfileSMarty(plaDirectory + "/smarty.profile.uml");
            ReaderConfig.setPathToProfileConcerns(plaDirectory + "/concerns.profile.uml");
            ReaderConfig.setPathProfileRelationship(plaDirectory + "/relationships.profile.uml");
            ReaderConfig.setPathToProfilePatterns(plaDirectory + "/patterns.profile.uml");
        }

        String xmiFilePath = pla;

        OPLA problem = null;
        try {
            problem = new OPLA(xmiFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Algorithm algorithm;
        SolutionSet todasRuns = new SolutionSet();
        // Thelma - Dez2013 - adicao da linha abaixo
        SolutionSet allSolutions = new SolutionSet();

        Crossover crossover;
        Mutation mutation = null;
        Selection selection;

        algorithm = new NSGAII(problem);

        // Algorithm parameters
        algorithm.setInputParameter("populationSize", populationSize_);
        algorithm.setInputParameter("maxEvaluations", maxEvaluations_);

        //ADIÇÃO DO OPERADOR DE MUTAÇÃO - modificado para contemplar os estilos
        boolean execution = false;
        if (style.equals("layer")) {
            System.out.println("Adicionando operadores com restrições de camadas");
            if (StyleGui.verifyLayer(pla)) {
                mutation = MutationFactory.getMutationOperator("PLAFeatureMutationConstraints", parameters, style);
                execution = true;
            }
        } else if (style.equals("clientserver")) {
            if (StyleGui.verifyClientServer(pla)) {
                System.out.println("Adicionando operadores com restrições de cliente/servidor");
                List<Style> clientsservers = new ArrayList<>();
                clientsservers.addAll(ClientServerIdentification.LISTCLIENTS);
                clientsservers.addAll(ClientServerIdentification.LISTSERVERS);
                mutation = MutationFactory.getMutationOperator("PLAFeatureMutationConstraints", parameters, style);
                execution = true;
            }
        } else {
            System.out.println("Adicionando operadores sem restrições");
            //parâmetro style para esse caso representa o escopo (scope) (allComponents, sameComponent)
            mutation = jmetal.operators.mutation.MutationFactory.getMutationOperator("PLAFeatureMutation", parameters);
            execution = true;
        }

        // Mutation and Crossover
        parameters = new HashMap();
        parameters.put("probability", crossoverProbability_);
        crossover = CrossoverFactory.getCrossoverOperator("PLACrossover", parameters);

        if (execution) {
            // Selection Operator 
            parameters = null;
            selection = SelectionFactory.getSelectionOperator("BinaryTournament", parameters);

            // Add the operators to the algorithm
            algorithm.addOperator("crossover", crossover);
            algorithm.addOperator("mutation", mutation);
            algorithm.addOperator("selection", selection);

            System.out.println("\n================ NSGAII ================");
            System.out.println("Architectural Style: " + style);
            System.out.println("PLA: " + pla);
            System.out.println("Params:");
            System.out.println("\tPop -> " + populationSize_);
            System.out.println("\tMaxEva -> " + maxEvaluations_);
            System.out.println("\tCross -> " + crossoverProbability_);
            System.out.println("\tMuta -> " + mutationProbability_);

            long heapSize = Runtime.getRuntime().totalMemory();
            heapSize = (heapSize / 1024) / 1024;
            System.out.println("Heap Size: " + heapSize + "Mb\n");

            long time[] = new long[runsNumber];

            Hypervolume.clearFile(directory + "/HYPERVOLUME.txt");

            int totalDiscartedSolutions = 0;
            int totalInterfacesCriadas = 0;
            int totalChamadas = 0;
            String stringFinal = "";
            for (int runs = 0; runs < runsNumber; runs++) {

                // Execute the Algorithm
                long initTime = System.currentTimeMillis();
                SolutionSet resultFront = algorithm.execute();
                long estimatedTime = System.currentTimeMillis() - initTime;
                //System.out.println("Iruns: " + runs + "\tTotal time: " + estimatedTime);
                time[runs] = estimatedTime;

                resultFront = problem.removeDominadas(resultFront);
                resultFront = problem.removeRepetidas(resultFront);

                resultFront.printObjectivesToFile(directory + "/FUN_" + plaName + "_" + runs + ".txt");
                //resultFront.printVariablesToFile(directory + "/VAR_" + runs);
                resultFront.printInformationToFile(directory + "/INFO_" + plaName + "_" + runs + ".txt");
                // resultFront.saveVariablesToFile(directory + "/VAR_" + runs + "_");
                if (shouldPrintVariables) {
                    //resultFront.saveVariablesToFile("VAR_" + runs + "_");
                }

                Hypervolume.printFormatedHypervolumeFile(resultFront, directory + "/HYPERVOLUME.txt", true);

                //armazena as solucoes de todas runs
                todasRuns = todasRuns.union(resultFront);

                //Thelma - Dez2013
                allSolutions = allSolutions.union(resultFront);
                resultFront.printMetricsToFile(directory + "/Metrics_" + plaName + "_" + runs + ".txt");
                stringFinal += "Execução " + runs + " possui " + OPLA.contDiscardedSolutions_ + " soluções descartadas \n";
                totalDiscartedSolutions += OPLA.contDiscardedSolutions_;
                OPLA.contDiscardedSolutions_ = 0;
                if (style.equals("layer") || style.equals("clientserver")) {
                    stringFinal += "Execução " + runs + " possui " + PLAFeatureMutationConstraints.featureDrivenMoveOperationToComponent + " chamadas ao moveOperation do Feature-Driven \n";
                    stringFinal += "Execução " + runs + " possui " + PLAFeatureMutationConstraints.featureDrivenMoveOperationToComponentCreateInterface + " interfaces criadas no moveOperation do Feature-Driven \n\n";
                    totalChamadas += PLAFeatureMutationConstraints.featureDrivenMoveOperationToComponent;
                    totalInterfacesCriadas += PLAFeatureMutationConstraints.featureDrivenMoveOperationToComponentCreateInterface;
                    PLAFeatureMutationConstraints.featureDrivenMoveOperationToComponent = 0;
                    PLAFeatureMutationConstraints.featureDrivenMoveOperationToComponentCreateInterface = 0;
                } else {
                    stringFinal += "Execução " + runs + " possui " + PLAFeatureMutation.featureDrivenMoveOperationToComponent + " chamadas ao moveOperation do Feature-Driven \n";
                    stringFinal += "Execução " + runs + " possui " + PLAFeatureMutation.featureDrivenMoveOperationToComponentCreateInterface + " interfaces criadas no moveOperation do Feature-Driven \n\n";
                    totalChamadas += PLAFeatureMutation.featureDrivenMoveOperationToComponent;
                    totalInterfacesCriadas += PLAFeatureMutation.featureDrivenMoveOperationToComponentCreateInterface;
                    PLAFeatureMutation.featureDrivenMoveOperationToComponent = 0;
                    PLAFeatureMutation.featureDrivenMoveOperationToComponentCreateInterface = 0;
                }
            }
            stringFinal += "Total de soluçoes descartadas: " + totalDiscartedSolutions + "\n";
            stringFinal += "Total de chamadas: " + totalChamadas + "\n";
            stringFinal += "Total de interfaces criadas: " + totalInterfacesCriadas + "\n";
            System.out.println(stringFinal);

            todasRuns.printTimeToFile(directory + "/TIME_" + plaName, runsNumber, time, pla);

            todasRuns = problem.removeDominadas(todasRuns);
            todasRuns = problem.removeRepetidas(todasRuns);

            System.out.println("------    All Runs - Non-dominated solutions --------");
            todasRuns.printObjectivesToFile(directory + "/FUN_All_" + plaName + ".txt");
            //todasRuns.printVariablesToFile(directory + "/VAR_All");
            todasRuns.printInformationToFile(directory + "/INFO_All_" + plaName + ".txt");
            //todasRuns.saveVariablesToFile(directory + "/VAR_All_");
            if (shouldPrintVariables) {
                todasRuns.saveVariablesToFile("VAR_All_");
            }

            //Thelma - Dez2013
            todasRuns.printMetricsToFile(directory + "/Metrics_All_" + plaName + ".txt");
            todasRuns.printAllMetricsToFile(directory + "/FUN_Metrics_All_" + plaName + ".txt");

        }
    }

    public static String getPlaName(String pla) {
        int beginIndex = pla.lastIndexOf('/') + 1;
        int endIndex = pla.length() - 4;
        return pla.substring(beginIndex, endIndex);
    }

    public static String getPlaDirectory(String pla) {
        int stop = pla.lastIndexOf('/');
        return pla.substring(0, stop + 1);
    }

}
