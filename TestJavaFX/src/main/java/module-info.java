module com.unicam.testjavafx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.unicam.testjavafx to javafx.fxml;
    exports com.unicam.testjavafx;
}