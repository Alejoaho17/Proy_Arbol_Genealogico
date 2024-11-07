/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proy_2_arb_gen;

/**
 *
 * @author Alejo_Samuel
 */
public class Persona {

    private String nombre;
    private String ofHisName;
    private String bornTo;
    private String knownThroughoutAs;
    private String heldTitle;
    private String wedTo;
    private String ofEyes;
    private String ofHair;
    private String[] fatherTo;
    private String notes;
    private String fate;

    public Persona(String nombre, String ofHisName, String bornTo, String knownThroughoutAs, String heldTitle, String wedTo, String ofEyes, String ofHair, String[] fatherTo, String notes, String fate) {
        this.nombre = nombre;
        this.ofHisName = ofHisName;
        this.bornTo = bornTo;
        this.knownThroughoutAs = knownThroughoutAs;
        this.heldTitle = heldTitle;
        this.wedTo = wedTo;
        this.ofEyes = ofEyes;
        this.ofHair = ofHair;
        this.fatherTo = fatherTo;
        this.notes = notes;
        this.fate = fate;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the ofHisName
     */
    public String getOfHisName() {
        return ofHisName;
    }

    /**
     * @param ofHisName the ofHisName to set
     */
    public void setOfHisName(String ofHisName) {
        this.ofHisName = ofHisName;
    }

    /**
     * @return the bornTo
     */
    public String getBornTo() {
        return bornTo;
    }

    /**
     * @param bornTo the bornTo to set
     */
    public void setBornTo(String bornTo) {
        this.bornTo = bornTo;
    }

    /**
     * @return the knownThroughoutAs
     */
    public String getKnownThroughoutAs() {
        return knownThroughoutAs;
    }

    /**
     * @param knownThroughoutAs the knownThroughoutAs to set
     */
    public void setKnownThroughoutAs(String knownThroughoutAs) {
        this.knownThroughoutAs = knownThroughoutAs;
    }

    /**
     * @return the heldTitle
     */
    public String getHeldTitle() {
        return heldTitle;
    }

    /**
     * @param heldTitle the heldTitle to set
     */
    public void setHeldTitle(String heldTitle) {
        this.heldTitle = heldTitle;
    }

    /**
     * @return the wedTo
     */
    public String getWedTo() {
        return wedTo;
    }

    /**
     * @param wedTo the wedTo to set
     */
    public void setWedTo(String wedTo) {
        this.wedTo = wedTo;
    }

    /**
     * @return the ofEyes
     */
    public String getOfEyes() {
        return ofEyes;
    }

    /**
     * @param ofEyes the ofEyes to set
     */
    public void setOfEyes(String ofEyes) {
        this.ofEyes = ofEyes;
    }

    /**
     * @return the ofHair
     */
    public String getOfHair() {
        return ofHair;
    }

    /**
     * @param ofHair the ofHair to set
     */
    public void setOfHair(String ofHair) {
        this.ofHair = ofHair;
    }

    /**
     * @return the fatherTo
     */
    public String[] getFatherTo() {
        return fatherTo;
    }

    /**
     * @param fatherTo the fatherTo to set
     */
    public void setFatherTo(String[] fatherTo) {
        this.fatherTo = fatherTo;
    }

    /**
     * @return the notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * @param notes the notes to set
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * @return the fate
     */
    public String getFate() {
        return fate;
    }

    /**
     * @param fate the fate to set
     */
    public void setFate(String fate) {
        this.fate = fate;
    }

    //Metodo para imprimir
    @Override
    public String toString() {
        String sons = "";
        for (int i = 0; i < this.fatherTo.length; i++) {
            if (sons.equals("")) {
                sons += this.fatherTo[i];
            } else 
                sons +=  ", " + this.fatherTo[i];
            
        }
        return "Name: " + this.nombre + "\nOf his name: " + this.ofHisName + "\nBorn to: " + this.bornTo + "\nKnow throughtout as: " + this.knownThroughoutAs + "\nHeld title: " + this.heldTitle + "\nWed to: " + this.wedTo + "\nOf eyes: " + this.ofEyes + "\nOf hair: " + this.ofHair + "\nFather to: " + sons + "\nNotes: " + this.notes + "\nFate: " + this.fate;
    }

}
