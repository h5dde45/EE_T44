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
public class SearchController implements Serializable {
    private SearchType searchType;
    private String searchString;
    private int selectedJobId;
    private static Map<String, SearchType> searchList =
            new HashMap<>();
    private ArrayList<Employees> currentEmploList;

    private int emploOnPage = 2;
    private int totalEmploCount;
    private int selectPageNumber = 1;
    private ArrayList<Integer> pageNambers = new ArrayList<>();
    private String currentSql;


    public SearchController() {
        fillEmploAll();
        ResourceBundle bundle = ResourceBundle.getBundle(
                "ru.web.loc.messages", FacesContext.
                        getCurrentInstance()
                        .getViewRoot().getLocale());
        searchList.put(bundle.getString("firstname"), SearchType.FIRSTNAME);
        searchList.put(bundle.getString("secondname"), SearchType.SECONDNAME);
    }

    public int getSelectedJobId() {
        return selectedJobId;
    }

    public void setSelectedJobId(int selectedJobId) {
        this.selectedJobId = selectedJobId;
    }

    public int getSelectPageNumber() {
        return selectPageNumber;
    }

    public void setSelectPageNumber(int selectPageNumber) {
        this.selectPageNumber = selectPageNumber;
    }

    public String getCurrentSql() {
        return currentSql;
    }


    public Integer getSelectedPageNumber() {
        return selectPageNumber;
    }

    public int getEmploOnPage() {
        return emploOnPage;
    }

    public void setEmploOnPage(int emploOnPage) {
        this.emploOnPage = emploOnPage;
    }

    public int getTotalEmploCount() {
        return totalEmploCount;
    }

    public void setTotalEmploCount(int totalEmploCount) {
        this.totalEmploCount = totalEmploCount;
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

    public void fillEmploBySQL(String sql) {
        Statement statement = null;
        ResultSet resultSet = null;
        Connection connection = null;

        StringBuilder stringBuilder = new StringBuilder(sql);
        currentSql = sql;

        try {
            connection = Database.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            resultSet.last();

            totalEmploCount = resultSet.getRow();
            fillPageNumbers(totalEmploCount, emploOnPage);

            if(totalEmploCount>emploOnPage){
                stringBuilder.append(" limit ");
                stringBuilder.append(selectPageNumber*emploOnPage-emploOnPage);
                stringBuilder.append(", ");
                stringBuilder.append(emploOnPage);
            }
            resultSet = statement.executeQuery(stringBuilder.toString());
            currentEmploList = new ArrayList<>();

            while (resultSet.next()) {
                Employees employees = new Employees();
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

    public void fillPageNumbers(int totalEmploCount, int emploOnPage) {
        int pageCount = totalEmploCount > 0 ? ((totalEmploCount + 1) / emploOnPage) : 0;
        pageNambers.clear();
        for (int i = 1; i <= pageCount; i++) {
            pageNambers.add(i);
        }
    }

    public void fillEmploAll() {

        fillEmploBySQL("select p.id, p.pin, p.image, " +
                "p.descr, f.name as fio, j.name as job " +
                "from mexican.per p " +
                "inner join mexican.fio f on p.fio_id=f.id " +
                "inner join mexican.job j on p.job_id=j.id " +
                "order by p.pin");

    }

    public void fillEmploByJob() {
        Map<String, String> params = FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap();
        selectedJobId = Integer.valueOf(params.get("job_id"));

        fillEmploBySQL("select p.id, p.pin, p.image, " +
                "p.descr, f.name as fio, j.name as job " +
                "from mexican.per p " +
                "inner join mexican.fio f on p.fio_id=f.id " +
                "inner join mexican.job j on p.job_id=j.id " +
                "where job_id=" + selectedJobId + " order by p.pin");

//        selectPageNumber=1;
    }

    public void fillEmploBySearch() {
        if (searchString.trim().length() == 0) {
            fillEmploAll();
            return;
        }

        StringBuilder sql = new StringBuilder("select p.id, p.pin, p.image, " +
                "p.descr, f.name as fio, j.name as job " +
                "from mexican.per p " +
                "inner join mexican.fio f on p.fio_id=f.id " +
                "inner join mexican.job j on p.job_id=j.id ");
        if (searchType == SearchType.FIRSTNAME) {
            sql.append("where lower(p.pin) like '%" + searchString.toLowerCase() +
                    "%' order by p.pin");
        } else if (searchType == SearchType.SECONDNAME) {
            sql.append("where lower(f.name) like '%" + searchString.toLowerCase() +
                    "%' order by p.pin");
        }
        fillEmploBySQL(sql.toString());

//        selectPageNumber=1;
//        selectedJobId=-1;
    }

    public ArrayList<Employees> getCurrentEmploList() {
        return currentEmploList;
    }

    public byte[] getImage(int id) {
        Statement statement = null;
        ResultSet resultSet = null;
        Connection connection = null;
        byte[] image = null;

        try {
            connection = Database.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery
                    ("select image from mexican.per WHERE id=" + id);


            while (resultSet.next()) {
                image = resultSet.getBytes("image");
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

 public byte[] getContent(int id) {
        Statement statement = null;
        ResultSet resultSet = null;
        Connection connection = null;
        byte[] content = null;

        try {
            connection = Database.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery
                    ("select content from mexican.per WHERE id=" + id);


            while (resultSet.next()) {
                content = resultSet.getBytes("content");
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
        return content;
    }

    public void selectPage() {
        Map<String, String> params = FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap();
        selectPageNumber = Integer.valueOf(params.get("page_number"));
        fillEmploBySQL(currentSql);
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public ArrayList<Integer> getPageNambers() {
        return pageNambers;
    }

    public void setPageNamber(ArrayList<Integer> pageNambers) {
        this.pageNambers = pageNambers;
    }
}

