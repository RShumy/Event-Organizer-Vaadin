package com.eventorganizr.organizr.views.eventView;

import com.eventorganizr.organizr.entity.Event;
import com.eventorganizr.organizr.security.SecurityService;
import com.eventorganizr.organizr.service.EventService;
import com.eventorganizr.organizr.views.MainPage;
import com.eventorganizr.organizr.views.navView.NavView;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinServletResponse;

import javax.annotation.security.PermitAll;
import java.io.IOException;
import java.util.Optional;

@PermitAll
@Route(value = "/"
        , layout = MainPage.class
)
public class EventMainView extends VerticalLayout {

    private SecurityService securityService;

    private final EventListView eventList = new EventListView();

    private final EventDetailsView eventDetailsView = new EventDetailsView();

    private final NavView navView;

    HorizontalLayout eventListAndDetailView = new HorizontalLayout(eventList,eventDetailsView);
    VerticalLayout navAboveListAndDetailView = new VerticalLayout();

    private final EventService eventService;

    public EventMainView(EventService eventService
            , SecurityService securityService
    ) throws IOException {
        this.securityService = securityService;
        this.eventService = eventService;
        this.navView = new NavView(securityService);
        addClassName("event-main-view");

        updateEvents();

        configureEventDetailsView();

        setSizeFull();

        add(getContent());
    }

    public void logoutRedirect() throws IOException {
        if (securityService.getAuthenticatedUser() == null)
            VaadinServletResponse.getCurrent().sendRedirect("/login");
    }


    private void configureEventDetailsView() {
        eventList.addListener(EventListView.AddNewEvent.class, event -> openEventDetailsView(new Event()));
        eventList.addListener(EventListView.SelectedEvent.class, event -> openEventDetailsView(event.getEvent()));
        eventDetailsView.setSizeFull();
        eventDetailsView.addListener(EventDetailsView.SaveEvent.class,this::saveEvent);
        eventDetailsView.addListener(EventDetailsView.DeleteEvent.class, this::deleteEvent);
        eventDetailsView.closeButton.addClickListener(click -> closeEventDetailsView());
    }

    private void deleteEvent(EventDetailsView.DeleteEvent event) {
        eventService.delete(event.getEvent().getEventId());
        updateEvents();
    }

    private void saveEvent(EventDetailsView.SaveEvent event) {
        if (Optional.ofNullable(event.getEvent().getEventId()).isPresent()) {
            eventService.updateEvent(event.getEvent().getEventId(), event.getEvent());
        }
        else eventService.saveEvent(event.getEvent());
        updateEvents();
    }


    public Component getContent() {
        configureEventDetailsView();
        navView.setHeight("5%");
        eventListAndDetailView.setHeight("95%");
        eventListAndDetailView.setWidth("100%");
        eventListAndDetailView.setFlexGrow(1, eventList);
        eventListAndDetailView.setFlexGrow(4, eventDetailsView);
        eventDetailsView.setVisible(false);
        navAboveListAndDetailView.add(navView,eventListAndDetailView);
        navAboveListAndDetailView.setSizeFull();
        return navAboveListAndDetailView;
    }

    public void openEventDetailsView(Event event){
        eventDetailsView.setEvent(event);
        if(Optional.ofNullable(event.getEventName()).orElse("").equals(""))
            eventDetailsView.setFields(true);
        eventList.setWidth("25%");
        eventDetailsView.setVisible(true);
    }

    public void closeEventDetailsView(){
        eventDetailsView.setVisible(false);
        eventListAndDetailView.setFlexGrow(0,eventDetailsView);
    }

    private void updateEvents() {
        eventList.updateEvents(eventService.getEvents());
    }


}
