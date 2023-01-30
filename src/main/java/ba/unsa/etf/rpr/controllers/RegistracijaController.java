package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.dao.KorisnikDaoSQLImpl;
import ba.unsa.etf.rpr.domain.Korisnik;
import ba.unsa.etf.rpr.exceptions.DigitronException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegistracijaController {
    public TextField textFld;
    public PasswordField passwordFld;
    public Label userNameLabel;
    public Label passwordLabel;
    private KorisnikDaoSQLImpl korisnikDaoSQLImpl;

    @FXML
    public void initialize() {
        textFld.textProperty().addListener((observableValue, o, n) -> {
            if(n.strip().length() < 8) {
                textFld.getStyleClass().removeAll("poljeJeIspravno");
                textFld.getStyleClass().add("poljeNijeIspravno");
                userNameLabel.setText("At least 8 characters");
            }
            else {
                textFld.getStyleClass().removeAll("poljeNijeIspravno");
                textFld.getStyleClass().add("poljeJeIspravno");
                userNameLabel.setText("");
            }
        });
        passwordFld.textProperty().addListener((observableValue, o, n) -> {
                if(n.strip().length() < 8) {
                    passwordFld.getStyleClass().removeAll("poljeJeIspravno");
                    passwordFld.getStyleClass().add("poljeNijeIspravno");
                    passwordLabel.setText("At least 8 characters");
                }
                else {
                    passwordFld.getStyleClass().removeAll("poljeNijeIspravno");
                    passwordFld.getStyleClass().add("poljeJeIspravno");
                    passwordLabel.setText("");
                }
            }
        );
    }

    public void onBtnClicked(ActionEvent actionEvent) throws DigitronException {
        korisnikDaoSQLImpl = new KorisnikDaoSQLImpl();
        Korisnik korisnik = new Korisnik();
        korisnik.setMode(true);
        korisnik.setUsername(textFld.getText());
        korisnik.setPassword(passwordFld.getText());
        korisnik = korisnikDaoSQLImpl.add(korisnik);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registration Status:");
        alert.setContentText("Uspješno ste se registrovali!");
        alert.showAndWait();
        Stage stage = (Stage)textFld.getScene().getWindow();
        stage.close();
    }
}
