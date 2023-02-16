package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.business.KorisnikManager;
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
    private KorisnikManager korisnikManager = new KorisnikManager();

    @FXML
    public void initialize() {
        textFld.textProperty().addListener((observableValue, o, n) -> {
            int length = n.strip().length();
            if(length < 8 || length > 255) updateStyle(textFld, POLJE_JE_ISPRAVNO, POLJE_NIJE_ISPRAVNO);
            else updateStyle(textFld, POLJE_NIJE_ISPRAVNO, POLJE_JE_ISPRAVNO);

        });
        passwordFld.textProperty().addListener((observableValue, o, n) -> {
            int length = n.strip().length();
                if(length < 8 || length > 255) updateStyle(passwordFld, POLJE_JE_ISPRAVNO, POLJE_NIJE_ISPRAVNO);
                else updateStyle(passwordFld, POLJE_NIJE_ISPRAVNO, POLJE_JE_ISPRAVNO);
            }
        );
    }

    private void updateStyle(TextField textFld, String remove, String add) {
        textFld.getStyleClass().removeAll(remove);
        textFld.getStyleClass().add(add);
    }

    public void onBtnClicked(ActionEvent actionEvent) throws DigitronException {
        try {
            Korisnik korisnik = new Korisnik();
            korisnik.setMode(true);
            korisnik.setUsername(textFld.getText().strip());
            korisnik.setPassword(passwordFld.getText().strip());
            korisnik = korisnikManager.add(korisnik);

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
        catch(DigitronException digitronException) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(digitronException.getMessage());
            alert.showAndWait();
        }
    }

    public void onCloseBtnClicked(ActionEvent actionEvent) {
        final Node source = (Node) actionEvent.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
