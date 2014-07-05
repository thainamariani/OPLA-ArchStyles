/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package identification;

import arquitetura.builders.ArchitectureBuilder;
import arquitetura.representation.Architecture;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.BeforeClass;
import pojo.Client;
import pojo.Layer;
import pojo.Server;
import pojo.Style;

/**
 *
 * @author Thainá
 */
public class ClientServerIdentificationTest {

    private static ClientServerIdentification clientServerIdentification;
    private static Architecture architectureTest;

    public ClientServerIdentificationTest() {
    }

    @BeforeClass
    public static void before() throws Exception {
        ArchitectureBuilder builder = new ArchitectureBuilder();
        architectureTest = builder.create("C:/Users/Thainá/Documents/NetBeansProjects/OPLA-ArchStyles/test/models/archtest4/model.uml");
        clientServerIdentification = new ClientServerIdentification(architectureTest);
    }

    @Test
    public void testRepeatSuffixPrefix() {
        List<Client> clients = new ArrayList<>();

        Client client1 = new Client();
        List<String> sufixos = new ArrayList<>();
        List<String> prefixos = new ArrayList<>();
        sufixos.add("cli1");
        client1.setSufixos(sufixos);
        client1.setPrefixos(prefixos);
        clients.add(client1);

        Client client2 = new Client();
        List<String> sufixos2 = new ArrayList<>();
        List<String> prefixos2 = new ArrayList<>();
        sufixos2.add("cli2");
        client2.setSufixos(sufixos2);
        client2.setPrefixos(prefixos2);
        clients.add(client2);

        Client client3 = new Client();
        List<String> sufixos3 = new ArrayList<>();
        List<String> prefixos3 = new ArrayList<>();
        sufixos3.add("cli3");
        client3.setSufixos(sufixos3);
        client3.setPrefixos(prefixos3);
        clients.add(client3);

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

        Server server3 = new Server();
        List<String> sufixos6 = new ArrayList<>();
        List<String> prefixos6 = new ArrayList<>();
        sufixos6.add("server3");
        server3.setSufixos(sufixos6);
        server3.setPrefixos(prefixos6);
        servers.add(server3);

        //cria uma lista só
        List<Style> clientsservers = new ArrayList<>();
        clientsservers.addAll(clients);
        clientsservers.addAll(servers);

        boolean repeatSuffixPrefix = clientServerIdentification.repeatSuffixPrefix(clientsservers);
        Assert.assertTrue(repeatSuffixPrefix);
    }

    @Test
    public void testRepeatSuffixPrefix2() {
        List<Client> clients = new ArrayList<>();

        Client client1 = new Client();
        List<String> sufixos = new ArrayList<>();
        List<String> prefixos = new ArrayList<>();
        sufixos.add("cli1");
        client1.setSufixos(sufixos);
        client1.setPrefixos(prefixos);
        clients.add(client1);

        Client client2 = new Client();
        List<String> sufixos2 = new ArrayList<>();
        List<String> prefixos2 = new ArrayList<>();
        sufixos2.add("cli1");
        client2.setSufixos(sufixos2);
        client2.setPrefixos(prefixos2);
        clients.add(client2);

        Client client3 = new Client();
        List<String> sufixos3 = new ArrayList<>();
        List<String> prefixos3 = new ArrayList<>();
        sufixos3.add("cli3");
        client3.setSufixos(sufixos3);
        client3.setPrefixos(prefixos3);
        clients.add(client3);

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

        Server server3 = new Server();
        List<String> sufixos6 = new ArrayList<>();
        List<String> prefixos6 = new ArrayList<>();
        sufixos6.add("server2");
        server3.setSufixos(sufixos6);
        server3.setPrefixos(prefixos6);
        servers.add(server3);

        //cria uma lista só
        List<Style> clientsservers = new ArrayList<>();
        clientsservers.addAll(clients);
        clientsservers.addAll(servers);

        boolean repeatSuffixPrefix = clientServerIdentification.repeatSuffixPrefix(clientsservers);
        Assert.assertFalse(repeatSuffixPrefix);
    }

    @Test
    public void testRepeatSuffixPrefix3() {
        List<Client> clients = new ArrayList<>();

        Client client1 = new Client();
        List<String> sufixos = new ArrayList<>();
        List<String> prefixos = new ArrayList<>();
        sufixos.add("cli1");
        client1.setSufixos(sufixos);
        client1.setPrefixos(prefixos);
        clients.add(client1);

        Client client2 = new Client();
        List<String> sufixos2 = new ArrayList<>();
        List<String> prefixos2 = new ArrayList<>();
        prefixos2.add("cli1");
        client2.setSufixos(sufixos2);
        client2.setPrefixos(prefixos2);
        clients.add(client2);

        Client client3 = new Client();
        List<String> sufixos3 = new ArrayList<>();
        List<String> prefixos3 = new ArrayList<>();
        prefixos3.add("cli2");
        client3.setSufixos(sufixos3);
        client3.setPrefixos(prefixos3);
        clients.add(client3);

        List<Server> servers = new ArrayList<>();

        Server server1 = new Server();
        List<String> sufixos4 = new ArrayList<>();
        List<String> prefixos4 = new ArrayList<>();
        prefixos4.add("server1");
        server1.setSufixos(sufixos4);
        server1.setPrefixos(prefixos4);
        servers.add(server1);

        Server server2 = new Server();
        List<String> sufixos5 = new ArrayList<>();
        List<String> prefixos5 = new ArrayList<>();
        prefixos5.add("server1");
        server2.setSufixos(sufixos5);
        server2.setPrefixos(prefixos5);
        servers.add(server2);

        Server server3 = new Server();
        List<String> sufixos6 = new ArrayList<>();
        List<String> prefixos6 = new ArrayList<>();
        sufixos6.add("server2");
        server3.setSufixos(sufixos6);
        server3.setPrefixos(prefixos6);
        servers.add(server3);

        //cria uma lista só
        List<Style> clientsservers = new ArrayList<>();
        clientsservers.addAll(clients);
        clientsservers.addAll(servers);

        boolean repeatSuffixPrefix = clientServerIdentification.repeatSuffixPrefix(clientsservers);
        Assert.assertFalse(repeatSuffixPrefix);
    }

    @Test
    public void testCheckSuffixPrefix() {
        List<Client> clients = new ArrayList<>();

        Client client1 = new Client();
        List<String> sufixos = new ArrayList<>();
        List<String> prefixos = new ArrayList<>();
        sufixos.add("cli1");
        client1.setSufixos(sufixos);
        client1.setPrefixos(prefixos);
        clients.add(client1);

        Client client2 = new Client();
        List<String> sufixos2 = new ArrayList<>();
        List<String> prefixos2 = new ArrayList<>();
        prefixos2.add("cli1");
        client2.setSufixos(sufixos2);
        client2.setPrefixos(prefixos2);
        clients.add(client2);

        Client client3 = new Client();
        List<String> sufixos3 = new ArrayList<>();
        List<String> prefixos3 = new ArrayList<>();
        prefixos3.add("cli2");
        client3.setSufixos(sufixos3);
        client3.setPrefixos(prefixos3);
        clients.add(client3);

        List<Server> servers = new ArrayList<>();

        Server server1 = new Server();
        List<String> sufixos4 = new ArrayList<>();
        List<String> prefixos4 = new ArrayList<>();
        prefixos4.add("server1");
        server1.setSufixos(sufixos4);
        server1.setPrefixos(prefixos4);
        servers.add(server1);

        Server server2 = new Server();
        List<String> sufixos5 = new ArrayList<>();
        List<String> prefixos5 = new ArrayList<>();
        prefixos5.add("server1");
        server2.setSufixos(sufixos5);
        server2.setPrefixos(prefixos5);
        servers.add(server2);

        Server server3 = new Server();
        List<String> sufixos6 = new ArrayList<>();
        List<String> prefixos6 = new ArrayList<>();
        sufixos6.add("server2");
        server3.setSufixos(sufixos6);
        server3.setPrefixos(prefixos6);
        servers.add(server3);

        //cria uma lista só
        List<Style> clientsservers = new ArrayList<>();
        clientsservers.addAll(clients);
        clientsservers.addAll(servers);

        boolean checkSuffixPrefix = clientServerIdentification.checkSuffixPrefix(clientsservers);
        Assert.assertFalse(checkSuffixPrefix);
    }

    @Test
    public void testCheckSuffixPrefix2() {
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

        boolean checkSuffixPrefix = clientServerIdentification.checkSuffixPrefix(clientsservers);
        Assert.assertTrue(checkSuffixPrefix);
    }

    @Test
    public void testIdentify() {
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

        boolean isCorrect = clientServerIdentification.isCorrect(clientsservers);
        Assert.assertFalse(isCorrect);
    }

}
