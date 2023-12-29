package com.eventorganizr.organizr.repository;

import com.eventorganizr.organizr.entity.Consumable;
import com.eventorganizr.organizr.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // Resolved like this because of LazyLoadingInitialization
    @Query(value = "SELECT " +
            "new Event(e.eventId, e.eventName, e.eventBeginDate, e.eventEndDate, e.locationQueryString, e.eventDescription, e.createdByUserId) " +
            "FROM Event e")
    List<Event> findAll();

    @Query(value = " SELECT e.consumables " +
            "FROM Event e " +
            "" +
            "where e.eventId = :id")
    List<Consumable> getConsumableAtEvent(Long id);

}
