package com.eventorganizr.organizr.entity;

import com.eventorganizr.organizr.entity.compositeKeys.ParticipantKey;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "participants")
// To Ignore list of ParticipantConsumables Json Infinite Recursion
@JsonIgnoreProperties("participantConsumables")
public class Participant {

    @EmbeddedId
    private ParticipantKey participantId;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("eventId")
    @JoinColumn(name = "event_id")
    @JsonBackReference(value = "event")
    private Event event;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "user")
    private User user;

    @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Column(nullable = false)
    @JsonManagedReference(value = "participantConsumables")
    private Set<ParticipantConsumable> participantConsumables;

    public Participant(){}

    public Participant(ParticipantKey participantId, Event event, User user) {
        this.participantId = participantId;
        this.event = event;
        this.user = user;
    }

    public Participant(Event event, User user){
        this.participantId = new ParticipantKey(event.getEventId(), user.getUserId());
        this.event = event;
        this.user = user;
    }

    public ParticipantKey getParticipantId() {
        return participantId;
    }

    public void setParticipantId(ParticipantKey participantID) {
        this.participantId = participantID;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Participant{" +
                "event=" + event.getEventId() +
                ", user=" + user.getUserId() +
                '}';
    }
}
