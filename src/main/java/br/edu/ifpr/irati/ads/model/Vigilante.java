package br.edu.ifpr.irati.ads.model;

import jakarta.persistence.Embedded;
import java.io.Serializable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity
public class Vigilante implements Serializable {

    @Id
    @jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.SEQUENCE, generator = "seq-vig")
    @SequenceGenerator(name = "seq-vig",
            sequenceName = "SEQ_VIG", allocationSize = 1, initialValue = 1)
    private Integer id;
    @Embedded
    private DadosPessoais dadosPessoais;
    private Boolean excluido;

    public Vigilante() {
        this.id = 0;
        this.dadosPessoais = new DadosPessoais();
        this.excluido = false;
    }

    public Vigilante(Integer id, DadosPessoais dadosPessoais) {
        this.id = id;
        this.dadosPessoais = dadosPessoais;
        this.excluido = false;
    }

    public void excluir() {
        this.excluido = true;
    }

    public void setDadosPessoais(DadosPessoais dadosPessoais) {
        this.dadosPessoais = dadosPessoais;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DadosPessoais getDadosPessoais() {
        return dadosPessoais;
    }

    public Integer getId() {
        return id;
    }

    public void setExcluido(Boolean excluido) {
        this.excluido = excluido;
    }

    public Boolean getExcluido() {
        return excluido;
    }

}
