package eu.redzoo.ml;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


public class FunctionOverfittingExample {

    public static void main(String[] args) throws IOException {

        // load the labels and features
        List<Double> labels = Data.loadLabels("/house_price_berlin_data.txt");
        List<Double[]> dataset = Data.loadFeaturesList("/house_price_berlin_data.txt");

        // add the fist 1.0 column
        List<Double[]>  extendedDataset = dataset.stream().map(features -> new Double[] { 1.0, features[0]}).collect(Collectors.toList());

        // add more (computed) features
        extendedDataset = extendedDataset.stream().map(features -> new Double[] { 1.0, features[1], Math.pow(features[1], 2), Math.pow(features[1], 3), Math.pow(features[1], 4), Math.pow(features[1], 5)}).collect(Collectors.toList());

        // scale
        Function<Double[], Double[]> scalingFunc = FeaturesScaling.createFunction(extendedDataset);
        List<Double[]>  scaledDataset  = extendedDataset.stream().map(scalingFunc).collect(Collectors.toList());


        // create and train the hypothesis function
        LinearRegressionFunction targetFunction =  new LinearRegressionFunction(new double[] { 1.0, 1.0, 1.0, 1.0, 1.0 });
        long loops = 5000000l;
        for (long i = 0; i < loops; i++) {
            targetFunction = Learner.train(targetFunction, scaledDataset, labels, 0.1);
        }

        // print some graphs
        Graph graph = Graph.create(Data.getFirstColumn(dataset), labels, "house prices", "Price(€) in 1000´s", "Size in m²");
        final LinearRegressionFunction func = targetFunction;
        graph.addLine("plain", x -> func.apply(scalingFunc.apply(new Double[] { 1.0, x, Math.pow(x, 2), Math.pow(x, 3), Math.pow(x, 4), Math.pow(x, 5) })));
        graph.display();
    }
}
