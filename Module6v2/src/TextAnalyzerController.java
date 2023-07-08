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


    /**
     * This method runs after the 'Generate' button is clicked.  It
     * gets all the options the user wants to use from the UI and then
     * calls the TextAnalyzer application
     */
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

    /**
     * This method runs after the user closes the UI application.  It
     * closes the current JavaFX window.
     */
    @FXML
    void onCloseApplicationClick() {
        // get a handle to the stage;
        Stage stage = (Stage) mnuBar.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

    /**
     * This method runs after the user select the option to use default
     * values.  This automatically inserts default values onto the UI.
     */
    @FXML
    void onUseDefaultsClick() {
        txtSourceUrl.setText("https://www.gutenberg.org/files/1065/1065-h/1065-h.htm");
        txtHtmlOfTitle.setText("<h1>The Raven</h1>");
        txtHtmlOfEnd.setText("</div><!--end chapter-->");
        grpInputType.selectToggle(rdoSourceUrl);
        //rdoSourceUrl.fireEvent(onRadioButtonClick());
        onRadioButtonClick();


    }

    /**
     * This method runs after the user clicks a radio button. Once clicked,
     * the button used to analyze the text gets enabled.
     */
    @FXML
    void onRadioButtonClick() {
        btnAnalyze.setDisable(false);
    }

    /**
     * This method runs after the user selects the number of results
     * to return.  This value will be used when generating the list
     * of unique words.
     */
    @FXML
    void onResultsMenuItemClicked() {
        RadioMenuItem selectedButton = (RadioMenuItem) grpMaxReturnCount.getSelectedToggle();
        topN = Integer.parseInt(selectedButton.getText().replace("Top ", ""));
        //System.out.println(topN);
    }

    /**
     * This class creates an object that has the word, frequency, and the row.
     */
    public class WordFrequency {
        SimpleStringProperty word;
        SimpleIntegerProperty frequency;
        SimpleIntegerProperty row;

        /**
         * Creates a word frequency object with a word, the frequency, and the row.
         *
         * @param  word  the unique word
         * @param  frequency the number of times the word appears
         * @param  row the row that the word is found in a list of unique words
         */
        WordFrequency(String word, Integer frequency, Integer row) {
            this.word = new SimpleStringProperty(word);
            this.frequency = new SimpleIntegerProperty(frequency);
            this.row = new SimpleIntegerProperty(row);
        }

        /**
         * Gets the frequency from the current wordFrequency object.
         *
         * @return   the integer that represents the frequency that a word appears
         */
        public Integer getFrequency() {
            return frequency.get();
        }

        /**
         * Sets the frequency from the current wordFrequency object.
         *
         * @param value    the integer that represents the frequency that a word appears
         */
        public void setFrequency(Integer value) {
            frequency.set(value);
        }

        /**
         * Gets the row from the current wordFrequency object.
         *
         * @return   the integer that represents the row where the word appears in a list
         */
        public Integer getRow() {
            return row.get();
        }

        /**
         * Sets the row from the current wordFrequency object.
         *
         * @param value    the integer that represents the row where the word appears in a list
         */
        public void setRow(Integer value) {
            row.set(value);
        }

        /**
         * Gets the word from the current wordFrequency object.
         *
         * @return   the word
         */
        public String getWord() {
            return word.get();
        }

        /**
         * Sets the word from the current wordFrequency object.
         *
         * @param value    the unique word
         */
        public void setWord(String value) {
            word.set(value);
        }
    }

}


