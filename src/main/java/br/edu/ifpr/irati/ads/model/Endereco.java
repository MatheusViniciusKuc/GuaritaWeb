package br.edu.ifpr.irati.ads.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class Endereco implements Serializable{

    private String cidade;
    private String uf;
    private String bairro;
    private String rua;
    private String numero;
    private String cep;

    public Endereco() {
        this.cidade = "";
        this.uf = "";
        this.bairro = "";
        this.rua = "";
        this.numero = "";
        this.cep = "";
    }

    public Endereco(String cidade, String uf, String bairro, String rua, String numero, String cep) {
        this.cidade = cidade;
        this.uf = uf;
        this.bairro = bairro;
        this.rua = rua;
        this.numero = numero;
        this.cep = cep;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    @Override
    public String toString() {
        String end = "";
        
        if (cidade != null) {
            end += cidade;
        }
        if (uf != null) {
            end += "/ " + uf.toUpperCase();
        }
        if (bairro != null) {
            end += " - " + bairro;
        }
        if (rua != null) {
            end += ", " + rua;
        }
        if (numero != null) {
            end += " - " + numero;
        }
        if (cep != null) {
            end += ", " + cep;
        }
        
        return end;
    }

}
