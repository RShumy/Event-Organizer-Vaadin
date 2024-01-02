package com.eventorganizr.organizr.views.accessViews;

import com.eventorganizr.organizr.entity.User;
import com.eventorganizr.organizr.repository.UserRepository;
import com.eventorganizr.organizr.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import javax.persistence.Column;
import java.util.Arrays;
import java.util.Optional;

@Route(value = "sign-up")
@AnonymousAllowed
@RouteAlias("register")
public class RegisterView extends VerticalLayout {

    private final UserService userService;

    private User user;
    Binder<User> userBinder = new BeanValidationBinder<>(User.class);

    TextField userName;
    TextField firstName;
    TextField lastName;
    TextField email;
    PasswordField password;
    PasswordField passwordRepeatCheck = new PasswordField();

    Button submit = new Button("Submit and Register", new Icon(VaadinIcon.PLUS));

    public RegisterView(UserService userService){
        this.userService = userService;
        userBinder.bindInstanceFields(this);
        userBinder.readBean(new User());
        configureRegisterView();
    }

    private void configureRegisterView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        configureSubmitButton();
        add(userName,firstName,lastName, email,password, passwordRepeatCheck);
    }

    private void configureSubmitButton() {
        this.submit.addClickListener(event -> saveUser());
    }

    private void saveUser() {
        if(Optional.ofNullable(user).isPresent())
            try{ userBinder.writeBean(user);}
            catch (ValidationException e) {
                System.out.println(Arrays.toString(e.getStackTrace()));
            }
        userService.saveUser(user);
    }



}
