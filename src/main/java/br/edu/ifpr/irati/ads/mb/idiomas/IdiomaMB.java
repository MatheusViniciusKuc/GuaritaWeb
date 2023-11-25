package br.edu.ifpr.irati.ads.mb.idiomas;

import java.io.Serializable;
import java.util.Locale;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@ViewScoped
public class IdiomaMB implements Serializable {

    private void mudarIdioma(Locale locale) {
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.getViewRoot().setLocale(locale);
    }

    public String ingles() {
        System.out.println("Teste");
        Locale locale = new Locale("en", "US");
        mudarIdioma(locale);
        return null;
    }

    public String portugues() {
        Locale locale = new Locale("pt", "BR");
        mudarIdioma(locale);
        return null;
    }
}
