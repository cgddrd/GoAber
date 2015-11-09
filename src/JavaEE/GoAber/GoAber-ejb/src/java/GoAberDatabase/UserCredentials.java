/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GoAberDatabase;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
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
@Table(name = "UserCredentials")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserCredentials.findAll", query = "SELECT u FROM UserCredentials u"),
    @NamedQuery(name = "UserCredentials.findByIdUserCredentials", query = "SELECT u FROM UserCredentials u WHERE u.idUserCredentials = :idUserCredentials"),
    @NamedQuery(name = "UserCredentials.findByUsername", query = "SELECT u FROM UserCredentials u WHERE u.username = :username"),
    @NamedQuery(name = "UserCredentials.findByPassword", query = "SELECT u FROM UserCredentials u WHERE u.password = :password")})
public class UserCredentials implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idUserCredentials")
    private Integer idUserCredentials;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "password")
    private String password;
    @OneToMany(mappedBy = "userCredentialsId")
    private Collection<User> userCollection;

    public UserCredentials() {
    }

    public UserCredentials(Integer idUserCredentials) {
        this.idUserCredentials = idUserCredentials;
    }

    public UserCredentials(Integer idUserCredentials, String username, String password) {
        this.idUserCredentials = idUserCredentials;
        this.username = username;
        this.password = password;
    }

    public Integer getIdUserCredentials() {
        return idUserCredentials;
    }

    public void setIdUserCredentials(Integer idUserCredentials) {
        this.idUserCredentials = idUserCredentials;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @XmlTransient
    public Collection<User> getUserCollection() {
        return userCollection;
    }

    public void setUserCollection(Collection<User> userCollection) {
        this.userCollection = userCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUserCredentials != null ? idUserCredentials.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserCredentials)) {
            return false;
        }
        UserCredentials other = (UserCredentials) object;
        if ((this.idUserCredentials == null && other.idUserCredentials != null) || (this.idUserCredentials != null && !this.idUserCredentials.equals(other.idUserCredentials))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GoAberDatabase.UserCredentials[ idUserCredentials=" + idUserCredentials + " ]";
    }
    
}
