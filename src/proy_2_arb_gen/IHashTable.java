/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proy_2_arb_gen;

interface IHashTable<T> {

    int getTam();

    void setTam(int tam);

    boolean insertar(String clave, T valor);

    boolean insertar(String clave);

    T buscar(String clave);

    LinkedList<T> buscarTodos(String clave);

    String toString();

}
