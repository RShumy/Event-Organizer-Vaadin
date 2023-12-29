package com.eventorganizr.organizr.controller;

import com.eventorganizr.organizr.entity.Consumable;
import com.eventorganizr.organizr.entity.ParticipantConsumable;
import com.eventorganizr.organizr.service.ConsumableService;
import com.eventorganizr.organizr.service.ParticipantConsumableService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/consumables")
public class ConsumableController {

    private final ConsumableService consumableService;

    public ConsumableController(ConsumableService consumableService){
        this.consumableService = consumableService;
    }

    @GetMapping
    List<Consumable> getAllConsumables(){return consumableService.getConsumables();}
}
