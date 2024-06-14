import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client
{
    final static int ServerPort = 4321;
    private DataInputStream input;
    private DataOutputStream output;
    private Scanner scan;

    public Client(){
        try{
            scan = new Scanner(System.in);

            Socket s = new Socket("localhost", ServerPort);

            input = new DataInputStream(s.getInputStream());
            output = new DataOutputStream(s.getOutputStream());
        } catch (IOException e){
            e.printStackTrace();
        }

        Thread sendMessage = new Thread(new Runnable()
        {
            @Override
            public void run() {
                while (true) {

                    // read the message to deliver.
                    String msg = scan.nextLine();

                    try {
                        // write on the output stream
                        output.writeUTF(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        // readMessage thread
        Thread readMessage = new Thread(new Runnable()
        {
            @Override
            public void run() {

                while (true) {
                    try {
                        // read the message sent to this client
                        String msg = input.readUTF();
                        System.out.println(msg);
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }
            }
        });

        sendMessage.start();
        readMessage.start();

    }

    public static void main(String args[]) throws UnknownHostException, IOException
    {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the number of clients");
        int n = scan.nextInt();
        for(int i = 0; i < n; i++)
            new Client();

        System.out.println("Connected to chat server");
    }
}
