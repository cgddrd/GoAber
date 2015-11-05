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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author helen
 */
@Entity
@Table(name = "devicetype")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DeviceType.findAll", query = "SELECT d FROM DeviceType d"),
    @NamedQuery(name = "DeviceType.findByIdDeviceType", query = "SELECT d FROM DeviceType d WHERE d.idDeviceType = :idDeviceType"),
    @NamedQuery(name = "DeviceType.findByName", query = "SELECT d FROM DeviceType d WHERE d.name = :name"),
    @NamedQuery(name = "DeviceType.findByTokenEndpoint", query = "SELECT d FROM DeviceType d WHERE d.tokenEndpoint = :tokenEndpoint"),
    @NamedQuery(name = "DeviceType.findByConsumerKey", query = "SELECT d FROM DeviceType d WHERE d.consumerKey = :consumerKey"),
    @NamedQuery(name = "DeviceType.findByConsumerSecret", query = "SELECT d FROM DeviceType d WHERE d.consumerSecret = :consumerSecret"),
    @NamedQuery(name = "DeviceType.findByClientId", query = "SELECT d FROM DeviceType d WHERE d.clientId = :clientId"),
    @NamedQuery(name = "DeviceType.findByAuthorizationEndpoint", query = "SELECT d FROM DeviceType d WHERE d.authorizationEndpoint = :authorizationEndpoint")})
public class DeviceType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idDeviceType")
    private Integer idDeviceType;
    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @Size(max = 250)
    @Column(name = "tokenEndpoint")
    private String tokenEndpoint;
    @Size(max = 45)
    @Column(name = "consumerKey")
    private String consumerKey;
    @Size(max = 45)
    @Column(name = "consumerSecret")
    private String consumerSecret;
    @Size(max = 45)
    @Column(name = "clientId")
    private String clientId;
    @Size(max = 250)
    @Column(name = "authorizationEndpoint")
    private String authorizationEndpoint;

    public DeviceType() {
    }

    public DeviceType(Integer idDeviceType) {
        this.idDeviceType = idDeviceType;
    }

    public Integer getIdDeviceType() {
        return idDeviceType;
    }

    public void setIdDeviceType(Integer idDeviceType) {
        this.idDeviceType = idDeviceType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTokenEndpoint() {
        return tokenEndpoint;
    }

    public void setTokenEndpoint(String tokenEndpoint) {
        this.tokenEndpoint = tokenEndpoint;
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getAuthorizationEndpoint() {
        return authorizationEndpoint;
    }

    public void setAuthorizationEndpoint(String authorizationEndpoint) {
        this.authorizationEndpoint = authorizationEndpoint;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDeviceType != null ? idDeviceType.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DeviceType)) {
            return false;
        }
        DeviceType other = (DeviceType) object;
        if ((this.idDeviceType == null && other.idDeviceType != null) || (this.idDeviceType != null && !this.idDeviceType.equals(other.idDeviceType))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GoAberDatabase.DeviceType[ idDeviceType=" + idDeviceType + " ]";
    }
    
}
