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
 * @author helen
 */
@Entity
@Table(name = "categoryunit")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CategoryUnit.findAll", query = "SELECT c FROM CategoryUnit c"),
    @NamedQuery(name = "CategoryUnit.findByIdCategoryUnit", query = "SELECT c FROM CategoryUnit c WHERE c.idCategoryUnit = :idCategoryUnit")})
public class CategoryUnit implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idCategoryUnit")
    private Integer idCategoryUnit;
    @JoinColumn(name = "categoryId", referencedColumnName = "idCategory")
    @ManyToOne(optional = false)
    private Category categoryId;
    @JoinColumn(name = "unitId", referencedColumnName = "idUnit")
    @ManyToOne(optional = false)
    private Unit unitId;

    public CategoryUnit() {
    }

    public CategoryUnit(Integer idCategoryUnit) {
        this.idCategoryUnit = idCategoryUnit;
    }

    public Integer getIdCategoryUnit() {
        return idCategoryUnit;
    }

    public void setIdCategoryUnit(Integer idCategoryUnit) {
        this.idCategoryUnit = idCategoryUnit;
    }

    public Category getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Category categoryId) {
        this.categoryId = categoryId;
    }

    public Unit getUnitId() {
        return unitId;
    }

    public void setUnitId(Unit unitId) {
        this.unitId = unitId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCategoryUnit != null ? idCategoryUnit.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CategoryUnit)) {
            return false;
        }
        CategoryUnit other = (CategoryUnit) object;
        if ((this.idCategoryUnit == null && other.idCategoryUnit != null) || (this.idCategoryUnit != null && !this.idCategoryUnit.equals(other.idCategoryUnit))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GoAberDatabase.CategoryUnit[ idCategoryUnit=" + idCategoryUnit + " ]";
    }
    
}
