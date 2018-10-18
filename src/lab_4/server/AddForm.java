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

    AddForm(ServerWindow serverWindow, PropellerCollection collection, boolean condition) {
        super("ADD PROPELLER");
        this.serverWindow = serverWindow;
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE); // change
        setBounds(100, 10, 400, 250); // gthtvtyyst
        setResizable(false);
        setVisible(true);
    }




}