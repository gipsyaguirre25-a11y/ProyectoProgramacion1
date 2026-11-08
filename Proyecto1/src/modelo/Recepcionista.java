/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author gipsy
 */
public class Recepcionista {

    private final String Usuario;
    private final String Password;

    public Recepcionista(String Usuario, String Password) {
        this.Usuario = Usuario;
        this.Password = Password;
    }

    public String getUsuario() {
        return Usuario;
    }

    public boolean validarPassword(String Password) {
        return this.Password.equals(Password);
    }

    @Override
    public String toString() {
        return Usuario;
    }

}
