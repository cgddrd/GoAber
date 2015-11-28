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
 * @author Dan
 */
@Entity
@Table(name = "webserviceauth")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WebServiceAuth.findAll", query = "SELECT w FROM WebServiceAuth w"),
    @NamedQuery(name = "WebServiceAuth.findByAuthtoken", query = "SELECT w FROM WebServiceAuth w WHERE w.authtoken = :authtoken"),
    @NamedQuery(name = "WebServiceAuth.findByAppname", query = "SELECT w FROM WebServiceAuth w WHERE w.appname = :appname"),
    @NamedQuery(name = "WebServiceAuth.findByUserid", query = "SELECT w FROM WebServiceAuth w WHERE w.userid = :userid"),
    @NamedQuery(name = "WebServiceAuth.findByExpire", query = "SELECT w FROM WebServiceAuth w WHERE w.expire = :expire"),
    @NamedQuery(name = "WebServiceAuth.findByStatusFlag", query = "SELECT w FROM WebServiceAuth w WHERE w.statusFlag = :statusFlag")})
public class WebServiceAuth implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "authtoken")
    private String authtoken;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "appname")
    private String appname;
    @Basic(optional = false)
    @NotNull
    @Column(name = "userid")
    private int userid;
    @Column(name = "expire")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expire;
    @Column(name = "status_flag")
    private Integer statusFlag;

    public WebServiceAuth() {
    }

    public WebServiceAuth(String authtoken) {
        this.authtoken = authtoken;
    }

    public WebServiceAuth(String authtoken, String appname, int userid) {
        this.authtoken = authtoken;
        this.appname = appname;
        this.userid = userid;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public Date getExpire() {
        return expire;
    }

    public void setExpire(Date expire) {
        this.expire = expire;
    }

    public Integer getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(Integer statusFlag) {
        this.statusFlag = statusFlag;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (authtoken != null ? authtoken.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WebServiceAuth)) {
            return false;
        }
        WebServiceAuth other = (WebServiceAuth) object;
        if ((this.authtoken == null && other.authtoken != null) || (this.authtoken != null && !this.authtoken.equals(other.authtoken))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GoAberDatabase.WebServiceAuth[ authtoken=" + authtoken + " ]";
    }
    
}
