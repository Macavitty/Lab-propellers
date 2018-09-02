package lab_4.client;

import lab_4.PropellerCollection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.rmi.UnknownHostException;
import java.util.Scanner;

public class Client {

    public  static void main(String []args) {

        String hostName = "localhost";
        int port = 4004;

        Scanner mainScanner = new Scanner(System.in);

        PropellerCollection collection = new PropellerCollection();

        while (!Thread.currentThread().isInterrupted()) {

            try (SocketChannel socketChannel = SocketChannel.open()) {

                socketChannel.configureBlocking(true);

                socketChannel.connect(new InetSocketAddress(hostName, port));

                ObjectInputStream ois = new ObjectInputStream(socketChannel.socket().getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socketChannel.socket().getOutputStream());
                String userCommand;

                System.out.println((String)ois.readObject());

                while(!Thread.currentThread().isInterrupted()){
                    userCommand = mainScanner.nextLine();
                    if (userCommand.equals("q")) {
                        System.out.println("* Have a good day *");
                        System.exit(0);
                    }
                    oos.writeObject(userCommand); // send command to server

                    System.out.println(ois.readObject());

 /*                   Object obj = ois.readObject();
                    if(obj instanceof Map) {
                        Map<String, Object> dataMap = (Map<String, Object>) obj;

                        System.out.println(dataMap.toString());
                    } else {
                        System.out.println(ois.readObject());
                        System.out.println(obj);
                    }*/

                    oos.flush();

                    //parseCommand(userCommand);
                }

            } catch (UnknownHostException e) { System.out.println("* smth fishy about host " + hostName + " *");
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("* cannot connect to " + hostName + " *");
                try{
                    Thread.sleep(2000);
                }catch(InterruptedException ie) {
                    System.out.println("* interrupted *");
                }
            }
        }

    }
}
