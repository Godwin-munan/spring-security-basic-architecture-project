package com.munan.feeniestore.security.authentication;

import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class TokenAuthentication extends UsernamePasswordAuthenticationToken {
    public TokenAuthentication(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public TokenAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
