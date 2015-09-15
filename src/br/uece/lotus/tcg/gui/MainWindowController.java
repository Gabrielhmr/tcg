package br.uece.lotus.tcg.gui;

import br.uece.lotus.Component;
import br.uece.lotus.tcg.TestBundleBuilder;
import br.uece.lotus.tcg.generation.GenerationManager;
import br.uece.lotus.tcg.generation.GeneratorSet;
import br.uece.lotus.tcg.generation.StatisticalGeneratorSet;
import br.uece.lotus.tcg.prioritization.PrioritizerSet;
import br.uece.lotus.tcg.prioritization.PriorizationManager;
import br.uece.lotus.tcg.purpose.TestPurposeParser;
import br.uece.lotus.tcg.purpose.TestPurposeSelector;
import br.uece.lotus.tcg.purpose.TestPurposeSuite;
import br.uece.lotus.tcg.selection.SelectionManager;
import br.uece.lotus.tcg.selection.SelectorSet;
import br.uece.lotus.tcg.selection.StatisticalSelectorSet;
import br.uece.lotus.tcg.stopcondition.StopConditionManager;
import br.uece.lotus.tcg.struct.LtsInfo;
import br.uece.lotus.tcg.struct.PathSet;
import br.uece.lotus.tcg.struct.ResultInfo;
import br.uece.lotus.tcg.struct.TestBundle;
import br.uece.lotus.tcg.utils.DebugLog;
import br.uece.lotus.viewer.ComponentViewImpl;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javax.swing.JOptionPane;

public class MainWindowController implements Initializable{

    @FXML
    private ScrollPane mScrollPane;

    @FXML
    private Label mLabelTestPurpose;
    
    @FXML
    private Button mButtonSelector;
    
    @FXML
    private VBox mVboxStopConditions;

    @FXML
    private VBox mMainVBox;

    @FXML
    private TextField mTextFieldGenParam;

    @FXML
    private ComboBox<String> mComboSelector;
    
    @FXML
    private ComboBox<String> mComboPrioritizer;
        
    @FXML
    private Label mLabelSelectorParameter;

    @FXML
    private TabPane mTabPane;

    @FXML
    private ComboBox<String> mGenCombo;
    
    @FXML
    private HBox mHboxGen;
    
    @FXML
    private HBox mHboxPrioritizer;
    
    @FXML
    private HBox mHboxTestPurpose;

    @FXML
    private TextField mTextFieldTestPurpose;

    @FXML
    private Button mButtonGenHelp;
    
    @FXML
    private Button mButtonPrioritizer;
    
    @FXML
    private Label mLabelGenParameter;
        
    @FXML
    private Label mLabelPrioritizerParameter;
    
    @FXML
    private HBox mHboxSelector;

    @FXML
    private VBox mTabMainVBox;

    @FXML
    private TextField mTextFieldSelectorParam;

    @FXML
    private TextField mTextFieldPrioritizerParam;

    @FXML
    private Label mLabelSelector;

    @FXML
    private Label mLabelPrioritizer;
    
    @FXML
    private Button mButtonSubmit;

    protected ComponentViewImpl mViewer;

    protected Thread mGenerationThread;

    protected boolean mStatisticalSuite;

    protected boolean mStoppedGeneration = false;

    protected LtsInfo mLtsInfo;

    private GenerationManager mGenManager;
    
    private PriorizationManager mPrioManager;
    
    private SelectionManager mSelManager;

    private StopConditionManager mStopConditionManager;

    AtomicBoolean mGeneratingPaths = new AtomicBoolean(false);

    private TestPurposeSuite mPurposeSuite;

    private TestBundle mTestBundle;

    protected List<String> mSelectedStopConditions;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        
        DebugLog.printLog("MainWindowController -> initialize", ">> Start");

        mViewer = new ComponentViewImpl();
        mScrollPane.setContent(mViewer);
        
        Component component = (Component) resources.getObject("component");
        mViewer.setComponent(component);

        mLtsInfo = new LtsInfo(component.getInitialState());

        mStatisticalSuite = (boolean) resources.getObject("statisticalmode");

        mTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);

        initManagers();
        initGenInfo();
        initSelectorInfo();
        initPrioritizerInfo();

        initStopConditions();

        mTextFieldTestPurpose.setText("*&ACCEPT");

        DebugLog.printLog("MainWindowController -> initialize", ">> Finish");
    }

    protected void onClickStopConditionCheckBox(MouseEvent event){
        
        if (event.getSource() instanceof CheckBox){
            
            CheckBox check = (CheckBox) event.getSource();

            if (check.isSelected()){
                mSelectedStopConditions.add(check.getText());
            }else{
                mSelectedStopConditions.remove(check.getText());
            }
        }
    }

    protected void initStopConditions(){
        
        mSelectedStopConditions = new ArrayList<>();

        for (String stopCondition : mStopConditionManager.getStopConditionList()){
            
            HBox box = new HBox();
            box.setFillHeight(false);
            box.setSpacing(20);
            box.setAlignment(Pos.CENTER_LEFT);

            CheckBox check = new CheckBox(stopCondition);
            check.setId("sc_check_" + stopCondition);
            
            check.setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent t){
                    onClickStopConditionCheckBox(t);
                }
            });

            Label label = new Label(mStopConditionManager.getParameterText(stopCondition));

            TextField textField = new TextField();
            textField.setId("sc_textField_" + stopCondition);
            textField.setText(mStopConditionManager.getDefaultParameterValue(stopCondition));

            box.getChildren().addAll(check, label, textField);
            mVboxStopConditions.getChildren().add(box);
        }
    }

    protected void initManagers(){
        
        if (mStatisticalSuite){
            mGenManager = new GenerationManager(new StatisticalGeneratorSet());
            mSelManager = new SelectionManager(new StatisticalSelectorSet());
        }else{
            mGenManager = new GenerationManager(new GeneratorSet());
            mSelManager = new SelectionManager(new SelectorSet());
            mPrioManager = new PriorizationManager(new PrioritizerSet());
        }

        mStopConditionManager = new StopConditionManager();
    }

    protected void initGenInfo(){
        
        for (String p : mGenManager.getGeneratorList()){
            mGenCombo.getItems().add(p);
        }

        if (mGenCombo.getItems().size() > 0){
            mGenCombo.setValue(mGenCombo.getItems().get(0));
        }

        adjustGenConfiguration();
    }
    
    protected void initSelectorInfo(){
        
        for (String p : mSelManager.getSelectorList()){
            mComboSelector.getItems().add(p);
        }

        if (mComboSelector.getItems().size() > 0){
            mComboSelector.setValue(mComboSelector.getItems().get(0));
        }

        adjustSelectorConfiguration();
    }
    
    protected void initPrioritizerInfo(){
        
        for (String p : mPrioManager.getPrioritizerList()){
            mComboPrioritizer.getItems().add(p);
        }

        if (mComboPrioritizer.getItems().size() > 0){
            mComboPrioritizer.setValue(mComboPrioritizer.getItems().get(0));
        }

        adjustPrioritizerConfiguration();
    }
    
    protected void adjustGenConfiguration(){
        
        String generator = getSelectedGenerator();

        if (generator != null && !"".equals(generator)){

            if (mGenManager.acceptParamater(generator)){
                
                if (!mHboxGen.getChildren().contains(mTextFieldGenParam)){
                    mHboxGen.getChildren().add(mLabelGenParameter);
                    mHboxGen.getChildren().add(mTextFieldGenParam);
                }

                String labelGenParam = mGenManager.getParameterText(generator);
                mTextFieldGenParam.setText("");
                mLabelGenParameter.setText(labelGenParam);
                
            }else{
                if (mHboxGen.getChildren().contains(mLabelGenParameter)){
                    mHboxGen.getChildren().remove(mLabelGenParameter);
                    mHboxGen.getChildren().remove(mTextFieldGenParam);
                }
            }

            if (!mGenManager.acceptPurpose(generator)){
                if (mHboxTestPurpose.getChildren().contains(mTextFieldTestPurpose)){
                    mHboxTestPurpose.getChildren().remove(mLabelTestPurpose);
                    mHboxTestPurpose.getChildren().remove(mTextFieldTestPurpose);
                }
            }else{
                if (!mHboxTestPurpose.getChildren().contains(mTextFieldTestPurpose)){
                    mHboxTestPurpose.getChildren().add(mLabelTestPurpose);
                    mHboxTestPurpose.getChildren().add(mTextFieldTestPurpose);
                }
            }
            
            // Selector
            if (!mGenManager.acceptSelector(generator)){
                
                if (mHboxSelector.getChildren().contains(mComboSelector)){
                    mHboxSelector.getChildren().remove(mLabelSelector);
                    mHboxSelector.getChildren().remove(mButtonSelector);
                    mHboxSelector.getChildren().remove(mComboSelector);
                    mHboxSelector.getChildren().remove(mLabelSelectorParameter);
                    mHboxSelector.getChildren().remove(mTextFieldSelectorParam);
                }
            }else{
                if (!mHboxSelector.getChildren().contains(mComboSelector)){
                    mHboxSelector.getChildren().add(mLabelSelector);
                    mHboxSelector.getChildren().add(mComboSelector);
                    mHboxSelector.getChildren().add(mButtonSelector);
                    adjustSelectorConfiguration();
                }
            }
            
            // Prioritizer
            if (!mGenManager.acceptPrioritizer(generator)){
                
                if (mHboxPrioritizer.getChildren().contains(mComboPrioritizer)){
                    mHboxPrioritizer.getChildren().remove(mLabelPrioritizer);
                    mHboxPrioritizer.getChildren().remove(mButtonPrioritizer);
                    mHboxPrioritizer.getChildren().remove(mComboPrioritizer);
                    mHboxPrioritizer.getChildren().remove(mLabelPrioritizerParameter);
                    mHboxPrioritizer.getChildren().remove(mTextFieldPrioritizerParam);
                }
            }else{
                if (!mHboxPrioritizer.getChildren().contains(mComboPrioritizer)){
                    mHboxPrioritizer.getChildren().add(mLabelPrioritizer);
                    mHboxPrioritizer.getChildren().add(mComboPrioritizer);
                    mHboxPrioritizer.getChildren().add(mButtonPrioritizer);
                    adjustPrioritizerConfiguration();
                }
            }
            
            mMainVBox.autosize();
        }
    }
    
    protected void adjustSelectorConfiguration(){
        
        String selector = getSelectedSelector();

        if (selector != null && !"".equals(selector)){
            
            if (mSelManager.acceptParameter(selector)){
                
                if (!mHboxSelector.getChildren().contains(mTextFieldSelectorParam)){
                    mHboxSelector.getChildren().add(mLabelSelectorParameter);
                    mHboxSelector.getChildren().add(mTextFieldSelectorParam);
                }

                mTextFieldSelectorParam.setText(mSelManager.getDefaultParameterValue(selector));
                mLabelSelectorParameter.setText(mSelManager.getParameterText(selector));
            }else{
                if (mHboxSelector.getChildren().contains(mTextFieldSelectorParam)){
                    mHboxSelector.getChildren().remove(mLabelSelectorParameter);
                    mHboxSelector.getChildren().remove(mTextFieldSelectorParam);
                }
            }
        }
    }
    
    protected void adjustPrioritizerConfiguration(){
        
        String prioritizer = getSelectedPrioritizer();
        
        if (prioritizer != null && !"".equals(prioritizer)){
            
            if (mPrioManager.acceptParameter(prioritizer)){
                
                if (!mHboxPrioritizer.getChildren().contains(mTextFieldPrioritizerParam)){
                    mHboxPrioritizer.getChildren().add(mLabelPrioritizerParameter);
                    mHboxPrioritizer.getChildren().add(mTextFieldPrioritizerParam);
                }

                mTextFieldPrioritizerParam.setText(mPrioManager.getDefaultParameterValue(prioritizer));
                mLabelPrioritizerParameter.setText(mPrioManager.getParameterText(prioritizer));
            }else{
                if (mHboxPrioritizer.getChildren().contains(mTextFieldPrioritizerParam)){
                    mHboxPrioritizer.getChildren().remove(mLabelPrioritizerParameter);
                    mHboxPrioritizer.getChildren().remove(mTextFieldPrioritizerParam);
                }
            }
        }
    }

    protected void createResultTab(Component component, ResultInfo result){
        
        TestResultTab newTab;

        if (!mStatisticalSuite){
            newTab = new TestResultTab(component, result);
        }else{
            newTab = new TestStatisticalResultTab(component, result);
        }

        newTab.setText("Results");
        newTab.setClosable(true);

        mTabPane.getTabs().add(newTab);
    }

    @FXML
    protected void onClickGeneratorCombobox(ActionEvent event){
        adjustGenConfiguration();
    }
    
    @FXML
    protected void onClickPrioritizerCombobox(ActionEvent event){
        adjustPrioritizerConfiguration();
    }
    
    @FXML
    protected void onClickSelectorCombobox(ActionEvent event){
        adjustSelectorConfiguration();
    }
    
    @FXML
    protected void onSubmit(ActionEvent event){
        
        if (mGeneratingPaths.get() == true){
            
            DebugLog.printLog("MainWindowController - ABORT CLICKED!!!");

            mButtonSubmit.setDisable(true);
            mGenManager.abortGeneration();
            mStoppedGeneration = true;

            return;
        }

        DebugLog.printLog("MainWindowController - SUBMIT CLICKED!!!");

        mStoppedGeneration = false;

        mPurposeSuite = null;
        mTestBundle = null;

        mGenerationThread = new Thread(new Runnable(){
            
            @Override
            public void run(){
                
                String selectedGen = getSelectedGenerator();
                String selectedPri = getSelectedPrioritizer();
                String selectedSel = getSelectedSelector();

                mGeneratingPaths.set(true);

                // StopConditionSet scSet = mStopConditionManager.mountSet(mountStopConditionMap());
                if (mGenManager.acceptParamater(selectedGen)){
                    mGenManager.setParameter(selectedGen, mTextFieldGenParam.getText());
                }

                if (mGenManager.acceptPurpose(selectedGen) && !mTextFieldTestPurpose.getText().isEmpty()){

                    try{
                        mPurposeSuite = TestPurposeParser.parse(mLtsInfo, mTextFieldTestPurpose.getText());
                    }catch (ParseException ex){
                        
                        mGeneratingPaths.set(false);

                        Platform.runLater(new Runnable(){                            
                            @Override
                            public void run(){
                                JOptionPane.showMessageDialog(null, ex.getMessage());
                                mButtonSubmit.setText("Submit");
                                mButtonSubmit.setDisable(false);
                            }
                        });
                        return;
                    }
                }

                mTestBundle = TestBundleBuilder.build(mLtsInfo,
                        mPurposeSuite,
                        mStopConditionManager.mountSet(mountStopConditionMap()));

                PathSet pathSet = mGenManager.generate(selectedGen, mLtsInfo, mTestBundle);

                if (mPurposeSuite != null){
                    pathSet = TestPurposeSelector.select(pathSet, mPurposeSuite);
                }
                
                if (mGenManager.acceptSelector(selectedGen)){
                    
                    String selParam = "";

                    if (mSelManager.acceptParameter(selectedSel)){
                        selParam = mTextFieldSelectorParam.getText();
                    }

                    pathSet = mSelManager.select(selectedSel, mLtsInfo, pathSet, selParam);
                }
                
                if(mGenManager.acceptPrioritizer(selectedGen)){
                    
                    String priParam = "";
                    
                    if (mPrioManager.acceptParameter(selectedPri)){
                        priParam = mTextFieldPrioritizerParam.getText();
                    }
                    
                     pathSet = mPrioManager.select(selectedPri, mLtsInfo, pathSet, priParam);
                }

                ResultInfo result = new ResultInfo(pathSet, selectedGen, getSelectedSelector(), getSelectedPrioritizer(), "");

                mGeneratingPaths.set(false);

                Platform.runLater(new Runnable(){
                    @Override
                    public void run(){
                        createResultTab(mViewer.getComponent(), result);
                        mButtonSubmit.setText("Submit");
                        mButtonSubmit.setDisable(false);
                    }
                });
            }
        });

        mGenerationThread.start();
        mButtonSubmit.setText("Stop");
    }

    protected String getSelectedGenerator(){
        return mGenCombo.getSelectionModel().getSelectedItem();
    }
    
    protected String getSelectedSelector(){
        return mComboSelector.getSelectionModel().getSelectedItem();
    }

    protected String getSelectedPrioritizer(){
        return mComboPrioritizer.getSelectionModel().getSelectedItem();
    }

    @FXML
    protected void onClickGenHelpButton(ActionEvent event){
        
        String generator = mGenCombo.getSelectionModel().getSelectedItem();

        if (generator != null){
            mButtonGenHelp.setTooltip(new Tooltip(mGenManager.getGeneratorDescription(generator)));
        }

        URL location = getClass().getResource("DescriptionPopup.fxml");

        FXMLLoader loader = new FXMLLoader();

        ResourceBundle rBundle = new DescriptionBundle(generator, mGenManager.getGeneratorDescription(generator));

        loader.setClassLoader(getClass().getClassLoader());
        loader.setLocation(location);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setResources(rBundle);

        try{
            Parent root = (Parent) loader.load(location.openStream());

            Popup popup = new Popup();

            Scene scene = new Scene(root);

            popup.setAutoHide(true);
            popup.setHideOnEscape(true);
            popup.getContent().addAll(scene.getRoot());
            popup.setAutoFix(true);
            popup.show(mMainVBox.getScene().getWindow());

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    protected void onClickSelectorButtonHelp(ActionEvent event){
        
        String selector = getSelectedSelector();

        if (selector != null){
            mButtonGenHelp.setTooltip(new Tooltip(mSelManager.getDescription(selector)));
        }

        URL location = getClass().getResource("DescriptionPopup.fxml");

        FXMLLoader loader = new FXMLLoader();

        ResourceBundle rBundle = new DescriptionBundle(selector, mSelManager.getDescription(selector));

        loader.setClassLoader(getClass().getClassLoader());
        loader.setLocation(location);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setResources(rBundle);

        try{
            Parent root = (Parent) loader.load(location.openStream());

            Popup popup = new Popup();

            Scene scene = new Scene(root);

            popup.setAutoHide(true);
            popup.setHideOnEscape(true);
            popup.getContent().addAll(scene.getRoot());
            popup.setAutoFix(true);
            popup.show(mMainVBox.getScene().getWindow());

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    protected void onClickPrioritizerHelpButton(ActionEvent event){
        
        String prioritizer = getSelectedPrioritizer();

        if (prioritizer != null){
            mButtonPrioritizer.setTooltip(new Tooltip(mPrioManager.getDescription(prioritizer)));
        }

        URL location = getClass().getResource("DescriptionPopup.fxml");

        FXMLLoader loader = new FXMLLoader();

        ResourceBundle rBundle = new DescriptionBundle(prioritizer, mPrioManager.getDescription(prioritizer));

        loader.setClassLoader(getClass().getClassLoader());
        loader.setLocation(location);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setResources(rBundle);

        try{
            Parent root = (Parent) loader.load(location.openStream());

            Popup popup = new Popup();

            Scene scene = new Scene(root);

            popup.setAutoHide(true);
            popup.setHideOnEscape(true);
            popup.getContent().addAll(scene.getRoot());
            popup.setAutoFix(true);
            popup.show(mMainVBox.getScene().getWindow());

        }catch (IOException e){
            e.printStackTrace();
        }
    }
            
    protected Map<String, String> mountStopConditionMap(){
        
        HashMap<String, String> scMap = new HashMap<>();

        for (Node node : mVboxStopConditions.getChildren()){
            
            if (node instanceof HBox){
                
                DebugLog.printLog("HBox found!");

                HBox hbox = (HBox) node;

                if (hbox.getChildren().get(0) instanceof CheckBox){
                    
                    DebugLog.printLog("CheckBox Found!");
                    CheckBox check = (CheckBox) hbox.getChildren().get(0);

                    String str = check.getId();

                    if (check.isSelected() && str.startsWith("sc_check_")){
                        
                        String stopConditionName = str.replaceFirst("sc_check_", "");

                        DebugLog.printLog("Stop Condition: " + stopConditionName);

                        TextField tf = (TextField) hbox.getChildren().get(2);
                        String value = tf.getText();

                        if (value.isEmpty()){
                            value = mStopConditionManager.getDefaultParameterValue(stopConditionName);
                        }

                        scMap.put(stopConditionName, value);

                        DebugLog.printLog("Stop Condition Added - value: " + value);
                    }
                }
            }
        }
        return scMap;
    }

    private class DescriptionBundle extends ResourceBundle{

        public DescriptionBundle(String title, String desc){
            mDescription = desc;
            mTitle = title;
        }

        private String mDescription;
        private String mTitle;

        @Override
        protected Object handleGetObject(String key){
            
            if (key == "description"){
                return mDescription;
            }

            if (key == "title"){
                return mTitle;
            }

            return null;
        }

        @Override
        public Enumeration<String> getKeys(){
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
}

