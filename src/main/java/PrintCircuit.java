import java.util.*;


class PrintCircuit{
    /**
     *
     * @param adjacentVertices list of adjacent vertices for current edges
     * @return circuit in deque list
     */
    Deque<Integer> printCircuit(Deque<Integer>[] adjacentVertices)
    {
        // edgeCount = number of unused edges from vertex
        Map<Integer,Integer> edgeCount = new HashMap<>();

        //populate edge count
        for (int i=0; i<adjacentVertices.length; i++)
        {
            edgeCount.put(i, adjacentVertices[i].size());
        }

        // return empty list for empty graph
        if (adjacentVertices.length==0)
            return new LinkedList<>(); //empty graph

        // Stack for the path in the current iteration
        Deque<Integer> currentPath = new ArrayDeque<>();

        // queue for the final circuit
        Deque<Integer> circuit = new LinkedList<>();

        // start from any vertex
        currentPath.push(0);
        int currentVertexNumber = 0; // Current vertex

        while (!currentPath.isEmpty())
        {
            // If there's remaining edge
            if (edgeCount.get(currentVertexNumber) > 0)
            {
                // Push the vertex
                currentPath.push(currentVertexNumber);

                Deque<Integer> currentVertex = adjacentVertices[currentVertexNumber];
                // Find the next vertex using an edge
                int nextVertexNumber = currentVertex.pop();

                // and remove that edge
                edgeCount.put(currentVertexNumber,edgeCount.get(currentVertexNumber)-1);

                // Move to next vertex
                currentVertexNumber = nextVertexNumber;
            }

            // back-track to find remaining circuit
            else
            {
                circuit.add(currentVertexNumber);

                // Back-tracking
                currentVertexNumber = currentPath.pop();
            }
        }

        // we've got the circuit, now print it in reverse
        while (!circuit.isEmpty())
        {
            System.out.print(circuit.pop() + " ");
        }
        System.out.println();
        return circuit;
    }


    public static void main(String[] args)
    {
        PrintCircuit pc = new PrintCircuit();
        //vector< vector<int> > adj1, adj2;
        Deque<Integer>[] adj1 = new Deque[3];
        for(int i=0;i<adj1.length;i++){
            adj1[i] = new ArrayDeque<>();
        }
        Deque<Integer>[] adj2 = new Deque[7];
        for(int i=0;i<adj2.length;i++){
            adj2[i] = new ArrayDeque<>();
        }


        // Build the edges
        adj1[0].push(1);
        adj1[1].push(2);
        adj1[2].push(0);
        pc.printCircuit(adj1);

        // Input Graph 2
        adj2[0].push(1);
        adj2[0].push(6);
        adj2[1].push(2);
        adj2[2].push(0);
        adj2[2].push(3);
        adj2[3].push(4);
        adj2[4].push(2);
        adj2[4].push(5);
        adj2[5].push(0);
        adj2[6].push(4);
        pc.printCircuit(adj2);

    }
}