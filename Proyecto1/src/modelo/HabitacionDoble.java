/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author gipsy
 */
public class HabitacionDoble extends Habitacion{
    public HabitacionDoble(String numero){
        super(numero);
    }
    
    @Override
    public String getTipo(){
        return "Doble";
    }
    @Override
    public int getCapacidad(){
        return 2;
    }
    @Override
    public long getTarifa(){
        return 65000L;
    }
    
}
