/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proy_2_arb_gen;

/**
 * Estructura de tabla hash para representar una estructura o tabla
 * hash.
 * Utiliza tipos de datos genéricos para guardar los valores
 * de cada nodo.
 * Y la clave utilizada, debe ser un String.
 */

public class StringHashTable<T> implements IHashTable<T> {

    /**
     * Clase Nodo<T> para poder tener una Clave y un Valor en la lista
     * de la Tabla de Hash. La clave es un String y el Valor un tipo
     * Genérico T.
     * 
     */

    @SuppressWarnings("hiding")
    private class Nodo<T> {
        /**
         * Clave del nodo. Se utiliza para calcular el hash. Y es un String.
         */
        String clave;
        /**
         * Valor del nodo, es genérico, asi que nos permite tener
         * cualquier tipo de dato en el HashTable.
         */
        T valor;

        /**
         * Constructor de la clase.
         * 
         * @param clave
         * @param valor
         */

        public Nodo(String clave, T valor) {
            this.clave = clave;
            this.valor = valor;
        }

        /**
         * Devuelve un String con la representación del nodo
         * 
         * @return el String con la representación del nodo.
         */
        public String toString() {
            String txt = "";
            txt += "Clave: " + this.clave + ", Valor: " + this.valor.toString();
            return txt;
        }
    }

    /**
     * Arreglo de enteros que contiene los primos.
     * Son utilizados para calcular el tamaño de la Tabla de Hash.
     * Y evitar, en lo posible colisiones.
     */

    private static final int[] PRIMOS = {
            2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67,
            71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149,
            151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229,
            233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293, 307, 311, 313,
            317, 331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389, 397, 401, 409,
            419, 421, 431, 433, 439, 443, 449, 457, 461, 463, 467, 479, 487, 491, 499,
            503, 509, 521, 523, 541, 547, 557, 563, 569, 571, 577, 587, 593, 599, 601,
            607, 613, 617, 619, 631, 641, 643, 647, 653, 659, 661, 673, 677, 683, 691,
            701, 709, 719, 727, 733, 739, 743, 751, 757, 761, 769, 773, 787, 797, 809,
            811, 821, 823, 827, 829, 839, 853, 857, 859, 863, 877, 881, 883, 887, 907,
            911, 919, 929, 937, 941, 947, 953, 967, 971, 977, 983, 991, 997, 1009
    };

    /**
     * Porcentaje de espacio adicional definido por default.
     */

    private static final int DEFAULT_PCT_ESPACIO_ADICIONAL = 50;

    /**
     * Porcentaje de espacio adicional máximo definido por default.
     */

    private static final int MAX_PCT_ESPACIO_ADICIONAL = 300;

    /**
     * Porcentaje de espacio adicional.
     */

    private Integer pctEspacioAdicional = null;

    /**
     * Tamaño de la tabla hash.
     */

    private int tam;

    /**
     * Número de items en la tabla hash.
     */

    private int numItems;

    /**
     * Un arreglo de LinkedList para representar la Tabla hash.
     */

    private LinkedList<Nodo<T>>[] hashTable;

    /**
     * Calcula el hash de una cadena de caracteres.
     * 
     * @param a la cadena de texto a calcular
     * @return el hash calculado
     */
    public int hash(String a) {
        if (a == null) {
            return -1;
        }
        if (a == "") {
            return -1;
        }
        int calculatedHash = 0;
        for (int i = 0; i < a.length(); i++) {
            calculatedHash += ((int) a.charAt(i)) * (i + 1);
        }
        return calculatedHash;
    };

    /**
     * Establece el porcentaje de espacio adicional.
     * 
     * @param pctEspacioAdicional el porcentaje de espacio adicional
     */

    public void setPctEspacioAdicional(int pctEspacioAdicional) {
        if (pctEspacioAdicional < 0 || pctEspacioAdicional > MAX_PCT_ESPACIO_ADICIONAL) {
            this.pctEspacioAdicional = DEFAULT_PCT_ESPACIO_ADICIONAL;
            return;
        }
        this.pctEspacioAdicional = pctEspacioAdicional;
    }

    /**
     * Calcula el tamaño de la tabla hash.
     * 
     * @param tam el tamaño de la tabla
     * @return el tamaño de la tabla hash
     */

    public int calcularTam(int tam) {
        if (tam < 0) {
            throw new IllegalArgumentException("El tamaño no puede ser negativo");
        }
        if (this.pctEspacioAdicional == null) {
            this.pctEspacioAdicional = DEFAULT_PCT_ESPACIO_ADICIONAL;
        }
        return tam * (100 + this.pctEspacioAdicional) / 100;
    }

    /**
     * Calcula el primo que va a ser utilizado para calcular el tamaño de la tabla
     * hash.
     * 
     * @param tam el tamaño de la tabla
     * @return el primo ha usar para calcular el hash
     */
    public int calcularPrimo(int tam) {
        if (tam < 0) {
            throw new IllegalArgumentException("El tamaño no puede ser negativo");
        }
        for (int i = 0; i < PRIMOS.length; i++) {
            if (PRIMOS[i] >= tam) {
                return PRIMOS[i];
            }
        }
        return -1;
    }

    /**
     * Constructor de la clase StringHashTable cuando se le da el tamaño de la
     * tabla.
     * 
     * @param tam el tamaño de la tabla
     */

    @SuppressWarnings("unchecked")
    public StringHashTable(int tam) {
        numItems = 0;
        if (tam < 0) {
            throw new IllegalArgumentException("El tamaño no puede ser negativo");
        }
        int tamCalculado = calcularTam(tam);
        int primoCalculado = calcularPrimo(tamCalculado);
        if (primoCalculado == -1) {
            this.tam = tamCalculado;
        } else {
            this.tam = primoCalculado;
        }
        this.hashTable = new LinkedList[this.tam];
        for (int i = 0; i < this.tam; i++) {
            this.hashTable[i] = new LinkedList<>();
        }
    }

    /**
     * Constructor de la clase StringHashTable cuando se le da el
     * tamaño de la tabla y el porcentaje de espacio adicional.
     * 
     * @param tam                 el tamaño de la tabla
     * @param pctEspacioAdicional el porcentaje de espacio adicional
     */

    @SuppressWarnings("unchecked")
    public StringHashTable(int tam, int pctEspacioAdicional) {
        numItems = 0;
        if (tam < 0) {
            throw new IllegalArgumentException("El tamaño no puede ser negativo");
        }
        this.setPctEspacioAdicional(pctEspacioAdicional);
        int tamCalculado = calcularTam(tam);
        int primoCalculado = calcularPrimo(tamCalculado);
        if (primoCalculado == -1) {
            this.tam = tamCalculado;
        } else {
            this.tam = primoCalculado;
        }
        this.hashTable = new LinkedList[this.tam];
        for (int i = 0; i < this.tam; i++) {
            this.hashTable[i] = new LinkedList<>();
        }

    }

    /**
     * Obtiene el tamaño de la tabla hash.
     * 
     * @return el tamaño de la tabla hash
     */

    @Override
    public int getTam() {
        return this.tam;
    }

    /**
     * Obtiene el número de items de la tabla hash.
     * 
     * @return el número de items de la tabla hash
     */

    public int getNumItems() {
        return this.numItems;
    }

    /**
     * Establece el tamaño de la tabla hash.
     * 
     * @param tam el tamaño de la tabla hash
     */

    @Override
    public void setTam(int tam) {
        int calcularPrimo = calcularPrimo(tam);
        if (calcularPrimo == -1) {
            this.tam = calcularTam(tam);
        }
        this.tam = tam;

    }

    /**
     * Reinicia la tabla hash.
     * En otras palabras, vacia la tabla hash.
     */

    @SuppressWarnings("unchecked")
    public void resetHashTable() {
        numItems = 0;
        int tamCalculado = calcularTam(this.tam);
        int primoCalculado = calcularPrimo(tamCalculado);
        if (primoCalculado == -1) {
            this.tam = tamCalculado;
        } else {
            this.tam = primoCalculado;
        }
        this.hashTable = new LinkedList[this.tam];
        for (int i = 0; i < this.tam; i++) {
            this.hashTable[i] = new LinkedList<>();
        }
    }

    /**
     * Inserta un par clave-valor en la tabla hash cuando se le da la clave y el
     * valor.
     * 
     * @param clave la clave del par clave-valor, tiene que ser un String.
     * @param valor el valor del par clave-valor, de tipo genérico T.
     * @return true si se pudo insertar el par clave-valor, false si no se pudo
     */

    @Override
    public boolean insertar(String clave, T valor) {
        if (clave == null) {
            return false;
        }
        if (clave == "") {
            return false;
        }
        int calculatedHash = this.hash(clave);
        if (calculatedHash == -1) {
            return false;
        }
        int index = calculatedHash % this.tam;
        Nodo<T> nodo = new Nodo<>(clave, valor);
        boolean resultado = this.hashTable[index].agregar(nodo);
        if (resultado) {
            numItems++;
        }
        return resultado;
    }

    /**
     * Inserta un par clave-valor en la tabla hash cuando la clave es el mismo valor
     * a guardar..
     * Utilizado para hacer pruebas, y no tener que crear clases para
     * cada par clave-valor, y así simplificar el código de pruebas.
     * 
     * @param clave la clave del par clave-valor
     * @return true si se pudo insertar el par clave-valor, false si no se pudo
     */

    @SuppressWarnings("unchecked")
    @Override
    public boolean insertar(String clave) {
        if (clave == null) {
            return false;
        }
        if (clave == "") {
            return false;
        }
        int calculatedHash = this.hash(clave);
        if (calculatedHash == -1) {
            return false;
        }
        int index = calculatedHash % this.tam;
        Nodo<String> nodo = new Nodo<>(clave, clave);
        boolean resultado = this.hashTable[index].agregar((Nodo<T>) nodo);
        if (resultado) {
            numItems++;
        }
        return resultado;
    }

    /**
     * Busca un par clave-valor en la tabla hash cuando se le da la clave.
     * Devuelve el primer valor que sea igual en la lista asociada al hash
     * calculado. Evita los que son distintos ero que por colisión tienen el mismo
     * hash.
     * Si no lo consigue devuelve null.
     * 
     * @param clave la clave del par clave-valor
     * @return el valor si se encuentra, null si no se encuentra
     */

    @Override
    public T buscar(String clave) {
        if (clave == null) {
            return null;
        }
        if (clave == "") {
            return null;
        }
        int calculatedHash = this.hash(clave);
        if (calculatedHash == -1) {
            return null;
        }
        int index = calculatedHash % this.tam;
        for (int i = 0; i < this.hashTable[index].size(); i++) {
            if (this.hashTable[index].get(i).clave.equals(clave)) {
                return this.hashTable[index].get(i).valor;
            }
        }
        return null;
    }

    /**
     * Busca todos los par clave-valor en la tabla hash cuando se le da la clave.
     * Es decir si hay valores repetidos, que estarían en la lista asociada a el
     * Hash calculado, devuelve todos los valores que sean iguales, sin devolver
     * los que por colisión, tengan el mismo hash, pero no sean iguales.
     * 
     * @param clave la clave del par clave-valor
     * @return la lista de valores si se encuentran, null si hay un error o una
     *         lista vacia si no se encuentran valores.
     */

    @Override
    public LinkedList<T> buscarTodos(String clave) {
        if (clave == null) {
            return null;
        }
        if (clave == "") {
            return null;
        }
        int calculatedHash = this.hash(clave);
        if (calculatedHash == -1) {
            return null;
        }
        int index = calculatedHash % this.tam;
        LinkedList<T> lista = new LinkedList<>();
        for (int i = 0; i < this.hashTable[index].size(); i++) {
            if (this.hashTable[index].get(i).clave.equals(clave)) {
                lista.agregar(this.hashTable[index].get(i).valor);
            }
        }
        return lista;
    }

    /**
     * Devuelve una representación en cadena de la tabla hash.
     * Muy util para hacer pruebas.
     * 
     * @return una representación en cadena de la tabla hash
     */

    @Override
    public String toString() {
        String txt = "";
        for (int i = 0; i < this.tam; i++) {
            txt += i + ": " + this.hashTable[i].toString();
            if (i < this.tam - 1) {
                txt += "\n";
            }
        }
        return txt;
    }

    /**
     * Solo para hacer pruebas
     * 
     * @param args
     */
    public static void main(String[] args) {

    }

}
