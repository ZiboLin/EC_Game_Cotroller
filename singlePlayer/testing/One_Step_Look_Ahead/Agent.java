package tracks.singlePlayer.testing.One_Step_Look_Ahead;
// package sampleonesteplookahead;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

import core.game.Observation;
import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types;
import tools.ElapsedCpuTimer;
import tracks.singlePlayer.tools.Heuristics.SimpleStateHeuristic;

/**
 * Created with IntelliJ IDEA.
 * User: ssamot
 * Date: 14/11/13
 * Time: 21:45
 * This is a Java port from Tom Schaul's VGDL - https://github.com/schaul/py-vgdl
 */
public class Agent extends AbstractPlayer {

    public Agent(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {}

    public Types.ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
        Types.ACTIONS bestAction = null; //当前最好的Action
        double maxQ = Double.NEGATIVE_INFINITY; //Variable to store the max reward (Q) found. 用来保存最高的得分
        SimpleStateHeuristic heuristic =  new SimpleStateHeuristic(stateObs);  
        for (Types.ACTIONS action : stateObs.getAvailableActions()) { //For all available actions. 循环所有可能的actions
            
            StateObservation stCopy = stateObs.copy();  //Copy the original state (to apply action from it) stCopy这里是当前action的state
            //System.out.println("stCopy = " + stCopy);
            stCopy.advance(action);                     //Apply the action. Object 'stCopy' holds the next state. 执行当前action
            double Q = heuristic.evaluateState(stCopy); //Get the reward for this state.
            // System.out.println("Q: " + Q );


            //Keep the action with the highest reward.
            if (Q > maxQ) { 
                maxQ = Q;
                bestAction = action;
            }

        }

        //Return the best action found.
        //System.out.println("====================");
        return bestAction;
    }
}