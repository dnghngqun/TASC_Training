package com.tasc.hongquan.gomsuserver.superAdmin;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;

public class SuperAdminAuthenticationToken extends AbstractAuthenticationToken {
    public SuperAdminAuthenticationToken() {
        super(List.of(new SimpleGrantedAuthority("superadmin"), new SimpleGrantedAuthority("admin")));
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return "superadmin";
    }
}
