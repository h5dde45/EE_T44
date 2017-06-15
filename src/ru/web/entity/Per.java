package ru.web.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Arrays;

@Entity
public class Per {
    private int id;
    private String pin;
    private int jobId;
    private int fioId;
    private byte[] image;
    private String descr;
    private byte[] content;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "pin")
    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    @Basic
    @Column(name = "job_id")
    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    @Basic
    @Column(name = "fio_id")
    public int getFioId() {
        return fioId;
    }

    public void setFioId(int fioId) {
        this.fioId = fioId;
    }

    @Basic
    @Column(name = "image")
    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Basic
    @Column(name = "descr")
    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    @Basic
    @Column(name = "content")
    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Per per = (Per) o;

        if (id != per.id) return false;
        if (jobId != per.jobId) return false;
        if (fioId != per.fioId) return false;
        if (pin != null ? !pin.equals(per.pin) : per.pin != null) return false;
        if (!Arrays.equals(image, per.image)) return false;
        if (descr != null ? !descr.equals(per.descr) : per.descr != null) return false;
        if (!Arrays.equals(content, per.content)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (pin != null ? pin.hashCode() : 0);
        result = 31 * result + jobId;
        result = 31 * result + fioId;
        result = 31 * result + Arrays.hashCode(image);
        result = 31 * result + (descr != null ? descr.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(content);
        return result;
    }
}
