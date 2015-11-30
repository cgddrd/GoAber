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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author helen
 */
@Entity
@Table(name = "communitychallenge")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CommunityChallenge.findAll", query = "SELECT c FROM CommunityChallenge c"),
    @NamedQuery(name = "CommunityChallenge.findByIdCommunityChallenge", query = "SELECT c FROM CommunityChallenge c WHERE c.idCommunityChallenge = :idCommunityChallenge"),
    @NamedQuery(name = "CommunityChallenge.findByStartedChallenge", query = "SELECT c FROM CommunityChallenge c WHERE c.startedChallenge = :startedChallenge")})
public class CommunityChallenge implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idCommunityChallenge")
    private Integer idCommunityChallenge;
    @Basic(optional = false)
    @NotNull
    @Column(name = "startedChallenge")
    private boolean startedChallenge;
    @JoinColumn(name = "challengeId", referencedColumnName = "idChallenge")
    @ManyToOne(optional = false)
    private Challenge challengeId;
    @JoinColumn(name = "communityId", referencedColumnName = "idCommunity")
    @ManyToOne(optional = false)
    private Community communityId;

    public CommunityChallenge() {
    }

    public CommunityChallenge(Integer idCommunityChallenge) {
        this.idCommunityChallenge = idCommunityChallenge;
    }

    public CommunityChallenge(Integer idCommunityChallenge, boolean startedChallenge) {
        this.idCommunityChallenge = idCommunityChallenge;
        this.startedChallenge = startedChallenge;
    }
    
    public CommunityChallenge (Community communityId, Challenge challengeId, boolean startedChallenge) {
        this.communityId = communityId;
        this.challengeId = challengeId;
        this.startedChallenge = startedChallenge;
    }
    
    public CommunityChallenge (Community communityId, Challenge challengeId) {
        this.communityId = communityId;
        this.challengeId = challengeId;
        this.startedChallenge = false;
    }

    public Integer getIdCommunityChallenge() {
        return idCommunityChallenge;
    }

    public void setIdCommunityChallenge(Integer idCommunityChallenge) {
        this.idCommunityChallenge = idCommunityChallenge;
    }

    public boolean getStartedChallenge() {
        return startedChallenge;
    }

    public void setStartedChallenge(boolean startedChallenge) {
        this.startedChallenge = startedChallenge;
    }

    public Challenge getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(Challenge challengeId) {
        this.challengeId = challengeId;
    }

    public Community getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Community communityId) {
        this.communityId = communityId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCommunityChallenge != null ? idCommunityChallenge.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CommunityChallenge)) {
            return false;
        }
        CommunityChallenge other = (CommunityChallenge) object;
        if ((this.idCommunityChallenge == null && other.idCommunityChallenge != null) || (this.idCommunityChallenge != null && !this.idCommunityChallenge.equals(other.idCommunityChallenge))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GoAberDB.CommunityChallenge[ idCommunityChallenge=" + idCommunityChallenge + " ]";
    }
    
}
