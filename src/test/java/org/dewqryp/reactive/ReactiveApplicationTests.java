package org.dewqryp.reactive;

import jdk.jfr.DataAmount;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class ReactiveApplicationTests {

    @Test
    void contextLoads() {
    }
    @Test
    public void createAFlux_just(){
        Flux<String> fruitFlux = Flux.just("apple","orange","banana", "grape");
        fruitFlux.subscribe(System.out::println);
        StepVerifier.create(fruitFlux)
                .expectNext("apple","orange","banana","grape").verifyComplete();
    }
    @Test
    public void createAFlux_fromArray(){
        String[] fruitArray = {"apple","orange","banana","grape"};
        Flux<String> fruitFlux = Flux.fromArray(fruitArray);
        StepVerifier.create(fruitFlux)
                .expectNext("apple","orange","banana","grape").verifyComplete();
    }
    @Test
    public void createAFlux_fromIterable(){
        List<String> fruitList = Arrays.asList("apple","orange","banana","grape");
        Flux<String> fruitFlux = Flux.fromIterable(fruitList);
        StepVerifier.create(fruitFlux)
                .expectNext("apple","orange","banana","grape").verifyComplete();

    }
    @Test
    public void createAFlux_range(){
        Flux<Integer> integerFlux = Flux.range(1,5);
        StepVerifier.create(integerFlux)
                .expectNext(1)
                .expectNext(2).
                expectNext(3).
                expectNext(4).
                expectNext(5).verifyComplete();

    }
    @Test
    public void mergeFluxes(){
        Flux<String> characterFlux = Flux.just("Garfield", "Kojak", "Barbossa")
                .delayElements(Duration.ofMillis(500));
        Flux<String> foodFlux = Flux.just("Lasagne", "Lollipop", "Apples")
                .delaySubscription(Duration.ofMillis(200))
                .delayElements(Duration.ofMillis(500));

        Flux<String> mergedFlux = characterFlux.mergeWith(foodFlux);
        StepVerifier.create(mergedFlux)
                .expectNext("Garfield", "Lasagne", "Kojak", "Lollipop", "Barbossa","Apples")
                .verifyComplete();
    }
    @Test
    public void zipFluxes(){
        Flux<String> characterFlux = Flux.just("Garfield", "Kojak", "Barbossa");
        Flux<String> foodFlux = Flux.just("Lasagne", "Lollipop", "Apples");

        Flux<Tuple2<String, String>> zipFlux = characterFlux.zipWith(foodFlux);
        StepVerifier.create(zipFlux)
                .expectNextMatches(p -> p.getT1().equals("Garfield") && p.getT2().equals("Lasagne"))
                .expectNextMatches(p -> p.getT1().equals("Kojak") && p.getT2().equals("Lollipop"))
                .expectNextMatches(p -> p.getT1().equals("Barbossa") && p.getT2().equals("Apples"))
                .verifyComplete();
    }
    @Test
    public void zipFluxesCustom(){
        Flux<String> characterFlux = Flux.just("Garfield", "Kojak", "Barbossa");
        Flux<String> foodFlux = Flux.just("Lasagne", "Lollipop", "Apples");

        Flux<String> zipFlux = Flux.zip(characterFlux, foodFlux, (c, f) -> c + " eats " + f);
        StepVerifier.create(zipFlux)
                .expectNext("Garfield eats Lasagne",  "Kojak eats Lollipop", "Barbossa eats Apples")
                .verifyComplete();
    }

    @Test
    public void firstWithSignalFlux(){
        Flux<String> slow = Flux.just("tortoise", "snail", "sloth")
                .delayElements(Duration.ofMillis(100));
        Flux<String> fast = Flux.just("cheetah", "tiger", "t-rex");

        Flux<String> firstWithSignalFlux = Flux.firstWithSignal(slow, fast);

        StepVerifier.create(firstWithSignalFlux)
                .expectNext("cheetah", "tiger", "t-rex").verifyComplete();
    }

    @Test
    public void skipFlux(){
        Flux<String> integers = Flux.just("one", "two", "skips", "ninety nine", "hundred")
                .skip(3);
        StepVerifier.create(integers)
                .expectNext("ninety nine", "hundred").verifyComplete();

    }

    @Test
    public void skipFluxDuration(){
        Flux<String> integers = Flux.just("one", "two", "skips", "ninety nine", "hundred")
                .delayElements(Duration.ofSeconds(1))
                .skip(Duration.ofSeconds(4));
        StepVerifier.create(integers)
                .expectNext("ninety nine", "hundred").verifyComplete();

    }

    @Test
    public void takeFluxDuration(){
        Flux<String> integers = Flux.just("one", "two", "skips", "ninety nine", "hundred")
                .delayElements(Duration.ofSeconds(1))
                .take(Duration.ofSeconds(4));
        StepVerifier.create(integers)
                .expectNext("one", "two", "skips").verifyComplete();

    }

    @Test
    public void filterFlux(){
        Flux<Integer> integers = Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .filter(i -> i % 2 == 0);
        StepVerifier.create(integers)
                .expectNext(2, 4, 6, 8, 10).verifyComplete();
    }

    @Test
    public void map(){
        Flux<Player> players = Flux.just("Michael Jordan", "Lebron James", "Steph Kurry")
                .map(player -> {
                    String[] playersArray = player.split("\\s");
                    return new Player(playersArray[0], playersArray[1]);
                });

        StepVerifier.create(players)
                .expectNext(new Player("Michael", "Jordan"), new Player("Lebron", "James"), new Player("Steph", "Kurry"))
                .verifyComplete();

    }
    @Test
    public void flatMap(){
        Flux<Player> players = Flux.just("Michael Jordan", "Lebron James", "Steph Kurry")
                .flatMap(Mono::just)
                .map(player -> {
                    String[] playersArray = player.split("\\s");
                    return new Player(playersArray[0], playersArray[1]);
                })
                .subscribeOn(Schedulers.parallel());
        List<Player> playersList = List.of(new Player("Michael", "Jordan"), new Player("Lebron", "James"),  new Player("Steph", "Kurry"));
        StepVerifier.create(players)
                .expectNextMatches(playersList::contains)
                .expectNextMatches(playersList::contains)
                .expectNextMatches(playersList::contains)
                .verifyComplete();


    }

    @Test
    public void buffer(){
        Flux<String> strings = Flux.just("apple", "orange", "banana", "grape", "watermelon");
        Flux<List<String>> bufferFlux = strings.buffer(3);
        StepVerifier.create(bufferFlux)
                .expectNext(List.of("apple", "orange", "banana"))
                .expectNext(List.of("grape", "watermelon"))
                .verifyComplete();
    }

    @Test
    public void bufferAndLog(){
        Flux.just("apple", "orange", "banana", "grape", "watermelon")
                .buffer(3)
                .flatMap(x ->
                    Flux.fromIterable(x).map(String::toUpperCase)
                            .subscribeOn(Schedulers.parallel())
                            .log())
                .subscribe();
    }


    @Data
    private static class Player {
        private final String name;
        private final String surname;
    }




}
