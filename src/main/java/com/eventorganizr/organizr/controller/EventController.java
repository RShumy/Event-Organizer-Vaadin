package com.eventorganizr.organizr.controller;

import com.eventorganizr.organizr.entity.Consumable;
import com.eventorganizr.organizr.entity.Event;
import com.eventorganizr.organizr.entity.Participant;
import com.eventorganizr.organizr.entity.User;
import com.eventorganizr.organizr.entity.compositeKeys.EventConsumablesKey;
import com.eventorganizr.organizr.entity.compositeKeys.ParticipantKey;
import com.eventorganizr.organizr.service.ConsumableService;
import com.eventorganizr.organizr.service.EventService;
import com.eventorganizr.organizr.service.ParticipantService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "/api/events")
public class EventController {

    private final EventService eventService;

    private final ConsumableService consumableService;
    private final ParticipantService participantService;

    public EventController(EventService eventService, ConsumableService consumableService, ParticipantService participantService) {
        this.eventService = eventService;
        this.consumableService = consumableService;
        this.participantService = participantService;
    }


    @GetMapping(path = "all")
    public List<Event> getEvents(){
        return eventService.getEvents();
    }

    @GetMapping(path = "{id}")
    public Event getEvent(@PathVariable Long id){
        return eventService.findEvent(id);
    }

//    @GetMapping(path = "created_by_logged_in")

    @PostMapping
    public void createEvent(@RequestBody Event event){
        //Has to be JSON Formatted when testing in PostMan
        eventService.saveEvent(event);
    }

    @DeleteMapping(path = "{id}")
    public void deleteEvent(@PathVariable Long id){
        eventService.delete(id);
    }

    @PutMapping(path = "{id}")
    public void updateEvent(@PathVariable Long id,@RequestBody Event event){
        eventService.updateEvent(id, event);
    }

    @PutMapping(path ="addConsumable")
    public void addConsumable(@RequestBody EventConsumablesKey eventConsumable){
        Event event = eventService.findEvent(eventConsumable.eventId());
        event.addConsumable(consumableService.findConsumable(eventConsumable.consumableId()));
        eventService.updateEvent(event.getEventId(), event);
    }

    @DeleteMapping(path = "removeConsumable")
    public void removeConsumable(@RequestBody EventConsumablesKey eventConsumable){
        Event event = eventService.findEvent(eventConsumable.eventId());
        event.removeConsumable(consumableService.findConsumable(eventConsumable.consumableId()));
        eventService.updateEvent(event.getEventId(), event);
    }

    @GetMapping(path = "allParticipants")
    public List<Participant> getAllParticipants(){
        return participantService.getAllParticipants();
    }

    @GetMapping(path = "events_for_user/{userId}")
    public List<Event> getEventsForUser(@PathVariable Long userId){
        return participantService.getEventsForUser(userId);
    }

    @GetMapping(path = "users_at_event/{eventId}")
    public List<User> getUsersAtEvent(@PathVariable Long eventId){
        return participantService.getUsersByEvent(eventId);
    }

    @PostMapping(path = "participantId")
    public void addParticipant(@RequestBody ParticipantKey participantId){
        participantService.saveParticipant(participantId);
    }
    @DeleteMapping(path = "participantId")
    public void deleteParticipant(@RequestBody ParticipantKey participantId){
        participantService.deleteById(participantId);
    }

    //EventConsumables
    @GetMapping(path = "{eventId}/consumables")
    public List<Consumable> getAllConsumables(@PathVariable Long eventId){
        return eventService.getConsumablesAtEvent(eventId);
    }

    @GetMapping(path = "consumableById")
    public Consumable findConsumableById(Long id){
        return consumableService.findConsumable(id);
    }
}
