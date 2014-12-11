package mutation;

import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Package;
import identification.ClientServerIdentification;
import identification.LayerIdentification;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import jmetal.core.Solution;
import jmetal.operators.mutation.Mutation;
import jmetal.problems.OPLA;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import operators.AddClass;
import operators.AddPackage;
import operators.FeatureDriven;
import operators.MoveAttribute;
import operators.MoveMethod;
import operators.MoveOperation;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import pojo.Style;
import util.OperatorUtil;
import util.StyleUtil;

public class PLAFeatureMutationConstraints extends Mutation {

    private static final long serialVersionUID = 9039316729379302747L;
    public static Logger LOGGER = LogManager.getLogger(PLAFeatureMutationConstraints.class.getName());
    private String name = "no";
    private Double mutationProbability_ = null;
    private String style;
    public static int featureDrivenMoveOperationToComponent = 0;
    public static int featureDrivenMoveOperationToComponentCreateInterface = 0;

    public PLAFeatureMutationConstraints(HashMap<String, Object> parameters, String style) {
        super(parameters);
        this.style = style;

        if (parameters.get("probability") != null) {
            mutationProbability_ = (Double) parameters.get("probability");
        }
    }

    public void doMutation(double probability, Solution solution) throws Exception {
        if (solution.getDecisionVariables()[0].getVariableType().toString().equals("class " + Architecture.ARCHITECTURE_TYPE)) {
            Architecture architecture = ((Architecture) solution.getDecisionVariables()[0]);
            List<Style> list = new ArrayList<>();
            
            switch (style) {
                case "layer":
                    LayerIdentification.clearPackagesFromLayers();
                    LayerIdentification.addPackagesToLayers(architecture);

                    list.addAll(LayerIdentification.getLISTLAYERS());
                    break;
                case "clientserver":
                    ClientServerIdentification.clearPackagesFromClientsServers();
                    ClientServerIdentification.addPackagesToClientsServers(architecture);

                    list.addAll(ClientServerIdentification.getLISTCLIENTS());
                    list.addAll(ClientServerIdentification.getLISTSERVERS());
                    break;
            }

            int r = PseudoRandom.randInt(0, 5);
            switch (r) {
                case 0:
                    FeatureDriven featureDriven = new FeatureDriven();
                    featureDriven.doMutation(probability, architecture, style, list);
                    name = "featuredriven";
                    break;
                case 1:
                    MoveMethod moveMethod = new MoveMethod();
                    moveMethod.doMutation(probability, architecture, style, list);
                    name = "movemethod";
                    break;
                case 2:
                    MoveAttribute moveAttribute = new MoveAttribute();
                    moveAttribute.doMutation(probability, architecture, style, list);
                    name = "moveattribute";
                    break;
                case 3:
                    MoveOperation moveOperation = new MoveOperation();
                    moveOperation.doMutation(probability, architecture, style, list);
                    name = "moveoperation";
                    break;
                case 4:
                    AddClass addClass = new AddClass();
                    addClass.doMutation(probability, architecture, style, list);
                    name = "addclass";
                    break;
                case 5:
                    AddPackage addPackage = new AddPackage();
                    addPackage.doMutation(probability, architecture, style, list);
                    name = "addpackage";
                    break;
            }
            System.out.println("Operador " +name);

        } else {
            Configuration.logger_.log(Level.SEVERE, "doMutation: invalid type. " + "{0}", solution.getDecisionVariables()[0].getVariableType());
            java.lang.Class<String> cls = java.lang.String.class;
            String name = cls.getName();
            throw new JMException("Exception in " + name + ".doMutation()");
        }
    }

//TODO: Modificar o execute
    @Override
    public Object execute(Object object) throws Exception {
        Solution solution = (Solution) object;
        Double probability = (Double) getParameter("probability");
        if (probability == null) {
            Configuration.logger_.severe("FeatureMutationConstraints.execute: probability not specified");
            java.lang.Class<String> cls = java.lang.String.class;
            String name = cls.getName();

            throw new JMException(
                    "Exception in " + name + ".execute()");
        }

        Solution solutionBeforeMutation = new Solution(solution);
        this.doMutation(mutationProbability_, solution);

        if (!OperatorUtil.isValidSolution(((Architecture) solution.getDecisionVariables()[0]))) {
            Architecture clone;
            //substituido para corrigir bug da solução inválida
            //clone = ((Architecture) solution.getDecisionVariables()[0]).deepClone();
            clone = ((Architecture) solutionBeforeMutation.getDecisionVariables()[0]).deepClone();
            solution.getDecisionVariables()[0] = clone;
            OPLA.contDiscardedSolutions_++;
        }

        return solution;
    }

}
