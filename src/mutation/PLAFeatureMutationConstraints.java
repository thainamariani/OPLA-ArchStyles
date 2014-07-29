package mutation;

import arquitetura.representation.Architecture;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import jmetal.core.Solution;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

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
import pojo.Style;
import util.OperatorUtil;

public class PLAFeatureMutationConstraints extends Mutation {

    private static final long serialVersionUID = 9039316729379302747L;
    static Logger LOGGER = LogManager.getLogger(PLAFeatureMutationConstraints.class.getName());

    private Double mutationProbability_ = null;
    private String style;
    private List<? extends Style> list;

    public PLAFeatureMutationConstraints(HashMap<String, Object> parameters, String style, List<? extends Style> list) {
        super(parameters);
        this.style = style;
        this.list = list;

        if (parameters.get("probability") != null) {
            mutationProbability_ = (Double) parameters.get("probability");
        }
    }

    public void doMutation(double probability, Solution solution) throws Exception {
        if (solution.getDecisionVariables()[0].getVariableType().toString().equals("class " + Architecture.ARCHITECTURE_TYPE)) {
            Architecture architecture = ((Architecture) solution.getDecisionVariables()[0]);
            int r = PseudoRandom.randInt(0, 5);
            switch (r) {
                case 0:
                    FeatureDriven featureDriven = new FeatureDriven();
                    featureDriven.doMutation(probability, architecture, style, list);
                case 1:
                    MoveMethod moveMethod = new MoveMethod();
                    moveMethod.doMutation(probability, architecture, style, list);
                case 2:
                    MoveAttribute moveAttribute = new MoveAttribute();
                    moveAttribute.doMutation(probability, architecture, style, list);
                case 3:
                    MoveOperation moveOperation = new MoveOperation();
                    moveOperation.doMutation(probability, architecture, style, list);
                case 4:
                    AddClass addClass = new AddClass();
                    addClass.doMutation(probability, architecture, style, list);
                case 5:
                    AddPackage addPackage = new AddPackage();
                    addPackage.doMutation(probability, architecture, style, list);
            }
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

        this.doMutation(mutationProbability_, solution);

        if (!OperatorUtil.isValidSolution(((Architecture) solution.getDecisionVariables()[0]))) {
            Architecture clone;
            clone = ((Architecture) solution.getDecisionVariables()[0]).deepClone();
            solution.getDecisionVariables()[0] = clone;
            OPLA.contDiscardedSolutions_++;
        }

        return solution;
    }

}