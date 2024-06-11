package org.eventorganizer.app.views.eventViews;

import org.eventorganizer.app.entity.Event;
import org.eventorganizer.app.security.SecurityService;
import org.eventorganizer.app.service.EventService;
import org.eventorganizer.app.views.MainPage;
import org.eventorganizer.app.views.navView.NavView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.eventorganizer.app.webClient.EventWebClient;

import javax.annotation.security.PermitAll;
import java.util.Optional;

@PermitAll
@Route(value = "/"
        , layout = MainPage.class
)
public class EventMainView extends VerticalLayout {

    private final EventService eventService;
    private final EventWebClient eventWebClient;

    private final EventListView eventListView = new EventListView();

    private final EventDetailsView eventDetailsView = new EventDetailsView();

    private final NavView navView;

    HorizontalLayout eventListAndDetailView = new HorizontalLayout();
    VerticalLayout navAboveListAndDetailView = new VerticalLayout();


    public EventMainView(EventService eventService,
                         EventWebClient eventWebClient,
                         SecurityService securityService) {
        this.eventService = eventService;
        this.eventWebClient = eventWebClient;
        this.navView = new NavView(securityService);
        addClassName("event-main-view");

        updateEventList();

        setSizeFull();

        add(getContent());
    }

    private void configureEventDetailsView() {
        eventListView.addListener(EventListView.AddNewEvent.class, event -> {
            System.out.println("Add Event has been clicked"); openEventDetailsView(new Event());
        });
        eventListView.addListener(EventListView.SelectedEvent.class, event -> {
            System.out.println("Selected Event has been clicked"); openEventDetailsView(event.getEvent());
        });
        eventDetailsView.setSizeFull();
        eventDetailsView.addListener(EventDetailsView.SaveEvent.class,this::saveEvent);
        eventDetailsView.addListener(EventDetailsView.DeleteEvent.class, this::deleteEvent);
        eventDetailsView.closeButton.addClickListener(click -> closeEventDetailsView());
    }

    private void deleteEvent(EventDetailsView.DeleteEvent event) {
        eventService.delete(event.getEvent().getEventId());
        updateEventList();
    }

    private void saveEvent(EventDetailsView.SaveEvent event) {
        if (Optional.ofNullable(event.getEvent().getEventId()).isPresent()) {
            eventWebClient.updateEvent(event.getEvent().getEventId(), event.getEvent());
        }
        else eventWebClient.saveEvent(event.getEvent());
        updateEventList();
    }


    public Component getContent() {
        eventListAndDetailView.add(eventListView,eventDetailsView);
        configureEventDetailsView();
        navView.setHeight("5%");
        eventListAndDetailView.setHeight("95%");
        eventListAndDetailView.setWidth("100%");
        eventListAndDetailView.setFlexGrow(1, eventListView);
        eventListAndDetailView.setFlexGrow(4, eventDetailsView);
        eventDetailsView.setVisible(false);
        navAboveListAndDetailView.add(navView,eventListAndDetailView);
        navAboveListAndDetailView.setSizeFull();
        return navAboveListAndDetailView;
    }

    public void openEventDetailsView(Event event){
        eventDetailsView.setEvent(event);
        if(Optional.ofNullable(event.getEventName()).orElse("").isEmpty())
            eventDetailsView.enableFields(true);
        eventListView.setWidth("25%");
        eventDetailsView.setVisible(true);
    }

    public void closeEventDetailsView(){
        eventDetailsView.setVisible(false);
        eventListAndDetailView.setFlexGrow(0,eventDetailsView);
    }

    private void updateEventList() {
        eventListView.updateEvents(eventWebClient.getAllEvents());
    }


}