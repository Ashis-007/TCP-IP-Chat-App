import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private String ip;
    private int portNo;
    private Socket soc;
    private ServerSocket serversoc;

    private DataOutputStream sender;      //sending msgs
    private Scanner input;           //input from keyboard
    private DataInputStream reader;   //reading incoming msgs

    private String request = "";
    private String response = "";

    Server() throws IOException {
        ip = "localhost";
        portNo = 2323;
        serversoc = new ServerSocket(portNo);
        soc = serversoc.accept();

        sender = new DataOutputStream(soc.getOutputStream());
        input = new Scanner(System.in);
        reader = new DataInputStream(soc.getInputStream());
        System.out.println("[SERVER] Connected!");
    }

    //send response to the client
    public void sendMsg() throws IOException
    {
        response = input.nextLine();
        sender.writeUTF("[SERVER] " + response);
    }

    //read and display the msg from client
    public void receiveMsg() throws IOException {
        request = reader.readUTF();
        System.out.println(request);
    }

    private void closeServer() {
        try {
            soc.close();
            serversoc.close();
            reader.close();
            sender.close();
            System.out.println("[SERVER] Disconnected");
            System.out.println("[SERVER] Shutting down");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception{

        System.out.println("[SERVER] Initialised");
        System.out.println("[SERVER] Waiting for connection ...");

        Server server = new Server();

        Thread receiveRequest = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    while(true){
                        server.receiveMsg();

                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                finally {
                    server.closeServer();
                    System.exit(1);
                }
            }
        });

        Thread sendResponse = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    while(true){
                        server.sendMsg();

                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                finally {
                    server.closeServer();
                    System.exit(1);
                }
            }
        });

        sendResponse.start();
        receiveRequest.start();
    }
}
