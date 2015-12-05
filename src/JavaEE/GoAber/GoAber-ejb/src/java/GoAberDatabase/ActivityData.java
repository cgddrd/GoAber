/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GoAberDatabase;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author connorgoddard
 */
@Entity
@Cacheable(false)
@Table(name = "ActivityData")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ActivityData.findAll", query = "SELECT a FROM ActivityData a"),
    @NamedQuery(name = "ActivityData.findByIdActivityData", query = "SELECT a FROM ActivityData a WHERE a.idActivityData = :idActivityData"),
    @NamedQuery(name = "ActivityData.findByValue", query = "SELECT a FROM ActivityData a WHERE a.value = :value"),
    @NamedQuery(name = "ActivityData.findByLastUpdated", query = "SELECT a FROM ActivityData a WHERE a.lastUpdated = :lastUpdated"),
    @NamedQuery(name = "ActivityData.findByDate", query = "SELECT a FROM ActivityData a WHERE a.date = :date"),
    @NamedQuery(name = "ActivityData.findAllForUser", query = "SELECT a FROM ActivityData a WHERE a.userId.idUser = :id"),
    @NamedQuery(name = "ActivityData.findAllForUserWithUnit", query = "SELECT a FROM ActivityData a WHERE a.userId.idUser = :id AND a.categoryUnitId.unitId.name = :unit"),
    @NamedQuery(name = "ActivityData.getAllForUserInDateRange", query = "SELECT a FROM ActivityData a WHERE a.userId.idUser = :id AND a.date > :startDate AND a.date < :endDate AND a.categoryUnitId.unitId.name = :unit"),
    @NamedQuery(name = "ActivityData.findActivityDataForGroup", query = "SELECT a FROM ActivityData a WHERE a.userId.groupId.idGroup = :id AND a.categoryUnitId.unitId.name = :unit"),
    @NamedQuery(name = "ActivityData.findActivityDataForGroupInDateRange", query = "SELECT a FROM ActivityData a WHERE a.userId.idUser = :userId AND a.userId.groupId.idGroup = :id AND a.categoryUnitId.unitId.name = :unit AND a.date >= :startDate AND a.date <= :endDate"),
    @NamedQuery(name = "ActivityData.findActivityDataForCommunityInDateRange", query = "SELECT a FROM ActivityData a WHERE a.userId.idUser = :userId AND a.userId.groupId.communityId.idCommunity = :id AND a.categoryUnitId.unitId.name = :unit AND a.date >= :startDate AND a.date <= :endDate")})
public class ActivityData implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idActivityData")
    private Integer idActivityData;
    @Column(name = "value")
    private Integer value;
    @Column(name = "lastUpdated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;
    @JoinColumn(name = "userId", referencedColumnName = "idUser")
    @ManyToOne(optional = false)
    private User userId;
    @JoinColumn(name = "categoryUnitId", referencedColumnName = "idCategoryUnit")
    @ManyToOne(optional = false)
    private CategoryUnit categoryUnitId;

    public ActivityData() {
        this.lastUpdated = new Date();
        this.date = new Date();
    }

    public ActivityData(Integer idActivityData) {
        this.idActivityData = idActivityData;
    }
    
    public ActivityData(Integer value, Date lastUpdated, Date date, User userId, CategoryUnit categoryUnitId) {
        this.value = value;
        this.lastUpdated = lastUpdated;
        this.date = date;
        this.userId = userId;
        this.categoryUnitId = categoryUnitId;
    }

    public Integer getIdActivityData() {
        return idActivityData;
    }

    public void setIdActivityData(Integer idActivityData) {
        this.idActivityData = idActivityData;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public CategoryUnit getCategoryUnitId() {
        return categoryUnitId;
    }

    public void setCategoryUnitId(CategoryUnit categoryUnitId) {
        this.categoryUnitId = categoryUnitId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idActivityData != null ? idActivityData.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ActivityData)) {
            return false;
        }
        ActivityData other = (ActivityData) object;
        if ((this.idActivityData == null && other.idActivityData != null) || (this.idActivityData != null && !this.idActivityData.equals(other.idActivityData))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GoAberDatabase.ActivityData[ idActivityData=" + idActivityData + " ]";
    }
    
}
