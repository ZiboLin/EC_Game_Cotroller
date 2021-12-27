package tracks.singlePlayer.GA;
import tracks.singlePlayer.GA.individual;

public class population {
    int popSize = 3;
    individual[] individuals = new individual[popSize];
    double fittest = 0;



    //Initialize population
    public void initialize() {
        for (int i = 0; i < individuals.length; i++) {
            System.out.println("initialized successful");
            individuals[i] = new individual();
        }
        /*for (int i = 0; i < individuals.length; i++) {
            for(int k=0; k<5; k++){
                System.out.println(individuals[i].genes[k]);
            }
        }*/

    }

    public void calculate() {

        for (int i = 0; i < popSize; i++) {
            individuals[i].calcFit();
        }
        getFitone();
    }

    public individual getFitone() {
        int max = 0;
        for (int i = 1; i < popSize; i++) {
            if (individuals[max].fitness <= individuals[i].fitness) {
                max = i;
            }
        }
        fittest = individuals[max].fitness;
        return individuals[max];
    }

    public individual getFittwo() {
        int m1 = 0;
        int m2 = 0;
        for (int i = 0; i < popSize; i++) {
            if (individuals[i].fitness > individuals[m1].fitness) {
                m2 = m1;
                m1 = i;
            } else if (individuals[i].fitness > individuals[m2].fitness) {
                m2 = i;
            }
        }
        return individuals[m2];
    }

    public int getWorst() {
        int m = 0;
        for (int i = 0; i < popSize; i++) {
            if (individuals[m].fitness >= individuals[i].fitness) {
                m = i;
            }
        }
        return m;
    }


}
