package br.uece.lotus.tcg.gui;

import br.uece.lotus.State;
import br.uece.lotus.Transition;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AddGuardController implements Initializable {

    @FXML
    private TextField txtGuard;

    @FXML
    private Label lblValueTested;

    @FXML
    private Button btnCancel;

    @FXML
    private Label lblGuard;

    @FXML
    private Button btnAdd;

    @FXML
    private TextField txtValueTested;

    @FXML
    private Label lblValue;

    @FXML
    private TextField txtValue;

    @FXML
    private ComboBox<String> cbTransitions;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initCombBox(resources);

    }

    private void initCombBox(ResourceBundle resources) {
        
        Iterable<State> trasitionsList = (Iterable<State>) resources.getObject("transitions");
        for (State transition : trasitionsList) {
            if (transition.getLabel() != null) {
                cbTransitions.getItems().add(transition.getLabel());
            }

        }
        if (cbTransitions.getItems().size() > 0) {
            cbTransitions.setValue(cbTransitions.getItems().get(0));
        }
    }

}
