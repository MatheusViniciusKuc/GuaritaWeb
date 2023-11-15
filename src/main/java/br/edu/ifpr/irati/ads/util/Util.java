package br.edu.ifpr.irati.ads.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class Util {

    public static void mensagemErro(String mensagem, String campo) {
        FacesMessage mensagemFM = new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagem, null);
        FacesContext.getCurrentInstance().addMessage(campo, mensagemFM);
    }
}
