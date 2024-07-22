package com.example.onlineStoreApi.core.security.authorization;

public class Is {
    public static final String USER = "@customSecurityExpression.isUser(#id)";
    public static final String ADMIN_OR_USER = "@customSecurityExpression.isAdminOrUser(#id)";
    public static final String ADMIN_OR_STAFF = "@customSecurityExpression.isAdminOrStaff()";
    public static final String STAFF = "@customSecurityExpression.isStaff()";
}
