/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package results;

import arquitetura.builders.ArchitectureBuilder;
import arquitetura.exceptions.NodeIdNotFound;
import arquitetura.helpers.XmiHelper;
import arquitetura.io.ReaderConfig;
import arquitetura.io.SaveAndMove;
import arquitetura.representation.Architecture;
import identification.ClientServerIdentification;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import pojo.Client;
import pojo.Server;
import pojo.Style;
import util.OperatorUtil;

/**
 *
 * @author thaina
 */
public class OutputIdentificationClientServer {

    private static org.w3c.dom.Document docUml = null;
    private static DocumentBuilder documentBuilder = null;
    private static int contCorreta = 0;
    private static int contIncorreta = 0;
    private static int contProblema = 0;
    private static int contArquiteturas = 0;

    public static void main(String[] args) throws NodeIdNotFound, SAXException, IOException, TransformerException, ParserConfigurationException {

        File directory = new File("experiment/");
        if (directory.exists()) {
            String[] list = directory.list();
            for (int i = 0; i < list.length; i++) {
                File subdirectory = new File(directory + "/" + list[i]);
                String[] split = list[i].split("_");
                if (split[0].equals("banking")) {
                    String[] configs = subdirectory.list();
                    for (int j = 0; j < configs.length; j++) {
                        File subsubdirectory = new File(subdirectory + "/" + configs[j]);
                        if (subsubdirectory.toString().endsWith("allComponents")) {
                            System.out.println("subsubdirectory: " + subsubdirectory.getName());
                            File output = new File(subsubdirectory + "/output");
                            String[] outputs = output.list();
                            for (int k = 0; k < outputs.length; k++) {
                                if (outputs[k].endsWith("uml") && outputs[k].startsWith("VAR_")) {
                                    contArquiteturas++;
                                    try {
                                        ReaderConfig.setPathToProfileSMarty(output + "/resources/smarty.profile.uml");
                                        ReaderConfig.setPathToProfileConcerns(output + "/resources/concerns.profile.uml");
                                        ReaderConfig.setPathProfileRelationship(output + "/resources/relationships.profile.uml");
                                        ReaderConfig.setPathToProfilePatterns(output + "/resources/patterns.profile.uml");
                                        ReaderConfig.setDirTarget(subsubdirectory + "/manipulation/");
                                        ReaderConfig.setDirExportTarget(subsubdirectory + "/output/");

                                        DocumentBuilderFactory docBuilderFactoryUml = DocumentBuilderFactory.newInstance();
                                        documentBuilder = docBuilderFactoryUml.newDocumentBuilder();
                                        docUml = documentBuilder.parse(output.getAbsolutePath() + "/" + outputs[k]);

                                        List<Style> clientsServers = new ArrayList<>();
                                        if (split[0].equals("BeT")) {
                                            clientsServers.addAll(verifyBet());
                                        } else {
                                            clientsServers.addAll(verifyBanking());
                                        }

                                        tryBuild(output, outputs[k], clientsServers, subsubdirectory);

                                        Document docNotation = documentBuilder.parse(output.getAbsolutePath() + "/" + outputs[k].substring(0, outputs[k].lastIndexOf(".")) + ".notation");
                                        Document docDi = documentBuilder.parse(output.getAbsolutePath() + "/" + outputs[k].substring(0, outputs[k].lastIndexOf(".")) + ".di");
                                        SaveAndMove.saveAndMove(docNotation, docUml, docDi, outputs[k].substring(0, outputs[k].lastIndexOf(".")), outputs[k].substring(0, outputs[k].lastIndexOf(".")));

                                    } catch (ParserConfigurationException ex) {
                                        Logger.getLogger(OutputIdentificationClientServer.class.getName()).log(Level.SEVERE, null, ex);
                                    } catch (SAXException ex) {
                                        Logger.getLogger(OutputIdentificationClientServer.class.getName()).log(Level.SEVERE, null, ex);
                                    } catch (IOException ex) {
                                        Logger.getLogger(OutputIdentificationClientServer.class.getName()).log(Level.SEVERE, null, ex);
                                    }

                                }
                            }

                        }
                    }
                    System.out.println("Número de arquiteturas: " + contArquiteturas);
                }
            }
        } else {
            System.out.println("Diretório não existe");
        }

        System.out.println("Corretas: " + contCorreta);
        System.out.println("Incorretas: " + contIncorreta);
        System.out.println("Com problema: " + contProblema);
    }

    public static void verificaIdXMI(String id, File output, String out) {

        NodeList elementsByTagName = docUml.getElementsByTagName("packagedElement");

        for (int l = 0; l < elementsByTagName.getLength(); l++) {
            try {
                Node node = elementsByTagName.item(l);
                NamedNodeMap attributes = node.getAttributes();
                String idForNode = XmiHelper.getIdForNode(node);

                for (int m = 0; m < attributes.getLength(); m++) {

                    Node item = attributes.item(m);
                    if ((item.getNodeName().equals("supplier") || item.getNodeName().equals("client")) && item.getNodeValue().equals(id)) {
                        Node parentNode = node.getParentNode();
                        parentNode.removeChild(node);
                        l--;

                        NodeList elementsByTagName2 = docUml.getElementsByTagName("packagedElement");
                        for (int o = 0; o < elementsByTagName2.getLength(); o++) {
                            Node node2 = elementsByTagName2.item(o);
                            NamedNodeMap attributes2 = node2.getAttributes();
                            for (int p = 0; p < attributes2.getLength(); p++) {
                                Node item2 = attributes2.item(p);
                                if (item2.getNodeName().equals("clientDependency") && item2.getNodeValue().equals(idForNode)) {
                                    attributes2.removeNamedItem(item2.getNodeName());
                                }
                            }
                        }
                    }
                }
            } catch (NodeIdNotFound ex) {
                ex.printStackTrace();
            }
        }

        NodeList elementsByTagName3 = docUml.getElementsByTagName("ownedEnd");

        for (int l = 0; l < elementsByTagName3.getLength(); l++) {
            Node node = elementsByTagName3.item(l);
            NamedNodeMap attributes = node.getAttributes();

            for (int m = 0; m < attributes.getLength(); m++) {

                Node item = attributes.item(m);
                if (item.getNodeName().equals("type") && item.getNodeValue().equals(id)) {
                    Node parentNode = node.getParentNode();
                    Node parentParentNode = parentNode.getParentNode();
                    parentParentNode.removeChild(parentNode);
                }
            }
        }
    }

    public static void verificaExcecao(Exception ex, File output, String out) throws NodeIdNotFound, SAXException, IOException, TransformerException, ParserConfigurationException {
        String message = ex.getMessage();
        String id = "";
        if (message.contains(".UnresolvedReferenceException")) {
            id = message.substring(message.indexOf("'") + 1, message.lastIndexOf("'"));

            verificaIdXMI(id, output, out);

        } else {
            ex.printStackTrace();
        }
    }

    public static boolean tryBuild(File output, String out, List<Style> clientsServers, File subsubdirectory) throws NodeIdNotFound, SAXException, IOException, TransformerException, ParserConfigurationException {
        //do {
        boolean correct = true;
        try {

            ArchitectureBuilder builder = new ArchitectureBuilder();
            Architecture architecture = builder.create(output.getAbsolutePath() + "/" + out);

            ClientServerIdentification clienteServerIdentification = new ClientServerIdentification(architecture);

            if (clienteServerIdentification.isCorrect(clientsServers)) {
                System.out.println("Experimento " + subsubdirectory + " Solução " + out + " está correta");
                contCorreta++;
                getInvalidsInterfaces(architecture);
                clienteServerIdentification.isCorrectClientServerCommunication(clientsServers);
            } else {
                System.out.println("Experimento " + subsubdirectory + " Solução " + out + " não está correta");
                contIncorreta++;
            }
            //break;
        } catch (Exception ex) {
            contProblema++;
            if (ex.getMessage().contains(".UnresolvedReferenceException")) {
                verificaExcecao(ex, output, out);
                correct = false;
            } else {
                //break;
            }
            //System.out.println("PROBLEMA: " + outputs[k]);
        }
        //} while (true);
        return correct;
    }

    public static void getInvalidsInterfaces(Architecture architecture) {
        boolean validSolution = OperatorUtil.isValidSolution(architecture);
        List<arquitetura.representation.Class> allClasses = new ArrayList<arquitetura.representation.Class>(architecture.getAllClasses());
        for (arquitetura.representation.Class c : allClasses) {
            if (c.getRelationships().isEmpty()) {
                System.out.println("Classe: " +c.getName());
                validSolution = false;
            }
        }
        System.out.println("Valida? " + validSolution);
    }

    public static List<Style> verifyBet() {
        List<Client> clients = new ArrayList<>();

        Client client1 = new Client();
        List<String> sufixos = new ArrayList<>();
        List<String> prefixos = new ArrayList<>();
        sufixos.add("GUI");
        client1.setSufixos(sufixos);
        client1.setPrefixos(prefixos);
        clients.add(client1);

        Client client2 = new Client();
        List<String> sufixos2 = new ArrayList<>();
        List<String> prefixos2 = new ArrayList<>();
        sufixos2.add("ClienteOnibus");
        client2.setSufixos(sufixos2);
        client2.setPrefixos(prefixos2);
        clients.add(client2);

        List<Server> servers = new ArrayList<>();

        Server server1 = new Server();
        List<String> sufixos4 = new ArrayList<>();
        List<String> prefixos4 = new ArrayList<>();
        sufixos4.add("ServidorOnibus");
        server1.setSufixos(sufixos4);
        server1.setPrefixos(prefixos4);
        servers.add(server1);

        Server server2 = new Server();
        List<String> sufixos5 = new ArrayList<>();
        List<String> prefixos5 = new ArrayList<>();
        sufixos5.add("Mgr");
        sufixos5.add("Ctrl");
        server2.setSufixos(sufixos5);
        server2.setPrefixos(prefixos5);
        servers.add(server2);

        List<Style> clientsservers = new ArrayList<>();
        clientsservers.addAll(clients);
        clientsservers.addAll(servers);
        return clientsservers;
    }

    public static List<Style> verifyBanking() {
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
        return clientsservers;
    }
}

//ALGUNS IDS COM ERROS NAS ARQUITETURAS:
//                                        //String id = "_umF5oP14EeOFY93jb2lkbw"; //mobilemedia
//                                        id = "_4L4Z4BViEeODAq4Tm5H6GQ"; //agm
//                                        id = "_82VhYBVjEeODAq4Tm5H6GQ"; //agm
//                                        id = "_McPUsBVjEeODAq4Tm5H6GQ"; //agm
//                                        id = "6e39af81-e580-488e-ae90-a0637b6fa1ed"; //agm
//                                        id = "_X5BBYBVgEeODAq4Tm5H6GQ"; //agm
//                                        id = "a08fb6cf-e927-4be0-9202-c676f5899127"; //agm
//                                        id = "_Lda8ABVjEeODAq4Tm5H6GQ"; //agm
//                                        id = "14f7fed3-8bb6-4c6a-9129-28ddde381e22"; //agm
//                                        id = "_-VLTYBViEeODAq4Tm5H6GQ"; //agm
//                                        id = "8c97e94a-7594-4a26-b237-ab3f54786a3a"; //agm
//                                        id = "d4cfe9f7-d5bd-4eea-9b68-ba98efd6afec"; //agm
//                                        id = "_4L4Z4BViEeODAq4Tm5H6GQ"; //agm
//                                        id = "82ed3e24-e48f-447d-860d-dfa97841b679"; //agm
