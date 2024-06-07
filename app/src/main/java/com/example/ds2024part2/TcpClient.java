package com.example.ds2024part2;


import model.Booking;
import model.Filters;
import model.Property;
import model.RoomRating;

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

    public void sendJsonOverTcp(Filters filters, String uuid, int function) {
        new Thread(() -> {
            try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
                 DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                 ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {

                dataOutputStream.writeUTF("Renter");
                dataOutputStream.writeUTF(uuid);
                dataOutputStream.writeInt(function);
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

    public String sendBookingOverTcp(Booking booking, String uuid, int function) {
        final String[] result = new String[1];
        final Object lock = new Object();

        Thread thread = new Thread(() -> {
            try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
                 DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                 ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {

                dataOutputStream.writeUTF("Renter");
                dataOutputStream.writeUTF(uuid);
                dataOutputStream.writeInt(function);
                dataOutputStream.flush();

                outputStream.writeObject(booking);
                outputStream.flush();

                // Read the result from the input stream
                Object response = ois.readObject();
                synchronized (lock) {
                    result[0] = response.toString();
                    lock.notify();
                }

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        synchronized (lock) {
            thread.start();
            try {
                lock.wait(); // Wait for the thread to finish and notify
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return result[0];
    }

    public void sendAccomodationRating(RoomRating roomRating, String uuid, int function) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
                     DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                     ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                     ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {

                    dos.writeUTF("Renter");
                    dos.writeUTF(uuid);
                    dos.writeInt(function);
                    dos.flush();

                    oos.writeObject(roomRating);
                    oos.flush();

                    // Read the server's response
                    String response = ois.readUTF();
                    if (callback != null) {
                        callback.onRatingResponseReceived(response.toString());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}