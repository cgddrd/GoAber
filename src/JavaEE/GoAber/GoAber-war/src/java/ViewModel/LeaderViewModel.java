package ViewModel;

import GoAberDatabase.Unit;
import javax.faces.model.DataModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author samuel
 */
public class LeaderViewModel {
    private DataModel<LeaderItemViewModel> items;
    private Unit unit;   

    /**
     * @return the items
     */
    public DataModel<LeaderItemViewModel> getItems() {
        return items;
    }

    /**
     * @param items the items to set
     */
    public void setItems(DataModel<LeaderItemViewModel> items) {
        this.items = items;
    }

    /**
     * @return the unit
     */
    public Unit getUnit() {
        return unit;
    }

    /**
     * @param unit the unit to set
     */
    public void setUnit(Unit unit) {
        this.unit = unit;
    }
}
