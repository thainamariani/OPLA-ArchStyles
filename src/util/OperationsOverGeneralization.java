package util;

import arquitetura.helpers.UtilResources;
import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Element;
import arquitetura.representation.Package;
import arquitetura.representation.relationship.GeneralizationRelationship;
import identification.ClientServerIdentification;
import identification.LayerIdentification;
import java.util.ArrayList;
import java.util.List;
import pojo.Layer;
import pojo.Server;
import pojo.Style;

public class OperationsOverGeneralization {

    private Architecture architecture;

    public OperationsOverGeneralization(Architecture architecture) {
        this.architecture = architecture;
    }

    public void moveGeneralizationParent(GeneralizationRelationship gene, Class targetClass) {
        gene.setParent(targetClass);
    }

    public void moveGeneralizationSubClass(GeneralizationRelationship gene, Class class1) {
        gene.setChild(class1);
    }

    /**
     *
     * @param gene
     * @param parent
     * @param child
     */
    public void moveGeneralization(GeneralizationRelationship gene, Class parent, Class child) {
        gene.setParent(parent);
        gene.setChild(child);
    }

    public void addChildToGeneralization(GeneralizationRelationship generalizationRelationship, Element newChild) {
        GeneralizationRelationship g = new GeneralizationRelationship(generalizationRelationship.getParent(), newChild, this.architecture.getRelationshipHolder(), UtilResources.getRandonUUID());
        this.architecture.addRelationship(g);
    }

    /**
     * Cria um relacionamento de generalização e o adiciona na arquitetura<br/><br/>
     *
     * NOTA: usando este método você não precisa chamar explicitamente algo como<br/><br/> {@code architecture.addRelationship(relationship)}.
     *
     * @param parent
     * @param child
     * @return
     */
    public GeneralizationRelationship createGeneralization(Element parent, Element child) {
        GeneralizationRelationship g = new GeneralizationRelationship(parent, child, this.architecture.getRelationshipHolder(), UtilResources.getRandonUUID());
        this.architecture.addRelationship(g);
        return g;
    }

    /**
     * Dada uma generalização {@link GeneralizationRelationship} move a mesma o pacote {@link Package} destino.<br/><br/>
     *
     * Este método irá pegar o pai (parent) e os filhos (childreen) da generalização passada como paramêtros e mover para o pacote destino.
     *
     * @param style
     * @param generalization - Generalização a ser movida
     * @param targetPackage - Pacote destino
     */
    public void moveGeneralizationToPackage(Style style, GeneralizationRelationship generalization, Package targetPackage) {

        String sout = "";
        int contElement = 0;
        Package parentPackage = ElementUtil.getPackage(generalization.getParent(), architecture);
        sout = "parent package: " + parentPackage;

        if (style instanceof Layer) {
            Layer layerParent = OperatorUtil.findPackageLayer(LayerIdentification.getLISTLAYERS(), parentPackage);

            for (Element element : generalization.getAllChildrenForGeneralClass()) {
                Package childPackage = ElementUtil.getPackage(element, architecture);
                sout += ". Child package: " + childPackage;
                if (layerParent.getNumero() == OperatorUtil.findPackageLayer(LayerIdentification.getLISTLAYERS(), childPackage).getNumero()) {
                    contElement++;
                }
            }

        } else {
            List<Style> clientsservers = new ArrayList<>();
            clientsservers.addAll(ClientServerIdentification.getLISTCLIENTS());
            clientsservers.addAll(ClientServerIdentification.getLISTSERVERS());
            Style clientServerParent = StyleUtil.returnClientServer(parentPackage, clientsservers);

            for (Element element : generalization.getAllChildrenForGeneralClass()) {
                Package childPackage = ElementUtil.getPackage(element, architecture);
                sout += ". Child package: " + childPackage;
                Style clientServerChild = StyleUtil.returnClientServer(childPackage, clientsservers);
                //se todos forem servidores, ou todos estiverem no mesmo pacote (abrange clientes no mesmo pacote)
                if (((clientServerParent instanceof Server) && (clientServerChild instanceof Server)) || (clientServerParent.equals(clientServerChild))) {
                    contElement++;
                }
            }
        }

        //movimentacao
        if (contElement == generalization.getAllChildrenForGeneralClass().size()) {
            architecture.moveElementToPackage(generalization.getParent(), targetPackage);
            for (Element element : generalization.getAllChildrenForGeneralClass()) {
                architecture.moveElementToPackage(element, targetPackage);
            }
        } else {
            System.out.println("Movimentação da generalização não realizada: " + sout);
        }
    }
}
