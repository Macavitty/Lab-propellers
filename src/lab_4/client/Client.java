package lab_4.client;

import lab_4.story_components.Karlson;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.rmi.UnknownHostException;
import java.util.Map;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {

        String hostName = "localhost";
        int port = 4004;

        Scanner mainScanner = new Scanner(System.in);

        Map<String, Karlson.Propeller> map;

        while (!Thread.currentThread().isInterrupted()) {

            try (SocketChannel socketChannel = SocketChannel.open()) {

                socketChannel.configureBlocking(true);

                socketChannel.connect(new InetSocketAddress(hostName, port));

                ObjectInputStream ois = new ObjectInputStream(socketChannel.socket().getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socketChannel.socket().getOutputStream());
                String userCommand;

                System.out.println((String) ois.readObject());

                // getting map
                oos.writeObject("getMap");
                map = (Map<String, Karlson.Propeller>) ois.readObject();
                ClientWindow clientWindow = new ClientWindow(map);

                while (!Thread.currentThread().isInterrupted()) {
                    /*userCommand = mainScanner.nextLine();
                    if (userCommand.equals("q")) {
                        System.out.println("* Have a good day *");
                        System.exit(0);
                    }*/
                    oos.writeObject("getMap"); // send command to server
                    Map<String, Karlson.Propeller> got = (Map<String, Karlson.Propeller>) ois.readObject();
                    if (!got.equals(map)) {
                        map = got;
                        clientWindow.refreshMap(map);
                        System.out.println("refresh" + map.toString());
                    }
                    oos.flush();
                }

            } catch (UnknownHostException e) {
                System.out.println("* smth fishy about host " + hostName + " *");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("* cannot connect to " + hostName + " *");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ie) {
                    System.out.println("* interrupted *");
                }
            }
        }

    }
}