/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operators;

import arquitetura.builders.ArchitectureBuilder;
import arquitetura.helpers.UtilResources;
import arquitetura.representation.Architecture;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import project.identification.ClientServerIdentification;
import project.identification.LayerIdentification;
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.lf5.util.ResourceUtils;
import org.junit.Assert;
import org.junit.Test;
import project.pojo.Client;
import project.pojo.Layer;
import project.pojo.Server;
import project.pojo.Style;
import project.util.OperatorUtil;
import static project.util.OperatorUtil.randomObject;
import static project.util.OperatorUtil.removeInterfacesInPatternStructureFromArray;
import project.util.ParametersRepository;
import project.util.StyleUtil;

/**
 *
 * @author Thainá
 */
public class MoveOperationTest {

    public MoveOperationTest() {
    }

    @Test
    public void testDoMutationLayer() throws Exception {
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
        Map sp3 = new HashMap();
        List<String> sufixos3 = new ArrayList<>();
        List<String> prefixos3 = new ArrayList<>();
        sufixos.add("L3");
        layer3.setSufixos(sufixos3);
        layer3.setPrefixos(prefixos3);
        camadas.add(layer3);

        if (layerIdentification.isCorrect(camadas)) {
            //EXEMPLO 1
            arquitetura.representation.Package sourceComp = architecture.findPackageByName("Package2L2");
            Layer layer = OperatorUtil.findPackageLayer(camadas, sourceComp);
            Interface sourceInterface = architecture.findInterfaceByName("Interface1L2");
            Set<Layer> layersImplementors = new HashSet<>();
            for (Element element : sourceInterface.getImplementors()) {
                arquitetura.representation.Package pac = architecture.findPackageByName(UtilResources.extractPackageName(element.getNamespace()));
                layersImplementors.add(OperatorUtil.findPackageLayer(camadas, pac));
            }
            Assert.assertTrue(layersImplementors.size() == 2);

            arquitetura.representation.Package targetComp = null;
            List<arquitetura.representation.Package> packages = new ArrayList<>();
            //if - seleciona o targetPackage da mesma camada que a sourceInterface
            //else - seleciona o targetPackage da mesma camada ou da inferior (caso haja) 
            if (layersImplementors.size() >= 2) {
                targetComp = OperatorUtil.randomObject(layer.getPackages());
            } else if (layersImplementors.size() == 1) {
                packages.addAll(layer.getPackages());
                if (layersImplementors.iterator().next().getNumero() != 1) {
                    packages.addAll(StyleUtil.returnPackagesLayerNumber(layersImplementors.iterator().next().getNumero() - 1, camadas));
                }
                targetComp = OperatorUtil.randomObject(packages);
            }

            Assert.assertTrue(layer.getPackages().contains(architecture.findPackageByName("Package1L2")));
            Assert.assertTrue(layer.getPackages().contains(architecture.findPackageByName("Package2L2")));
            Assert.assertFalse(layer.getPackages().contains(architecture.findPackageByName("Package1L1")));
            Assert.assertFalse(layer.getPackages().contains(architecture.findPackageByName("Package2L1")));
            Assert.assertFalse(layer.getPackages().contains(architecture.findPackageByName("Package1L3")));
            Assert.assertFalse(layer.getPackages().contains(architecture.findPackageByName("Package2L3")));
            Assert.assertTrue(layer.getPackages().size() == 2);

            //EXEMPLO 2 
            sourceComp = architecture.findPackageByName("Package2L2");
            layer = OperatorUtil.findPackageLayer(camadas, sourceComp);
            sourceInterface = architecture.findInterfaceByName("Interface2L2");
            layersImplementors = new HashSet<>();
            for (Element element : sourceInterface.getImplementors()) {
                arquitetura.representation.Package pac = architecture.findPackageByName(UtilResources.extractPackageName(element.getNamespace()));
                layersImplementors.add(OperatorUtil.findPackageLayer(camadas, pac));
            }
            Assert.assertTrue(layersImplementors.size() == 1);

            //if - seleciona o targetPackage da mesma camada que a sourceInterface
            //else - seleciona o targetPackage da mesma camada ou da inferior (caso haja) 
            if (layersImplementors.size() >= 2) {
                targetComp = OperatorUtil.randomObject(layer.getPackages());
            } else if (layersImplementors.size() == 1) {
                packages.addAll(layer.getPackages());
                if (layersImplementors.iterator().next().getNumero() != 1) {
                    packages.addAll(StyleUtil.returnPackagesLayerNumber(layersImplementors.iterator().next().getNumero() - 1, camadas));
                }
                targetComp = OperatorUtil.randomObject(packages);
            }

            Assert.assertTrue(packages.contains(architecture.findPackageByName("Package1L2")));
            Assert.assertTrue(packages.contains(architecture.findPackageByName("Package2L2")));
            Assert.assertTrue(packages.contains(architecture.findPackageByName("Package1L1")));
            Assert.assertTrue(packages.contains(architecture.findPackageByName("Package2L1")));
            Assert.assertFalse(packages.contains(architecture.findPackageByName("Package1L3")));
            Assert.assertFalse(packages.contains(architecture.findPackageByName("Package2L3")));
            Assert.assertTrue(packages.size() == 4);

        }
    }

    @Test
    public void testDoMutationClientServer() throws Exception {
        ArchitectureBuilder builder = new ArchitectureBuilder();
        Architecture architecture = builder.create("C:/Users/Thainá/Documents/NetBeansProjects/OPLA-ArchStyles/test/models/archtest5/model.uml");
        ClientServerIdentification clientServerIdentification = new ClientServerIdentification(architecture);

        List<Client> clients = new ArrayList<>();

        Client client1 = new Client();
        List<String> sufixos = new ArrayList<>();
        List<String> prefixos = new ArrayList<>();
        sufixos.add("client1");
        client1.setSufixos(sufixos);
        client1.setPrefixos(prefixos);
        clients.add(client1);

        Client client2 = new Client();
        List<String> sufixos2 = new ArrayList<>();
        List<String> prefixos2 = new ArrayList<>();
        sufixos2.add("client2");
        client2.setSufixos(sufixos2);
        client2.setPrefixos(prefixos2);
        clients.add(client2);

        List<Server> servers = new ArrayList<>();

        Server server1 = new Server();
        List<String> sufixos4 = new ArrayList<>();
        List<String> prefixos4 = new ArrayList<>();
        sufixos4.add("server1");
        server1.setSufixos(sufixos4);
        server1.setPrefixos(prefixos4);
        servers.add(server1);

        Server server2 = new Server();
        List<String> sufixos5 = new ArrayList<>();
        List<String> prefixos5 = new ArrayList<>();
        sufixos5.add("server2");
        server2.setSufixos(sufixos5);
        server2.setPrefixos(prefixos5);
        servers.add(server2);

        //cria uma lista só
        List<Style> clientsservers = new ArrayList<>();
        clientsservers.addAll(clients);
        clientsservers.addAll(servers);

        if (clientServerIdentification.isCorrect(clientsservers)) {
            clientsservers.clear();
            clientsservers.addAll(ClientServerIdentification.getLISTCLIENTS());
            clientsservers.addAll(ClientServerIdentification.getLISTSERVERS());

            //EXEMPLO 1
            arquitetura.representation.Package sourceComp = architecture.findPackageByName("Pacote1Server1");
            Interface sourceInterface = architecture.findInterfaceByName("InterfaceS1-1");

            //seleciona os clientes ou servidores dos implementadores
            Set<Style> clientsServersImplementors = new HashSet<>();
            for (Element element : sourceInterface.getImplementors()) {
                arquitetura.representation.Package pac = architecture.findPackageByName(UtilResources.extractPackageName(element.getNamespace()));
                clientsServersImplementors.add(StyleUtil.returnClientServer(pac, clientsservers));
            }

            Assert.assertTrue(clientsServersImplementors.size() == 1);
            Assert.assertTrue(clientsServersImplementors.iterator().next().getSufixos().get(0).equalsIgnoreCase("client1"));

            //cria lista de possíveis targets
            List<Style> targetClientServer = new ArrayList<>();
            //adiciona todos os servidores
            targetClientServer.addAll(ClientServerIdentification.getLISTSERVERS());

            //se todos os implementadores estiveram em um único cliente, adiciona este cliente a lista
            //se não houver implementadores adiciona todos os clientes a lista (a lista conterá todos os pacotes)
            if ((clientsServersImplementors.size() == 1) && (clientsServersImplementors.iterator().next() instanceof Client)) {
                targetClientServer.add(clientsServersImplementors.iterator().next());
            } else if (clientsServersImplementors.isEmpty()) {
                targetClientServer.addAll(ClientServerIdentification.getLISTCLIENTS());
            }

            Assert.assertTrue(targetClientServer.size() == 3);
            Assert.assertTrue((targetClientServer.get(0).getSufixos().get(0).equalsIgnoreCase("server1")) || (targetClientServer.get(1).getSufixos().get(0).equalsIgnoreCase("server1")) || (targetClientServer.get(2).getSufixos().get(0).equalsIgnoreCase("server1")));
            Assert.assertTrue((targetClientServer.get(0).getSufixos().get(0).equalsIgnoreCase("server2")) || (targetClientServer.get(1).getSufixos().get(0).equalsIgnoreCase("server2")) || (targetClientServer.get(2).getSufixos().get(0).equalsIgnoreCase("server2")));
            Assert.assertTrue((targetClientServer.get(0).getSufixos().get(0).equalsIgnoreCase("client1")) || (targetClientServer.get(1).getSufixos().get(0).equalsIgnoreCase("client1")) || (targetClientServer.get(2).getSufixos().get(0).equalsIgnoreCase("client1")));

            //EXEMPLO 2
            sourceComp = architecture.findPackageByName("Pacote2Client1");
            sourceInterface = architecture.findInterfaceByName("InterfaceC1-1");

            //seleciona os clientes ou servidores dos implementadores
            clientsServersImplementors = new HashSet<>();
            for (Element element : sourceInterface.getImplementors()) {
                arquitetura.representation.Package pac = architecture.findPackageByName(UtilResources.extractPackageName(element.getNamespace()));
                clientsServersImplementors.add(StyleUtil.returnClientServer(pac, clientsservers));
            }

            Assert.assertTrue(clientsServersImplementors.size() == 1);
            Assert.assertTrue(clientsServersImplementors.iterator().next().getSufixos().get(0).equalsIgnoreCase("client1"));

            //cria lista de possíveis targets
            targetClientServer = new ArrayList<>();
            //adiciona todos os servidores
            targetClientServer.addAll(ClientServerIdentification.getLISTSERVERS());

            //se todos os implementadores estiveram em um único cliente, adiciona este cliente a lista
            //se não houver implementadores adiciona todos os clientes a lista (a lista conterá todos os pacotes)
            if ((clientsServersImplementors.size() == 1) && (clientsServersImplementors.iterator().next() instanceof Client)) {
                targetClientServer.add(clientsServersImplementors.iterator().next());
            } else if (clientsServersImplementors.isEmpty()) {
                targetClientServer.addAll(ClientServerIdentification.getLISTCLIENTS());
            }

            Assert.assertTrue(targetClientServer.size() == 3);
            Assert.assertTrue((targetClientServer.get(0).getSufixos().get(0).equalsIgnoreCase("server1")) || (targetClientServer.get(1).getSufixos().get(0).equalsIgnoreCase("server1")) || (targetClientServer.get(2).getSufixos().get(0).equalsIgnoreCase("server1")));
            Assert.assertTrue((targetClientServer.get(0).getSufixos().get(0).equalsIgnoreCase("server2")) || (targetClientServer.get(1).getSufixos().get(0).equalsIgnoreCase("server2")) || (targetClientServer.get(2).getSufixos().get(0).equalsIgnoreCase("server2")));
            Assert.assertTrue((targetClientServer.get(0).getSufixos().get(0).equalsIgnoreCase("client1")) || (targetClientServer.get(1).getSufixos().get(0).equalsIgnoreCase("client1")) || (targetClientServer.get(2).getSufixos().get(0).equalsIgnoreCase("client1")));

            //EXEMPLO 3
            sourceComp = architecture.findPackageByName("Pacote1Client2");
            sourceInterface = architecture.findInterfaceByName("InterfaceC2-1");

            //seleciona os clientes ou servidores dos implementadores
            clientsServersImplementors = new HashSet<>();
            for (Element element : sourceInterface.getImplementors()) {
                arquitetura.representation.Package pac = architecture.findPackageByName(UtilResources.extractPackageName(element.getNamespace()));
                clientsServersImplementors.add(StyleUtil.returnClientServer(pac, clientsservers));
            }

            Assert.assertTrue(clientsServersImplementors.size() == 1);
            Assert.assertTrue(clientsServersImplementors.iterator().next().getSufixos().get(0).equalsIgnoreCase("client2"));

            //cria lista de possíveis targets
            targetClientServer = new ArrayList<>();
            //adiciona todos os servidores
            targetClientServer.addAll(ClientServerIdentification.getLISTSERVERS());

            //se todos os implementadores estiveram em um único cliente, adiciona este cliente a lista
            //se não houver implementadores adiciona todos os clientes a lista (a lista conterá todos os pacotes)
            if ((clientsServersImplementors.size() == 1) && (clientsServersImplementors.iterator().next() instanceof Client)) {
                targetClientServer.add(clientsServersImplementors.iterator().next());
            } else if (clientsServersImplementors.isEmpty()) {
                targetClientServer.addAll(ClientServerIdentification.getLISTCLIENTS());
            }

            Assert.assertTrue(targetClientServer.size() == 3);
            Assert.assertTrue((targetClientServer.get(0).getSufixos().get(0).equalsIgnoreCase("server1")) || (targetClientServer.get(1).getSufixos().get(0).equalsIgnoreCase("server1")) || (targetClientServer.get(2).getSufixos().get(0).equalsIgnoreCase("server1")));
            Assert.assertTrue((targetClientServer.get(0).getSufixos().get(0).equalsIgnoreCase("server2")) || (targetClientServer.get(1).getSufixos().get(0).equalsIgnoreCase("server2")) || (targetClientServer.get(2).getSufixos().get(0).equalsIgnoreCase("server2")));
            Assert.assertTrue((targetClientServer.get(0).getSufixos().get(0).equalsIgnoreCase("client2")) || (targetClientServer.get(1).getSufixos().get(0).equalsIgnoreCase("client2")) || (targetClientServer.get(2).getSufixos().get(0).equalsIgnoreCase("client2")));

            //EXEMPLO 4
            sourceComp = architecture.findPackageByName("Pacote2Server2");
            sourceInterface = architecture.findInterfaceByName("InterfaceS2-2");

            //seleciona os clientes ou servidores dos implementadores
            clientsServersImplementors = new HashSet<>();
            for (Element element : sourceInterface.getImplementors()) {
                arquitetura.representation.Package pac = architecture.findPackageByName(UtilResources.extractPackageName(element.getNamespace()));
                clientsServersImplementors.add(StyleUtil.returnClientServer(pac, clientsservers));
            }

            Assert.assertTrue(clientsServersImplementors.isEmpty());

            //cria lista de possíveis targets
            targetClientServer = new ArrayList<>();
            //adiciona todos os servidores
            targetClientServer.addAll(ClientServerIdentification.getLISTSERVERS());

            //se todos os implementadores estiveram em um único cliente, adiciona este cliente a lista
            //se não houver implementadores adiciona todos os clientes a lista (a lista conterá todos os pacotes)
            if ((clientsServersImplementors.size() == 1) && (clientsServersImplementors.iterator().next() instanceof Client)) {
                targetClientServer.add(clientsServersImplementors.iterator().next());
            } else if (clientsServersImplementors.isEmpty()) {
                targetClientServer.addAll(ClientServerIdentification.getLISTCLIENTS());
            }

            Assert.assertTrue(targetClientServer.size() == 4);
            Assert.assertTrue((targetClientServer.get(0).getSufixos().get(0).equalsIgnoreCase("server1")) || (targetClientServer.get(1).getSufixos().get(0).equalsIgnoreCase("server1")) || (targetClientServer.get(2).getSufixos().get(0).equalsIgnoreCase("server1")) || (targetClientServer.get(3).getSufixos().get(0).equalsIgnoreCase("server1")));
            Assert.assertTrue((targetClientServer.get(0).getSufixos().get(0).equalsIgnoreCase("server2")) || (targetClientServer.get(1).getSufixos().get(0).equalsIgnoreCase("server2")) || (targetClientServer.get(2).getSufixos().get(0).equalsIgnoreCase("server2")) || (targetClientServer.get(3).getSufixos().get(0).equalsIgnoreCase("server2")));
            Assert.assertTrue((targetClientServer.get(0).getSufixos().get(0).equalsIgnoreCase("client1")) || (targetClientServer.get(1).getSufixos().get(0).equalsIgnoreCase("client1")) || (targetClientServer.get(2).getSufixos().get(0).equalsIgnoreCase("client1")) || (targetClientServer.get(2).getSufixos().get(0).equalsIgnoreCase("client1")));

            //EXEMPLO 5
            sourceComp = architecture.findPackageByName("Pacote1Server2");
            sourceInterface = architecture.findInterfaceByName("InterfaceS2-3");

            //seleciona os clientes ou servidores dos implementadores
            clientsServersImplementors = new HashSet<>();
            for (Element element : sourceInterface.getImplementors()) {
                arquitetura.representation.Package pac = architecture.findPackageByName(UtilResources.extractPackageName(element.getNamespace()));
                clientsServersImplementors.add(StyleUtil.returnClientServer(pac, clientsservers));
            }

            Assert.assertTrue(clientsServersImplementors.size() == 2);
            
            //cria lista de possíveis targets
            targetClientServer = new ArrayList<>();
            //adiciona todos os servidores
            targetClientServer.addAll(ClientServerIdentification.getLISTSERVERS());

            //se todos os implementadores estiveram em um único cliente, adiciona este cliente a lista
            //se não houver implementadores adiciona todos os clientes a lista (a lista conterá todos os pacotes)
            if ((clientsServersImplementors.size() == 1) && (clientsServersImplementors.iterator().next() instanceof Client)) {
                targetClientServer.add(clientsServersImplementors.iterator().next());
            } else if (clientsServersImplementors.isEmpty()) {
                targetClientServer.addAll(ClientServerIdentification.getLISTCLIENTS());
            }

            Assert.assertTrue(targetClientServer.size() == 2);
            Assert.assertTrue((targetClientServer.get(0).getSufixos().get(0).equalsIgnoreCase("server1")) || (targetClientServer.get(1).getSufixos().get(0).equalsIgnoreCase("server1")) || (targetClientServer.get(2).getSufixos().get(0).equalsIgnoreCase("server1")));
            Assert.assertTrue((targetClientServer.get(0).getSufixos().get(0).equalsIgnoreCase("server2")) || (targetClientServer.get(1).getSufixos().get(0).equalsIgnoreCase("server2")) || (targetClientServer.get(2).getSufixos().get(0).equalsIgnoreCase("server2")));
        }
    }
}
