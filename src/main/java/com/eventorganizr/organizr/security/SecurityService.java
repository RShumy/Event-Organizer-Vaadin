package com.eventorganizr.organizr.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinServletResponse;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SecurityService {

    private static final String LOGOUT_URL = "/login";

    private SecurityContext getSecurityContext(){
        return SecurityContextHolder.getContext();
    }

    public UserDetails getAuthenticatedUser() throws IOException {
        Object principal = getSecurityContext().getAuthentication().getPrincipal();
        if (principal == null) {
            VaadinServletResponse.getCurrent().sendRedirect("/login");
            return null;
        }
        if (principal instanceof UserDetails) {
            return (UserDetails) principal;
        }
        // Anonymous or no authentication.
        return null;
    }

    public void logout() throws IOException {
//        UI.getCurrent().getPage().setLocation(LOGOUT_URL);
        VaadinSession.getCurrent().getSession().invalidate();
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        System.out.println("LOGOUT HANDLER IS CALLED");
        logoutHandler.logout(
                VaadinServletRequest.getCurrent().getHttpServletRequest(),
                null,
                getSecurityContext().getAuthentication());

        System.out.println(getSecurityContext().getAuthentication());
    }

}