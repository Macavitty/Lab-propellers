package lab_4.client;


import lab_4.PropellerCollection;
import lab_4.orm.DBManager;

import java.text.DateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import lab_4.story_components.Karlson;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.util.*;

public class ClientWindow extends JFrame {

    private GridLayout filtGrid = new GridLayout(0, 1);
    private JPanel filtersPanel = new JPanel(filtGrid),
            controlPanel = new JPanel(),
            objectsPanel = new JPanel(new BorderLayout());
    private ImagePanel imagePanel;
    private JButton startButton = new JButton("Start animation"),
            stopButton = new JButton("Stop animation"),
            exitButton = new JButton("Exit");

    private JLabel filterTitleL = new JLabel("FILTERS"),
            objectsTitleL = new JLabel("PROPELLERS"),
            chooseColorL = new JLabel("Choose color:"),
            chooseSizeL = new JLabel("Choose size:"),
            chooseSpeedL = new JLabel("Choose speed:"),
            chooseMaxWL = new JLabel("Choose max weight:"),
            chooseYearL = new JLabel("Choose year:");

    private JTextField modelField = new JTextField(),
            sizeField = new JTextField(),
            speedField = new JTextField(),
            maxWeightField = new JTextField();

    private JSpinner yearSpinner = new JSpinner();
    private JComboBox<String> colorBox;

    private JSlider sizeSlider = new JSlider(),
            speedSlider = new JSlider(),
            maxWeightSlider = new JSlider();
    private JMenu mainMenu;

    private Map<String, Karlson.Propeller> map;
    private DBManager dbManager;

    private Color cRed = Color.decode("#ff3333"),
            cSilver = Color.decode("#c2d6d6"),
            cWhite = Color.decode("#ffffff"),
            cBlack = Color.decode("#0a0f0f"),
            cGreen = Color.decode("#269900"),
            cBlue = Color.decode("#0066ff"),
            cPurple = Color.decode("#9900ff"),
            cGolden = Color.decode("#ff9900"),
            cOrange = Color.decode("#e65c00"),
            cDefault = Color.decode("#003333");

    private final int F_WIDTH = 750, F_HEIGHT = 800;

    private JMenuBar menuBar;
    ResourceBundle resourceBundle;
    final int COLOR_NUM = 9;
    private String[] colors = {"white", "red", "black", "green", "blue", "silver",
            "purple", "golden", "orange"};
    private String createdString = "created";

    ClientWindow(Map<String, Karlson.Propeller> map) {

        super("CLIENT");
        dbManager = new DBManager();
        dbManager.initDB();
        Locale.setDefault(new Locale("en"));
        this.map = map;

        setBounds(10, 10, F_WIDTH, F_HEIGHT);
        setSize(F_WIDTH, F_HEIGHT);
        setPreferredSize(new Dimension(F_WIDTH, F_HEIGHT));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

//        this.setContentPane(panel);

        createObjectsPanel();
        createFiltersPanel();
        createControlPanel();
        imagePanel = new ImagePanel(map);
        objectsTitleL.setHorizontalAlignment(SwingConstants.CENTER);
        objectsPanel.add(objectsTitleL, BorderLayout.NORTH);
        objectsPanel.add(imagePanel, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.4);
        splitPane.setEnabled(false);
        splitPane.setDividerSize(0);
        splitPane.add(filtersPanel);
        splitPane.add(controlPanel);
        JPanel anotherPanel = new JPanel(new BorderLayout());
        anotherPanel.add(splitPane, BorderLayout.CENTER);

        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.9);
        splitPane.setEnabled(false);
        splitPane.setDividerSize(0);

        splitPane.add(objectsPanel);
        splitPane.add(anotherPanel);
        add(splitPane);

        //setContentPane(contentPanel);
        createMenuBar();
        setJMenuBar(menuBar);
        setColors();
        setVisible(true);
    }

    private void createMenuBar() {
        mainMenu = new JMenu("LANGUAGE");

        JMenuItem menuItemRussian = new JMenuItem("Русский");
        menuItemRussian.addActionListener((ActionEvent event) -> {
            Locale locale = new Locale("ru", "RU");
            changeLocale(locale);
        });
        JMenuItem menuItemDutch = new JMenuItem("Nederlands"); //
        menuItemDutch.addActionListener((ActionEvent event) -> {
            Locale locale = new Locale("nl", "NL");
            changeLocale(locale);
        });
        JMenuItem menuItemLithuanian = new JMenuItem("Lietuviškai"); // Lt_LT
        menuItemLithuanian.addActionListener((ActionEvent event) -> {
            Locale locale = new Locale("lt", "LT");
            changeLocale(locale);
        });
        JMenuItem menuItemSpanish = new JMenuItem("español"); // es_CO
        menuItemSpanish.addActionListener((ActionEvent event) -> {
            Locale locale = new Locale("es", "CO");
            changeLocale(locale);
        });

        mainMenu.add(menuItemRussian);
        mainMenu.add(menuItemDutch);
        mainMenu.add(menuItemLithuanian);
        mainMenu.add(menuItemSpanish);

        menuBar = new JMenuBar();
        menuBar.setBorderPainted(true);
        menuBar.add(mainMenu);
    }

    private void createControlPanel() {
        Border border = BorderFactory.createLineBorder(Color.decode("#8c8773"));

        controlPanel.add(startButton, BorderLayout.CENTER);
        controlPanel.add(stopButton, BorderLayout.CENTER);
        controlPanel.add(exitButton, BorderLayout.EAST);

        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                imagePanel.resizeCircles();

            }
        });
        stopButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                imagePanel.stopAnimation();

            }
        });
        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);

                System.exit(0);
            }
        });

        Action buttonAction = new AbstractAction("Exit") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                System.exit(0);
            }
        };

        String key = "Exit";
        exitButton.setAction(buttonAction);
        buttonAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_R);
        exitButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK), key);
        exitButton.getActionMap().put(key, buttonAction);
    }

    private void createFiltersPanel() {

        filtGrid.setHgap(1);
        filtGrid.setVgap(1);
        GridLayout threeGrid = new GridLayout(0, 3),
                twoGrid = new GridLayout(0, 2);
        threeGrid.setHgap(1);
        threeGrid.setVgap(1);
        twoGrid.setHgap(1);
        twoGrid.setVgap(1);
        JPanel three = new JPanel(threeGrid),
                two = new JPanel(twoGrid);

        // border
        Border border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        filtersPanel.setBorder(border);

        //filtersPanel.setSize(200, 200);
        setSlider(maxWeightField, maxWeightSlider, 10, 30);
        setSlider(sizeField, sizeSlider, 0, 100);
        setSlider(speedField, speedSlider, 10, 200);

        // 1 row
        three.add(chooseColorL);
        colorBox = new JComboBox<>(colors);
        JTextField forColor = new JTextField();
        forColor.setEnabled(false);
        colorBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (colorBox.getSelectedItem() != null)
                    forColor.setBackground(parseColor(colorBox.getSelectedItem().toString()));
            }
        });
        three.add(colorBox);
        three.add(forColor);

        // 2 row
        three.add(chooseMaxWL);
        three.add(maxWeightSlider);
        three.add(maxWeightField);

        // 3 row
        three.add(chooseSizeL);
        three.add(sizeSlider);
        three.add(sizeField);

        // 4 row
        three.add(chooseSpeedL);
        three.add(speedSlider);
        three.add(speedField);

        // 5 row

        two.add(chooseYearL);
        // yearSpinner
        Calendar calendar = Calendar.getInstance();
        final int currentYear = calendar.get(Calendar.YEAR);
        yearSpinner = new JSpinner(new SpinnerNumberModel(currentYear, currentYear - 300, currentYear, 1));
        yearSpinner.setEditor(new JSpinner.NumberEditor(yearSpinner, "#"));
        yearSpinner.setMaximumSize(new Dimension(50, 10));
        two.add(yearSpinner);

        three.setBackground(Color.decode("#bfff80"));
        two.setBackground(Color.decode("#bfff80"));

        filtersPanel.add(three);
        filtersPanel.add(two);
    }

    private void createObjectsPanel() {
        Border border = BorderFactory.createLineBorder(Color.decode("#8c8773"));
        objectsPanel.setBorder(border);

    }

    private void setColors() {
        Color buttonColor = Color.decode("#400165");
        Color dangerousButtonColor = Color.decode("#A70308");
        Color filtersLabelColor = Color.decode("#808080");

        filtersPanel.setBackground(Color.decode("#bfff80"));
        controlPanel.setBackground(Color.decode("#bfff80"));
        objectsPanel.setBackground(Color.decode("#ccff99"));
        imagePanel.setBackground(Color.decode("#ccff99"));

        startButton.setForeground(buttonColor);
        stopButton.setForeground(buttonColor);
        exitButton.setForeground(dangerousButtonColor);

        filterTitleL.setForeground(Color.decode("#8c8773"));
        objectsTitleL.setForeground(Color.decode("#8c8773"));
        chooseColorL.setForeground(filtersLabelColor);
        chooseSizeL.setForeground(filtersLabelColor);
        chooseSpeedL.setForeground(filtersLabelColor);
        chooseMaxWL.setForeground(filtersLabelColor);
        chooseYearL.setForeground(filtersLabelColor);

    }

    private void setSlider(JTextField text, JSlider slider, int min, int max) {
        text.setEnabled(false);
        text.setHorizontalAlignment(JTextField.CENTER);
        text.setText(Integer.toString(sizeSlider.getValue()));
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                text.setText(Integer.toString(slider.getValue()));
            }
        });
        slider.setMaximum(max);
        slider.setMinimum(min);
        slider.setValue((max + min) / 2);
    }

    private Color parseColor(String name) {

        if (name.equals(colors[1])) return cRed;
        else if (name.equals(colors[5])) return cSilver;
        else if (name.equals(colors[2])) return cBlack;
        else if (name.equals(colors[4])) return cBlue;
        else if (name.equals(colors[3])) return cGreen;
        else if (name.equals(colors[7])) return cGolden;
        else if (name.equals(colors[0])) return cWhite;
        else if (name.equals(colors[6])) return cPurple;
        else if (name.equals(colors[8])) return cOrange;
        return cDefault;

    }

    private class ImagePanel extends JPanel implements MouseMotionListener {

        private Map<String, Karlson.Propeller> map;
        private Map<String, Circle> circles = new HashMap<>();
        private Map<String, Integer> xCoords = new HashMap<>();
        private Map<String, Integer> yCoords = new HashMap<>();

        ImagePanel(Map<String, Karlson.Propeller> map) {
            this.map = map;
            setPreferredSize(new Dimension(100, 100));
            createCircles();
            addMouseMotionListener(this);
        }

        public void doIt(Map<String, Karlson.Propeller> newMap) {
            if (!map.equals(newMap)) {
                for (Map.Entry<String, Karlson.Propeller> entry : newMap.entrySet()) {
                    if (map.keySet().stream().anyMatch(a -> a.compareTo(entry.getKey()) == 0)) { // found new
                        Karlson.Propeller propeller = map.get(entry.getKey());
                        createCircle(propeller);
                        map.put(entry.getKey(), propeller);
                        repaint();
                    }
                }
                for (Map.Entry<String, Karlson.Propeller> entry : map.entrySet()) {
                    if (newMap.keySet().stream().anyMatch(a -> a.compareTo(entry.getKey()) == 0)) { // found removed
                        removeCircle(entry.getKey());
                        map.remove(entry.getKey());
                        repaint();
                    }

                }
            }
        }

        private class Circle extends Ellipse2D.Double {
            Color color;
            double size,
                    virginSize,
                    speed,
                    maxWeight,
                    year;
            private long startTime = -1;
            private long shrinkDuration = 5000;
            private long normalizeDuration = 4000;
            private double shrinkStep, normalizeStep;
            Date date;

            Circle(int speed, int maxWeight, double size, int year, Color color, int x, int y, Date date) {
                this.speed = speed;
                this.maxWeight = maxWeight;
                this.color = color;
                this.size = size;
                this.virginSize = size;
                this.year = year;
                this.date = date;
                setFrame(x, y, size, size);
                shrinkStep = (virginSize - virginSize / 3) / 500;
                normalizeStep = (virginSize - virginSize / 3) / 400;
            }

            void setSize(double newSize) {
                this.size = (int) newSize;
                this.virginSize = (int) newSize;
                setFrame(x, y, size, size);
                shrinkStep = (virginSize - virginSize / 3) / 500;
                normalizeStep = (virginSize - virginSize / 3) / 400;
                sizeSlider.setValue((int) newSize);
                imagePanel.repaint();
            }

            Timer normalizeTimer = new Timer(4, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    if (startTime < 0) {
                        startTime = System.currentTimeMillis();
                    }
                    long now = System.currentTimeMillis();
                    long clockTime = now - startTime;
                    if (clockTime >= normalizeDuration || size >= virginSize) {
                        // for sure
                        size = virginSize;
                        setFrame(x, y, virginSize, virginSize);
                        normalizeTimer.stop();
                    } else {
                        size += normalizeStep;
                        setFrame(x, y, size, size);
                    }
                    imagePanel.repaint();
                }
            });
            Timer shrinkTimer = new Timer(5, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    if (startTime < 0) {
                        startTime = System.currentTimeMillis();
                    }
                    long now = System.currentTimeMillis();
                    long clockTime = now - startTime;
                    if (clockTime >= shrinkDuration || size <= virginSize / 3) {
                        shrinkTimer.stop();
                        startTime = -1;
                        normalizeTimer.start();
                    } else {
                        size -= shrinkStep;
                        setFrame(x, y, size, size);
                    }
                    imagePanel.repaint();
                }
            });
        }

        void addCircle(Karlson.Propeller p) {
            createCircle(p);
            repaint();
        }

        void removeCircle(String m) {
            circles.remove(m);
            xCoords.remove(m);
            yCoords.remove(m);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            for (Map.Entry<String, Circle> entry : circles.entrySet()) {
                Karlson.Propeller propeller = map.get(entry.getKey());
                Circle circle = circles.get(entry.getKey());
                g2.setColor(circle.color);
                g2.fill(circle);
                g2.setColor(Color.decode("#8c8773"));
                g2.draw(circle);
            }
        }

        @Override
        public void mouseDragged(MouseEvent mouseEvent) {
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
            for (Map.Entry<String, Circle> entry : circles.entrySet()) {

                if (circles.get(entry.getKey()).contains(e.getPoint())) {
                    setToolTipText(entry.getKey() + ", " + createdString + ": " + df.format(new Date()));
                    repaint();
                }
                ToolTipManager.sharedInstance().mouseMoved(e);

            }
        }

        void createCircles() {
            for (Map.Entry<String, Karlson.Propeller> entry : map.entrySet()) {
                Karlson.Propeller propeller = map.get(entry.getKey());
                createCircle(propeller);

            }
        }

        void createCircle(Karlson.Propeller propeller) {
            String model = propeller.getModel();
            Color color = parseColor(propeller.getColor());
            int size = propeller.getSize() < 1 ? 1 : propeller.getSize() > 100 ? 100 : propeller.getSize();
            int x = (int) (Math.random() * F_WIDTH * 0.8);
            int y = (int) (Math.random() * F_HEIGHT * 0.45);
            Circle circle = new Circle(propeller.getSpeed(), propeller.getMaxWeight(), size, propeller.getYear(), color, x, y, propeller.getDate());
            circles.put(model, circle);
            xCoords.put(model, x);
            yCoords.put(model, y);
        }

        void resizeCircles() {

            for (Map.Entry<String, Circle> entry : circles.entrySet()) {
                Circle c = entry.getValue();
                if (sizeSlider.getValue() == c.size &&
                        parseColor((String) colorBox.getSelectedItem()).equals(c.color) &&
                        speedSlider.getValue() == c.speed &&
                        maxWeightSlider.getValue() == c.maxWeight &&
                        (Integer) yearSpinner.getValue() == c.year) {
                    if (!c.shrinkTimer.isRunning()) {
                        c.startTime = -1;
                        c.shrinkTimer.start();
                    }
                }
            }
        }

        void stopAnimation() {
            for (Map.Entry<String, Circle> entry : circles.entrySet()) {
                Circle c = entry.getValue();
                if (c.shrinkTimer.isRunning()) {
                    c.shrinkTimer.stop();
                    c.setSize(c.size);
                } else if (c.normalizeTimer.isRunning()) {
                    c.normalizeTimer.stop();
                    c.setSize(c.size);
                }
            }

        }

    }

    private void changeLocale(Locale newLocale) {
        Locale.setDefault(newLocale);
        resourceBundle = ResourceBundle.getBundle("locale", newLocale);
        changeLang();
    }

    private void changeLocale(String newLocale) {
        Locale locale = new Locale(newLocale);
        changeLocale(locale);
    }

    public void refreshMap(Map<String, Karlson.Propeller> map) {
        imagePanel.doIt(map);
    }

    private void changeLang() {
        stopButton.setText(resourceBundle.getString("stopBtn"));
        startButton.setText(resourceBundle.getString("startBtn"));
        exitButton.setText(resourceBundle.getString("exitBtn"));
        filterTitleL.setText(resourceBundle.getString("filterTitleL"));
        objectsTitleL.setText(resourceBundle.getString("objectsTitleL"));
        chooseColorL.setText(resourceBundle.getString("chooseColorL"));
        chooseSizeL.setText(resourceBundle.getString("chooseSizeL"));
        chooseSpeedL.setText(resourceBundle.getString("chooseSpeedL"));
        chooseMaxWL.setText(resourceBundle.getString("chooseMaxWL"));
        chooseYearL.setText(resourceBundle.getString("chooseYearL"));
        mainMenu.setText(resourceBundle.getString("lang"));
        colorBox.removeAllItems();
        for (int i = 0; i < COLOR_NUM; i++) {
            colors[i] = resourceBundle.getString(String.valueOf(i + 1));
            colorBox.addItem(colors[i]);
        }
        createdString = resourceBundle.getString("created");
    }
}