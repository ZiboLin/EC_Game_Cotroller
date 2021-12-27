package tracks.singlePlayer.self_defined.evolution;

import core.game.StateObservation;

public class Individual {
    public int id;
    public StateObservation stateObs;

    public Individual(StateObservation i_stateObs, int i_id) {
        stateObs = i_stateObs;
        id = i_id;
    }
}
