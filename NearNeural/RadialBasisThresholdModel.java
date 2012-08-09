package NearNeural;
/**A threshold which is linear.<strong>This method may not work at all with some backpropagation methods</strong>

  @author Stephen Wattam
  @version 0.3
*/  
public class RadialBasisThresholdModel extends WeightedThresholdModel implements ThresholdingAlgorithm{
	public RadialBasisThresholdModel(double x){
		super(1);
	}

	/**Calculates Neuron value from all Edges and their source Neurons.
		@return This Neuron's value
	*/
	public double value(Edge[] edges){
		double result = 0;
		int limit = edges.length-1;	//ugly but quick versino of the loop

		for (int i=limit;i>=0;i--)		//sum all weights times their node's value
			result += (edges[i].getWeight()*edges[i].getSource().value());	
		



		return rbf(result);	//take sigmoid of that to smoothly combine
	}


	private double rbf(double x){
	    if(Math.abs(x) >= 1){
		return Math.abs(1) + 1.4;
	    }

	    return Math.sqrt(1+x);
	}


	private double drbf(double x){
	    if(Math.abs(x) >= 1){
		if(x > 0)
		    return 1;
		else
		    return -1;
	    }
		
		return Math.sqrt(Math.pow(x, 2));
	}
	    

	/**Validates input against the applicable range for this thresholding algorithm.
	 	@param input The value to check
		@return True if the value is applicable to run though without error
	 */
	public boolean validateInput(double input){
	       return true;
	}

	/**Returns the differential of the thresholding function.
	  @param edges The edges that comprise the links this neuron has.
	  @return The differential of the value
	*/
	public double differentialValue(Edge[] edges){
	    //TODO
		double result = 0;
		int limit = edges.length-1;	//ugly but quick versino of the loop

		for (int i=limit;i>=0;i--)		//sum all weights times their node's value
			result += (edges[i].getWeight()*edges[i].getSource().value());	
	

		return drbf(result);	
	    
	}
}
