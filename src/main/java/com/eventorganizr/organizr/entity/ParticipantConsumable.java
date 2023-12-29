package com.eventorganizr.organizr.entity;

import com.eventorganizr.organizr.entity.compositeKeys.ParticipantConsumablesKey;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "participant_consumables")
public class ParticipantConsumable {

    @EmbeddedId
    private ParticipantConsumablesKey participantConsumablesId;


    @ManyToOne
    @MapsId("participantId")
    @JoinColumns({
            @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = true, updatable = true),
            @JoinColumn(name = "event_id",referencedColumnName = "event_id" ,insertable = true, updatable = true)
    })
    @JsonBackReference
    private Participant participant;

    @ManyToOne
    @MapsId("consumableId")
    @JoinColumn(name = "consumable_id")
    @JsonBackReference
    private Consumable consumable;

    public ParticipantConsumable(Participant participant, Consumable consumable){
        participantConsumablesId = new ParticipantConsumablesKey(consumable.getConsumableId(), participant.getParticipantId());
        this.participant = participant;
        this.consumable = consumable;
    }

    public ParticipantConsumable() {}

    public ParticipantConsumablesKey getParticipantConsumablesId() {
        return participantConsumablesId;
    }

    public void setParticipantConsumablesId(ParticipantConsumablesKey participantConsumablesId) {
        this.participantConsumablesId = participantConsumablesId;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public Consumable getConsumable() {
        return consumable;
    }

    public void setConsumable(Consumable consumable) {
        this.consumable = consumable;
    }

}
