/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GoAberDatabase;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Dan
 */
@Entity
@Table(name = "tasktype")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tasktype.findAll", query = "SELECT t FROM Tasktype t"),
    @NamedQuery(name = "Tasktype.findByTaskId", query = "SELECT t FROM Tasktype t WHERE t.taskId = :taskId"),
    @NamedQuery(name = "Tasktype.findByTaskName", query = "SELECT t FROM Tasktype t WHERE t.taskName = :taskName")})
public class Tasktype implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "taskId")
    private String taskId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "taskName")
    private String taskName;
    @OneToMany(mappedBy = "tasktype")
    private Collection<JobDetail> jobdetailCollection;

    public Tasktype() {
    }

    public Tasktype(String taskId) {
        this.taskId = taskId;
    }

    public Tasktype(String taskId, String taskName) {
        this.taskId = taskId;
        this.taskName = taskName;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    @XmlTransient
    public Collection<JobDetail> getJobdetailCollection() {
        return jobdetailCollection;
    }

    public void setJobdetailCollection(Collection<JobDetail> jobdetailCollection) {
        this.jobdetailCollection = jobdetailCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (taskId != null ? taskId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tasktype)) {
            return false;
        }
        Tasktype other = (Tasktype) object;
        if ((this.taskId == null && other.taskId != null) || (this.taskId != null && !this.taskId.equals(other.taskId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GoAberDatabase.Tasktype[ taskId=" + taskId + " ]";
    }
    
}
