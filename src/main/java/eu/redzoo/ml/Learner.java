package eu.redzoo.ml;


import java.util.List;


public class Learner {

    public static LinearRegressionFunction train(LinearRegressionFunction targetFunction, List<Double[]> dataset, List<Double> labels, double alpha) {
        int m = dataset.size();
        double[] thetaVector = targetFunction.getThetas();
        double[] newThetaVector = new double[thetaVector.length];

        // compute the new theta of each element of the theta array
        for (int j = 0; j < thetaVector.length; j++) {

            // summarize the error gap * feature
            double sumErrors = 0;
            for (int i = 0; i < m; i++) {
                Double[] featureVector = dataset.get(i);
                double error = targetFunction.apply(featureVector) - labels.get(i);
                sumErrors += error * featureVector[j];
            }

            // compute the new theta value
            double gradient = (1.0 / m) * sumErrors;
            newThetaVector[j] = thetaVector[j] - alpha * gradient;
        }

        return new LinearRegressionFunction(newThetaVector);
    }
}
