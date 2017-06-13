package ru.web.controllers;

import ru.web.beans.Employees;
import ru.web.db.Database;
import ru.web.enums.SearchType;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

@ManagedBean
@SessionScoped
public class SearchController implements Serializable{
    private SearchType searchType;
    private String searchString;
    private static Map<String,SearchType> searchList=
            new HashMap<>();
    private ArrayList<Employees> currentEmploList;

    public SearchController() {
        fillEmploAll();
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


    public void setSearchType(SearchType searchType) {
        this.searchType = searchType;
    }

    public Map<String, SearchType> getSearchList() {
        return searchList;
    }

    public void fillEmploBySQL(String sql){
        Statement statement = null;
        ResultSet resultSet = null;
        Connection connection = null;

        try {
            connection = Database.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            currentEmploList=new ArrayList<>();

            while (resultSet.next()) {
                Employees employees=new Employees();
                employees.setId(resultSet.getInt("id"));
                employees.setPin(resultSet.getString("pin"));
                employees.setFio(resultSet.getString("fio"));
                employees.setJob(resultSet.getString("job"));
                employees.setDescr(resultSet.getString("descr"));
                currentEmploList.add(employees);
            }

        } catch (SQLException ex) {
            Logger.getLogger(JobController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(JobController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void fillEmploAll(){

        fillEmploBySQL("select p.id, p.pin, p.image, " +
                "p.descr, f.name as fio, j.name as job " +
                "from mexican.per p " +
                "inner join mexican.fio f on p.fio_id=f.id "+
                "inner join mexican.job j on p.job_id=j.id "+
                "order by p.pin");
    }

    public void fillEmploByJob(){
        Map<String,String> params=FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap();
        Integer job_id=Integer.valueOf(params.get("job_id"));

        fillEmploBySQL("select p.id, p.pin, p.image, " +
                "p.descr, f.name as fio, j.name as job " +
                "from mexican.per p " +
                "inner join mexican.fio f on p.fio_id=f.id "+
                "inner join mexican.job j on p.job_id=j.id "+
                "where job_id="+job_id+" order by p.pin");
    }
    public void fillEmploBySearch(){
        if(searchString.trim().length()==0){
            fillEmploAll();
            return;
        }

        StringBuilder sql=new StringBuilder("select p.id, p.pin, p.image, " +
                "p.descr, f.name as fio, j.name as job " +
                "from mexican.per p " +
                "inner join mexican.fio f on p.fio_id=f.id "+
                "inner join mexican.job j on p.job_id=j.id ");
        if(searchType==SearchType.FIRSTNAME){
            sql.append("where lower(p.pin) like '%"+searchString.toLowerCase()+
            "%' order by p.pin");
        }else if(searchType==SearchType.SECONDNAME){
            sql.append("where lower(f.name) like '%"+searchString.toLowerCase()+
            "%' order by p.pin");
        }
        fillEmploBySQL(sql.toString());
    }

    public ArrayList<Employees> getCurrentEmploList() {
        return currentEmploList;
    }

    public byte[] getImage(int id){
        Statement statement = null;
        ResultSet resultSet = null;
        Connection connection = null;
        byte[] image=null;

        try {
            connection = Database.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery
                    ("select image from mexican.per WHERE id="+id);


            while (resultSet.next()) {
                image=resultSet.getBytes("image");
            }

        } catch (SQLException ex) {
            Logger.getLogger(JobController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(JobController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return image;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }
}

