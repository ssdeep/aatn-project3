/*
 * This source code is designed and written by Saideep Sambaraju
 * It is to be used for non commercial purposes only
 * DO NOT copy this code without permission from the author
 * Contact Info : 408-203-0492
 */

package aatnproject3;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author esamsai
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // call the regular experiment for the first part of the question
        regularExperiment();
        // call the flip experiment for the second part of the question
        flipExperiment();
           
    }
    
     private static void regularExperiment() throws IOException {
     
         /*
        All combinations involved in the network can be represented by the numbers from 0 to 1023 as follows
        0 - 0000000000
        1 - 0000000001 - one link is up
        2 - 0000000010 - a different link is up
        3 - 0000000011 - two different links are up
        ...
        1023 - 1111111111 - all links are up!
        This gives us an easier way to represent all configurations of a network
        */
        /*
         The 10 edges of the network itself can be encoded as follows
        0--1 : 0
        0--2 : 1
        0--3 : 2
        0--4 : 3
        1--2 : 4
        1--3 : 5
        1--4 : 6
        2--3 : 7
        2--4 : 8
        3--4 : 9
        */
        int V = 5 ; // number of nodes
        int filler = 1; // to fill up the edges with
        
        // data for plotting to be stored in ...
        ArrayList<Double> pvals = new ArrayList<Double>(); // for link probability values
        ArrayList<Double> rvals = new ArrayList<Double>(); // for reliability values
        
        // we repeat this experiment with the p values from [0,1]
        
      for(double p = 0.00 ; p < 1.01 ; p = p + 0.01){
          
          double Rnetwork = 0.00; // reliability of a network with p as given
      
            for(int i = 0 ; i <= 1023 ; i++){

                    // to store the edges that are down
                    ArrayList<Integer> downEdges = new ArrayList<>();
                    // a function that fills up the above array list with indices
                    fillUp(downEdges, i, V);
                    // create a complte graph with the new constructor and filler value
                    Graph network = new Graph(V, filler);
                    // set the down edge value accordingly
                    for(Integer d : downEdges){
                        // set the downed edge in the adjacency matrix
                        Edge e = getRowColumn(d, V);
                        network.addEdgeUndirected(e);
                    }
                    //printMatrix(network.adjMatrix);
                    // if the network is up add to the reliability value
                    if(network.isConnected()){
                        int goodEdges = V*(V-1)/2 - downEdges.size();
                        int failEdges = downEdges.size();
                        Rnetwork += Math.pow(1-p, failEdges)*Math.pow(p, goodEdges);
                    }

            }
            
            pvals.add(p);
            rvals.add(Rnetwork);
      }
      // write the results to some file
      writeToFile(pvals, rvals, "PvsR.csv");
     
     }
     
      private static void flipExperiment() throws IOException {
        /*
        All combinations involved in the network can be represented by the numbers from 0 to 1023 as follows
        0 - 0000000000
        1 - 0000000001 - one link is up
        2 - 0000000010 - a different link is up
        3 - 0000000011 - two different links are up
        ...
        1023 - 1111111111 - all links are up!
        This gives us an easier way to represent all configurations of a network
        */
        /*
         The 10 edges of the network itself can be encoded as follows
        0--1 : 0
        0--2 : 1
        0--3 : 2
        0--4 : 3
        1--2 : 4
        1--3 : 5
        1--4 : 6
        2--3 : 7
        2--4 : 8
        3--4 : 9
        */
        int V = 5 ; // number of nodes
        int filler = 1; // to fill up the edges with
        double p = 0.95;// p is contstant for this experiment
        // data for plotting to be stored in ...
        ArrayList<Integer> kvals = new ArrayList<Integer>(); // for link probability values
        ArrayList<Double> rvals = new ArrayList<Double>(); // for reliability values
        
        // we repeat this experiment with the k values from [0,99]
        
      for(int k = 0 ; k < 100 ; k++){
          
          double Rnetwork = 0.00; // reliability of a network with p as given
          int num_trials = 100;
          // for num_trials number of trials
            for(int trials = 0 ; trials < num_trials; trials++){
                
                // select k random configurations
                ArrayList<Integer> krandom = getKRandom(k, (int) Math.pow(2, V*(V-1)/2));
                    for(int i = 0 ; i <= 1023 ; i++){

                            // to store the edges that are down
                            ArrayList<Integer> downEdges = new ArrayList<>();
                            // a function that fills up the above array list with indices
                            // here do a check to see if the number is contained in our list
                            if(krandom.contains(i))
                                fillUp(downEdges, ~i, V);
                            else
                                fillUp(downEdges, i, V);
                            
                            // create a complte graph with the new constructor and filler value
                            Graph network = new Graph(V, filler);
                            // set the down edge value accordingly
                            for(Integer d : downEdges){
                                // set the downed edge in the adjacency matrix
                                Edge e = getRowColumn(d, V);
                                network.addEdgeUndirected(e);
                            }
                            //printMatrix(network.adjMatrix);
                            // if the network is up add to the reliability value
                            if(network.isConnected()){
                                int goodEdges = V*(V-1)/2 - downEdges.size();
                                int failEdges = downEdges.size();
                                Rnetwork += Math.pow(1-p, failEdges)*Math.pow(p, goodEdges);
                            }

                    }
            }
            kvals.add(k);
            rvals.add(Rnetwork/(double)num_trials);
      }
      // write the results to some file
      writeToFileK(kvals, rvals, "KvsR.csv");
     
      
      
      }

    private static int nChoose(int n, int i) {
        
        if(i<0)
            return 0;
        if(i == 0)
            return 1;
        if(i == n)
            return 1;
        else return nFactorial(n)/(nFactorial(i)*nFactorial(i - 1));
    
    }

    private static int nFactorial(int n) {
       
        if(n == 0)
            return 1;
        else return n*nFactorial(n - 1);
    }

    private static void fillUp(ArrayList<Integer> downEdges, int num, int V) {
       
        int marker = 1; // to be across the number to spot 1's
        
        //System.out.println(marker<<0);
        for(int i = 0 ; i < V*(V-1)/2 ; i++){
            int result = marker & num;
            if( result == 0)
                downEdges.add(i);
            
            marker = marker<<1;
               
        }
        
        
    
    }

    private static Edge getRowColumn(Integer d, int V) {
        V = V - 1;
        int row = 0 ;
        
        do{
            if(d < V)
                return new Edge(row, row+d+1, 0);
            row++;
            d = d - V;
            V = V - 1;
            
        }while(V>=0 && d>=0);
    
        return new Edge(0,0,0);
    }

    // debugging utility
    private static void printMatrix(int[][] adjMatrix) {
      
        
        for(int i = 0 ; i < adjMatrix.length ; i++){
        String line = "";
            for(int j = 0 ; j < adjMatrix.length ; j++){
                line = line+adjMatrix[i][j]+"\t";
            }
            System.out.println(line);
        }
    
    }

    private static void writeToFile(ArrayList<Double> pvals, ArrayList<Double> rvals, String fname) throws IOException {
        FileWriter fw = new FileWriter(fname);
        // code to write to files
        DecimalFormat df = new DecimalFormat("#.##");
        for(int i = 0 ; i < pvals.size() ; i++){
            fw.write(df.format(pvals.get(i))+","+rvals.get(i)+"\n");
        }
        fw.close();
    
    }

    private static void writeToFileK(ArrayList<Integer> kvals, ArrayList<Double> rvals, String fname) throws IOException {
         FileWriter fw = new FileWriter(fname);
        // code to write to files
        //DecimalFormat df = new DecimalFormat("#.##");
        for(int i = 0 ; i < kvals.size() ; i++){
            fw.write(kvals.get(i)+","+rvals.get(i)+"\n");
        }
        fw.close();
    
    }

    private static ArrayList<Integer> getKRandom(int k, int i) {
    // getting k random numbers from 0 to i-1;
        
        Random r = new Random();
        ArrayList<Integer> randSet = new ArrayList<>();
        while(randSet.size() < k){
            
            int newRandom = Math.abs(r.nextInt())%i;
            if(!randSet.contains(newRandom))
                randSet.add(newRandom);
        }
        
        return randSet;
    
    }

    

   

   
    
    
}
