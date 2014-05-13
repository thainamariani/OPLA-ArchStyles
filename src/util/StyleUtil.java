/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.List;
import pojo.Layer;

/**
 *
 * @author Thainá
 */
public class StyleUtil {

    public static List<arquitetura.representation.Package> returnPackagesLayerNumber(int numLayer, List<Layer> layers) {
        for (Layer layer : layers) {
            if (layer.getNumero() == numLayer) {
                return layer.getPackages();
            }
        }
        return null;
    }

    public static Layer returnLayerNumber(int numLayer, List<Layer> layers) {
        for (Layer layer : layers) {
            if (layer.getNumero() == numLayer) {
                return layer;
            }
        }
        return null;
    }

}
