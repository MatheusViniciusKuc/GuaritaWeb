package br.edu.ifpr.irati.ads.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import java.io.Serializable;

@Entity
public class Ocorrencia implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq-ocorrencia")
    @SequenceGenerator(name = "seq-ocorrencia",
            sequenceName = "SEQ_OCORRENCIA", allocationSize = 1, initialValue = 1)
    private Integer id;
    @Column(name = "mensagem", nullable = false)
    private String ocorrido;
    @ManyToOne
    @JoinColumn(name = "espaco_id")
    private Espaco espaco;

    public Ocorrencia() {
        this.id = 0;
        this.ocorrido = "";
    }

    public Ocorrencia(Integer id, String ocorrido, Espaco espaco) {
        this.id = id;
        this.ocorrido = ocorrido;
        this.espaco = espaco;
    }

    public Integer getId() {
        return id;
    }

    public String getOcorrido() {
        return ocorrido;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setOcorrido(String ocorrido) {
        this.ocorrido = ocorrido;
    }

    public void setEspaco(Espaco espaco) {
        this.espaco = espaco;
    }

    public Espaco getEspaco() {
        return espaco;
    }

}
