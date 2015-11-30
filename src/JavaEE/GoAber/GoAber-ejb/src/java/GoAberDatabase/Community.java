/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GoAberDatabase;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
 * @author connorgoddard
 */
@Entity
@Table(name = "Community")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Community.findAll", query = "SELECT c FROM Community c"),
    @NamedQuery(name = "Community.findByIdCommunity", query = "SELECT c FROM Community c WHERE c.idCommunity = :idCommunity"),
    @NamedQuery(name = "Community.findByName", query = "SELECT c FROM Community c WHERE c.name = :name"),
    @NamedQuery(name = "Community.findByEndpointUrl", query = "SELECT c FROM Community c WHERE c.endpointUrl = :endpointUrl")})
public class Community implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idCommunity")
    private Integer idCommunity;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "name")
    private String name;
    @Size(max = 250)
    @Column(name = "endpointUrl")
    private String endpointUrl;
    @OneToMany(mappedBy = "communityId")
    private Collection<Team> teamCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "communityId")
    private Collection<CommunityChallenge> communityChallengeCollection;

    public Collection<CommunityChallenge> getCommunityChallengeCollection() {
        return communityChallengeCollection;
    }

    public void setCommunityChallengeCollection(Collection<CommunityChallenge> communityChallengeCollection) {
        this.communityChallengeCollection = communityChallengeCollection;
    }

    public Community() {
    }

    public Community(Integer idCommunity) {
        this.idCommunity = idCommunity;
    }

    public Community(Integer idCommunity, String name) {
        this.idCommunity = idCommunity;
        this.name = name;
    }

    public Integer getIdCommunity() {
        return idCommunity;
    }

    public void setIdCommunity(Integer idCommunity) {
        this.idCommunity = idCommunity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEndpointUrl() {
        return endpointUrl;
    }

    public void setEndpointUrl(String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }
/*
    @XmlTransient
    public Collection<CommunityChallenge> getCommunityChallengeCollection() {
        return communityChallengeCollection;
    }

    public void setCommunityChallengeCollection(Collection<CommunityChallenge> communityChallengeCollection) {
        this.communityChallengeCollection = communityChallengeCollection;
    }*/

    @XmlTransient
    public Collection<Team> getTeamCollection() {
        return teamCollection;
    }

    public void setTeamCollection(Collection<Team> teamCollection) {
        this.teamCollection = teamCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCommunity != null ? idCommunity.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Community)) {
            return false;
        }
        Community other = (Community) object;
        if ((this.idCommunity == null && other.idCommunity != null) || (this.idCommunity != null && !this.idCommunity.equals(other.idCommunity))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GoAberDatabase.Community[ idCommunity=" + idCommunity + " ]";
    }
    
}
