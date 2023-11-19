package br.edu.ifpr.irati.ads.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

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
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataRegistrada;
    @OneToOne
    private Servidor servidor;
    @OneToOne
    private Vigilante vigilante;

    public Ocorrencia() {
        this.id = 0;
        this.ocorrido = "";
        this.dataRegistrada = new Date();
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

    public Date getDataRegistrada() {
        return dataRegistrada;
    }

    public void setDataRegistrada(Date dataRegistrada) {
        this.dataRegistrada = dataRegistrada;
    }

    public Servidor getServidor() {
        return servidor;
    }

    public void setServidor(Servidor servidor) {
        this.servidor = servidor;
    }

    public void setVigilante(Vigilante vigilante) {
        this.vigilante = vigilante;
    }

    public Vigilante getVigilante() {
        return vigilante;
    }

}
