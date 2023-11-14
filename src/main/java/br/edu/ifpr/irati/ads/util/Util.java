package br.edu.ifpr.irati.ads.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class Util {

    public static void mensagemErro(String mensagem) {
        FacesMessage mensagemFM = new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagem, null);
        FacesContext.getCurrentInstance().addMessage("siape_emp", mensagemFM);
    }
}
