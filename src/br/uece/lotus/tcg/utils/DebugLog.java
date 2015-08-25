package br.uece.lotus.tcg.utils;

import br.uece.lotus.Transition;
import java.util.ArrayList;

public class DebugLog {

    // Set this value to false, in case you don't want to see any debug messages printed.
    static private boolean mPrintMessage = false;

    static public void printLog(String header, String msg){        
        if (mPrintMessage){
            System.out.println(header + " - " + msg);
        }        
    }

    static public void printLog(String msg) {        
        if (mPrintMessage){
            System.out.println(msg);
        }        
    }

    static public void printTransitionArray(ArrayList<Transition> arr){
        
        if (mPrintMessage){
            
            String labels = "";
            
            for (Transition t : arr){
                labels += t.getLabel() + "[ " + t.getGuard() + "]" + ", ";
            }
            
            System.out.println(labels);
        }
    }

    static public void printTransitionArrayListByStates(ArrayList<ArrayList<Transition>> ret){
        
        if (mPrintMessage) {
            
            String labels = "";
            
            for (ArrayList<Transition> arr : ret){
                
                labels += "{ " + arr.get(0).getSource().getLabel();

                for (Transition t : arr){
                    labels += ", " + t.getDestiny().getLabel();
                }

                labels += "}; ";
            }
            System.out.println(labels);
        }
    }

    public static void printTransitionArrayList(ArrayList<ArrayList<Transition>> ret){
        
        if (mPrintMessage){
            
            String labels = "";

            for (ArrayList<Transition> arr : ret){
                
                for (Transition t : arr){
                    labels += t.getLabel() + "[ " + t.getGuard() + "]" + ", ";
                }
                
            }
            System.out.println(labels);
        }
    }
}
