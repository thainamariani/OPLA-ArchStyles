/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.pojo;

import arquitetura.representation.Package;
import java.util.List;

/**
 *
 * @author Thainá
 */
public class Client extends Style {

    private List<String> sufixos;
    private List<String> prefixos;
    private List<arquitetura.representation.Package> packages;

    public Client() {
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

    public List<Package> getPackages() {
        return packages;
    }

    public void setPackages(List<Package> packages) {
        this.packages = packages;
    }

}
