package br.uece.lotus.tcg.gui;

import br.uece.lotus.Component;
import br.uece.lotus.Transition;
import br.uece.lotus.tcg.gui.DataCoverageWindowController.DataCoverageTest;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import jdk.nashorn.internal.ir.ForNode;

public class AddGuardController implements Initializable {

    @FXML
    private TextField txtGuard;

    @FXML
    private Button btnCancel;

    @FXML
    private Label lblGuard;

    @FXML
    private Button btnAdd;

    @FXML
    private Label lblValue;

    @FXML
    private TextField txtValue;

    @FXML
    private ComboBox<String> cbTransitions;

    ResourceBundle resources;

    Iterable<Transition> trasitionsList;
    
    Component component;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.resources = resources;
        initCombBox();

    }

    private void initCombBox() {

        component = (Component) resources.getObject("component");
        trasitionsList =  component.getTransitions();
        for (Transition transition : trasitionsList) {
            if (transition.getLabel() != null) {
                cbTransitions.getItems().add(transition.getLabel());
                // System.out.println("tranicao: "+transition.getLabel());
            }

        }
        if (cbTransitions.getItems().size() > 0) {
            cbTransitions.setValue(cbTransitions.getItems().get(0));
        }
    }

    protected String getSelectedTransition() {
        return cbTransitions.getSelectionModel().getSelectedItem();
    }

    @FXML
    void onClickGenCalcelButton(ActionEvent event) throws Exception {

        Stage stage = (Stage) btnCancel.getScene().getWindow();

        stage.close();
    }

    @FXML
    void onClickGenAddButton(ActionEvent event) throws Exception {

        System.err.println("Add Transition: " + getSelectedTransition());

        String guard = txtGuard.getText();
        System.err.println("Add Guuard: " + guard);

        if (txtGuard.getText() != null) {
            for (Transition transition : trasitionsList) {
                if (transition.getLabel().equals(getSelectedTransition())) {
                    transition.setGuard(guard);
                    Stage stage = (Stage) btnCancel.getScene().getWindow();
                    stage.close();
                    
                    component.getTransitionByLabel(transition.getLabel()).setGuard(guard);
                    
                }
            }
        }

    }

}
