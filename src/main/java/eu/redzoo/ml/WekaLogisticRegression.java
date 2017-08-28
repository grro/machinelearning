package eu.redzoo.ml;

import eu.redzoo.ml.utils.Data;
import eu.redzoo.ml.utils.Graph;
import weka.classifiers.Classifier;
import weka.classifiers.functions.Logistic;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.List;


public class WekaLogisticRegression {

    public static void main(String[] args) throws Exception {

        // build the features list
        ArrayList<Attribute> attributes = new ArrayList<>();
        Attribute sizeAttribute = new Attribute("sizeFeature");
        attributes.add(sizeAttribute);

        Attribute squaredSizeAttribute = new Attribute("squaredSizeFeature");
        attributes.add(squaredSizeAttribute);

        ArrayList<String> classVal = new ArrayList<>();
        classVal.add("true");
        classVal.add("false");
        Attribute topsellerAttribute = new Attribute("topsellerLabel", classVal);
        attributes.add(topsellerAttribute);


        Instances trainingSet = new Instances("trainData", attributes, 13);
        trainingSet.setClassIndex(trainingSet.numAttributes() - 1);

        Instance instance = new DenseInstance(3);
        instance.setValue(sizeAttribute, 90.0);
        instance.setValue(squaredSizeAttribute, Math.pow(90.0, 2));
        instance.setValue(topsellerAttribute, "true");
        trainingSet.add(instance);



        Instances dataset = new Instances("dataSet", attributes, 13);
        dataset.setClassIndex(dataset.numAttributes() - 1);

        // load the labels and features
        List<Double> labels = Data.loadLabels("/house_berlin_bestseller.txt");
        List<Double[]> featuresList = Data.loadFeaturesList("/house_berlin_bestseller.txt");

        for (int i = 0; i < featuresList.size(); i++) {
            instance = new DenseInstance(3);
            instance.setValue(sizeAttribute, featuresList.get(i)[0]);
            instance.setValue(squaredSizeAttribute, Math.pow(featuresList.get(i)[0], 2));
            instance.setValue(topsellerAttribute, ((labels.get(i) == 1) ? "true" : "false"));
            dataset.add(instance);
        }

        trainingSet = dataset;
        Instances validationSet  = dataset;


        // create the target function and train it by calling the build methods
        Classifier targetFunction = new Logistic();
        //Classifier targetFunction = new J48();
        targetFunction.buildClassifier(trainingSet);
        System.out.println("targetFunction " + targetFunction);


        // predict
        Instances unlabeledInstances = new Instances("trainData", attributes, 1);
        unlabeledInstances.setClassIndex(trainingSet.numAttributes() - 1);
        Instance unlabeled = new DenseInstance(3);
        unlabeled.setValue(sizeAttribute, 440.0);
        unlabeled.setValue(squaredSizeAttribute, Math.pow(440.0, 2));
        unlabeledInstances.add(unlabeled);

        int clsLabel  = (int) targetFunction.classifyInstance(unlabeledInstances.get(0));
        String prediction = classVal.get(clsLabel);
        System.out.println(prediction);
        assert prediction.equals("false");



        unlabeledInstances = new Instances("trainData", attributes, 1);
        unlabeledInstances.setClassIndex(trainingSet.numAttributes() - 1);
        unlabeled = new DenseInstance(3);
        unlabeled.setValue(sizeAttribute, 113.0);
        unlabeled.setValue(squaredSizeAttribute, Math.pow(113.0, 2));
        unlabeledInstances.add(unlabeled);

        clsLabel  = (int) targetFunction.classifyInstance(unlabeledInstances.get(0));
        prediction = classVal.get(clsLabel);
        System.out.println(prediction);
        assert prediction.equals("true");


    }
}

