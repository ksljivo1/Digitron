package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.dao.DaoFactory;
import ba.unsa.etf.rpr.dao.OmiljenaOperacijaDaoSQLImpl;
import ba.unsa.etf.rpr.domain.Korisnik;
import ba.unsa.etf.rpr.domain.OmiljenaOperacija;
import ba.unsa.etf.rpr.exceptions.DigitronException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class RegistracijaController {
    public static final String POLJE_JE_ISPRAVNO = "poljeJeIspravno";
    public static final String POLJE_NIJE_ISPRAVNO = "poljeNijeIspravno";
    public TextField textFld;
    public PasswordField passwordFld;
    private boolean neispravanUnos = true;
    private boolean duplikatUBazi = false;

    @FXML
    public void initialize() {
        textFld.textProperty().addListener((observableValue, o, n) -> {
            if(n.strip().length() < 8) updateStyle(textFld, POLJE_JE_ISPRAVNO, POLJE_NIJE_ISPRAVNO);
            else updateStyle(textFld, POLJE_NIJE_ISPRAVNO, POLJE_JE_ISPRAVNO);

        });
        passwordFld.textProperty().addListener((observableValue, o, n) -> {
                if(n.strip().length() < 8) updateStyle(passwordFld, POLJE_JE_ISPRAVNO, POLJE_NIJE_ISPRAVNO);
                else updateStyle(passwordFld, POLJE_NIJE_ISPRAVNO, POLJE_JE_ISPRAVNO);
            }
        );
    }

    private void updateStyle(TextField textFld, String remove, String add) {
        textFld.getStyleClass().removeAll(remove);
        textFld.getStyleClass().add(add);
    }

    public void onBtnClicked(ActionEvent actionEvent) throws DigitronException {
        duplikatUBazi = DaoFactory.korisnikDao().getKorisnikByUsername(textFld.getText()) != null;
        neispravanUnos = textFld.getText().strip().length() < 8 || passwordFld.getText().strip().length() < 8;
        if(neispravanUnos || duplikatUBazi) {
            String poruka;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Registration failed");
            if(neispravanUnos && duplikatUBazi) poruka = "Username or password is not long enough and account with entered username already exists!";
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
            korisnik = DaoFactory.korisnikDao().add(korisnik);

            OmiljenaOperacijaDaoSQLImpl omiljenaOperacijaDaoSQL = new OmiljenaOperacijaDaoSQLImpl();
            OmiljenaOperacija omiljenaOperacija = new OmiljenaOperacija();
            omiljenaOperacija.setIdKorisnik(korisnik.getId());
            omiljenaOperacija.setOperacija("");
            omiljenaOperacija.setBrojPonavljanja(0);
            omiljenaOperacija = omiljenaOperacijaDaoSQL.add(omiljenaOperacija);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registration Status");
            alert.setContentText("Congratulations, your account has been successfully created!");
            alert.showAndWait();
            Stage stage = (Stage) textFld.getScene().getWindow();
            stage.close();
        }
    }

    public void onCloseBtnClicked(ActionEvent actionEvent) {
        final Node source = (Node) actionEvent.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
