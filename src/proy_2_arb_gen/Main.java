/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package proy_2_arb_gen;

/**
 *
 * @author Alejo_Samuel
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String[] fatherTo = new String[]{"Aenys", "Maegor", "Baelon"};
        Persona persona1 = new Persona("Aegon Targaryen", "First", "[Unknown]", "Aegon the Conqueror", "King of the Andals and the First Men, Lord of the Seven Kingdoms", "", "Purple", "Silver", fatherTo, "Founder of House Targaryen", "");
        
        System.out.println(persona1);
        
        General_Tree<Persona> tree = new General_Tree("Casa 1");
        tree.addNode(persona1);       
        System.out.println();
        
    }
    
}
//