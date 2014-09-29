

import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;

import java.util.Random;
import java.util.Properties;
//test
public class player17_submission1 implements ContestSubmission
{
	private Random rnd_;
	private ContestEvaluation evaluation_;
	private int evaluations_limit_;

	public player17_submission1()
	{
		rnd_ = new Random();
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
		
		while(evals<evaluations_limit_/10)
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
			// Apply variation operators and get children
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
		Random rand = new Random();
		int mutatePosition = rand.nextInt(10);
		
		child[mutatePosition] = ((rand.nextDouble() * 10d) - 5d);
		return child;
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
