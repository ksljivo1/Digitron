package ba.unsa.etf.rpr.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DigitronController {
    public Label display;
    public double rez = 0;
    public ListView historyView;

    @FXML
    public void initialize() {
        Label praznaHistorija = new Label("Calculation history is empty");
        praznaHistorija.setTextFill(Color.SLATEBLUE);
        historyView.setPlaceholder(praznaHistorija);
    }

    // prva verzija funkcije koja parsira izraz, zatim ga racuna / baca izuzetak ukoliko izraz nije valjan ili se javlja dijeljenje nulom
    // podrzane su osnovne aritmeticke operacije (+, -, *, /)
    // podrzan je prioritet izvrsavanja operacija
    // nije podrzano koristenje zagrada
    // nije podrzan unarni minus

    private static String evaluate(String izraz) {
        try {
            Double.parseDouble(izraz);
            return izraz + ".0"; // ako je izraz vec broj, odmah se vraca
        }
        catch(Exception ignored) {

        }

        // lista koja sadrzi sve argumente osnovnih aritmetickih operacija koje se javljaju u izrazu
        List<String> listaArgumenata = new ArrayList<>(Arrays.asList(izraz.split(" \\+ | \\* | \\- | \\/ ")));
        // lista koja sadrzi sve osnovne aritmeticke operacije koje se javljaju u izrazu
        List<String> listaOperacija = new ArrayList<>(Arrays.asList(Arrays.stream(izraz.split("[+-]?([0-9]*[.]?[0-9]+|[0-9]+[.]?[0-9]*)"))
                                                                          .filter(s -> !(s.isBlank()))
                                                                          .toArray(String[]::new)));

        // posto su svi podrzani operatori binarni, valjan izraz ce uvijek imati broj operatora za jedan manji od broja argumenata
        if(listaOperacija.size() >= listaArgumenata.size()) return "ERROR: Invalid syntax";
        double rezultat = 0;
        try {
            for(int i = 0; i < listaArgumenata.size() - 1; i++) {
                double op1 = Double.parseDouble(listaArgumenata.get(i));
                if(i != 0) op1 = rezultat;
                int t = i + 1;
                while(t < listaOperacija.size() && (listaOperacija.get(t).trim().equals("*") || listaOperacija.get(t).trim().equals("/"))) {
                    if(listaOperacija.get(t).trim().equals("*"))
                        listaArgumenata.set(t,
                                String.valueOf(Double.parseDouble(listaArgumenata.get(t)) * Double.parseDouble(listaArgumenata.get(t + 1))));
                    else
                        listaArgumenata.set(t,
                                String.valueOf(Double.parseDouble(listaArgumenata.get(t)) / Double.parseDouble(listaArgumenata.get(t + 1))));
                    listaOperacija.remove(t);
                    listaArgumenata.remove(t + 1);
                }
                double op2 = Double.parseDouble(listaArgumenata.get(i + 1));
                String znak = listaOperacija.get(i).trim();
                if(znak.equals("+")) rezultat = (op1 + op2);
                else if(znak.equals("-")) rezultat = (op1 - op2);
                else if(znak.equals("*")) rezultat = (op1 * op2);
                else rezultat = (op1 / op2);
            }
            if(Double.valueOf(rezultat).isInfinite() || Double.valueOf(rezultat).isNaN()) return "ERROR: Division by zero";
            else return String.valueOf(rezultat);

        }
        catch (NumberFormatException e) {
            return "ERROR: Invalid syntax";
        }
    }

    public void btn0Click(ActionEvent actionEvent) {
        String tekst = display.getText();
        if(tekst.equals("0") || tekst.contains("=")) display.setText("0");
        else {
            display.setText(tekst + "0");
        }
    }

    public void btn1Click(ActionEvent actionEvent) {
        String tekst = display.getText();
        if(tekst.equals("0") || tekst.contains("=")) display.setText("1");
        else {
            display.setText(tekst + "1");
        }
    }

    public void btn2Click(ActionEvent actionEvent) {
        String tekst = display.getText();
        if(tekst.equals("0") || tekst.contains("=")) display.setText("2");
        else {
            display.setText(tekst + "2");
        }
    }

    public void btn3Click(ActionEvent actionEvent) {
        String tekst = display.getText();
        if(tekst.equals("0") || tekst.contains("=")) display.setText("3");
        else {
            display.setText(tekst + "3");
        }
    }

    public void btn4Click(ActionEvent actionEvent) {
        String tekst = display.getText();
        if(tekst.equals("0") || tekst.contains("=")) display.setText("4");
        else {
            display.setText(tekst + "4");
        }
    }

    public void btn5Click(ActionEvent actionEvent) {
        String tekst = display.getText();
        if(tekst.equals("0") || tekst.contains("=")) display.setText("5");
        else {
            display.setText(tekst + "5");
        }
    }

    public void btn6Click(ActionEvent actionEvent) {
        String tekst = display.getText();
        if(tekst.equals("0") || tekst.contains("=")) display.setText("6");
        else {
            display.setText(tekst + "6");
        }
    }

    public void btn7Click(ActionEvent actionEvent) {
        String tekst = display.getText();
        if(tekst.equals("0") || tekst.contains("=")) display.setText("7");
        else {
            display.setText(tekst + "7");
        }
    }

    public void btn8Click(ActionEvent actionEvent) {
        String tekst = display.getText();
        if(tekst.equals("0") || tekst.contains("=")) display.setText("8");
        else {
            display.setText(tekst + "8");
        }
    }

    public void btn9Click(ActionEvent actionEvent) {
        String tekst = display.getText();
        if(tekst.equals("0") || tekst.contains("=")) display.setText("9");
        else {
            display.setText(tekst + "9");
        }
    }

    public void dotBtnClicked(ActionEvent actionEvent) {
        String tekst = display.getText();
        display.setText(tekst + ".");
    }

    public void minusBtnClicked(ActionEvent actionEvent) {
        String tekst = display.getText();
        display.setText(tekst + " - ");
    }

    public void multipliesBtnClicked(ActionEvent actionEvent) {
        String tekst = display.getText();
        display.setText(tekst + " * ");
    }

    public void dividesBtnClicked(ActionEvent actionEvent) {
        String tekst = display.getText();
        display.setText(tekst + " / ");
    }

    public void plusBtnClicked(ActionEvent actionEvent) {
        String tekst = display.getText();
        display.setText(tekst + " + ");
    }

    public void equalsBtnClicked(ActionEvent actionEvent) {
        String rez = display.getText() + " = " + evaluate(display.getText());
        if(!rez.contains("ERROR")) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            Button button = new Button();
            HBox hBox = new HBox();
            HBox.setHgrow(hBox, Priority.ALWAYS);
            ImageView image = new ImageView("slike/kanta.png");
            image.setFitHeight(15);
            image.setFitWidth(15);
            image.setPreserveRatio(true);
            button.setGraphic(image);
            hBox.getChildren().add(new Label(String.format("%-50s%s", rez, "(" + dtf.format(now) + ") ")));
            hBox.getChildren().add(button);
            button.setOnAction(event -> historyView.getItems().remove(hBox));
            historyView.getItems().addAll(hBox);
        }
        display.setText(rez);
    }

    public void cBtnClicked(ActionEvent actionEvent) {
        display.setText("0");
    }
}