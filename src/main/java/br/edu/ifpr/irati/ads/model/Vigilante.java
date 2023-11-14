package br.edu.ifpr.irati.ads.model;

import java.io.Serializable;
import jakarta.persistence.Column;
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
    @Column(name = "nome", nullable = false)
    private String nome;
    @Column(name = "cpf", nullable = false)
    private String cpf;

    public Vigilante() {
        this.id = 0;
        this.nome = "";
        this.cpf = "";
    }

    public Vigilante(Integer id, String nome, String cpf) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
    }

    public String getCpf() {
        return cpf;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}
