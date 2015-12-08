/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ViewModel;

import GoAberDatabase.ActivityData;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author samuel
 */
public final class LeaderItemViewModel {
    private final String name;
    private final int numMembers;
    private final double total;

    public LeaderItemViewModel(String name, List<ActivityData> data) {
        this.name = name;
        this.numMembers = countUsers(data);
        this.total = sumData(data);
    }
    
    public int countUsers(List<ActivityData> data) {
        Set<Integer> userIds = new HashSet<>();
        for(ActivityData item : data) {
            userIds.add(item.getUserId().getIdUser());
        }
        return userIds.size();
    }
    
    public double sumData(List<ActivityData> data) {
        double totalValue = 0;
        for(ActivityData item : data) {
            totalValue += item.getValue();
        }
        return totalValue;
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the numMembers
     */
    public int getNumMembers() {
        return numMembers;
    }

    /**
     * @return the total
     */
    public double getTotal() {
        return total;
    }
}
