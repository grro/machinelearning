package eu.redzoo.ml;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class FunctionUnderfittingExample {

    public static void main(String[] args) throws IOException {

        // load the labels and features
        List<Double> labels = Data.loadLabels("/house_price_berlin_data.txt");
        List<Double[]> dataset = Data.loadFeaturesList("/house_price_berlin_data.txt");

        // add the fist 1.0 column
        List<Double[]>  extendedDataset = dataset.stream().map(features -> new Double[] { 1.0, features[0]}).collect(Collectors.toList());


        // create and train the target function
        LinearRegressionFunction targetFunction =  new LinearRegressionFunction(new double[] { 1.0, 1.0 });
        Map<Double, Double> costHistory = new HashMap<>();

//        for (int i = 0; i < 50; i++) {
//        for (int i = 0; i < 200; i++) {
        for (int i = 0; i < 1000; i++) {
            // get better fitting function
            targetFunction = Learner.train(targetFunction, extendedDataset, labels, 0.00000002);

            // save the cost of new function
            costHistory.put(i+1.0, Cost.cost(targetFunction, extendedDataset, labels));
        }


        System.out.println("trained function:              " + targetFunction);
        System.out.println("calling it with [1.0, 1330.0]: " + targetFunction.apply(new Double[] { 1.0, 1330.0 }));


        // print some graphs
        Graph costGraph = Graph.create(costHistory, "cost curve", "cost", "iterations");
        costGraph.display();

        Graph graph = Graph.create(Data.getFirstColumn(dataset), labels, "house prices", "Price(€) in 1000´s", "Size in m²");
        final LinearRegressionFunction func = targetFunction;
        graph.addLine("plain", x -> func.apply(new Double[] { 1.0, x }));
        graph.display();
    }
}

