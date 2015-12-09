/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GoAberDatabase;

import DTO.IJobDetail;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Dan
 */
@Entity
@Table(name = "jobdetail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Jobdetail.findAll", query = "SELECT j FROM JobDetail j"),
    @NamedQuery(name = "JobDetail.findByTasktype", query = "SELECT j FROM JobDetail j WHERE j.tasktype = :tasktype"),
    @NamedQuery(name = "JobDetail.findBySchedtype", query = "SELECT j FROM JobDetail j WHERE j.schedtype = :schedtype"),
    @NamedQuery(name = "Jobdetail.findByJobid", query = "SELECT j FROM JobDetail j WHERE j.jobid = :jobid"),
    @NamedQuery(name = "Jobdetail.findByShcedtimemins", query = "SELECT j FROM JobDetail j WHERE j.shcedtimemins = :shcedtimemins"),
    @NamedQuery(name = "Jobdetail.findByStartnow", query = "SELECT j FROM JobDetail j WHERE j.startnow = :startnow")})
public class JobDetail implements Serializable, IJobDetail {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "jobid")
    private String jobid;
    @Column(name = "shcedtimemins")
    private int shcedtimemins = 0;
    @Column(name = "startnow")
    private Boolean startnow = true;
    @JoinColumn(name = "schedtype", referencedColumnName = "shedId")
    @ManyToOne
    private Scheduletype schedtype;
    @JoinColumn(name = "tasktype", referencedColumnName = "taskId")
    @ManyToOne
    private Tasktype tasktype;

    public JobDetail() {
    }

    public JobDetail(String jobid) {
        this.jobid = jobid;
    }

    @Override
    public String getJobid() {
        return jobid;
    }

    public void setJobid(String jobid) {
        this.jobid = jobid;
    }

    @Override
    public int getShcedtimemins() {
        return shcedtimemins;
    }

    public void setShcedtimemins(int shcedtimemins) {
        this.shcedtimemins = shcedtimemins;
    }

    @Override
    public Boolean getStartnow() {
        return startnow;
    }

    public void setStartnow(Boolean startnow) {
        this.startnow = startnow;
    }
    
    public Scheduletype getSchedtypeObj() {
        return schedtype;
    }

    public void setSchedtypeObj(Scheduletype schedtype) {
        this.schedtype = schedtype;
    }

    @Override
    public String getSchedtype() {
        if (schedtype == null) {
            schedtype = new Scheduletype();
            tasktype.setTaskId("Jawbone");
        }
        return schedtype.getShedId();
    }

    public void setSchedtype(String schedtype) {
        this.schedtype = new Scheduletype();
        if ("R".equals(schedtype)) {
            this.schedtype.setShedName("Recurring");
        } else {
            this.schedtype.setShedName("Once");
            
        }
        this.schedtype.setShedId(schedtype);
    }
    
    public Tasktype getTasktypeObj() {
        return tasktype;
    }

    public void setTasktypeObj(Tasktype tasktype) {
        this.tasktype = tasktype;
    }


    @Override
    public String getTasktype() {
         if (tasktype == null) {
            tasktype = new Tasktype();
            tasktype.setTaskId("R");
        }
        return tasktype.getTaskId();
    }

    public void setTasktype(String tasktype) {
        this.tasktype = new Tasktype();
        this.tasktype.setTaskId(tasktype);
        this.tasktype.setTaskName(tasktype);
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
    
    //Pass arguments to scheduler.
    @Transient
    private String[] is_args;
    public void setSchedulerArgs(String[] as_args) {
        is_args = as_args;
    }
    @Override
    public String[] scheduler_args() {
        return is_args;
    }
    
}
