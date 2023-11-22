package br.edu.ifpr.irati.ads.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

@Embeddable
public class DadosPessoais {

    private String telefone;
    @NotBlank
    private String email;
    @NotBlank
    @Column(name = "cpf", nullable = false)
    private String cpf;
    @NotBlank
    @Column(name = "nome", nullable = false)
    private String nome;
    private Endereco endereco;

    public DadosPessoais() {
        this.telefone = "";
        this.email = "";
        this.cpf = "";
        this.nome = "";
        this.endereco = new Endereco();
    }

    public DadosPessoais(String telefone, String email, String cpf, String nome, Endereco endereco) {
        this.telefone = telefone;
        this.email = email;
        this.cpf = cpf;
        this.nome = nome;
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

}
