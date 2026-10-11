package org.dewqryp.reactive;

import org.dewqryp.reactive.controllers.TacoController;
import org.dewqryp.reactive.data.Ingredient;
import org.dewqryp.reactive.data.Taco;
import org.dewqryp.reactive.repositories.TacoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TacoControllerTest {
    @Test
    public void shouldReturnRecentTacos(){
        Taco[] tacos = {
                testTaco(1L), testTaco(2L),
                testTaco(3L), testTaco(4L),
                testTaco(5L), testTaco(6L),
                testTaco(7L), testTaco(8L),
                testTaco(9L), testTaco(10L),
                testTaco(11L), testTaco(12L),
                testTaco(13L), testTaco(14L)
        };
        Flux<Taco> tacoFlux = Flux.just(tacos);
        TacoRepository tacoRepository = Mockito.mock(TacoRepository.class);
        when(tacoRepository.findAll()).thenReturn(tacoFlux);
        WebTestClient webTestClient = WebTestClient.bindToController(new TacoController(tacoRepository)).build();

        webTestClient.get().uri("/api/tacos?recent")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$").isNotEmpty()
                .jsonPath("$[0].id").isEqualTo(tacos[0].getId().toString())
                .jsonPath("$[0].name").isEqualTo("Taco 1")
                .jsonPath("$[1].id").isEqualTo(tacos[1].getId().toString())
                .jsonPath("$[1].name").isEqualTo("Taco 2")
                .jsonPath("$[11].id").isEqualTo(tacos[11].getId().toString())
                .jsonPath("$[11].name").isEqualTo("Taco 12")
                .jsonPath("$[12]").doesNotExist();
    }

    @Test
    public void shouldSaveTaco(){
        TacoRepository tacoRepository = Mockito.mock(TacoRepository.class);
        WebTestClient webTestClient = WebTestClient.bindToController(new TacoController(tacoRepository)).build();
        Mono<Taco> unsavedTacoMono = Mono.just(testTaco(1L));
        Taco savedTaco = testTaco(1L);
        Flux<Taco> savedTacoMono = Flux.just(savedTaco);
        when(tacoRepository.saveAll(any(Mono.class))).thenReturn(savedTacoMono);
        webTestClient.post().uri("/api/tacos")
                .contentType(MediaType.APPLICATION_JSON)
                .body(unsavedTacoMono, Taco.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Taco.class)
                .isEqualTo(savedTaco);

    }


    private Taco testTaco(Long number){
        Taco taco = new Taco();
        taco.setId(number != null ? number : 0);
        taco.setName("Taco " + number);
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("INGA", "Ingredient A", Ingredient.Type.WRAP));
        ingredients.add(new Ingredient("INGB", "Ingredient B", Ingredient.Type.PROTEIN));
        ingredients.add(new Ingredient("INGC", "Ingredient C", Ingredient.Type.CHEESE));
        ingredients.add(new Ingredient("INGD", "Ingredient D", Ingredient.Type.SAUCE));
        taco.setIngredients(ingredients);
        return taco;

    }
}
