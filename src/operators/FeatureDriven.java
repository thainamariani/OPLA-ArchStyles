/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operators;

import arquitetura.representation.Architecture;
import arquitetura.representation.Concern;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import jmetal.problems.OPLA;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import pojo.Layer;
import pojo.Style;
import util.OperatorUtil;

/**
 *
 * @author Thainá
 */
public class FeatureDriven implements OperatorConstraints {

    @Override
    public void doMutation(double probability, Architecture architecture, String style, List<? extends Style> styles) throws JMException {
        if (PseudoRandom.randDouble() < probability) {
            if (style.equals("layer")) {
                doMutationLayer(probability, architecture, (List<Layer>) styles);
            } else {
                //doMutationClientServer(probability, architecture, (List<ClientServer>) styles);
            }
        }
    }

    @Override
    public void doMutationLayer(double probability, Architecture architecture, List<Layer> list) {
        try {
            final List<arquitetura.representation.Package> allComponents = new ArrayList<arquitetura.representation.Package>(architecture.getAllPackages());
            if (!allComponents.isEmpty()) {
                final arquitetura.representation.Package selectedComp = OperatorUtil.randomObject(allComponents);
                List<Concern> concernsSelectedComp = new ArrayList<Concern>(selectedComp.getAllConcerns());
                
                if (concernsSelectedComp.size() > 1) {
                    final Concern selectedConcern = OperatorUtil.randomObject(concernsSelectedComp);
                    List<arquitetura.representation.Package> allComponentsAssignedOnlyToConcern = new ArrayList<arquitetura.representation.Package>(OperatorUtil.searchComponentsAssignedToConcern(selectedConcern, allComponents));
                    if (allComponentsAssignedOnlyToConcern.isEmpty()) {
                        OPLA.contComp_++;
                        OperatorUtil.modularizeConcernInComponent(allComponents, architecture.createPackage("Package" + OPLA.contComp_ + OperatorUtil.getSuffix(selectedComp)), selectedConcern, architecture);
                    } else {
                        if (allComponentsAssignedOnlyToConcern.size() == 1) {
                            OperatorUtil.modularizeConcernInComponent(allComponents, allComponentsAssignedOnlyToConcern.get(0), selectedConcern, architecture);
                        } else {
                            OperatorUtil.modularizeConcernInComponent(allComponents, OperatorUtil.randomObject(allComponentsAssignedOnlyToConcern), selectedConcern, architecture);
                        }
                    }
                    allComponentsAssignedOnlyToConcern.clear();
                }
                concernsSelectedComp.clear();
                allComponents.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doMutationClientServer(double probability, Architecture architecture, List<Style> list) {
    }

}
