package lab_4.server;

import lab_4.PropellerCollection;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    static int port = 4004;

    static ExecutorService executor = Executors.newFixedThreadPool(4);

    public static void main(String[] args) {
        PropellerCollection collection = new PropellerCollection();
        ServerWindow window = new ServerWindow(collection);
        Runtime.getRuntime().addShutdownHook(new Thread(collection::save));

        try {
            ServerSocket ss = new ServerSocket(port);

            int i = 0;
            while (!ss.isClosed()) {
                i++;
                Socket client = ss.accept();
                executor.execute(new ClientHandler(client, collection));
                System.out.println("* connection accepted * " + i);
            }
            executor.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket client;
        private String command;
        private PropellerCollection collection;
        // private CommandManager commandManager = new CommandManager();

        private ClientHandler(Socket socket, PropellerCollection p) {
            this.client = socket;
            collection = p;
        }

        public void run() {
            try (ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
                 ObjectInputStream ois = new ObjectInputStream(client.getInputStream())) {

                oos.writeObject("Hi, enter your command please\n"
                        + "Type " + (char) 27 + "[35mhelp / aaa " + (char) 27 + "[30mfor help or " + (char) 27 + "[35mq / Q " + (char) 27 + "[30mto quit");

                while (true) {
                    new CommandManager(oos, ois, collection);
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    System.out.println("* client disconnected *");
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

class ServerWindow extends JFrame {

    private ServerWindow thisWindow = this;

    private JPanel buttonsPanel, treePanel, loginPanel;
    private JScrollPane treeScrollPane;

    private JButton removeGreaterKeyButton,
            addIfMaxButton,
            toJsonButton,
            removeButton,
            addButton;

    private JTree propellersTree;
    private DefaultMutableTreeNode root;
    private DefaultTreeModel treeModel;

    private JMenuBar menuBar;
    private JMenu mainMenu, collectionMenu;
    private JMenuItem menuItemLoad,
            menuItemSave,
            menuItemClear,
            menuItemHelp,
            menuItemExit,
            menuItemLogin;
    private boolean isLoggedIn = false;

    private PropellerCollection collection;
    private AuthorizationForm loginForm;

    public ServerWindow(PropellerCollection collection) {
        super("THIS IS SERVER");
        this.collection = collection;
        setBounds(100, 10, 800, 500); // gthtvtyyst
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        createMenuBar();
        setJMenuBar(menuBar);

        createTreePanel();
        add(treePanel, BorderLayout.CENTER);

        createButtonsPanel();
        add(buttonsPanel, BorderLayout.EAST);

        setColors();
        setVisible(true);
    }


    private void createMenuBar() {

        // CLEAR
        menuItemClear = new JMenuItem("Clear collection");
        menuItemClear.addActionListener((ActionEvent event) -> {
            if (isLoggedIn) {
                if (JOptionPane.showConfirmDialog(this, "Do you really want to clear the collection ?", "", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.YES_OPTION)
                    JOptionPane.showMessageDialog(this, collection.clear(), "Clearing result", JOptionPane.PLAIN_MESSAGE);
                refreshPropellersTree();
            } else showFigy();
        });

        // SAVE
        menuItemSave = new JMenuItem("Save collection");
        menuItemSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        menuItemSave.addActionListener((ActionEvent event) -> {
            if (isLoggedIn) {
                JOptionPane.showMessageDialog(this, collection.save(), "Saving result", JOptionPane.PLAIN_MESSAGE);
            } else showFigy();
        });

        // LOAD
        menuItemLoad = new JMenuItem("Load collection");
        menuItemLoad.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        menuItemLoad.addActionListener((ActionEvent event) -> {
            if (isLoggedIn) {
                JOptionPane.showMessageDialog(this, collection.load(), "Loading result", JOptionPane.PLAIN_MESSAGE);
                refreshPropellersTree();
            } else showFigy();
        });

        // HELP
        menuItemHelp = new JMenuItem("Help");
        menuItemHelp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
        menuItemHelp.addActionListener((ActionEvent event) -> {
            JOptionPane.showMessageDialog(this, "Come on! \n It`s pretty easy \n (if you are logged in)", "Help", JOptionPane.PLAIN_MESSAGE);
        });

        // EXIT
        menuItemExit = new JMenuItem("Exit");
        menuItemExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
        menuItemExit.addActionListener((ActionEvent event) -> {
            System.exit(0);
        });

        // LOGIN
        menuItemLogin = new JMenuItem("Sing in");
        if (isLoggedIn) menuItemLogin.setEnabled(false);
        else menuItemLogin.setEnabled(true);
        menuItemLogin.addActionListener((ActionEvent event) -> {
            loginForm = new AuthorizationForm(thisWindow);
        });

        mainMenu = new JMenu("MENU");
        mainMenu.setMnemonic(KeyEvent.VK_M);
        collectionMenu = new JMenu("COLLECTION");

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
        treeScrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
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
        Border border = BorderFactory.createLineBorder(Color.decode("#A5AAAF"));
        treePanel.setBorder(border);
    }

    public void refreshPropellersTree() {
        String[] models = collection.getPropellerMap().keySet().stream().toArray(String[]::new);
        root.removeAllChildren();
        for (int i = 0; i < collection.getSize(); i++)
            root.add(new DefaultMutableTreeNode(models[i]));
        treeModel = new DefaultTreeModel(root);
        propellersTree.setModel(treeModel);
    }

    private void createButtonsPanel() {

        buttonsPanel = new JPanel();
        Border border = BorderFactory.createLineBorder(Color.decode("#A5AAAF"));
        buttonsPanel.setBorder(border);
        GroupLayout layout = new GroupLayout(buttonsPanel);
        buttonsPanel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        removeGreaterKeyButton = new JButton("remove greater");
        addIfMaxButton = new JButton("add if max");
        removeButton = new JButton("remove");
        toJsonButton = new JButton("to Json");
        addButton = new JButton("add");

        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(addButton));

        removeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (isLoggedIn) {
                    if (!propellersTree.isSelectionEmpty()) {
                        if (JOptionPane.showConfirmDialog(thisWindow, "Are you sure ?", "", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.YES_OPTION) {
                            collection.remove(propellersTree.getLastSelectedPathComponent().toString());
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
                        if (JOptionPane.showConfirmDialog(thisWindow, "Are you sure ?", "", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.YES_OPTION) {
                            collection.removeGreaterKey(propellersTree.getLastSelectedPathComponent().toString());
                            refreshPropellersTree();
                        }
                    }
                } else showFigy();
            }
        });

        addButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (isLoggedIn) {
                    thisWindow.setEnabled(false);
                    new AddForm(thisWindow, collection, false);
                } else showFigy();
            }
        });
        addIfMaxButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (isLoggedIn) {
                    thisWindow.setEnabled(false);
                    new AddForm(thisWindow, collection, true);
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

        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(addButton)
                                .addComponent(removeButton))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(addIfMaxButton)
                                .addComponent(removeGreaterKeyButton)))
                .addComponent(toJsonButton));

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(addButton)
                                .addComponent(addIfMaxButton))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(removeButton)
                                .addComponent(removeGreaterKeyButton)))
                .addComponent(toJsonButton));

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
    }

    private void showFigy() {
        if (JOptionPane.showConfirmDialog(this, "Sorry, you are not authorized for this operation\n" +
                "Do you want to?", "PERMISSION DENIED", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.YES_OPTION) {
            loginForm = new AuthorizationForm(thisWindow);
        }

    }

    public void setLoggedIn(boolean b){
     isLoggedIn = b;
    }
}

/*
    3. Написать графический интерфейс для клиентской части, который отображает в окне объекты коллекции в виде кругов соответствующего
    размера и цвета, расположенных согласно своим координатам. Интерфейс должен удовлетворять следующим требованиям:
        ◦ при наведении мышкой на объект должна появляться всплывающая подсказка с именем объекта;
        ◦ должны быть реализованы фильтры для каждой характеристики объектов;
        ◦ при реализации фильтров должны быть использованы JRadioButton, JTextField, JSpinner и другие компоненты;
        ◦ при нажатии на кнопку "Старт" объекты, характеристики которых соответствуют текущим значениям фильтров, должны в течение 5 секунд плавно уменьшать размер в 3 раза, затем в течение 4 секунд возвращаться в исходное состояние;
        ◦ при нажатии на кнопку "Стоп" анимация должна останавливаться.
*/



























