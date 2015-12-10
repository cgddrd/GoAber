package JSF;

import GoAberDatabase.Result;
import JSF.util.JsfUtil;
import SessionBean.ResultFacade;

import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;

@Named("resultController")
@SessionScoped
public class ResultController implements Serializable {
    private List<Result> items = null;
    @EJB
    private SessionBean.ResultFacade ejbFacade;


    public ResultController() {
    }


    private ResultFacade getFacade() {
        return ejbFacade;
    }


    public String prepareList() {
        recreateModel();
        return "List";
    }


    public List<Result> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }


    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Result getResult(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Result.class)
    public static class ResultControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ResultController controller = (ResultController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "resultController");
            return controller.getResult(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Result) {
                Result o = (Result) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Result.class.getName());
            }
        }

    }

}
