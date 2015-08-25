package br.uece.lotus.tcg.gui;

import br.uece.lotus.Component;
import br.uece.lotus.tcg.struct.ResultInfo;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TestStatisticalResultTab extends TestResultTab{

    protected TableColumn<TestResultTab.PathView, Double> mProbabilityCollumn;

    public TestStatisticalResultTab(Component component, ResultInfo result){
        super(component, result);
    }

    @Override
    protected void initTable(){
        
        mStatesCollumn = new TableColumn<>("States");
        mLabelsCollumn = new TableColumn<>("Test Cases");
        mActionsCollumn = new TableColumn<>("#Actions");
        mProbabilityCollumn = new TableColumn<>("Probability");

        mStatesCollumn.setCellValueFactory(new PropertyValueFactory<>("pathStates"));
        mStatesCollumn.setPrefWidth(200);

        mLabelsCollumn.setCellValueFactory(new PropertyValueFactory<>("pathLabels"));
        mLabelsCollumn.setPrefWidth(400);

        mActionsCollumn.setCellValueFactory(new PropertyValueFactory<>("pathActions"));
        mActionsCollumn.setPrefWidth(150);

        mProbabilityCollumn.setCellValueFactory(new PropertyValueFactory<>("pathProbability"));
        mProbabilityCollumn.setPrefWidth(300);

        mTableView = new TableView();
        mTableView.setPrefHeight(500);
        mProbabilityCollumn.setSortType(TableColumn.SortType.DESCENDING);
        mTableView.getColumns().addAll(mStatesCollumn, mLabelsCollumn, mActionsCollumn, mProbabilityCollumn);

        mMainVbox.getChildren().add(mTableView);
    }

    @Override
    protected void afterInit(){
        mTableView.getSortOrder().add(mProbabilityCollumn);
    }
    
    @Override
    protected String getProbabilityXMLString(PathView path){
       String str = "<probability>" + path.getPathProbability() + "</probability>\n";       
       return str;
    }
}
