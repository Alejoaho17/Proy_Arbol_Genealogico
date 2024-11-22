/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proy_2_arb_gen;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

public class AppController {
    
    House house;

    Tree<Lord> houseTree;

    StringHashTable<Tree<Lord>> hashTableUniqueName;

    StringHashTable<Tree<Lord>> hashTableFullName;

    StringHashTable<Tree<Lord>> hashTableTitle;

    StringHashTable<Tree<Lord>> hashTableAlias;

    Graph graph;

    public AppController() {
        this.house = new House();
        this.houseTree = null;
        this.graph = new SingleGraph("Árbol Genealógico de la Casa");

    }

    public boolean loadHouse(String nombreArchivo) {

        JsonLoader jsonLoader = new JsonLoader();
        String jsonHouse = jsonLoader.load(nombreArchivo);
        if(!this.house.parse(jsonHouse)){
            return false;
        }

        int tam = this.house.lords.size();
        
        hashTableUniqueName = new StringHashTable<>(tam);

        hashTableFullName = new StringHashTable<>(tam);

        hashTableTitle = new StringHashTable<>(tam);

        hashTableAlias = new StringHashTable<>(tam);

        return true;

    }

    public House getHouse(){
        return this.house;
    }

    public StringHashTable<Tree<Lord>> getHashTableUniqueName(){
        return this.hashTableUniqueName;
    }

    public StringHashTable<Tree<Lord>> getHashTableFullName(){
        return this.hashTableFullName;
    }

    public StringHashTable<Tree<Lord>> getHashTableTitle(){
        return this.hashTableTitle;
    }

    public StringHashTable<Tree<Lord>> getHashTableAlias(){
        return this.hashTableAlias;
    }

    public void loadTreeAndHashesUsingTreeSearch(){
        for(int i = 0; i < this.house.lords.size(); i++){
            Lord lord = this.house.lords.get(i);
            if(i != 0 && lord.father == null){
                throw new RuntimeException("El lord: " + lord.fullName + " no tiene padre");
            }
            Tree<Lord> lordTree = new Tree<>(lord);
            if(lord.uniqueName != null){
                this.hashTableUniqueName.insertar(lord.uniqueName, lordTree);
            }
            if(lord.fullName != null){
                this.hashTableFullName.insertar(lord.fullName, lordTree);
            }
            if(lord.title != null){
                this.hashTableTitle.insertar(lord.title, lordTree);
            }
            if(lord.alias != null){
                this.hashTableAlias.insertar(lord.alias, lordTree);
            }
            if(i == 0){
                this.houseTree = lordTree;
                continue;
            }
            Lord lordPadre = new Lord();
            lordPadre.alias = lord.father;
            lordPadre.uniqueName = lord.father;
            lordPadre.fullName = lord.father;

            this.houseTree.setComparador(Lord.comparadorAlias);

            LinkedList<ITree<Lord>> padres = this.houseTree.buscar(lordPadre);
            if(padres.size() > 1){
                throw new RuntimeException("El lord: " + lord.fullName + " tiene más de un padre");
            }

            if(padres.size() == 1){
                Tree<Lord> padre = (Tree<Lord>) padres.get(0);
                padre.agregarHijo(lordTree);
                continue;
            }

            this.houseTree.setComparador(Lord.comparadorUnicoNombre);

            padres = this.houseTree.buscar(lordPadre);
            if(padres.size() > 1){
                throw new RuntimeException("El lord: " + lord.fullName + " tiene más de un padre");
            }

            if(padres.size() == 1){
                Tree<Lord> padre = (Tree<Lord>) padres.get(0);
                padre.agregarHijo(lordTree);
                continue;
            }

            this.houseTree.setComparador(Lord.comparadorNombreFull);

            padres = this.houseTree.buscar(lordPadre);
            if(padres.size() > 1){
                throw new RuntimeException("El lord: " + lord.fullName + " tiene más de un padre");
            }

            if(padres.size() == 1){
                Tree<Lord> padre = (Tree<Lord>) padres.get(0);
                padre.agregarHijo(lordTree);
                continue;
            }

            throw new RuntimeException("El padre del lord: " + lord.fullName + " no aparece en el árbol");

        }
    }

    public void loadTreeAndHashesUsingHashSearch(){
        for(int i = 0; i < this.house.lords.size(); i++){
            Lord lord = this.house.lords.get(i);
            if(i != 0 && lord.father == null){
                throw new RuntimeException("El lord: " + lord.fullName + " no tiene padre");
            }
            Tree<Lord> lordTree = new Tree<>(lord);
            if(lord.uniqueName != null){
                this.hashTableUniqueName.insertar(lord.uniqueName, lordTree);
            }
            if(lord.fullName != null){
                this.hashTableFullName.insertar(lord.fullName, lordTree);
            }
            if(lord.title != null){
                this.hashTableTitle.insertar(lord.title, lordTree);
            }
            if(lord.alias != null){
                this.hashTableAlias.insertar(lord.alias, lordTree);
            }
            if(i == 0){
                this.houseTree = lordTree;
                continue;
            }
            Lord lordPadre = new Lord();
            lordPadre.alias = lord.father;
            lordPadre.uniqueName = lord.father;
            lordPadre.fullName = lord.father;

            LinkedList<Tree<Lord>> padres = this.hashTableAlias.buscarTodos(lordPadre.alias);

            if(padres.size() > 1){
                throw new RuntimeException("El lord: " + lord.fullName + " tiene más de un padre");
            }

            if(padres.size() == 1){
                Tree<Lord> padre = (Tree<Lord>) padres.get(0);
                padre.agregarHijo(lordTree);
                continue;
            }

            padres = this.hashTableUniqueName.buscarTodos(lordPadre.uniqueName);
            if(padres.size() > 1){
                throw new RuntimeException("El lord: " + lord.fullName + " tiene más de un padre");
            }

            if(padres.size() == 1){
                Tree<Lord> padre = (Tree<Lord>) padres.get(0);
                padre.agregarHijo(lordTree);
                continue;
            }


            padres = this.hashTableFullName.buscarTodos(lordPadre.fullName);
            if(padres.size() > 1){
                throw new RuntimeException("El lord: " + lord.fullName + " tiene más de un padre");
            }

            if(padres.size() == 1){
                Tree<Lord> padre = (Tree<Lord>) padres.get(0);
                padre.agregarHijo(lordTree);
                continue;
            }

            throw new RuntimeException("El padre del lord: " + lord.fullName + " no aparece en el árbol");

        }
    }

    public static void main(String[] args){
        AppController appController = new AppController();
        appController.loadHouse("./data/Baratheon.json");
        appController.loadTreeAndHashesUsingHashSearch();
        System.out.println(appController.houseTree.nivelesToString());
    }


}
