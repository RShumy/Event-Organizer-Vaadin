package com.eventorganizr.organizr.service;

import com.eventorganizr.organizr.entity.Consumable;
import com.eventorganizr.organizr.entity.Event;
import com.eventorganizr.organizr.exception.RecordNotFoundException;
import com.eventorganizr.organizr.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event findEvent(Long id) {
        return eventRepository.findById(id).
                orElseThrow(RecordNotFoundException::new);
    }

    public List<Event> getEvents() {
        return eventRepository.findAll();
    }

    public void saveEvent(Event event) {
        if (Optional.ofNullable(event.getEventName()).isEmpty() || Optional.ofNullable(event.getEventName()).get().equals(""))
            throw new RuntimeException("Name cannot be Empty");
        if (Optional.ofNullable(event.getEventBeginDate()).isEmpty())
            throw new RuntimeException("Cannot create Event without a Beginning Date");
        if (Optional.ofNullable(event.getEventEndDate()).isEmpty())
            event.setEventEndDate(event.getEventBeginDate().plus(3, ChronoUnit.HOURS));
        try { eventRepository.save(event); }
        catch (RuntimeException e){
            throw new RuntimeException("Server Error check console Stack Trace");
        }
    }

    public void updateEvent(Long id, Event event){
        Event eventToUpdate = eventRepository.findById(id).
                orElseThrow(RecordNotFoundException::new);
        eventToUpdate.setEventName(event.getEventName());
        eventToUpdate.setEventBeginDate(event.getEventBeginDate());
        eventToUpdate.setEventEndDate(event.getEventEndDate());
        eventToUpdate.setLocationQueryString(event.getLocationQueryString());
        eventToUpdate.setEventDescription(event.getEventDescription());
        eventRepository.save(eventToUpdate);
    }

    public void delete(Long id){
        if (eventRepository.findById(id).isPresent()) try {
            eventRepository.deleteById(id);
        }
        catch (RecordNotFoundException e){
            e.printStackTrace();
        }
    }

    public List<Consumable> getConsumablesAtEvent(Long eventId){
        return eventRepository.getConsumableAtEvent(eventId);
    }

}
