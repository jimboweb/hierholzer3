import java.util.*;



class PrintCircuit{
    /**
     *
     * @param edges list of adjacent vertices for current edges
     * @return circuit in deque list
     */
    Deque<Integer> makeEulerianCircuit(Deque<Integer>[] edges)
    {
        // edgeCount = number of unused edges from vertex
        Map<Integer,Integer> edgeCount = new HashMap<>();

        //populate edge count
        for (int i=0; i<edges.length; i++)
        {
            edgeCount.put(i, edges[i].size());
        }

        // return empty list for empty graph
        if (edges.length==0)
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
            if (edgeCount.get(currentVertexNumber) > 0)
            {
                currentPath.push(currentVertexNumber);
                Deque<Integer> currentVertex = edges[currentVertexNumber];
                int nextVertexNumber = currentVertex.pop();
                edgeCount.put(currentVertexNumber,edgeCount.get(currentVertexNumber)-1);
                currentVertexNumber = nextVertexNumber;
            }

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

        // TODO: 3/2/18 0) getInput, 1) buildGraph 2) if(!isEven) println("0") and quit, 3) makeEulerianCircuit
        PrintCircuit pc = new PrintCircuit();
        pc.inputAndPrintCircuit(pc.new ConsoleOutput());

    }


    private void inputAndPrintCircuit(Outputter outputter){
        Scanner scanner = new Scanner(System.in);
        List<List<Integer>> inputs;
        List<Integer> in = new ArrayList<>();
        in.add(scanner.nextInt());
        in.add(scanner.nextInt());
        if (in.get(0) == 0 || in.get(1) == 0) {
            System.out.println("0");
            System.exit(0);
        }
        //set the capacity of the array in advance to save a few microseconds.
        inputs = new ArrayList<>(in.get(1)+1);
        inputs.add(in);
        for (int i = 0; i < inputs.get(0).get(1); i++) {
            in = new ArrayList<>();
            in.add(scanner.nextInt());
            in.add(scanner.nextInt());
            inputs.add(in);

        }
        Deque<Integer>[] graph = buildGraph(inputs);
        if(graph.length==0){
            System.out.println("0");
            System.exit(0);
        } else {
            System.out.println("1");
        }
        Deque<Integer> circuit = makeEulerianCircuit(graph);
        circuit.removeFirst();
        while(!circuit.isEmpty()){
            int nextNode = circuit.pollLast()+1;
            System.out.print(nextNode + " ");
        }
        System.out.println();
    }

    /**
     * create an array of Deques to represent the edges of the graph
     * @param inputs list of integer pair lists from input. first list is the number of nodes and edges.
     * @return arrayList of Dequeues to represent the edges in the graph. empty array means graph isn't even and has no Eulerian circuit.
     */
    private Deque<Integer>[] buildGraph(List<List<Integer>> inputs){
        int numberOfNodes = inputs.get(0).get(0);
        int numberOfEdges = inputs.get(0).get(1);
        Deque<Integer>[] edges = new Deque[numberOfEdges];
        for(int i=0;i<edges.length;i++){
            edges[i] = new ArrayDeque<>();
        }
        //evenChecker is array for each node where
        // [0] = inputs and [1] = outputs.
        int[][] evenChecker = new int[numberOfNodes][2];
        for(int i=1;i<inputs.size();i++){
            List<Integer> input = inputs.get(i);
            int from = input.get(0)-1;
            int to = input.get(1)-1;
            evenChecker[from][0]++;
            evenChecker[to][1]++;
            edges[from].push(to);
        }

        //checks to make sure inputs=outputs for each edge.
        if(!isGraphEven(evenChecker)){
            return new Deque[0];
        }

        return edges;
    }

    private boolean isGraphEven(int[][] evenChecker){
        for(int[] evenCheck:evenChecker){
            if(evenCheck.length!=2){
                throw new IllegalArgumentException("evenChecker must be array of size-2 arrays");
            }
            if(evenCheck[0]!=evenCheck[1]){
                return false;
            }
        }
        return true;
    }



    interface Outputter{
        public void output(String output);
        public void outputLine(String output);
        public void newLine();
    }


    class ConsoleOutput implements Outputter{
        public void output(String output) {
            System.out.print(output);
        }

        public void outputLine(String output){
            System.out.println(output);
        }

        public void newLine(){
            System.out.println();
        }
    }

}