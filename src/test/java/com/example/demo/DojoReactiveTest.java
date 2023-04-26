package com.example.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import java.util.Comparator;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;



public class DojoReactiveTest {

    @Test
    void converterData(){
        var flux = Flux.fromIterable(CsvUtilFile.getPlayers());
        //long count = flux.count().block();
        //Assertions.assertEquals(18207, count, "El número de elementos en el flujo no es el esperado");
    }



    @Test
    void jugadoresMayoresA35SegunClub(){
        var flux = Flux.fromIterable(CsvUtilFile.getPlayers());
        flux.filter(player -> player.getAge() >= 35)
                        .collect(Collectors.groupingBy(
                                Player::getClub,
                                Collectors.mapping(Player::getName, Collectors.toList())
                        )).subscribe(System.out::println);

    }

    @Test
    void mejorJugadorConNacionalidadFrancia() {
        var flux = Flux.fromIterable(CsvUtilFile.getPlayers());
        flux.filter(player1 -> "France".equals(player1.getNational()))
                .collect(Collectors.maxBy(Comparator.comparingDouble(
                        player1 -> ((double) player1.getWinners() / player1.getGames())
                ))).subscribe(System.out::println);

    }

    @Test
    void mejorJugadorConNacionalidadFrancia1() {
        var flux = Flux.fromIterable(CsvUtilFile.getPlayers());
        flux.filter(player -> "France".equals(player.getNational()))
                .reduce(BinaryOperator.maxBy(Comparator.comparingDouble(
                        player1 -> ((double) player1.getWinners() / player1.getGames())
                ))).subscribe(System.out::println);

    }

    @Test
    void clubsAgrupadosPorNacionalidad(){
        var flux = Flux.fromIterable(CsvUtilFile.getPlayers());
        flux.collect(Collectors.groupingBy(
                Player::getNational,
                Collectors.mapping(Player::getClub, Collectors.toList())
        )).subscribe(System.out::println);
    }

    @Test
    void clubsAgrupadosPorNacionalidad1(){
        var flux = Flux.fromIterable(CsvUtilFile.getPlayers());
        flux.collectMultimap(Player::getNational, Player::getClub)
                .subscribe(System.out::println);
    }

    @Test
    void clubConElMejorJugador(){
        var flux = Flux.fromIterable(CsvUtilFile.getPlayers());
        flux.collect(Collectors.maxBy(Comparator.comparingDouble(
                player1 -> ((double) player1.getWinners() / player1.getGames()))
        )).map(player -> player.get().getClub()).subscribe(club -> System.out.println(club));
    }

    @Test
    void clubConElMejorJugador1() {
        var flux = Flux.fromIterable(CsvUtilFile.getPlayers());
        flux.reduce(BinaryOperator.maxBy(Comparator.comparingDouble(
                        player -> player.getWinners() / player.getGames())))
                .map(player -> player.getClub()).subscribe(club -> System.out.println(club));
    }

    @Test
    void mejorJugadorSegunNacionalidad(){
        var flux = Flux.fromIterable(CsvUtilFile.getPlayers());
        flux.collect(Collectors.groupingBy(
                Player::getNational,
                Collectors.collectingAndThen(
                        Collectors.maxBy(
                                Comparator.comparingDouble(
                                        player -> ((double) player.getWinners() / player.getGames()))),
                        player -> player.get().getName())
        )).subscribe(player -> System.out.println(player));
    }

    @Test
    void mejorJugadorSegunNacionalidad1(){
        var flux = Flux.fromIterable(CsvUtilFile.getPlayers());
        flux.collect(Collectors.groupingBy(Player::getNational, Collectors.collectingAndThen(Collectors.reducing(BinaryOperator.maxBy(Comparator.comparingDouble(
                player -> ((double) player.getWinners() / player.getGames())))), player -> player.get().getName()))
        ).subscribe(System.out::println);
    }
}
