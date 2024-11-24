/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proy_2_arb_gen;

interface ITree<T> {
    public boolean vacio();

    public int numElementos();

    public int altura();

    public T getValor();

    public void setValor(T valor);

    public ITree<T> getRaiz();

    public boolean agregarHijo(ITree<T> hijo);

    public LinkedList<ITree<T>> getHijos();

    public ITree<T> getHijo(int i);

    public void setComparador(IComparador<T> comparador);

    public LinkedList<ITree<T>> buscarHijo(T valor);

    public LinkedList<ITree<T>> buscar(T valor);

    public ITree<T> getPadre(ITree<T> tree);

    public LinkedList<ITree<T>> getAscendentes(ITree<T> tree);

    public LinkedList<ITree<T>> getDescendientes(ITree<T> tree);

    public LinkedList<ITree<T>>[] getNiveles();

    public LinkedList<ITree<T>> getNivel(int nivel);

    public int getNumNiveL(ITree<T> tree);

    public void vaciar();

    public String nivelesToString();

    @Override
    public String toString();
}
