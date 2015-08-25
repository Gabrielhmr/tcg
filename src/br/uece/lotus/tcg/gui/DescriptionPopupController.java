package br.uece.lotus.tcg.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Popup;

public class DescriptionPopupController implements Initializable{

    @FXML
    private Label mLabelTitle;

    @FXML
    private TextArea mTextAreaDesc;

    @FXML
    private Button mButtonClose;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        mLabelTitle.setText(rb.getString("title"));
        mTextAreaDesc.setText(rb.getString("description"));
    }

    @FXML
    void onButtonCloseClick(ActionEvent event){
        Popup popup = (Popup) mButtonClose.getScene().getWindow();
        popup.hide();
    }
}
