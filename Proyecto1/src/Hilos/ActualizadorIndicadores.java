/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Hilos;

import java.util.function.Consumer;
import modelo.GestorHotel;
import modelo.IndicadoresHotel;

/**
 *
 * @author Diana
 */
public class ActualizadorIndicadores implements Runnable {

    private final GestorHotel gestor;

    private final Consumer<IndicadoresHotel> alActualizar;

    private final Runnable alFinalizar;

    public ActualizadorIndicadores(GestorHotel gestor, Consumer<IndicadoresHotel> alActualizar, Runnable alFinalizar) {
        this.gestor = gestor;
        this.alActualizar = alActualizar;
        this.alFinalizar = alFinalizar;
    }

   @Override
public void run() {

    String nombre =
            Thread.currentThread().getName();

    System.out.println(
            "[" + nombre + "] Inicio");

    try {

        for (int i = 1; i <= 5; i++) {

            IndicadoresHotel datos =
                    gestor.calcularIndicadores();

            System.out.println(
                    "[" + nombre + "] Ciclo "
                    + i);

            alActualizar.accept(datos);

            if (i < 5) {
                Thread.sleep(1000);
            }
        }

    } catch (InterruptedException ex) {

        Thread.currentThread().interrupt();

        System.out.println(
                "[" + nombre
                + "] Interrumpido");

    } finally {

        System.out.println(
                "[" + nombre
                + "] Finalizado");

        alFinalizar.run();
    }
}
}
