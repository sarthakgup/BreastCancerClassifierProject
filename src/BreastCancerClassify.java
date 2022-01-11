/**
 * Name: Sarthak Gupta
 * Mrs. Kankelborg
 * Period: 2
 * Project 0 Breast Cancer Classifier
 * Revision History:
 *    09/09/19 - Opened program. Filling out comments and starting with first 3 methods
 *    09/10/19 - Finished first method
 *    09/13/19 - Methods completed and rounded to 2 decimals for solution but correct %accuracy not displaying
 *    10/10/19 - Finishing up resubmission request
 * 
 * BreastCancerClassify contains the core implementation of the 
 * kNearestNeighbors algorithm to classify cell clumps as malignant
 * or benign. 
 * 
 * Work on the functions in the following order:
 *  0) author
 * 	1) calculateDistance - once you finish this, you should see a
 * 	   graph of distances appear!
 * 	2) getAllDistances
 * 	3) findKClosestEntries
 * 	4) classify
 *  5) kNearestNeighbors (use your helpers correctly!)
 *  6) getAccuracy
 */
public class BreastCancerClassify
{
	static int K = 5;
	static int BENIGN = 2;
	static int MALIGNANT = 4;
	
	/**
	 * calculateDistance computes the distance between the two data
	 * parameters. The distance is found by taking the difference in each 
	 * "coordinate", squaring it, adding all of those, and then taking the 
	 * square root of the result. 
	 * 
	 * Remember to exclude the patient ID and the tumor classification
	 * 
	 * For example: 
	 * [12345, 6, 4, 4, MALIGNANT]
	 * [22344, 2, 8, 3, BENIGN]
	 * 
	 * distance = sqrt((6-2)^2 + (4-8)^2 + (4-3)^2)
	 */
	public static double calculateDistance(int[] first, int[] second)
	{
		//difference, square, sum, sqrt
		int counter = first.length - 2;
		double distance = 0.0;
		
		for(int i = 1; i <= counter; i++) //Starts at 2nd value to exclude id
		{
			distance += Math.pow(first[i]-second[i], 2.0);
		}
		
		//completing distance formula by square rooting
		distance = Math.sqrt(distance);

		//returning the double distance value
		return distance;
	}
	
	/**
	 * getAllDistances creates an array of doubles with the distances
	 * to each training instance. The double[] returned should have the 
	 * same number of instances as trainData.
	 */
	public static double[] getAllDistances(int[][] trainData, int[] testInstance)
	{
		//create the array to store distances which will eventually be returned
		double[] allDistances = new double[trainData.length];
		
		//finding and filling distances to array
		for(int i = 0; i < trainData.length; i++)
		{
			for(int x = 0; x < trainData[i].length; x++)
			{
				allDistances[i] = calculateDistance(trainData[i], testInstance);
			}
		}
		
		//returning the array containing distances
		return allDistances;
	}
	
	/**
	 * findKClosestEntries finds and returns the indexes of the 
	 * K closest distances in allDistances. Return an array of size K, 
	 * that is filled with the indexes of the closest distances (not
	 * the distances themselves). 
	 * 
	 * Be careful! This function can be tricky.
	 */
	public static int[] findKClosestEntries(double[] allDistances)
	{
		//create array of size K (will return this)
		int[] kClosestIndexes = new int[K]; //5 slots because k is 5

		for(int x = 0; x < K; x++)
		{
			int temp = 0;
			
			if(x == 0)
			{
				//Finding the first smallest index
				for(int i = 0; i < allDistances.length; i++)
				{
					if(allDistances[i] < allDistances[temp])
					{
						temp = i;
					}
				}
			}
			else
			{
				//Finding the other 4 smallest indices
				int prevMinIndex = kClosestIndexes[x-1]; //stores the previous smallest index
				double prevMinVal = allDistances[prevMinIndex]; //stores previous smallest val
				double minDiff = 0; //stores minimum diff which resets for every k array slot
				
				for(int i = 0; i < allDistances.length; i++)
				{
					//finding diff
					double diff = allDistances[i] - prevMinVal;
					
					//using diff to make sure it is the next smallest and isnt already in the array
					if(diff > 0)
					{
						if((minDiff == 0) || (diff < minDiff))
						{
							//assign minDiff and temp
							minDiff = diff;
							temp = i;
						}
					}
				}	
			}
			
			kClosestIndexes[x] = temp;		
		}
		
		//return my kClosestIndexes int[]
		return kClosestIndexes;	
	}
	
	/**
	 * classify makes a decision as to whether an instance of testing 
	 * data is BENIGN or MALIGNANT. The function makes this decision based
	 * on the K closest train data instances (whose indexes are stored in 
	 * kClosestIndexes). If more than half of the closest instances are 
	 * malignant, classify the growth as malignant. Otherwise classify
	 * as benign.
	 * 
	 * Return one of the global integer constants defined in this function. 
	 */
	public static int classify(int[][] trainData, int[] kClosestIndexes)
	{
		int ben = 0; //counter for num of benign instances
		int mal = 0; //counter for num of malignant instances
		
		//loop through all values of kClosestIndexes
		for(int i = 0; i < kClosestIndexes.length; i++)
		{
			if(trainData[kClosestIndexes[i]][trainData[i].length-1] == BENIGN) //if benign
			{
				//if benign, increment one to the benign counter
				ben++;
			}
			else
			{
				//if not benign, increment one to malignant counter
				mal++;
			}
		}
		
		if(mal > ((ben+mal)/2)) //if malignant
		{
			//return the global variable for malignant
			return MALIGNANT;
		}
		else //if benign
		{
			//return the global variable for benign
			return BENIGN;
		}
	}
	
	/**
	 * kNearestNeighbors classifies all the data instances in testData as 
	 * BENIGN or MALIGNANT using the helper functions you wrote and the kNN 
	 * algorithm.
	 * 
	 * For each instance of your test data, use your helper methods to find the
	 * K closest points, and classify your result based on that!
	 * @param trainData: all training instances
	 * @param testData: all testing instances
	 * @return: int array of classifications (BENIGN or MALIGNANT)
	 */
	public static int[] kNearestNeighbors(int[][] trainData, int[][] testData)
	{
		//create new array of length testData.length which i will return
		int[] myResults = new int[testData.length];
		
		//for loop to run classify() for every value in myResults
		for(int i = 0; i < myResults.length; i++)
		{
			myResults[i] = classify(trainData, findKClosestEntries
									(getAllDistances(trainData, testData[0])));
		}

		//return the int[] with my results
		return myResults;
	}

	/**
	 * printAccuracy returns a String representing the classification accuracy.
	 *
	 * The output String should be rounded to two decimal places followed by the % symbol.
	 * For example, if 4 out of 5 outcomes were correctly predicted, the returned String should be: "80.00%"
	 * For example, if 3 out of 9 outcomes were correctly predicted, the returned String should be: "33.33%"
	 * For example, if 6 out of 9 outcomes were correctly predicted, the returned String should be: "66.67%"
	 * Look up Java's String Formatter to learn how to round a double to two-decimal places.
	 *
	 * This method should work for any data set, given that the classification outcome is always listed
	 * in the last column of the data set.
	 * @param: myResults: The predicted classifications produced by your KNN model
	 * @param: testData: The original data that contains the true classifications for the test data.
	 */
	public static String getAccuracy(int[] myResults, int[][] testData)
	{
		//variables
		int correct = 0; //number values matching between myResults and testData
		double quotient = 0; //100 * the quotient
		
		//find total number of correct
		for(int i = 0; i < myResults.length; i++)
		{
			//is myResults value matching the constant in testData
			if(myResults[i] == testData[i][testData[i].length-1])
			{
				//increment correct if a correct val found
				correct++;
			}
		}

		quotient = 100.0*((1.0)*correct/myResults.length);
		
		//format output
		String answerPercent = String.format("%.2f", quotient); //rounded to 2 decimals
		answerPercent += "%";
		
		//return answer
		return answerPercent;
	}

	//DO NOT MODIFY THE MAIN METHOD
	public static void main(String[] args)
	{

		int[][] trainData = InputHandler.populateData("./datasets/train_data.csv");
		int[][] testData = InputHandler.populateData("./datasets/test_data.csv");
		
		//Display the distances between instances of the train data. 
		//Points in the upper left corner (both benign) or in the bottom
		//right (both malignant) should be darker. 
		Grapher.createGraph(trainData);

		int[] myResults = kNearestNeighbors(trainData, testData);

		System.out.println("Model Accuracy: " + getAccuracy(myResults, testData));
	}
}