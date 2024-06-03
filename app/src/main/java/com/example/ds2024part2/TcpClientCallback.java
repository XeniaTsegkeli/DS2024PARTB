package com.example.ds2024part2;

import java.util.List;
import model.Property;

public interface TcpClientCallback {
    void onPropertiesReceived(List<Property> properties);
}