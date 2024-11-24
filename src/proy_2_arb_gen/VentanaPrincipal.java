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
import org.graphstream.ui.view.ViewerPipe;
import org.graphstream.ui.view.ViewerListener;

/**
 * Venatana principal en la cual se va a mostrar toda la ejecución del programa
 * y se va a manejar todas las acciones de la interfaz gráfica.
 */

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

    /**
     * Constructor de VentanaPrincipal
     */
    public VentanaPrincipal() {
        initComponents();
    }

    /**
     * Constructor de VentanaPrincipal cuando se le pasa el main del programa
     * 
     * @param mainClass la clase main del programa
     */

    public VentanaPrincipal(Proy_2_Arb_Gen mainClass) {
        System.setProperty("org.graphstream.ui", "swing");
        setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Árbol Genealógico de las Casas");
        this.mainClass = mainClass;
        this.appController = this.mainClass.appController;
        this.graph = this.appController.graph;

        initComponents();
        setLayout(new BorderLayout());
        jPanelTree.setLayout(new BorderLayout());
        jPanelTree.setPreferredSize(new Dimension(1000, 1000));
        jPanelTitle.setLayout(new BorderLayout());
        jPanelControlArea.setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1400, 1200));
        this.setJLabelTitle("House");
        this.setUpGraphView();
    }

    /**
     * Metodo para configurar la vista gráfica
     */

    private void setUpGraphView() {
        viewer = (SwingViewer) new SwingViewer(graph, SwingViewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        viewer.disableAutoLayout();
        view = viewer.addDefaultView(false);
        jPanelTree.add((Component) view, BorderLayout.CENTER);
        ((Component) view).addMouseMotionListener(this);
        managingMouse();
    }

    /**
     * Metodo para manejar los eventos del mouse
     */

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

    /**
     * Metodo para cerrar la ventana
     * 
     * @param viewName el nombre de la ventana
     */

    @Override
    public void viewClosed(String viewName) {
        loop = false;
    }

    /**
     * Metodo para manejar los eventos de un nodo o lord del arbol cuando se
     * presiona
     * 
     * @param id el id del nodo o lord
     */

    @Override
    public void buttonPushed(String id) {
        // System.out.println("Mouse presionado nodo: " + id);
        Node node = graph.getNode(id);
        if (node != null) {
            this.selectedNode = node;
        }
    }

    /**
     * Metodo para manejar los eventos de un nodo o lord del arbol
     * cuando se mantiene presionado.
     * 
     * @param id el id del nodo o lord
     */

    @Override
    public void buttonReleased(String id) {
        // System.out.println("Mouse soltado nodo: " + id);
        Node node = graph.getNode(id);
        if (node != null) {
            if (this.selectedNode != null) {
                if (this.draggedNode) {
                    this.draggedNode = false;
                    return;
                }
                // Aquí tengo que buscar el lord y mostrar sus detalles
                this.getVentanaDetalleLord(id);
            }
        }
        this.draggedNode = false;
    }

    @Override
    public void mouseOver(String id) {
        // Manejar el evento de mouse sobre un nodo
        // Se deja vacio para cumplir con la interfaz
    }

    @Override
    public void mouseLeft(String id) {
        // Manejar el evento de mouse dejando un nodo
        // Se deja vacio para cumplir con la interfaz
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // Manejar el arrastre del mouse sobre un nodo
        // System.out.println("Mouse arrastrado");
        if (this.selectedNode != null) {
            this.draggedNode = true;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Manejar el movimiento del mouse sobre un nodo
        // Se deja vacio para cumplir con la interfaz
    }

    /**
     * Metodo para configurar el titulo de la ventana
     * 
     * @param titleString el titulo de la ventana
     */

    public void setJLabelTitle(String titleString) {
        this.jLabelTituloVentanaPrincipal.setText(titleString);
        this.jLabelTituloVentanaPrincipal.setVisible(true);
    }

    /**
     * Metodo para resetear la ventana.
     */

    public void resetWindow() {
        if (this.appController.houseTree == null || this.appController.houseTree.vacio()) {
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
        // hay que cambiar el valor min y max
        this.jTextAreaIntegrantesGeneracion.setText("");
        this.appController.loadTreeGraph((ITree<Lord>) this.appController.houseTree);
        elementsCheckEnabled();
    }

    /**
     * Metodo para habilitar o desahabilitar los elementos de la ventana
     */

    public void elementsCheckEnabled() {
        if (this.appController.houseTree == null || this.appController.houseTree.vacio()) {
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
        if (jListLordsEncontradosPorNombre.getSelectedValue() != null) {
            this.jButtonAntepasados.setEnabled(true);
        } else {
            this.jButtonAntepasados.setEnabled(false);
        }
    }

    /**
     * Metodo para configurar el spinner
     */

    private void setUpSpinner() {
        if (this.appController.houseTree != null && !this.appController.houseTree.vacio()) {
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

    /**
     * Metodo para mostrar la ventana de detalle de un Lord
     * 
     * @param lordString el nombre o alias del Lord
     * @return el arbol del lord que se busca
     */

    public Tree<Lord> getVentanaDetalleLord(String lordString) {
        String[] lordUniqueNameAndAlias = lordString.split(":");
        String lordUniqueName = lordUniqueNameAndAlias[0].strip();
        String lordAlias = null;
        if (lordUniqueNameAndAlias.length > 1) {
            lordAlias = lordUniqueNameAndAlias[1].strip();
        }

        Tree<Lord> lordTree = null;
        if (lordAlias != null && !lordAlias.isEmpty()) {
            // Buscamos en el hashTable por Alias
            lordTree = this.appController.getHashTableAlias().buscar(lordAlias);
        }
        if (lordTree == null && lordUniqueName != null && !lordUniqueName.isEmpty()) {
            // Buscamos en el hashTable por Nombre Unico
            lordTree = this.appController.getHashTableUniqueName().buscar(lordUniqueName);
        }
        if (lordTree == null) {
            JOptionPane.showMessageDialog(this,
                    "No se consiguió el lord: " + lordString + " por Alias o por Nombre Único!");
            return null;
        }
        VentanaDetalleLord ventanaDetalleLord = new VentanaDetalleLord(this, lordTree.getValor());
        ventanaDetalleLord.setVisible(true);
        return lordTree;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated

    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelTree = new javax.swing.JPanel();
        jPanelTitle = new javax.swing.JPanel();
        jLabelTituloVentanaPrincipal = new javax.swing.JLabel();
        jPanelControlArea = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldNombre = new javax.swing.JTextField();
        jButtonBuscarPorNombreAlias = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListLordsEncontradosPorNombre = new javax.swing.JList<>();
        jButtonAntepasados = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldTitulo = new javax.swing.JTextField();
        jButtonBuscarPorTitulo = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jListLordsEncontradosPorTitulo = new javax.swing.JList<>();
        jSpinnerNroGeneracion = new javax.swing.JSpinner();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextAreaIntegrantesGeneracion = new javax.swing.JTextArea();
        jButtonReset = new javax.swing.JButton();
        jButtonBuscarGeneracion = new javax.swing.JButton();
        jMenuBarVentanaPrincipal = new javax.swing.JMenuBar();
        jMenuArchivoVentanaPrincipal = new javax.swing.JMenu();
        jMenuItemSeleccionarArchivo = new javax.swing.JMenuItem();
        jMenu1SalirVentanaPrincipal = new javax.swing.JMenu();
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

        javax.swing.GroupLayout jPanelTreeLayout = new javax.swing.GroupLayout(jPanelTree);
        jPanelTree.setLayout(jPanelTreeLayout);
        jPanelTreeLayout.setHorizontalGroup(
            jPanelTreeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelTreeLayout.setVerticalGroup(
            jPanelTreeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 684, Short.MAX_VALUE)
        );

        jLabelTituloVentanaPrincipal.setFont(new java.awt.Font("Papyrus", 1, 36)); // NOI18N
        jLabelTituloVentanaPrincipal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTituloVentanaPrincipal.setText("Titulo");
        jLabelTituloVentanaPrincipal.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanelTitleLayout = new javax.swing.GroupLayout(jPanelTitle);
        jPanelTitle.setLayout(jPanelTitleLayout);
        jPanelTitleLayout.setHorizontalGroup(
            jPanelTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTitleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTituloVentanaPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 1092, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelTitleLayout.setVerticalGroup(
            jPanelTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelTitleLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelTituloVentanaPrincipal)
                .addContainerGap())
        );

        jLabel1.setText("Nombre / Mote:");

        jTextFieldNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldNombreActionPerformed(evt);
            }
        });

        jButtonBuscarPorNombreAlias.setText("Buscar");
        jButtonBuscarPorNombreAlias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBuscarPorNombreAliasActionPerformed(evt);
            }
        });

        jListLordsEncontradosPorNombre.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
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

        jButtonAntepasados.setText("Antepasados");
        jButtonAntepasados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAntepasadosActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        jLabel3.setText("Seleccionar uno para mostrar antepasados.");

        jLabel4.setText("Título:");

        jButtonBuscarPorTitulo.setText("Buscar");
        jButtonBuscarPorTitulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBuscarPorTituloActionPerformed(evt);
            }
        });

        jListLordsEncontradosPorTitulo.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
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

        jLabel6.setText("Generación nro:");

        jTextAreaIntegrantesGeneracion.setEditable(false);
        jTextAreaIntegrantesGeneracion.setColumns(20);
        jTextAreaIntegrantesGeneracion.setRows(5);
        jScrollPane3.setViewportView(jTextAreaIntegrantesGeneracion);

        jButtonReset.setText("Reset");
        jButtonReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonResetActionPerformed(evt);
            }
        });

        jButtonBuscarGeneracion.setText("Buscar");
        jButtonBuscarGeneracion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBuscarGeneracionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelControlAreaLayout = new javax.swing.GroupLayout(jPanelControlArea);
        jPanelControlArea.setLayout(jPanelControlAreaLayout);
        jPanelControlAreaLayout.setHorizontalGroup(
            jPanelControlAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelControlAreaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelControlAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelControlAreaLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanelControlAreaLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addComponent(jButtonBuscarPorNombreAlias)
                        .addGap(138, 138, 138))
                    .addGroup(jPanelControlAreaLayout.createSequentialGroup()
                        .addGroup(jPanelControlAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelControlAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelControlAreaLayout.createSequentialGroup()
                                    .addComponent(jLabel4)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTextFieldTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButtonBuscarPorTitulo))
                                .addGroup(jPanelControlAreaLayout.createSequentialGroup()
                                    .addComponent(jLabel3)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButtonAntepasados))
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelControlAreaLayout.createSequentialGroup()
                                .addGroup(jPanelControlAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanelControlAreaLayout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jSpinnerNroGeneracion, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButtonBuscarGeneracion))
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jButtonReset)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanelControlAreaLayout.setVerticalGroup(
            jPanelControlAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelControlAreaLayout.createSequentialGroup()
                .addGroup(jPanelControlAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonReset)
                    .addGroup(jPanelControlAreaLayout.createSequentialGroup()
                        .addGroup(jPanelControlAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel1)
                            .addComponent(jTextFieldNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonBuscarPorNombreAlias))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelControlAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jButtonAntepasados))
                        .addGap(18, 18, 18)
                        .addGroup(jPanelControlAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonBuscarPorTitulo)
                            .addGroup(jPanelControlAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel4)
                                .addComponent(jTextFieldTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanelControlAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jSpinnerNroGeneracion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonBuscarGeneracion))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelControlAreaLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jScrollPane1, jScrollPane2, jScrollPane3});

        jMenuArchivoVentanaPrincipal.setText("Archivo");

        jMenuItemSeleccionarArchivo.setText("Seleccionar Archivo");
        jMenuItemSeleccionarArchivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSeleccionarArchivoActionPerformed(evt);
            }
        });
        jMenuArchivoVentanaPrincipal.add(jMenuItemSeleccionarArchivo);

        jMenuBarVentanaPrincipal.add(jMenuArchivoVentanaPrincipal);

        jMenu1SalirVentanaPrincipal.setText("Salir");

        jMenuItemSalir.setText("Salir de la App");
        jMenuItemSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSalirActionPerformed(evt);
            }
        });
        jMenu1SalirVentanaPrincipal.add(jMenuItemSalir);

        jMenuBarVentanaPrincipal.add(jMenu1SalirVentanaPrincipal);

        setJMenuBar(jMenuBarVentanaPrincipal);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelControlArea, javax.swing.GroupLayout.PREFERRED_SIZE, 461, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanelTree, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanelTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanelTree, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanelControlArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jListLordsEncontradosPorTituloMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jListLordsEncontradosPorTituloMouseClicked
        if (this.selectedLordByTitle == null || this.selectedLordByTitle.isEmpty()) {
            return;
        }
        if (this.selectedLordByTitleShowed) {
            this.selectedLordByTitleShowed = false;
            return;
        }
        this.getVentanaDetalleLord(this.selectedLordByTitle);
        this.elementsCheckEnabled();
    }// GEN-LAST:event_jListLordsEncontradosPorTituloMouseClicked

    private void jListLordsEncontradosPorNombreMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jListLordsEncontradosPorNombreMouseClicked
        if (this.selectedLordByName == null || this.selectedLordByName.isEmpty()) {
            return;
        }
        if (this.selectedLordByNameShowed) {
            this.selectedLordByNameShowed = false;
            return;
        }
        Tree<Lord> lordTree = this.getVentanaDetalleLord(this.selectedLordByName);
        this.appController.loadTreeGraph((ITree<Lord>) lordTree);
        this.elementsCheckEnabled();
    }// GEN-LAST:event_jListLordsEncontradosPorNombreMouseClicked

    private void jListLordsEncontradosPorTituloValueChanged(javax.swing.event.ListSelectionEvent evt) {// GEN-FIRST:event_jListLordsEncontradosPorTituloValueChanged
        String lord = jListLordsEncontradosPorTitulo.getSelectedValue();
        if (lord == null || lord.isEmpty()) {
            return;
        }
        if (this.selectedLordByTitle != null && this.selectedLordByTitle.equals(lord)) {
            if (this.selectedLordByTitleShowed) {
                return;
            }
        }
        this.selectedLordByTitle = lord;
        this.selectedLordByTitleShowed = true;
        this.getVentanaDetalleLord(this.selectedLordByTitle);
        this.elementsCheckEnabled();
    }// GEN-LAST:event_jListLordsEncontradosPorTituloValueChanged

    private void jButtonAntepasadosActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonAntepasadosActionPerformed
        if (this.selectedLordByName == null) {
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
            // Buscamos en el hashTable por Alias
            lordTree = this.appController.getHashTableAlias().buscar(lordAlias);
        }
        if (lordTree == null && lordUniqueName != null && !lordUniqueName.isEmpty()) {
            // Buscamos en el hashTable por Nombre Unico
            lordTree = this.appController.getHashTableUniqueName().buscar(lordUniqueName);
        }
        if (lordTree == null) {
            JOptionPane.showMessageDialog(this,
                    "No se consiguió el lord: " + this.selectedLordByName + " por Alias o por Nombre Único!");
            return;
        }
        this.appController.loadAntepasadosGraph(lordTree);
    }// GEN-LAST:event_jButtonAntepasadosActionPerformed

    private void jListLordsEncontradosPorNombreValueChanged(javax.swing.event.ListSelectionEvent evt) {// GEN-FIRST:event_jListLordsEncontradosPorNombreValueChanged

        String lord = jListLordsEncontradosPorNombre.getSelectedValue();
        if (lord == null || lord.isEmpty()) {
            return;
        }
        if (this.selectedLordByName != null && this.selectedLordByName.equals(lord)) {
            if (this.selectedLordByNameShowed) {
                return;
            }
        }
        this.selectedLordByName = lord;
        this.selectedLordByNameShowed = true;
        Tree<Lord> lordTree = this.getVentanaDetalleLord(this.selectedLordByName);
        this.appController.loadTreeGraph((ITree<Lord>) lordTree);
        this.elementsCheckEnabled();

    }// GEN-LAST:event_jListLordsEncontradosPorNombreValueChanged

    private void jButtonBuscarPorNombreAliasActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonBuscarPorNombreAliasActionPerformed
        String[] arregloVacio = new String[0];
        this.jListLordsEncontradosPorNombre.clearSelection();
        this.jListLordsEncontradosPorNombre.setListData(arregloVacio);

        if (this.jTextFieldNombre.getText().isEmpty()) {
            return;
        }

        Lord lord = new Lord();

        lord.uniqueName = this.jTextFieldNombre.getText();
        lord.alias = this.jTextFieldNombre.getText();

        this.appController.houseTree.setComparador(Lord.comparadorUnicoNombreComienzaPor);

        LinkedList<ITree<Lord>> porNombre = this.appController.houseTree.buscar(lord);

        this.appController.houseTree.setComparador(Lord.comparadorAliasComienzaPor);
        LinkedList<ITree<Lord>> porAlias = this.appController.houseTree.buscar(lord);

        LinkedList<ITree<Lord>> porNombreAlias = new LinkedList<ITree<Lord>>();
        if (!porNombre.vacia()) {
            porNombreAlias = porNombre;
        }
        if (!porNombreAlias.vacia() && !porAlias.vacia()) {
            for (int i = 0; i < porAlias.size(); i++) {
                boolean encontrado = false;
                for (int j = 0; j < porNombreAlias.size(); j++) {
                    if (porAlias.get(i) == porNombreAlias.get(j)) {
                        encontrado = true;
                        break;
                    }
                }
                if (!encontrado) {
                    porNombreAlias.agregar(porAlias.get(i));
                }
            }
        } else if (porNombreAlias.vacia() && !porAlias.vacia()) {
            porNombreAlias = porAlias;
        }

        String[] arreglo = new String[porNombreAlias.size()];
        for (int i = 0; i < porNombreAlias.size(); i++) {
            String nombre = porNombreAlias.get(i).getValor().uniqueName;
            if (nombre == null) {
                nombre = "";
            }
            String nombreAlias = nombre;
            String alias = porNombreAlias.get(i).getValor().alias;
            if (alias != null) {
                nombreAlias += ": " + alias;
            }

            arreglo[i] = nombreAlias;
        }
        this.jListLordsEncontradosPorNombre.setListData(arreglo);

    }// GEN-LAST:event_jButtonBuscarPorNombreAliasActionPerformed

    private void jTextFieldNombreActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jTextFieldNombreActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_jTextFieldNombreActionPerformed

    private void jButtonBuscarPorTituloActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonBuscarPorTituloActionPerformed
        String[] arregloVacio = new String[0];
        this.jListLordsEncontradosPorTitulo.clearSelection();
        this.jListLordsEncontradosPorTitulo.setListData(arregloVacio);

        if (this.jTextFieldTitulo.getText().isEmpty()) {
            return;
        }

        Lord lord = new Lord();
        lord.title = this.jTextFieldTitulo.getText();

        this.appController.houseTree.setComparador(Lord.comparadorTitleComienzaPor);
        LinkedList<ITree<Lord>> porTitulo = this.appController.houseTree.buscar(lord);

        String[] arreglo = new String[porTitulo.size()];
        for (int i = 0; i < porTitulo.size(); i++) {
            String nombre = porTitulo.get(i).getValor().uniqueName;
            if (nombre == null) {
                nombre = "";
            }
            String nombreAlias = nombre;
            String alias = porTitulo.get(i).getValor().alias;
            if (alias != null) {
                nombreAlias += ": " + alias;
            }

            arreglo[i] = nombreAlias;
        }
        this.jListLordsEncontradosPorTitulo.setListData(arreglo);
    }// GEN-LAST:event_jButtonBuscarPorTituloActionPerformed

    private void jButtonBuscarGeneracionActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonBuscarGeneracionActionPerformed
        int numGeneracion = (int) this.jSpinnerNroGeneracion.getValue();
        LinkedList<ITree<Lord>> porGeneracion = this.appController.houseTree.getNivel(numGeneracion);
        this.jTextAreaIntegrantesGeneracion.setText("");
        for (int i = 0; i < porGeneracion.size(); i++) {
            Lord lord = porGeneracion.get(i).getValor();
            String uniqueName = lord.uniqueName;
            if (uniqueName == null) {
                uniqueName = "";
            }
            String nombreAlias = uniqueName;
            String alias = lord.alias;
            if (alias != null) {
                nombreAlias += ": " + alias;
            }

            this.jTextAreaIntegrantesGeneracion.append(nombreAlias + "\n");
        }
    }// GEN-LAST:event_jButtonBuscarGeneracionActionPerformed

    private void jButtonResetActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonResetActionPerformed
        this.resetWindow();
    }// GEN-LAST:event_jButtonResetActionPerformed

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {// GEN-FIRST:event_formWindowGainedFocus
        this.elementsCheckEnabled();
        this.setUpSpinner();
    }// GEN-LAST:event_formWindowGainedFocus

    private void jMenuItemSalirActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemSalirActionPerformed
        System.exit(0);
    }// GEN-LAST:event_jMenuItemSalirActionPerformed

    private void jMenuItemSeleccionarArchivoActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemSeleccionarArchivoActionPerformed
        this.resetWindow();
        VentanaSeleccionarArchivo ventanaSeleccionarArchivo = new VentanaSeleccionarArchivo(mainClass, this);
        ventanaSeleccionarArchivo.setVisible(true);
    }// GEN-LAST:event_jMenuItemSeleccionarArchivoActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
        // (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default
         * look and feel.
         * For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE,
                    null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE,
                    null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE,
                    null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE,
                    null, ex);
        }
        // </editor-fold>
        // </editor-fold>

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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabelTituloVentanaPrincipal;
    private javax.swing.JList<String> jListLordsEncontradosPorNombre;
    private javax.swing.JList<String> jListLordsEncontradosPorTitulo;
    private javax.swing.JMenu jMenu1SalirVentanaPrincipal;
    private javax.swing.JMenu jMenuArchivoVentanaPrincipal;
    private javax.swing.JMenuBar jMenuBarVentanaPrincipal;
    private javax.swing.JMenuItem jMenuItemSalir;
    private javax.swing.JMenuItem jMenuItemSeleccionarArchivo;
    private javax.swing.JPanel jPanelControlArea;
    private javax.swing.JPanel jPanelTitle;
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
