/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GoAberDatabase;

//From GoAber-Sheduler project.
import DTO.IJobDetail;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
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
    @NamedQuery(name = "JobDetail.findAll", query = "SELECT j FROM JobDetail j"),
    @NamedQuery(name = "JobDetail.findByJobid", query = "SELECT j FROM JobDetail j WHERE j.jobid = :jobid"),
    @NamedQuery(name = "JobDetail.findByTasktype", query = "SELECT j FROM JobDetail j WHERE j.tasktype = :tasktype"),
    @NamedQuery(name = "JobDetail.findBySchedtype", query = "SELECT j FROM JobDetail j WHERE j.schedtype = :schedtype"),
    @NamedQuery(name = "JobDetail.findByShcedtimemins", query = "SELECT j FROM JobDetail j WHERE j.shcedtimemins = :shcedtimemins"),
    @NamedQuery(name = "JobDetail.findByStartnow", query = "SELECT j FROM JobDetail j WHERE j.startnow = :startnow")})
public class JobDetail implements Serializable, IJobDetail {

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
    @Column(name = "shcedtimemins")
    private int shcedtimemins;
    @Column(name = "startnow")
    private Boolean startnow = false;

    public JobDetail() {
    }

    public JobDetail(String jobid) {
        this.jobid = jobid;
    }

    public JobDetail(String jobid, int tasktype, int schedtype, int shcedtimemins) {
        this.jobid = jobid;
        this.tasktype = tasktype;
        this.schedtype = schedtype;
        this.shcedtimemins = shcedtimemins;
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

    public int getShcedtimemins() {
        return shcedtimemins;
    }

    public void setShcedtimemins(int shcedtimemins) {
        this.shcedtimemins = shcedtimemins;
    }

    public Boolean getStartnow() {
        return startnow;
    }

    public void setStartnow(Boolean startnow) {
        this.startnow = startnow;
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
        if (!(object instanceof JobDetail)) {
            return false;
        }
        JobDetail other = (JobDetail) object;
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
