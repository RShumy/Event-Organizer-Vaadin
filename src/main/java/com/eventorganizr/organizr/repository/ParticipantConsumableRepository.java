package com.eventorganizr.organizr.repository;

import com.eventorganizr.organizr.entity.Consumable;
import com.eventorganizr.organizr.entity.Participant;
import com.eventorganizr.organizr.entity.ParticipantConsumable;
import com.eventorganizr.organizr.entity.compositeKeys.ParticipantConsumablesKey;
import com.eventorganizr.organizr.entity.compositeKeys.ParticipantKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface ParticipantConsumableRepository extends JpaRepository<ParticipantConsumable, Long> {

    List<ParticipantConsumable> findByParticipant(Participant participant);

    List<ParticipantConsumable> findByParticipant_ParticipantId(ParticipantKey participant_participantId);

    List<ParticipantConsumable> findByParticipantAndConsumable_ConsumableType(Participant participant, String consumable_consumableType);

    void deleteParticipantConsumableByParticipantConsumablesId(ParticipantConsumablesKey participantConsumablesKey);

    void deleteAllByParticipant_ParticipantId(ParticipantKey participant_participantId);

    void deleteAllByParticipant_Event_EventIdAndConsumable_ConsumableType(Long participant_event_eventId, String consumable_consumableType);
}
