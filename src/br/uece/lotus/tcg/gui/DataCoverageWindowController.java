/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.tcg.gui;

import br.uece.lotus.Component;
import br.uece.lotus.tcg.struct.LtsInfo;
import br.uece.lotus.tcg.utils.DebugLog;
import br.uece.lotus.viewer.ComponentViewImpl;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
/**
 *
 * @author Gabriel
 */
public class DataCoverageWindowController implements Initializable{
    
     @FXML
    private ScrollPane mScrollPane;

    @FXML
    private Button mButtonSubmit;

    @FXML
    private Button mButtonGenHelp;

    @FXML
    private Label mLabelGenParameter;

    @FXML
    private VBox mMainVBox;

    @FXML
    private TextField mTextFieldGenParam;

    @FXML
    private TabPane mTabPane;

    @FXML
    private ComboBox<?> mGenCombo;

    @FXML
    private HBox mHboxGen;
    
    protected ComponentViewImpl mViewer;
    
    protected LtsInfo mLtsInfo;

    @FXML
    void onClickGeneratorCombobox(ActionEvent event) {

    }

    @FXML
    void onClickGenHelpButton(ActionEvent event) {

    }

    @FXML
    void onSubmit(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DebugLog.printLog("DataCoverageWindowController -> initialize", ">> Start");

        mViewer = new ComponentViewImpl();
        mScrollPane.setContent(mViewer);
        
        Component component = (Component) resources.getObject("component");
        mViewer.setComponent(component);

        mLtsInfo = new LtsInfo(component.getInitialState());

        
    }
    
}
