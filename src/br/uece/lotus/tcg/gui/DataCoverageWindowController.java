/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.tcg.gui;

import br.uece.lotus.Component;
import br.uece.lotus.Transition;
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
public class DataCoverageWindowController implements Initializable {

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
    private ComboBox<String> mGenCombo;

    @FXML
    private HBox mHboxGen;

    protected ComponentViewImpl mViewer;

    protected LtsInfo mLtsInfo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DebugLog.printLog("DataCoverageWindowController -> initialize", ">> Start");

        mViewer = new ComponentViewImpl();
        mScrollPane.setContent(mViewer);

        Component component = (Component) resources.getObject("component");
        mViewer.setComponent(component);

        mLtsInfo = new LtsInfo(component.getInitialState());
        mButtonGenHelp = new Button();

        initGuardInfo();
    }

    protected void initGuardInfo() {

        Iterable<Transition> trasitionsList = mViewer.getComponent().getTransitions();

        for (Transition transition : trasitionsList) {
            if (transition.getGuard() != null) {
                mGenCombo.getItems().add(transition.getGuard());
            }

        }
        if (mGenCombo.getItems().size() > 0) {
            mGenCombo.setValue(mGenCombo.getItems().get(0));
        }
    }

    protected String getSelectedGuard() {
        return mGenCombo.getSelectionModel().getSelectedItem();
    }

    @FXML
    void onClickGeneratorCombobox(ActionEvent event) {
        String guard = getSelectedGuard();
        System.err.println("Guard Selected: "+ guard);
    }

    @FXML
    void onClickGenHelpButton(ActionEvent event) {

    }

    @FXML
    void onSubmit(ActionEvent event) {
         String value = mTextFieldGenParam.getText();
         System.err.println("Test Value: "+ value);
         
         //DataCOverageClass
         
    }
}
