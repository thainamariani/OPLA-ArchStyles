/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojo;

import java.util.List;
import java.util.Map;
import arquitetura.representation.Package;

/**
 *
 * @author Thainá
 */
public class Layer extends Style {

    private int numero;
    //armazena o tipo (sufixo ou prefixo) e os respectivos sufixos e prefixos da camada;
    private List<String> sufixos;
    private List<String> prefixos;
    private List<Package> packages;

    public Layer() {
    }

    public Layer(int numero, List<String> sufixos, List<String> prefixos, List<Package> packages) {
        this.numero = numero;
        this.sufixos = sufixos;
        this.prefixos = prefixos;
        this.packages = packages;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
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
