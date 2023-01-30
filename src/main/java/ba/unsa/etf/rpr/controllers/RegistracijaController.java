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
    private boolean neispravanUnos = true;
    private boolean duplikatUBazi = false;

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
        duplikatUBazi = korisnikDaoSQLImpl.getKorisnikByUsername(textFld.getText()) != null;
        neispravanUnos = textFld.getText().strip().length() < 8 || passwordFld.getText().strip().length() < 8;
        if(neispravanUnos || duplikatUBazi) {
            String poruka;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Registration failed");
            if(neispravanUnos && duplikatUBazi)
                poruka = "Username or password is not long enough and" +
                        " account with entered username already exists!";
            else if(!neispravanUnos) poruka = "Account with entered username already exists!";
            else poruka = "Username or password is not long enough!";
            alert.setContentText(poruka);
            alert.showAndWait();
        }
        else {
            Korisnik korisnik = new Korisnik();
            korisnik.setMode(true);
            korisnik.setUsername(textFld.getText());
            korisnik.setPassword(passwordFld.getText());
            korisnik = korisnikDaoSQLImpl.add(korisnik);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registration Status");
            alert.setContentText("Congratulations, your account has been successfully created!");
            alert.showAndWait();
            Stage stage = (Stage) textFld.getScene().getWindow();
            stage.close();
        }
    }
}
