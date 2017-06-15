package ru.web.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Locale;

@ManagedBean
@SessionScoped
public class LocaleChanger implements Serializable {

    private Locale currentLocale= FacesContext.getCurrentInstance()
            .getViewRoot().getLocale();

    public LocaleChanger() {
    }

    public Locale getCurrentLocale() {
        return currentLocale;
    }

    public void changeLocale(String locale) {
        currentLocale = new Locale(locale);
    }
}
