package com.eventorganizr.organizr.service;

import com.eventorganizr.organizr.entity.Consumable;
import com.eventorganizr.organizr.exception.RecordNotFoundException;
import com.eventorganizr.organizr.repository.ConsumableRepository;
import com.eventorganizr.organizr.entity.Consumable;
import com.eventorganizr.organizr.exception.RecordNotFoundException;
import com.eventorganizr.organizr.repository.ConsumableRepository;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class ConsumableService {

    private final ConsumableRepository consumableRepository;

    public ConsumableService(ConsumableRepository consumableRepository) {
        this.consumableRepository = consumableRepository;
    }

    public Consumable findConsumable(Long id){
        return consumableRepository.findById(id).
                orElseThrow(RecordNotFoundException::new);
    }

    public List<Consumable> getConsumables() {
        return consumableRepository.findAll();
    }

    public void saveConsumable(Consumable consumable) {
        if (Optional.ofNullable(consumable.getName()).isEmpty() || Optional.ofNullable(consumable.getName()).get().equals(""))
            throw new RuntimeException("Name cannot be Empty");
        if (Optional.ofNullable(consumable.getConsumableType()).isEmpty() || Optional.ofNullable(consumable.getConsumableType()).get().equals(""))
            throw new RuntimeException("Cannot create Consumable without a Type");
        if (Optional.ofNullable(consumable.getShortDescription()).isEmpty() || Optional.ofNullable(consumable.getShortDescription()).get().equals(""))
            consumable.setShortDescription("Empty Description");
        try { consumableRepository.save(consumable); }
        catch (RuntimeException e){
            throw new RuntimeException("Server Error check console Stack Trace");
        }
    }

    public void updateConsumable(Long id, Consumable consumable){
        Consumable consumableToUpdate = consumableRepository.findById(id).
                orElseThrow(RecordNotFoundException::new);
        consumableToUpdate.setName(consumable.getName());
        consumableToUpdate.setConsumableType(consumable.getConsumableType());
        consumableToUpdate.setShortDescription(consumable.getShortDescription());
        consumableRepository.save(consumableToUpdate);
    }

    public List<Consumable> searchByNameContains(String nameContains){
        return consumableRepository.searchConsumablesByNameContains(nameContains);
    }

    public void delete(Long id){
        boolean existsById = consumableRepository.existsById(id);
        if (!existsById) throw new RecordNotFoundException();
        consumableRepository.deleteById(id);
    }
}
