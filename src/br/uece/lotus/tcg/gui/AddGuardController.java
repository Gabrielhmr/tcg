package br.uece.lotus.tcg.gui;


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

    ResourceBundle resources;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        this.resources = resources;
        initCombBox();

    }

    private void initCombBox() {

        Iterable<Transition> trasitionsList = (Iterable<Transition>) resources.getObject("transitions");
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

    @FXML
    void onClickGenCalcelButton(ActionEvent event) throws Exception {
         
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        
        stage.close();
    }
    
    @FXML
    void onClickGenAddButton(ActionEvent event) throws Exception {
        
        if(txtGuard.getText()!=null){
            
            //DataCoverageTest dct = new DataCoverageTest();
            //dct.getGuard();
            
            
            
            
        }
         
        
    }
    

}
