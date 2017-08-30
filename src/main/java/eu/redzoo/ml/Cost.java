package eu.redzoo.ml;

import java.util.List;
import java.util.function.Function;


public class Cost {

    public static double cost(Function<Double[], Double> targetFunction, List<Double[]> dataset, List<Double> labels) {
        int m = dataset.size();

        double sumSquaredErrors = 0;
        for (int i = 0; i < m; i++) {
            // get the next feature vector
            Double[] featureVector = dataset.get(i);

            // compute the "gap"
            double predicted = targetFunction.apply(featureVector);
            double label = labels.get(i);
            double gap = predicted - label;

            // add it to the sum
            sumSquaredErrors += Math.pow(gap, 2);
        }

        return (1.0 / (2 * m)) * sumSquaredErrors;
    }
}
