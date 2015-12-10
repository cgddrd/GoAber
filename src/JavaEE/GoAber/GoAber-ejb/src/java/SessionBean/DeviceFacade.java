/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import GoAberDatabase.CategoryUnit;
import GoAberDatabase.Device;
import GoAberDatabase.DeviceType;
import GoAberDatabase.User;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author helen
 */
@Stateless
public class DeviceFacade extends AbstractFacade<Device> {
    @PersistenceContext(unitName = "GoAber-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DeviceFacade() {
        super(Device.class);
    }
    
    public Device findByUserAndDeviceType(User userId, DeviceType deviceTypeId){
        try {
          return (Device)em.createNamedQuery("Device.findByUserAndDeviceType").setParameter("deviceTypeId", deviceTypeId).setParameter("userId", userId).getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }
}
