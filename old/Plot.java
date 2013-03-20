package pszt;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Plot {
    Random seed = new Random();
    final int SIZE = 8;
    final double MAX = 10.0;
    PlotPanel plotPanel;

    private JPanel getContent() {
        plotPanel = new PlotPanel();
        Refresher t = new Refresher(plotPanel);
        t.start();
        return plotPanel;
    }

    private JPanel getUIPanel() {
        JButton button1 = new JButton("reset data");
        JButton button3 = new JButton("reset net");
        JButton buttonZoomIn = new JButton("zoom in");
        JButton buttonZoomOut = new JButton("zoom out");
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                plotPanel.resetData();
            }
        });
        button3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				plotPanel.resetNetwork();
			}
		});
        buttonZoomIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				double[] r = plotPanel.getRange();
				for(int i = 0; i < 4; i++)
					r[i]*=(8.0/9.0);
				plotPanel.setRange(r);
			}
		});
        buttonZoomOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				double[] r = plotPanel.getRange();
				for (int i = 0; i < 4; i++)
					r[i]*=(9.0/8.0);
				plotPanel.setRange(r);
			}
		});
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setBorder(BorderFactory.createEtchedBorder());
        gbc.weightx = 1.0;
        panel.add(button1, gbc);
        panel.add(buttonZoomOut, gbc);
        panel.add(buttonZoomIn, gbc);
        panel.add(button3, gbc);
        return panel;
    }

    public static void main(String[] args) {
        Plot plot = new Plot();
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(plot.getContent());
        f.add(plot.getUIPanel(), "Last");
        f.setSize(400,400);
        f.setLocation(50,50);
        f.setVisible(true);
    }
}

class PlotPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	ArrayList<Double> x;
	ArrayList<Double>[] y;
    double xMin;
    double xMax;
    double yMin;
    double yMax;
    
    double xScale;
    double yScale;
    Point2D.Double origin = new Point2D.Double();
    Point2D.Double offset = new Point2D.Double();
    
    final int PAD = 20;
    final boolean DEBUG = false;
    
    private final int inputN = 1;
    private final int hiddenN = 20;
    private final int outputN = 2;
    Perceptron perceptron = new Perceptron(inputN, hiddenN, outputN, true);
    
    boolean y1set = false;
    boolean y2set = false;
    double xval, y1val, y2val;
    
    boolean plotall = false;

    public PlotPanel() {
        resetData();
        double[] n = {-8, 8, -3, 3};
        setRange(n);
        this.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {
				if (! (y1set || y2set))
				{
					xval = (e.getX()-offset.x)/xScale;
				}
				if (e.getButton() == MouseEvent.BUTTON1)
				{
					y1val = (offset.y-e.getY())/yScale;
					y1set = true;
				}
				if (e.getButton() == MouseEvent.BUTTON2 || e.getButton() == MouseEvent.BUTTON3)
				{
					y2val = (offset.y-e.getY())/yScale;
					y2set = true;
				}
				if (y1set && y2set)
				{
					x.add(xval);
					y[0].add(y1val);
					y[1].add(y2val);
					y1set = y2set = false;
				}
				repaint();
			}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {}
		});
    }
    
    public double[] getRange()
    {
    	double[] range = {xMin, xMax, yMin, yMax};
    	return range;
    }
    
    public void setRange(double[] range)
    {
    	if (range.length != 4) return;
    	xMin = range[0];
    	xMax = range[1];
    	yMin = range[2];
    	yMax = range[3];
    	repaint();
    }
    
    protected void setScale()
    {
//        double[] xVals = {-10,10,20};//getExtremeValues(x);
//        xMin = xVals[0]-0.1*xVals[2];
//        xMax = xVals[1]+0.1*xVals[2];
        if(DEBUG)
            System.out.printf("xMin = %5.1f  xMax = %5.1f%n", xMin, xMax);
//        double[] yVals = {-10,10,20};//getExtremeValues(y);
//        yMin = yVals[0]-0.1*yVals[2];
//        yMax = yVals[1]+0.1*yVals[2];
        int w = getWidth();
        int h = getHeight();
        xScale = (w - 2*PAD)/(xMax - xMin);
        yScale = (h - 2*PAD)/(yMax - yMin);
        if(xMax < 0) {
            origin.x = w - PAD;
            offset.x = origin.x - xScale*xMax;
        } else if(xMin < 0) {
            origin.x = PAD - xScale*xMin;
            offset.x = origin.x;
        } else {
            origin.x = PAD;
            offset.x = PAD - xScale*xMin;
        }
        if(yMax < 0) {
            origin.y = PAD;
            offset.y = yScale*yMax - origin.y;
        } else if(yMin < 0) {
            origin.y = h - (PAD - yScale*yMin) ;
            offset.y = origin.y;
        } else {
            origin.y = h - PAD;
            offset.y = PAD - yScale*yMin + h;
        }    	
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();
        setScale();

        // Draw abcissa.
        g2.draw(new Line2D.Double(PAD, origin.y, w-PAD, origin.y));
        // Draw ordinate.
        g2.draw(new Line2D.Double(origin.x, PAD, origin.x, h-PAD));
        g2.setPaint(Color.red);
        // Mark origin.
        g2.fill(new Ellipse2D.Double(origin.x-2, origin.y-2, 4, 4));

        // Plot data.
        Color[] colors = { Color.blue, Color.green, Color.red, Color.cyan, Color.magenta, Color.yellow };
        for(int i = 0; i < y.length; i++)
        {
        	g2.setPaint(colors[i%y.length]);
           	for (int j=0; j<x.size(); j++)
        	{
	            double x1 = offset.x + xScale*x.get(j);
	            double y1 = offset.y - yScale*y[i].get(j);
	            g2.fill(new Ellipse2D.Double(x1-2, y1-2, 4, 4));
        	}
        }
        
        // Plot new points (if present)
        if (y1set || y2set)
        {
        	g2.setPaint(Color.gray);
        	g2.draw(new Line2D.Double(
        			offset.x + xScale*xval,
        			offset.y - yScale*yMin,
        			offset.x + xScale*xval,
        			offset.y - yScale*yMax));
        }
        
        if (y1set)
        {
        	g2.setPaint(Color.blue);
        	double x1 = offset.x + xScale*xval;
        	double y1 = offset.y - yScale*y1val;
        	g2.fill(new Ellipse2D.Double(x1-1,y1-1,3,3));
        }
        
        if (y2set)
        {
        	g2.setPaint(Color.green);
        	double x1 = offset.x + xScale*xval;
        	double y1 = offset.y - yScale*y2val;
        	g2.fill(new Ellipse2D.Double(x1-1,y1-1,3,3));
        }
        
        int plotN;
        if (plotall) plotN = hiddenN + outputN;
        else plotN = outputN;
        
        // Calculate perceptron output.
	    double[] dx = new double [(int)(w-2*PAD)/4];
	    double[][] py = new double [dx.length][plotN];
	    if (plotall)
	    {
	    	for (int i = 0; i < dx.length; i++)
	    	{
	        	dx[i] = xMin + (xMax-xMin)*i/dx.length;
	        	py[i] = perceptron.calcall(dx[i]);	    		
	    	}
	    }
	    else
	    {
	        for (int i = 0; i < dx.length; i++)
	        {
	        	dx[i] = xMin + (xMax-xMin)*i/dx.length;
	        	py[i] = perceptron.calculate(dx[i]);
	        }
        }
	    
	    // Plot perceptron output.
        for(int i = 0; i < plotN; i++)
        {
        	for (int j = 0; j < dx.length-1; j++)
        	{
	        	g2.setPaint(colors[i%colors.length]);
	        	double x1 = offset.x + xScale*dx[j];
	            double y1 = offset.y - yScale*py[j][i];
	            double x2 = offset.x + xScale*dx[j+1];
	            double y2 = offset.y - yScale*py[j+1][i];
	            g2.draw(new Line2D.Double(x1, y1, x2, y2));
        	}
        }

        // Draw extreme data values.
        g2.setPaint(Color.black);
        Font font = g2.getFont();
        FontRenderContext frc = g2.getFontRenderContext();
        LineMetrics lm = font.getLineMetrics("0", frc);
        String s = String.format("%.1f", xMin);
        float width = (float)font.getStringBounds(s, frc).getWidth();
        double x = offset.x + xScale*xMin;
        g2.drawString(s, (float)x, (float)origin.y+lm.getAscent());
        s = String.format("%.1f", xMax);
        width = (float)font.getStringBounds(s, frc).getWidth();
        x = offset.x + xScale*xMax;
        g2.drawString(s, (float)x-width, (float)origin.y+lm.getAscent());
        s = String.format("%.1f", yMin);
        width = (float)font.getStringBounds(s, frc).getWidth();
        double y = offset.y - yScale*yMin;
        g2.drawString(s, (float)origin.x+1, (float)y+lm.getAscent());
        s = String.format("%.1f", yMax);
        width = (float)font.getStringBounds(s, frc).getWidth();
        y = offset.y - yScale*yMax;
        g2.drawString(s, (float)origin.x+1, (float)y);
    }

    public void trainNetwork()
    {
    	if (this.x.size() == 0) return;
    	int i = (int)(Math.random()*this.x.size());
    	if (i == this.x.size()) i = 0;
    	double[] x = new double[inputN];
    	double[] y = new double[outputN];
    	x[0] = this.x.get(i);
    	y[0] = this.y[0].get(i);
    	y[1] = this.y[1].get(i);
    	perceptron.learn(x,y,this.x.size());
    	repaint();
    }
    
    public void resetData()
    {
    	x = new ArrayList<Double>();
    	y = (ArrayList<Double>[])(new ArrayList[outputN]);
    	for (int i=0; i<y.length; i++)
    	{
    		y[i] = new ArrayList<Double>();
    	}
    	repaint();
    }
    
    public void resetNetwork()
    {
    	perceptron = new Perceptron(inputN, hiddenN, outputN, true);
//    	double[][] h = { { 1, 3 }, { -5, -1 }, { -1, -7}, { 4, -2 } };
//    	double[][] o = { { 2, 3, 1, 2, 1 }, { 0, 0, 0, 0, 0 } };
//    	perceptron = new Perceptron(h, o);
    	repaint();
    }

    private double[] getExtremeValues(ArrayList<Double> ... d) {
        double min = Double.MAX_VALUE;
        double max = -min;
        for(int i = 0; i < d.length; i++) {
        	for (int j = 0; j < d[i].size(); j++)
        	{
	            if(d[i].get(j) < min) {
	                min = d[i].get(j);
	            }
	            if(d[i].get(j) > max) {
	                max = d[i].get(j);
	            }
        	}
        }
        return new double[] { min, max, max-min };
    }
}

class Refresher extends Thread
{
	PlotPanel plot;
	public Refresher(PlotPanel plotref)
	{
		plot = plotref;
	}
	
	public void run()
	{
		try
		{
			while(true)
			{
				plot.trainNetwork();
				Thread.sleep(50L);
			}
		} catch (InterruptedException ex) {}
	}
}
