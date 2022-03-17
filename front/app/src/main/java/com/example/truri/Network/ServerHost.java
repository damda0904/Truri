package com.example.truri.Network;

public class ServerHost {
    private String spring_port = ":8080";
    private String flask_port = ":5000";
    private String localhost = "http://10.0.2.2";
    private String phone_ip = "http://192.168.0.2:8080";

    public String getHost_url() {
        return phone_ip;
    }

    public String getHost_url(String server) {
        if(server.equals("spring")) {
            return localhost + spring_port;
        } else {
            return localhost + flask_port;
        }
    }

    public String getLocalhost() {
        return localhost;
    }

    public String getSpring_port() {
        return spring_port;
    }

    public String getFlask_port() {
        return flask_port;
    }
}
