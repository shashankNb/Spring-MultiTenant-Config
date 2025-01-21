package com.ea.crm.config;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Tenant {
    private String tenantId;
    private String tenantName;

    // Constructor
    public Tenant(String tenantId, String tenantName) {
        this.tenantId = tenantId;
        this.tenantName = tenantName;
    }

}