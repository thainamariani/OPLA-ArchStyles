/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operators;

import arquitetura.builders.ArchitectureBuilder;
import arquitetura.io.ReaderConfig;
import arquitetura.representation.Architecture;
import aspect.AspectManipulation;
import identification.ClientServerIdentification;
import java.util.ArrayList;
import java.util.List;
import mutation.PLAFeatureMutationConstraints;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import pojo.Client;
import pojo.Server;
import pojo.Style;
import util.OperatorUtil;
import util.StyleUtil;

/**
 *
 * @author Thainá
 */
public class MoveMethodTest {

    public MoveMethodTest() {
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
            Assert.assertTrue(clientsservers.size() == 4);
            //teste server
            arquitetura.representation.Package sourceComp = architecture.findPackageByName("Pacote1Server1");
            arquitetura.representation.Class sourceClass = architecture.findClassByName("ClassS1-1").get(0);
            Style csSourcePackage = StyleUtil.returnClientServer(sourceComp, clientsservers);
            arquitetura.representation.Package targetPackage = null;
            //restricao
            if (csSourcePackage instanceof Client) {
                targetPackage = OperatorUtil.randomObject(csSourcePackage.getPackages());
            } else {
                Server targetServer = OperatorUtil.randomObject(ClientServerIdentification.getLISTSERVERS());
                targetPackage = OperatorUtil.randomObject(targetServer.getPackages());
            }
            Assert.assertTrue((targetPackage.getName().equalsIgnoreCase("Pacote1Server1")) || (targetPackage.getName().equalsIgnoreCase("Pacote1Server2")) || (targetPackage.getName().equalsIgnoreCase("Pacote2Server2")));

            //teste client
            sourceComp = architecture.findPackageByName("Pacote2Client1");
            sourceClass = architecture.findClassByName("ClassC1-2").get(0);
            csSourcePackage = StyleUtil.returnClientServer(sourceComp, clientsservers);
            targetPackage = null;
            //restricao
            if (csSourcePackage instanceof Client) {
                Assert.assertTrue(csSourcePackage.getPackages().size() == 2);
                targetPackage = OperatorUtil.randomObject(csSourcePackage.getPackages());
            } else {
                Server targetServer = OperatorUtil.randomObject(ClientServerIdentification.getLISTSERVERS());
                targetPackage = OperatorUtil.randomObject(targetServer.getPackages());
            }

            Assert.assertTrue((targetPackage.getName().equalsIgnoreCase("Pacote1Client1")) || (targetPackage.getName().equalsIgnoreCase("Pacote2Client1")));
        }
    }

    @Test
    public void testDoMutationAspect() throws Exception {
        ReaderConfig.setPathToProfileSMarty("/home/thaina/NetBeansProjects/OPLA-ArchStyles/test/models/aspect/smarty.profile.uml");
        ReaderConfig.setPathToProfileConcerns("/home/thaina/NetBeansProjects/OPLA-ArchStyles/test/models/aspect/concerns.profile.uml");
        ReaderConfig.setPathProfileRelationship("/home/thaina/NetBeansProjects/OPLA-ArchStyles/test/models/aspect/relationships.profile.uml");
        ReaderConfig.setPathToProfileAspect("/home/thaina/NetBeansProjects/OPLA-ArchStyles/test/models/aspect/aspect.profile.uml");
        ArchitectureBuilder builder = new ArchitectureBuilder();
        Architecture architecture = builder.create("/home/thaina/NetBeansProjects/OPLA-ArchStyles/test/models/aspect/model.uml");

        arquitetura.representation.Package sourceComp = architecture.findPackageByName("Package1");
        arquitetura.representation.Class class1 = architecture.findClassByName("Class1").get(0);
        arquitetura.representation.Class class2 = architecture.findClassByName("Class2").get(0);
        arquitetura.representation.Class class3 = architecture.findClassByName("Class3").get(0);
        arquitetura.representation.Class class4 = architecture.findClassByName("Class4").get(0);
        arquitetura.representation.Class class5 = architecture.findClassByName("Class5").get(0);
        arquitetura.representation.Class class6 = architecture.findClassByName("Class6").get(0);
        arquitetura.representation.Class aspect = architecture.findClassByName("Aspect").get(0);

        List<arquitetura.representation.Class> ClassesComp = AspectManipulation.returnClassesWithoutAspect(sourceComp);
        Assert.assertTrue(ClassesComp.size() == 6);
        Assert.assertTrue(ClassesComp.contains(class1));
        Assert.assertTrue(ClassesComp.contains(class2));
        Assert.assertTrue(ClassesComp.contains(class3));
        Assert.assertTrue(ClassesComp.contains(class4));
        Assert.assertTrue(ClassesComp.contains(class5));
        Assert.assertTrue(ClassesComp.contains(class6));
        Assert.assertFalse(ClassesComp.contains(aspect));

        if (ClassesComp.size() > 0) {
            final arquitetura.representation.Class sourceClass = OperatorUtil.randomObject(ClassesComp);
            final arquitetura.representation.Package targetPackage = OperatorUtil.randomObject(new ArrayList<arquitetura.representation.Package>(architecture.getAllPackages()));
            List<arquitetura.representation.Class> targetClasses = AspectManipulation.returnClassesWithoutAspect(targetPackage);
            Assert.assertTrue(targetClasses.size() == 6);
            Assert.assertTrue(targetClasses.contains(class1));
            Assert.assertTrue(targetClasses.contains(class2));
            Assert.assertTrue(targetClasses.contains(class3));
            Assert.assertTrue(targetClasses.contains(class4));
            Assert.assertTrue(targetClasses.contains(class5));
            Assert.assertTrue(targetClasses.contains(class6));
            Assert.assertFalse(targetClasses.contains(aspect));

            arquitetura.representation.Class targetClass = OperatorUtil.randomObject(targetClasses);
            MoveMethod moveMethod = new MoveMethod();
            //moveMethod.mutation(targetClass, sourceClass, architecture, targetPackage, sourceComp);
        }

    }
}
