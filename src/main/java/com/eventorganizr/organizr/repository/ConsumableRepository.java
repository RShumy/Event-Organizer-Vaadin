package com.eventorganizr.organizr.repository;

import com.eventorganizr.organizr.entity.Consumable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConsumableRepository extends JpaRepository<Consumable, Long> {

    List<Consumable> searchConsumablesByNameContains(String stringContainedInName);
}
