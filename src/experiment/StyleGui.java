/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package experiment;

import arquitetura.builders.ArchitectureBuilder;
import arquitetura.representation.Architecture;
import identification.ClientServerIdentification;
import identification.LayerIdentification;
import java.util.ArrayList;
import java.util.List;
import pojo.Client;
import pojo.Layer;
import pojo.Server;
import pojo.Style;

/**
 *
 * @author Thainá
 */
public class StyleGui {

    public static boolean verifyLayer(String plaPath) throws Exception {
        ArchitectureBuilder builder = new ArchitectureBuilder();
        Architecture architecture = builder.create(plaPath);
        if (!Experiment.getPlaName(plaPath).equalsIgnoreCase("bet")) {
            return verifyLayerAgmMm(architecture);
        } else {
            return verifyLayerBet(architecture);
        }
    }

    public static boolean verifyLayerBet(Architecture architecture) {
        System.out.println("Verificando camadas da BET");
        LayerIdentification layerIdentification = new LayerIdentification(architecture);

        //Criação das camadas - fazer na GUI
        List<Layer> camadas = new ArrayList<>();
        Layer layer1 = new Layer();
        layer1.setNumero(1);
        List<String> sufixos = new ArrayList<>();
        List<String> prefixos = new ArrayList<>();
        sufixos.add("Mgr");
        sufixos.add("Ctrl");
        layer1.setSufixos(sufixos);
        layer1.setPrefixos(prefixos);
        camadas.add(layer1);

        Layer layer2 = new Layer();
        layer2.setNumero(2);
        List<String> sufixos2 = new ArrayList<>();
        List<String> prefixos2 = new ArrayList<>();
        sufixos2.add("Gui");
        layer2.setSufixos(sufixos2);
        layer2.setPrefixos(prefixos2);
        camadas.add(layer2);

        return layerIdentification.isCorrect(camadas);
    }

    public static boolean verifyLayerAgmMm(Architecture architecture) {
        System.out.println("Verificando camadas da AGM ou Mobile Media");
        LayerIdentification layerIdentification = new LayerIdentification(architecture);

        //Criação das camadas - fazer na GUI
        List<Layer> camadas = new ArrayList<>();
        Layer layer1 = new Layer();
        layer1.setNumero(1);
        List<String> sufixos = new ArrayList<>();
        List<String> prefixos = new ArrayList<>();
        sufixos.add("Mgr");
        layer1.setSufixos(sufixos);
        layer1.setPrefixos(prefixos);
        camadas.add(layer1);

        Layer layer2 = new Layer();
        layer2.setNumero(2);
        List<String> sufixos2 = new ArrayList<>();
        List<String> prefixos2 = new ArrayList<>();
        sufixos2.add("Ctrl");
        layer2.setSufixos(sufixos2);
        layer2.setPrefixos(prefixos2);
        camadas.add(layer2);

        Layer layer3 = new Layer();
        layer3.setNumero(3);
        List<String> sufixos3 = new ArrayList<>();
        List<String> prefixos3 = new ArrayList<>();
        sufixos3.add("Gui");
        layer3.setSufixos(sufixos3);
        layer3.setPrefixos(prefixos3);
        camadas.add(layer3);
        return layerIdentification.isCorrect(camadas);
    }

    static boolean verifyClientServer(String plaPath) throws Exception {
        ArchitectureBuilder builder = new ArchitectureBuilder();
        Architecture architecture = builder.create(plaPath);
        ClientServerIdentification clientServerIdentification = new ClientServerIdentification(architecture);

        List<Client> clients = new ArrayList<>();

        Client client1 = new Client();
        List<String> sufixos = new ArrayList<>();
        List<String> prefixos = new ArrayList<>();
        sufixos.add("Client1");
        client1.setSufixos(sufixos);
        client1.setPrefixos(prefixos);
        clients.add(client1);

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

        List<Style> clientsservers = new ArrayList<>();
        clientsservers.addAll(clients);
        clientsservers.addAll(servers);
        return clientServerIdentification.isCorrect(clientsservers);
    }

}
