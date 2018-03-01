// A Java program to check if a given directed graph is Eulerian or not

// A class that represents an undirected graph
import java.util.*;
import java.util.LinkedList;

// This class represents a directed graph using adjacency list
class Graph
{
    private int vertices;   // No. of vertices
    private LinkedList<Integer> adjacentVertices[];//Adjacency List
    private int in[];           //maintaining in degree

    //Constructor
    Graph(int vertices)
    {
        this.vertices = vertices;
        adjacentVertices = new LinkedList[vertices];
        in = new int[this.vertices];
        for (int i=0; i<vertices; ++i)
        {
            adjacentVertices[i] = new LinkedList();
            in[i]  = 0;
        }
    }

    //Function to add an edge into the graph
    void addEdge(int v,int w)
    {
        adjacentVertices[v].add(w);
        in[w]++;
    }

    // A recursive function to print DFS starting from v
    void depthFirstSearch(int v, Boolean visited[])
    {
        // Mark the current node as visited
        visited[v] = true;

        int n;

        // Recur for all the vertices adjacent to this vertex
        Iterator<Integer> i = adjacentVertices[v].iterator();
        while (i.hasNext())
        {
            n = i.next();
            if (!visited[n])
                depthFirstSearch(n,visited);
        }
    }

    // Function that returns reverse (or transpose) of this graph
    Graph reverseGraph()
    {
        Graph g = new Graph(vertices);
        for (int v = 0; v < vertices; v++)
        {
            // Recur for all the vertices adjacent to this vertex
            Iterator<Integer> i = adjacentVertices[v].listIterator();
            while (i.hasNext())
            {
                g.adjacentVertices[i.next()].add(v);
                (g.in[v])++;
            }
        }
        return g;
    }



    /* This function returns true if the directed graph has an eulerian
       cycle, otherwise returns false  */
    Boolean isEulerian()
    {

        // Check if in degree and out degree of every vertex is same
        for (int i = 0; i < vertices; i++)
            if (adjacentVertices[i].size() != in[i])
                return false;

        return true;
    }

    public static void main (String[] args) throws java.lang.Exception
    {
        Graph g = new Graph(5);
        g.addEdge(1, 0);
        g.addEdge(0, 2);
        g.addEdge(2, 1);
        g.addEdge(0, 3);
        g.addEdge(3, 4);
        g.addEdge(4, 0);

        if (g.isEulerian())
            System.out.println("Given directed graph is eulerian ");
        else
            System.out.println("Given directed graph is NOT eulerian ");
    }
}
//This code is contributed by Aakash Hasija