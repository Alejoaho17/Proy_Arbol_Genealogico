/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package proy_2_arb_gen;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;

public class VentanaPrincipal extends javax.swing.JFrame implements ViewerListener, MouseMotionListener {

    private Proy_2_Arb_Gen mainClass;
    private AppController appController;
    private Graph graph;
    private View view;
    private SwingViewer viewer;

    private boolean loop = true;
    private Node selectedNode = null;
    private boolean draggedNode = false;
    private String selectedLordByName = null;
    private boolean selectedLordByNameShowed = false;
    private String selectedLordByTitle = null;
    private boolean selectedLordByTitleShowed = false;
    
    
    public VentanaPrincipal(Proy_2_Arb_Gen mainClass) {
        System.setProperty("org.graphstream.ui", "swing");
        setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Árbol Genealógico de las Casas");
        this.mainClass = mainClass;
        this.appController = this.mainClass.appController;
        this.graph = this.mainClass.appController.graph;
        initComponents();

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1400, 1200));
        this.jLabelTituloVentanaPrincipal.setText("House");
        this.jPanelTree.setLayout(new BorderLayout());
        this.jPanelTree.setPreferredSize(new Dimension(1000, 1000));
        setUpGraphView();
    }

    private void setUpGraphView() {
        viewer = (SwingViewer) new SwingViewer(graph, SwingViewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        viewer.disableAutoLayout();
        view = this.viewer.addDefaultView(false);
        this.jPanelTree.add((Component) view, BorderLayout.CENTER);
        ((Component) view).addMouseMotionListener(this);
        managingMouse();
    }

    private void managingMouse() {
        ViewerPipe fromViewer = viewer.newViewerPipe();
        fromViewer.addViewerListener(this);
        fromViewer.addSink(graph);
        new Thread(() -> {
            while (loop) {
                fromViewer.pump();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void viewClosed(String viewName) {
        loop = false;
    }

    @Override
    public void buttonPushed(String id) {
        Node nodo = graph.getNode(id);
        if (nodo != null) {
            this.selectedNode = nodo;
        }
    }

    @Override
    public void buttonReleased(String id) {
        Node nodo = graph.getNode(id);
        if (nodo != null) {
            if (this.selectedNode != null) {
                if (this.draggedNode) {
                    this.draggedNode = false;
                    return;
                }
                //Aqui es donde tengo que hacer algo
                this.getVentanaDetalleLord(id);
            }
        }
        this.draggedNode = false;
    }

    @Override
    public void mouseOver(String id) {
        //Solo para cumplir con la interfaz
    }

    @Override
    public void mouseLeft(String id) {
        //Solo para cumplir con la interfaz
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (this.selectedNode != null) {
            this.draggedNode = true;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //Solo para cumplir con la interfaz
    }
    
    public void setJLabelTitle(String titleString) {
        this.jLabelTituloVentanaPrincipal.setText(titleString);
        this.jLabelTituloVentanaPrincipal.setVisible(true);
    }
    
    public void resetWindows(){
        if(this.appController.houseTree == null){
            return;
        }
        this.jTextFieldNombre.setText("");
        this.jTextFieldTitulo.setText("");
        String[] arregloVacio = new String[0];
        this.jListLordsEncontradosPorNombre.clearSelection();
        this.jListLordsEncontradosPorNombre.setListData(arregloVacio);
        this.jListLordsEncontradosPorTitulo.clearSelection();
        this.jListLordsEncontradosPorTitulo.setListData(arregloVacio);
        this.jSpinnerNroGeneracion.setValue(0);
        //hAY QUE CAMBIAR EL VALOR MIN Y MAX
        this.jTextAreaIntegrantesGeneracion.setText("");
        this.appController.loadTreeGraph(this.appController.houseTree);
        this.elementsCheckEnable();
    }
    
    public void elementsCheckEnable(){
        if(this.appController.houseTree == null){
            this.jButtonBuscarPorNombreAlias.setEnabled(false);
            this.jTextFieldNombre.setEnabled(false);
            String[] arregloVacio = new String[0];
            this.jListLordsEncontradosPorNombre.clearSelection();
            this.jListLordsEncontradosPorNombre.setListData(arregloVacio);
            this.jButtonAntepasados.setEnabled(false);
            this.jButtonBuscarPorTitulo.setEnabled(false);
            this.jTextFieldTitulo.setEnabled(false);
            this.jListLordsEncontradosPorTitulo.clearSelection();
            this.jListLordsEncontradosPorTitulo.setListData(arregloVacio);
            this.jSpinnerNroGeneracion.setValue(0);
            this.jSpinnerNroGeneracion.setEnabled(false);
            this.jButtonBuscarGeneracion.setEnabled(false);
            this.jTextAreaIntegrantesGeneracion.setText("");
            this.jButtonReset.setEnabled(false);
            return;
        }
        this.jButtonBuscarPorNombreAlias.setEnabled(true);
        this.jTextFieldNombre.setEnabled(true);
        this.jButtonBuscarPorTitulo.setEnabled(true);
        this.jTextFieldTitulo.setEnabled(true);
        this.jSpinnerNroGeneracion.setEnabled(true);
        this.jButtonBuscarGeneracion.setEnabled(true);
        this.jButtonReset.setEnabled(true);
        if(this.jListLordsEncontradosPorNombre.getSelectedValue() != null){
            this.jButtonAntepasados.setEnabled(true);
        } else {
            this.jButtonAntepasados.setEnabled(false);
        }
    }
    
    private void setUpSpinner(){
        if(this.appController.houseTree != null){
            int treeHeight = this.appController.houseTree.altura();
            SpinnerNumberModel model = new SpinnerNumberModel(0, 0, treeHeight - 1, 1);
            this.jSpinnerNroGeneracion.setModel(model);
            JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) this.jSpinnerNroGeneracion.getEditor();
            editor.getTextField().setEditable(false);
            return;
        }
        SpinnerNumberModel model = new SpinnerNumberModel(0, 0, 0, 0);
        this.jSpinnerNroGeneracion.setModel(model);
    }

    public Tree<Lord> getVentanaDetalleLord(String lordString) {
        String[] lordUniqueNameAndAlias = lordString.split(":");
        String lordUniqueName = lordUniqueNameAndAlias[0].strip();
        String lordAlias = null;
        if (lordUniqueNameAndAlias.length > 1) {
            lordAlias = lordUniqueNameAndAlias[1].strip();
        }
        Tree<Lord> lordTree = null;
        if (lordAlias != null && !lordAlias.isEmpty()) {
            lordTree = this.appController.getHashTableAlias().buscar(lordAlias);
        }   
        if (lordTree == null && lordUniqueName != null && !lordUniqueName.isEmpty()) {
            lordTree = this.appController.getHashTableUniqueName().buscar(lordUniqueName);
        }
        if(lordTree == null){
            JOptionPane.showMessageDialog(this, "No se consiguio el lord : " + lordString + ", ni por el Alias ni por el Nombre Unico");
        }
        VentanaDetalleLord ventanaDetalleLord = new VentanaDetalleLord(this, lordTree.getValor());
        ventanaDetalleLord.setVisible(true);
        return lordTree;
    }

    public VentanaPrincipal() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabelTituloVentanaPrincipal = new javax.swing.JLabel();
        jPanelTree = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldNombre = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListLordsEncontradosPorNombre = new javax.swing.JList<>();
        jLabel2 = new javax.swing.JLabel();
        jButtonBuscarPorNombreAlias = new javax.swing.JButton();
        jButtonAntepasados = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldTitulo = new javax.swing.JTextField();
        jButtonBuscarPorTitulo = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jListLordsEncontradosPorTitulo = new javax.swing.JList<>();
        jLabel4 = new javax.swing.JLabel();
        jSpinnerNroGeneracion = new javax.swing.JSpinner();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextAreaIntegrantesGeneracion = new javax.swing.JTextArea();
        jButtonBuscarGeneracion = new javax.swing.JButton();
        jButtonReset = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItemCargarArchivo = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItemSalir = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });

        jLabelTituloVentanaPrincipal.setFont(new java.awt.Font("Papyrus", 1, 36)); // NOI18N
        jLabelTituloVentanaPrincipal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTituloVentanaPrincipal.setText("Título");
        jLabelTituloVentanaPrincipal.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabelTituloVentanaPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 890, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabelTituloVentanaPrincipal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout jPanelTreeLayout = new javax.swing.GroupLayout(jPanelTree);
        jPanelTree.setLayout(jPanelTreeLayout);
        jPanelTreeLayout.setHorizontalGroup(
            jPanelTreeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 908, Short.MAX_VALUE)
        );
        jPanelTreeLayout.setVerticalGroup(
            jPanelTreeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 658, Short.MAX_VALUE)
        );

        jLabel1.setText("Nombre / Mote:");

        jListLordsEncontradosPorNombre.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jListLordsEncontradosPorNombreMouseClicked(evt);
            }
        });
        jListLordsEncontradosPorNombre.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListLordsEncontradosPorNombreValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jListLordsEncontradosPorNombre);

        jLabel2.setText("Seleccionar uno para mostrar antepasados");

        jButtonBuscarPorNombreAlias.setText("Buscar");
        jButtonBuscarPorNombreAlias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBuscarPorNombreAliasActionPerformed(evt);
            }
        });

        jButtonAntepasados.setText("Antepasados");
        jButtonAntepasados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAntepasadosActionPerformed(evt);
            }
        });

        jLabel3.setText("Título:");

        jButtonBuscarPorTitulo.setText("Buscar");
        jButtonBuscarPorTitulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBuscarPorTituloActionPerformed(evt);
            }
        });

        jListLordsEncontradosPorTitulo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jListLordsEncontradosPorTituloMouseClicked(evt);
            }
        });
        jListLordsEncontradosPorTitulo.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListLordsEncontradosPorTituloValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(jListLordsEncontradosPorTitulo);

        jLabel4.setText("Generación Nro:");

        jTextAreaIntegrantesGeneracion.setEditable(false);
        jTextAreaIntegrantesGeneracion.setColumns(20);
        jTextAreaIntegrantesGeneracion.setRows(5);
        jScrollPane3.setViewportView(jTextAreaIntegrantesGeneracion);

        jButtonBuscarGeneracion.setText("Buscar");
        jButtonBuscarGeneracion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBuscarGeneracionActionPerformed(evt);
            }
        });

        jButtonReset.setText("Reset");
        jButtonReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonResetActionPerformed(evt);
            }
        });

        jMenu1.setText("Archivo");

        jMenuItemCargarArchivo.setText("Cargar Archivo");
        jMenuItemCargarArchivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCargarArchivoActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemCargarArchivo);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Salir");

        jMenuItemSalir.setText("Salir");
        jMenuItemSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSalirActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItemSalir);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldNombre)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonBuscarPorNombreAlias))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(34, 34, 34)
                        .addComponent(jButtonAntepasados, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 442, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonBuscarPorTitulo))
                    .addComponent(jScrollPane2)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(3, 3, 3)
                        .addComponent(jSpinnerNroGeneracion, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonBuscarGeneracion)
                        .addGap(52, 52, 52))
                    .addComponent(jScrollPane3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addComponent(jButtonReset)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanelTree, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanelTree, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButtonReset)
                        .addGap(39, 39, 39))))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextFieldNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonBuscarPorNombreAlias))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jButtonAntepasados))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextFieldTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonBuscarPorTitulo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jSpinnerNroGeneracion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonBuscarGeneracion))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItemSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSalirActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItemSalirActionPerformed

    private void jMenuItemCargarArchivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCargarArchivoActionPerformed
        this.resetWindows();
        VentanaSeleccionarArchivo ventanaSeleccionarArchivo = new VentanaSeleccionarArchivo(this.mainClass, this);
        ventanaSeleccionarArchivo.setVisible(true);
    }//GEN-LAST:event_jMenuItemCargarArchivoActionPerformed

    private void jButtonBuscarPorNombreAliasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBuscarPorNombreAliasActionPerformed
        String[] arregloVacio = new String[0];
        this.jListLordsEncontradosPorNombre.clearSelection();
        this.jListLordsEncontradosPorNombre.setListData(arregloVacio);
        if(this.jTextFieldNombre.getText().isEmpty()){
            return;
        }
        Lord lord = new Lord();
        lord.uniqueName = this.jTextFieldNombre.getText();
        lord.alias = this.jTextFieldNombre.getText();
        this.appController.houseTree.setComparador(Lord.comparadorUnicoNombreComienzaPor);
        LinkedList<ITree<Lord>> porNombre = this.appController.houseTree.buscar(lord);
        this.appController.houseTree.setComparador(Lord.comparadorAliasCompienzaPor);
        LinkedList<ITree<Lord>> porAlias = this.appController.houseTree.buscar(lord);
        LinkedList<ITree<Lord>> porNombreAlias = new LinkedList<>();
        if(!porNombre.vacia()){
            porNombreAlias = porNombre;
        }
        if(!porNombreAlias.vacia() && !porAlias.vacia()){
            for(int i = 0; i < porAlias.size(); i++){
                boolean encontrado = false;
                for(int j = 0; j < porNombreAlias.size(); j++){
                    if(porAlias.get(i) == porNombreAlias.get(j)){
                        encontrado = true;
                        break;
                    }   
                }
                if(encontrado == false){
                    porNombreAlias.agregar(porAlias.get(i));
                }
            }
        } else if(porNombreAlias.vacia() && !porAlias.vacia()){
            porNombreAlias = porAlias;
        }
        
        String[] arreglo = new String[porNombreAlias.size()];
        for(int i = 0; i < porNombreAlias.size(); i++){
            String nombre = porNombreAlias.get(i).getValor().uniqueName;
            if(nombre == null){
                nombre = "";
            }
            String alias = porNombreAlias.get(i).getValor().alias;
            if(alias == null){
                alias = "";
            }
            String nombreAlias = nombre + ": " + alias;
            arreglo[i] = nombreAlias;
        }
        this.jListLordsEncontradosPorNombre.setListData(arreglo);
    }//GEN-LAST:event_jButtonBuscarPorNombreAliasActionPerformed

    private void jButtonBuscarGeneracionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBuscarGeneracionActionPerformed
        int numGeneracion = (int) this.jSpinnerNroGeneracion.getValue();
        LinkedList<ITree<Lord>> porGeneracion = this.appController.houseTree.getNivel(numGeneracion);
        this.jTextAreaIntegrantesGeneracion.setText("");
        for(int i = 0; i < porGeneracion.size(); i++){
            Lord lord = porGeneracion.get(i).getValor();
            String uniqueName = lord.uniqueName;
            if(uniqueName == null){
                uniqueName = "";
            }
            String alias = lord.alias;
            if(alias == null){
                alias = "";
            }
            String nombreAlias = uniqueName + ": " + alias;
            this.jTextAreaIntegrantesGeneracion.append(nombreAlias + "\n");
        }
    }//GEN-LAST:event_jButtonBuscarGeneracionActionPerformed

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
        this.elementsCheckEnable();
        this.setUpSpinner();
    }//GEN-LAST:event_formWindowGainedFocus

    private void jButtonResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonResetActionPerformed
        this.resetWindows();
    }//GEN-LAST:event_jButtonResetActionPerformed

    private void jListLordsEncontradosPorNombreValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListLordsEncontradosPorNombreValueChanged
        String lord = this.jListLordsEncontradosPorNombre.getSelectedValue();
        if(lord == null || lord.isEmpty()){
            return;
        }
        if(this.selectedLordByName != null && lord.equals(this.selectedLordByName)){
            return;
        }
        this.selectedLordByName = lord;
        this.selectedLordByNameShowed = true;
        Tree<Lord> lordTree = this.getVentanaDetalleLord(lord);
        this.appController.loadTreeGraph(lordTree);
        this.elementsCheckEnable();
    }//GEN-LAST:event_jListLordsEncontradosPorNombreValueChanged

    private void jButtonAntepasadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAntepasadosActionPerformed
        if(this.selectedLordByName == null){
            return;
        }
        String[] lordUniqueNameAndAlias = this.selectedLordByName.split(":");
        String lordUniqueName = lordUniqueNameAndAlias[0].strip();
        String lordAlias = null;
        if (lordUniqueNameAndAlias.length > 1) {
            lordAlias = lordUniqueNameAndAlias[1].strip();
        }
        Tree<Lord> lordTree = null;
        if (lordAlias != null && !lordAlias.isEmpty()) {
            lordTree = this.appController.getHashTableAlias().buscar(lordAlias);
        }   
        if (lordTree == null && lordUniqueName != null && !lordUniqueName.isEmpty()) {
            lordTree = this.appController.getHashTableUniqueName().buscar(lordUniqueName);
        }
        if(lordTree == null){
            JOptionPane.showMessageDialog(this, "No se consiguio el lord : " + this.selectedLordByName + ", ni por el Alias ni por el Nombre Unico");
            return;
        }
        this.appController.loadAntepasadosGraph(lordTree);
        
        
        
    }//GEN-LAST:event_jButtonAntepasadosActionPerformed

    private void jListLordsEncontradosPorNombreMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListLordsEncontradosPorNombreMouseClicked
       if(this.selectedLordByName == null || this.selectedLordByName.isEmpty()){
           return;
       }
       if(this.selectedLordByNameShowed){
           this.selectedLordByNameShowed = false;
           return;
       }
       this.getVentanaDetalleLord(selectedLordByName);
       this.elementsCheckEnable();
    }//GEN-LAST:event_jListLordsEncontradosPorNombreMouseClicked

    private void jButtonBuscarPorTituloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBuscarPorTituloActionPerformed
        String[] arregloVacio = new String[0];
        this.jListLordsEncontradosPorTitulo.clearSelection();
        this.jListLordsEncontradosPorTitulo.setListData(arregloVacio);
        if(this.jTextFieldTitulo.getText().isEmpty()){
            return;
        }
        Lord lord = new Lord();
        lord.title = this.jTextFieldTitulo.getText();
        this.appController.houseTree.setComparador(Lord.comparadorTitleComienzaPor);
        LinkedList<ITree<Lord>> porTitulo = this.appController.houseTree.buscar(lord);
        
        String[] arreglo = new String[porTitulo.size()];
        for(int i = 0; i < porTitulo.size(); i++){
            String nombre = porTitulo.get(i).getValor().uniqueName;
            if(nombre == null){
                nombre = "";
            }
            String alias = porTitulo.get(i).getValor().alias;
            if(alias == null){
                alias = "";
            }
            String nombreAlias = nombre + ": " + alias;
            arreglo[i] = nombreAlias;
        }
        this.jListLordsEncontradosPorTitulo.setListData(arreglo);
    }//GEN-LAST:event_jButtonBuscarPorTituloActionPerformed

    private void jListLordsEncontradosPorTituloValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListLordsEncontradosPorTituloValueChanged
        String lord = this.jListLordsEncontradosPorTitulo.getSelectedValue();
        if(lord == null || lord.isEmpty()){
            return;
        }
        if(this.selectedLordByTitle != null && lord.equals(this.selectedLordByTitle)){
            return;
        }
        this.selectedLordByTitle = lord;
        this.selectedLordByTitleShowed = true;
        this.getVentanaDetalleLord(lord);
        this.elementsCheckEnable();
    }//GEN-LAST:event_jListLordsEncontradosPorTituloValueChanged

    private void jListLordsEncontradosPorTituloMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListLordsEncontradosPorTituloMouseClicked
        if(this.selectedLordByTitle == null || this.selectedLordByTitle.isEmpty()){
           return;
       }
       if(this.selectedLordByTitleShowed){
           this.selectedLordByTitleShowed = false;
           return;
       }
       this.getVentanaDetalleLord(selectedLordByTitle);
       this.elementsCheckEnable();
    }//GEN-LAST:event_jListLordsEncontradosPorTituloMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAntepasados;
    private javax.swing.JButton jButtonBuscarGeneracion;
    private javax.swing.JButton jButtonBuscarPorNombreAlias;
    private javax.swing.JButton jButtonBuscarPorTitulo;
    private javax.swing.JButton jButtonReset;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelTituloVentanaPrincipal;
    private javax.swing.JList<String> jListLordsEncontradosPorNombre;
    private javax.swing.JList<String> jListLordsEncontradosPorTitulo;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItemCargarArchivo;
    private javax.swing.JMenuItem jMenuItemSalir;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelTree;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSpinner jSpinnerNroGeneracion;
    private javax.swing.JTextArea jTextAreaIntegrantesGeneracion;
    private javax.swing.JTextField jTextFieldNombre;
    private javax.swing.JTextField jTextFieldTitulo;
    // End of variables declaration//GEN-END:variables
}
