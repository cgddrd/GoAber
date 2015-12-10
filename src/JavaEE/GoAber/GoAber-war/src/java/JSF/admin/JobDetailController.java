package JSF.admin;

import GoAberDatabase.Community;
import GoAberDatabase.JobDetail;
import JSF.AuditController;
import JSF.AuditController;
import JSF.util.JsfUtil;
import JSF.util.PaginationHelper;
import SessionBean.JobDetailFacade;
import SessionBean.SchedulerSessionBeanRemote;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@Named("jobDetailController")
@SessionScoped
public class JobDetailController implements Serializable {

    @EJB(name = "io_SchedSessBean")
    private SchedulerSessionBeanRemote io_SchedSessBean;

    private JobDetail current;
    private List<JobDetail> items = null;
    private List<JobDetail> filteredItems = null;
    @EJB
    private SessionBean.JobDetailFacade ejbFacade;
    

    public JobDetailController() {
    }
    
    @PostConstruct
    public void init() {
        recreateItems();
    }

    
    
    public SchedulerSessionBeanRemote getScheduler() {
        return io_SchedSessBean;
    }

    public JobDetail getSelected() {
        if (current == null) {
            current = new JobDetail();
        }
        return current;
    }

    private JobDetailFacade getFacade() {
        return ejbFacade;
    }
    
    public List<SelectItem> getJobDetailSelectList() {
        List<SelectItem> jobdetailList = new ArrayList<>();
        
        for (JobDetail jobdetail : getFacade().findAll()) {
            jobdetailList.add(new SelectItem(jobdetail.getJobid()));
        }
        return jobdetailList;
    }


    public String prepareList() {
        recreateItems();
        return "List";
    }

    public String prepareView(JobDetail item) {
        current = item;
        return "View";
    }

    public String prepareCreate() {
        current = new JobDetail();
        return "Create";
    }

    public String create() {
        try {
            getScheduler().AddJob(current);
            getFacade().create(current);
            
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("JobDetailCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit(JobDetail data) {
        current = data;
        return "Edit";
    }

    public String update() {
        try {
            getScheduler().EditJob(current);
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("JobDetailUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
   public String prepareDestroy(JobDetail item) {
        current = item;
        destroy();
        return "List";
    }

    public String destroy() {
        performDestroy();
        // must recreate as something has been removed!
        recreateItems();
        return "List";
    }


    private void performDestroy() {
        try {
            getScheduler().RemoveJob(current);
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("JobDetailDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    
    private void recreateItems() {
        items = getFacade().findAll();
        filteredItems = null;
    }

    public List<JobDetail> getItems() {
        recreateItems();
        return items;
    }

    private void recreateModel() {
        items = null;
    }
    

    /**
     * @return the filteredItems
     */
    public List<JobDetail> getFilteredItems() {
        return filteredItems;
    }

    /**
     * @param filteredItems the filteredItems to set
     */
    public void setFilteredItems(List<JobDetail> filteredItems) {
        this.filteredItems = filteredItems;
    }
    

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public JobDetail getJobDetail(java.lang.String id) {
        return ejbFacade.find(id);
    }
    
    public JobDetail getCurrent() {
        return current;
    }

    @FacesConverter(forClass = JobDetail.class)
    public static class JobDetailControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            JobDetailController controller = (JobDetailController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "jobDetailController");
            return controller.getJobDetail(getKey(value));
        }

        java.lang.String getKey(String value) {
            java.lang.String key;
            key = value;
            return key;
        }

        String getStringKey(java.lang.String value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof JobDetail) {
                JobDetail o = (JobDetail) object;
                return getStringKey(o.getJobid());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + JobDetail.class.getName());
            }
        }

    }

}
