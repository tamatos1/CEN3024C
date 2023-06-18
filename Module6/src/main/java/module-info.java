module com.example.module6 {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens com.example.module6 to javafx.fxml;
    exports com.example.module6;
}