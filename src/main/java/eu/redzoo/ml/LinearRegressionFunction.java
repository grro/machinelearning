package eu.redzoo.ml;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Function;

public class LinearRegressionFunction implements Function<Double[], Double> {
    private final double[] thetaVector;

    public LinearRegressionFunction(double[] thetaVector) {
        this.thetaVector = Arrays.copyOf(thetaVector, thetaVector.length);
    }

    public Double apply(Double[] featureVector) {
        // for computational reasons the first element has to be 1.0
        assert featureVector[0] == 1.0;

        // simple, sequential implementation
        double prediction = 0;
        for (int j = 0; j < thetaVector.length; j++) {
            prediction += thetaVector[j] * featureVector[j];
        }

        return prediction;
    }

    public double[] getThetas() {
        return Arrays.copyOf(thetaVector, thetaVector.length);
    }


    public String toString() {
        String func = "";
        for (int j = 0; j < thetaVector.length; j++) {
            if (j == 0) {
                func += thetaVector[j] + " * 1";
            } else {
                func += " + " + thetaVector[j] + " * x" + j;
            }
        }

        return func;
    }


    public static void main(String[] args) throws IOException {

        // the theta vector is the output of the learn process
        double[] thetaVector = new double[] { 1.004579, 5.286822 };
        LinearRegressionFunction targetFunction = new LinearRegressionFunction(thetaVector);

        // calling the
        Double[] featureVector = new Double[] { 1.0, 1330.0 };
        double predictedPrice = targetFunction.apply(featureVector);



        System.out.println(predictedPrice);
    }
}
