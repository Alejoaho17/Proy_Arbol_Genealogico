/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proy_2_arb_gen;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

/**
 * Clase encargada de manejar las acciones de la interfaz gráfica
 * vs la representación interna de las clases árbol, hash, etc...
 * Se encarga de coordinar todas estas acciones para lleven un orden y una
 * ejecución correcta.
 */

public class AppController {

    /**
     * Tamaño de la altura del grafo
     */

    public static final int GRAPH_HEIGHT = 1000;

    /**
     * Tamaño de la anchura del grafo
     */

    public static final int GRAPH_WIDTH = 1000;

    JsonLoader jsonLoader = new JsonLoader();
    House house;
    Tree<Lord> houseTree;
    StringHashTable<Tree<Lord>> hashTableUniqueName;
    StringHashTable<Tree<Lord>> hashTableAlias;

    Graph graph;

    /**
     * Constructor de AppController.
     */

    public AppController() {
        this.house = new House();
        this.houseTree = null;
        this.graph = new SingleGraph("Árbol Genealógico de la Casa");
    }

    /**
     * Carga la casa a usar mediante la carga del archivo JSON.
     *
     * @param nombreArchivo el nombre del archivo JSON
     * @return true si la carga fue exitosa, false en caso contrario
     */

    public boolean loadHouse(String nombreArchivo) {
        String json = this.jsonLoader.load(nombreArchivo);
        if (!this.house.parse(json)) {
            return false;
        }
        int tam = this.house.lords.size();
        this.hashTableUniqueName = new StringHashTable<>(tam);
        this.hashTableAlias = new StringHashTable<>(tam);

        return true;
    }

    /**
     * Obtiene la casa actual
     * 
     * @return la casa actual
     */

    public House getHouse() {
        return this.house;
    }

    /**
     * Obtiene el HashTable de los unique names de los lords.
     * 
     * @return el HashTableUniqueName
     */

    public StringHashTable<Tree<Lord>> getHashTableUniqueName() {
        return this.hashTableUniqueName;
    }

    /**
     * Obtiene el HashTable de los alias de los lords.
     * 
     * @return el HashTableAlias
     */

    public StringHashTable<Tree<Lord>> getHashTableAlias() {
        return this.hashTableAlias;
    }

    /**
     * Carga el árbol y las hashTables.
     */

    public void loadTreeAndHashes() {
        for (int i = 0; i < this.house.lords.size(); i++) {

            // el lord que vamos a ingresar al árbol y a las hashTables
            Lord lord = this.house.lords.get(i);
            if (i != 0) {
                if (lord.father == null) {
                    throw new RuntimeException("Error en la carga del árbol, el padre de " + lord.name
                            + " es Unknown y no es el primero de la lista.");
                }
            }

            // creamos el sub-árbol del lord.
            Tree<Lord> lordTree = new Tree<>(lord);

            LinkedList<Tree<Lord>> lordDuplicados;

            // insertamos el sub-árbol del lord en las hashTables
            if (lord.uniqueName != null) {
                this.hashTableUniqueName.insertar(lord.uniqueName, lordTree);

                lordDuplicados = this.hashTableUniqueName.buscarTodos(lord.uniqueName);
                if (lordDuplicados.size() > 1) {
                    throw new RuntimeException(
                            "Error en la carga del árbol, hay mas de un unique name de: " + lord.uniqueName);
                }
            }

            if (lord.alias != null) {
                this.hashTableAlias.insertar(lord.alias, lordTree);

                lordDuplicados = this.hashTableAlias.buscarTodos(lord.alias);
                if (lordDuplicados.size() > 1) {
                    throw new RuntimeException(
                            "Error en la carga del árbol, hay mas de un alias de: " + lord.uniqueName + ": "
                                    + lord.alias);
                }
            }
            if (i == 0) {
                this.houseTree = lordTree;
                continue;
            }

            // Creamos un lord padre para poder hacer las búsquedas.
            Lord lordPadre = new Lord();
            lordPadre.uniqueName = lord.father;
            lordPadre.fullName = lord.father;
            lordPadre.alias = lord.father;

            // Buscamos por el alias a ver si es el que necesitamos
            this.houseTree.setComparador(Lord.comparadorAlias);
            LinkedList<ITree<Lord>> padres = this.houseTree.buscar(lordPadre);
            if (padres.size() > 1) {
                throw new RuntimeException("Error en la carga del árbol, hay mas de un padre de " + lord.name);
            }
            if (padres.size() == 1) {
                // Si lo encontramos lo agregamos como hizo del padre encontrado.
                Tree<Lord> padre = (Tree<Lord>) padres.get(0);
                if (padre != null) {
                    padre.agregarHijo(lordTree);
                    continue;
                }
            }

            // Buscamos por el nombre único a ver si es el que necesitamos
            this.houseTree.setComparador(Lord.comparadorUnicoNombre);
            padres = this.houseTree.buscar(lordPadre);
            if (padres.size() > 1) {
                throw new RuntimeException("Error en la carga del árbol, hay mas de un padre de " + lord.name);
            }
            if (padres.size() == 1) {
                // Si lo encontramos lo agregamos como hizo del padre encontrado.
                Tree<Lord> padre = (Tree<Lord>) padres.get(0);
                if (padre != null) {
                    padre.agregarHijo(lordTree);
                    continue;
                }
            }

            // Buscamos por el nombre completo
            this.houseTree.setComparador(Lord.comparadorNombreFull);
            padres = this.houseTree.buscar(lordPadre);
            if (padres.size() > 1) {
                throw new RuntimeException("Error en la carga del árbol, hay mas de un padre de " + lord.name);
            }
            if (padres.size() == 1) {
                // Si se encontró lo agregamos como hizo del padre encontrado.
                Tree<Lord> padre = (Tree<Lord>) padres.get(0);
                if (padre != null) {
                    padre.agregarHijo(lordTree);
                    continue;
                }
            }

            // Si llegó aquí, no se encontró el padre.
            throw new RuntimeException("Error en la carga del árbol, el padre de " + lord.name
                    + " no se encuentra en el árbol.");
        }
        // Vaciamos la lista, pues ya se encuentra en el árbol y se prepara por si se
        // tiene que volver a usar
        this.house.vaciar();
    }

    /**
     * Crea un gráfico con el árbol de la casa, el id del nodo del padre, la
     * coordenada "x", la coordenada "y" y el tamaño de los pasos que que realizan
     * en ambos ejes, y lo carga. Esta es la parte recursiva de loadTreeGraph.
     * 
     * @param lord         el árbol o sub-árbol que se va a cargar.
     * @param fatherNodeId el id del nodo del padre
     * @param x            la coordenada x
     * @param y            la coordenada y
     * @param xStepSize    el tamaño de los pasos que se realizan en el eje x
     * @param yStepSize    el tamaño de los pasos que se realizan en el eje y
     */

    private void loadTreeGraph(ITree<Lord> lord, String fatherNodeId, int x, int y,
            int xStepSize,
            int yStepSize) {

        // Si el padre es null, limpiamos el gráfico porque es la raiz.
        if (fatherNodeId == null) {
            this.graph.clear();
        }

        // vamos a crear un id para el nodo en graph stream, con el cual luego sea
        // posible hacer la búsqueda en el hash ya sea por nombre único o por alias
        // usamos el formato: uniqueName:alias. Así cuando tenga un nodo hago un
        // split por el ":" y luego lo busco en el hashTable correspondiente.
        String uniqueName = lord.getValor().uniqueName;
        if (uniqueName == null) {
            uniqueName = "";
        }
        String alias = lord.getValor().alias;
        if (alias == null) {
            alias = "";
        }
        String name = lord.getValor().name;
        if (name == null) {
            name = "";
        }
        String nodeId = uniqueName + ":" + alias;

        // asigno el nodeId para crear el nodo y le asigno como etiqueta el nombre,
        // ya que es más corto y permite ver mejor a los nodos del árbol en su
        // representación en graphStream.
        Node node = this.graph.addNode(nodeId);
        node.setAttribute("ui.label", name);

        // Le asigno su posición dentro de la cuadricula que se creó para que el grafo
        // tenga forma de árbol genealógico.
        node.setAttribute("xy", x, y);

        // Se crea la arista y se le da un id representativo, aunque en este proyecto
        // el id de la arista no se usa.
        if (fatherNodeId != null) {
            this.graph.addEdge(fatherNodeId + "->" + nodeId, fatherNodeId, nodeId);
        }

        // Obtengo los hijos del subarbol que estoy recorriendo.
        LinkedList<ITree<Lord>> hijos = lord.getHijos();

        // Si no tiene hijos, se termina la recursividad.
        if (hijos.vacia()) {
            return;
        }

        // Se calcula el nodo mas a la izquierda para que todos queden centrados con
        // respecto a su padre. Llamamos a este valor xOffset. Como va a la izquierda
        // en el plano cartesiano, el signo es negativo.
        int xOffset = -1 * ((int) hijos.size() / 2) * xStepSize;
        x = x + xOffset;
        for (int i = 0; i < hijos.size(); i++) {
            // a cada hijo se lo llama recursivamente con un y al que se le resta yStepSize,
            // para que este por debajo de su padre, y el x calculado sumándole al x el
            // xStepSize, así cada hijo se va a ir moviendo hacia la derecha y todos
            // centrados con respecto a su padre.
            this.loadTreeGraph(hijos.get(i), nodeId, x, y - yStepSize, xStepSize, yStepSize);
            x += xStepSize;
        }
    }

    /**
     * Crea un gráfico con el árbol de la casa y lo carga. Hace una llamada a
     * loadTreeGraph(lord, fatherNodeId, x, y, xStepSize, yStepSize), para que
     * se carguen los nodos recursivamente, en las posiciones correspondientes.
     * Al final se creo una cuadricula y cada nodo va en un punto de la cuadricula,
     * asi los padres tienen a sus hijos en un nivel mas bajo, todos en la misma
     * fila y todos centrados con respecto a el.
     * 
     * @param lord el lord (arbol o subarbol) que se va a cargar y por ende
     *             mostrar en el gráfico.
     */

    public void loadTreeGraph(ITree<Lord> lord) {
        // Obtengo los niveles del árbol, para poder calcular los xStepSize y yStepSize.
        LinkedList<ITree<Lord>>[] niveles = lord.getNiveles();
        int numNiveles = niveles.length;

        // Calculo los xStepSize y yStepSize
        int yStepSize = AppController.GRAPH_HEIGHT / numNiveles;
        int maxNumGen = 0;
        // el mayor número de hijos de todos los niveles
        // para calcular el xStepSize
        for (int i = 0; i < niveles.length; i++) {
            if (niveles[i].size() > maxNumGen) {
                maxNumGen = niveles[i].size();
            }
        }
        int xStepSize = AppController.GRAPH_WIDTH / maxNumGen;

        // ahora hago la llamada recursiva para cargar el grafo de graphStream.
        this.loadTreeGraph(lord, null, 0, 0, xStepSize, yStepSize);

        // Y le doy estilo a los nodos y aristas
        String css = "node {" +
                " text-size: 16px;" + // Tamaño del texto
                " text-font: Papyrus;" + // Define la fuente como Papyrus
                " text-style: bold;" + // Texto en negrita
                " text-alignment: center;" + // Texto centrado
                " text-background-mode: plain;" + // Activa el fondo de texto
                " text-background-color: #FFFFE0;" + // Color de fondo del texto (un tono claro de amarillo)
                " text-padding: 5px;" + // Relleno alrededor del texto

                " fill-color: #F0E68C;" + // Color de relleno del nodo (un tono más oscuro de amarillo)
                " stroke-mode: plain;" + // Activa el borde del nodo
                " stroke-color: black;" + // Color del borde
                " shape: box;" + // Forma de los nodos como caja
                " size-mode: fit;" + // Ajuste automático del tamaño del nodo
                "}" +
                "edge {" +
                " shape: cubic-curve;" + // Forma de las aristas como curva cubica
                " fill-color: gray;" + // Color de relleno de las aristas
                " stroke-mode: plain;" + // Activa el borde de las aristas
                // " stroke-width: 3px;" + // Ancho del borde de las aristas
                "}";
        this.graph.setAttribute("ui.stylesheet", css);
        this.graph.setAttribute("ui.antialias", true);
        this.graph.setAttribute("ui.quality", true);
    }

    /**
     * Crea un grafo con los antepasados del lord (árbol o sub-árbol) y lo carga.
     * 
     * @param lord el lord (árbol o sub-árbol) al cual se le van a cargar
     *             los antepasados y mostrar en el gráfico.
     */

    public void loadAntepasadosGraph(ITree<Lord> lord) {

        // Limpiamos el gráfico
        this.graph.clear();
        // obtenemos todos los antepasados del lord
        LinkedList<ITree<Lord>> antepasados = this.houseTree.getAscendentes(lord);
        if (antepasados.vacia()) {
            return;
        }

        // necesitamos un arreglo de ids de nodos para los antepasados
        // al igual que con el loadTreeGraph, los ids serán uniqueName:alias
        // ya que me permite poder hacer luego busquedas en el hashTable
        // ya sea por uniqueName o por alias haciendo solamente un split
        // de uniqueName:alias por el ":".

        String[] nodeIds = new String[antepasados.size()];
        int numAntepasados = antepasados.size();

        // Calculamos el yStepSize para que los nodos estén equidistantes.
        int yStepSize = AppController.GRAPH_HEIGHT / numAntepasados;
        for (int i = 0, y = 0; i < antepasados.size(); i++, y -= yStepSize) {

            // calculo los nodeIds
            String uniqueName = antepasados.get(i).getValor().uniqueName;
            if (uniqueName == null) {
                uniqueName = "";
            }
            String alias = antepasados.get(i).getValor().alias;
            if (alias == null) {
                alias = "";
            }
            String name = antepasados.get(i).getValor().name;
            if (name == null) {
                name = "";
            }
            String nodeId = uniqueName + ":" + alias;
            nodeIds[i] = nodeId;

            // creo el nodo con el nodeId
            Node node = this.graph.addNode(nodeId);

            // pero la etiqueta (label) lo hago con el name
            node.setAttribute("ui.label", name);
            // y le doy su ubicación en el gráfico, ver que el for, decrementa
            // el valor de y en yStepSize en cada iteración, de esta forma los hijos
            // están por debajo de los padres
            node.setAttribute("xy", 0, y);
        }
        for (int i = 0; i < nodeIds.length - 1; i++) {
            // ahora uno con una arista a cada padre con su hijo.
            String fatherNodeId = nodeIds[i];
            String nodeId = nodeIds[i + 1];
            this.graph.addEdge(fatherNodeId + "->" + nodeId, fatherNodeId, nodeId);
        }

        // le damos el estilo al gráfico
        String css = "node {" +
                " text-size: 16px;" + // Tamaño del texto
                " text-font: Papyrus;" + // Define la fuente como Papyrus
                " text-style: bold;" + // Texto en negrita
                " text-alignment: center;" + // Texto centrado
                " text-background-mode: plain;" + // Activa el fondo de texto
                " text-background-color: #FFFFE0;" + // Color de fondo del texto (un tono claro de amarillo)
                " text-padding: 5px;" + // Relleno alrededor del texto

                " fill-color: #F0E68C;" + // Color de relleno del nodo (un tono más oscuro de amarillo)
                " stroke-mode: plain;" + // Activa el borde del nodo
                " stroke-color: black;" + // Color del borde
                " shape: box;" + // Forma de los nodos como caja
                " size-mode: fit;" + // Ajuste automático del tamaño del nodo
                "}" +
                "edge {" +
                " shape: cubic-curve;" + // Forma de las aristas como curva cubica
                " fill-color: gray;" + // Color de relleno de las aristas
                " stroke-mode: plain;" + // Activa el borde de las aristas
                // " stroke-width: 3px;" + // Ancho del borde de las aristas
                "}";
        this.graph.setAttribute("ui.stylesheet", css);
        this.graph.setAttribute("ui.antialias", true);
        this.graph.setAttribute("ui.quality", true);

    }

    /**
     * Devuelve una representación del grafo del árbol en String.
     * Solo usado para hacer pruebas.
     * 
     * @return la representación del grafo del árbol en String
     */

    public String graphToString() {
        String txt = "Grafo: " + this.graph.toString() + "\n";
        for (int i = 0; i < this.graph.getNodeCount(); i++) {
            txt += "nodo: " + this.graph.getNode(i).toString() + "\n";
        }
        for (int i = 0; i < this.graph.getEdgeCount(); i++) {
            txt += "Arista: " + this.graph.getEdge(i).getId().toString() + "\n";
        }
        return txt;
    }

    /**
     * Hace un reset del árbol y el grafo
     */

    public void reset() {
        this.house.vaciar();
        if (this.houseTree != null) {
            this.houseTree.vaciar();
        }
        this.graph.clear();
    }

    public static void main(String[] args) {

    }

}
