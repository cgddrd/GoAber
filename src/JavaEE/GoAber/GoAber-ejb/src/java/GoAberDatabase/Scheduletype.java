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
@Table(name = "scheduletype")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Scheduletype.findAll", query = "SELECT s FROM Scheduletype s"),
    @NamedQuery(name = "Scheduletype.findByShedId", query = "SELECT s FROM Scheduletype s WHERE s.shedId = :shedId"),
    @NamedQuery(name = "Scheduletype.findByShedName", query = "SELECT s FROM Scheduletype s WHERE s.shedName = :shedName")})
public class Scheduletype implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "shedId")
    private String shedId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "shedName")
    private String shedName;
    @OneToMany(mappedBy = "schedtype")
    private Collection<JobDetail> jobdetailCollection;

    public Scheduletype() {
    }

    public Scheduletype(String shedId) {
        this.shedId = shedId;
    }

    public Scheduletype(String shedId, String shedName) {
        this.shedId = shedId;
        this.shedName = shedName;
    }

    public String getShedId() {
        return shedId;
    }

    public void setShedId(String shedId) {
        this.shedId = shedId;
    }

    public String getShedName() {
        return shedName;
    }

    public void setShedName(String shedName) {
        this.shedName = shedName;
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
        hash += (shedId != null ? shedId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Scheduletype)) {
            return false;
        }
        Scheduletype other = (Scheduletype) object;
        if ((this.shedId == null && other.shedId != null) || (this.shedId != null && !this.shedId.equals(other.shedId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GoAberDatabase.Scheduletype[ shedId=" + shedId + " ]";
    }
    
}
