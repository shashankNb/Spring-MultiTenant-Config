package com.ea.crm.dataprovider;

import java.util.Map;

public class Modules {
    public static final int CRM = 1;

    private final Map<Integer, Integer> map;

    public Modules(Map<Integer, Integer> modStatusMap) {
        this.map = modStatusMap;
    }

    public boolean hasCRM() {
        return this.hasModule(CRM);
    }

    public boolean hasModule(Integer modId) {
        return map.containsKey(modId) && map.get(modId) == 1;
    }
}
