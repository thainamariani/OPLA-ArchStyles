/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package identification;

import arquitetura.representation.Architecture;
import java.util.List;
import pojo.Layer;
import pojo.Style;

/**
 *
 * @author Thainá
 */
public abstract class StylesIdentification {

    protected Architecture architecture;

    public StylesIdentification(Architecture architecture) {
        this.architecture = architecture;
    }

    public boolean repeatSuffixPrefix(List<Layer> style) {
        return false;
    }

    public boolean checkSuffixPrefix(List<Layer> style) {
        return false;
    }

    public boolean verifySuffix(String suffix) {
        return false;

    }

    public boolean verifyPrefix(String prefix) {
        return false;
    }

    public boolean identify(List<Layer> list) {
        return false;
    }

    public boolean checkStyle(List<Layer> list) {
        return false;
    }

    public Architecture getArchitecture() {
        return architecture;
    }

    public void setArchitecture(Architecture architecture) {
        this.architecture = architecture;
    }

}
