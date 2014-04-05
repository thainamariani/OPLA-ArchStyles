/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojo;

import java.util.Map;

/**
 *
 * @author Thainá
 */
public class Layer {

    private int numero;
    //armazena o tipo (sufixo ou prefixo) e os respectivos sufixos e prefixos da camada;
    private Map<String, String> sp;

    public Layer() {
    }

    public Layer(int numero, Map<String, String> sp) {
        this.numero = numero;
        this.sp = sp;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public Map<String, String> getSp() {
        return sp;
    }

    public void setSp(Map<String, String> sp) {
        this.sp = sp;
    }

}
