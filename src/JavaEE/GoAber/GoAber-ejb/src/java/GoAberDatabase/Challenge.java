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
 * @author connorgoddard
 */
@Entity
@Table(name = "Challenge")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Challenge.findAll", query = "SELECT c FROM Challenge c"),
    @NamedQuery(name = "Challenge.findByIdChallenge", query = "SELECT c FROM Challenge c WHERE c.idChallenge = :idChallenge"),
    @NamedQuery(name = "Challenge.findByCategoryUnit", query = "SELECT c FROM Challenge c WHERE c.categoryUnitId = :categoryUnitId"),
    @NamedQuery(name = "Challenge.findByStartTime", query = "SELECT c FROM Challenge c WHERE c.startTime = :startTime"),
    @NamedQuery(name = "Challenge.findByEndTime", query = "SELECT c FROM Challenge c WHERE c.endTime = :endTime"),
    @NamedQuery(name = "Challenge.findByName", query = "SELECT c FROM Challenge c WHERE c.name = :name")})
public class Challenge implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idChallenge")
    private Integer idChallenge;
    /*@Basic(optional = false)
    @NotNull
    @Column(name = "categoryUnit")
    private int categoryUnit;*/
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
    }

    public Challenge(Integer idChallenge) {
        this.idChallenge = idChallenge;
    }

    public Challenge(Integer idChallenge, CategoryUnit categoryUnitId, Date startTime) {
        this.idChallenge = idChallenge;
        this.categoryUnitId = categoryUnitId;
        this.startTime = startTime;
    }

    public Integer getIdChallenge() {
        return idChallenge;
    }

    public void setIdChallenge(Integer idChallenge) {
        this.idChallenge = idChallenge;
    }

    public CategoryUnit getCategoryUnitId() {
        return categoryUnitId;
    }

    public void setCategoryUnitId(CategoryUnit categoryUnitId) {
        this.categoryUnitId = categoryUnitId;
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
        System.out.println("getName()");
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        System.out.println("groupChallengeCollection.size()");
        System.out.println(groupChallengeCollection.size());
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
