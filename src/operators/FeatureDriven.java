/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operators;

import arquitetura.representation.Architecture;
import arquitetura.representation.Concern;
import arquitetura.representation.Package;
import identification.LayerIdentification;
import java.util.ArrayList;
import java.util.List;
import jmetal.problems.OPLA;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import pojo.Layer;
import pojo.Style;
import util.OperatorUtil;
import util.ParametersRepository;

/**
 *
 * @author Thainá
 */
public class FeatureDriven implements OperatorConstraints {

    private boolean suffix;

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
                    //interesse selecionado
                    final Concern selectedConcern = OperatorUtil.randomObject(concernsSelectedComp);
                    ParametersRepository.setSelectedConcern(selectedConcern);

                    //adicionado: cria ou seleciona um pacote de modularização para cada camada
                    List<arquitetura.representation.Package> modularizationPackages = new ArrayList<>();
                    for (Layer layer : list) {
                        arquitetura.representation.Package newComp = null;
                        List<arquitetura.representation.Package> packagesLayerAssignedOnlyToConcern = new ArrayList<arquitetura.representation.Package>(OperatorUtil.searchComponentsAssignedToConcern(selectedConcern, layer.getPackages()));
                        if (packagesLayerAssignedOnlyToConcern.isEmpty()) {
                            String name = getSuffixPrefix(layer);
                            if (suffix) {
                                newComp = architecture.createPackage("Package" + OPLA.contComp_ + name);
                            } else {
                                newComp = architecture.createPackage(name + "Package" + OPLA.contComp_);
                            }
                            OPLA.contComp_++;
                            OperatorUtil.modularizeConcernInComponent(list, layer, newComp, selectedConcern, architecture);
                        } else {
                            if (packagesLayerAssignedOnlyToConcern.size() == 1) {
                                modularizationPackages.add(packagesLayerAssignedOnlyToConcern.get(0));
                                OperatorUtil.modularizeConcernInComponent(list, layer, packagesLayerAssignedOnlyToConcern.get(0), selectedConcern, architecture);
                            } else {
                                Package pac = OperatorUtil.randomObject(packagesLayerAssignedOnlyToConcern);
                                modularizationPackages.add(pac);
                                OperatorUtil.modularizeConcernInComponent(list, layer, pac, selectedConcern, architecture);
                            }
                        }
                        packagesLayerAssignedOnlyToConcern.clear();

                        if (newComp != null) {
                            if (newComp.getElements().isEmpty()) {
                                architecture.removePackage(newComp);
                            } else {
                                //adiciona o novo pacote na lista de camadas
                                layer.getPackages().add(newComp);
                                for (Layer l : LayerIdentification.getLISTLAYERS()) {
                                    if (l.getNumero() == layer.getNumero()) {
                                        l = layer;
                                    }
                                }
                                modularizationPackages.add(newComp);
                            }
                        }
                    }
                    ParametersRepository.setModularizationPackages(modularizationPackages);
                }
                concernsSelectedComp.clear();
                allComponents.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doMutationClientServer(double probability, Architecture architecture, List<Style> list
    ) {
    }

    //métodos adicionais
    public String getSuffixPrefix(Layer layerSelect) {
        String name = "";
        int result = PseudoRandom.randInt(0, 1);
        if (layerSelect.getSufixos().isEmpty()) {
            result = 1;
        } else if (layerSelect.getPrefixos().isEmpty()) {
            result = 0;
        }

        if (result == 0) {
            suffix = true;
            name = OperatorUtil.randomObject(layerSelect.getSufixos());
        } else {
            suffix = false;
            name = OperatorUtil.randomObject(layerSelect.getPrefixos());
        }

        return name;
    }

}
