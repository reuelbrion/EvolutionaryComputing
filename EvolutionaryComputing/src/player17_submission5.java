//function 1 score:  	9.9107
//function 2 score:		0.07976
//function 3 score:		3.9751

import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;

import java.util.ArrayList;
import java.util.Random;
import java.util.Properties;
//test
public class player17_submission5 implements ContestSubmission
{
	final static int POPULATION_SIZE = 100; //individuals
	final static int PARENTS_SURVIVE = 20; //number of parents that survive into the next generation
	final static int NUMBER_OF_MUTATIONS = 1;
	final static boolean ONLY_MUTANTS = false; //wel of niet alleen mutanten als kinderen toeveogen aan nieuwe gen
	
	ArrayList<Individual> population;
	private Random rnd_;
	private ContestEvaluation evaluation_;
	private int evaluations_limit_;
	
	public player17_submission5()
	{
		rnd_ = new Random();
		population = new ArrayList<Individual>();
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
		//set random values for starting array
		int evals = 1;
		initPopulation(); 
		
		while(evals<evaluations_limit_/POPULATION_SIZE)
		{
			makeNewPopulation();
			System.out.println("best fitness: " + population.get(0).fitness + " worst fitness:  " + population.get(99).fitness + " evals: " + evals);
			evals++;
		}
	}
	
	void makeNewPopulation()
	{
		Random rand = new Random();
		ArrayList<Individual> tempPopulation = new ArrayList<Individual>();
		for(int i = 0; i < PARENTS_SURVIVE; i++)
		{
			tempPopulation.add(i, population.get(i));
		}
		while(tempPopulation.size() < POPULATION_SIZE)
		{			
			Individual parent1 = population.get(rand.nextInt(PARENTS_SURVIVE));
			Individual parent2 = population.get(rand.nextInt(PARENTS_SURVIVE));
			Individual child = crossoverParents(parent1, parent2);
			if(!ONLY_MUTANTS)
			{
				child.fitness = (Double)evaluation_.evaluate(child.genome);
				addToPopulationSorted(child, tempPopulation);
			}
			Individual mutant = child.clone();
			mutateChild(mutant);
			mutant.fitness = (Double)evaluation_.evaluate(mutant.genome);
			addToPopulationSorted(mutant, tempPopulation);
		}
		population = tempPopulation;
	}
	
	private Individual crossoverParents(Individual parent1, Individual parent2)
	{
		Random rand = new Random();
		Individual child = new Individual();
		for(int i = 0; i < parent1.genome.length; i++)
		{
			if (rand.nextBoolean())
			{
				child.genome[i] = parent1.genome[i];
			}
			else
			{
				child.genome[i] = parent2.genome[i];
			}
		}
		return child;
	}
	
	private void mutateChild(Individual child)
	{
		Random rand = new Random();
		int[] mutatePosition = createMutatePositionsArray(NUMBER_OF_MUTATIONS);	
		for (int i = 0; i < NUMBER_OF_MUTATIONS; i++)
		{
			double oldValue = child.genome[mutatePosition[i]];
			double mutation = rand.nextDouble();
			if(rand.nextBoolean())
			{
				mutation *= -1d;
			}
			mutation = oldValue + mutation;
			if(mutation > 5d)
			{
				mutation = 5d;
			}
			else if(mutation < -5d)
			{
				mutation = -5d;
			}
			child.genome[mutatePosition[i]] = mutation;
		}
	}

	private int[] createMutatePositionsArray(int size) 
	{
		Random rand = new Random();
		int[] output = new int[size];
		output[0] = rand.nextInt(10);
		for (int i = 1; i<size; i++)
		{
			int newPosition = rand.nextInt(10);
			while(arrayContains(output, newPosition, i))
			{
				newPosition = rand.nextInt(10);
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

	public void addToPopulationSorted(Individual input, ArrayList<Individual> pop)
	{
		int index = 0;
		while(index < pop.size() && population.get(index).fitness > input.fitness)
		{
			index++;
		}
		pop.add(index, input);
	}
	
	public void initPopulation()
	{
		Individual ind = new Individual();
		ind.initializeRandom();
		ind.fitness = (Double)evaluation_.evaluate(ind.genome);
		population.add(ind);
		while(population.size() < POPULATION_SIZE)
		{
			ind = new Individual();
			ind.initializeRandom();
			ind.fitness = (Double)evaluation_.evaluate(ind.genome);
			addToPopulationSorted(ind, population);
		}
	}
}

