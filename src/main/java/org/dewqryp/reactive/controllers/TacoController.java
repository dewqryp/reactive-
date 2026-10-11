package org.dewqryp.reactive.controllers;

import org.dewqryp.reactive.data.Taco;
import org.dewqryp.reactive.repositories.TacoRepository;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/api/tacos", produces = "application/json")
@CrossOrigin("*")
public class TacoController {
    private final TacoRepository tacoRepository;

    public TacoController(TacoRepository tacoRepository) {
        this.tacoRepository = tacoRepository;
    }

    @GetMapping(params = "recent")
    public Flux<Taco> recentTacos(){
        return tacoRepository.findAll().take(12);
    }

    @GetMapping("/{id}")
    public Mono<Taco> findById(@PathVariable Long id){
        return tacoRepository.findById(id);
    }

    @PostMapping(consumes = "application/json")
    public Mono<Taco> save(@RequestBody Mono<Taco> taco){
        return tacoRepository.saveAll(taco).next();
    }
}
