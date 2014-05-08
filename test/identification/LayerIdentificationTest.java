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
import org.junit.Assert;
import org.junit.Test;
import org.junit.BeforeClass;
import pojo.Layer;

/**
 *
 * @author Thainá
 */
public class LayerIdentificationTest {

    private static LayerIdentification layerIdentificationAgm;

    public LayerIdentificationTest() {
    }

    @BeforeClass
    public static void before() throws Exception {
        //instância agm
        ArchitectureBuilder builder = new ArchitectureBuilder();
        Architecture architectureAgm = builder.create("C:/Users/Thainá/workspace/AGM/agm.uml");
        layerIdentificationAgm = new LayerIdentification(architectureAgm);
    }

    @Test
    public void testRepeatSuffixPrefix() throws Exception {
        //caso de teste agm
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

        boolean repeatSuffixPrefix = layerIdentificationAgm.repeatSuffixPrefix(camadas);
        Assert.assertTrue(repeatSuffixPrefix);
    }

    @Test
    public void testRepeatSuffixPrefix2() throws Exception {
        //caso de teste agm
        List<Layer> camadas = new ArrayList<>();
        Layer layer1 = new Layer();
        layer1.setNumero(1);
        Map sp1 = new HashMap();
        sp1.put("mgr", "prefix");
        layer1.setSp(sp1);
        camadas.add(layer1);

        Layer layer2 = new Layer();
        layer2.setNumero(2);
        Map sp2 = new HashMap();
        sp2.put("mgr", "prefix");
        layer2.setSp(sp2);
        camadas.add(layer2);

        Layer layer3 = new Layer();
        layer3.setNumero(3);
        Map sp3 = new HashMap();
        sp3.put("gUi", "suffix");
        layer3.setSp(sp3);
        camadas.add(layer3);

        boolean repeatSuffixPrefix = layerIdentificationAgm.repeatSuffixPrefix(camadas);
        Assert.assertFalse(repeatSuffixPrefix);

    }

    @Test
    public void testCheckSuffixPrefix() {
        //caso de teste agm
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

        boolean checkSuffixPrefix = layerIdentificationAgm.checkSuffixPrefix(camadas);
        Assert.assertTrue(checkSuffixPrefix);
    }

    @Test
    public void testCheckSuffixPrefix2() {
        //caso de teste agm
        List<Layer> camadas = new ArrayList<>();
        Layer layer1 = new Layer();
        layer1.setNumero(1);
        Map sp1 = new HashMap();
        sp1.put("mgr", "prefix");
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

        boolean checkSuffixPrefix = layerIdentificationAgm.checkSuffixPrefix(camadas);
        Assert.assertFalse(checkSuffixPrefix);
    }

    @Test
    public void testIdentify() {
        //caso de teste agm
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

        boolean identify = layerIdentificationAgm.identify(camadas);
        Assert.assertTrue(identify);
    }

    @Test
    public void testIdentify2() {
        //caso de teste agm
        List<Layer> camadas = new ArrayList<>();
        Layer layer1 = new Layer();
        layer1.setNumero(1);
        Map sp1 = new HashMap();
        sp1.put("mg", "suffix");
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

        boolean identify = layerIdentificationAgm.identify(camadas);
        Assert.assertFalse(identify);

    }

}
