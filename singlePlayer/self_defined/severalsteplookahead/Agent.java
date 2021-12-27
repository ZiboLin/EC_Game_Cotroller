package tracks.singlePlayer.self_defined.severalsteplookahead;

import tracks.singlePlayer.tools.Heuristics.AdvanceStateHeuristic;
import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types;
import tools.ElapsedCpuTimer;
import tools.Utils;

import java.util.Random;

/**
 * Created with IntelliJ IDEA. User: ssamot Date: 14/11/13 Time: 21:45 This is a
 * Java port from Tom Schaul's VGDL - https://github.com/schaul/py-vgdl
 */
public class Agent extends AbstractPlayer {

    public double epsilon = 1e-6;
    public Random m_rnd;
    private ElapsedCpuTimer timer;


    public Agent(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {

        m_rnd = new Random();

    }

    /**
     *
     * Looks Serveral steps ahead.
     *
     * @param stateObs     Observation of the current state.
     * @param elapsedTimer Timer when the action returned is due.
     * @return An action for the current state
     */
    public Types.ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {

        Types.ACTIONS bestAction = null;
        this.timer = elapsedTimer;

        double maxQ = Double.NEGATIVE_INFINITY;
        AdvanceStateHeuristic heuristic = new AdvanceStateHeuristic(stateObs);


        
        //for game 10
        // outerloop:
        // for (Types.ACTIONS action1 : stateObs.getAvailableActions()) {
        //     StateObservation stCopy1 = stateObs.copy();
        //     stCopy1.advance(action1);
        //     for (int i = 0; i < 15; i++) {
        //         double remainingtime = timer.remainingTimeMillis();
        //         if(remainingtime<3.0) break outerloop;
        //         stCopy1.advance(action1);
        //         double Q = heuristic.evaluateState(stCopy1);
        //         Q = Utils.noise(Q, this.epsilon, this.m_rnd.nextDouble());

        //         // System.out.println("Action:" + action1 + " score:" + Q);
        //         if (Q > maxQ) {
        //             maxQ = Q;
        //             bestAction = action1;
        //         }
        //     }
        // }
        
        //for other games
        outerloop:
        for (Types.ACTIONS action : stateObs.getAvailableActions()) {
            StateObservation stCopy = stateObs.copy();
            //first action for several times
            for (int i = 0; i < 1; i++) {
                double remainingtime = timer.remainingTimeMillis();
                if(remainingtime<3.0) break outerloop;
                stCopy.advance(action);
                double Q = heuristic.evaluateState(stCopy);
                Q = Utils.noise(Q, this.epsilon, this.m_rnd.nextDouble());
                for (Types.ACTIONS action1 : stCopy.getAvailableActions()) {
                    StateObservation stCopy1 = stCopy.copy();
                    //second action for several times
                    for (int j = 0; j < 1; j++) {
                        stCopy1.advance(action1);
                        double Q1 = heuristic.evaluateState(stCopy1);
                        // Q1 = Utils.noise(Q1, this.epsilon, this.m_rnd.nextDouble());

                        // System.out.println("Action:" + action + " score:" + Q);
                        if (Q1 > maxQ) {
                        maxQ = Q1;
                        bestAction = action;
                        }
                    }
                
                }
                if (Q > maxQ) {
                maxQ = Q;
                bestAction = action;
                }
            }
        }

        



        // // StateObservation stCopy = stateObs.copy();
        // // stCopy.advance(action);

        // // StateObservation stCopy1 = stCopy.copy();
        // // stCopy1.advance(action);

        // // StateObservation stCopy2 = stCopy1.copy();
        // // stCopy2.advance(action);

        // }

        // System.out.println("======== " + maxQ + " " + bestAction + "============");
        return bestAction;

    }

}
