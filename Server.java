//az geek for geeks va link haye mojud dar doc baraye piade sazi estefade kardam
//https://www.geeksforgeeks.org/introducing-threads-socket-programming-java/
//https://www.geeksforgeeks.org/multi-threaded-chat-application-set-1/

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
    static ArrayList<ClientHandler> clients = new ArrayList<>();
    static int clientCounter = 1;
    public static void main(String[] args) throws IOException{
        ServerSocket server = new ServerSocket(4321);
        System.out.println("Chat server is running on port 4321");
        Socket socket;

        while(true){
            socket = server.accept();
            System.out.println("New client request recieved : " + socket);

            DataInputStream input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            ClientHandler match = new ClientHandler(socket, "User" + clientCounter, input, output);

            Thread t = new Thread(match);
            clients.add(match);
            t.start();

            clientCounter++;
        }
    }
}
class ClientHandler extends Thread{
    Scanner scan = new Scanner(System.in);
    final DataInputStream input;
    final DataOutputStream output;
    Socket s;
    String clientName;
    boolean isLogged;

    public ClientHandler(Socket s, String clientName, DataInputStream in, DataOutputStream out){
        this.input = in;
        this.output = out;
        this.clientName = clientName;
        this.s = s;
        this.isLogged = true;
    }

    @Override
    public void run() {
        String msgReceived;
        while (true){
            try{
                msgReceived = input.readUTF();

                if(msgReceived.equals("logout")){
                    this.isLogged = false;
                    this.s.close();
                    break;
                }

                output.writeUTF(this.clientName + ": " + msgReceived);

            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        try{
            this.input.close();
            this.output.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
