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
@Table(name = "Audit")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Audit.findAll", query = "SELECT a FROM Audit a"),
    @NamedQuery(name = "Audit.findByIdAudit", query = "SELECT a FROM Audit a WHERE a.idAudit = :idAudit"),
    @NamedQuery(name = "Audit.findByUrlAccessed", query = "SELECT a FROM Audit a WHERE a.urlAccessed = :urlAccessed"),
    @NamedQuery(name = "Audit.findByTimestamp", query = "SELECT a FROM Audit a WHERE a.timestamp = :timestamp"),
    @NamedQuery(name = "Audit.findByMessage", query = "SELECT a FROM Audit a WHERE a.message = :message")})
public class Audit implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idAudit")
    private Integer idAudit;
    @Size(max = 255)
    @Column(name = "urlAccessed")
    private String urlAccessed;
    @Column(name = "timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    @Size(max = 1000)
    @Column(name = "message")
    private String message;
    @JoinColumn(name = "userId", referencedColumnName = "idUser")
    @ManyToOne
    private User userId;

    public Audit() {
    }

    public Audit(Integer idAudit) {
        this.idAudit = idAudit;
    }

    public Integer getIdAudit() {
        return idAudit;
    }

    public void setIdAudit(Integer idAudit) {
        this.idAudit = idAudit;
    }

    public String getUrlAccessed() {
        return urlAccessed;
    }

    public void setUrlAccessed(String urlAccessed) {
        this.urlAccessed = urlAccessed;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAudit != null ? idAudit.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Audit)) {
            return false;
        }
        Audit other = (Audit) object;
        if ((this.idAudit == null && other.idAudit != null) || (this.idAudit != null && !this.idAudit.equals(other.idAudit))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GoAberDatabase.Audit[ idAudit=" + idAudit + " ]";
    }
    
}
