import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


import static org.junit.Assert.*;

public class PrintCircuitTest {

    Random rnd = new Random();


    public static void main(String[] args){
            PrintCircuitTest test = new PrintCircuitTest();
            test.graphTimeToProblemSize();

        //test.customInput(6,9,6,3,3,2,3,4,3,1,1,5,2,3,3,6,4,3,5,3);
        //
        //5,8,1,2,2,1,2,3,3,2,3,4,4,3,4,5,5,4
    }


    public void graphTimeToProblemSize(){
        ArrayList<long[]> timeToProblemSize = new ArrayList<>();
        for(int trial = 0;trial<10000;trial++) {
            int graphSize = rnd.nextInt(2000)+2;
            InputGraph g = makeBalancedInputGraph(graphSize);
            PrintCircuit h = new PrintCircuit();
            int overtimeCount = 0;
            int loopCount = 0;
            long startTime = System.nanoTime();
            Deque<Integer>[] edges  = createInput(g,h);
            loopCount = getLoopCount(h,edges,g.nodes.size());
            long time = System.nanoTime() - startTime;
            timeToProblemSize.add(new long[]{g.edges.size(), loopCount, time});
            double ratio = (double)loopCount/g.edges.size();
            System.out.println("trial = " + trial + " operations = " + loopCount + " ratio = " + ratio + " time = " + time + " size = " + g.edges.size() );
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries operationsToProblemSizeSeries = new XYSeries("OperationsToProblemSize");
        XYSeries timeToProblemSizeSeries = new XYSeries("TimeToProblemSize");
        for(int i=1;i<timeToProblemSize.size();i++){
            long[] probSize = timeToProblemSize.get(i);
            operationsToProblemSizeSeries.add(probSize[0],probSize[1]);
            timeToProblemSizeSeries.add(probSize[0], probSize[2]/100);
        }
        dataset.addSeries(operationsToProblemSizeSeries);
        dataset.addSeries(timeToProblemSizeSeries);
        SwingUtilities.invokeLater(() -> {
            TestScatterPlot example = new TestScatterPlot("Time and Loop Count to Problem Size",dataset);
            example.setSize(800, 400);
            example.setLocationRelativeTo(null);
            example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            example.setVisible(true);
            int x = 0;
        });


    }

    private int getLoopCount(PrintCircuit pc, Deque<Integer>[] edges, int numberOfNodes) {
        //long startTime = System.nanoTime();
        pc.makeEulerianCircuit(edges, numberOfNodes);
        return pc.loopCount;
        //return System.nanoTime() - startTime;
        //
        // return pc.getOperations();
    }

//    @Test
//    public void testHierholzersAlgorithm() {
//        for(int trial = 0;trial<10000;trial++) {
//            int graphSize = rnd.nextInt(10)+2;
//            InputGraph g = makeBalancedInputGraph(graphSize);
//            String input = createInput(g);
//            PrintCircuit h = new PrintCircuit();
//            long runTime = getLoopCount(h);
//            long ratio = runTime/g.edges.size();
//            if (runTime>200000){
//                System.out.println("went over time");
//                fail("input = \n" + input);
//            }
////            testEulerianCycle(runTime);
//        }
//    }

    public class TestScatterPlot extends JFrame {
        public TestScatterPlot(String title, XYSeriesCollection dataset){
            super(title);
            JFreeChart chart = ChartFactory.createScatterPlot(
                    "Operations to problem size",
                    "problem size",
                    "time",
                    dataset);
            XYPlot plot = (XYPlot)chart.getPlot();
            plot.setBackgroundPaint(new Color(255,228,196));


            // Create Panel
            ChartPanel panel = new ChartPanel(chart);
            setContentPane(panel);
        }
    }

//    @org.junit.Test
//    public void findPath() {
//    }
//
//    @org.junit.Test
//    public void findFirstVertex() {
//    }
//
//    @org.junit.Test
//    public void makeNewPath() {
//    }

    private Deque<Integer>[] createInput(InputGraph g, PrintCircuit pc){

        Scanner scanner = new Scanner(System.in);
        Deque<Integer>[] edges = new Deque[g.nodes.size()];
        for(int i=0;i<g.nodes.size();i++)
        {
            edges[i] = new ArrayDeque<>();
        }

        // evenChecker is a Nx2 array where [0] = incoming edges and [1] = outgoing edges
        //should be equal or graph isn't Eulerian
        int[][] evenChecker = new int[g.nodes.size()][2];
        for (int i = 0; i < g.edges.size(); i++) {
            int from = g.edges.get(i).getFromNode();
            int to = g.edges.get(i).getToNode();
            evenChecker[from][0]++;
            evenChecker[to][1]++;
            edges[from].push(to);

        }
        if(!pc.isGraphEven(evenChecker)){
            edges = new Deque[0];
        }
        return edges;
    }



//    private String getFailOutput(Cycle c, InputGraph g, EulerianCycle instance){
//        String s = "Input graph was " + g.getInputAsString();
//        s+="\n";
//        s+=c.toString() + "\n";
//        //s+="buildCycleOps: " + instance.buildCycleOps;
//        return s;
//    }

    class InputNode{
        private ArrayList<Integer> edgesOut;
        private ArrayList<Integer> edgesIn;
        private final int index;
        //positive balance = more out than in
        //neg. balance = more in than out
        //0 balance = even
        private int balance;

        public InputNode(int index) {
            this.edgesOut = new ArrayList<>();
            this.edgesIn = new ArrayList<>();
            this.index = index;
            this.balance = 0;
        }

        public void addEdgeOut(int e){
            edgesOut.add(e);
            balance--;
        }
        public void addEdgeIn(int e){
            edgesIn.add(e);
            balance++;
        }

        public int getBalance() {
            return balance;
        }

        public boolean balanceIsPositive(){
            return balance>0;
        }

        public boolean balanceIsNegative(){
            return balance<0;
        }
        public boolean isBalanced(){
            return balance==0;
        }

        public int getIndex() {
            return index;
        }
    }

    class InputEdge{
        private final int index;
        private final int fromNode;
        private final int toNode;

        public InputEdge(int index, int fromNode, int toNode) {
            this.index = index;
            this.fromNode = fromNode;
            this.toNode = toNode;
        }

        public int getIndex() {
            return index;
        }

        public int getFromNode() {
            return fromNode;
        }

        public int getToNode() {
            return toNode;
        }

        @Override
        public String toString() {
            int f = fromNode+1;
            int t = toNode +1;
            return f + " " + t;
        }
    }

    class InputGraph {
        Random rnd = new Random();
        private final ArrayList<InputNode> nodes;
        ArrayList<InputEdge> edges;
        BitSet balancedNodes;
        boolean graphIsBalanced;
        public InputGraph(int size) {
            ArrayList<InputNode> nodes = new ArrayList<>();
            for(int i=0;i<size;i++){
                nodes.add(new InputNode(i));
            }
            this.nodes=nodes;
            this.edges = new ArrayList<>();
            balancedNodes = new BitSet(nodes.size());
        }

        public ArrayList<InputNode> getNodes() {
            return nodes;
        }

        /**
         * <ol>
         *     <li>adds an edge from node to node</li>
         *     <li>adds edge to edgeOut of from and edgeIn of to</li>
         *     <li>sets the balance of node which sets graphIsBalanced</li>
         * </ol>
         *
         * @param from from node
         * @param to to node
         */
        public void addEdge(int from, int to){
            if(!(nodes.size()>=(from))){
                throw new IllegalArgumentException("node " + from + " out of bounds");
            }
            if(!(nodes.size()>=to)){
                throw new IllegalArgumentException("node " + to + "out of bounds");
            }
            int edgeInd = edges.size();
            edges.add(new InputEdge(edgeInd,from,to));
            InputNode fromNode = nodes.get(from);
            InputNode toNode = nodes.get(to);
            fromNode.addEdgeOut(edgeInd);
            setOrClearBalancedNode(fromNode);
            toNode.addEdgeIn(edgeInd);
            setOrClearBalancedNode(toNode);
        }

        /**
         * checks if node is balanced and sets or clears the boolean in balancedNodes
         * @param n node to check
         */
        private void setOrClearBalancedNode(InputNode n){
            int ind = n.index;
            if(n.isBalanced()){
                setBalancedNode(ind);
            } else {
                clearBalancedNode(ind);
            }
        }

        /**
         * sets node in balancedNodes and sets graphIsBalanced
         * @param ind index of node
         */
        private void setBalancedNode(int ind){
            balancedNodes.set(ind);
            setGraphBalance();
        }

        /**
         * clears node in balancedNodes and sets graphIsBalanced
         * @param ind index of node
         */
        private void clearBalancedNode(int ind){
            balancedNodes.clear(ind);
            setGraphBalance();
        }

        /**
         * sets the balance of the graph if it's balanced
         */
        private void setGraphBalance(){
            graphIsBalanced = balancedNodes.cardinality()==nodes.size();
        }

        public InputNode getNode(int ind){
            return nodes.get(ind);
        }
        public InputEdge getEdge(int ind){
            return edges.get(ind);
        }
        public String getInputAsString(){
            String rtrn = nodes.size() + " " + edges.size()+"\n";
            for(InputEdge e:edges){
                rtrn+=e.toString() + "\n";
            }
            return rtrn;
        }
        public ArrayList<int[]> getInputAsArray(){
            ArrayList<int[]> input = new ArrayList<>();
            int[] firstLine = {nodes.size(),edges.size()};
            input.add(firstLine);
            for(InputEdge e:edges){
                int[] nextLine = {e.fromNode+1,e.toNode+1};
                input.add(nextLine);
            }
            return input;
        }

        /**
         * return a random node with optional sign; 0 = sign doesn't matter
         * @param sign 0 for any node, positive for positive node, negative for negative node
         * @return any node if sign 0, otherwise node of balanced sign
         */
        public InputNode getRandomNode(int sign){
            if(sign==0){
                int nodeNum = rnd.nextInt(nodes.size());
                InputNode n = nodes.get(nodeNum);
                return n;
            }
            boolean positive = sign>0;
            BitSet nodeWasTried = new BitSet(nodes.size());
            // TODO: 1/14/18 failing to get positive or negative node and returning new node with index -1
            while(nodeWasTried.cardinality()<nodes.size()){
                int rtrnInd = rnd.nextInt(nodes.size());
                if(nodeWasTried.get(rtrnInd)){
                    continue;
                }
                InputNode possibleRtrn = nodes.get(rtrnInd);
                if(positive) {
                    if (possibleRtrn.balanceIsPositive()) {
                        return possibleRtrn;
                    }
                } else {
                    if (possibleRtrn.balanceIsNegative()){
                        return possibleRtrn;
                    }
                }
                nodeWasTried.set(rtrnInd);
            }
            return new InputNode(-1);
        }

        public boolean graphIsBalanced() {
            return graphIsBalanced;
        }
    }

    /**
     * @return a balanced input graph
     */
    private InputGraph makeBalancedInputGraph(int graphSize){
        Random rnd = new Random();
        int n = rnd.nextInt(graphSize) + 2;
        boolean balanced = rnd.nextInt(10)<1;
        int m = 0;
        InputGraph gr = new InputGraph(n);
        for(InputNode fromNode:gr.getNodes()){
            int fromNodeInd = fromNode.getIndex();
            InputNode toNode;
            do{
                toNode = gr.getRandomNode(0);
            } while(toNode.index==fromNode.index);
            int toNodeInd = toNode.getIndex();
            gr.addEdge(fromNodeInd,toNodeInd);
        }

        while(!gr.graphIsBalanced()){
            InputNode nodeToBalance;
            do{
                nodeToBalance = gr.getRandomNode(0);
            } while(nodeToBalance.isBalanced());
            InputNode otherNode;
            if(nodeToBalance.balanceIsPositive()){
                otherNode = gr.getRandomNode(-1);
                gr.addEdge(nodeToBalance.getIndex(),otherNode.getIndex());
            } else {
                otherNode = gr.getRandomNode(1);
                gr.addEdge(otherNode.getIndex(),nodeToBalance.getIndex());
            }
        }
        boolean areNodesPointingToSelf = rnd.nextBoolean();
        int nodesPointingToSelf = areNodesPointingToSelf? rnd.nextInt(n):0;
        for(int i=0;i<nodesPointingToSelf;i++){
            int nodePointingToSelf = rnd.nextInt(n);
            gr.addEdge(nodePointingToSelf,nodePointingToSelf);
        }
        return gr;
    }

    /**
     * <o>Checks to make sure that the cycle meets following criteria:</p>
     * <ol>
     *     <li>every edge connects to the next edge</li>
     *     <li>every edge is used</li>
     *     <li>no edge used more than once</li>
     *     <li>last edge connects to first edge</li>
     * </ol>
     * @param g the original input graph
     * @return true if cycle is Eulerian in InputGraph g
     */
    private void testEulerianCycle(String output, InputGraph g, String input, long runTime){
        //System.out.println("output:\n" + output);
        System.out.println("problem size: " + g.edges.size());
        System.out.println("runtime: " + runTime);
        long ratio = runTime/(long)g.edges.size();
        System.out.println("ratio: " + ratio);
        if(output.split("\n")[0]=="0"){
            fail("Eulerian path marked as not Eulearian");
        }
        String[] nodeListAsString = output.split("\n")[1].split(" ");
        ArrayList<Integer> nodeList = new ArrayList<>();
        for (String s:nodeListAsString){
            try{
                nodeList.add(Integer.parseInt(s)-1);
            } catch (NumberFormatException ex) {
                System.out.println(ex);
            }
        }
        boolean[] edgeIsUsed = new boolean[g.edges.size()];


        Integer resultFrom = nodeList.get(0);
        int firstNode = resultFrom;
        Integer resultTo;
        InputNode testFrom = g.nodes.stream().filter(n->n.index==firstNode).findFirst().orElse(new InputNode(-1));
        if(testFrom.index!=resultFrom){
            fail("first node not in graph");
        }
        for(int i=0;i<nodeList.size();i++){
            resultFrom = nodeList.get(i);
            int resultFromFinal = resultFrom;
            testFrom = g.nodes.stream().filter(n->n.index==resultFromFinal).findFirst().orElse(new InputNode(-1));
            resultTo = nodeList.get((i+1)%nodeList.size());
            int resultToFinal = resultTo;
            Optional<Integer> testTo = testFrom.edgesOut.stream().filter(e->g.edges.get(e).toNode==resultToFinal).findFirst();
            if (!testTo.isPresent() || g.edges.get(testTo.get()).toNode!=resultTo){
                String failString = "Incorrect path at " + i + ". Node " + resultToFinal +
                        " does not connect from node " + resultFrom;
                failString += "output was " + output;
                failString += "input was " + input;
                fail(failString);
            }
            int fromFinal = resultFrom;
            int toFinal = resultTo;
            // FIXME: 2/20/18 right now this won't mark parallel edges because of findFirst
            Optional<InputEdge> nextEdge = g.edges.stream().filter(edge->edge.fromNode == fromFinal && edge.toNode == toFinal).findFirst();
            if(!nextEdge.isPresent()){
                fail("edge from " + fromFinal + " to " + toFinal + " does not exist");
            }
            edgeIsUsed[nextEdge.get().index] = true;
        }
//        for(int i=0;i<edgeIsUsed.length;i++){
//            boolean edgeUsed = edgeIsUsed[i];
//            if(!edgeUsed){
//                fail("edge " + i + " not used");
//            }
//        }




        System.out.println("Test passed");
    }


    private static int triangular(int n){
        int tri = 0;
        for(int i=1; i<n; i++){
            tri = tri + i;
        }
        return tri;
    }

}