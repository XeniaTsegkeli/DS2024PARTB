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
    private TcpClientCallback callback;

    public TcpClient(TcpClientCallback callback) {
        this.callback = callback;
    }

    public void sendJsonOverTcp(Filters filters, String uuid) {
        new Thread(() -> {
            try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
                 DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                 ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {

                dataOutputStream.writeUTF("Renter");
                dataOutputStream.writeUTF(uuid);
                dataOutputStream.writeInt(2);
                dataOutputStream.flush();

                outputStream.writeObject(filters);
                outputStream.flush();

                List<Property> properties = ((HashMap<String, List<Property>>) ois.readObject()).get(uuid);
                if (callback != null) {
                    callback.onPropertiesReceived(properties);
                }

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}