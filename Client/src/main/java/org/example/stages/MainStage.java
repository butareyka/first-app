package org.example.stages;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import org.example.Client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainStage implements Initializable {

    @FXML
    private ChoiceBox<String> languagesVariants;

    @FXML
    private Text helloText;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        languagesVariants.getItems().setAll("Russian", "Slovenian", "Ukrainian", "Spanish (Puerto Rico)");
        languagesVariants.getSelectionModel().getSelectedItem();
        helloText.setText("Hello, " + Client.clientRequester.getUserName());
    }

    @FXML
    void logOut(MouseEvent event) {

    }

    @FXML
    void switchToIntro(MouseEvent event) throws IOException {
        Client.setRoot("intro");
    }

    @FXML
    void switchToLogIn(MouseEvent event) throws IOException {
        Client.setRoot("logIn");
    }

    @FXML
    void switchToCommands(MouseEvent event) throws IOException {
        Client.setRoot("commands");
    }

    @FXML
    void switchToSignUp(MouseEvent event) throws IOException {
        Client.setRoot("signUp");
    }

    public void setUsername(String username) {
    }
}
