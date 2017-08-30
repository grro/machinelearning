package eu.redzoo.ml;


import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.function.Function2D;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;



// helper class to print a graph
public class Graph extends ApplicationFrame {

    private static final Color[] COLORS = new Color[] { Color.YELLOW, Color.GREEN, Color.BLUE, Color.MAGENTA };
    private final JFreeChart chart;
    private final XYSeries xySeries;
    private int idxLines = 0;

    private Graph(XYSeries xySeries, String title, String yAxisLabel, String xAxisLabel) {
        super(title);
        this.xySeries = xySeries;
        this.chart = ChartFactory.createScatterPlot(title,
                                                    xAxisLabel,
                                                    yAxisLabel,
                                                    new XYSeriesCollection(xySeries),
                                                    PlotOrientation.VERTICAL,
                                                    true,
                                                    true,
                                                    false);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(1000, 540));
        setContentPane(chartPanel);
    }

    public static Graph createArrayList(List<Double> data, String name, String xAxisLabel, String yAxisLabel) {
        final Map<Integer, Double> map = new HashMap<>();
        for (int i = 0; i < data.size(); i++) {
            map.put(i + 1, data.get(i));
        }
        return create(map, name, xAxisLabel, yAxisLabel);
    }

    public static Graph create(List<Double> xData, List<Double> yData, String name, String xAxisLabel, String yAxisLabel) {
        Map<Double, Double> map = new HashMap<>();
        for (int i = 0; i < xData.size(); i++) {
            map.put(xData.get(i), yData.get(i));
        }
        return create(map, name, xAxisLabel, yAxisLabel);
    }

    public static Graph create(Map<? extends Number, ? extends Number> data, String name, String xAxisLabel, String yAxisLabel) {
        return new Graph(toXYSeries(data, name), name,xAxisLabel, yAxisLabel);
    }

    private static XYSeries toXYSeries(Map<? extends Number, ? extends Number> data, String name) {
        final XYSeries series = new XYSeries(name);
        for (Map.Entry<? extends Number, ? extends Number> point : data.entrySet()) {
            series.add(point.getKey(), point.getValue());
        }
        return series;
    }

    public void addLine(String label, Function<Double, Double> func) {
        addLine(label, idxLines + 1, COLORS[idxLines], (int) xySeries.getMaxX(), func);
        idxLines++;
    }


    private void addLine(String label, int layer, Color color, int length, Function<Double, Double> func) {
        LineFunction linefunction2d = new LineFunction(func);
        XYDataset dataset = DatasetUtilities.sampleFunction2D(linefunction2d, 0D, length, 100, label);

        XYPlot xyplot = chart.getXYPlot();
        xyplot.setDataset(layer, dataset);
        XYLineAndShapeRenderer xylineandshaperenderer = new XYLineAndShapeRenderer(true, false);
        xylineandshaperenderer.setSeriesPaint(0, color);
        xyplot.setRenderer(layer, xylineandshaperenderer);
    }


    public void display() {
        pack();
        RefineryUtilities.centerFrameOnScreen(this);
        setVisible(true);
    }

    private class LineFunction implements Function2D {

        private final Function<Double, Double> func;

        private LineFunction(Function<Double, Double> func) {
            this.func = func;
        }

        public double getValue(double x) {
            double y = func.apply(x);
            return y;
        }

    }
}
