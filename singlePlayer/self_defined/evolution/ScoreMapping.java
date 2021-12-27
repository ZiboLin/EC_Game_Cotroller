package tracks.singlePlayer.self_defined.evolution;
import tracks.singlePlayer.self_defined.evolution.ScoreMapping;
public class ScoreMapping implements Comparable<ScoreMapping>{
    public int individualId;
    public double individualScore;
    
    public ScoreMapping(int x, double y) {
        individualId = x;
        individualScore = y;
    }

    public int compareTo(ScoreMapping another) {
        return (this.individualScore > another.individualScore) ? 1 : -1;
    }

    
}
