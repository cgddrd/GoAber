/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ViewModel;

import GoAberDatabase.ActivityData;
import GoAberDatabase.User;
import java.util.List;

/**
 *
 * @author samuel
 */
public final class ParticipantLeaderViewModel {
    private final User user;
    private final double total;

    public ParticipantLeaderViewModel(User user, List<ActivityData> data) {
        this.user = user;
        this.total = sumData(data);
    }

    public double sumData(List<ActivityData> data) {
        double totalValue = 0;
        for(ActivityData item : data) {
            totalValue += item.getValue();
        }
        return totalValue;
    }
    
    
    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @return the total
     */
    public double getTotal() {
        return total;
    }
}
