// A Java program for a Server
import java.net.*;
import java.io.*;

public class PrimeNumberCheckerServer
{
    //initialize socket and input stream
    private Socket          socket   = null;
    private ServerSocket    server   = null;
    private DataInputStream in       =  null;
    private DataOutputStream out = null;


    // constructor with port
    public PrimeNumberCheckerServer(int port) {
        // starts server and waits for a connection
        try
        {
            server = new ServerSocket(port);
            System.out.println("Server started");

            System.out.println("Waiting for a client ...");

            socket = server.accept();
            System.out.println("Client accepted");

            // takes input from the client socket
            in = new DataInputStream(
                    new BufferedInputStream(socket.getInputStream()));

            out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

            String line = "";

            // reads message from client until "Over" is sent
            while (!line.equals("Over"))
            {
                try
                {
                    line = in.readUTF();

                    if (!line.equals("Over")) {
                        System.out.println("From client: " + line);
                        int number = Integer.parseInt(line);
                        String message = "";

                        if(number >=0 ) {
                            boolean flag =  isPrime(number);
                            message = number  + " is " + (flag ? "" : "not ") + "a prime number.";
                            System.out.println(message);
                        } else {
                            message = line + " is not a number";
                            System.out.println(message);
                        }

                        out.writeUTF(message);
                        out.flush();
                    }




                }
                catch(IOException i)
                {
                    System.out.println(i);
                }
            }
            System.out.println("Closing connection");

            // close connection
            socket.close();
            in.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }

    private static boolean isPrime(int number) {
        if (number <= 1)
            return false;
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0)
                return false;
        }
        return true;
    }


    public static void main(String args[])
    {
        PrimeNumberCheckerServer primeNumberCheckerServer = new PrimeNumberCheckerServer(8000);
    }
}