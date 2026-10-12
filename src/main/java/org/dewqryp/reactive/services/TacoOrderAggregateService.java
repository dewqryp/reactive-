package org.dewqryp.reactive.services;

import lombok.RequiredArgsConstructor;
import org.dewqryp.reactive.data.Taco;
import org.dewqryp.reactive.data.TacoOrder;
import org.dewqryp.reactive.repositories.TacoOrderRepository;
import org.dewqryp.reactive.repositories.TacoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TacoOrderAggregateService {
    private final TacoRepository tacoRepository;
    private final TacoOrderRepository tacoOrderRepository;

    public Mono<TacoOrder> save(TacoOrder tacoOrder) {
        return Mono.just(tacoOrder)
                .flatMap(order -> {
                    List<Taco> tacos = order.getTacos();
                    order.setTacos(new ArrayList<>());
                    order.setTacoIds(new HashSet<>());
                    return tacoRepository.saveAll(tacos)
                            .collectList()
                            .map(tacoList -> {
                                tacoList.forEach(order::addTaco);
                                return order;
                            });
                }).flatMap(tacoOrderRepository::save);
    }

    public Mono<TacoOrder> findById(Long id) {
        return tacoOrderRepository.findById(id)
                .flatMap(order -> {
                    return tacoRepository.findAllById(order.getTacoIds())
                            .collectList()
                            .map(tacos -> {
                                order.setTacos(tacos);
                                return order;
                            });
                });
    }

}
