package br.edu.ifpr.irati.ads.model;

import jakarta.persistence.CascadeType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;

@Entity
public class Espaco implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq-espaco")
    @SequenceGenerator(name = "seq-espaco",
            sequenceName = "SEQ_ESPACO", allocationSize = 1, initialValue = 1)
    private Integer id;
    @Column(name = "nome", nullable = false)
    private String nome;
    @OneToMany(mappedBy = "espaco", cascade = CascadeType.ALL)
    private List<Ocorrencia> ocorrencias;
    private Boolean disponivelEmprestimo;
    private Boolean excluido;

    public Espaco() {
        this.id = 0;
        this.nome = "";
        this.ocorrencias = new ArrayList<>();
        this.disponivelEmprestimo = true;
        this.excluido = false;
    }

    public Espaco(Integer id, String nome, List<Ocorrencia> ocorrencias) {
        this.id = id;
        this.nome = nome;
        this.ocorrencias = ocorrencias;
        this.disponivelEmprestimo = true;
        this.excluido = false;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setOcorrencias(List<Ocorrencia> ocorrencias) {
        this.ocorrencias = ocorrencias;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome != null ? nome : "";
    }

    public List<Ocorrencia> getOcorrencias() {
        return ocorrencias;
    }

    public void setDisponivelEmprestimo(Boolean disponivelEmprestimo) {
        this.disponivelEmprestimo = disponivelEmprestimo;
    }

    public Boolean getDisponivelEmprestimo() {
        return disponivelEmprestimo;
    }

    public void excluir() {
        this.excluido = true;
    }

    public Boolean getExcluido() {
        return excluido;
    }

    @Override
    public String toString() {
        return nome;
    }

    @Override
    public boolean equals(Object obj) {
        Espaco l = (Espaco) obj;
        return this.id == l.getId();
    }

}
