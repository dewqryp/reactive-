package org.dewqryp.reactive.repositories;

import org.dewqryp.reactive.data.Taco;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TacoRepository extends ReactiveCrudRepository<Taco,Long> {


}
