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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
 * @author helen
 */
@Entity
@Table(name = "dataRemovalAudit")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DataRemovalAudit.findAll", query = "SELECT d FROM DataRemovalAudit d"),
    @NamedQuery(name = "DataRemovalAudit.findByIdDataRemovalAudit", query = "SELECT d FROM DataRemovalAudit d WHERE d.idDataRemovalAudit = :idDataRemovalAudit"),
    @NamedQuery(name = "DataRemovalAudit.findByDateRemoved", query = "SELECT d FROM DataRemovalAudit d WHERE d.dateRemoved = :dateRemoved"),
    @NamedQuery(name = "DataRemovalAudit.findByDataRemoved", query = "SELECT d FROM DataRemovalAudit d WHERE d.dataRemoved = :dataRemoved")})
public class DataRemovalAudit implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idDataRemovalAudit")
    private Integer idDataRemovalAudit;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dateRemoved")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateRemoved;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "dataRemoved")
    private String dataRemoved;
    @Size(max = 200)
    @Column(name = "message")
    private String message;
    @JoinColumn(name = "userIdData", referencedColumnName = "idUser")
    @ManyToOne(optional = false)
    private User userIdData;
    @JoinColumn(name = "userIdWhoRemoved", referencedColumnName = "idUser")
    @ManyToOne(optional = false)
    private User userIdWhoRemoved;

    public DataRemovalAudit() {
    }

    public DataRemovalAudit(Integer idDataRemovalAudit) {
        this.idDataRemovalAudit = idDataRemovalAudit;
    }

    public DataRemovalAudit(Integer idDataRemovalAudit, Date dateRemoved, String dataRemoved) {
        this.idDataRemovalAudit = idDataRemovalAudit;
        this.dateRemoved = dateRemoved;
        this.dataRemoved = dataRemoved;
    }

    public Integer getIdDataRemovalAudit() {
        return idDataRemovalAudit;
    }

    public void setIdDataRemovalAudit(Integer idDataRemovalAudit) {
        this.idDataRemovalAudit = idDataRemovalAudit;
    }

    public Date getDateRemoved() {
        return dateRemoved;
    }

    public void setDateRemoved(Date dateRemoved) {
        this.dateRemoved = dateRemoved;
    }

    public String getDataRemoved() {
        return dataRemoved;
    }

    public void setDataRemoved(String dataRemoved) {
        this.dataRemoved = dataRemoved;
    }

    public User getUserIdData() {
        return userIdData;
    }

    public void setUserIdData(User userIdData) {
        this.userIdData = userIdData;
    }

    public User getUserIdWhoRemoved() {
        return userIdWhoRemoved;
    }

    public void setUserIdWhoRemoved(User userIdWhoRemoved) {
        this.userIdWhoRemoved = userIdWhoRemoved;
    }

    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDataRemovalAudit != null ? idDataRemovalAudit.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DataRemovalAudit)) {
            return false;
        }
        DataRemovalAudit other = (DataRemovalAudit) object;
        if ((this.idDataRemovalAudit == null && other.idDataRemovalAudit != null) || (this.idDataRemovalAudit != null && !this.idDataRemovalAudit.equals(other.idDataRemovalAudit))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GoAberDatabase.DataRemovalAudit[ idDataRemovalAudit=" + idDataRemovalAudit + " ]";
    }
    
}
