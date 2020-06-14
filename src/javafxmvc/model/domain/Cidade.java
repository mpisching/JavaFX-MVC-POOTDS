/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxmvc.model.domain;

/**
 *
 * @author mpisching
 */
public class Cidade {
    private int cdCidade;
    private String nome;
    private String uf;

    /**
     * @return the cdCidade
     */
    public int getCdCidade() {
        return cdCidade;
    }

    /**
     * @param cdCidade the cdCidade to set
     */
    public void setCdCidade(int cdCidade) {
        this.cdCidade = cdCidade;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the uf
     */
    public String getUf() {
        return uf;
    }

    /**
     * @param uf the uf to set
     */
    public void setUf(String uf) {
        this.uf = uf;
    }
    
    
}
