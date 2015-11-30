/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JSF.services;

import GoAberDatabase.Category;
import GoAberDatabase.CategoryUnit;
import GoAberDatabase.Unit;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import JSF.ViewModels.CategoryUnitViewModel;

/** Category - Unit Service class.
 * 
 * This provides access to the common functions used to get information about 
 * category units and for formatting them for display.
 *
 * @author samuel
 */
@Stateless
@LocalBean
@ManagedBean(name="categoryUnitService")
public class CategoryUnitService {

    @EJB
    SessionBean.CategoryUnitFacade ejbFacade;
    @EJB
    SessionBean.CategoryFacade categoryFacade;
    @EJB
    SessionBean.UnitFacade unitFacade;
    
    /** Get a list of category - unit view models for each entry in the system.
     * 
     * This is primarily a helper method to make it easier to display the complex
     * relationship between activity data, categories and units.
     * 
     * @return list of category unit view models
     */
    public List<CategoryUnitViewModel> getCategoryUnits() {
        List<CategoryUnitViewModel> viewModels = new ArrayList<>();
        List<Category> categories = categoryFacade.findAll();
        
        for(Category category : categories) {
            List<CategoryUnit> unitsForCategory = ejbFacade.findByCategory(category.getIdCategory());
            List<Unit> units = new ArrayList<>();
            CategoryUnitViewModel viewModel = new CategoryUnitViewModel();
            viewModel.setCategory(category);
             
            for(CategoryUnit categoryUnitItem : unitsForCategory) {
                Unit unit = unitFacade.find(categoryUnitItem.getUnitId().getIdUnit());
                units.add(unit);
            }
            viewModel.setUnits(units);
            viewModels.add(viewModel);
        }
        
        return viewModels;
    }
    
    /** Get a list of category - unit select items for each entry in the system.
     * 
     * This is primarily a helper method to make it easier to display the complex
     * relationship between activity data, categories and units as a drop down list.
     * 
     * This will create items for a drop down list with the categories as groups
     * and units as items.
     * 
     * @return list of category unit select items
     */
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
    
    private SelectItem[] createUnitsForCategory(int id) {
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
}
