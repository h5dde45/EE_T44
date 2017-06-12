package ru.web.controllers;

import ru.web.enums.SearchType;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

@ManagedBean
@SessionScoped
public class SearchController implements Serializable{
    private SearchType searchType;
    private static Map<String,SearchType> searchList=
            new HashMap<>();

    public SearchController() {
        ResourceBundle bundle = ResourceBundle.getBundle(
                "ru.web.loc.messages", FacesContext.
                        getCurrentInstance()
                        .getViewRoot().getLocale());
        searchList.put(bundle.getString("firstname"),SearchType.FIRSTNAME);
        searchList.put(bundle.getString("secondname"),SearchType.SECONDNAME);
    }

    public SearchType getSearchType() {
        return searchType;
    }

    public Map<String, SearchType> getSearchList() {
        return searchList;
    }

}

