/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operators;

import arquitetura.representation.Architecture;
import arquitetura.representation.Concern;
import arquitetura.representation.Package;
import identification.ClientServerIdentification;
import java.util.ArrayList;
import java.util.List;
import jmetal.problems.OPLA;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import mutation.PLAFeatureMutationConstraints;
import pojo.Client;
import pojo.Layer;
import pojo.Server;
import pojo.Style;
import util.OperatorUtil;
import util.ParametersRepository;
import util.StyleUtil;

/**
 *
 * @author Thainá
 */
public class FeatureDriven implements OperatorConstraints {

    private boolean suffix;
    private List<arquitetura.representation.Package> modularizationPackages = new ArrayList<>();

    @Override
    public void doMutation(double probability, Architecture architecture, String style, List<? extends Style> styles) throws JMException {
        if (PseudoRandom.randDouble() < probability) {
            if (style.equals("layer")) {
                doMutationLayer(probability, architecture, (List<Layer>) styles);
            } else if (style.equals("clientserver")) {
                doMutationClientServer(probability, architecture, (List<Style>) styles);
            } else if (style.equals("aspect")) {
                doMutationAspect(probability, architecture);
            }
        }
    }

    public void doMutationAspect(double probability, Architecture architecture) {
        try {
            final List<arquitetura.representation.Package> allComponents = new ArrayList<arquitetura.representation.Package>(architecture.getAllPackages());
            if (!allComponents.isEmpty()) {
                final arquitetura.representation.Package selectedComp = OperatorUtil.randomObject(allComponents);
                List<Concern> concernsSelectedComp = new ArrayList<Concern>(selectedComp.getAllConcerns());

                if (concernsSelectedComp.size() > 1) {
                    //interesse selecionado
                    final Concern selectedConcern = OperatorUtil.randomObject(concernsSelectedComp);
                    ParametersRepository.setSelectedConcern(selectedConcern);

                    List<arquitetura.representation.Package> modularizationPackages = new ArrayList<>();
                    arquitetura.representation.Package newComp = null;
                    List<arquitetura.representation.Package> packagesLayerAssignedOnlyToConcern = new ArrayList<arquitetura.representation.Package>(OperatorUtil.searchComponentsAssignedToConcern(selectedConcern, allComponents));
                    if (packagesLayerAssignedOnlyToConcern.isEmpty()) {
                        String name = OperatorUtil.getSuffix(selectedComp);
                        newComp = architecture.createPackage("Package" + OPLA.contComp_ + name);
                        OPLA.contComp_++;

                        OperatorUtil.modularizeConcernInComponent(true, null, null, newComp, selectedConcern, architecture);
                    } else {
                        if (packagesLayerAssignedOnlyToConcern.size() == 1) {
                            modularizationPackages.add(packagesLayerAssignedOnlyToConcern.get(0));
                            OperatorUtil.modularizeConcernInComponent(true, null, null, packagesLayerAssignedOnlyToConcern.get(0), selectedConcern, architecture);
                        } else {
                            Package pac = OperatorUtil.randomObject(packagesLayerAssignedOnlyToConcern);
                            modularizationPackages.add(pac);
                            OperatorUtil.modularizeConcernInComponent(true, null, null, pac, selectedConcern, architecture);
                        }
                    }
                    packagesLayerAssignedOnlyToConcern.clear();

                    if (newComp != null) {
                        if (newComp.getElements().isEmpty()) {
                            architecture.removePackage(newComp);
                        } else {
                            //adiciona o novo pacote na lista de camadas
                            modularizationPackages.add(newComp);
                        }
                    }
                    ParametersRepository.setModularizationPackages(modularizationPackages);
                    PLAFeatureMutationConstraints.LOGGER.info("----------------------------");
                    PLAFeatureMutationConstraints.LOGGER.info("Executado FeatureDriven");
                    PLAFeatureMutationConstraints.LOGGER.info("Concern modularizado: " + ParametersRepository.getSelectedConcern());

                }
                concernsSelectedComp.clear();
                allComponents.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
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
                            layer.getPackages().add(newComp);
                            OperatorUtil.modularizeConcernInComponent(false, list, layer, newComp, selectedConcern, architecture);
                        } else {
                            if (packagesLayerAssignedOnlyToConcern.size() == 1) {
                                modularizationPackages.add(packagesLayerAssignedOnlyToConcern.get(0));
                                OperatorUtil.modularizeConcernInComponent(false, list, layer, packagesLayerAssignedOnlyToConcern.get(0), selectedConcern, architecture);
                            } else {
                                Package pac = OperatorUtil.randomObject(packagesLayerAssignedOnlyToConcern);
                                modularizationPackages.add(pac);
                                OperatorUtil.modularizeConcernInComponent(false, list, layer, pac, selectedConcern, architecture);
                            }
                        }
                        packagesLayerAssignedOnlyToConcern.clear();

                        if (newComp != null) {
                            if (newComp.getElements().isEmpty()) {
                                architecture.removePackage(newComp);
                                layer.getPackages().remove(newComp);
                            } else {
                                //adiciona o novo pacote na lista de camadas
                                modularizationPackages.add(newComp);
                            }
                        }
                    }
                    ParametersRepository.setModularizationPackages(modularizationPackages);
                    PLAFeatureMutationConstraints.LOGGER.info("----------------------------");
                    PLAFeatureMutationConstraints.LOGGER.info("Executado FeatureDriven");
                    PLAFeatureMutationConstraints.LOGGER.info("Concern modularizado: " + ParametersRepository.getSelectedConcern());

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
        try {
            final List<arquitetura.representation.Package> allComponents = new ArrayList<arquitetura.representation.Package>(architecture.getAllPackages());
            if (!allComponents.isEmpty()) {
                final arquitetura.representation.Package selectedComp = OperatorUtil.randomObject(allComponents);
                List<Concern> concernsSelectedComp = new ArrayList<Concern>(selectedComp.getAllConcerns());

                if (concernsSelectedComp.size() > 1) {
                    //interesse selecionado
                    final Concern selectedConcern = OperatorUtil.randomObject(concernsSelectedComp);
                    ParametersRepository.setSelectedConcern(selectedConcern);

                    //MODULARIZAÇÃO NOS SERVIDORES
                    mutationServer(architecture, selectedConcern, list);

                    //MODULARIZAÇÃO NOS CLIENTES
                    mutationClient(architecture, selectedConcern, list);

                    ParametersRepository.setModularizationPackages(modularizationPackages);
                }
                concernsSelectedComp.clear();
                allComponents.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //métodos adicionais
    public String getSuffixPrefix(Style styleSelect) {
        String name = "";
        int result = PseudoRandom.randInt(0, 1);
        if (styleSelect.getSufixos().isEmpty()) {
            result = 1;
        } else if (styleSelect.getPrefixos().isEmpty()) {
            result = 0;
        }

        if (result == 0) {
            suffix = true;
            name = OperatorUtil.randomObject(styleSelect.getSufixos());
        } else {
            suffix = false;
            name = OperatorUtil.randomObject(styleSelect.getPrefixos());
        }

        return name;
    }

    public void mutationServer(Architecture architecture, Concern selectedConcern, List<Style> list) {
        List<Package> serversPackages = new ArrayList<>();
        for (Style style : ClientServerIdentification.getLISTSERVERS()) {
            serversPackages.addAll(style.getPackages());
        }

        arquitetura.representation.Package newComp = null;
        Server serverSelect = null;
        List<arquitetura.representation.Package> packagesLayerAssignedOnlyToConcern = new ArrayList<arquitetura.representation.Package>(OperatorUtil.searchComponentsAssignedToConcern(selectedConcern, serversPackages));
        if (packagesLayerAssignedOnlyToConcern.isEmpty()) {
            //select a server
            serverSelect = OperatorUtil.randomObject(ClientServerIdentification.getLISTSERVERS());
            String name = getSuffixPrefix(serverSelect);
            if (suffix) {
                newComp = architecture.createPackage("Package" + OPLA.contComp_ + name);
            } else {
                newComp = architecture.createPackage(name + "Package" + OPLA.contComp_);
            }
            OPLA.contComp_++;
            serverSelect.getPackages().add(newComp);
            OperatorUtil.modularizeConcernInComponent(false, list, serverSelect, newComp, selectedConcern, architecture);
        } else {
            if (packagesLayerAssignedOnlyToConcern.size() == 1) {
                modularizationPackages.add(packagesLayerAssignedOnlyToConcern.get(0));
                serverSelect = (Server) StyleUtil.returnClientServer(packagesLayerAssignedOnlyToConcern.get(0), list);
                OperatorUtil.modularizeConcernInComponent(false, list, serverSelect, packagesLayerAssignedOnlyToConcern.get(0), selectedConcern, architecture);
            } else {
                Package pac = OperatorUtil.randomObject(packagesLayerAssignedOnlyToConcern);
                modularizationPackages.add(pac);
                serverSelect = (Server) StyleUtil.returnClientServer(pac, list);
                OperatorUtil.modularizeConcernInComponent(false, list, serverSelect, pac, selectedConcern, architecture);
            }
        }
        packagesLayerAssignedOnlyToConcern.clear();

        if (newComp != null) {
            if (newComp.getElements().isEmpty()) {
                architecture.removePackage(newComp);
                serverSelect.getPackages().remove(newComp);
            } else {
                //adiciona o novo pacote na lista de servidores
                modularizationPackages.add(newComp);
            }
        }

    }

    public void mutationClient(Architecture architecture, Concern selectedConcern, List<Style> list) {
        for (Client client : ClientServerIdentification.getLISTCLIENTS()) {
            arquitetura.representation.Package newComp = null;
            List<arquitetura.representation.Package> packagesLayerAssignedOnlyToConcern = new ArrayList<arquitetura.representation.Package>(OperatorUtil.searchComponentsAssignedToConcern(selectedConcern, client.getPackages()));
            if (packagesLayerAssignedOnlyToConcern.isEmpty()) {
                String name = getSuffixPrefix(client);
                if (suffix) {
                    newComp = architecture.createPackage("Package" + OPLA.contComp_ + name);
                } else {
                    newComp = architecture.createPackage(name + "Package" + OPLA.contComp_);
                }
                OPLA.contComp_++;
                client.getPackages().add(newComp);
                OperatorUtil.modularizeConcernInComponent(false, list, client, newComp, selectedConcern, architecture);
            } else {
                if (packagesLayerAssignedOnlyToConcern.size() == 1) {
                    modularizationPackages.add(packagesLayerAssignedOnlyToConcern.get(0));
                    OperatorUtil.modularizeConcernInComponent(false, list, client, packagesLayerAssignedOnlyToConcern.get(0), selectedConcern, architecture);
                } else {
                    Package pac = OperatorUtil.randomObject(packagesLayerAssignedOnlyToConcern);
                    modularizationPackages.add(pac);
                    OperatorUtil.modularizeConcernInComponent(false, list, client, pac, selectedConcern, architecture);
                }
            }
            packagesLayerAssignedOnlyToConcern.clear();

            if (newComp != null) {
                if (newComp.getElements().isEmpty()) {
                    architecture.removePackage(newComp);
                    client.getPackages().remove(newComp);
                } else {
                    //adiciona o novo pacote na lista de clients
                    modularizationPackages.add(newComp);
                }
            }

        }
    }

}
