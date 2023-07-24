import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class PrimeNumberCheckerServerV2 extends Application {
    //initialize socket and input stream
    private Socket socket   = null;
    private ServerSocket server   = null;
    private DataInputStream in       =  null;
    private DataOutputStream out = null;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PrimeNumberCheckerServerV2.class.getResource("PrimeNumberCheckerServer.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 470, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Module 11 Assignment - Server");
        primaryStage.show();

        primaryStage.setOnCloseRequest(windowEvent -> {

            Platform.exit();
            // close the connection
            try {
                in.close();
                out.close();
                socket.close();
            }
            catch (IOException i) {
                System.out.println(i);
            }
        });
        // starts server and waits for a connection
        try
        {
            server = new ServerSocket(8000);
            PrimeNumberCheckerServerController.setTxtMessages("Server started" + "\n");
            System.out.println("Server started");

            PrimeNumberCheckerServerController.setTxtMessages("Waiting for a client ..." + "\n");
            System.out.println("Waiting for a client ...");

            socket = server.accept();
            PrimeNumberCheckerServerController.setTxtMessages("Client accepted" + "\n");
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
                        PrimeNumberCheckerServerController.setTxtMessages("From client: " + line + "\n");
                        System.out.println("From client: " + line);
                        int number = Integer.parseInt(line);
                        String message = "";

                        if(number >=0 ) {
                            boolean flag =  isPrime(number);
                            message = number  + " is " + (flag ? "" : "not ") + "a prime number.";
                            PrimeNumberCheckerServerController.setTxtMessages(message + "\n");
                            System.out.println(message);
                        } else {
                            message = line + " is not a number";
                            PrimeNumberCheckerServerController.setTxtMessages(message + "\n");
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
            PrimeNumberCheckerServerController.setTxtMessages("Closing connection" + "\n");
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
}
