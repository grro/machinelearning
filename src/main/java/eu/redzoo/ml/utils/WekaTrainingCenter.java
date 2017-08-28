package eu.redzoo.ml.utils;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;

import weka.classifiers.Classifier;
import weka.classifiers.functions.LinearRegression;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;


public class WekaTrainingCenter {


    public static Function<Double[], Double> train(List<Double[]> featuresList, List<Double> labels) {
        return train(new LinearRegression(), featuresList, labels);
    }

    public static Function<Double[], Double> train(Classifier classifier, List<Double[]> featuresList, List<Double> labels) {
        return train(classifier, Data.merge(featuresList, labels));
    }

    public static Function<Double[], Double> train(Classifier classifier, Map<Double, Double> trainData) {
        return train(classifier, trainData.entrySet().stream().map(entry -> new Double[] { entry.getKey(), entry.getValue() }).collect(Collectors.toList()));
    }

    public static WekaHypothesisFunction train(Classifier classifier, List<Double[]> trainData) {
        final ArrayList<Attribute> attrs = buildAttributes(trainData.get(0).length);
        final Instances trainInstances = newInstanceList(attrs, trainData);

        try {
            classifier.buildClassifier(trainInstances);
            return new WekaHypothesisFunction(classifier, attrs);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static ArrayList<Attribute> buildAttributes(int numberOfAttributes) {
        final ArrayList<Attribute> attrs = new ArrayList<Attribute>(numberOfAttributes);
        for (int i = 0; i < numberOfAttributes; i++) {
            attrs.add(new Attribute("attr" + i));
        }
        return attrs;
    }

    private static Instances newUnlabeldInstanceList(ArrayList<Attribute> attrs, List<Double[]> records) {
        return newInstanceList(attrs, Data.addLastColumn(records, 0));
    }

    private static Instances newInstanceList(ArrayList<Attribute> attrs, List<Double[]> records) {
        final Instances dataSet = new Instances("Instances", attrs, attrs.size());
        dataSet.setClassIndex(attrs.size() - 1);

        // set values
        for (Double[] record : records) {
            final Instance instance = new DenseInstance(attrs.size());
            for (int i = 0; i < attrs.size(); i++) {
                instance.setValue((Attribute) attrs.get(i), Double.valueOf(record[i].toString()));
            }
            dataSet.add(instance);
        }

        return dataSet;
    }

    public static final class WekaHypothesisFunction implements Function<Double[], Double> {
        private final ArrayList<Attribute> attrs;
        private final Classifier classifier;

        WekaHypothesisFunction(Classifier classifier, ArrayList<Attribute> attrs) {
            this.attrs = attrs;
            this.classifier = classifier;
        }

        @Override
        public Double apply(Double[] featureVector) {
            final Instances instances = newUnlabeldInstanceList(attrs, ImmutableList.of(featureVector));
            try {
                return classifier.classifyInstance(instances.get(0));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public String toString() {
            return classifier.toString();
        }
    }
}
