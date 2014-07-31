/*
 * This source code is designed and written by Saideep Sambaraju
 * It is to be used for non commercial purposes only
 * DO NOT copy this code without permission from the author
 * Contact Info : 408-203-0492
 */

package aatnproject3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author esamsai
 */
class Graph{
    /*
    Graph is defined as an adjacency matrix of dimensions VxV
    Here V is number of vertices of the graph
    The vertices are numbered 0..V-1
    */
    /*
    The structure that stores the adjacency matrix that created this graph
    */
    int[][] adjMatrix;
    // We have a structure to store the all pairs shortest paths between any two vertices i and j
    int[][] allPairs;
    // We have a structure to store the next node for an all pairs shortest path solution
    int[][] nextNode;
    // Number of vertices in the graph
    int V;
    // For graph connectivity
    int numConnectComp = 0; // number of connected components initialized to 0
    // constructor for initializing the graph of project 3
    Graph(int V, int filler){
         adjMatrix = new int[V][V];
        allPairs = new int[V][V];
        nextNode = new int[V][V];
       //toVisit = new boolean[V];
        this.V = V; 
        for(int i = 0 ; i < this.V ; i++){
            for(int j = 0 ; j < this.V ; j++){
                if(i!=j)
                    adjMatrix[i][j] = filler;
            }
        }
    }
    // default constructor initializes the graph to 0s
    Graph(int V){
        adjMatrix = new int[V][V];
        allPairs = new int[V][V];
        nextNode = new int[V][V];
       //toVisit = new boolean[V];
        this.V = V;    
    }
    /*
    The Graph constructor initiates the adjacency matrix with dimensions VxV
    and initialize all edges to infinity or in this case to Integer.MAX_VALUE
    initiates all the shortest pairs to Integer.MAX_VALUE
    and initiates all the next hops to -1
    */
    Graph(int V, ArrayList<Edge> E){
        adjMatrix = new int[V][V];
        allPairs = new int[V][V];
        nextNode = new int[V][V];
       // toVisit = new boolean[V];
            this.V = V;    
            // Initialize all the matrices
                for(int i = 0 ; i < V ; i++){
                    for(int j = 0 ; j < V ; j++){
                        if(i!=j){
                            adjMatrix[i][j] = Integer.MAX_VALUE;
                            allPairs[i][j] = Integer.MAX_VALUE;
                            nextNode[i][j] = -1;
                            }
                        else
                            nextNode[i][j] = j;
                    }
                }
                
            // Initialize all the edges
                for(Edge eij : E){
                    
                    addEdge(eij);
                    
                }
            
        
    }
    /*
    This function adds an edge x-y to the graph. We add all the edges to the graph to initialize the graph
    Note: This remains a directed graph so an edge x-y is not the same as edge y-x
    */
    void addEdge(Edge E){
        adjMatrix[E.x][E.y] = E.weight;
        // In preparation for the Floyd Warshall Algorithm
        allPairs[E.x][E.y]  = E.weight;
        nextNode[E.x][E.y] = E.y;
    }
    /*
    This function adds an edge x-y to the undirected graph. We add all the edges to the graph to initialize the graph
    
    */
    void addEdgeUndirected(Edge E){
        adjMatrix[E.x][E.y] = E.weight;
        adjMatrix[E.y][E.x] = E.weight;
        // In preparation for the Floyd Warshall Algorithm
        allPairs[E.x][E.y]  = E.weight;
        allPairs[E.y][E.x]  = E.weight;
        nextNode[E.x][E.y] = E.y;
        nextNode[E.y][E.x] = E.x;
    }
    
    void floydWarshall(){
        
        // Perform the shortest path computation for all the pairs using Floyd Warshall Algorithm
        
        for(int k = 0 ; k < V ; k++){
            for(int i = 0 ; i < V ; i++){
                for(int j = 0 ; j < V ; j++){
                    if(allPairs[i][k]+allPairs[k][j]<allPairs[i][j]){
                        allPairs[i][j] = allPairs[i][k] + allPairs[k][j];
                        nextNode[i][j] = nextNode[i][k];
                    }
                }
            }
        }
    }

    void forceInputs() throws FileNotFoundException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        Scanner sc = new Scanner(new File("forcedInput.txt"));
        for(int i = 0 ; i < this.V ; i++){
            String[] inputs = sc.nextLine().split("\\t");
            for(int j = 0 ; j < this.V ; j++){
                this.adjMatrix[i][j] = Integer.parseInt(inputs[j]);
            }
        }
    }

    boolean isConnected() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
        this.numConnectComp=1;
        // Next To visit
        boolean[] toVisit = new boolean[this.V];
        int numVertices = this.V;
        ArrayList<Integer> next = new ArrayList<Integer>();
        next.add(0);
        //ArrayList<Integer> after = new ArrayList<Integer>();
        while(next.size()!=0){
            int i = next.get(0);
            toVisit[i] = true;
            numVertices--;
            next.remove(0);
            for(int k = 0 ; k < V ; k++){
                if(this.adjMatrix[i][k] > 0 && !toVisit[k] && !next.contains(k)){
                    next.add(k);
                }
            }
            
        }
        
        if(numVertices == 0){
            return true;
        }
        else
        {
            System.out.println("Not connected are");
            for(int i = 0 ; i < this.V ; i++){
                if(!toVisit[i]){
                    System.out.println(i);
                }
            }
            return false;
        }
    
    }

    
    
}
