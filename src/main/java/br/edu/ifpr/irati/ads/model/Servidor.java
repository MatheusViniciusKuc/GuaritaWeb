package br.edu.ifpr.irati.ads.model;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity
public class Servidor implements Serializable {

    @Id
    @jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.SEQUENCE, generator = "seq-serv")
    @SequenceGenerator(name = "seq-serv",
            sequenceName = "SEQ_SERV", allocationSize = 1, initialValue = 1)
    private Integer id;
    @Column(name = "siape", nullable = false)
    private String siape;
    @Column(name = "nome", nullable = false)
    private String nome;
    @Column(name = "cpf", nullable = false)
    private String cpf;

    public Servidor() {
        this.id = 0;
        this.siape = "";
        this.nome = "";
        this.cpf = "";
    }

    public Servidor(Integer id, String siape, String nome, String cpf) {
        this.id = id;
        this.siape = siape;
        this.nome = nome;
        this.cpf = cpf;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSiape(String siape) {
        this.siape = siape;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getSiape() {
        return siape;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCpf() {
        return cpf;
    }

    @Override
    public String toString() {
        return nome + " - " + siape;
    }
}
