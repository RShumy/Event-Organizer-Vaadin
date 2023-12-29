package com.eventorganizr.organizr.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@Table(name = "events")
// To Ignore Json Infinite Recursion
// https://www.baeldung.com/jackson-bidirectional-relationships-and-infinite-recursion
// https://stackoverflow.com/questions/3325387/infinite-recursion-with-jackson-json-and-hibernate-jpa-issue
@JsonIgnoreProperties({"participants","consumables"})
public class Event {
    public Event(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;
    private String eventName;
    private LocalDateTime eventBeginDate;
    private LocalDateTime eventEndDate;
    private String locationQueryString;

    private String eventDescription;

    private Long createdByUserId;


    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @Column(nullable = false)
    @JsonManagedReference(value = "participants")
    private Set<Participant> participants;


    @ManyToMany(targetEntity = Consumable.class,cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(
            name = "event_consumables",
            joinColumns = { @JoinColumn(name = "event_id", insertable = true, updatable = true) },
            inverseJoinColumns = { @JoinColumn(name = "consumable_id", insertable = true, updatable = true) }
    )
    private Set<Consumable> consumables;

    public void addConsumable(Consumable consumable){
       consumables.add(consumable);
    }

    public void removeConsumable(Consumable consumable){
        consumables.remove(consumable);
    }


    public Event(String eventName, LocalDateTime eventBeginDate, LocalDateTime eventEndDate,
                 String eventDescription, String locationQueryString, Long createdByUserId) {
        this.eventName = eventName;
        this.eventBeginDate = eventBeginDate;
        this.eventEndDate = eventEndDate;
        this.locationQueryString = locationQueryString;
        this.eventDescription = eventDescription;
        this.createdByUserId = createdByUserId;
    }

    public Event(Long eventId, String eventName, LocalDateTime eventBeginDate, LocalDateTime eventEndDate, String locationQueryString, String eventDescription, Long createdByUserId) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventBeginDate = eventBeginDate;
        this.eventEndDate = eventEndDate;
        this.locationQueryString = locationQueryString;
        this.eventDescription = eventDescription;
        this.createdByUserId = createdByUserId;
    }
}
