package operators;

import arquitetura.representation.Architecture;
import java.util.HashMap;
import java.util.List;

import jmetal.core.Solution;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import jmetal.operators.mutation.Mutation;
import jmetal.problems.OPLA;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import pojo.Style;
import util.OperatorUtil;

public class PLAFeatureMutationConstraints extends Mutation {

    private static final long serialVersionUID = 9039316729379302747L;
    static Logger LOGGER = LogManager.getLogger(PLAFeatureMutationConstraints.class.getName());

    private Double mutationProbability_ = null;
    private String style;
    private List<Style> list;

    public PLAFeatureMutationConstraints(HashMap<String, Object> parameters, String style, List<Style> list) {
        super(parameters);
        this.style = style;
        this.list = list;

        if (parameters.get("probability") != null) {
            mutationProbability_ = (Double) parameters.get("probability");
        }
    }

    public void doMutation(double probability, Solution solution) throws Exception {
        Architecture architecture = ((Architecture) solution.getDecisionVariables()[0]);
        MoveMethod moveMethod = new MoveMethod();
        moveMethod.doMutation(probability, architecture, style, list);
        
//        String scope = "sameComponent"; //"allComponents" usar "sameComponent" para que a troca seja realizada dentro do mesmo componente da arquitetura
//        String scopeLevels = "allLevels"; //usar "oneLevel" para não verificar a presença de interesses nos atributos e métodos
//
//        //int r = PseudoRandom.randInt(0, 5);
////        switch (r) {
////                case 0:FeatureMutation(probability, solution, scopeLevels); break;
////                case 1: MoveMethodMutation(probability, solution, scope); break;
////                case 2: MoveAttributeMutation(probability, solution, scope); break;
////                case 3: MoveOperationMutation(probability, solution); break;
////                case 4: AddClassMutation(probability, solution, scope); break;
////                case 5: AddManagerClassMutation(probability, solution); break;
//    }
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
