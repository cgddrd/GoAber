/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GoAberDatabase;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Dan
 */
@Entity
@Table(name = "jobdetail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Jobdetail.findAll", query = "SELECT j FROM Jobdetail j"),
    @NamedQuery(name = "Jobdetail.findByJobid", query = "SELECT j FROM Jobdetail j WHERE j.jobid = :jobid"),
    @NamedQuery(name = "Jobdetail.findByTasktype", query = "SELECT j FROM Jobdetail j WHERE j.tasktype = :tasktype"),
    @NamedQuery(name = "Jobdetail.findBySchedtype", query = "SELECT j FROM Jobdetail j WHERE j.schedtype = :schedtype"),
    @NamedQuery(name = "Jobdetail.findByShceddate", query = "SELECT j FROM Jobdetail j WHERE j.shceddate = :shceddate")})
public class Jobdetail implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "jobid")
    private String jobid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tasktype")
    private int tasktype;
    @Basic(optional = false)
    @NotNull
    @Column(name = "schedtype")
    private int schedtype;
    @Basic(optional = false)
    @NotNull
    @Column(name = "shceddate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date shceddate;

    public Jobdetail() {
    }

    public Jobdetail(String jobid) {
        this.jobid = jobid;
    }

    public Jobdetail(String jobid, int tasktype, int schedtype, Date shceddate) {
        this.jobid = jobid;
        this.tasktype = tasktype;
        this.schedtype = schedtype;
        this.shceddate = shceddate;
    }

    public String getJobid() {
        return jobid;
    }

    public void setJobid(String jobid) {
        this.jobid = jobid;
    }

    public int getTasktype() {
        return tasktype;
    }

    public void setTasktype(int tasktype) {
        this.tasktype = tasktype;
    }

    public int getSchedtype() {
        return schedtype;
    }

    public void setSchedtype(int schedtype) {
        this.schedtype = schedtype;
    }

    public Date getShceddate() {
        return shceddate;
    }

    public void setShceddate(Date shceddate) {
        this.shceddate = shceddate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (jobid != null ? jobid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Jobdetail)) {
            return false;
        }
        Jobdetail other = (Jobdetail) object;
        if ((this.jobid == null && other.jobid != null) || (this.jobid != null && !this.jobid.equals(other.jobid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GoAberDatabase.Jobdetail[ jobid=" + jobid + " ]";
    }
    
}
