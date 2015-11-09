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
@Table(name = "GroupChallenge")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GroupChallenge.findAll", query = "SELECT g FROM GroupChallenge g"),
    @NamedQuery(name = "GroupChallenge.findByIdGroupChallenge", query = "SELECT g FROM GroupChallenge g WHERE g.idGroupChallenge = :idGroupChallenge")})
public class GroupChallenge implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idGroupChallenge")
    private Integer idGroupChallenge;
    @JoinColumn(name = "groupId", referencedColumnName = "idGroup")
    @ManyToOne(optional = false)
    private Team groupId;
    @JoinColumn(name = "challengeId", referencedColumnName = "idChallenge")
    @ManyToOne(optional = false)
    private Challenge challengeId;

    public GroupChallenge() {
    }

    public GroupChallenge(Integer idGroupChallenge) {
        this.idGroupChallenge = idGroupChallenge;
    }

    public Integer getIdGroupChallenge() {
        return idGroupChallenge;
    }

    public void setIdGroupChallenge(Integer idGroupChallenge) {
        this.idGroupChallenge = idGroupChallenge;
    }

    public Team getGroupId() {
        return groupId;
    }

    public void setGroupId(Team groupId) {
        this.groupId = groupId;
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
        hash += (idGroupChallenge != null ? idGroupChallenge.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GroupChallenge)) {
            return false;
        }
        GroupChallenge other = (GroupChallenge) object;
        if ((this.idGroupChallenge == null && other.idGroupChallenge != null) || (this.idGroupChallenge != null && !this.idGroupChallenge.equals(other.idGroupChallenge))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GoAberDatabase.GroupChallenge[ idGroupChallenge=" + idGroupChallenge + " ]";
    }
    
}
