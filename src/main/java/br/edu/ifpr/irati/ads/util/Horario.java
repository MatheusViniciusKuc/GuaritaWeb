package br.edu.ifpr.irati.ads.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Horario implements Serializable{

    private Date dataInicio;
    private Date dataFinal;
    private String horarioSelecionadoIni;
    private String horarioSelecionadoFim;
    private List<String> horariosInicial;
    private List<String> horariosFim;

    public Horario() {
        this.dataInicio = new Date();
        this.dataFinal = new Date();
        setHorarios();
        this.horarioSelecionadoIni = horariosInicial.get(0);
        this.horarioSelecionadoFim = horariosFim.get(0);
    }

    private void setHorarios() {
        this.horariosInicial = new ArrayList<>();
        this.horariosInicial.add("08:00");
        this.horariosInicial.add("09:00");
        this.horariosInicial.add("10:00");
        this.horariosInicial.add("11:00");
        this.horariosInicial.add("12:00");
        this.horariosInicial.add("13:00");
        this.horariosInicial.add("14:00");
        this.horariosInicial.add("15:00");
        this.horariosInicial.add("16:00");
        this.horariosInicial.add("17:00");
        this.horariosInicial.add("18:00");
        this.horariosInicial.add("19:00");
        this.horariosInicial.add("20:00");
        this.horariosInicial.add("21:00");
        this.horariosInicial.add("22:00");
        this.horariosInicial.add("23:00");

        this.horariosFim = horariosInicial;
    }

    public List<String> getHorariosInicial() {
        return horariosInicial;
    }

    public List<String> getHorariosFim() {
        return horariosFim;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setHorarioSelecionadoFim(String horarioSelecionadoFim) {
        this.horarioSelecionadoFim = horarioSelecionadoFim;
    }

    public void setHorarioSelecionadoIni(String horarioSelecionadoIni) {
        this.horarioSelecionadoIni = horarioSelecionadoIni;
    }

    public String getHorarioSelecionadoFim() {
        return horarioSelecionadoFim;
    }

    public String getHorarioSelecionadoIni() {
        return horarioSelecionadoIni;
    }

}
