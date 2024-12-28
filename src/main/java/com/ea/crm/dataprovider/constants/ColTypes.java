package com.ea.crm.dataprovider.constants;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Shashank Bhattarai on 3/29/2018.
 */
public enum ColTypes {
    /**
     * NONE = no column type
     */
    NONE,
    /**
     * INTEGER = meant to render a textbox that accepts a numeric filter
     * expression
     */
    INTEGER,
    /**
     * STRING = meant to render a textbox that accepts a String filter
     * expression
     */
    STRING,
    /**
     * DATE = meant to render a textbox that accepts a Date filter expression
     */
    DATE,
    /**
     * FLOAT = meant to render a textbox that accept a numeric decimal filter
     */
    FLOAT,
    /**
     * DOLLAR = meant to render a textbox that accept amount
     */
    DOLLAR,
    /**
     * PERCENTAGE = meant to render a textbox that accept percentage
     */
    PERCENTAGE,
    /**
     * AGE = meant to render a textbox that accepts an numeric age or age range
     * which does not produce a subset of age>89 population
     */
    AGE;

    public static ColTypes valueOf(int colType) {
        for (ColTypes ct : ColTypes.values()) {
            if (ct.ordinal() == colType) {
                return ct;
            }
        }
        return NONE;
    }

    public static List<ColTypes> getNumericTypes(){
        return Arrays.asList(INTEGER, FLOAT, DOLLAR, PERCENTAGE);
    }
}
