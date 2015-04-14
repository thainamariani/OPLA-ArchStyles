/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.pojo;

import java.util.List;

/**
 *
 * @author Thainá
 */
public abstract class Style {

    private List<String> sufixos;
    private List<String> prefixos;
    private List<arquitetura.representation.Package> packages;

    public Style() {
    }

    public List<String> getSufixos() {
        return sufixos;
    }

    public void setSufixos(List<String> sufixos) {
        this.sufixos = sufixos;
    }

    public List<String> getPrefixos() {
        return prefixos;
    }

    public void setPrefixos(List<String> prefixos) {
        this.prefixos = prefixos;
    }

    public List<arquitetura.representation.Package> getPackages() {
        return packages;
    }

    public void setPackages(List<arquitetura.representation.Package> packages) {
        this.packages = packages;
    }

}
