package tracks.singlePlayer.GA;
import tools.Utils;
import tracks.ArcadeMachine_test;
import java.text.DecimalFormat;
import java.io.File;

import java.io.*;


import java.util.Random;
import tools.Utils;
import tracks.ArcadeMachine;
public class individual {
    double fitness = 0;    //适应度 成绩决定
    double[] genes = new double[5];    //like [0.9,7,5,0.1,1.0 / SIMULATION_DEPTH]     GAMMA,SIMULATION_DEPTH,POPULATION_SIZE,RECPRONB,MUT
    int Length = 5;
    public individual() {                   //initialize the individual
        DecimalFormat df = new DecimalFormat("0.0");
        genes[0] = Math.abs(NormalDistribution( 0.9,  0.02));
        genes[1] = Math.abs(NormalDistribution(7, 0.1)) ;
        genes[2] = Math.abs(NormalDistribution(5, 0.1)) ;
        genes[3] = Math.abs(NormalDistribution( 0.1, 0.002)) ;
        genes[4] = Math.abs(NormalDistribution(1.0, 0.02) / genes[1]);

        if(genes[4] == 0){
            genes[4] = 0.1;
        }

        genes[0] = Double.parseDouble(df.format(genes[0]));
        genes[1] = Double.parseDouble(df.format(genes[1]));
        genes[2] = Double.parseDouble(df.format(genes[2]));
        genes[3] = Double.parseDouble(df.format(genes[3]));
        genes[4] = Double.parseDouble(df.format(genes[4]));


        fitness = 0;

    }

    public void calcFit() {
        //test individual genes
        System.out.println("Test following parameters setting");
        System.out.println("GAMMA: " + genes[0]);
        System.out.println("SIMULATION_DEPTH: " + (int) Math.ceil(genes[1]));      //same with the read number of New_Agent
        System.out.println("POPULATION_SIZE: " + (int) Math.ceil(genes[2]));
        System.out.println("RECPROB:" + genes[3]);
        System.out.println("MUT:" + genes[4]);
        //Load available games
        String spGamesCollection =  "examples/all_games_sp.csv";
        String[][] games = Utils.readGames(spGamesCollection);
        String sampleTestingController3 = "tracks.singlePlayer.testing.Genetic.New_Agent";
        int gameIdx = 0;  //0 = Aliens,   11 = Boulderdash       13= Butterflies       18= Chase
        int levelIdx = 1; // level names from 0 to 4 (game_lvlN.txt).

        String game = games[gameIdx][0];
        String gameName = games[gameIdx][1];
        String level1 = game.replace(gameName, gameName + "_lvl" + levelIdx);

        //output to txt

        try {
            //File writeName = new File("output.txt");
            File writeName = new File("C:\\Users\\Jen\\GVGAI-master\\src\\tracks\\singlePlayer\\GA\\output.txt");           //please change the path to test
            writeName.createNewFile();
            try (FileWriter writer = new FileWriter(writeName);
                 BufferedWriter out = new BufferedWriter(writer)
            ) {
                out.write(String.valueOf(genes[0]+"\r\n"));
                out.write(String.valueOf(genes[1]+"\r\n"));
                out.write(String.valueOf(genes[2]+"\r\n"));
                out.write(String.valueOf(genes[3]+"\r\n"));
                out.write(String.valueOf(genes[4]+"\r\n"));
                out.flush(); // push the content in cache to the file
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("run the ArcadeMachine");

        //运行文件
        fitness = ArcadeMachine_test.runGames(game, new String[]{level1}, 5, sampleTestingController3, null);



    }

    //正态分布individual
    public static double NormalDistribution(double u, double v){
        java.util.Random random = new java.util.Random();
        return Math.sqrt(v)*random.nextGaussian()+u;
    }






}
