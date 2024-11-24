    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proy_2_arb_gen;

/**
 * Estructura de árbol general para representar una estructura o árbol
 * genealógico.
 * Utiliza tipos de datos genéricos para guardar los valores de cada nodo.
 * 
 * Es una definición recursiva donde cada nodo es un árbol en si mismo,
 * es decir cada hijo es un sub-árbol.
 */

public class Tree<T> implements ITree<T> {
    /**
     * Comparador por defecto para Strings. usado en guessComparador.
     * 
     * @see guessComparador
     */
    private static IComparador<String> comparadorString = (String a, String b) -> a.compareTo(b);

    /**
     * Comparador por defecto para Numbers. usado en guessComparador.
     * 
     * @see guessComparador
     */

    private static IComparador<Number> comparadorNumber = (Number a, Number b) -> {
        Double num1 = a.doubleValue();
        Double num2 = b.doubleValue();
        return num1.compareTo(num2);
    };

    /**
     * Tipo genérico para guardar el valor de cada nodo.
     */

    private T valor;

    /**
     * Lista para guardar los hijos de cada nodo. Tienen valores de tipo genérico.
     */

    private LinkedList<ITree<T>> hijos;

    /**
     * Comparador para hacer búsquedas de los hijos de cada nodo. Debe ser definido
     * para cada tipo de datos. Si el tipo de datos es String o Number, se asigna un
     * comparador por defecto, gracias a la función guessComparador.
     * 
     * @see guessComparador
     */
    private IComparador<T> comparador;

    /**
     * Si los tipos de datos guardados en el árbol son String o Number, se
     * asigna un comparador genérico, de forma que no se tiene que estar definiendo
     * comparadores para ellos. Muy util en las pruebas de uso de la clase.
     */
    @SuppressWarnings("unchecked")
    private void guessComparador() {
        if (comparador != null) {
            return;
        }
        if (this.valor == null) {
            return;
        }
        if (this.valor instanceof String) {
            this.comparador = (IComparador<T>) comparadorString;
            return;
        }
        if (this.valor instanceof Number) {
            this.comparador = (IComparador<T>) comparadorNumber;
            return;
        }
    }

    /**
     * Constructor de Tree.
     */
    public Tree() {
        this.hijos = new LinkedList<>();
    }

    /**
     * Constructor de Tree cuando se le da un valor.
     * 
     * @param valor el valor del nodo
     * 
     */
    public Tree(T valor) {
        this.valor = valor;
        this.hijos = new LinkedList<>();
        this.guessComparador();
    }

    /**
     * Constructor de Tree cuando se le da un valor y un comparador.
     * 
     * @param valor      el valor del nodo
     * @param comparador el comparador
     */

    public Tree(T valor, IComparador<T> comparador) {
        this.valor = valor;
        this.hijos = new LinkedList<>();
        this.comparador = comparador;
    }

    /**
     * Devuelve true si el arbol esta vacio.
     * 
     * @return true si el arbol esta vacio, false en caso contrario
     */

    @Override
    public boolean vacio() {
        if (this.valor == null && this.hijos.vacia()) {
            return true;
        }
        return false;
    }

    /**
     * Devuelve el numero de elementos del arbol.
     * 
     * @return el numero de elementos del arbol
     */

    @Override
    public int numElementos() {
        if (this.hijos.vacia()) {
            if (this.vacio()) {
                return 0;
            }
            return 1;
        }
        int sumElementos = 0;
        for (int i = 0; i < this.hijos.size(); i++) {
            sumElementos += this.hijos.get(i).numElementos();
        }
        if (this.valor == null) {
            return sumElementos;
        }
        return sumElementos + 1;
    }

    /**
     * Devuelve el valor del nodo.
     * 
     * @return el valor del nodo
     */

    @Override
    public T getValor() {
        return this.valor;
    }

    /**
     * Modifica el valor del nodo.
     * 
     * @param valor el nuevo valor del nodo
     */

    @Override
    public void setValor(T valor) {
        this.valor = valor;
        this.guessComparador();
    }

    /**
     * Devuelve la raiz del arbol.
     * 
     * @return la raiz del arbol
     */

    @Override
    public Tree<T> getRaiz() {
        return this;
    }

    /**
     * Agrega un nuevo hijo al arbol.
     * 
     * @param hijo el nuevo hijo
     * @return true si se agrego el nuevo hijo, false en caso contrario
     */

    @Override
    public boolean agregarHijo(ITree<T> hijo) {
        if (hijo == null) {
            return false;
        }
        return this.hijos.agregar(hijo);
    }

    /**
     * Devuelve la lista de hijos del arbol.
     * 
     * @return la lista de hijos del arbol
     */

    @Override
    public LinkedList<ITree<T>> getHijos() {
        return this.hijos;
    }

    /**
     * Devuelve el hijo en la posicion i.
     * 
     * @param i la posicion del hijo
     * @return el hijo en la posicion i
     */

    @Override
    public Tree<T> getHijo(int i) {
        return (Tree<T>) this.hijos.get(i);
    }

    /**
     * Asigna un comparador al arbol.
     * 
     * @param comparador el nuevo comparador
     */

    @Override
    public void setComparador(IComparador<T> comparador) {
        this.comparador = comparador;
    }

    /**
     * Busca un hijo en el árbol. Antes de realizar la búsqueda se debe definir un
     * comparador, y usar un objeto dummy de clase T, que tenga el valor del campo
     * por el valor que se desea buscar.
     * 
     * @param valor el valor del hijo
     * @throws RuntimeException si no se ha definido un comparador
     * @return la lista de hijos encontrados
     */

    @Override
    public LinkedList<ITree<T>> buscarHijo(T valor) {
        if (this.comparador == null) {
            throw new RuntimeException("No se ha definido un comparador");
        }
        LinkedList<ITree<T>> encontrados = new LinkedList<>();
        for (int i = 0; i < this.hijos.size(); i++) {
            if (this.comparador.compareTo(this.hijos.get(i).getValor(), valor) == 0) {
                encontrados.agregar(this.hijos.get(i));
            }
        }
        return encontrados;
    }

    /**
     * Busca un elemento en el árbol y lo guarda en la lista de hijos encontrados.
     * Hace la búsqueda por amplitud, usando una cola implementada con una
     * LinkedList, donde se agregan los nodos por el final y se remueven por la
     * cabeza. Antes de realizar la búsqueda se debe definir un
     * comparador, y usar un objeto dummy de clase T que tenga el valor del campo
     * por el valor
     * que se desea buscar.
     * 
     * @param valor el valor del elemento a buscar
     * @throws RuntimeException si no se ha definido un comparador
     * @return la lista de hijos encontrados
     */
    @Override
    public LinkedList<ITree<T>> buscar(T valor) {
        if (this.comparador == null) {
            throw new RuntimeException("No se ha definido un comparador");
        }
        LinkedList<ITree<T>> encontrados = new LinkedList<>();
        LinkedList<ITree<T>> cola = new LinkedList<>();
        if (this.vacio()) {
            return encontrados;
        }
        cola.agregar(this);
        while (!cola.vacia()) {
            ITree<T> tree = cola.remover(0);
            if (this.comparador.compareTo(tree.getValor(), valor) == 0) {
                encontrados.agregar(tree);
            }
            LinkedList<ITree<T>> hijos = tree.getHijos();
            for (int i = 0; i < hijos.size(); i++) {
                cola.agregar(hijos.get(i));
            }
        }
        return encontrados;
    }

    /**
     * Devuelve la altura del arbol.
     * 
     * @return la altura del arbol
     */

    @Override
    public int altura() {
        if (this.hijos.size() == 0) {
            return 1;
        }
        int maxAltura = 0;
        for (int i = 0; i < this.hijos.size(); i++) {
            int alturaHijo = this.hijos.get(i).altura();
            if (alturaHijo > maxAltura) {
                maxAltura = alturaHijo;
            }
        }
        return maxAltura + 1;
    }

    /**
     * Devuelve los niveles del arbol.
     * Esta es la parte recursiva del metodo getNiveles.
     * 
     * @param nivel   el nivel del arbol
     * @param niveles la lista de niveles
     * @return los niveles del arbol
     */

    private void getNiveles(int nivel, LinkedList<ITree<T>>[] niveles) {
        niveles[nivel].agregar(this);
        for (int i = 0; i < this.hijos.size(); i++) {
            Tree<T> hijo = (Tree<T>) this.hijos.get(i);
            hijo.getNiveles(nivel + 1, niveles);
        }
    }

    /**
     * Devuelve los niveles del arbol.
     * Esta función llama a la parte recursiva del metodo getNiveles.
     *
     * @return los niveles del arbol
     */

    @SuppressWarnings("unchecked")
    @Override
    public LinkedList<ITree<T>>[] getNiveles() {
        LinkedList<ITree<T>>[] niveles = new LinkedList[this.altura()];
        for (int i = 0; i < niveles.length; i++) {
            niveles[i] = new LinkedList<>();
        }
        this.getNiveles(0, niveles); // Llamada recursiva
        return niveles;
    }

    /**
     * Devuelve los elementos del nivel deseado del arbol.
     * Esta es la parte recursiva del metodo getNivel.
     * 
     * @param nivelActual    el nivel actual del arbol
     * @param elementosNivel la lista de elementos del nivel deseado
     * @param nivelDeseado   el nivel deseado del arbol
     */

    private void getNivel(int nivelActual, LinkedList<ITree<T>> elementosNivel, int nivelDeseado) {
        if (nivelActual == nivelDeseado) {
            elementosNivel.agregar(this);
            return;
        }
        for (int i = 0; i < this.hijos.size(); i++) {
            Tree<T> hijo = (Tree<T>) this.hijos.get(i);
            hijo.getNivel(nivelActual + 1, elementosNivel, nivelDeseado);
        }
    }

    /**
     * Devuelve los elementos del nivel deseado del arbol.
     * Esta función llama a la parte recursiva del metodo getNivel.
     * 
     * @param nivel el nivel deseado del arbol
     * @return los elementos del nivel deseado del arbol
     */
    @Override
    public LinkedList<ITree<T>> getNivel(int nivel) {
        if (nivel < 0 || nivel >= this.altura()) {
            return null;
        }
        LinkedList<ITree<T>> elementosNivel = new LinkedList<>();
        this.getNivel(0, elementosNivel, nivel); // Llamada recursiva
        return elementosNivel;
    }

    /**
     * Devuelve los ascendentes del arbol.
     * 
     * @param tree el arbol
     * @return los ascendentes del arbol
     */

    @Override
    public LinkedList<ITree<T>> getAscendentes(ITree<T> tree) {
        if (tree == null) {
            return null;
        }
        if (this == tree) {
            return new LinkedList<ITree<T>>();
        }
        if (this.hijos.vacia()) {
            return null;
        }
        for (int i = 0; i < this.hijos.size(); i++) {
            LinkedList<ITree<T>> ascendentes = this.hijos.get(i).getAscendentes(tree);
            if (ascendentes == null) {
                continue;
            }
            ascendentes.insertar(0, this);
            return ascendentes;
        }
        return null;
    }

    /**
     * Devuelve los descendientes del arbol.
     * 
     * @param tree el arbol
     * @return los descendientes del arbol
     */

    @Override
    public LinkedList<ITree<T>> getDescendientes(ITree<T> tree) {
        if (tree == null) {
            return null;
        }
        LinkedList<ITree<T>> descendientes = new LinkedList<>();
        LinkedList<ITree<T>>[] niveles = tree.getNiveles();
        if (niveles.length <= 1) {
            return descendientes;
        }
        for (int i = 1; i < niveles.length; i++) {
            for (int j = 0; j < niveles[i].size(); j++) {
                descendientes.agregar(niveles[i].get(j));
            }
        }
        return descendientes;
    }

    /**
     * Devuelve el nivel del arbol.
     * 
     * @param tree el arbol
     * @return el nivel del arbol
     */

    @Override
    public int getNumNiveL(ITree<T> tree) {
        if (tree == null) {
            return -1;
        }
        LinkedList<ITree<T>> ascendentes = this.getAscendentes(tree);
        if (ascendentes == null) {
            return -1;
        }
        return ascendentes.size();
    }

    /**
     * Devuelve el padre del arbol. Hay que recordar
     * que cada nodo en el arbol es un sub-árbol.
     * Por eso se dice que devuelve el padre de el arbol
     * dado como argumento, es decir es el hijo del
     * padre que va a devolver.
     * Si no consigue un padre, devuelve null.
     * 
     * @param tree el arbol al que se le busca un padre.
     * @return el padre del arbol o null si no lo consigue.
     */

    @Override
    public ITree<T> getPadre(ITree<T> tree) {
        if (tree == null) {
            return null;
        }
        LinkedList<ITree<T>> ascendentes = this.getAscendentes(tree);
        if (ascendentes == null) {
            return null;
        }
        if (ascendentes.vacia()) {
            return null;
        }
        return ascendentes.get(ascendentes.size() - 1);
    }

    /**
     * Devuelve la representacion del arbol en String.
     * Muy útil para hacer pruebas.
     * 
     * @return la representacion del arbol en String
     */

    @Override
    public String toString() {
        String txt = "";
        if (this.valor == null) {
            txt += "//";
        } else {
            txt += this.valor.toString();
        }
        txt += ": [";
        for (int i = 0; i < this.hijos.size(); i++) {
            txt += this.hijos.get(i).getValor().toString();
            if (i < this.hijos.size() - 1) {
                txt += ", ";
            }
        }
        txt += "]";
        return txt;
    }

    /**
     * Parte recursiva de vaciar el arbol.
     * 
     * @param nivel el nivel del arbol
     */

    private void vaciar(int nivel) {
        this.valor = null;
        for (int i = 0; i < this.hijos.size(); i++) {
            Tree<T> hijo = (Tree<T>) this.hijos.get(i);
            hijo.vaciar(nivel + 1);
        }
        if (nivel > 0) {
            this.hijos = null;
        }
        this.hijos = new LinkedList<>();
    }

    /**
     * Vacia el arbol. Hace una llamada recursiva al metodo vaciar(int nivel).
     */
    @Override
    public void vaciar() {
        this.vaciar(0);
    }

    /**
     * Devuelve la representacion de los niveles del arbol en String.
     * Muy util para hacer pruebas.
     * 
     * @return la representacion del arbol en String
     */

    @Override
    public String nivelesToString() {
        String txt = "";
        LinkedList<ITree<T>>[] niveles = this.getNiveles();
        for (int i = 0; i < niveles.length; i++) {
            for (int j = 0; j < niveles[i].size(); j++) {
                txt += niveles[i].get(j).toString();
                if (j < niveles[i].size() - 1) {
                    txt += "\t";
                }
            }
            txt += "\n";
        }
        return txt;
    }

    public static void main(String[] args) {

    }
}
