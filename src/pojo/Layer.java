/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojo;

import arquitetura.representation.Package;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Thainá
 */
public class Layer extends Style {

    private int numero;
    //armazena o tipo (sufixo ou prefixo) e os respectivos sufixos e prefixos da camada;
    private List<String> sufixos = new ArrayList<>();
    private List<String> prefixos = new ArrayList<>();
    private List<Package> packages = new ArrayList<>();

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
//        List<Package> packages = new ArrayList<>();
//        for(int i = 0; i < nomesPacotes.size(); i++){
//            pacote = architecture.findPackageByName(nomePacote);
//            if(pacote != null){
//                packages.add(pacote);
//            } else {
//                nomesPacotes.remove(nomePacote);
//                i--;
//            }
//        }
        return packages;
    }

    public void setPackages(List<Package> packages) {
        this.packages = packages;
    }

}
