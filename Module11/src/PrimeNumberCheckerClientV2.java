import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class PrimeNumberCheckerClientV2 extends Application {
    // initialize socket and input output streams
    private static Socket socket = null;
    private static DataInputStream inputFromTerminal = null;
    private static DataInputStream inputFromServer = null;
    private static DataOutputStream out = null;


    public static void main(String[] args) {

        launch(args);

    }

    public static String getResults(int number) {
        String message = "";



        try {

            out.writeUTF(Integer.toString(number));
            message = "From server: " + inputFromServer.readUTF() + "\n";
            System.out.print(message);
        }
        catch (IOException i) {
            System.out.println(i);
        }


        return  message;
    }


    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PrimeNumberCheckerClientV2.class.getResource("PrimeNumberCheckerClient.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 470, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Module 11 Assignment - Client");
        primaryStage.show();

        primaryStage.setOnCloseRequest(windowEvent -> {

            Platform.exit();
            // close the connection
            try {
                // sends output to the socket
                out.writeUTF("Over");


                //inputFromTerminal.close();
                inputFromServer.close();
                out.close();
                socket.close();
            }
            catch (IOException i) {
                System.out.println(i);
            }
        });

        //startServer();
        // establish a connection
        try {
            socket = new Socket("localhost", 8000);
            System.out.println("Connected");

            // takes input from server
            inputFromServer = new DataInputStream(socket.getInputStream());

            // sends output to the socket
            out = new DataOutputStream(
                    socket.getOutputStream());
        }
        catch (UnknownHostException u) {
            System.out.println(u);

        }
        catch (IOException i) {
            System.out.println(i);

        }
    }
}
