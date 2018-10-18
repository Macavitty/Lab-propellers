package lab_4.server;

import lab_4.PropellerCollection;
import lab_4.orm.DBManager;
import lab_4.story_components.Karlson;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Map;
import java.util.Scanner;

class ServerWindow extends JFrame {

    private ServerWindow thisWindow = this;

    private JPanel treePanel;

    private JButton removeGreaterKeyButton,
            addIfMaxButton,
            toJsonButton,
            removeButton,
            addButton, editButton;

    private JTree propellersTree;
    private DefaultMutableTreeNode root;
    private DefaultTreeModel treeModel;

    private JMenuBar menuBar;
    private boolean isLoggedIn = false;

    private PropellerCollection collection;
    private AuthorizationForm loginForm;

    // ------ for edit field ------
    private JPanel editPanel, buttonsPanel;
    private JTextField modelTextField = new JTextField(),
            fansTextField = new JTextField(),
            sizeTextField = new JTextField(),
            speedTextField = new JTextField(),
            maxWeightTextField = new JTextField();
    private JLabel modelL = new JLabel("Model: "),
            fansL = new JLabel("Fans: "),
            colorL = new JLabel("Color: "),
            speedL = new JLabel("Speed: "),
            yearL = new JLabel("Year: "),
            maxWL = new JLabel("Max weight: "),
            sizeL = new JLabel("Size: ");
    private JSpinner yearSpinner;
    private JComboBox<String> colorBox;

    private JSlider sizeSlider = new JSlider(),
            speedSlider = new JSlider(),
            maxWeightSlider = new JSlider();
    private boolean condition;
    private Calendar calendar;
    private Dimension fieldsDimention = new Dimension(200, 10);

    private DBManager dbManager;

    private String currModel = "";
    // ------------------------------

    public ServerWindow(PropellerCollection collection) {
        super("THIS IS SERVER");
        this.collection = collection;
        dbManager = new DBManager();
        dbManager.fromFileToDB(collection);
        setBounds(100, 10, 990, 500);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        createTreePanel();
        // ------ now work with edit field ------
        createEditfield();

        // stub
        JPanel stub = new JPanel();
        stub.setBackground(Color.decode("#FBCA96"));
        stub.add(new JLabel("\n\n\n\n\n\n"));
        stub.add(new JLabel("     "));
        add(stub, BorderLayout.NORTH);

        JPanel superMaintPanel = new JPanel();
        superMaintPanel.setBackground(Color.decode("#FBCA96"));
        GroupLayout layout = new GroupLayout(superMaintPanel);
        superMaintPanel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(stub))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(treePanel))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(editPanel))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(stub))));

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(stub)
                                .addComponent(treePanel)
                                .addComponent(editPanel)
                                .addComponent(stub))));

        createMenuBar();
        setJMenuBar(menuBar);
        add(superMaintPanel);
        setColors();
        setVisible(true);
    }

    private void createMenuBar() {

        // CLEAR
        JMenuItem menuItemClear = new JMenuItem("Clear collection");
        menuItemClear.addActionListener((ActionEvent event) -> {
            if (isLoggedIn) {
                if (JOptionPane.showConfirmDialog(this, "Do you really want to clear the collection ?", "",
                        JOptionPane.OK_CANCEL_OPTION) == JOptionPane.YES_OPTION) {

                    JOptionPane.showMessageDialog(this, collection.clear(), "Clearing result",
                            JOptionPane.PLAIN_MESSAGE);
                    dbManager.deleteAllRow(collection);
                }
                refreshPropellersTree();
            } else showFigy();
        });

        // SAVE
        JMenuItem menuItemSave = new JMenuItem("Save collection");
        menuItemSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        menuItemSave.addActionListener((ActionEvent event) -> {
            if (isLoggedIn) {
                JOptionPane.showMessageDialog(this, collection.save(System.getenv("whereArePropellers")), "Saving result", JOptionPane.PLAIN_MESSAGE);
            } else showFigy();
        });

        // LOAD
        JMenuItem menuItemLoad = new JMenuItem("Load collection");
        menuItemLoad.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        menuItemLoad.addActionListener((ActionEvent event) -> {
            if (isLoggedIn) {
                dbManager.deleteAllRow(collection);
                JOptionPane.showMessageDialog(this, collection.load(), "Loading result", JOptionPane.PLAIN_MESSAGE);
                dbManager.fromFileToDB(collection);
                refreshPropellersTree();
            } else showFigy();
        });

        // HELP
        JMenuItem menuItemHelp = new JMenuItem("Help");
        menuItemHelp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
        menuItemHelp.addActionListener((ActionEvent event) -> {
            JOptionPane.showMessageDialog(this, "Come on! \n It`s pretty easy \n (if you are logged in)", "Help",
                    JOptionPane.PLAIN_MESSAGE);
        });

        // EXIT
        JMenuItem menuItemExit = new JMenuItem("Exit");
        menuItemExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
        menuItemExit.addActionListener((ActionEvent event) -> {
            //collection.save(System.getenv("whereArePropellers"));
            System.exit(0);
        });

        // LOGIN
        JMenuItem menuItemLogin = new JMenuItem("Sing in");
        if (isLoggedIn) menuItemLogin.setEnabled(false);
        else menuItemLogin.setEnabled(true);
        menuItemLogin.addActionListener((ActionEvent event) -> {
            loginForm = new AuthorizationForm(thisWindow);
        });

        JMenu mainMenu = new JMenu("MENU");
        mainMenu.setMnemonic(KeyEvent.VK_M);
        JMenu collectionMenu = new JMenu("COLLECTION");

        collectionMenu.add(menuItemSave);
        collectionMenu.add(menuItemLoad);
        collectionMenu.add(menuItemClear);
        mainMenu.add(menuItemLogin);
        mainMenu.add(menuItemHelp);
        mainMenu.add(menuItemExit);

        menuBar = new JMenuBar();
        menuBar.setBorderPainted(true);
        menuBar.add(mainMenu);
        menuBar.add(collectionMenu);
    }

    private void createTreePanel() {
        JScrollPane treeScrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        treePanel = new JPanel();
        root = new DefaultMutableTreeNode("Propellers");
        // leaves
        String[] models = collection.getPropellerMap().keySet().stream().toArray(String[]::new);
        for (int i = 0; i < collection.getSize(); i++) {
            root.add(new DefaultMutableTreeNode(models[i]));
        }
        treeModel = new DefaultTreeModel(root);
        propellersTree = new JTree(treeModel);
        treeScrollPane.setViewportView(propellersTree);
        treePanel.add(treeScrollPane);
        //icon
        ImageIcon icon = new ImageIcon("/home/tania/IdeaProjects/untitled (копия)/src/lab_4/propp.png");
        Image image = icon.getImage();
        Image newimg = image.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
        icon = new ImageIcon(newimg);
        DefaultTreeCellRenderer render = new DefaultTreeCellRenderer();
        render.setLeafIcon(icon);
        propellersTree.setCellRenderer(render);
        propellersTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
                if (!modelTextField.getText().equals("")) {
                    editButton.setEnabled(true);
                    removeButton.setEnabled(true);
                    removeGreaterKeyButton.setEnabled(true);
                    toJsonButton.setEnabled(true);
                }
                if (propellersTree.getLastSelectedPathComponent() != null && root.getChildCount() != 0) {
                    Karlson.Propeller p = collection.getPropellerMap().get(propellersTree.getLastSelectedPathComponent().toString());
                    StringBuilder builder = new StringBuilder();
                    for (String value : p.getFans()) {
                        builder.append(value);
                    }
                    modelTextField.setText(p.getModel());
                    yearSpinner.setValue(p.getYear());
                    fansTextField.setText(builder.toString());
                    sizeSlider.setValue(p.getSize());
                    sizeTextField.setText(String.valueOf(p.getSize()));
                    speedSlider.setValue(p.getSpeed());
                    speedTextField.setText(String.valueOf(p.getSpeed()));
                    maxWeightSlider.setValue(p.getMaxWeight());
                    maxWeightTextField.setText(String.valueOf(p.getMaxWeight()));
                    colorBox.setSelectedItem(p.getColor());
                } else {
                    modelTextField.setText("");
                    yearSpinner.setValue(2018);
                    fansTextField.setText("");
                    sizeSlider.setValue(50);
                    sizeTextField.setText("50");
                    speedSlider.setValue(50);
                    speedTextField.setText("50");
                    maxWeightSlider.setValue(50);
                    maxWeightTextField.setText("50");
                    colorBox.setSelectedItem("red");
                }
                currModel = modelTextField.getText();
            }
        });
    }

    public void refreshPropellersTree() {
        String[] models = dbManager.getCollectionFromDB().getPropellerMap().keySet().stream().toArray(String[]::new);
        root.removeAllChildren();
        for (int i = 0; i < collection.getSize(); i++)
            root.add(new DefaultMutableTreeNode(models[i]));
        treeModel = new DefaultTreeModel(root);
        propellersTree.setModel(treeModel);
    }

    private void createEditfield() {
        editPanel = new JPanel();
        JPanel editFiltPanel = new JPanel();
        //editPanel.setBounds(100, 10, 400, 250);
        // border
        Border border = BorderFactory.createLineBorder(Color.decode("#A5AAAF"));
        editFiltPanel.setBorder(border);
        editFiltPanel.setBackground(Color.decode("#FBCA96"));
        organizeModel();
        setSlider(sizeTextField, sizeSlider, 1, 482);
        setSlider(speedTextField, speedSlider, 0, 600);
        setSlider(maxWeightTextField, maxWeightSlider, 1, 300);
        setSpinners();
        // colorBox
        String[] colors = {"red", "white", "black", "green", "blue", "silver",
                "purple", "maroon", "golden", "orange"};
        colorBox = new JComboBox<>(colors);
        colorBox.setMaximumSize(fieldsDimention);
        GroupLayout layout = new GroupLayout(editFiltPanel);
        fansTextField.setMaximumSize(fieldsDimention);
        organizeGroupLayout(layout);
        createButtonsPanel();
        editFiltPanel.setLayout(layout);
        editPanel.add(editFiltPanel, BorderLayout.NORTH);
        editPanel.add(buttonsPanel, BorderLayout.CENTER);
    }

    private void showFigy() {
        if (JOptionPane.showConfirmDialog(this, "Sorry, you are not authorized for this operation\n" +
                "Do you want to?", "PERMISSION DENIED", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.YES_OPTION) {
            loginForm = new AuthorizationForm(thisWindow);
        }

    }

    public void setLoggedIn(boolean b) {
        isLoggedIn = b;
    }

    private void setSpinners() {
        // yearSpinner
        calendar = Calendar.getInstance();
        final int currentYear = calendar.get(Calendar.YEAR);
        yearSpinner = new JSpinner(new SpinnerNumberModel(currentYear, currentYear - 300, currentYear, 1));
        yearSpinner.setEditor(new JSpinner.NumberEditor(yearSpinner, "#"));
        yearSpinner.setMaximumSize(fieldsDimention);
    }

    private void setSlider(JTextField text, JSlider slider, int min, int max) {
        text.setEnabled(false);
        text.setMaximumSize(new Dimension(60, 19));
        text.setMinimumSize(new Dimension(60, 19));
        text.setHorizontalAlignment(JTextField.CENTER);
        text.setText(Integer.toString(sizeSlider.getValue()));
        slider.setMaximum(max);
        slider.setMinimum(min);
        slider.setMaximumSize(fieldsDimention);
        slider.setBackground(Color.decode("#FBCA96"));
        slider.setForeground(Color.decode("#400165"));
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                text.setText(Integer.toString(slider.getValue()));
            }
        });
    }

    private void createButtonsPanel() {
        GridLayout grid = new GridLayout(2, 3);
        grid.setVgap(3);
        grid.setHgap(3);
        buttonsPanel = new JPanel(grid);
        removeGreaterKeyButton = new JButton("remove greater");
        addIfMaxButton = new JButton("add if max");
        toJsonButton = new JButton("to Json");
        removeButton = new JButton("remove");
        editButton = new JButton("edit");
        addButton = new JButton("add");
        addIfMaxButton.setEnabled(false);
        addButton.setEnabled(false);
        removeButton.setEnabled(false);
        removeGreaterKeyButton.setEnabled(false);
        toJsonButton.setEnabled(false);
        editButton.setEnabled(false);
        editButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (isLoggedIn) {
                    String propeller = toJson();
                    collection.remove(currModel);
                    dbManager.deleteRow(currModel);
                    JOptionPane.showMessageDialog(thisWindow, collection.add(propeller), "EDITING", JOptionPane.PLAIN_MESSAGE);
                    dbManager.insertProp(propeller);
                    refreshPropellersTree();
                } else showFigy();
            }
        });
        addButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (isLoggedIn) {
                    String propeller = toJson();
                    JOptionPane.showMessageDialog(thisWindow, collection.add(propeller), "ADDING", JOptionPane.PLAIN_MESSAGE);
                    dbManager.insertProp(propeller);
                    collection.save(System.getenv("whereArePropellers"));
                    refreshPropellersTree();
                } else showFigy();
            }
        });
        addIfMaxButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (isLoggedIn) {
                    String propeller = toJson();
                    JOptionPane.showMessageDialog(thisWindow, collection.addIfMax(propeller), "ADDING", JOptionPane.PLAIN_MESSAGE);
                    dbManager.insertProp(propeller);
                    refreshPropellersTree();
                } else showFigy();
            }
        });
        removeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (isLoggedIn) {
                    if (!propellersTree.isSelectionEmpty()) {
                        if (JOptionPane.showConfirmDialog(thisWindow, "Are you sure ?", "",
                                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.YES_OPTION) {
                            collection.remove(propellersTree.getLastSelectedPathComponent().toString());
                            dbManager.deleteRow(propellersTree.getLastSelectedPathComponent().toString());
                            refreshPropellersTree();
                        }
                    }
                } else showFigy();
            }
        });

        removeGreaterKeyButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (isLoggedIn) {
                    if (!propellersTree.isSelectionEmpty()) {
                        if (JOptionPane.showConfirmDialog(thisWindow, "Are you sure ?", "",
                                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.YES_OPTION) {
                            collection.removeGreaterKey(propellersTree.getLastSelectedPathComponent().toString());
                            dbManager.deleteRow(propellersTree.getLastSelectedPathComponent().toString());
                            refreshPropellersTree();
                        }
                    }
                } else showFigy();
            }
        });
        toJsonButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (isLoggedIn) {
                    if (!propellersTree.isSelectionEmpty()) {
                        JOptionPane.showMessageDialog(thisWindow,
                                collection.toJson(propellersTree.getLastSelectedPathComponent().toString()),
                                "JSon", JOptionPane.PLAIN_MESSAGE);
                    }
                } else showFigy();
            }
        });

        buttonsPanel.add(addButton);
        buttonsPanel.add(addIfMaxButton);
        buttonsPanel.add(editButton);
        buttonsPanel.add(removeButton);
        buttonsPanel.add(removeGreaterKeyButton);
        buttonsPanel.add(toJsonButton);
    }

    private String toJson() {
        return
                "{\"model\":\""
                        + modelTextField.getText()
                        + "\",\"year\":"
                        + yearSpinner.getValue()
                        + ",\"size\":"
                        + sizeTextField.getText()
                        + ",\"speed\":"
                        + speedTextField.getText()
                        + ",\"maxWeight\":"
                        + maxWeightTextField.getText()
                        + ",\"color\":\""
                        + colorBox.getSelectedItem()
                        + "\",\"fans\":[\""
                        + fansTextField.getText()
                        + "\"]}";
    }

    private void organizeModel() {
        MyFocusListener focusListener = new MyFocusListener();
        modelTextField = new JTextField();
        modelTextField.setMaximumSize(fieldsDimention);
        modelTextField.addCaretListener(new CaretListener() {
            public void caretUpdate(CaretEvent e) {
                Map<String, Karlson.Propeller> map = collection.getPropellerMap();
                if (map.keySet().stream().anyMatch(a -> a.compareTo(modelTextField.getText()) == 0)
                        || modelTextField.getText().equals("")) {
                    //modelTextField.setBackground(wrongInputColor);
                    addButton.setEnabled(false);
                    addIfMaxButton.setEnabled(false);
                    if (!modelTextField.getText().equals("")) {
                        removeButton.setEnabled(true);
                        removeGreaterKeyButton.setEnabled(true);
                        toJsonButton.setEnabled(true);
                        editButton.setEnabled(true);
                    }
                    modelTextField.addFocusListener(focusListener);
                } else {
                    //modelTextField.setBackground(correctInputColor);
                    addButton.setEnabled(true);
                    addIfMaxButton.setEnabled(true);
                    removeButton.setEnabled(false);
                    removeGreaterKeyButton.setEnabled(false);
                    modelTextField.removeFocusListener(focusListener);
                }
            }
        });
    }

    private void organizeGroupLayout(GroupLayout layout) {
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(modelL)
                        .addComponent(yearL)
                        .addComponent(fansL)
                        .addComponent(colorL)
                        .addComponent(sizeL)
                        .addComponent(speedL)
                        .addComponent(maxWL))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(modelTextField)
                        .addComponent(yearSpinner)
                        .addComponent(fansTextField)
                        .addComponent(colorBox)
                        .addComponent(sizeSlider)
                        .addComponent(speedSlider)
                        .addComponent(maxWeightSlider))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(sizeTextField)
                        .addComponent(speedTextField)
                        .addComponent(maxWeightTextField)));

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(modelL)
                        .addComponent(modelTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(yearL)
                        .addComponent(yearSpinner))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(fansL)
                        .addComponent(fansTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(colorL)
                        .addComponent(colorBox))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(sizeL)
                        .addComponent(sizeSlider)
                        .addComponent(sizeTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(speedL)
                        .addComponent(speedSlider)
                        .addComponent(speedTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(maxWL)
                        .addComponent(maxWeightSlider)
                        .addComponent(maxWeightTextField)));
    }

    class MyFocusListener implements FocusListener {

        @Override
        public void focusGained(FocusEvent focusEvent) {
            //modelTextField.setToolTipText("This model already exists!");
        }

        @Override
        public void focusLost(FocusEvent focusEvent) {
        }

    }

    private void setColors() {
        Color buttonColor = Color.decode("#400165");
        Color dangerousButtonColor = Color.decode("#A70308");
        treePanel.setBackground(Color.decode("#FBCA96"));
        buttonsPanel.setBackground(Color.decode("#FBD0A1"));
        addButton.setForeground(buttonColor);
        addIfMaxButton.setForeground(buttonColor);
        removeGreaterKeyButton.setForeground(dangerousButtonColor);
        removeButton.setForeground(dangerousButtonColor);
        toJsonButton.setForeground(buttonColor);
        // edit field
        editPanel.setBackground(Color.decode("#FBCA96"));
        buttonsPanel.setBackground(Color.decode("#E5DDDB"));
        modelL.setForeground(Color.decode("#400165"));
        speedL.setForeground(Color.decode("#400165"));
        sizeL.setForeground(Color.decode("#400165"));
        colorL.setForeground(Color.decode("#400165"));
        yearL.setForeground(Color.decode("#400165"));
        maxWL.setForeground(Color.decode("#400165"));
        fansL.setForeground(Color.decode("#400165"));
    }
    public PropellerCollection getCollection(){
        return collection;
    }
}

