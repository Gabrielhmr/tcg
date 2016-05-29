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
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
    private TableView mTableView;
    
    @FXML
    private TableColumn mColumnGuard;
    
    @FXML
    private TableColumn mColumnInput;
    
    @FXML
    private TableColumn mColumnExpectedValue;

    @FXML
    private ComboBox<String> mGenCombo;

    @FXML
    private HBox mHboxGen;

    protected ComponentViewImpl mViewer;

    protected LtsInfo mLtsInfo;
    
    private final ObservableList<DataCoverageTest> data = FXCollections.observableArrayList();
    
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
    void onClickGenHelpButton(ActionEvent event) throws Exception {

        URL location = getClass().getResource("AddGuard.fxml");

        FXMLLoader loader = new FXMLLoader();

        loader.setClassLoader(getClass().getClassLoader());
        loader.setLocation(location);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
       // loader.setResources(rBundle);

        try{
            Parent root = (Parent) loader.load(location.openStream());

            Scene scene = new Scene(root);
            Stage window = new Stage(StageStyle.UTILITY);
            window.initModality(Modality.APPLICATION_MODAL);

            window.setResizable(false);
            window.setMinWidth(300);
            window.setMinHeight(250);

            window.setScene(scene);
            window.showAndWait();

        }catch (IOException e){
        }
        
    }

    @FXML
    void onSubmit(ActionEvent event) {
         String value = mTextFieldGenParam.getText();
         System.err.println("Test Value: "+ value);
                  
         System.err.println("preenchendo colunas de table");
         DataCoverageTest test = new DataCoverageTest(getSelectedGuard(), value, getSelectedGuard()); 
         data.add(test);
        
         mColumnGuard.setCellValueFactory(new PropertyValueFactory<>("Guard"));
         mColumnInput.setCellValueFactory(new PropertyValueFactory<>("Input"));
         mColumnExpectedValue.setCellValueFactory(new PropertyValueFactory<>("ExpectedValue"));
         
         mTableView.setItems(data);
    }
    
    public static class DataCoverageTest {
 
        private final SimpleStringProperty guard;
        private final SimpleStringProperty input;
        private final SimpleStringProperty expectedValue;
 
        private DataCoverageTest(String guard, String input, String expectedValue) {
            this.guard = new SimpleStringProperty(guard);
            this.input = new SimpleStringProperty(input);
            this.expectedValue = new SimpleStringProperty(expectedValue);
        }
 
        public String getGuard() {
            return guard.get();
        }
 
        public void setGuard(String guard) {
            this.guard.set(guard);
        }
 
        public String getInput() {
            return input.get();
        }
 
        public void setInput(String input) {
            this.input.set(input);
        }
 
        public String getExpectedValue() {
            return expectedValue.get();
        }
 
        public void setExpectedValue(String expectedValue) {
            this.expectedValue.set(expectedValue);
        }
    }
}
