package JSF;

import GoAberDatabase.Category;
import GoAberDatabase.CategoryUnit;
import GoAberDatabase.Unit;
import JSF.util.JsfUtil;
import JSF.util.PaginationHelper;
import SessionBean.CategoryUnitFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;


@ManagedBean(name="categoryUnitController")
@SessionScoped
public class CategoryUnitController implements Serializable {


    private CategoryUnit current;
    private DataModel items = null;
    @EJB private SessionBean.CategoryUnitFacade ejbFacade;
    @EJB private SessionBean.CategoryFacade categoryFacade;
    @EJB private SessionBean.UnitFacade unitFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public CategoryUnitController() {
    }
    
    public CategoryUnit getSelected() {
        if (current == null) {
            current = new CategoryUnit();
            selectedItemIndex = -1;
        }
        return current;
    }

    private CategoryUnitFacade getFacade() {
        return ejbFacade;
    }
    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem()+getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (CategoryUnit)getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new CategoryUnit();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CategoryUnitCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (CategoryUnit)getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CategoryUnitUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (CategoryUnit)getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CategoryUnitDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count-1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex+1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public List<SelectItem> getDisplayItems() {
        List<SelectItem> itemsList = new ArrayList<>();
        List<Category> categories = categoryFacade.findAll();
        for(Category category : categories) {
            SelectItemGroup group = new SelectItemGroup(category.getName());
            SelectItem[] selectItems = createUnitsForCategory(category.getIdCategory());
            group.setSelectItems(selectItems);
            itemsList.add(group);
        }
        return itemsList;
    }

    public SelectItem[] createUnitsForCategory(int id) {
        List<CategoryUnit> categoryUnits = ejbFacade.findByCategory(id);
        SelectItem[] selectItems = new SelectItem[categoryUnits.size()];
        int i =0;
        for(CategoryUnit categoryUnitItem : categoryUnits) {
            Unit unit = unitFacade.find(categoryUnitItem.getUnitId().getIdUnit());
            SelectItem item = new SelectItem(categoryUnitItem, unit.getName());
            selectItems[i] = item;
            i++;
        }
        return selectItems;
    }

    @FacesConverter(value="categoryUnitConverter",forClass=CategoryUnit.class)
    public static class CategoryUnitControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CategoryUnitController controller = (CategoryUnitController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "categoryUnitController");
            return controller.ejbFacade.find(getKey(value));
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
            if (object instanceof CategoryUnit) {
                CategoryUnit o = (CategoryUnit) object;
                return getStringKey(o.getIdCategoryUnit());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+CategoryUnit.class.getName());
            }
        }

    }

}
