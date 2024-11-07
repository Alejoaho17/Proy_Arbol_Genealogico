/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proy_2_arb_gen;

/**
 *
 * @author Alejo_Samuel
 */
public class General_Tree <T> {
    
    private Nodo<T> root;
    private String house;

    public General_Tree(String house) {
        this.house = house;
    }

    /**
     * @return the root
     */
    public Nodo<T> getRoot() {
        return root;
    }

    /**
     * @param root the root to set
     */
    public void setRoot(Nodo<T> root) {
        this.root = root;
    }

    /**
     * @return the house
     */
    public String getHouse() {
        return house;
    }

    /**
     * @param house the house to set
     */
    public void setHouse(String house) {
        this.house = house;
    }
    
    public boolean isEmpty () {
        return this.root == null;
    }
    
    public void addNode (T dato) {
        Nodo<T> nuevoNodo = new Nodo (dato); 
        
        if (this.isEmpty()) {
            this.root = nuevoNodo;
        } else {
            
        }
        
    }
    
}
