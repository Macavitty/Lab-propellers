package lab_4.client;


import lab_4.story_components.Karlson;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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

    private Map<String, Karlson.Propeller> map;

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

    ClientWindow(Map<String, Karlson.Propeller> map) {

        super("CLIENT");
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


        JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        sp.setResizeWeight(0.4);
        sp.setEnabled(false);
        sp.setDividerSize(0);
        sp.add(filtersPanel);
        sp.add(controlPanel);
        JPanel anotherPanel = new JPanel(new BorderLayout());
        anotherPanel.add(sp, BorderLayout.CENTER);

        // anotherPanel.add(filtersPanel);
        //anotherPanel.add(controlPanel);

        sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        sp.setResizeWeight(0.9);
        sp.setEnabled(false);
        sp.setDividerSize(0);

        sp.add(objectsPanel);
        sp.add(anotherPanel);
        add(sp);

        //setContentPane(contentPanel);
        setColors();
        setVisible(true);
    }


    private void createControlPanel() {
        Border border = BorderFactory.createLineBorder(Color.decode("#8c8773"));
        //controlPanel.setBorder(border);
        //controlPanel.setSize(1000, 100);

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
        String[] colors = {"white", "red", "black", "green", "blue", "silver",
                "purple", "golden", "orange"};
        colorBox = new JComboBox<>(colors);
        JTextField forColor = new JTextField();
        forColor.setEnabled(false);
        colorBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
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

        // 6 row
        two.add(chooseYearL);
        // yearSpinner
        Calendar calendar = Calendar.getInstance();
        final int currentYear = calendar.get(Calendar.YEAR);
        yearSpinner = new JSpinner(new SpinnerNumberModel(currentYear, currentYear - 300, currentYear, 1));
        yearSpinner.setEditor(new JSpinner.NumberEditor(yearSpinner, "#"));
        two.add(yearSpinner);

        three.setBackground(Color.decode("#bfff80"));
        two.setBackground(Color.decode("#bfff80"));

        filtersPanel.add(three);
        filtersPanel.add(two);
    }

    private void createObjectsPanel() {
        Border border = BorderFactory.createLineBorder(Color.decode("#8c8773"));
        objectsPanel.setBorder(border);
        //objectsPanel.setSize(100, 100);

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
        switch (name) {
            case "red":
                return cRed;
            case "silver":
                return cSilver;
            case "black":
                return cBlack;
            case "blue":
                return cBlue;
            case "green":
                return cGreen;
            case "golden":
                return cGolden;
            case "white":
                return cWhite;
            case "purple":
                return cPurple;
            case "orange":
                return cOrange;
            default:
                return cDefault;
        }
    }

    private class ImagePanel extends JPanel implements MouseMotionListener {


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

            Circle(int speed, int maxWeight, double size, int year, Color color, int x, int y) {
                this.speed = speed;
                this.maxWeight = maxWeight;
                this.color = color;
                this.size = size;
                this.virginSize = size;
                this.year = year;
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

        private Map<String, Karlson.Propeller> map;
        private Map<String, Circle> circles = new HashMap<>();

        ImagePanel(Map<String, Karlson.Propeller> map) {
            this.map = map;
            setPreferredSize(new Dimension(100, 100));
            createCircles();
            addMouseMotionListener(this);
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
            for (Map.Entry<String, Circle> entry : circles.entrySet()) {

                if (circles.get(entry.getKey()).contains(e.getPoint())) {
                    setToolTipText(entry.getKey());
                    repaint();
                }
                ToolTipManager.sharedInstance().mouseMoved(e);

            }
        }

        void createCircles() {
            for (Map.Entry<String, Karlson.Propeller> entry : map.entrySet()) {
                Karlson.Propeller propeller = map.get(entry.getKey());
                String model = propeller.getModel();
                Color color = parseColor(propeller.getColor());
                int size = propeller.getSize() < 1 ? 1 : propeller.getSize() > 100 ? 100 : propeller.getSize();
                Circle circle = new Circle(propeller.getSpeed(), propeller.getMaxWeight(), size,
                        propeller.getYear(), color,
                        (int) (Math.random() * F_WIDTH * 0.8),
                        (int) (Math.random() * F_HEIGHT * 0.45));
                circles.put(model, circle);

            }
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
}


