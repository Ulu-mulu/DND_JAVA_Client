package com.max.idea;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.lang.constant.Constable;
import java.net.InetAddress;
import java.net.Socket;

public class ClientUI extends JFrame implements ConnectionListener {
    private  JButton buttonSend = new JButton("Send");
    private  JButton buttonConnect = new JButton("Connect");
    private JTextField inputConnect = new JTextField();
    private JTextField inputSend = new JTextField();
    private JTextArea output = new JTextArea(5,20);
    private JScrollPane scroll = new JScrollPane(output);


    private Connection c;

    public ClientUI()
    {
        super("Client");
        this.dispose();

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setBounds(100,100,800,600);
        setState(false);


        buttonConnect.addActionListener(e -> {

            try {
                String host = inputConnect.getText().trim();
                Socket socket = new Socket(InetAddress.getByName(host),Connection.PORT);
                c = new ConnectionImpl(socket,this);
                connectionCreated(c);
                setState(true);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        buttonSend.addActionListener(e ->
        {
            String text = inputSend.getText().trim();
            c.send(text);
            inputSend.setText("");
        });

        Container container = this.getContentPane();
        container.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        //ввод ip
        constraints.weightx = 0.5;
        constraints.gridy = 0;
        constraints.gridx = 0;
        container.add(inputConnect,constraints);
        //кнопка подключения
        constraints.gridx = 1;
        container.add(buttonConnect,constraints);
        //чат
        constraints.gridy = 1;
        constraints.gridx = 0;
        constraints.ipady = 100;
        constraints.gridwidth = 3;
        constraints.weightx = 0;
        output.setEnabled(false);
        container.add(scroll,constraints);
        // ввод сообщения
        constraints.ipadx = 0;
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        container.add(inputSend,constraints);
        // кнопка отправления
        constraints.gridx = 1;
        container.add(buttonSend,constraints);

    }

    @Override
    public void connectionCreated(Connection c) {
        System.out.println("Connection created");
        setState(true);
        this.c = c;
    }

    @Override
    public void connectionClosed(Connection c) {
        System.out.println("Connection created");
    }

    @Override
    public void connectionException(Connection c, Exception ex) {

    }

    @Override
    public void recievedContent(String msg) {
        output.setText(output.getText()+msg+"\n");
    }

    private void setState(boolean isConnected)
    {
        buttonSend.setEnabled(isConnected);
        inputSend.setEnabled(isConnected);
        output.setText("");
    }

}
