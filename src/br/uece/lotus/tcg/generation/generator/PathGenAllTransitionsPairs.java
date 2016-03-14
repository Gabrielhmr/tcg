/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.tcg.generation.generator;

import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.tcg.struct.PathStruct;
import java.time.Clock;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gabriel
 */
public class PathGenAllTransitionsPairs extends PathGenAllTransitions {

    protected Integer mParameter;

    @Override
    public String getName() {
        return "All Trasitions Pairs";
    }

    @Override
    public String getDescription() {
        return "Returns Every pair of adjacent transitions in the FSM or statechart model must be traversed at least once.";
    }

    @Override
    public boolean acceptParameter() {
        return true;
    }

    @Override
    public String getParameterText() {
        return "State:";
    }

    @Override
    public void setParameter(String value) {
        if (value == null || value.isEmpty()) {
            mParameter = 10;
        } else {
            mParameter = Integer.parseInt(value);
        }
        System.out.println("state: " + mParameter);
    }

    @Override
    protected List<PathStruct> expand(PathStruct path) {

        List<PathStruct> list = new ArrayList<>();
        String stateLabel = mParameter.toString();

        for (Transition t : path.getCurrentState().getOutgoingTransitions()) {
            System.out.println("-----to no FOR como : " + path.getCurrentState().getLabel());
            State lastState = t.getDestiny();
            //State lastState = path.getCurrentState();
            if (lastState.getLabel().equals(stateLabel)) {
                System.out.println("entrei e sou estado: " + lastState.getLabel());
                
                List<Transition> mTransicoesEntrada = lastState.getIncomingTransitionsList();              
                System.out.println("Trasitions de entrada");
                for (Transition mTransicoesEntrada1 : mTransicoesEntrada) {
                    System.out.println(mTransicoesEntrada1.getLabel());
                }

                System.out.println("Trasitions de saida");
                List<Transition> mTransicoesSaida = lastState.getOutgoingTransitionsList();
                for (Transition mTransicoesSaida1 : mTransicoesSaida) {
                    System.out.println(mTransicoesSaida1.getLabel());
                }

                for (Transition tEntrada : mTransicoesEntrada) {
                    for (Transition tSaida : mTransicoesSaida) {
                        System.out.println("cria new path");
                        PathStruct newPath = new PathStruct();
                        newPath.addTransition(tEntrada);
                        newPath.addTransition(tSaida);
                        System.out.println("add a lista trans: " + tEntrada.getLabel() + tSaida.getLabel());
                        list.add(newPath);
                        System.out.println("ADICIONADO");
                    }
                }
            }
            break;
        }

        for (PathStruct ps : list) {
            System.out.println("path: " + ps.getInitialTransition().getLabel() + ps.getLastTransition().getLabel());
        }
        return list;
    }
}
