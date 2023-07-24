import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class PrimeNumberCheckerServerController {

    @FXML
    private static TextArea txtMessages;

    public static void setTxtMessages(String message) {
        txtMessages.appendText(message + "n/");
    }

}
