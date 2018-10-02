package lab_4.server;

import lab_4.PropellerCollection;
import lab_4.story_components.Karlson;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.Map;

public class AddForm extends JFrame {

    private AddForm thisWindow = this;
    private ServerWindow serverWindow;

    private JPanel mainPanel, buttonsPanel;

    private JTextField model = new JTextField(),
            fans = new JTextField(),
            size = new JTextField(),
            speed = new JTextField(),
            maxWeight = new JTextField();

    private JLabel modelL = new JLabel("Model: "),
            fansL = new JLabel("Fans: "),
            colorL = new JLabel("Color: "),
            speedL = new JLabel("Speed: "),
            yearL = new JLabel("Year: "),
            maxWL = new JLabel("Max weight: "),
            sizeL = new JLabel("Size: ");

    private JSpinner year;
    private JComboBox<String> color;

    private JSlider sizeSlider = new JSlider(),
            speedSlider = new JSlider(),
            maxWeightSlider = new JSlider();

    private JButton addButton, cancelButton;

    private PropellerCollection collection;
    private boolean condition;

    private Calendar calendar;
    private Color wrongInputColor = Color.decode("#FF5537");
    private Color correctInputColor = Color.decode("#F3BFFF");

    AddForm(ServerWindow serverWindow, PropellerCollection collection, boolean condition) {
        super("ADD PROPELLER");
        this.serverWindow = serverWindow;
        this.collection = collection;
        this.condition = condition;
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE); // change
        setBounds(100, 10, 400, 250); // gthtvtyyst
        setResizable(false);

        mainPanel = new JPanel();

        organizeModel();
        setSlider(size, sizeSlider, 1, 482);
        setSlider(speed, speedSlider, 0, 600);
        setSlider(maxWeight, maxWeightSlider, 1, 300);
        setSpinners();

        // color
        String[] colors = {"red", "white", "black", "green", "blue", "silver",
                "purple", "maroon", "golden", "orange"};
        // TODO change background
        color = new JComboBox<>(colors);

        GroupLayout layout = new GroupLayout(mainPanel);
        organizeGroupLayout(layout);
        setButtons();
        add(mainPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);

        setColors();
        setVisible(true);
    }

    private void setSpinners() {
        // year
        calendar = Calendar.getInstance();
        final int currentYear = calendar.get(Calendar.YEAR);
        year = new JSpinner(new SpinnerNumberModel(currentYear, currentYear - 300, currentYear, 1));
        year.setEditor(new JSpinner.NumberEditor(year, "#"));
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

    private void setButtons() {
        buttonsPanel = new JPanel();
        addButton = new JButton("Add propeller");
        cancelButton = new JButton("Cancel");
        addButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (!model.getText().equals("")) {
                    String propeller = toJson();

                    if (condition) { // if add if max called
                        JOptionPane.showMessageDialog(thisWindow, collection.addIfMax(propeller), "ADDING", JOptionPane.PLAIN_MESSAGE);
                    } else { // if add called
                        JOptionPane.showMessageDialog(thisWindow, collection.add(propeller), "ADDING", JOptionPane.PLAIN_MESSAGE);
                    }
                    serverWindow.refreshPropellersTree();
                } else
                    JOptionPane.showMessageDialog(thisWindow, "MODEL NAME IS EMPTY", "Warning", JOptionPane.PLAIN_MESSAGE);
            }
        });
        cancelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                AddForm.super.setVisible(false);

                serverWindow.setVisible(true);
                serverWindow.setEnabled(true);
            }
        });
        buttonsPanel.add(addButton);
        buttonsPanel.add(cancelButton);
    }

    private String toJson() {
        return
                "{\"model\":\""
                        + model.getText()
                        + "\",\"year\":"
                        + year.getValue()
                        + ",\"size\":"
                        + size.getText()
                        + ",\"speed\":"
                        + speed.getText()
                        + ",\"maxWeight\":"
                        + maxWeight.getText()
                        + ",\"color\":\""
                        + color.getSelectedItem()
                        + "\",\"fans\":[\""
                        + fans.getText()
                        + "\"]}";
    }

    private void organizeModel() {
        MyFocusListener focusListener = new MyFocusListener();
        model = new JTextField();
        model.addCaretListener(new CaretListener() {
            public void caretUpdate(CaretEvent e) {
                Map<String, Karlson.Propeller> map = collection.getPropellerMap();
                if (map.keySet().stream().anyMatch(a -> a.compareTo(model.getText()) == 0)) {
                    model.setBackground(wrongInputColor);
                    model.addFocusListener(focusListener);
                } else {
                    model.setBackground(correctInputColor);
                    model.removeFocusListener(focusListener);
                }
            }
        });
    }

    private void organizeGroupLayout(GroupLayout layout) {
        mainPanel.setLayout(layout);
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
                        .addComponent(model)
                        .addComponent(year)
                        .addComponent(fans)
                        .addComponent(color)
                        .addComponent(sizeSlider)
                        .addComponent(speedSlider)
                        .addComponent(maxWeightSlider))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(size)
                        .addComponent(speed)
                        .addComponent(maxWeight)));

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(modelL)
                        .addComponent(model))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(yearL)
                        .addComponent(year))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(fansL)
                        .addComponent(fans))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(colorL)
                        .addComponent(color))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(sizeL)
                        .addComponent(sizeSlider)
                        .addComponent(size))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(speedL)
                        .addComponent(speedSlider)
                        .addComponent(speed))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(maxWL)
                        .addComponent(maxWeightSlider)
                        .addComponent(maxWeight)));
    }

    class MyFocusListener implements FocusListener {

        @Override
        public void focusGained(FocusEvent focusEvent) {
            model.setToolTipText("This model already exists!");
        }

        @Override
        public void focusLost(FocusEvent focusEvent) {
        }
    }

    private void setColors() {
        mainPanel.setBackground(Color.decode("#E5DDDB"));
        buttonsPanel.setBackground(Color.decode("#E5DDDB"));
        modelL.setForeground(Color.decode("#400165"));
        speedL.setForeground(Color.decode("#400165"));
        sizeL.setForeground(Color.decode("#400165"));
        colorL.setForeground(Color.decode("#400165"));
        yearL.setForeground(Color.decode("#400165"));
        maxWL.setForeground(Color.decode("#400165"));
        fansL.setForeground(Color.decode("#400165"));

    }
}