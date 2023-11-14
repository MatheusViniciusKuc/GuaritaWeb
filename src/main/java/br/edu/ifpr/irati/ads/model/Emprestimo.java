package br.edu.ifpr.irati.ads.model;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class Emprestimo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq-emprestimo")
    @SequenceGenerator(name = "seq-emprestimo",
            sequenceName = "SEQ_EMPRESTIMO", allocationSize = 1, initialValue = 1)
    private Integer id;
    @OneToOne
    private Servidor servidor;
    @OneToOne
    private Espaco espaco;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataInicio;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataFim;
    @OneToOne
    private Ocorrencia ocorrencia;
    @Enumerated(EnumType.STRING)
    private Status status;

    public Emprestimo() {
        this.id = 0;
        this.servidor = new Servidor();
        this.espaco = new Espaco();
        this.dataInicio = new Date();
        this.dataFim = new Date();
        this.ocorrencia = null;
        this.status = Status.AGENDADO;
    }

    public Emprestimo(Integer id, Servidor servidor, Espaco espaco, Date dataInicio, Date dataFim, Ocorrencia ocorrencia, Status status) {
        this.id = id;
        this.servidor = servidor;
        this.espaco = espaco;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.ocorrencia = ocorrencia;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public Ocorrencia getOcorrencia() {
        return ocorrencia;
    }

    public void setOcorrencia(Ocorrencia ocorrencia) {
        this.ocorrencia = ocorrencia;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Servidor getServidor() {
        return servidor;
    }

    public Espaco getEspaco() {
        return espaco;
    }

    public void setServidor(Servidor servidor) {
        this.servidor = servidor;
    }

    public void setEspaco(Espaco espaco) {
        this.espaco = espaco;
    }

}
