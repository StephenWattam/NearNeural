import javax.swing.*; 
import javax.swing.JPanel;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.*;
import java.awt.font.*;
/**This was very quickly hacked together in order to graph things, it's not fully documented and still buggy.
@author Stephen Wattam
  @version 0.1
*/
public class LineGraph extends Canvas {
	private final int PERCENT_BORDER = 20;
	/**Holds edges of various types, based on precise net structure*/
	Vector<Double> valueSet = new Vector<Double>();


	public LineGraph(){
		repaint();	
		setVisible(true);
	}

	public void add(double val){
		valueSet.add(new Double(val));
	}

	public void paint(Graphics g){
		if(valueSet.size() > 0){
			Iterator<Double> iter = valueSet.iterator();
			double min = 0;
			double max = 0;
			while(iter.hasNext()){
				double currentVal = iter.next().doubleValue();
				if(currentVal > max)
					max = currentVal;
				if(currentVal < min)
					min = currentVal;
			}
			double range = max - min;
			double yInterval = (double)getHeight()/(double)range;
			double xInterval = (double)getWidth()/(double)valueSet.size();

			//=================================================================================

			//Graphics2D g2d = (Graphics2D)g;
			//g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			//g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);

			g.fillRect(0, 0, this.getWidth(), this.getHeight());

			g.setColor(Color.GRAY);
			for(double i = 0;i<=getWidth();i+=xInterval)
				g.drawLine((int)i, 0, (int)i, getHeight());
			for(double i = 0;i<=getHeight();i+=yInterval)
				g.drawLine(0, (int)i, getWidth(), (int)i);
		
			
			for(int i=0;i<valueSet.size();i++){
				double currentVal = valueSet.get(i).doubleValue();
				g.setColor(Color.WHITE);
				
				g.drawOval(	(int)((i*xInterval)-2.5), 
						getHeight() - (int)((currentVal*(getHeight()/max))+2.5), 5, 5);

				if(i > 0)
					g.drawLine(	(int)((i-1)*xInterval), getHeight() - (int)(valueSet.get(i-1).doubleValue()*(getHeight()/max)),
							(int)(i*xInterval), getHeight() - (int)(currentVal*(getHeight()/max)));
			}

		//	renderText("Max edge weight: " + new Double(maxWeight).toString(), g2d, 5, 10, 8);




		}

	}

	/**Renders text to the x-y coordinate given, on the Graphics2D plane provided.
	  	@param p_text The text which is to be written
		@param g2d The graphics2D object on which to write
		@param p_x The x co-ordinate Of the bottoom left
		@param p_y The y co-ordinate of the bottom of the text
	*/
	private void renderText(String p_text, Graphics2D g2d, int p_x, int p_y, int fontsize){
		FontRenderContext frc = g2d.getFontRenderContext();
		Font f = new Font("Helvetica",Font.BOLD, fontsize);
		String s = new String(p_text);
		TextLayout tl = new TextLayout(s, f, frc);
		Dimension theSize=getSize();
		g2d.setColor(Color.WHITE);
		tl.draw(g2d, p_x,p_y+fontsize);
		
	}

	/**Recalculates edge and neurons lists from the net.
	  	@throws NearNeural.IndexOutOfBoundsException based on the conditions of the other two.
	*/
	public void update() throws NearNeural.IndexOutOfBoundsException{
		repaint();
	}

}
