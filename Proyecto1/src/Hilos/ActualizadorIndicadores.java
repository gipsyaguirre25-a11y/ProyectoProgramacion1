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

        try {
            for (int i = 1; i < 5; i++) {
                Thread.sleep(1000);

                IndicadoresHotel datos = gestor.calcularIndicadores();

                System.out.println("Actualizar de indicadores - Ciclo " + i);
                System.out.println("Habitaciones totales: " + datos.totalHabitaciones);
                System.out.println("Actualizar de indicadores - Ciclo " + datos.habitacionesOcupadas);
                System.out.println("Actualizar de indicadores - Ciclo " + datos.reservasConfirmadas);
                System.out.println("Actualizar de indicadores - Ciclo " + datos.reservasVencidas);
                System.out.println("Ingresos esperados: ₡ " + datos.ingresosEsperados);

                alActualizar.accept(datos);

            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();

            System.out.println(
                    "Actualizador de indicadores interrumpido.");

        } finally {

            alFinalizar.run();
        }
    }

}
