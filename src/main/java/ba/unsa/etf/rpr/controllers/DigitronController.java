package ba.unsa.etf.rpr.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class DigitronController {
    public Label display;
    public double rez = 0;

    // prva verzija funkcije koja parsira izraz, zatim ga racuna / baca izuzetak ukoliko izraz nije valjan ili se javlja dijeljenje nulom
    // podrzane su osnovne aritmeticke operacije (+, -, *, /)
    // podrzan je prioritet izvrsavanja operacija
    // nije podrzano koristenje zagrada

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
        String tekst = display.getText();
        display.setText(tekst + " = " + evaluate(tekst));
    }

    public void cBtnClicked(ActionEvent actionEvent) {
        display.setText("0");
    }
}