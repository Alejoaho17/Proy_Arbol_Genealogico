/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proy_2_arb_gen;

interface IAppController {
    public boolean loadHouse(String nombreArchivo);

    public House getHouse();

    public StringHashTable<Tree<Lord>> getHashTableUniqueName();

    public StringHashTable<Tree<Lord>> getHashTableFullName();

    public StringHashTable<Tree<Lord>> getHashTableAlias();

    public StringHashTable<Tree<Lord>> getHashTableTitle();

    public void loadTreeAndHashesUsingTreeSearch();

    public void loadTreeAndHashesUsingHashSearch();

    public ITree<Lord> getHouseTree();

    public void loadTreeGraph(ITree<Lord> lord, String fatherNodeId, int nivel);

    public void loadTreeGraph(ITree<Lord> lord);

    public String graphToString();

    public void reset();
}
