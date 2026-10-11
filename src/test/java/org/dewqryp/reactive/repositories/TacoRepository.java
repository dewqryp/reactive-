package org.dewqryp.reactive.repositories;

import org.dewqryp.reactive.data.Taco;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface TacoRepository extends ReactiveCrudRepository<Taco,Integer> {
}
