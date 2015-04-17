/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import arquitetura.builders.ArchitectureBuilder;
import arquitetura.representation.Architecture;
import project.identification.ClientServerIdentification;
import java.util.ArrayList;
import java.util.List;
import project.operators.FeatureDriven;
import project.pojo.Client;
import project.pojo.Server;
import project.pojo.Style;
import project.util.ArchitectureRepository;

/**
 *
 * @author Thainá
 */
//this class generate archictures that should be analysed manually
//the architectures are saved in: path/experiment/operatorName
public class MainTestClientServer {

    public static void main(String[] args) throws Exception {
        ArchitectureBuilder builder = new ArchitectureBuilder();
        Architecture architecture = builder.create("test/models/archtest7/model.uml");
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

        //change the operator you want to test
        if (clientServerIdentification.isCorrect(clientsservers)) {
            clientsservers.clear();
            clientsservers.addAll(ClientServerIdentification.getLISTCLIENTS());
            clientsservers.addAll(ClientServerIdentification.getLISTSERVERS());

            //MoveMethod moveMethod = new MoveMethod();
            //moveMethod.doMutation(1, architecture, "clientserver", clientsservers);
            //MoveAttribute moveAttribute = new MoveAttribute();
            //moveAttribute.doMutation(1, architecture, "clientserver", clientsservers);
            //AddClass addClass = new AddClass();
            //addClass.doMutation(1, architecture, "clientserver", clientsservers);
            //MoveOperation moveOperation = new MoveOperation();
            //moveOperation.doMutation(1, architecture, "clientserver", clientsservers);
            //AddPackage addPackage = new AddPackage();
            //addPackage.doMutation(1, architecture, "clientserver", clientsservers);
            FeatureDriven featureDriven = new FeatureDriven();
            featureDriven.doMutation(1, architecture, "clientserver", clientsservers);

            //save the architecture
            ArchitectureRepository.setCurrentArchitecture(architecture);
            ArchitectureRepository.saveArchitecture("featuredriven", "archtest7");
        }
    }
}
