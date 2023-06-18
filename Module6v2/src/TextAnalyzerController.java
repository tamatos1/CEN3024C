import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;


import java.io.IOException;
import java.util.*;

public class TextAnalyzerController {
    private Integer topN = 5;

    @FXML
    private MenuBar mnuBar;

    @FXML
    private MenuItem mItmFileClose;

    @FXML
    private Menu mnuResults;


    @FXML
    private Button btnAnalyze;

    @FXML
    private ToggleGroup grpInputType;

    @FXML
    private ToggleGroup grpMaxReturnCount;

    @FXML
    private Label lblHtmlOfEnd;

    @FXML
    private Label lblHtmlOfTitle;

    @FXML
    private Label lblSourceUrl;

    @FXML
    private RadioButton rdoSourceTextbox;

    @FXML
    private RadioButton rdoSourceUrl;



    @FXML
    private TextField txtHtmlOfEnd;

    @FXML
    private TextField txtHtmlOfTitle;

    @FXML
    private TextArea txtSourceTextbox;

    @FXML
    private TextField txtSourceUrl;


    @FXML
    private TableColumn<?, ?> tblcCount;

    @FXML
    private TableColumn<?, ?> tblcRow;

    @FXML
    private TableColumn<?, ?> tblcWord;

    @FXML
    private TableView<WordFrequency> tblvwFrequency;



    @FXML
    void onClicked() throws IOException {
        int i = 1;
        tblvwFrequency.getSortOrder().add((TableColumn<WordFrequency, ?>) tblcCount);
        ObservableList<WordFrequency> data = FXCollections.observableArrayList();
        Map myMap = new TreeMap() {
        };

        onResultsMenuItemClicked();

        RadioButton selectedButton = (RadioButton) grpInputType.getSelectedToggle();
        switch (selectedButton.getText())
        {
            case "Using URL":
                myMap = TextAnalyzer.getListOfWordFrequencyUsingURL(txtSourceUrl.getText(), txtHtmlOfTitle.getText(), txtHtmlOfEnd.getText(), topN);
                break;
            case "Using Textbox":
                myMap = TextAnalyzer.getListOfWordFrequencyUsingText(txtSourceTextbox.getText(), topN);
                break;
        }

        tblcWord.setCellValueFactory(new PropertyValueFactory("word"));
        tblcCount.setCellValueFactory(new PropertyValueFactory("frequency"));
        tblcRow.setCellValueFactory(new PropertyValueFactory("row"));

        Iterator<Map.Entry<String, Integer>> iterator = myMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Integer> pair = iterator.next();

            data.add(new WordFrequency(pair.getKey(), pair.getValue(), i));
            i++;

        }
        tblvwFrequency.setItems(data);

    }


    @FXML
    void onCloseApplicationClick() {
        // get a handle to the stage;
        Stage stage = (Stage) mnuBar.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

    @FXML
    void onUseDefaultsClick() {
        txtSourceUrl.setText("https://www.gutenberg.org/files/1065/1065-h/1065-h.htm");
        txtHtmlOfTitle.setText("<h1>The Raven</h1>");
        txtHtmlOfEnd.setText("</div><!--end chapter-->");
        grpInputType.selectToggle(rdoSourceUrl);
        //rdoSourceUrl.fireEvent(onRadioButtonClick());
        onRadioButtonClick();


    }

    @FXML
    void onRadioButtonClick() {
        btnAnalyze.setDisable(false);
    }

    @FXML
    void onResultsMenuItemClicked() {
        RadioMenuItem selectedButton = (RadioMenuItem) grpMaxReturnCount.getSelectedToggle();
        topN = Integer.parseInt(selectedButton.getText().replace("Top ", ""));
        System.out.println(topN);
    }

    public class WordFrequency {
        SimpleStringProperty word;
        SimpleIntegerProperty frequency;
        SimpleIntegerProperty row;

        WordFrequency(String word, Integer frequency, Integer row) {
            this.word = new SimpleStringProperty(word);
            this.frequency = new SimpleIntegerProperty(frequency);
            this.row = new SimpleIntegerProperty(row);
        }

        public Integer getFrequency() {
            return frequency.get();
        }

        public void setFrequency(Integer value) {
            frequency.set(value);
        }

        public Integer getRow() {
            return row.get();
        }

        public void setRow(Integer value) {
            row.set(value);
        }

        public String getWord() {
            return word.get();
        }

        public void setWord(String value) {
            word.set(value);
        }
    }

}


