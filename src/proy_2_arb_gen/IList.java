/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proy_2_arb_gen;

/**
 * Interfaz lista de elementos de tipo genérico
 * 
 */
interface IList<T> {
    /**
     * Método que retorna el tamaño de la lista
     * 
     * @return el tamaño de la lista
     */
    int size();

    /**
     * Método que indica si la lista esta vacia
     * 
     * @return true si la lista esta vacia, false en caso contrario
     */
    boolean vacia();

    /**
     * Método que inserta un elemento en la lista
     * 
     * @param index el indice en la lista donde insertar el elemento
     * @param valor el valor a insertar
     * @return true si se pudo insertar, false en caso contrario
     */
    boolean insertar(int index, T valor);

    /**
     * Método que agrega un elemento a la lista
     * 
     * @param valor el valor a agregar
     * @return true si se pudo agregar, false en caso contrario
     */
    boolean agregar(T valor);

    /**
     * Método que agrega un array de elementos a la lista
     * 
     * @param valor el array a agregar
     */
    void agregar(T[] valor);

    /**
     * Método que agrega una lista de elementos a la lista
     * 
     * @param valor la lista a agregar
     */
    void agregar(IList<T> valor);

    /**
     * Método que retorna un elemento de la lista
     * 
     * @param index el indice del elemento
     * @return el valor
     */
    T get(int index);

    /**
     * Método que modifica un elemento de la lista
     * 
     * @param index el indice del elemento
     * @param valor el nuevo valor
     * @return true si se pudo modificar, false en caso contrario
     */
    boolean set(int index, T valor);

    /**
     * Método que elimina un elemento de la lista y lo retorna
     * 
     * @param index el indice del elemento
     * @return el valor que se elimino
     */
    T remover(int index);

    /**
     * Método que vacía la lista
     */
    void vaciar();

    /**
     * Método que copia la lista y retorna la copia
     * 
     * @return la copia
     */
    IList<T> copiar();

    /**
     * Método que permite saber si dos listas son iguales usando un comparador.
     * 
     * @param list       la lista a comparar
     * @param comparador expresión lambda con el comparador
     * 
     * @return true si son iguales, false en caso contrario
     */
    public boolean equals(IList<T> list, IComparador<T> comparador);

    /**
     * Método que permite saber si dos listas son iguales sin comparador.
     * Compara si los elementos de la lista son Strings o Numbers.
     * Si son de otro tipo devuelve false. Usar equals(IList, IComparador)
     * para estos casos. @see equals(IList, IComparador)
     * 
     * @param list la lista a comparar
     * @return
     */
    public boolean equals(IList<T> list);

    /**
     * Método que ordena la lista de menor a mayor, usando un comparador.
     * 
     * @param comparador expresión lambda con el comparador @see IComparador
     */
    public void ordenar(IComparador<T> comparador);

    /**
     * Método que ordena la lista de menor a mayor, sin usar un comparador.
     * Compara si los elementos de la lista son Strings o Numbers.
     * Si son de otro tipo no ordena la lista. Usar ordenar(IList, IComparador)
     * para estos casos. @see ordenar(IList, IComparador)
     *
     */
    public void ordenar();

    /**
     * Método que invierte la lista
     * 
     */
    public void invertir();

    /**
     * Método que retorna el indice de un elemento de la lista, si está presente
     * o -1 en caso contrario. Utiliza un comparador.
     * 
     * @param valor      el valor a buscar
     * @param comparador expresión lambda con el comparador @see IComparador
     * 
     * @return el indice o -1
     */

    public int buscar(T valor, IComparador<T> comparador);

    /**
     * Método que retorna el indice de un elemento de la lista, si está presente
     * o -1 en caso contrario. No utiliza un comparador. Solo funciona para
     * elementos de tipo Number o String. Si son de otro tipo devuelve -1. Usar
     * buscar(IList, IComparador) para estos casos. @see buscar(IList, IComparador)
     * 
     * @param valor el valor a buscar
     * @return el indice o -1
     */
    public int buscar(T valor);

    /**
     * Método que retorna un string con la representación de la lista.
     * 
     * @return la representación
     */
    public String toString();
}
