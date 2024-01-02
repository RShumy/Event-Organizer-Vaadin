package com.eventorganizr.organizr.views.accessViews;

import com.eventorganizr.organizr.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinServletResponse;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.io.IOException;


@Route("/login")
@AnonymousAllowed
public class LogInView extends VerticalLayout
        implements BeforeEnterObserver
{
    private final UserService userService;
    private final LoginForm loginForm = new LoginForm();
    private final VerticalLayout registerNewUser = new VerticalLayout();
    private final HorizontalLayout frontDesk = new HorizontalLayout();

    public LogInView(UserService userService){
        this.userService = userService;
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        configureRegisterNewUser();

        frontDesk.add(loginForm,registerNewUser);
        loginForm.setAction("login");
        add(
                new H1("Event Organizer App"),
                frontDesk
        );
    }

    public void configureRegisterNewUser(){
        RegisterView registerView = new RegisterView(userService);
        H2 description = new H2("Do you want to try ?");
        Button signUpNow = new Button("Sign Up Now");
        signUpNow.setIcon(new Icon(VaadinIcon.USER));
        signUpNow.addClickListener(event -> {
            try {
                VaadinServletResponse.getCurrent().sendRedirect("sign-up");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        registerNewUser.add(description, signUpNow);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            loginForm.setError(true);
        }
    }
}

