package com.ea.crm.config;

import com.ea.crm.dataprovider.exceptions.DataServiceException;
import com.ea.crm.dataprovider.exceptions.ErrorCodes;
import jakarta.annotation.PostConstruct;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Component
public class TenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl<Object> {
    private static final String DEFAULT_TENANT_ID = "000-000";

    @Autowired
    private DataSource defaultDS;

    @Autowired
    private ApplicationContext context;

    private Map<String, DataSource> map = new HashMap<>();

    boolean init = false;

    @PostConstruct
    public void load() {
        map.put(DEFAULT_TENANT_ID, defaultDS);
    }

    @Override
    protected DataSource selectAnyDataSource() {
        return map.get(DEFAULT_TENANT_ID);
    }

    @Override
    protected DataSource selectDataSource(Object tenantObject) {
        if (!init) {
            init = true;
            TenantDataSource tenantDataSource = context.getBean(TenantDataSource.class);
            map.putAll(tenantDataSource.getAll());
        }

        // Extract tenant identifier from the passed object
        String tenantIdentifier = extractTenantIdentifier(tenantObject);

        // Retrieve DataSource based on the tenant identifier
        if (map.get(tenantIdentifier) != null) {
            return map.get(tenantIdentifier);
        } else {
            throw new DataServiceException("Error", ErrorCodes.UNAUTHORIZED);
        }
    }

    // Method to extract tenant identifier from the passed object
    private String extractTenantIdentifier(Object tenantObject) {
        // Assuming tenantObject is an instance of a class with a `getTenantId()` method
        if (tenantObject instanceof Tenant) {
            return ((Tenant) tenantObject).getTenantId();  // Assuming Tenant is the class and getTenantId() gets the tenant identifier
        } else {
            throw new IllegalArgumentException("Invalid tenant object type");
        }
    }
}
