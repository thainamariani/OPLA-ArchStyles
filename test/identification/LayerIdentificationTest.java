/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package identification;

import arquitetura.builders.ArchitectureBuilder;
import arquitetura.representation.Architecture;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import pojo.Layer;

/**
 *
 * @author Thainá
 */
public class LayerIdentificationTest {

    public LayerIdentificationTest() {
    }

    @Test
    public void testIdentify() throws Exception {
        //criação da arquitetura
        ArchitectureBuilder builder = new ArchitectureBuilder();
        Architecture architecture = builder.create("agm/Papyrus/agm.uml");

        LayerIdentification layerIdentification = new LayerIdentification(architecture);

        //valores abaixo (camadas, sufixos, prefixos) serão indicados pelo usuário (GUI)
        //int qtdeCamadas = 3;
        List<Layer> camadas = new ArrayList<>();

        Layer layer1 = new Layer();
        layer1.setNumero(1);
        Map sp1 = new HashMap();
        sp1.put("mgr", "suffix");
        layer1.setSp(sp1);
        camadas.add(layer1);

        Layer layer2 = new Layer();
        layer2.setNumero(2);
        Map sp2 = new HashMap();
        sp2.put("ctrl", "suffix");
        layer2.setSp(sp2);
        camadas.add(layer2);

        Layer layer3 = new Layer();
        layer3.setNumero(3);
        Map sp3 = new HashMap();
        sp3.put("gUi", "suffix");
        layer3.setSp(sp3);
        camadas.add(layer3);

        //próximas linhas verificam se o sufixo (ou prefixo) existe na arquitetura
        boolean exist;
        boolean identify = true;

        for (Layer layer : camadas) {
            for (Map.Entry<String, String> entry : layer.getSp().entrySet()) {
                exist = layerIdentification.checkSuffixPrefix(entry.getValue(), entry.getKey());
                //Assert.assertEquals(true, exist);
                if (exist == false) {
                    System.out.println(entry.getValue().equals("suffix") ? "Sufixo " + entry.getKey() + " não existe. Insira novamente" : "Prefixo " + entry.getKey() + " não existe. Insira novamente");
                    identify = false;
                    break;
                }

            }
        }

        if (identify) {
            //se a camada 1 não utiliza ngm: OK
            layerIdentification.checkStyle(camadas);
        }

    }
}
