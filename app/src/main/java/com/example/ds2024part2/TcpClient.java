package com.example.ds2024part2;


import model.Filters;
import model.Property;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;

public class TcpClient {

    private static final String SERVER_IP = "192.168.1.11";
    private static final int SERVER_PORT = 4321;

    public void sendJsonOverTcp(Filters filters, String uuid) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
                     DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                     ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                     DataOutputStream renterFunction = new DataOutputStream(socket.getOutputStream());
                     ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {

                    // Send mode, user, and function
//                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
//                    DataOutputStream renterFunction = new DataOutputStream(socket.getOutputStream());

                    dataOutputStream.writeUTF("Renter");
                    //dataOutputStream.flush();
                    dataOutputStream.writeUTF(uuid);
                    //dataOutputStream.flush();
                    renterFunction.writeInt(2);
                    dataOutputStream.flush();
                    renterFunction.flush();

                    // Convert JSON object to bytes and send over the socket
//                    String jsonString = jsonObject.toString();
//                    byte[] jsonData = jsonString.getBytes("UTF-8");
                    outputStream.writeObject(filters);
                    outputStream.flush();

                    List<Property> properties = ((HashMap<String, List<Property>>) ois.readObject()).get(uuid);
                    for (Property property : properties) {
                        System.out.println(property);
                    }

                    System.out.println("\n");

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}