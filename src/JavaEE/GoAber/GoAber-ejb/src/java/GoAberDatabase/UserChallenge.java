/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GoAberDatabase;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author connorgoddard
 */
@Entity
@Table(name = "UserChallenge")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserChallenge.findAll", query = "SELECT u FROM UserChallenge u"),
    @NamedQuery(name = "UserChallenge.findByIdUserChallenge", query = "SELECT u FROM UserChallenge u WHERE u.idUserChallenge = :idUserChallenge")})
public class UserChallenge implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idUserChallenge")
    private Integer idUserChallenge;
    @JoinColumn(name = "userId", referencedColumnName = "idUser")
    @ManyToOne(optional = false)
    private User userId;
    @JoinColumn(name = "challengeId", referencedColumnName = "idChallenge")
    @ManyToOne(optional = false)
    private Challenge challengeId;

    public UserChallenge() {
    }

    public UserChallenge(Integer idUserChallenge) {
        this.idUserChallenge = idUserChallenge;
    }

    public Integer getIdUserChallenge() {
        return idUserChallenge;
    }

    public void setIdUserChallenge(Integer idUserChallenge) {
        this.idUserChallenge = idUserChallenge;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public Challenge getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(Challenge challengeId) {
        this.challengeId = challengeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUserChallenge != null ? idUserChallenge.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserChallenge)) {
            return false;
        }
        UserChallenge other = (UserChallenge) object;
        if ((this.idUserChallenge == null && other.idUserChallenge != null) || (this.idUserChallenge != null && !this.idUserChallenge.equals(other.idUserChallenge))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GoAberDatabase.UserChallenge[ idUserChallenge=" + idUserChallenge + " ]";
    }
    
}
