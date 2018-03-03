import java.util.*;



class PrintCircuit{
    /**
     *
     * @param edges list of adjacent vertices for current edges
     * @return circuit in deque list
     */
    Deque<Integer> makeEulerianCircuit(Deque<Integer>[] edges, int numberOfNodes)
    {

        // return empty list for empty graph
        if (edges.length==0)
            return new LinkedList<>(); //empty graph

        // Stack for the path in the current iteration
        Deque<Integer> currentPath = new ArrayDeque<>();

        // queue for the final circuit
        Deque<Integer> circuit = new ArrayDeque<>();

        // start from any vertex
        currentPath.push(0);
        int currentVertexNumber = 0; // Current vertex

        while (!currentPath.isEmpty())
        {
            //if there are outgoing edges
            if (edges[currentVertexNumber].size() > 0)
            {
                currentPath.push(currentVertexNumber);
                int nextVertexNumber = edges[currentVertexNumber].pop();
                currentVertexNumber = nextVertexNumber;
            }

            // otherwise step back
            else
            {
                circuit.add(currentVertexNumber);
                currentVertexNumber = currentPath.pop();
            }
        }

        return circuit;

    }


    public static void main(String[] args)
    {

        PrintCircuit pc = new PrintCircuit();
        pc.inputAndPrintCircuit();

    }


    /**
     * Get the input, check to make sure the graph is even and run the printCircuit function
     */
    private void inputAndPrintCircuit(){
        Scanner scanner = new Scanner(System.in);
        int[] in = new int[2];
        in[0] = scanner.nextInt();
        in[1] = scanner.nextInt();
        Deque<Integer>[] edges = new Deque[in[0]];
        for(int i=0;i<in[0];i++)
        {
            edges[i] = new ArrayDeque<>();
        }

        // evenChecker is a Nx2 array where [0] = incoming edges and [1] = outgoing edges
        //should be equal or graph isn't Eulerian
        int[][] evenChecker = new int[in[0]][2];
        for (int i = 0; i < in[1]; i++) {
            int from = scanner.nextInt()-1;
            int to = scanner.nextInt()-1;
            evenChecker[from][0]++;
            evenChecker[to][1]++;
            edges[from].push(to);

        }
        if(!isGraphEven(evenChecker)){
            System.out.println("0");
            System.exit(0);
        } else {
            System.out.println("1");
        }
        Deque<Integer> circuit = makeEulerianCircuit(edges, in[0]);
        while(circuit.size()>1){
            int nextNode = circuit.pollLast()+1;
            System.out.print(nextNode + " ");
        }
        System.out.println();
    }

    /**
     * checks to make sure that incoming edges = outgoing edges
     * @param evenChecker a Nx2 array where [0] = incoming edges and [1] = outgoing edges
     * @return true if incoming eges = outgoing edges, false otherwise
     */
    private boolean isGraphEven(int[][] evenChecker){
        for(int[] evenCheck:evenChecker){
            if(evenCheck[0]!=evenCheck[1]){
                return false;
            }
        }
        return true;
    }



}