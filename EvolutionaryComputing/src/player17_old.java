

import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;

import java.util.Random;
import java.util.Properties;
//test
public class player17_old implements ContestSubmission
{
	private Random rnd_;
	private ContestEvaluation evaluation_;
	private int evaluations_limit_;

	private double[] currentScore;
	
	public player17_old()
	{
		rnd_ = new Random();
		currentScore = new double[2];
	}

	public void setSeed(long seed)
	{
		// Set seed of algortihms random process
		rnd_.setSeed(seed);
	}

	public void setEvaluation(ContestEvaluation evaluation)
	{
		// Set evaluation problem used in the run
		evaluation_ = evaluation;

		// Get evaluation properties
		Properties props = evaluation.getProperties();
		evaluations_limit_ = Integer.parseInt(props.getProperty("Evaluations"));
		boolean isMultimodal = Boolean.parseBoolean(props.getProperty("Multimodal"));
		boolean hasStructure = Boolean.parseBoolean(props.getProperty("GlobalStructure"));
		boolean isSeparable = Boolean.parseBoolean(props.getProperty("Separable"));

		// Change settings(?)
		if(isMultimodal){
			// Do sth
		}else{
			// Do sth else
		}
	}

	public void run()
	{
		// Run your algorithm here

		int evals = 0;
		
		//set random values for starting array
		double[][] population = initPopulation(); 
		
		while(evals<evaluations_limit_/population.length)
		{
			// Select parents
			int[] bestParents = {0,1};
			Double[] parentFitness = {(Double)evaluation_.evaluate(population[0]), (Double)evaluation_.evaluate(population[1])};
			Double currentFitness;
			for (int i = 2; i < population.length; i++)
			{
				currentFitness = (Double)evaluation_.evaluate(population[i]);
				if(currentFitness > parentFitness[0])
				{
					if(parentFitness[0] > parentFitness[1])
					{
						bestParents[1] = i;
						parentFitness[1] = currentFitness;
					}
					else
					{
						bestParents[0] = i;
						parentFitness[0] = currentFitness;
					}
				
				}
				else if(currentFitness  > parentFitness[1])
				{
					bestParents[1] = i;
					parentFitness[1] = currentFitness;
				}
			}	
			
			//System.out.println("p1 fitness: " + parentFitness[0] + " p2 fitness:  " + parentFitness[1] + " evals: " + evals);
			currentScore[0] = parentFitness[0];
			currentScore[1] = parentFitness[1];
			population = createPopulationFromParents(population[bestParents[0]], population[bestParents[1]]);
			
			evals++;
		}
	}
	
	private double[][] createPopulationFromParents(double[] parent1, double[] parent2) 
	{
		double[][] output = new double[10][10];
		Random rand = new Random();
		int randInt1, base;
		double[] child;
		
		for (int i = 0; i < output.length; i++)
		{
			child = new double[10];
			randInt1 = rand.nextInt(6);
			base = 0;
			
			for (int j = 0; j < randInt1; j++)
			{
				child[j] = parent1[j];
				base++;
			}
			for (int h = 0; h < randInt1; h++)
			{
				child[h + base] = parent2[h + base];
			}
			
			output[i] = mutateChild(child);
		}		
		return output;
	}

	private double[] mutateChild(double[] child)
	{
		int numberOfMutations = 3;
		double currentScoreDub = currentScore[0] + currentScore[1];
		if (currentScoreDub > 6d && currentScoreDub <= 12d)
		{
			numberOfMutations = 2;
		}
		else if (currentScoreDub > 12d)
		{
			numberOfMutations = 1;
		}
		Random rand = new Random();
		int[] mutatePosition = createMutatePositionsArray(numberOfMutations);	
		for (int i = 0; i < numberOfMutations; i++)
		{
			child[mutatePosition[i]] = ((rand.nextDouble() * 10d) - 5d);
		}
		return child;
	}

	private int[] createMutatePositionsArray(int size) 
	{
		Random rand = new Random();
		int[] output = new int[size];
		output[0] = rand.nextInt(9);
		for (int i = 1; i<size; i++)
		{
			int newPosition = rand.nextInt(9);
			while(arrayContains(output, newPosition, i))
			{
				newPosition = rand.nextInt(9);
			}
			output[i] = newPosition;
		}
		return output;
	}

	private boolean arrayContains(int[] intArray, int check, int steps) 
	{
		for (int i = 0; i < steps; i++)
		{
			if (intArray[i] == check)
			{
				return true;
			}
		}
		return false;
	}

	public double[][] initPopulation()
	{
		double[][] output = new double[10][10];
		for (int i = 0; i < output.length; i++)
		{
			output[i] = makeRandomDoubleArray();
		}		
		return output;
	}
	
	public double[] makeRandomDoubleArray()
	{
		double randDub;
		Random rand = new Random();
		double[] output = new double[10];
		for (int i = 0; i < output.length; i++)
		{
			randDub = rand.nextDouble();
			randDub *= 10d;
			randDub -= 5d;
			output[i] = randDub;
		}		
		
		return output;
	}
}

