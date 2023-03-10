package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.business.KorisnikManager;
import ba.unsa.etf.rpr.dao.DaoFactory;
import ba.unsa.etf.rpr.domain.Korisnik;
import ba.unsa.etf.rpr.exceptions.DigitronException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * JavaFX controller for log in form
 *
 * @author ksljivo1
 */

public class LogInController {
    public TextField textFld;
    public PasswordField passwordFld;
    private KorisnikManager korisnikManager = new KorisnikManager();

    public void onBtnKreirajNoviClicked(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/registracija.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Registration form");
        stage.setScene(new Scene(root, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE));
        stage.setResizable(false);
        stage.show();
    }

    public void onPrijaviSeBtnClicked(ActionEvent actionEvent) throws DigitronException, IOException {
        Korisnik korisnik = DaoFactory.korisnikDao().getKorisnikByUsername(textFld.getText());
        if(!korisnikManager.comparePasswords(korisnik, passwordFld.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login failed");
            alert.setContentText("Username or password is wrong!");
            alert.showAndWait();
        }
        else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/digitron.fxml"));
            Parent root = loader.load();
            DigitronController digitronController = loader.getController();
            digitronController.setKorisnik(korisnik);

            Stage stage = new Stage();
            stage.setTitle("Digitron");
            Scene scene = new Scene(root, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
            stage.setScene(scene);
            stage.setMinWidth(600);
            stage.setMinHeight(400);

            List<KeyCode> keyCodes = new ArrayList<>();
            scene.setOnKeyPressed(event -> {
                KeyCode codeString = event.getCode();
                keyCodes.add(codeString);
                if(codeString.equals(KeyCode.ADD)) digitronController.plusBtnClicked();
                else if(codeString.equals(KeyCode.SUBTRACT)) digitronController.minusBtnClicked();
                else if(codeString.equals(KeyCode.MULTIPLY)) digitronController.multipliesBtnClicked();
                else if(codeString.equals(KeyCode.DIVIDE)) digitronController.dividesBtnClicked();
                else if(codeString.isKeypadKey()) {
                    Button button = (Button) scene.lookup("#btn" + codeString.toString().charAt(codeString.toString().length() - 1));
                    button.fire();
                }
                else if(codeString.equals(KeyCode.C)) digitronController.cBtnClicked();
                else if(codeString.equals(KeyCode.BACK_SPACE)) digitronController.backspaceClick();
                else if(codeString.equals(KeyCode.DECIMAL)) digitronController.dotBtnClicked();
                else if(codeString.equals(KeyCode.LEFT_PARENTHESIS) || (
                        (keyCodes.size() >= 2 && keyCodes.get(keyCodes.size() - 1).equals(KeyCode.DIGIT9))
                                && keyCodes.get(keyCodes.size() - 2).equals(KeyCode.SHIFT))) digitronController.leftParenthesisClick();
                else if(codeString.equals(KeyCode.LEFT_PARENTHESIS) || (
                        (keyCodes.size() >= 2 && keyCodes.get(keyCodes.size() - 1).equals(KeyCode.DIGIT0))
                                && keyCodes.get(keyCodes.size() - 2).equals(KeyCode.SHIFT))) digitronController.rightParenthesisClick();
                else if(codeString.equals(KeyCode.E)) {
                    try {
                        digitronController.equalsBtnClicked();
                    } catch (DigitronException e) {
                        e.printStackTrace();
                    }
                }
                else ;
            });

            stage.show();
        }
    }
}
