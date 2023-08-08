package ba.unsa.etf.rpr;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/**
 * Main class for working with JavaFX framework
 */

public class DigitronApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/logIn.fxml"));
        stage.setTitle("Log in");
        stage.setScene(new Scene(root, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE));
        stage.setResizable(false);
        stage.show();

        /*List<KeyCode> keyCodes = new ArrayList<>();
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
        });*/

        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }

}
