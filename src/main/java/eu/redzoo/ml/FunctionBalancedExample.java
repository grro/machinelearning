package eu.redzoo.ml;

import eu.redzoo.ml.utils.Graph;
import eu.redzoo.ml.utils.WekaTrainingCenter;
import eu.redzoo.ml.utils.Data;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


public class FunctionBalancedExample {

    public static void main(String[] args) throws IOException {

        // load the labels and features
        List<Double> labels = Data.loadLabels("/house_price_berlin_data.txt");
        List<Double[]> datasetFile = Data.loadFeaturesList("/house_price_berlin_data.txt");
        List<Double[]> dataset = datasetFile.stream().map(features -> new Double[] { 1.0, features[0], Math.pow(features[0], 2)}).collect(Collectors.toList());


        // scale the extended feature list
        Function<Double[], Double[]> scalingFunc = FeaturesScaling.createFunction(dataset);
        List<Double[]>  scaledDataset  = dataset.stream().map(scalingFunc).collect(Collectors.toList());

        // create hypothesis function with initial thetas and train it
        LinearRegressionFunction targetFunction =  new LinearRegressionFunction(new double[] { 1.0, 1.0, 1.0 });
        for (int i = 0; i < 10000; i++) {
            targetFunction = Learner.train(targetFunction, scaledDataset, labels, 0.1);
        }


        // make a prediction for 600 m2 size house
        Double[] scaledFeatureVector = scalingFunc.apply(new Double[] { 1.0, 600.0, 360000.0 });
        double predictedPrice = targetFunction.apply(scaledFeatureVector);
        System.out.println(predictedPrice);


        // print some graphs
        Graph graph = Graph.create(Data.getFirstColumn(datasetFile), labels, "house prices", "Price(€) in 1000´s", "Size in m²");
        final LinearRegressionFunction func = targetFunction;
        graph.addLine("plain", x -> func.apply(scalingFunc.apply(new Double[] { 1.0, x, Math.pow(x, 2) })));
        graph.display();
    }
}

