// A Java program for a Client
import java.io.*;
import java.net.*;

public class PrimeNumberCheckerClient {
    // initialize socket and input output streams
    private Socket socket = null;
    private DataInputStream inputFromTerminal = null;
    private DataInputStream inputFromServer = null;
    private DataOutputStream out = null;

    // constructor to put ip address and port
    public PrimeNumberCheckerClient(String address, int port) {
        // establish a connection
        try {
            socket = new Socket(address, port);
            System.out.println("Connected");

            // takes input from terminal
            inputFromTerminal = new DataInputStream(System.in);

            // takes input from server
            inputFromServer = new DataInputStream(socket.getInputStream());

            // sends output to the socket
            out = new DataOutputStream(
                    socket.getOutputStream());
        }
        catch (UnknownHostException u) {
            System.out.println(u);
            return;
        }
        catch (IOException i) {
            System.out.println(i);
            return;
        }

        // string to read message from input
        String line = "";

        // keep reading until "Over" is input
        while (!line.equals("Over")) {
            try {
                line = inputFromTerminal.readLine();
                out.writeUTF(line);
                System.out.println("From server: " + inputFromServer.readUTF());
            }
            catch (IOException i) {
                System.out.println(i);
            }
        }

        // close the connection
        try {
            inputFromTerminal.close();
            out.close();
            socket.close();
        }
        catch (IOException i) {
            System.out.println(i);
        }
    }

    public static void main(String args[])
    {
        PrimeNumberCheckerClient primeNumberCheckerClient = new PrimeNumberCheckerClient("localhost", 8000);
    }
}
