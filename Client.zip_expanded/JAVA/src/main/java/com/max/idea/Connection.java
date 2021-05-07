package com.max.idea;

public interface Connection {

    public static final int PORT = 9090;

    public void send(String msg);

    public void  close();

}
