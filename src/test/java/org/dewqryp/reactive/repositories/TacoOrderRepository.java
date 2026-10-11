package org.dewqryp.reactive.repositories;

import org.dewqryp.reactive.data.TacoOrder;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface TacoOrderRepository extends ReactiveCrudRepository<TacoOrder,Long> {
}
