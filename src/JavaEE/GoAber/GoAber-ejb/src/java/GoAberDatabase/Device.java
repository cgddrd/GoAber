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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author helen
 */
@Entity
@Table(name = "device")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Device.findAll", query = "SELECT d FROM Device d"),
    @NamedQuery(name = "Device.findByIdDevice", query = "SELECT d FROM Device d WHERE d.idDevice = :idDevice"),
    @NamedQuery(name = "Device.findByAccessToken", query = "SELECT d FROM Device d WHERE d.accessToken = :accessToken"),
    @NamedQuery(name = "Device.findByRefreshToken", query = "SELECT d FROM Device d WHERE d.refreshToken = :refreshToken"),
    @NamedQuery(name = "Device.findByTokenExpiration", query = "SELECT d FROM Device d WHERE d.tokenExpiration = :tokenExpiration")})
public class Device implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idDevice")
    private Integer idDevice;
    @Size(max = 45)
    @Column(name = "accessToken")
    private String accessToken;
    @Size(max = 45)
    @Column(name = "refreshToken")
    private String refreshToken;
    @Column(name = "tokenExpiration")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tokenExpiration;
    @JoinColumn(name = "deviceTypeId", referencedColumnName = "idDeviceType")
    @ManyToOne(optional = false)
    private DeviceType deviceTypeId;
    @JoinColumn(name = "userId", referencedColumnName = "idUser")
    @ManyToOne(optional = false)
    private User userId;

    public Device() {
    }

    public Device(Integer idDevice) {
        this.idDevice = idDevice;
    }

    public Integer getIdDevice() {
        return idDevice;
    }

    public void setIdDevice(Integer idDevice) {
        this.idDevice = idDevice;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Date getTokenExpiration() {
        return tokenExpiration;
    }

    public void setTokenExpiration(Date tokenExpiration) {
        this.tokenExpiration = tokenExpiration;
    }

    public DeviceType getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(DeviceType deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDevice != null ? idDevice.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Device)) {
            return false;
        }
        Device other = (Device) object;
        if ((this.idDevice == null && other.idDevice != null) || (this.idDevice != null && !this.idDevice.equals(other.idDevice))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GoAberDatabase.Device[ idDevice=" + idDevice + " ]";
    }
    
}
