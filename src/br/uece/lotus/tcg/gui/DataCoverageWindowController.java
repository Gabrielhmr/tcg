/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.tcg.gui;

import br.uece.lotus.Component;
import br.uece.lotus.Transition;
import br.uece.lotus.tcg.dataCoverage.ComparatorUtils;
import br.uece.lotus.tcg.dataCoverage.DataCoverage;
import br.uece.lotus.tcg.generation.generator.OneLoopPath;
import br.uece.lotus.tcg.struct.LtsInfo;
import br.uece.lotus.tcg.utils.DebugLog;
import br.uece.lotus.viewer.ComponentViewImpl;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

/**
 *
 * @author Gabriel
 */
public class DataCoverageWindowController implements Initializable {

    @FXML
    private ScrollPane mScrollPane;

    @FXML
    private Button mButtonRunTest;

    @FXML
    private Button mButtonSubmit;

    @FXML
    private Button mButtonClear;

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
    private ComboBox<String> mGenComboTransition;

    @FXML
    private HBox mHboxGen;

    protected ComponentViewImpl mViewer;

    protected LtsInfo mLtsInfo;

    private DataCoverage dataCoverage;

    private Iterable<Transition> trasitionsList;
    
    private List<String> guardList = new ArrayList<>();

    private final ObservableList<DataCoverageTest> dataTableSubmit = FXCollections.observableArrayList();

    private final ObservableList<DataCoverageResultTab> dataTableRunTest = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DebugLog.printLog("DataCoverageWindowController -> initialize", ">> Start");

        mViewer = new ComponentViewImpl();
        mScrollPane.setContent(mViewer);

        Component component = (Component) resources.getObject("component");
        mViewer.setComponent(component);
        
        //teste all one loop path novo
        
        OneLoopPath olp = new OneLoopPath();
        System.out.println("novo all one loop: ");
        System.err.println(olp.createOneLoopPath(component));

        mLtsInfo = new LtsInfo(component.getInitialState());
        mButtonRunTest.setDisable(true);

        mTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);
        dataCoverage = new DataCoverage();

        initComboBoxInfo();
    }

    protected void initComboBoxInfo() {

        trasitionsList = mViewer.getComponent().getTransitions();

        for (Transition transition : trasitionsList) {
            System.out.println(transition.getLabel());
            if (transition.getGuard() != null) {
                //mGenCombo.getItems().add(transition.getGuard());
                guardList.add(transition.getGuard());
                String guardName = new ComparatorUtils().getGuardName(transition.getGuard());
                if (!mGenCombo.getItems().contains(guardName)) {
                    mGenCombo.getItems().add(guardName);
                }
            }
            mGenComboTransition.getItems().add(transition.getLabel());
        }
        mGenComboTransition.setValue(mGenComboTransition.getItems().get(0));
        if (mGenCombo.getItems().size() > 0) {
            mGenCombo.setValue(mGenCombo.getItems().get(0));
        }
    }
    
    protected String getSelectedGuard() {
        //ComparatorUtils comparatorUtils = new ComparatorUtils();
        //return comparatorUtils.getGuardName(mGenCombo.getSelectionModel().getSelectedItem());
        return mGenCombo.getSelectionModel().getSelectedItem();
    }

    @FXML
    void onClickGeneratorCombobox(ActionEvent event) {
        String guard = getSelectedGuard();
        System.err.println("Guard Selected: " + guard);
    }
    
    protected String getSelectedTransition() {
        return mGenComboTransition.getSelectionModel().getSelectedItem();
    }

    @FXML
    void onClickGeneratorComboboxTransition(ActionEvent event) {
        String expectedValue = getSelectedTransition();
        System.err.println("Expected Output Transition Selected: " + expectedValue);
    }

    @FXML
    void onSubmit(ActionEvent event) {
        String value = mTextFieldGenParam.getText();
        System.err.println("Test Value: " + value);

        if (!value.isEmpty()) {
            System.err.println("preenchendo colunas de table OnSubmit");
            DataCoverageTest test = new DataCoverageTest(getSelectedGuard(), value);
            dataTableSubmit.add(test);

            mColumnGuard.setCellValueFactory(new PropertyValueFactory<>("Guard"));
            mColumnInput.setCellValueFactory(new PropertyValueFactory<>("Input"));

            mTableView.setItems(dataTableSubmit);
            mButtonRunTest.setDisable(false);       
        }

    }
    
    @FXML
    void onClear(ActionEvent event){
        mTableView.getItems().clear();
        mButtonRunTest.setDisable(true);
    }
    
    @FXML
    void onRunTest(ActionEvent event) {
        
        List<String> columnDataGuardList = new ArrayList<>();
        List<String> columnDataInputList = new ArrayList<>();
        formatGuardTable(columnDataGuardList,columnDataInputList);
        
        
        //clear Result Table
        dataCoverage.getResults().clear();
        dataTableRunTest.clear();
        
        List<List<Transition>> pathList = dataCoverage.getPathList(mLtsInfo,getSelectedTransition());
        
        dataCoverage.validateTest(pathList, columnDataGuardList, columnDataInputList);
        
        DataCoverageResultTab mTabResult = null;
        for (List<String> resultList : dataCoverage.getResults()) {
            mTabResult = new DataCoverageResultTab(resultList);
            dataTableRunTest.add(mTabResult);
        } 

        TableView mTableResult = mTabResult.createTable(dataTableRunTest);
        
        mTabResult.setContent(mTableResult);
        mTabResult.setText("Results");
        mTabResult.setClosable(true);

        mTabPane.getTabs().add(mTabResult);
        
        mButtonRunTest.setDisable(true);
    }

    private void formatGuardTable(List<String> columnDataGuardList,List<String> columnDataInputList) {
        for (Object item : mTableView.getItems()) {
            String guardNames = (String) mColumnGuard.getCellObservableValue(item).getValue();
            String values = (String) mColumnInput.getCellObservableValue(item).getValue();
            guardNames = new ComparatorUtils().getGuardByName(guardNames,values,guardList);
            columnDataGuardList.add(guardNames);
            columnDataInputList.add(values);
        }
    }

    public static class DataCoverageTest {

        private final SimpleStringProperty guard;
        private final SimpleStringProperty input;

        private DataCoverageTest(String guard, String input) {
            this.guard = new SimpleStringProperty(guard);
            this.input = new SimpleStringProperty(input);
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
    }
}
