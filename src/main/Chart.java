


/* This file is based on a demo of JFreeChart:
 * http://www.java2s.com/Code/Java/Chart/JFreeChartLineChartDemo6.htm
 * 
 * The code has been modified by us (Peter Dekker, Yikang Wang,
 * Rafael Reia, Artur Alkaim) to suit the needs of our project.
 */

package main;
import java.util.Arrays;


/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2004, by Object Refinery Limited and Contributors.
 *
 * Project Info:  http://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc. 
 * in the United States and other countries.]
 *
 * -------------------
 * LineChartDemo6.java
 * -------------------
 * (C) Copyright 2004, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: LineChartDemo6.java,v 1.5 2004/04/26 19:11:55 taqua Exp $
 *
 * Changes
 * -------
 * 27-Jan-2004 : Version 1 (DG);
 * 
 */


import java.awt.BasicStroke;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.Series;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
//import org.jfree.ui.Spacer;

/**
 * A simple demonstration application showing how to create a line chart using data from an
 * {@link XYDataset}.
 *
 */
public class Chart extends ApplicationFrame {
	
	double[] average;
	
    /**
     * Creates a new demo.
     *
     * @param title  the frame title.
     */
    public Chart(final String title, double[][] parameterSettings) {

        super(title);

        final XYDataset dataset = getDataFromFiles(title, parameterSettings);
        final JFreeChart chart = createChart(dataset,title);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);

    }
    
    /**
     * Creates a sample dataset.
     * 
     * @return a sample dataset.
     */
    private XYDataset getDataFromFiles(String title, double[][] parameterSettings) {
    	int nSettings = parameterSettings.length;
    	
    	// Create dataset, one series for every saved parameter setting
    	final XYSeries[] series= new XYSeries[nSettings];
    	final XYSeriesCollection dataset = new XYSeriesCollection();
    	
    	int j=0;
    	
        double[] sum = new double[nSettings];
        average = new double[nSettings];
    	
    	for(j = 0; j <nSettings; j++)
    	{
    		System.out.println("j:"+j);
	    	series[j]= new XYSeries(Arrays.toString(parameterSettings[j]));
	    	
	        // Open file
	    	BufferedReader reader = null;
			try {
				String fileName =  "output" + j + ".txt";
				System.out.println(j);
				reader = new BufferedReader( new FileReader (fileName));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        String line = null;
	        
	        int i = 1;

	        try {
				while( ( line = reader.readLine() ) != null ) {
					int num = Integer.parseInt(line);
					sum[j] += num;
			        series[j].add(i,num);
					i++;
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
	    	average[j] = sum[j]/(i-1);
			
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 dataset.addSeries(series[j]);
    	}       
        return dataset;
        
    }
    
    /**
     * Creates a chart.
     * 
     * @param dataset  the data for the chart.
     * 
     * @return a chart.
     */
    private JFreeChart createChart(final XYDataset dataset, final String title) {
        
        // create the chart...
        final JFreeChart chart = ChartFactory.createXYLineChart(
            title,      // chart title
            "X",                      // x axis label
            "Y",                      // y axis label
            dataset,                  // data
            PlotOrientation.VERTICAL,
            true,                     // include legend
            true,                     // tooltips
            false                     // urls
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);
        for(int i = 0; i < average.length; i++)
        {
        	chart.addSubtitle(new TextTitle("Average " + (i+1) + ": "+ average[i], TextTitle.DEFAULT_FONT));
        }

        
        // get a reference to the plot for further customisation...
        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        
        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setLinesVisible(true);
        renderer.setShapesVisible(false);
        plot.setRenderer(renderer);

        // change the auto tick unit selection to integer units only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        
        return chart;
    }
    

}