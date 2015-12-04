/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GoAberDatabase;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Dan
 */
@Entity
@Table(name = "Challenge")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Challenge.findAll", query = "SELECT c FROM Challenge c"),
    @NamedQuery(name = "Challenge.findByIdChallenge", query = "SELECT c FROM Challenge c WHERE c.idChallenge = :idChallenge"),
    @NamedQuery(name = "Challenge.findByStartTime", query = "SELECT c FROM Challenge c WHERE c.startTime = :startTime"),
    @NamedQuery(name = "Challenge.findByEndTime", query = "SELECT c FROM Challenge c WHERE c.endTime = :endTime"),
    @NamedQuery(name = "Challenge.findByName", query = "SELECT c FROM Challenge c WHERE c.name = :name"),
    @NamedQuery(name = "Challenge.findByComplete", query = "SELECT c FROM Challenge c WHERE c.complete = :complete"),
     @NamedQuery(name = "Challenge.unEnteredGroup", query = "SELECT d FROM Challenge d "
                                               + "INNER JOIN GroupChallenge g on d = g.challengeId "
                                               //+ " JOIN UserChallenge u on d = u.challengeId "
                                               + "WHERE g.groupId = :groupId"),// u.userId != :userId AND 
    @NamedQuery(name = "Challenge.unEnteredCommunity", query = "SELECT d FROM Challenge d "
                                               + "INNER JOIN CommunityChallenge c on d = c.challengeId "
                                               //+ "JOIN UserChallenge u on d = u.challengeId "
                                               + "WHERE c.communityId = :communityId"),

    @NamedQuery(name = "Challenge.enteredGroup", query = "SELECT d FROM Challenge d "
                                               + "INNER JOIN GroupChallenge g on d = g.challengeId "
                                               + "INNER JOIN UserChallenge u on d = u.challengeId "
                                               + "WHERE g.groupId = u.userId.groupId AND u.userId = :userId"),
    @NamedQuery(name = "Challenge.enteredCommunity", query = "SELECT d FROM Challenge d "
                                               + "INNER JOIN CommunityChallenge c on d = c.challengeId "
                                               + "INNER JOIN UserChallenge u on d = u.challengeId "
                                               + "WHERE u.userId.groupId.communityId = c.communityId AND u.userId = :userId")
    
        
    })
public class Challenge implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "idChallenge")
    private String idChallenge;
    @Basic(optional = false)
    @NotNull
    @Column(name = "startTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;
    @Column(name = "endTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;
    @Size(max = 100)
    @Column(name = "name")
    private String name;
    @Column(name = "complete")
    private Integer complete = 0;
    @JoinColumn(name = "categoryUnitId", referencedColumnName = "idCategoryUnit")
    @ManyToOne(optional = false)
    private CategoryUnit categoryUnitId;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "challengeId")
    private Collection<UserChallenge> userChallengeCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "challengeId")
    private Collection<GroupChallenge> groupChallengeCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "challengeId")
    private Collection<CommunityChallenge> communityChallengeCollection;

    public Challenge() {
        this.complete = 0;
    }

    public Challenge(String idChallenge) {
        this.complete = 0;
        this.idChallenge = idChallenge;
    }

    public Challenge(String idChallenge, CategoryUnit categoryUnitId,  Date startTime) {
        this.complete = 0;
        this.idChallenge = idChallenge;
        this.categoryUnitId = categoryUnitId;
        this.startTime = startTime;
    }

    public String getIdChallenge() {
        return idChallenge;
    }

    public void setIdChallenge(String idChallenge) {
        this.idChallenge = idChallenge;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getComplete() {
        if (complete == null) {
            complete = 0;
        }
        return complete != 0;
    }

    public void setComplete(boolean complete) {
        if  (complete) {
            this.complete = 1;   
        } else {
            this.complete = 0;
        }
    }

    public CategoryUnit getCategoryUnitId() {
        return categoryUnitId;
    }

    public void setCategoryUnitId(CategoryUnit categoryUnitId) {
        this.categoryUnitId = categoryUnitId;
    }
    
        @XmlTransient
    public Collection<UserChallenge> getUserChallengeCollection() {
        return userChallengeCollection;
    }

    public void setUserChallengeCollection(Collection<UserChallenge> userChallengeCollection) {
        this.userChallengeCollection = userChallengeCollection;
    }

    @XmlTransient
    public Collection<GroupChallenge> getGroupChallengeCollection() {
        return groupChallengeCollection;
    }

    public void setGroupChallengeCollection(Collection<GroupChallenge> groupChallengeCollection) {
        this.groupChallengeCollection = groupChallengeCollection;
    }

    
    @XmlTransient
    public Collection<CommunityChallenge> getCommunityChallengeCollection() {
        return communityChallengeCollection;
    }

    public void setCommunityChallengeCollection(Collection<CommunityChallenge> communityChallengeCollection) {
        this.communityChallengeCollection = communityChallengeCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idChallenge != null ? idChallenge.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Challenge)) {
            return false;
        }
        Challenge other = (Challenge) object;
        if ((this.idChallenge == null && other.idChallenge != null) || (this.idChallenge != null && !this.idChallenge.equals(other.idChallenge))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GoAberDatabase.Challenge[ idChallenge=" + idChallenge + " ]";
    }
    
}
