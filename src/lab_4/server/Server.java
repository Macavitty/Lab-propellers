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
        collection.load();
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
        private PropellerCollection collection;

        private ClientHandler(Socket socket, PropellerCollection p) {
            this.client = socket;
            collection = p;
        }

        public void run() {
            try (ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
                 ObjectInputStream ois = new ObjectInputStream(client.getInputStream())) {

                oos.writeObject("Hi, enter your command please\n"
                        + "Type " + (char) 27 + "[35mhelp / aaa " + (char) 27 +
                        "[30mfor help or " + (char) 27 + "[35mq / Q " + (char) 27 + "[30mto quit");

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
