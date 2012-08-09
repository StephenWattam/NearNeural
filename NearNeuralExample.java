import java.io.*;
import NearNeural.*;

/**This is just a simple example class to show what Neareural.NeuralNet can do.  It is not fully documented or structured to be reusable as an object in a larger system and is essentially one procedure.

  @author Stephen Wattam
  @version 0.2.0a
 */
public class NearNeuralExample{
	public static void main(String[] args){
		createAndTrain();		//train then save a net
		run();				//run and report on results of a net
	}

	private static void createAndTrain(){
		/*The number of neurons in hidden and output layers.*/
		int[] netLayers = {4,5,3};

		/*Three input items will be passed into the constructor, providing three input neurons*/
		double[] trainingInput = {.2,.6,.8};

		/*This is a terrible example as it's too simple!  The number of items in this array has to match the number of items in the final layer.*/		
		double[] desirableOutput = {.2,.6,.8};

		//somewhere to store the result
		double[] netOutput;
		
		try{
			System.out.println("Creating new net");
			/*Creates a new net with the values defined above.*/
			NeuralNet testNet = new NeuralNet(	trainingInput, 
							netLayers, 						
							new HyperbolicTangentThresholdModel(1), 		/*hyptan model with k of 1*/
							new ThreadedUnweightedBackPropagationMethod(.0002));	/*learning rate of 0.0002*/

			/*Removes a specific neuron.  This is one of the harder features to use without causing a ton of exceptions.
			 Things are indexed from 0, so this removes the second neuron on the second layer
			*/
			testNet.removeNeuron(testNet.getNeuron(1,1));
			/**/

			/*An example of pruning all edges with a weight -.1 > x > .1 
			  This would not normally be done before training, as the entire point is to speed a trained net without adversely affecting accuracy too much.
			* /
			testNet.prune(.1);				
			/**/

                /*
		 *                                        note to other folk
		 *
		 * Attaching a visualiser means that neurons are never truly deleted and never truly pruned.
		 * The visualiser does a sort of Schrodinger thing, the references it uses to monitor values prevent
		 * the garbage collector from getting rid of the objects used, even though I call it manually.
		 *
		 * To accurately refresh you have to run update(); to get the visualiser to rebuild the net structure.
		 * The repaint() method only adjusts weights, not structure (reading the net each time would be very slow)
		 *
		 *        repainting an old diagram by uncovering part of its window or panel or by callingn repaint(), will cause the repaint manager to 
		 * try to repaint neuron values which can no longer be referenced.  This causes a fuckload of null pointer exceptions
		 *  The moral of this story is to keep your net structure up to date. 
                 */
			NeuralVisualisationFrame monitorWindow = new NeuralVisualisationFrame(testNet, 320, 240, "complete net");
			monitorWindow.update();			//read the net structure

			LineGraphFrame graph = new LineGraphFrame(640, 480, "MSE");

			System.out.println("Training");
			System.out.println("--------");
		
			/*Creates a new file which will log the value of every single neuron and the mean squared error.*/
			FileWriter file = new FileWriter("log.csv");	

			int loops = 100000;	//loop this many times
			int output = 5000;	//output csv data every <this> loops
			double MSE;		//the mean squared error


			for(int i=0;i<loops;i++){
				/* This sets the input values and is used if they ever change, which in this example they do not
				   
				testNet.setInputValues(trainingInput);
				*/
				

				/*Runs the net.  This is not necessary but can be used to write output during training.  
				  Since the output of this script is more detailed than that this line is unnecessary
				  netOutput = testNet.getOutput();		//run net
				*/

				/*				more notes to other folk
				*
				* Training like this will do you very little good.  For a really accurate training regimen one must apply entropy to 
				* the inputs of the net whilst expecting the same output.  This will allow the net to recognise the larger features 
				* in the data you're trying to run rather than the irrelevant ones
				*/
				MSE = testNet.train(desirableOutput);		/*train net using last run values, store the mean squared error*/

				if((i%output) == 0){				/*update vals and do output*/
					monitorWindow.repaint();		/*redraw the monitor window to update values*/
					graph.add(MSE);
					graph.repaint();
					System.out.print("\r"+MSE + "\t" + i + " /" + loops + "              ");	/*simple console output*/

					//get all values and dumpt them to the csv
					for(int j=0;j<testNet.getLayerStructure().length;j++){
						for(int k=0;k<testNet.getLayer(j).countNeurons();k++){
							file.write(testNet.getLayer(j).getNeuron(k).value()+",");
						}
					}
					
					//dump the ideal values and mean squared error to a file
					file.write(MSE + "," + desirableOutput[0] + "," + desirableOutput[1] + "," + desirableOutput[2] +  "\n");
					
				}
			}
			//done training

			//write and close the file
			file.flush();
			file.close();
			
			/*  This code is here to output the values of the net before it is saved and loaded.
			    It's here to check the save/load code, but feel free to uncomment it (remove the space from the thing below)
			* /
			This needs to be done if input values have changed only.
			testNet.setInputValues(trainingInput);			//set input values
			System.out.println("Running net");			//run the net
			netOutput = testNet.getOutput();
			System.out.print("\n");		
			for(int i=0;i<netOutput.length;i++)			//relay output values to the user
				System.out.println(i + ": " + netOutput[i]);
			/**/
			
			
			System.out.println("\nSaving net");
			/*Save net to a file and then discard the reference.*/
			testNet.save(new FileWriter("example.net"));		//save the net
			testNet = null;						//dereference object, let the garbage collector get it


		/*OK, so this lot is pretty crap: I ought to be catching each one individually so that I can provide really meaningful error messages.
		  This, however, is just an example, and I like setting bad ones)	*/
		}catch (NearNeural.IndexOutOfBoundsException IOOBe){
			System.out.println("Nice user-friendly, relevant error message\n");
			IOOBe.printStackTrace();
			System.exit(1);
		}catch (ValueOutOfBoundsException VOOBe){
			System.out.println("Nice user-friendly, relevant error message\n");
			VOOBe.printStackTrace();
			System.exit(1);
		}catch (IOException IOe){
			System.out.println("Nice user-friendly, relevant error message\n");
			IOe.printStackTrace();
			System.exit(1);
		}catch(ItemNotFoundException INFe){
			System.out.println("Nice user-friendly, relevant error message\n");
			INFe.printStackTrace();
			System.exit(1);
		}
	}
	
	private static void run(){
		double[] netInput = {.2,.6,.8};					//we'll use the same as our training values
		NeuralNet testNet;						//create unreferenced variables
		double[] netOutput;
		try{
			System.out.println("Loading net from file");
			/*This lot must be loaded using the same thresholds and such in order to get the same value.  Of course some functions are approximations of 
			  each other anyway (sigmoid, hyptan), so in that case they are interchangable  The learning rate is part of the backpropagation method
			  (if it has one!), which is a little tricky to maintain from a UI point of view but makes sense from an algorithmic one.
			*/
			testNet = new NeuralNet(new FileReader("example.net"), 
						new HyperbolicTangentThresholdModel(1), 
						new ThreadedUnweightedBackPropagationMethod(0.002));	/*backprop is never used here so is an irrelevance*/

			/*Shows a vis window of the loaded net for immediate comparison with the other. */
			NeuralVisualisationFrame monitorWindow = new NeuralVisualisationFrame(testNet, 640, 480, "load results");
			monitorWindow.update();

			/*Sets the input values and thus precalculates.  This is very resource intensive due to precalculation (though it doesn't rank anywhere near 
			 training using most backpropagation methods.*/
			testNet.setInputValues(netInput);		

			/*Runs the net, but in reality merely extracts values from the output layer*/
			System.out.println("Running net");			
			netOutput = testNet.getOutput();

			/*Outputs the output to screen*/
			System.out.print("\n");		
			for(int i=0;i<netOutput.length;i++)		
				System.out.println(i + ": " + netOutput[i]);
		
		/*OK, so this lot is pretty crap: I ought to be catching each one individually so that I can provide really meaningful error messages.
		  This, however, is just an example, and I like setting bad ones)	*/
		}catch (NearNeural.IndexOutOfBoundsException IOOBe){
			System.out.println("Nice user-friendly, relevant error message\n");
			IOOBe.printStackTrace();
			System.exit(1);
		}catch (ValueOutOfBoundsException VOOBe){
			System.out.println("Nice user-friendly, relevant error message\n");
			VOOBe.printStackTrace();
			System.exit(1);
		}catch (java.io.FileNotFoundException FNFe){
			System.out.println("Nice user-friendly, relevant error message\n");
			FNFe.printStackTrace();
			System.exit(1);
		}catch (IOException IOe){
			System.out.println("Nice user-friendly, relevant error message\n");
			IOe.printStackTrace();
			System.exit(1);
		}
	}

}
