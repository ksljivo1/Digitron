package ba.unsa.etf.rpr.models;

import ba.unsa.etf.rpr.domain.Racun;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RacuniModel {
    private ObservableList<RacunModel> racuni = FXCollections.observableArrayList();

    public ObservableList<RacunModel> getRacuni() {
        return racuni;
    }

    public void setRacuni(ObservableList<RacunModel> racuni) {
        this.racuni = racuni;
    }
}
