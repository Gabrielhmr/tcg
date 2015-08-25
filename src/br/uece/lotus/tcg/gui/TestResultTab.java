package br.uece.lotus.tcg.gui;

import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.tcg.struct.PathSet;
import br.uece.lotus.tcg.struct.ResultInfo;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javax.swing.JOptionPane;

public class TestResultTab extends Tab{

    protected Boolean mGeneratingCSV;
    protected Boolean mLastGenerationSuccess;

    final String QUANTITY_STRING = "Total:";
    final String GENERATOR_STRING = "Generator:";
    final String SELECTOR_STRING = "Selector:";

    protected VBox mMainVbox;
    protected HBox mInfoHbox;

    protected TableView<TestResultTab.PathView> mTableView;
    protected TableColumn<TestResultTab.PathView,String> mStatesCollumn;
    protected TableColumn<TestResultTab.PathView,String> mGuardCollumn;
    protected TableColumn<TestResultTab.PathView,String> mLabelsCollumn;
    protected TableColumn<TestResultTab.PathView,Integer> mActionsCollumn;
    protected Label mQuantLabel;
    protected Label mGeneratorLabel;
    protected Label mSelectorLabel;
    protected Label mStoppedLabel;
    protected Component mComponent;
    protected Button mButtonExportCSV;
    protected Button mButtonExportXML;

    protected ObservableList<TestResultTab.PathView> mPathList = FXCollections.observableArrayList();

    TestResultTab(Component component, ResultInfo result){
        
        mComponent = component;
        PathSet pathSet = result.getPathSet();

        if (pathSet.getPathList() != null){            
            for (List<Transition> path : pathSet.getPathList()){
                PathView pv = new PathView(path);
                mPathList.add(pv);
            }
        }

        mGeneratorLabel = new Label(GENERATOR_STRING + " " + result.getGeneratorName());
        mSelectorLabel = new Label(SELECTOR_STRING + " " + result.getSelectorName());
        mStoppedLabel = new Label();

        mButtonExportCSV = new Button("Export to CSV");
        mButtonExportXML = new Button("Export selected case to XML");

        /*if (stopped){
         mStoppedLabel = new Label("Generation has been stopped!");   
         }*/
        
        init();
    }

    public class PathView{

        PathView(List<Transition> path){
            
            mPathLabels = "";
            mPathActions = 0;
            mPathStates = "";
            mPathGuards = "";
            
            boolean initialTransition = true;
            
            mTransitionList = path;
            mPathProbability = 1.0;

            for (Transition t : path){
                
                mPathLabels += t.getLabel() + ", ";
                mPathStates += t.getDestiny().getLabel() + ", ";

                if (initialTransition){
                    mPathStates = t.getSource().getLabel() + ", " + mPathStates;
                }

                if (t.getGuard() != null){
                    mPathGuards += t.getGuard() + ", ";
                }

                initialTransition = false;

                double p = 0.0;

                if (t.getProbability() != null){
                    p = t.getProbability();
                }

                mPathProbability *= p;

            }

            if (path.size() > 0){
                
                mPathLabels = mPathLabels.substring(0, mPathLabels.length() - 2);
                mPathStates = mPathStates.substring(0, mPathStates.length() - 2);
                
                if (!mPathGuards.isEmpty()){
                    mPathGuards = mPathGuards.substring(0, mPathGuards.length() - 2);
                }

                mPathActions = path.size();

                /*long factor = (long) Math.pow(10, 4);
                 mPathProbability = mPathProbability * factor;
                 long tmp = Math.round(mPathProbability);
                 mPathProbability = (double) tmp / factor;*/
            }
        }

        private String mPathLabels;
        private Integer mPathActions;
        private String mPathStates;
        private String mPathGuards;
        private Double mPathProbability;

        public List<Transition> mTransitionList;

        public String getPathLabels(){
            return mPathLabels;
        }

        public Integer getPathActions(){
            return mPathActions;
        }

        public Double getPathProbability(){
            return mPathProbability;
        }

        public String getPathStates(){
            return mPathStates;
        }

        public String getPathGuards(){
            return mPathGuards;
        }
    }

    protected void initTable(){
        
        mStatesCollumn = new TableColumn<>("States");
        mLabelsCollumn = new TableColumn<>("Test Cases");
        mActionsCollumn = new TableColumn<>("#Actions");
        mGuardCollumn = new TableColumn<>("Guard");

        mStatesCollumn.setCellValueFactory(new PropertyValueFactory<>("pathStates"));
        mStatesCollumn.setPrefWidth(200);

        mLabelsCollumn.setCellValueFactory(new PropertyValueFactory<>("pathLabels"));
        mLabelsCollumn.setPrefWidth(400);

        mActionsCollumn.setCellValueFactory(new PropertyValueFactory<>("pathActions"));
        mActionsCollumn.setPrefWidth(100);

        mGuardCollumn.setCellValueFactory(new PropertyValueFactory<>("pathGuards"));
        mGuardCollumn.setPrefWidth(200);

        mTableView = new TableView();
        mTableView.setPrefHeight(500);
        mActionsCollumn.setSortType(TableColumn.SortType.DESCENDING);
        mTableView.getColumns().addAll(mStatesCollumn, mLabelsCollumn, mActionsCollumn);
        mMainVbox.getChildren().add(mTableView);
    }

    protected void init(){
        
        mMainVbox = new VBox(0);
        mMainVbox.setPrefHeight(500);

        mInfoHbox = new HBox(30);

        initTable();

        Integer qtd = mPathList.size();

        mQuantLabel = new Label(QUANTITY_STRING + " " + qtd.toString());

        mInfoHbox.getChildren().addAll(mQuantLabel, mGeneratorLabel, mSelectorLabel, mStoppedLabel, mButtonExportCSV, mButtonExportXML);
        mMainVbox.getChildren().add(mInfoHbox);

        mTableView.setItems(mPathList);
        mTableView.setOnMouseClicked(onTableClick);
        VBox.setVgrow(mTableView, Priority.ALWAYS);

        mMainVbox.setMaxHeight(Double.MAX_VALUE);

        setContent(mMainVbox);
        setClosable(true);

        mButtonExportCSV.setOnAction(new EventHandler<ActionEvent>(){
            
            @Override
            public void handle(ActionEvent event){
                
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Test Cases");
                fileChooser.setInitialDirectory(
                        new File(System.getProperty("user.home"))
                );

                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Test Cases Files (*.csv)", "*.csv"),
                        new FileChooser.ExtensionFilter("Test Cases (*.txt)", "*.txt"),
                        new FileChooser.ExtensionFilter("All files", "*")
                );

                File file = fileChooser.showSaveDialog(null);

                if (file != null){
                    try{
                        GenerateCSV(file);

                        JOptionPane.showMessageDialog(null, "File Successfully Saved");
                    } catch (IOException e){
                        JOptionPane.showMessageDialog(null, "File was not Saved succesfully");
                    }
                }
            }

        });

        mButtonExportXML.setOnAction(new EventHandler<ActionEvent>(){
            
            @Override
            public void handle(ActionEvent event){
                
                if (mTableView.getSelectionModel().getSelectedItems().isEmpty()){
                    return;
                }

                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Test Cases");
                fileChooser.setInitialDirectory(
                        new File(System.getProperty("user.home"))
                );

                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Test Case XML (*.xml)", "*.xml")
                );

                File file = fileChooser.showSaveDialog(null);

                if (file != null){
                    try{
                        GenerateXML(file);
                        JOptionPane.showMessageDialog(null, "File Successfully Saved");
                    } catch (IOException e){
                        JOptionPane.showMessageDialog(null, "File was not Saved succesfully");
                    }
                }
            }

        });

        afterInit();
    }

    protected void GenerateCSV(File file) throws IOException{

        String content = "";
        content += "Test Cases\n";

        content += "\n";

        for (PathView pv : mTableView.getItems()){
            content += pv.getPathLabels().replaceAll(",", " ") + "\n";
        }

        try (PrintStream out = new PrintStream(file)){
            out.print(content);
        }
    }

    protected void GenerateXML(File file) throws IOException{

        PathView selectedPath = mTableView.getSelectionModel().getSelectedItem();

        try (PrintStream out = new PrintStream(file)){

            String content = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                    + "<project id='" + mTableView.getSelectionModel().getSelectedIndex() + "'>\n"
                    + "    <actions>" + selectedPath.getPathActions() + "</actions>\n"
                    + "    <states>" + selectedPath.mPathStates + "</states>\n";
            content += getProbabilityXMLString(selectedPath);

            Integer i = 0;
            
            for (String str : selectedPath.getPathLabels().split(",")){
                str = str.trim();
                content += "    <tcg aux='" + i.toString() + "'>" + str + "</tcg>\n";
                i++;
            }

            content += "</project>";
            out.print(content);
        }
    }

    protected String getProbabilityXMLString(PathView path){
        return "";
    }

    protected void afterInit(){
        mTableView.getSortOrder().add(mActionsCollumn);
    }

    protected void showChoices(State state){
        
        applyEnableStyle(state);
        
        for (Transition t : state.getOutgoingTransitions()){            
            applyChoiceStyle(t);
            applyChoiceStyle(t.getDestiny());
        }
    }

    protected void applyEnableStyle(State s){
        s.setColor(null);
        s.setTextColor("black");
        s.setTextSyle(State.TEXTSTYLE_NORMAL);
        s.setBorderColor("black");
        s.setBorderWidth(1);
    }

    protected void applyEnableStyle(Transition t){
        t.setColor("black");
        t.setTextSyle(Transition.TEXTSTYLE_NORMAL);
        t.setTextColor("black");
        t.setWidth(1);
    }

    protected void applyDisableAll(){
        
        State s = mComponent.getInitialState();
        ArrayList<State> stateList = new ArrayList<>();
        ArrayList<Transition> visitedTransitions = new ArrayList<>();
        int i = 0;
        stateList.add(s);

        while (i < stateList.size()){
            
            s = stateList.get(i);
            applyDisabledStyle(s);
            
            for (Transition t : s.getOutgoingTransitions()){
                
                if (!stateList.contains(t.getDestiny())){
                    stateList.add(t.getDestiny());
                }
                
                if (!visitedTransitions.contains(t)){
                    applyDisabledStyle(t);
                    visitedTransitions.add(t);
                }
            }
            ++i;
        }
    }

    protected void applyDisabledStyle(State s){
        s.setColor("#d0d0d0");
        s.setTextColor("#c0c0c0");
        s.setTextSyle(State.TEXTSTYLE_NORMAL);
        s.setBorderColor("gray");
        s.setBorderWidth(1);
    }

    protected void applyDisabledStyle(Transition t){
        t.setColor("#d0d0d0");
        t.setTextColor("#c0c0c0");
        t.setTextSyle(Transition.TEXTSTYLE_NORMAL);
        t.setWidth(1);
    }

    private void applyChoiceStyle(Transition t){
        t.setColor("blue");
        t.setTextSyle(Transition.TEXTSTYLE_BOLD);
        t.setTextColor("blue");
        t.setWidth(2);
    }

    private void applyChoiceStyle(State s){
        s.setColor(null);
        s.setBorderColor("blue");
        s.setTextSyle(Transition.TEXTSTYLE_BOLD);
        s.setTextColor("blue");
        s.setBorderWidth(2);
    }

    protected EventHandler<? super MouseEvent> onTableClick = new EventHandler<MouseEvent>(){

        @Override
        public void handle(MouseEvent e){
            
            PathView selectedPath = mTableView.getSelectionModel().getSelectedItem();

            if (selectedPath == null){
                return;
            }

            applyDisableAll();

            for (Transition t : selectedPath.mTransitionList){
                applyEnableStyle(t);
                applyEnableStyle(t.getDestiny());
                applyEnableStyle(t.getSource());
            }
        }
    };
}
