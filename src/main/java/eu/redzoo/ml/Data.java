package eu.redzoo.ml;


import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jfree.data.xy.XYSeries;

import com.google.common.base.Splitter;



// helper class to load example data
public class Data {

    public static List<Double[]> load(String name) {
        try {
            List<String> records = Files.readAllLines(new File(Data.class.getResource(name).getFile()).toPath());
            return records.stream()
                    .map(String::trim)
                    .filter(record -> !record.startsWith("#"))
                    .map(Data::split)
                    .collect(Collectors.toList());
        } catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

    public static List<Double[]> loadFeaturesList(String name) {
        List<Double[]> data = Data.load(name);
        return removeLastColumn(data);
    }

    public static List<Double> loadLabels(String name) {
        List<Double[]> data = Data.load(name);
        return getLastColumn(data);
    }

    private static Double[] split(String recordLine) {
        final List<String> elements = Splitter.on(",").trimResults().splitToList(recordLine);
        final Double[] result = new Double[elements.size()];
        for (int i = 0; i < elements.size(); i++) {
            result[i] = Double.parseDouble(elements.get(i));
        }
        return result;
    }

    public static XYSeries toXYSeries(List<Double[]> featuresList, int idxX, int idxY, String name) {
        XYSeries series = new XYSeries(name);
        for (Double[] features : featuresList) {
            series.add(features[idxY], features[idxX]);
        }
        return series;
    }


    public static List<Double[]> merge(List<Double[]> records, List<Double> values) {
        List<Double[]> result = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            result.add(merge(records.get(i), values.get(i)));
        }
        return result;
    }


    public static  List<Double[]> addLastColumn( List<Double[]> records, double value) {
        List<Double[]> result = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            result.add(addLastColumn(records.get(i), value));
        }
        return result;
    }


    private static Double[] addLastColumn(Double[] record, double value) {
        Double[] result = new Double[record.length + 1];
        System.arraycopy(record, 0, result, 0, record.length);
        result[result.length - 1] = value;
        return result;
    }


    private static Double[] merge(Double[] record, Double value) {
        Double[] result = new Double[record.length + 1];
        System.arraycopy(record, 0, result, 0, record.length);
        result[result.length - 1] = value;
        return result;
    }

    public static List<Double> getFirstColumn(List<Double[]> records) {
        return getColumn(records, 0);
    }

    private static List<Double> getLastColumn(List<Double[]> records) {
        return getColumn(records, records.get(0).length - 1);
    }

    private static List<Double> getColumn(List<Double[]> records, int idx) {
        return records.stream().map(record -> record[idx]).collect(Collectors.toList());
    }


    private static List<Double[]> removeLastColumn(List<Double[]> records) {
        return removeColumn(records, records.get(0).length - 1);
    }


    private static List<Double[]> removeColumn(List<Double[]> records, int idx) {
        return records.stream().map(record -> remove(record, idx)).collect(Collectors.toList());
    }


    private static Double[] remove(Double[] record, int idx) {
        final List<Double> result = new ArrayList<>();
        for (int i = 0; i < record.length; i++) {
            if (i != idx) {
                result.add(record[i]);
            }
        }
        return result.toArray(new Double[result.size()]);
    }

}
