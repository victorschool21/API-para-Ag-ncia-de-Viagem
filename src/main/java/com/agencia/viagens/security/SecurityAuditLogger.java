package com.agencia.viagens.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityAuditLogger {

    private static final Logger securityLogger = LoggerFactory.getLogger("SECURITY_AUDIT");

    public void logSuccessfulLogin(String username, String ipAddress) {
        securityLogger.info("LOGIN_SUCCESS - Username: {}, IP: {}", username, ipAddress);
    }

    public void logFailedLogin(String username, String ipAddress, String reason) {
        securityLogger.warn("LOGIN_FAILED - Username: {}, IP: {}, Reason: {}", username, ipAddress, reason);
    }

    public void logUserCreated(String username, String createdBy) {
        securityLogger.info("USER_CREATED - Username: {}, CreatedBy: {}", username, createdBy);
    }

    public void logInvalidToken(String token, String reason) {
        securityLogger.warn("INVALID_TOKEN - Reason: {}, Token: {}", reason, token != null ? token.substring(0, Math.min(20, token.length())) + "..." : "null");
    }
}

