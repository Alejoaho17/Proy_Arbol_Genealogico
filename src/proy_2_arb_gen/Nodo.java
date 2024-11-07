/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proy_2_arb_gen;

/**
 *
 * @author Alejo_Samuel
 */
public class Nodo <T> { //Se utiliza para arbol general
    private Nodo<T> leftSon;
    private Nodo<T> brother;
    
    private T dato; //Permite generalizacion de tipo de dato

    public Nodo(T dato) {
        this.dato = dato;
    }

    /**
     * @return the leftSon
     */
    public Nodo<T> getLeftSon() {
        return leftSon;
    }

    /**
     * @param leftSon the leftSon to set
     */
    public void setLeftSon(Nodo<T> leftSon) {
        this.leftSon = leftSon;
    }

    /**
     * @return the brother
     */
    public Nodo<T> getBrother() {
        return brother;
    }

    /**
     * @param brother the brother to set
     */
    public void setBrother(Nodo<T> brother) {
        this.brother = brother;
    }

    /**
     * @return the dato
     */
    public T getDato() {
        return dato;
    }

    /**
     * @param dato the dato to set
     */
    public void setDato(T dato) {
        this.dato = dato;
    }
    
    
    
    
    
      
}
