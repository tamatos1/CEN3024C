module com.example.module11v2 {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens com.example.module11v2 to javafx.fxml;
    exports com.example.module11v2;
}