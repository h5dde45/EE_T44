package ru.web.beans;

import ru.web.db.Database;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

@ManagedBean
@ApplicationScoped
public class Jobs implements Serializable {

    private ArrayList<Job> jobList = new ArrayList<>();

    private ArrayList<Job> getJobs() {
        Statement statement = null;
        ResultSet resultSet = null;
        Connection connection = null;

        try {
            connection = Database.getConnection();

            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from mexican.job order by name");
            while (resultSet.next()) {
                Job job = new Job();
                job.setName(resultSet.getString("name"));
                job.setId(resultSet.getInt("id"));
                jobList.add(job);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Jobs.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(Jobs.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return jobList;
    }

    public ArrayList<Job> getJobList() {
        if (!jobList.isEmpty()) {
            return jobList;
        } else {
            return getJobs();
        }
    }
}
