package Java101.reactiveJava;


import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.List;
import java.util.Locale;

public class Reactive {
    List<String> programingLanguage = List.of("Java", "Dart", "Python", "Kotlin", "Javascript", "Typescript");

    // For Single data use Mono Publisher
    private Mono<String> greet() {
        return Mono.just("Hello Ayman");
    }

    // For Multiple data use Flux Publisher
    private Flux<String> getLanguages() {
        return Flux.fromIterable(programingLanguage);
    }

    // Map return element
    private Flux<String> testMap() {
        Flux<String> fluxStrings = getLanguages();
        return fluxStrings.map(s -> s.toUpperCase(Locale.ROOT));
    }

    // FlatMap return Publisher of element
    private Flux<String> testFlatMap() {
        Flux<String> fluxStrings = getLanguages();
        return fluxStrings.flatMap(s -> Mono.just(s.toLowerCase(Locale.ROOT)));
    }

    // This will skip some element by index or something else ...
    private Flux<String> testSkip() {
        Flux<String> fluxStrings = getLanguages();
        return fluxStrings.skip(2).flatMap(s -> Mono.just(s.toUpperCase()));
    }


    private Flux<String> testSkipWithDelay() {
        Flux<String> fluxStrings = getLanguages();
        return fluxStrings
                .delayElements(Duration.ofMillis(500))
                .skip(2)
                .flatMap(s -> Mono.just(s.toUpperCase()));
    }

    private Flux<Integer> testComplexSkip() {
        Flux<Integer> fluxStrings = Flux.range(1, 20);
        return fluxStrings
                .delayElements(Duration.ofMillis(300))
                .skipWhile(e -> e < 10)
                .flatMap(Mono::just);
    }

    private Flux<Integer> testConcat() {
        Flux<Integer> flux1 = Flux.range(1, 20);
        Flux<Integer> flux2 = Flux.range(30, 40);

        return Flux.concat(flux1, flux2);
    }

    private Flux<Integer> testMerge() {
        Flux<Integer> flux1 = Flux.range(1, 20).delayElements(Duration.ofMillis(500));
        Flux<Integer> flux2 = Flux.range(30, 40).delayElements(Duration.ofMillis(500));

        return Flux.merge(flux1, flux2);
    }

              // Tuple3<Integer, Integer, Integer>
    private Flux<Tuple2<Integer, Integer>> testZip() {
        Flux<Integer> flux1 = Flux.range(1, 20).delayElements(Duration.ofMillis(300));
        Flux<Integer> flux2 = Flux.range(30, 40).delayElements(Duration.ofMillis(300));

        return Flux.zip(flux1, flux2);
    }


    public static void main(String[] args) throws InterruptedException {
        Reactive reactive = new Reactive();

//        reactive.greet().subscribe(System.out::println);
//        reactive.getLanguages().subscribe(data -> System.out.println(data));
//        reactive.testMap().subscribe(System.out::println);
//        reactive.testFlatMap().subscribe(data -> System.out.println(data));
//        reactive.testSkip().subscribe(data -> System.out.println(data));

//        reactive.testSkipWithDelay().subscribe(data -> System.out.println(data));
//        reactive.testComplexSkip().subscribe(data -> System.out.println(data));

//        reactive.testConcat().subscribe(System.out::println);

//        reactive.testMerge().subscribe(System.out::println);
        reactive.getLanguages().subscribe(System.out::println);
        Thread.sleep(8000);

    }
}
