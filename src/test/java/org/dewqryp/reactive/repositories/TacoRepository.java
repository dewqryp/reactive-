package org.dewqryp.reactive.repositories;

import org.dewqryp.reactive.data.Taco;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.List;

public interface TacoRepository extends ReactiveCrudRepository<Taco,Long> {
    List<Taco> id(Long id);
}
