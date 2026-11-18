/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Hilos;

import java.time.LocalDate;
import modelo.GestorHotel;

/**
 *
 * @author gipsy
 */
public class SupervisorReservas implements Runnable{
    
    private final GestorHotel gestor;
    private final Runnable finalizar;
    
    public SupervisorReservas (
    GestorHotel gestor, Runnable finalizar) {
        
        this.gestor = gestor;
        this.finalizar = finalizar;
        
    }
    
    @Override 
    public void run(){
        String nombre = Thread.currentThread().getName();
        
        System.out.println ("[" + nombre + "] Inicio de supervision");
        
        try {
            for (int i = 1; i<= 5; i++) {
                
                int actualizadas = gestor.actualizarVencidas(LocalDate.now());
                
                System.out.println("[" + nombre + "] Ciclo " + i +   "-Reserva vencidad actuazalidas" + actualizadas);
                
                if (i < 5) {
                    Thread.sleep(1000);
                }
            }
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
            System.out.println("[" + nombre + "] Hilo interrumpido");
        } finally {
            System.out.println ( "[" + nombre +"] finalizado");
            
            finalizar.run();
        }
      
    }
}
