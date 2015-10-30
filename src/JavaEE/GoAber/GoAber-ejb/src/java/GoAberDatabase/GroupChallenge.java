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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author helen
 */
@Entity
@Table(name = "groupchallenge")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GroupChallenge.findAll", query = "SELECT g FROM GroupChallenge g"),
    @NamedQuery(name = "GroupChallenge.findByIdGroupChallenge", query = "SELECT g FROM GroupChallenge g WHERE g.idGroupChallenge = :idGroupChallenge"),
    @NamedQuery(name = "GroupChallenge.findByGroupId", query = "SELECT g FROM GroupChallenge g WHERE g.groupId = :groupId"),
    @NamedQuery(name = "GroupChallenge.findByChallengeId", query = "SELECT g FROM GroupChallenge g WHERE g.challengeId = :challengeId")})
public class GroupChallenge implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idGroupChallenge")
    private Integer idGroupChallenge;
    @Basic(optional = false)
    @NotNull
    @Column(name = "groupId")
    private int groupId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "challengeId")
    private int challengeId;

    public GroupChallenge() {
    }

    public GroupChallenge(Integer idGroupChallenge) {
        this.idGroupChallenge = idGroupChallenge;
    }

    public GroupChallenge(Integer idGroupChallenge, int groupId, int challengeId) {
        this.idGroupChallenge = idGroupChallenge;
        this.groupId = groupId;
        this.challengeId = challengeId;
    }

    public Integer getIdGroupChallenge() {
        return idGroupChallenge;
    }

    public void setIdGroupChallenge(Integer idGroupChallenge) {
        this.idGroupChallenge = idGroupChallenge;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(int challengeId) {
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
