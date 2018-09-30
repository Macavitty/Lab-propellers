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
            chooseColorL = new JLabel("Choose colorBox:"),
            chooseSizeL = new JLabel("Choose sizeField:"),
            chooseModelL = new JLabel("Choose modelField:"),
            chooseSpeedL = new JLabel("Choose speedField:"),
            chooseMaxWL = new JLabel("Choose max weight:"),
            chooseYearL = new JLabel("Choose yearSpinner:");

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

    ClientWindow(Map<String, Karlson.Propeller> map) {

        super("CLIENT");
        this.map = map;

        final int WIDTH = 750, HEIGHT = 800;
        setBounds(10, 10, WIDTH, HEIGHT);
        setSize(WIDTH, HEIGHT);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
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
        sp.setResizeWeight(0.7);
        sp.setEnabled(false);
        sp.setDividerSize(0);
        sp.add(filtersPanel);
        sp.add(controlPanel);
        JPanel anotherPanel = new JPanel(new BorderLayout());
        anotherPanel.add(sp, BorderLayout.CENTER);

        // anotherPanel.add(filtersPanel);
        //anotherPanel.add(controlPanel);

        sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        sp.setResizeWeight(0.95);
        sp.setEnabled(false);
        sp.setDividerSize(0);

        sp.add(objectsPanel);
        sp.add(anotherPanel);
        add(sp);

        //setContentPane(contentPanel);
        letsDraw();
        setColors();
        setVisible(true);
    }

    private void letsDraw() {

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
        setSlider(sizeField, sizeSlider, 40, 100);
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
        two.add(chooseModelL);
        two.add(modelField);

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
        chooseModelL.setForeground(filtersLabelColor);
        chooseSpeedL.setForeground(filtersLabelColor);
        chooseMaxWL.setForeground(filtersLabelColor);
        chooseYearL.setForeground(filtersLabelColor);

    }

    private void setSlider(JTextField text, JSlider slider, int min, int max) {
        text.setEnabled(false);
        text.setHorizontalAlignment(JTextField.CENTER);
        text.setText(Integer.toString(sizeSlider.getValue()));
        slider.setMaximum(max);
        slider.setMinimum(min);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                text.setText(Integer.toString(slider.getValue()));
            }
        });
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
                    verginSize,
                    speed,
                    maxWeight,
                    year;
            Map<String, Karlson.Propeller> map;
            private long startTime = -1;
            private long shrinkDuration = 5000;
            private long normalizeDuration = 4000;
            private double shrinkStep, normalizeStep;

            Circle(int speed, int maxWeight, int size, int year, Color color, int x, int y) {
                this.speed = speed;
                this.maxWeight = maxWeight;
                this.color = color;
                this.size = size;
                this.verginSize = size;
                this.year = year;
                setFrame(x, y, size, size);
                shrinkStep = (verginSize - verginSize / 3) / 500;
                normalizeStep = (verginSize - verginSize / 3) / 400;
            }

            Timer normalizeTimer = new Timer(4, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    if (startTime < 0) {
                        startTime = System.currentTimeMillis();
                    }
                    long now = System.currentTimeMillis();
                    long clockTime = now - startTime;
                    if (clockTime >= normalizeDuration || size >= verginSize) {
                        // for sure
                        size = verginSize;
                        setFrame(x, y, verginSize, verginSize);
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
                    if (clockTime >= shrinkDuration || size <= verginSize / 3) {
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
                int size = propeller.getSize() < 40 ? 40 : propeller.getSize() > 100 ? 100 : propeller.getSize();
                Circle circle = new Circle(propeller.getSpeed(), propeller.getMaxWeight(), size, propeller.getYear(), color, (int) (Math.random() * 300), (int) (Math.random() * 400));
                circles.put(model, circle);

            }
        }

        void resizeCircles() {

            for (Map.Entry<String, Circle> entry : circles.entrySet()) {
                Circle c = entry.getValue();
                System.out.println(
                        sizeSlider.getValue() + " " + entry.getValue().size + " " + entry.getValue().verginSize);
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

            /*
             */
            //System.out.println(parseColor( (String)colorBox.getSelectedItem()) + " " + c.colorBox);
        }
    }
}






/*
    3. Написать графический интерфейс для клиентской части, который отображает в окне объекты коллекции в виде кругов соответствующего
    размера и цвета, расположенных согласно своим координатам. Интерфейс должен удовлетворять следующим требованиям:

        ◦ при нажатии на кнопку "Старт" объекты, характеристики которых соответствуют текущим значениям фильтров,
        должны в течение 5 секунд плавно уменьшать размер в 3 раза, затем в течение 4 секунд возвращаться в исходное состояние;
        ◦ при нажатии на кнопку "Стоп" анимация должна останавливаться.
*/

/*
public class ClientLaunch {

    public void go() {
        //создаю объект класса PBtn
        btn = new PBtn();


        //нарисовал
        collection.forEach(e -> {
            btn.addBtn((lb.getString("Client.date")), e.loca.getX(), e.loca.getY(), getSizeFromEnum(e.epj), (int) 1.3 * getSizeFromEnum(e.epj), e.name, btn.getColorFromEnum(e.colorBox), e.epjc, btn, e.odt);
        });


        List<JCheckBox> cList = new ArrayList<>();

        cList.forEach(e -> {
            menuPanel.add(e);
            e.setSelected(true);
            e.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    getColorsString(cList);
                    filterActors();
                    btn.getMyBtns().forEach(n -> {
                        if (n.shrinkTimer.isRunning())
                            n.shrinkTimer.stop();
                        if (n.animated)
                            n.changeColor();
                    });
                }
            });
        });

        //текстфилд
        JTextField sizeField = new JTextField();
        JLabel checkTextField = new JLabel();
        checkTextField.setFont(new Font("Consolas", Font.PLAIN, 10));
        sizeField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (!checkSize(sizeField.getText())) {
                    checkTextField.setText(lb.getString("Client.CTF"));
                } else checkTextField.setText("");
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (!checkSize(sizeField.getText())) {
                    checkTextField.setText(lb.getString("Client.CTF"));
                } else checkTextField.setText("");
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

        //кнопка анимации
        JButton anime = new JButton(lb.getString("Client.anime"));
        anime.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterActors();
                btn.getMyBtns().forEach(n -> {
                    if (n.animated)
                        n.changeColor();
                });
            }
        });

        JButton stop = new JButton(lb.getString("Client.stop"));
        stop.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btn.getMyBtns().forEach(n -> {
                    n.shrinkTimer.stop();
                });
            }
        });

    }


    public void filterActors() {
        ArrayList<ColorsEnum> filteredColors = getColorFromCheckBox(colorCheckBox);
        btn.getMyBtns().forEach(n -> {
            animeClearance = true;
            animeColor = true;
            animeSize = true;
            if (ifSizeCorrect) {
                if (!(n.width == sizeWidth)) animeSize = false;
            }
            if (spinnerSetted()) {
                if (!(n.clearance == EPjc.valueOf(spinClearance.getValue().toString().toUpperCase())))
                    animeClearance = false;
            }
            if (filteredColors.sizeField() != 0)
                animeColor = filteredColors.stream().anyMatch(colorBox -> ((n.nativeColor.getRed() == btn.getColorFromEnum(colorBox).getRed()) &&
                        (n.nativeColor.getGreen() == btn.getColorFromEnum(colorBox).getGreen()) &&
                        (n.nativeColor.getBlue() == btn.getColorFromEnum(colorBox).getBlue())));
            n.animated = (animeClearance && animeColor && animeSize);
        });

}

class PBtn extends JComponent {
    static class MyBtn extends Rectangle {
        MyBtn() {
            deltaB = -(compColor.getBlue() - 192) / 400f;
            deltaG = -(compColor.getGreen() - 192) / 400f;
            deltaR = -(compColor.getRed() - 192) / 400f;
            shrinkTimer = new Timer(5, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //вычисление нового цвета
                    R = Math.abs(R + deltaR);
                    G = Math.abs(G + deltaG);
                    B = Math.abs(B + deltaB);
                    colorBox = new Color((int) R, (int) G, (int) B);

                    //если достигнут серый цвет, дельта меняет знак, и начинается перекрашивание обратно
                    if (isGray()) {
                        deltaR = -deltaR;
                        deltaG = -deltaG;
                        deltaB = -deltaB;
                    }
                    //если достигнут дефолт, таймер останавливается
                    if (isDefault()) {
                        deltaR = -deltaR;
                        deltaG = -deltaG;
                        deltaB = -deltaB;
                        shrinkTimer.stop();
                    }
                    parent.repaint();
                }
            });

        }

        public void changeColor() {
            if (!shrinkTimer.isRunning()) {
                shrinkTimer.start();
            }
        }
    }


    void addBtn(String zone, int x, int y, int width, int height, String name, Color colorBox, EPjc clearance, JComponent parent, OffsetDateTime created) {
        MyBtn myBtn = new MyBtn(x, y, width, height, name, colorBox, clearance, parent, created);
        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (myBtn.contains(e.getPoint())) {
                    setToolTipText(name + ", created: " + created.atZoneSameInstant(ZoneId.of(zone)));
                }
                ToolTipManager.sharedInstance().mouseMoved(e);
            }
        });
        myBtns.add(myBtn);
        repaint();
    }

    void addBtn(MyBtn m) {
        MyBtn myBtn = new MyBtn(m.x, m.y, m.width, m.height, m.name, m.colorBox, m.clearance, m.parent, m.created);
        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (myBtn.contains(e.getPoint())) {
                    setToolTipText(m.name);
                }
                ToolTipManager.sharedInstance().mouseMoved(e);
            }
        });
        myBtns.add(myBtn);
        repaint();
    }

}
*/