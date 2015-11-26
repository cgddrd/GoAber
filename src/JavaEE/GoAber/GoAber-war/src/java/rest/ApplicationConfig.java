/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author samuel
 */
public class ApplicationConfig {
    private final Set<Class<?>> classes;

    public ApplicationConfig() {
        HashSet<Class<?>> c = new HashSet<>();
        c.add(ActivityDataAPI.class);
        classes = Collections.unmodifiableSet(c);
    }

    public Set<Class<?>> getClasses() {
        return classes;
    }
}
