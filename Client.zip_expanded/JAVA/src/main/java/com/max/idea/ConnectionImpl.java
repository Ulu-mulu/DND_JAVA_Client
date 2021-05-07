package com.max.idea;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionImpl implements Connection, Runnable{
    private Socket socket;
    private ConnectionListener connectionListener;
    private boolean needToRun = true;
    private OutputStream out;
    private InputStream in;

    public ConnectionImpl(Socket socket, ConnectionListener connectionListener)
    {
        try {
            this.socket = socket;
            this.connectionListener = connectionListener;
            in =  socket.getInputStream();
            out = socket.getOutputStream();
            Thread t = new Thread(this);
            t.setPriority(Thread.MIN_PRIORITY);
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void send(String msg) {
        try {
            ObjectOutputStream objOut = new ObjectOutputStream(out);
            byte [] box = msg.getBytes(StandardCharsets.UTF_8);
            System.out.println(box.toString());
            out.write((":msg:"+msg).getBytes(StandardCharsets.UTF_8));
            out.flush();

            //objOut.write((msg+"\n").getBytes(StandardCharsets.UTF_8));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void close() {
        needToRun = false;
    }

    @Override
    public void run() {
        while(needToRun)
        {
            try {

                int amount = in.available();
                if(amount > 0)
                {

                    byte [] box = new byte[amount];
                    in.read(box);
                    String msg = new String(box,"UTF-8");
                    System.out.println(msg);
                    connectionListener.recievedContent(msg);
                }
                else
                {
                    Thread.sleep(200);
                }

            } catch (IOException  | InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
