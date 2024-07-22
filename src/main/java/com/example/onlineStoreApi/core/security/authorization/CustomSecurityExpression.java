package com.example.onlineStoreApi.core.security.authorization;


import com.example.onlineStoreApi.core.security.authentication.AppUserDetails;
import com.example.onlineStoreApi.features.users.utils.UserRoles;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
public class CustomSecurityExpression {

    public boolean isUser(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        // Assuming UserDetailsService returns a UserDetails object that contains the user ID
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        return userDetails.getId().equals(userId);
    }


    public boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(UserRoles.admin.name()));
    }


    public boolean isStaff() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(UserRoles.staff.name()));
    }


    public boolean isAdminOrUser(Long userId) {
        return isUser(userId) || isAdmin();
    }


    public boolean isAdminOrStaff() {
        return isStaff() || isAdmin();
    }
}
