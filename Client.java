import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private String ip;
    private int portNo;
    private Socket soc;

    private DataOutputStream sender;  //sending msg
    private Scanner input;            //input from keyboard
    private DataInputStream reader;   //reading incoming msgs

    private String request = "";
    private String response = "";

    Client() throws IOException {
        ip = "localhost";
        portNo = 2323;
        soc = new Socket(ip, portNo);

        sender = new DataOutputStream(soc.getOutputStream());
        input = new Scanner(System.in);
        reader = new DataInputStream(soc.getInputStream());
        System.out.println("====Connected with server====");
    }

    public void sendMsg() throws IOException
    {
        request = input.nextLine();
        sender.writeUTF(request);
    }

    public void receiveMsg() throws IOException {
        response = reader.readUTF();
        System.out.println(response);
    }

    private void closeClient() {
        try {
            soc.close();
            reader.close();
            sender.close();
            System.out.println();
            System.out.println("====Disconnected from Server====");
            System.out.println("====Application closed====");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception{

        System.out.println("====Application started====");
        Client client = new Client();

        Thread senderThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    while(true){
                        client.sendMsg();

                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                finally {
                    client.closeClient();
                    System.exit(1);
                }

            }
        });

        Thread receiverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    while(true){
                        client.receiveMsg();
                    }
                }catch(Exception e){

                }
                finally {
                    client.closeClient();
                    System.exit(1);
                }
            }
        });

        senderThread.start();
        receiverThread.start();
    }

}
