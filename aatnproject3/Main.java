/*
 * This source code is designed and written by Saideep Sambaraju
 * It is to be used for non commercial purposes only
 * DO NOT copy this code without permission from the author
 * Contact Info : 408-203-0492
 */

package aatnproject3;

import java.util.ArrayList;

/**
 *
 * @author esamsai
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
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
        for(int i = 1022 ; i <= 1022 ; i++){
            
                // to store the edges that are down
                ArrayList<Integer> downEdges = new ArrayList<>();
                // a function that fills up the above array list with indices
                fillUp(downEdges, i);
                // create a complte graph with the new constructor and filler value
                Graph network = new Graph(V, filler);
                // set the down edge value accordingly
                for(Integer d : downEdges){
                    // set the downed edge in the adjacency matrix
                    Edge e = getRowColumn(d, V);
                    network.addEdgeUndirected(e);
                }
                printMatrix(network.adjMatrix);
          
        }
           
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

    private static void fillUp(ArrayList<Integer> downEdges, int num) {
       
        int marker = 1; // to be across the number to spot 1's
        
        //System.out.println(marker<<0);
        for(int i = 0 ; i < 10 ; i++){
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

    
    
}
