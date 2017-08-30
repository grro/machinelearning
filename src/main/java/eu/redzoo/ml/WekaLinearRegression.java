package eu.redzoo.ml;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LinearRegression;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.List;


public class WekaLinearRegression {

    public static void main(String[] args) throws Exception {

        List<Double[]> featuresList = new ArrayList<>();
        featuresList.add(new Double[] { 90.0 });
        featuresList.add(new Double[] { 101.0 });
        featuresList.add(new Double[] { 103.0 });
        featuresList.add(new Double[] { 90.0 });

        // create the labels
        List<Double> labels = new ArrayList<>();
        labels.add(249.0);
        labels.add(338.0);
        labels.add(304.0);


        // load the labels and features
        labels = Data.loadLabels("/house_price_berlin_data.txt");
        featuresList = Data.loadFeaturesList("/house_price_berlin_data.txt");


        // build the features list
        ArrayList<Attribute> attributes = new ArrayList<>();
        Attribute sizeAttribute = new Attribute("sizeFeature");
        attributes.add(sizeAttribute);

        Attribute squaredSizeAttribute = new Attribute("squaredSizeFeature");
        attributes.add(squaredSizeAttribute);

        Attribute priceAttribute = new Attribute("priceLabel");
        attributes.add(priceAttribute);


        Instances trainingSet = new Instances("trainData", attributes, 13);
        trainingSet.setClassIndex(trainingSet.numAttributes() - 1);

        Instance instance = new DenseInstance(3);
        instance.setValue(sizeAttribute, 90.0);
        instance.setValue(squaredSizeAttribute, Math.pow(90.0, 2));
        instance.setValue(priceAttribute, 249.0);
        trainingSet.add(instance);


        Instances dataset = new Instances("dataSet", attributes, featuresList.size());
        dataset.setClassIndex(dataset.numAttributes() - 1);

        for (int i = 0; i < featuresList.size(); i++) {
            instance = new DenseInstance(3);
            instance.setValue(sizeAttribute, featuresList.get(i)[0]);
            instance.setValue(squaredSizeAttribute, Math.pow(featuresList.get(i)[0], 2));
            instance.setValue(priceAttribute, labels.get(i));
            dataset.add(instance);
        }

        trainingSet = dataset;
        Instances validationSet  = dataset;



        // create the target function and train it by calling the build methods
        Classifier targetFunction = new LinearRegression();
        targetFunction.buildClassifier(trainingSet);
        System.out.println("targetFunction " + targetFunction);

        // evaluate
        Evaluation evaluation = new Evaluation(trainingSet);
        evaluation.evaluateModel(targetFunction, validationSet);
        System.out.println(evaluation.toSummaryString("Results", false));


        // predict
        Instances unlabeledInstances = new Instances("trainData", attributes, 1);
        unlabeledInstances.setClassIndex(trainingSet.numAttributes() - 1);
        Instance unlabeled = new DenseInstance(3);
        unlabeled.setValue(sizeAttribute, 1200.0);
        unlabeled.setValue(squaredSizeAttribute, Math.pow(1200.0, 2));
        unlabeledInstances.add(unlabeled);

        double prediction  = targetFunction.classifyInstance(unlabeledInstances.get(0));
        System.out.println(prediction);


        Graph graph = Graph.create(Data.getFirstColumn(featuresList), labels, "house prices", "Price(€) in 1000´s", "Size in m²");
        graph.addLine("weka", x -> {
            try {
                Instance inst = new DenseInstance(3);
                inst.setValue(sizeAttribute, x);
                inst.setValue(squaredSizeAttribute, Math.pow(x, 2));
                return targetFunction.classifyInstance(inst);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        graph.display();
    }
}

