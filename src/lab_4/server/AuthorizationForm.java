package lab_4.server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AuthorizationForm extends JFrame {

    // TODO abstract class

    private AuthorizationForm thisWindow = this;
    private ServerWindow serverWindow;

    private JPanel mainPanel = new JPanel(),
            buttonsPanel = new JPanel();

    private JLabel loginL = new JLabel("Login: "),
            passwordL = new JLabel("Password: ");

    private JRadioButton showButton = new JRadioButton("show");
    private JButton loginButton = new JButton("Sign in"), cancelButton = new JButton("Cancel");

    private JTextField login = new JTextField();
    private JPasswordField password = new JPasswordField();

    private String trueLogin;
    private String truePassword;

    public AuthorizationForm(ServerWindow serverWindow) {
        super("AUTHORIZATION");
        this.serverWindow = serverWindow;

        // TODO put this in some file
        trueLogin = "a";
        truePassword = "a";

        showButton.addActionListener((event) -> {
            password.setEchoChar(showButton.isSelected() ? 0 : password.getEchoChar());
        });

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE); // change
        setBounds(100, 10, 400, 200); // gthtvtyyst
        setResizable(false);

        setButtons();
        add(mainPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);

        GroupLayout layout = new GroupLayout(mainPanel);
        organizeGroupLayout(layout);

        setColors();
        setVisible(true);
    }

    private void setColors() {
        mainPanel.setBackground(Color.decode("#E5DDDB"));
        buttonsPanel.setBackground(Color.decode("#E5DDDB"));
        loginL.setForeground(Color.decode("#400165"));
        passwordL.setForeground(Color.decode("#400165"));
        showButton.setForeground(Color.decode("#276C02"));
    }

    private void organizeGroupLayout(GroupLayout layout) {
        mainPanel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(loginL)
                        .addComponent(passwordL))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(login)
                        .addComponent(password))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(showButton)
                ));

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(loginL)
                        .addComponent(login))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(passwordL)
                        .addComponent(password)
                        .addComponent(showButton)
                ));
    }

    private void setButtons() {

        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (login.getText().equals("") || String.valueOf(password.getPassword()).equals(""))
                    JOptionPane.showMessageDialog(thisWindow, "Fill in all fields",
                            "AUTHORIZATION FAILED", JOptionPane.ERROR_MESSAGE);

                else if (login.getText().equals(trueLogin) && String.valueOf(password.getPassword()).equals(truePassword)) {
                    serverWindow.setLoggedIn(true);
                    exitThisWindow();
                } else
                    JOptionPane.showMessageDialog(thisWindow, "Your input is incorrect \n Don`t do this again!",
                            "AUTHORIZATION FAILED", JOptionPane.ERROR_MESSAGE);
            }
        });
        cancelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                exitThisWindow();
            }
        });
        buttonsPanel.add(loginButton);
        buttonsPanel.add(cancelButton);
    }

    private void exitThisWindow() {
        AuthorizationForm.super.setVisible(false);
        serverWindow.setVisible(true);
        serverWindow.setEnabled(true);
    }
}