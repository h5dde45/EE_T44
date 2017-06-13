package ru.web.controllers;

import ru.web.beans.Job;
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

@ManagedBean(eager = true)
@ApplicationScoped
public class JobController implements Serializable {

    private ArrayList<Job> jobList = new ArrayList<>();

    public JobController() {
        fillJobAll();
    }

    private ArrayList<Job> fillJobAll() {
        Statement statement = null;
        ResultSet resultSet = null;
        Connection connection = null;

        try {
            connection = Database.getConnection();

            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from mexican.job order by name");
            while (resultSet.next()) {
                Job job = new Job();
                job.setId(resultSet.getInt("id"));
                job.setName(resultSet.getString("name"));

                jobList.add(job);
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

        return jobList;
    }

    public ArrayList<Job> getJobList() {

            return jobList;

    }
}
