package tracks.singlePlayer.tools.Heuristics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import core.game.Observation;
import core.game.StateObservation;
import ontology.Types;
import tools.Vector2d;

public class AdvanceStateHeuristic {
    double initialNpcCounter = 0;

    public AdvanceStateHeuristic(StateObservation stateObs) {

    }

    public double evaluateState(StateObservation stateObs) {
        double distanceNPC = 0;// Distance to closest NPC
        double noNPC = 0;// number of NPCs
        double score = stateObs.getGameScore(); // find game score in this stateObs
        double distancePortal = 0;// Distance to closest portal
        double noResource = 0;// Distance to closest resource
        double distanceResource = 0;// Number of resources
        double winOrLoose = 0;// win or loose score
        double totalScore = 0;

        // win or loose
        if (stateObs.getGameWinner() == Types.WINNER.PLAYER_WINS) {
            return 9999999;
        } else if (stateObs.getGameWinner() == Types.WINNER.PLAYER_LOSES) {
            return -9999999;
        }
        Vector2d myPosition = stateObs.getAvatarPosition();

        // set value to distanceNPC and noNPC
        ArrayList<Observation>[] npcPositions = stateObs.getNPCPositions(myPosition);

        if (npcPositions != null) {
            for (int i = 0; i < npcPositions.length; i++) {
                noNPC += npcPositions[i].size();
            }
            if (npcPositions[0].size() != 0) {
                Vector2d closestNPC = npcPositions[0].get(0).position;
                distanceNPC = myPosition.dist(closestNPC);
            }
        }

        // set value to distancePortal helping find gates to go through wall
        ArrayList<Observation>[] portalPositions = stateObs.getPortalsPositions(myPosition);
        if (portalPositions != null) {
            if (portalPositions[0].size() != 0) {
                Vector2d closestPortal = portalPositions[0].get(0).position;
                distancePortal = myPosition.dist(closestPortal);
            }
        }

        // set value to distanceResource
        ArrayList<Observation>[] resourcesPositions = stateObs.getResourcesPositions(myPosition);
        if (resourcesPositions != null) {
            for (int i = 0; i < resourcesPositions.length; i++) {
                noResource += resourcesPositions[i].size();
            }
            if (resourcesPositions[0].size() != 0) {
                Vector2d closestResource = resourcesPositions[0].get(0).position;
                distanceResource = myPosition.dist(closestResource);
            }
        }

        

        
        if (distanceResource != 0) { //if game is to collect resources,we get rid of npcs
            int randnum = 9 + (int)(Math.random() * ((12 - 9) + 1)); //most of times, getting rid of npcs are more important than get resourses,but sometimes take the risk
            totalScore = +distanceNPC * randnum - noNPC * 300 + 100 * score - 200 * noResource - 5 * distancePortal
                    - 10 * distanceResource + winOrLoose;
        } else {
            totalScore = -distanceNPC / 200 - noNPC * 300 + 100 * score - 100 * noResource - 5 * distancePortal
                    - 10 * distanceResource + winOrLoose;

        }

        return totalScore;
    }
}
