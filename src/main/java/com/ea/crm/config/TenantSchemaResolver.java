package com.ea.crm.config;

import com.ea.crm.config.TenantContext;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

@Component
public class TenantSchemaResolver implements CurrentTenantIdentifierResolver {

    @Override
    public String resolveCurrentTenantIdentifier() {
        String currentTenant =  TenantContext.getCurrentTenant();
        if(currentTenant != null){
            return currentTenant;
        } else {
            return "public";
        }
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
