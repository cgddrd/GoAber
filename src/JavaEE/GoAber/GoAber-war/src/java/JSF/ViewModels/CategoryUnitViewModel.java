/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JSF.ViewModels;

import GoAberDatabase.Category;
import GoAberDatabase.Unit;
import java.util.List;

/**
 *
 * @author samuel
 */
public class CategoryUnitViewModel {
    private Category category;
    private List<Unit> units;

    /**
     * @return the category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * @return the units
     */
    public List<Unit> getUnits() {
        return units;
    }

    /**
     * @param units the units to set
     */
    public void setUnits(List<Unit> units) {
        this.units = units;
    }
}
