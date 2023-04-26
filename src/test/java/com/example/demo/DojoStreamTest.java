package com.example.demo;


import org.junit.jupiter.api.Test;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class DojoStreamTest {

    @Test
    void converterData(){
        List<Player> list = CsvUtilFile.getPlayers();
        //
    }

    @Test
    void jugadoresMayoresA35SegunClub(){
        List<Player> list = CsvUtilFile.getPlayers();
         Map<String, List<String>> jugadoresPorClub = list.parallelStream()
                 .collect(Collectors.groupingBy(
                         Player::getClub,
                                 Collectors.collectingAndThen(
                                 Collectors.filtering(player -> player.age >= 35, Collectors.toList()),
                                         players -> players.stream().map(player -> player.getName()).collect(Collectors.toList()))
                 ));
         System.out.println(jugadoresPorClub);
    }

    @Test
    void mejorJugadorConNacionalidadFrancia(){
        List<Player> list = CsvUtilFile.getPlayers();
        var player1 = list.stream()
                .filter(player -> player.getNational().equals("France"))
                .max(Comparator.comparingDouble(
                                player -> ((double) player.getWinners() / player.getGames())
                        )
                )
                .orElse(null);
        System.out.println(player1);
    }


    @Test
    void clubsAgrupadosPorNacionalidad(){
        List<Player> jugadores = CsvUtilFile.getPlayers();
        Map<String, List<String>> clubsPorNacionalidad = jugadores.stream()
                .collect(Collectors.groupingBy(
                        Player::getNational,
                        Collectors.mapping(Player::getClub, Collectors.toList())
                ));
        System.out.println(clubsPorNacionalidad);
    }

    @Test
    void clubConElMejorJugador(){
        List<Player> jugadores = CsvUtilFile.getPlayers();
        String club = jugadores.stream()
                .max(Comparator.comparingDouble(
                        player -> ((double) player.getWinners() / player.getGames())
                )).map(player -> player.getClub())
                .orElse(null);
        System.out.println(club);
    }


    @Test
    void mejorJugadorSegunNacionalidad(){
        List<Player> jugadores = CsvUtilFile.getPlayers();
        Map<String, String> mejorJugadorNacionalidad = jugadores.stream()
                .collect(Collectors.groupingBy(
                        Player::getNational,
                        Collectors.collectingAndThen(
                                Collectors.maxBy(
                                        Comparator.comparingDouble(
                                                player -> ((double) player.getWinners() / player.getGames())
                                        )
                                ),
                                player -> player.get().getName()
                        )
                ));

        System.out.println(mejorJugadorNacionalidad);
    }


}
