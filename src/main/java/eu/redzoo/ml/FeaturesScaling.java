package eu.redzoo.ml;

import java.util.List;
import java.util.function.Function;

public class FeaturesScaling implements Function<Double[], Double[]> {
    private final int numAttr;
    private final double[] range;
    private final double[] average;

    public static Function<Double[], Double[]> createFunction(List<Double[]> featuresList) {
        return new FeaturesScaling(featuresList);
    }

    private FeaturesScaling(List<Double[]> featuresList) {
        this.numAttr = featuresList.get(0).length;
        this.average = new double[numAttr];
        this.range = new double[numAttr];

        double[] max = new double[numAttr];
        double[] min = new double[numAttr];

        for (Double[] features : featuresList) {
            for (int j = 0; j < numAttr; j++) {
                if (max[j] < features[j]) {
                    max[j] = features[j];
                }
                if (min[j] > features[j]) {
                    min[j] = features[j];
                }

                average[j] += features[j];
            }
        }

        for (int j = 0; j < numAttr; j++) {
            range[j] = max[j] - min[j];
            average[j] = average[j] / featuresList.size();
        }
    }

    @Override
    public Double[] apply(Double[] features) {
        Double[] normalizedFeatures = new Double[features.length];
        for (int j = 0; j < numAttr; j++) {
            if (j == 0) {
                // do not scale the first column
                normalizedFeatures[j] = features[j];

            } else {
                normalizedFeatures[j] = (features[j] - average[j]) / range[j];
            }
        }
        return normalizedFeatures;
    }
}