import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class PrimeNumberCheckerClientController {

    @FXML
    private Button btnCheck;

    @FXML
    private TextArea txtResutls;

    @FXML
    private TextField txtNumber;

    @FXML
    void getPrimeNumber(ActionEvent event) {
        int number = 0;
        String message = "";

        number = Integer.parseInt(txtNumber.getText());

        message = PrimeNumberCheckerClientV2.getResults(number);
        txtResutls.appendText(message);


    }

}