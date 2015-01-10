/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import arquitetura.exceptions.ConcernNotFoundException;
import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.Method;
import arquitetura.representation.RelationshipsHolder;
import arquitetura.representation.relationship.AssociationEnd;
import arquitetura.representation.relationship.AssociationRelationship;
import arquitetura.representation.relationship.Relationship;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import pojo.Layer;
import pojo.Style;

/**
 *
 * @author Thainá
 */
public class StyleUtil {

    public static List<arquitetura.representation.Package> returnPackagesLayerNumber(int numLayer, List<? extends Style> layers) {
        for (Style layer : layers) {
            if (((Layer) layer).getNumero() == numLayer) {
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

    public static Style returnClientServer(arquitetura.representation.Package pacote, List<Style> clientsservers) {
        for (Style clientserver : clientsservers) {
            for (arquitetura.representation.Package pac : clientserver.getPackages()) {
                if (pac.equals(pacote)) {
                    return clientserver;
                }
            }
        }
        return null;
    }

}
