/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package identification;

import arquitetura.builders.ArchitectureBuilder;
import arquitetura.representation.Architecture;
import arquitetura.representation.Element;
import arquitetura.representation.OperationsOverAbstraction;
import arquitetura.representation.relationship.AbstractionRelationship;
import arquitetura.representation.relationship.AssociationClassRelationship;
import arquitetura.representation.relationship.AssociationRelationship;
import arquitetura.representation.relationship.Relationship;
import arquitetura.representation.relationship.UsageRelationship;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import pojo.Client;
import pojo.Layer;
import pojo.Server;
import pojo.Style;
import util.ElementUtil;
import util.RelationshipUtil;
import util.StyleUtil;

/**
 *
 * @author Thainá
 */
public class ClientServerIdentification2Test {

    private static ClientServerIdentification clientServerIdentification;
    private static Architecture architectureTest;
    private static List<Style> clientsservers = new ArrayList<>();

    public ClientServerIdentification2Test() {
    }

    @BeforeClass
    public static void before() throws Exception {
        ArchitectureBuilder builder = new ArchitectureBuilder();
        architectureTest = builder.create("C:/Users/Thainá/Documents/NetBeansProjects/OPLA-ArchStyles/test/models/archtest4/model.uml");
        clientServerIdentification = new ClientServerIdentification(architectureTest);
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
        clientsservers.addAll(clients);
        clientsservers.addAll(servers);

        //popular os clientes e servidores com os pacotes
        clientServerIdentification.isCorrect(clientsservers);
    }

    @Test
    public void testCheckAssociationClassRelationship() {
        boolean associationclass1 = false;
        boolean associationclass2 = true;
        boolean associationclass3 = true;
        boolean associationclass4 = false;

        arquitetura.representation.Class classS22 = architectureTest.findClassByName("ClassS2-2").get(0);
        arquitetura.representation.Package packageClassS22 = ElementUtil.getPackage(classS22, architectureTest);
        Style clientserver = StyleUtil.returnClientServer(packageClassS22, clientsservers);

        for (Relationship relationship : classS22.getRelationships()) {
            if (relationship instanceof AssociationClassRelationship) {
                associationclass1 = clientServerIdentification.checkAssociationClassRelationship((AssociationClassRelationship) relationship, clientserver.getPackages(), clientserver, clientsservers);
            }
        }

        arquitetura.representation.Class classC11 = architectureTest.findClassByName("ClassC1-1").get(0);
        arquitetura.representation.Package packageClassC11 = ElementUtil.getPackage(classC11, architectureTest);
        clientserver = StyleUtil.returnClientServer(packageClassC11, clientsservers);

        for (Relationship relationship : classC11.getRelationships()) {
            if (relationship instanceof AssociationClassRelationship) {
                if (relationship.getName().equalsIgnoreCase("associationclass2")) {
                    associationclass2 = clientServerIdentification.checkAssociationClassRelationship((AssociationClassRelationship) relationship, clientserver.getPackages(), clientserver, clientsservers);
                }
                if (relationship.getName().equalsIgnoreCase("associationclass4")) {
                    associationclass4 = clientServerIdentification.checkAssociationClassRelationship((AssociationClassRelationship) relationship, clientserver.getPackages(), clientserver, clientsservers);
                }
            }
        }

        arquitetura.representation.Class classC21 = architectureTest.findClassByName("ClassC2-1").get(0);
        arquitetura.representation.Package packageClassC21 = ElementUtil.getPackage(classC21, architectureTest);
        clientserver = StyleUtil.returnClientServer(packageClassC21, clientsservers);

        for (Relationship relationship : classC21.getRelationships()) {
            if (relationship instanceof AssociationClassRelationship) {
                associationclass3 = clientServerIdentification.checkAssociationClassRelationship((AssociationClassRelationship) relationship, clientserver.getPackages(), clientserver, clientsservers);
            }
        }

        Assert.assertTrue(associationclass1);
        Assert.assertFalse(associationclass2);
        Assert.assertFalse(associationclass3);
        Assert.assertTrue(associationclass4);
    }

    @Test
    public void testCheckBidirectionalRelationship() {
        boolean biassociation1 = false;
        boolean biassociation2 = false;
        boolean biassociation3 = true;
        boolean biassociation4 = false;
        boolean biassociation5 = true;

        arquitetura.representation.Class classS21 = architectureTest.findClassByName("ClassS2-1").get(0);
        arquitetura.representation.Package packageClassS21 = ElementUtil.getPackage(classS21, architectureTest);
        Style clientserver = StyleUtil.returnClientServer(packageClassS21, clientsservers);

        for (Relationship relationship : classS21.getRelationships()) {
            if (relationship.getName().equalsIgnoreCase("biassociation1")) {
                biassociation1 = clientServerIdentification.checkBidirectionalRelationship((AssociationRelationship) relationship, clientserver.getPackages(), clientserver, clientsservers);
            }
            if (relationship.getName().equalsIgnoreCase("biassociation2")) {
                biassociation2 = clientServerIdentification.checkBidirectionalRelationship((AssociationRelationship) relationship, clientserver.getPackages(), clientserver, clientsservers);
            }
            if (relationship.getName().equalsIgnoreCase("biassociation3")) {
                biassociation3 = clientServerIdentification.checkBidirectionalRelationship((AssociationRelationship) relationship, clientserver.getPackages(), clientserver, clientsservers);
            }
        }

        arquitetura.representation.Class classC12 = architectureTest.findClassByName("ClassC1-2").get(0);
        arquitetura.representation.Package packageClassC12 = ElementUtil.getPackage(classC12, architectureTest);
        clientserver = StyleUtil.returnClientServer(packageClassC12, clientsservers);

        for (Relationship relationship : classC12.getRelationships()) {
            if (relationship.getName().equalsIgnoreCase("biassociation4")) {
                biassociation4 = clientServerIdentification.checkBidirectionalRelationship((AssociationRelationship) relationship, clientserver.getPackages(), clientserver, clientsservers);
            }
            if (relationship.getName().equalsIgnoreCase("biassociation5")) {
                biassociation5 = clientServerIdentification.checkBidirectionalRelationship((AssociationRelationship) relationship, clientserver.getPackages(), clientserver, clientsservers);
            }
        }

        Assert.assertTrue(biassociation1);
        Assert.assertTrue(biassociation2);
        Assert.assertFalse(biassociation3);
        Assert.assertTrue(biassociation4);
        Assert.assertFalse(biassociation5);
    }

    @Test
    public void testCheckUnidirectionalRelationship() {
        //Qualquer relacionamento no servidor sempre vai retornar true, o teste é feito somente no cliente
        boolean associationuni1 = true;
        boolean abstraction1 = false;
        boolean realization1 = false;
        boolean dependency1 = true;
        boolean usage1 = false;

        arquitetura.representation.Class classS12 = architectureTest.findClassByName("ClassS1-2").get(0);
        arquitetura.representation.Package packageClassS12 = ElementUtil.getPackage(classS12, architectureTest);
        Style clientserver = StyleUtil.returnClientServer(packageClassS12, clientsservers);

        for (Relationship relationship : classS12.getRelationships()) {
            if (relationship instanceof AbstractionRelationship) {
                Element used = RelationshipUtil.getUsedElementFromRelationship(relationship);
                Element client = RelationshipUtil.getClientElementFromRelationship(relationship);
                abstraction1 = clientServerIdentification.checkUnidirectionalRelationship(used, client, clientserver, clientsservers, clientserver.getPackages(), packageClassS12, relationship);
            } else if (relationship.getName().equalsIgnoreCase("usage1")) {
                Element used = RelationshipUtil.getUsedElementFromRelationship(relationship);
                Element client = RelationshipUtil.getClientElementFromRelationship(relationship);
                usage1 = clientServerIdentification.checkUnidirectionalRelationship(used, client, clientserver, clientsservers, clientserver.getPackages(), packageClassS12, relationship);
            }
        }

        arquitetura.representation.Class classC11 = architectureTest.findClassByName("ClassC1-1").get(0);
        arquitetura.representation.Package packageClassC11 = ElementUtil.getPackage(classC11, architectureTest);
        clientserver = StyleUtil.returnClientServer(packageClassC11, clientsservers);

        for (Relationship relationship : classC11.getRelationships()) {
            if (relationship.getName().equalsIgnoreCase("associationuni1")) {
                Element used = RelationshipUtil.getUsedElementFromRelationship(relationship);
                Element client = RelationshipUtil.getClientElementFromRelationship(relationship);
                associationuni1 = clientServerIdentification.checkUnidirectionalRelationship(used, client, clientserver, clientsservers, clientserver.getPackages(), packageClassC11, relationship);
            }
        }

        arquitetura.representation.Class classC12 = architectureTest.findClassByName("ClassC1-2").get(0);
        arquitetura.representation.Package packageClassC12 = ElementUtil.getPackage(classC12, architectureTest);
        clientserver = StyleUtil.returnClientServer(packageClassC12, clientsservers);

        for (Relationship relationship : classC12.getRelationships()) {
            if (relationship.getName().equalsIgnoreCase("realization1")) {
                Element used = RelationshipUtil.getUsedElementFromRelationship(relationship);
                Element client = RelationshipUtil.getClientElementFromRelationship(relationship);
                realization1 = clientServerIdentification.checkUnidirectionalRelationship(used, client, clientserver, clientsservers, clientserver.getPackages(), packageClassC12, relationship);
            }
        }

        arquitetura.representation.Class classC21 = architectureTest.findClassByName("ClassC2-1").get(0);
        arquitetura.representation.Package packageClassC21 = ElementUtil.getPackage(classC21, architectureTest);
        clientserver = StyleUtil.returnClientServer(packageClassC21, clientsservers);

        for (Relationship relationship : classC21.getRelationships()) {
            if (relationship.getName().equalsIgnoreCase("dependency1")) {
                Element used = RelationshipUtil.getUsedElementFromRelationship(relationship);
                Element client = RelationshipUtil.getClientElementFromRelationship(relationship);
                dependency1 = clientServerIdentification.checkUnidirectionalRelationship(used, client, clientserver, clientsservers, clientserver.getPackages(), packageClassC21, relationship);
            }
        }

        Assert.assertTrue(abstraction1);
        Assert.assertTrue(usage1);
        Assert.assertFalse(associationuni1);
        Assert.assertTrue(realization1);
        Assert.assertFalse(dependency1);
    }
}
