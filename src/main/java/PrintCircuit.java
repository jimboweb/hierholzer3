import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.List;

class PrintCircuit{
    void printCircuit(List<List<Integer>> adj)
    {
        // adj represents the adjacency list of
        // the directed graph
        // edge_count represents the number of edges
        // emerging from a vertex
        Map<Integer,Integer> edge_count = new HashMap<>();

        for (int i=0; i<adj.size(); i++)
        {
            //find the count of edges to keep track
            //of unused edges
            edge_count.put(i, adj.get(i).size());
        }

        if (adj.size()==0)
            return; //empty graph

        // Maintain a stack to keep vertices
        Stack<Integer> curr_path = new Stack<>();

        // vector to store final circuit
        List<Integer> circuit = new ArrayList<>();

        // start from any vertex
        curr_path.push(0);
        int curr_v = 0; // Current vertex

        while (!curr_path.empty())
        {
            // If there's remaining edge
            if (edge_count.containsKey(curr_v))
            {
                // Push the vertex
                curr_path.push(curr_v);

                // Find the next vertex using an edge
                int next_v = adj.get(curr_v).get(0); //??not sure if this is right

                // and remove that edge
                edge_count.put(curr_v,edge_count.get(curr_v)-1);
                adj.get(curr_v).remove(0); //??this probably isn't right

                // Move to next vertex
                curr_v = next_v;
            }

            // back-track to find remaining circuit
            else
            {
//                circuit.push_back(curr_v); //TODO: figure out what to do here

                // Back-tracking
//                curr_v = curr_path.top();  //TODO: what does this mean?
                curr_path.pop();
            }
        }

        // we've got the circuit, now print it in reverse
        for (int i=circuit.size()-1; i>=0; i--)
        {
            System.out.println(circuit.get(i));
            if (i!=0)
                System.out.println(" -> ");
        }
    }
}