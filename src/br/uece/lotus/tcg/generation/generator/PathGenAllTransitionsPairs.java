/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.tcg.generation.generator;

/**
 *
 * @author Gabriel
 */
public class PathGenAllTransitionsPairs  extends PathGenAllTransitions{
     
    protected Integer mParameter;
    @Override
    public String getName(){
        return "All Trasitions Pairs";
    }
    
    @Override
    public String getDescription(){
        return "Returns Every pair of adjacent transitions in the FSM or statechart model must be traversed at least once.";
    }
    
    @Override
    public boolean acceptParameter(){
        return true;
    }
    
    @Override
    public String getParameterText(){
        return "State:";
    }
    
    @Override
    public void setParameter(String value){
        if (value == null || value.isEmpty()){
            mParameter = 10;    
        } else{
            mParameter = Integer.parseInt(value);
        }
         System.out.println("state: " + mParameter);
    }
}

