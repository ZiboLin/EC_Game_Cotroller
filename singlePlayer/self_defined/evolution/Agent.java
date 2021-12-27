package tracks.singlePlayer.self_defined.evolution;

import core.game.StateObservation;
import core.player.AbstractPlayer;
import tools.ElapsedCpuTimer;
import ontology.Types;
import java.util.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import tracks.singlePlayer.tools.Heuristics.StateHeuristic;
import tracks.singlePlayer.tools.Heuristics.WinScoreHeuristic;
import tracks.singlePlayer.tools.Heuristics.AdvanceStateHeuristic;


public class Agent extends AbstractPlayer {

    final int lamSize = 5;
    final int populationSize = lamSize * 2;//2 times of lanSize to make tournament

    ArrayList<Individual> population;//population is a list of StateObs
    private final int N_ACTIONS;
    Random seed = new Random();
    private ElapsedCpuTimer timer;



    // constructor, where the controller is first created to play the entire game
    public Agent(StateObservation stateObs, ElapsedCpuTimer elapsedTime) {
        // do all initializations here
        N_ACTIONS = stateObs.getAvailableActions().size();

    }

    //randomly choose 2 states and compare, replace the bad one with the good one
    //to keep the diversity
    private void tournament_replace(ArrayList<ScoreMapping> state_scores) throws TimeoutException {
        int a, b, W, L;
        a = 0;
        Collections.shuffle(state_scores);//shuffle the scores to make next step randomized
        // tournament lam times to find lam winnners to be the new population
        for (int i = 0; i < lamSize; i++) {
            a = state_scores.get(i).individualId;
            b = state_scores.get(populationSize -i-1).individualId;

            if (state_scores.get(a).individualScore < state_scores.get(b).individualScore) {
                W = b;
                L = a;
            } else {
                L = b;
                W = a;
            }
            int indexOfLooseIndivindexual = state_scores.get(L).individualId;
            int indexOfWinIndivindexual = state_scores.get(W).individualId;
            StateObservation stateOfWinIndivindexual = population.get(indexOfWinIndivindexual).stateObs.copy();
            int indexOfBestIndivindexual = population.get(indexOfWinIndivindexual).id;
            Individual newIndivindexual = new Individual(stateOfWinIndivindexual, indexOfBestIndivindexual);
            population.set(indexOfLooseIndivindexual, newIndivindexual);

        }

    }

    //re-gernerate scoremap and get best individual's index
    private int generate_score(ArrayList<ScoreMapping> individualScoresMap,StateObservation stateObs) throws TimeoutException {

        for (int i = 0; i < populationSize; i++) {
            //use an advanced evaluate funtion to evaluate individuals
            AdvanceStateHeuristic heuristic = new AdvanceStateHeuristic(stateObs);//this is self_defined at 'tracks.singlePlayer.tools.Heuristics.AdvanceStateHeuristic;'
            individualScoresMap.add(new ScoreMapping(i, heuristic.evaluateState(population.get(i).stateObs)));
        }

        //copy a new score map to sort
        ArrayList<ScoreMapping> sortedMap = new ArrayList<ScoreMapping>();
        for (ScoreMapping p : individualScoresMap) {
            sortedMap.add(new ScoreMapping(p.individualId, p.individualScore));
        }

        // get the best individual's id
        Collections.sort(sortedMap);
        int sortedsize = sortedMap.size();
        int maxScoreIndividualId = population.get(sortedMap.get(sortedsize - 1).individualId).id;
        return maxScoreIndividualId;
    }

    //take an action and store the firstMove of each individual in the population
    private ArrayList<Types.ACTIONS> takeNextAction(StateObservation stateObs) throws TimeoutException{
        ArrayList<Types.ACTIONS> firstMove = new ArrayList<Types.ACTIONS>();

        for (int i = 0; i < populationSize; i++) {
            int numMoves = population.get(i).stateObs.getAvailableActions().size();
            // this will happen if one of the indivindexuals has died and no move remains
            if (numMoves > 0) {
                int action_index = seed.nextInt(N_ACTIONS);//a random action index
                //population make a random action
                firstMove.add(stateObs.getAvailableActions().get(action_index));
                population.get(i).stateObs.advance(stateObs.getAvailableActions().get(action_index));
            }
        }

        return firstMove;
    }

    private Types.ACTIONS evolute(StateObservation stateObs, StateHeuristic heuristic, int iterations) {

        //initialize the population and take one step and record this step
        int maxScoreIndividualId = 0; // the index of the indivindexual in the population who is best
        population = new ArrayList<Individual>();//population is a list of StateObs
        for (int i = 0; i < populationSize; i++) {
            population.add(new Individual(stateObs.copy(), i));
        }

        //take an action and store the firstMove of each individual in the population
        ArrayList<Types.ACTIONS> firstMove = new ArrayList<Types.ACTIONS>();
        try {
            firstMove = takeNextAction(stateObs);
        } catch (TimeoutException e) {
        }

        double remainingtime = timer.remainingTimeMillis();

        ArrayList<ScoreMapping> individualScoresMap = new ArrayList<ScoreMapping>();//like a sql
        
        
        //while remaining enough time, keep evoluting
        outerloop: while (remainingtime > 3.0) {
            individualScoresMap.clear();//when a new generation, ScoreMap should start from empty
            try {
                maxScoreIndividualId = generate_score(individualScoresMap,stateObs);//re-gernerate scoremap and get best individual's index
            } catch (TimeoutException e) {// if time is not enough, quit and break the loop
                break outerloop;
            }

            remainingtime = timer.remainingTimeMillis();
            if(remainingtime<3.0) break outerloop;
            try {
                tournament_replace(individualScoresMap);//replace the bad state with the better state
            } catch (TimeoutException e) {// if time is not enough, quit and break the loop
                break outerloop;
            }

            remainingtime = timer.remainingTimeMillis();
            if(remainingtime<3.0) break outerloop;
            try {
                takeNextAction(stateObs);
            } catch (TimeoutException e) {// if time is not enough, quit and break the loop
                break outerloop;
            }
            
            remainingtime = timer.remainingTimeMillis();
        }


        Types.ACTIONS maxAction = firstMove.get(maxScoreIndividualId);

        return maxAction;

    }

 
    public Types.ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {

        this.timer = elapsedTimer;
        Types.ACTIONS lastGoodAction = evolute(stateObs,  new WinScoreHeuristic(stateObs), 100);

        return lastGoodAction;
    }

}