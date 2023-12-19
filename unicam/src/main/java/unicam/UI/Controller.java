package unicam.UI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;

//ci riportiamo dentro, tutti gli elementi che abbiamo creato nel file fxml (stessa tipologia), es Label welcomeText
// quindi ho bisogno dello stesso id nello scene builder
public class Controller {
    @FXML
    private Label welcomeText;

    @FXML
    private Button button;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    private void onMouseOver() {
        Effect shadow = new DropShadow();
        button.setEffect(shadow);
        System.out.println("Mouse over");
    }

    @FXML
    private void onMouseExit() {
        button.setEffect(null);
        System.out.println("Mouse exit");
    }
}