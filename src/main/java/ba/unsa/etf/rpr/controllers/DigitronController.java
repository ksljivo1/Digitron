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
    }

    public void cBtnClicked(ActionEvent actionEvent) {
        display.setText("0");
    }
}