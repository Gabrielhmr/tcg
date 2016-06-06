/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.tcg.gui;

import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author Gabriel
 */
public class DataCoverageResultTab extends Tab{
    
    private TableColumn<DataCoverageResultTab,String> mCoveragedPathCollumn;
    private TableColumn<DataCoverageResultTab,String> mFinalTransitionCollumn;
    private TableColumn<DataCoverageResultTab,String> mResultMsgCollumn;
    private TableView mTableView;
    private final String coveragedPath;
    private final String finalTransition;
    private final String resultMsg;
    
    
    
    DataCoverageResultTab(List<String> resultTab) {
        coveragedPath = resultTab.get(0);
        finalTransition = resultTab.get(1);
        resultMsg = resultTab.get(2);
    }
    
    public String getCoveragedPath(){
           return coveragedPath; 
    }
    
    public String getFinalTransition(){
           return finalTransition; 
    }
    
    public String getResultMsg(){
           return resultMsg; 
    }


    public TableView createTable(ObservableList<DataCoverageResultTab> dataTableRunTest) {
        
        mCoveragedPathCollumn = new TableColumn<>("Coveraged Path");
        mFinalTransitionCollumn = new TableColumn<>("Final Transition");
        mResultMsgCollumn = new TableColumn<>("Result");
        
        mCoveragedPathCollumn.setCellValueFactory(new PropertyValueFactory<>("CoveragedPath"));
        mCoveragedPathCollumn.setPrefWidth(400);
       
        mFinalTransitionCollumn.setCellValueFactory(new PropertyValueFactory<>("FinalTransition"));
        mFinalTransitionCollumn.setPrefWidth(200);
        
        mResultMsgCollumn.setCellValueFactory(new PropertyValueFactory<>("ResultMsg"));
        mResultMsgCollumn.setPrefWidth(200);
        
        
        mTableView = new TableView();
        mTableView.setPrefHeight(500);
        //mActionsCollumn.setSortType(TableColumn.SortType.DESCENDING);
        //mWeightCollumn.setSortType(TableColumn.SortType.DESCENDING);
        mTableView.getColumns().addAll(mCoveragedPathCollumn, mFinalTransitionCollumn,mResultMsgCollumn);
        //mMainVbox.getChildren().add(mTableView);
        mTableView.setItems(dataTableRunTest);
        
        return mTableView;
    }
    
    
    
}
