/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operators;

import arquitetura.builders.ArchitectureBuilder;
import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Concern;
import identification.ClientServerIdentification;
import identification.LayerIdentification;
import java.util.ArrayList;
import java.util.List;
import jmetal.problems.OPLA;
import jmetal.util.PseudoRandom;
import org.apache.log4j.lf5.util.ResourceUtils;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
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
public class FeatureDrivenTest {

    boolean suffix = false;
    private List<arquitetura.representation.Package> modularizationPackages = new ArrayList<>();

    public FeatureDrivenTest() {
    }

    @Test
    public void testDoMutation() throws Exception {
    }

    @Test
    public void testDoMutationLayer() {

        try {
            ArchitectureBuilder builder = new ArchitectureBuilder();
            Architecture architecture = builder.create("C:/Users/Thainá/Documents/NetBeansProjects/OPLA-ArchStyles/test/models/archtest2/model.uml");
            LayerIdentification layerIdentification = new LayerIdentification(architecture);
            List<Layer> camadas = new ArrayList<>();
            Layer layer1 = new Layer();
            layer1.setNumero(1);
            List<String> sufixos = new ArrayList<>();
            List<String> prefixos = new ArrayList<>();
            sufixos.add("L1");
            layer1.setSufixos(sufixos);
            layer1.setPrefixos(prefixos);
            camadas.add(layer1);

            Layer layer2 = new Layer();
            layer2.setNumero(2);
            List<String> sufixos2 = new ArrayList<>();
            List<String> prefixos2 = new ArrayList<>();
            sufixos2.add("L2");
            layer2.setSufixos(sufixos2);
            layer2.setPrefixos(prefixos2);
            camadas.add(layer2);

            Layer layer3 = new Layer();
            layer3.setNumero(3);
            List<String> sufixos3 = new ArrayList<>();
            List<String> prefixos3 = new ArrayList<>();
            sufixos3.add("L3");
            layer3.setSufixos(sufixos3);
            layer3.setPrefixos(prefixos3);
            camadas.add(layer3);

            if (layerIdentification.isCorrect(camadas)) {
                final List<arquitetura.representation.Package> allComponents = new ArrayList<arquitetura.representation.Package>(architecture.getAllPackages());
                if (!allComponents.isEmpty()) {
                    //final arquitetura.representation.Package selectedComp = OperatorUtil.randomObject(allComponents);
                    arquitetura.representation.Package selectedComp = architecture.findPackageByName("Package1L1");
                    List<Concern> concernsSelectedComp = new ArrayList<Concern>(selectedComp.getAllConcerns());
                    Concern concernPlay = new Concern("play");
                    Concern concernBrickles = new Concern("brickles");
                    Assert.assertTrue(concernsSelectedComp.contains(concernPlay));
                    Assert.assertTrue(concernsSelectedComp.contains(concernBrickles));

                    if (concernsSelectedComp.size() > 1) {
                        //selecionado manualmente para testes
                        final Concern selectedConcern = concernPlay;
                        int cont0 = 0;
                        int cont1 = 0;
                        int cont2 = 0;
                        arquitetura.representation.Package package1l3 = architecture.findPackageByName("Package1L3");
                        arquitetura.representation.Package package2l3 = architecture.findPackageByName("Package2L3");
                        for (Layer layer : LayerIdentification.getLISTLAYERS()) {
                            arquitetura.representation.Package newComp = null;
                            List<arquitetura.representation.Package> packagesLayerAssignedOnlyToConcern = new ArrayList<arquitetura.representation.Package>(OperatorUtil.searchComponentsAssignedToConcern(selectedConcern, layer.getPackages()));
                            if (packagesLayerAssignedOnlyToConcern.isEmpty()) {
                                String name = getSuffixPrefix(layer);
                                if (suffix) {
                                    newComp = architecture.createPackage("Package" + OPLA.contComp_ + name);
                                } else {
                                    newComp = architecture.createPackage(name + "Package" + OPLA.contComp_);
                                }
                                cont0++;
                                //OperatorUtil.modularizeConcernInComponent(allComponents, newComp, selectedConcern, architecture);
                            } else {
                                if (packagesLayerAssignedOnlyToConcern.size() == 1) {
                                    Assert.assertTrue(packagesLayerAssignedOnlyToConcern.get(0).getName().equals("Package2L1"));
                                    cont1++;
                                    //OperatorUtil.modularizeConcernInComponent(allComponents, packagesLayerAssignedOnlyToConcern.get(0), selectedConcern, architecture);
                                } else {
                                    cont2++;
                                    Assert.assertTrue(packagesLayerAssignedOnlyToConcern.contains(package1l3));
                                    Assert.assertTrue(packagesLayerAssignedOnlyToConcern.contains(package2l3));
                                    //OperatorUtil.modularizeConcernInComponent(allComponents, OperatorUtil.randomObject(packagesLayerAssignedOnlyToConcern), selectedConcern, architecture);
                                }
                            }
                            packagesLayerAssignedOnlyToConcern.clear();
                        }
                        Assert.assertTrue(cont0 == 1);
                        Assert.assertTrue(cont1 == 1);
                        Assert.assertTrue(cont2 == 1);
                    }
                    concernsSelectedComp.clear();
                    allComponents.clear();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDoMutationClientServer() throws Exception {
        ArchitectureBuilder builder = new ArchitectureBuilder();
        Architecture architecture = builder.create("C:/Users/Thainá/Documents/NetBeansProjects/OPLA-ArchStyles/test/models/archtest6/model.uml");
        ClientServerIdentification clientServerIdentification = new ClientServerIdentification(architecture);

        List<Client> clients = new ArrayList<>();

        Client client1 = new Client();
        List<String> sufixos = new ArrayList<>();
        List<String> prefixos = new ArrayList<>();
        sufixos.add("Client1");
        client1.setSufixos(sufixos);
        client1.setPrefixos(prefixos);
        clients.add(client1);

        Client client2 = new Client();
        List<String> sufixos2 = new ArrayList<>();
        List<String> prefixos2 = new ArrayList<>();
        sufixos2.add("Client2");
        client2.setSufixos(sufixos2);
        client2.setPrefixos(prefixos2);
        clients.add(client2);

        List<Server> servers = new ArrayList<>();

        Server server1 = new Server();
        List<String> sufixos4 = new ArrayList<>();
        List<String> prefixos4 = new ArrayList<>();
        sufixos4.add("Server1");
        server1.setSufixos(sufixos4);
        server1.setPrefixos(prefixos4);
        servers.add(server1);

        Server server2 = new Server();
        List<String> sufixos5 = new ArrayList<>();
        List<String> prefixos5 = new ArrayList<>();
        sufixos5.add("Server2");
        server2.setSufixos(sufixos5);
        server2.setPrefixos(prefixos5);
        servers.add(server2);

        //cria uma lista só
        List<Style> clientsservers = new ArrayList<>();
        clientsservers.addAll(clients);
        clientsservers.addAll(servers);

        //TESTE 1
        if (clientServerIdentification.isCorrect(clientsservers)) {
            clientsservers.clear();
            clientsservers.addAll(ClientServerIdentification.getLISTCLIENTS());
            clientsservers.addAll(ClientServerIdentification.getLISTSERVERS());

            Concern concernPlay = new Concern("play");

            //TEST CONCERN PLAY
            Concern selectedConcern = concernPlay;

            //MODULARIZAÇÃO NOS SERVIDORES
            testMutationServer(architecture, selectedConcern, clientsservers);

            //MODULARIZAÇÃO NOS CLIENTES
            testMutationClient(architecture, selectedConcern, clientsservers);

            Assert.assertTrue(modularizationPackages.size() == 3);
            Assert.assertTrue(modularizationPackages.get(0).getName().equals("Pacote1Client1") || modularizationPackages.get(1).getName().equals("Pacote1Client1") || modularizationPackages.get(2).getName().equals("Pacote1Client1"));

            modularizationPackages.clear();
        }
        
        //TESTE 2
        builder = new ArchitectureBuilder();
        architecture = builder.create("C:/Users/Thainá/Documents/NetBeansProjects/OPLA-ArchStyles/test/models/archtest6/model.uml");
        clientServerIdentification = new ClientServerIdentification(architecture);

        //cria uma lista só
        clientsservers = new ArrayList<>();
        clientsservers.addAll(clients);
        clientsservers.addAll(servers);

        if (clientServerIdentification.isCorrect(clientsservers)) {
            clientsservers.clear();
            clientsservers.addAll(ClientServerIdentification.getLISTCLIENTS());
            clientsservers.addAll(ClientServerIdentification.getLISTSERVERS());

            //TEST CONCERN PONG
            Concern concernPong = new Concern("pong");
            Concern selectedConcern = concernPong;

            //MODULARIZAÇÃO NOS SERVIDORES
            testMutationServer(architecture, selectedConcern, clientsservers);

            //MODULARIZAÇÃO NOS CLIENTES
            testMutationClient(architecture, selectedConcern, clientsservers);

            Assert.assertTrue(modularizationPackages.size() == 3);
            Assert.assertTrue(modularizationPackages.get(0).getName().equals("Pacote1Server2") || modularizationPackages.get(0).getName().equals("Pacote2Client1") || modularizationPackages.get(0).getName().equals("Pacote1Client2"));
            Assert.assertTrue(modularizationPackages.get(1).getName().equals("Pacote1Server2") || modularizationPackages.get(1).getName().equals("Pacote2Client1") || modularizationPackages.get(1).getName().equals("Pacote1Client2"));
            Assert.assertTrue(modularizationPackages.get(2).getName().equals("Pacote1Server2") || modularizationPackages.get(2).getName().equals("Pacote2Client1") || modularizationPackages.get(2).getName().equals("Pacote1Client2"));
        }
    }

    @Test
    public void testGetSuffixPrefix() {
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

    public void testMutationServer(Architecture architecture, Concern selectedConcern, List<Style> list) {
        List<arquitetura.representation.Package> serversPackages = new ArrayList<>();
        for (Style style : ClientServerIdentification.getLISTSERVERS()) {
            serversPackages.addAll(style.getPackages());
        }

        Assert.assertTrue(serversPackages.size() == 3);
        Assert.assertTrue(serversPackages.get(0).getName().equalsIgnoreCase("Pacote1Server1") || serversPackages.get(0).getName().equalsIgnoreCase("Pacote1Server2") || serversPackages.get(0).getName().equalsIgnoreCase("Pacote2Server2"));
        Assert.assertTrue(serversPackages.get(1).getName().equalsIgnoreCase("Pacote1Server1") || serversPackages.get(1).getName().equalsIgnoreCase("Pacote1Server2") || serversPackages.get(1).getName().equalsIgnoreCase("Pacote2Server2"));
        Assert.assertTrue(serversPackages.get(2).getName().equalsIgnoreCase("Pacote1Server1") || serversPackages.get(2).getName().equalsIgnoreCase("Pacote1Server2") || serversPackages.get(2).getName().equalsIgnoreCase("Pacote2Server2"));

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
            OperatorUtil.modularizeConcernInComponent(list, serverSelect, newComp, selectedConcern, architecture);
        } else {
            if (packagesLayerAssignedOnlyToConcern.size() == 1) {
                modularizationPackages.add(packagesLayerAssignedOnlyToConcern.get(0));
                serverSelect = (Server) StyleUtil.returnClientServer(packagesLayerAssignedOnlyToConcern.get(0), list);
                OperatorUtil.modularizeConcernInComponent(list, serverSelect, packagesLayerAssignedOnlyToConcern.get(0), selectedConcern, architecture);
            } else {
                arquitetura.representation.Package pac = OperatorUtil.randomObject(packagesLayerAssignedOnlyToConcern);
                modularizationPackages.add(pac);
                serverSelect = (Server) StyleUtil.returnClientServer(pac, list);
                OperatorUtil.modularizeConcernInComponent(list, serverSelect, pac, selectedConcern, architecture);
            }
        }
        packagesLayerAssignedOnlyToConcern.clear();

        //ocultado para testes, mas está funcionando no código original
        if (newComp != null) {
            if (newComp.getElements().isEmpty()) {
                architecture.removePackage(newComp);
            } else {
                //adiciona o novo pacote na lista de servidores
                //serverSelect.getPackages().add(newComp);
                modularizationPackages.add(newComp);
            }
        }
    }

    public void testMutationClient(Architecture architecture, Concern selectedConcern, List<Style> list) {
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
                OperatorUtil.modularizeConcernInComponent(list, client, newComp, selectedConcern, architecture);
            } else {
                if (packagesLayerAssignedOnlyToConcern.size() == 1) {
                    modularizationPackages.add(packagesLayerAssignedOnlyToConcern.get(0));
                    OperatorUtil.modularizeConcernInComponent(list, client, packagesLayerAssignedOnlyToConcern.get(0), selectedConcern, architecture);
                } else {
                    arquitetura.representation.Package pac = OperatorUtil.randomObject(packagesLayerAssignedOnlyToConcern);
                    modularizationPackages.add(pac);
                    OperatorUtil.modularizeConcernInComponent(list, client, pac, selectedConcern, architecture);
                }
            }
            packagesLayerAssignedOnlyToConcern.clear();

            //ocultado para testes, mas está funcionando nno código original
            if (newComp != null) {
                if (newComp.getElements().isEmpty()) {
                    architecture.removePackage(newComp);
                } else {
                    //adiciona o novo pacote na lista de clients
                    //client.getPackages().add(newComp);
                    modularizationPackages.add(newComp);
                }
            }
        }
    }

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

}
