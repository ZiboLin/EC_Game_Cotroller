package tracks.singlePlayer.GA;

import java.text.DecimalFormat;
import java.util.*;

import tracks.singlePlayer.GA.individual;
import tracks.singlePlayer.GA.population;

public class optimizer {

    public static population pop = new population();
    public static individual fittest;
    public static individual secondFittest;
    public static int generation = 5;
    public static int generationCount = 0;
    public static individual overallbest;

    public static double bestfitness = 0;
    public static double gamma = 0;
    public static int simudepth = 0;
    public static int popsize = 0;
    public static double RECPROB = 0;
    public static double MUT = 0;



    public static void main(String argv[]){
        double[] result = new double[5];
        optimizer op = new optimizer();
        ////Initialize population
        op.pop.initialize();
        System.out.println("finish initialization");

        //Calculate fitness of each individual
        op.pop.calculate();
        individual best = op.pop.getFitone();
        overallbest = best;
        System.out.println("Generation: " + op.generationCount + " Fittest: " + overallbest.fitness);
        System.out.println("Here is the values: ");

        System.out.println("GAMMA:" + overallbest.genes[0]);
        System.out.println("SIMULATION_DEPTH:" + (int) Math.ceil(overallbest.genes[1]));      //same with the read number of New_Agent
        System.out.println("POPULATION_SIZE:" + (int) Math.ceil(overallbest.genes[2]));
        System.out.println("RECPROB:" + overallbest.genes[3]);
        System.out.println("MUT:" + overallbest.genes[4]);


        //output the solution for each generation
        /*for(int i=0;i<5;i++){
            System.out.println(best.genes[i]);
        }*/

        //generation
        for(int i=0; i<generation; i++){


            op.selection();
            op.crossover();
            /*Random r = new Random();
            if (r.nextInt()%7 < 5) {           //for control the possibility of mutation
                demo.mutation();
            }*/
            op.mutation();
            op.selection();


            op.update();


            op.pop.calculate();

            best = op.pop.getFitone();

            if(best.fitness >= bestfitness){
                overallbest = best;
                bestfitness = overallbest.fitness;
                gamma = overallbest.genes[0];
                simudepth = (int) Math.ceil(overallbest.genes[1]);
                popsize = (int) Math.ceil(overallbest.genes[2]);
                RECPROB = overallbest.genes[3];
                MUT = overallbest.genes[4];

            }
            op.generationCount++;
            System.out.println("Generation: " + op.generationCount + " Finished, Start next generation");
            //System.out.println("The overall fittest is " + overallbest.fitness);

            /*System.out.println("Here is the parameters values for best parameter setting:");

            System.out.println("GAMMA:" + overallbest.genes[0]);
            System.out.println("SIMULATION_DEPTH:" + Math.ceil(overallbest.genes[1]));      //same with the read number of New_Agent
            System.out.println("POPULATION_SIZE:" + Math.ceil(overallbest.genes[2]));
            System.out.println("RECPROB:" + overallbest.genes[3]);
            System.out.println("MUT:" + overallbest.genes[4]);*/



        }

        System.out.println("Here is the parameters values for best parameter setting:");
        System.out.println("Highest fitness:" + bestfitness);
        System.out.println("GAMMA:" + gamma);
        //System.out.println("SIMULATION_DEPTH:" + Math.ceil(overallbest.genes[1]));      //same with the read number of New_Agent
        //System.out.println("POPULATION_SIZE:" + Math.ceil(overallbest.genes[2]));
        System.out.println("SIMULATION_DEPTH:" + simudepth);      //same with the read number of New_Agent
        System.out.println("POPULATION_SIZE:" + popsize);
        System.out.println("RECPROB:" + RECPROB);
        System.out.println("MUT:" + MUT);

        //test individual
        /*for(int i=0; i<5; i++){
            result[i] = indi.genes[i];
        }
        for(int i=0; i<5; i++){
            System.out.println(indi.genes[i] + " ");
        }
        indi.calcFitness();*/


    }
    public static void selection() {

        fittest = pop.getFitone();
        secondFittest = pop.getFittwo();
    }

    public static void mutation(){

        Random r = new Random();

        int mutationPoint = r.nextInt(pop.individuals[0].Length);
        DecimalFormat df = new DecimalFormat("0.0");
        for(int i=0; i<pop.popSize; i++){                               //mutation based on normal distribution step1
            pop.individuals[i].genes[mutationPoint] = Math.abs(NormalDistribution( pop.individuals[i].genes[mutationPoint],  pop.individuals[i].genes[mutationPoint]/50)) ;
            pop.individuals[i].genes[mutationPoint] = Double.parseDouble(df.format(pop.individuals[i].genes[mutationPoint]));
            if(pop.individuals[i].genes[4] == 0){
                pop.individuals[i].genes[4] = 0.1;
            }
        }
        mutationPoint = r.nextInt(pop.individuals[0].Length);          //mutation based on normal distribution step2
        for(int i=0; i<pop.popSize; i++){
            pop.individuals[i].genes[mutationPoint] = Math.abs(NormalDistribution( pop.individuals[i].genes[mutationPoint],  pop.individuals[i].genes[mutationPoint]/50)) ;
            pop.individuals[i].genes[mutationPoint] = Double.parseDouble(df.format(pop.individuals[i].genes[mutationPoint]));
            if(pop.individuals[i].genes[4] == 0){
                pop.individuals[i].genes[4] = 0.1;
            }
        }



    }

    public static void crossover() {
        Random r = new Random();

        int crossOverPoint = r.nextInt(pop.individuals[0].Length);

        for (int i = 0; i < crossOverPoint; i++) {
            double t = fittest.genes[i];
            fittest.genes[i] = secondFittest.genes[i];
            secondFittest.genes[i] = t;

        }

    }

    public static void update() {               //update for offspring

        //Update fitness values of offspring


        fittest = pop.getFitone();
        secondFittest = pop.getFittwo();
        //Get index
        int leastFittestIndex = pop.getWorst();

        //Replace
        pop.individuals[leastFittestIndex] = fittest;
    }
    public static double NormalDistribution(double u, double v){
        java.util.Random random = new java.util.Random();
        return Math.sqrt(v)*random.nextGaussian()+u;
    }

}

