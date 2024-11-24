/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package proy_2_arb_gen;

public class Proy_2_Arb_Gen {
    
    

    public static final String DefaultDirectory = "./data/";
    

    AppController appController;

    public Proy_2_Arb_Gen() {
        this.appController = new AppController();
        VentanaPrincipal ventanaPrincipal = new VentanaPrincipal(this);
        ventanaPrincipal.setVisible(true);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new Proy_2_Arb_Gen();
    }

}
