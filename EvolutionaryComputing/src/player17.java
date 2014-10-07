//function 1 score:  	
//function 2 score:		
//function 3 score:		

import org.vu.contest.ContestSubmission;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.vu.contest.ContestEvaluation;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Properties;

public class player17 implements ContestSubmission 
{
	final static int POPULATION_SIZE = 20; //individuals, moet altijd deelbaar door 2 zijn
	final static int NUMBER_OF_POPULATIONS = 10; //wordt nog niet gebruikt
	final static int PARENTS_SURVIVE = 10; //number of parents that survive into the next generation
	final static int NUMBER_OF_MUTATIONS = 1;
	final static boolean ONLY_MUTANTS = true; //wel of niet alleen mutanten als kinderen toeveogen aan nieuwe gen
	final static int RANDOM_MUTATION_CHANCE = 5; //procent kans dat een gen totaal random muteert
	final static int RANDOMS_ADDED = 11; //aantal individuen dat random wordt vervangen bij te lage variance 
	final static double VARIANCE_TRESHOLD = 5d; //waarde van variance in fitness waarbij individuen worden vervangen door randoms
	
	ArrayList<Individual> population;
	private Random rnd_;
	private ContestEvaluation evaluation_;
	private int evaluations_limit_;
	JFreeChart chart;
	XYSeriesCollection dataset;
	int column;
	
	
	public player17()
	{
		rnd_ = new Random();
		population = new ArrayList<Individual>();
		dataset = new XYSeriesCollection();
		column = 0;
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
			Iterator<Individual> iterator = population.iterator();
			XYSeries series = new XYSeries(""+column);
			while(iterator.hasNext())
			{
				double fitness = iterator.next().fitness.doubleValue();
				series.add(column, fitness);
			}
			column++;
			dataset.addSeries(series);
			System.out.println("best fitness: " + population.get(0).fitness + " worst fitness:  " + population.get(19).fitness + " evals: " + evals + " individual vatriance: " + computeIndividualVariance());
			if( computeIndividualVariance() < VARIANCE_TRESHOLD)
			{
				addRandomsToPopulation();
			}
			evals++;
		}

		//maak een plot op scherm, dit uitzetten bij inleveren
		chart = ChartFactory.createScatterPlot(
	            "Plot 1",       // chart title
	            "Evals",                    // domain axis label
	            "Fitness",                   // range axis label
	            dataset,                   // data
	            PlotOrientation.VERTICAL,  // orientation
	            false,                      // include legend
	            true,                      // tooltips
	            false                      // urls
	        );
		chart.setBackgroundPaint(Color.white);
		ChartFrame frame = new ChartFrame("First", chart);
        frame.pack();
        frame.setVisible(true);
	}
	
	private void addRandomsToPopulation() 
	{
		int initSize = population.size();
		Random rand = new Random();
		while (population.size() >= initSize-RANDOMS_ADDED)
		{
			int removePos = rand.nextInt(population.size());
			population.remove(removePos);
		}
		while (population.size() < initSize)
		{
			Individual ind = new Individual();
			ind.initializeRandom();
			ind.fitness = (Double)evaluation_.evaluate(ind.genome);
			addToPopulationSorted(ind, population);
		}
	}

	double computeFitnessVariance()
	{
		double output = 0;
		double numberOfIndividuals = 0;
		Iterator<Individual> iterator = population.iterator();
		while(iterator.hasNext())
		{
			output += iterator.next().fitness;
			numberOfIndividuals++;
		}
		double mean = output/numberOfIndividuals; //get mean
		output = 0;
		iterator = population.iterator();
		while(iterator.hasNext())
		{
			double fitness = iterator.next().fitness;
			output += (mean-fitness) * (mean-fitness);
		}
		return output/numberOfIndividuals;
	}
	
	double computeIndividualVariance() //todo
	{
		double[] meanIndividual = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		double[] temp =  {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		double numberOfIndividuals = 0;
		Iterator<Individual> iterator = population.iterator();
		while(iterator.hasNext())
		{
			Individual ind = iterator.next();
			for(int i = 0; i < 10; i++)
			{
				meanIndividual[i] += ind.genome[i];
			}
			numberOfIndividuals++;
		}
		for(int i = 0; i < 10; i++) //get mean
		{
			meanIndividual[i] /= numberOfIndividuals;
		}
		
		iterator = population.iterator();
		while(iterator.hasNext())
		{
			Individual ind = iterator.next();
			for(int i = 0; i < 10; i++)
			{
				temp[i] += (meanIndividual[i]-ind.genome[i]) * (meanIndividual[i]-ind.genome[i]);
			}
		}
		for(int i = 0; i < 10; i++)
		{
			temp[i] /= numberOfIndividuals;
		}
		double output = 0;
		for(int i = 0; i < 10; i++)
		{
			output += temp[i];
		}		
		return output;
	}
	
	void makeNewPopulation()
	{
		Random rand = new Random();
		ArrayList<Individual> tempPopulation1 = new ArrayList<Individual>();
		ArrayList<Individual> tempPopulation2 = new ArrayList<Individual>();
		//split pop in 2
		while(tempPopulation1.size()<POPULATION_SIZE/2)
		{
			tempPopulation1.add(population.remove(rand.nextInt(population.size())));
		}
		while(tempPopulation2.size()<POPULATION_SIZE/2)
		{
			tempPopulation2.add(population.remove(rand.nextInt(population.size())));
		}
		//BATTLE!
		while(tempPopulation1.size() > 0) 
		{
			Individual parent1 = tempPopulation1.remove(rand.nextInt(tempPopulation1.size()));
			Individual parent2 = tempPopulation2.remove(rand.nextInt(tempPopulation2.size()));
			if (parent1.fitness > parent2.fitness)
			{
				addToPopulationSorted(parent1, population);
			}
			else
			{
				addToPopulationSorted(parent2, population);
			}
		}
		while(population.size() > PARENTS_SURVIVE)
		{
			population.remove(population.size()-1);
		}
		while(population.size() < POPULATION_SIZE)
		{			
			Individual parent1 = population.get(rand.nextInt(PARENTS_SURVIVE));
			Individual parent2 = population.get(rand.nextInt(PARENTS_SURVIVE));
			Individual child = crossoverParents(parent1, parent2);
			if(!ONLY_MUTANTS)
			{
				child.fitness = (Double)evaluation_.evaluate(child.genome);
				addToPopulationSorted(child, population);
			}
			Individual mutant = child.clone();
			mutateChild(mutant);
			mutant.fitness = (Double)evaluation_.evaluate(mutant.genome);
			addToPopulationSorted(mutant, population);
		}
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
			//small change of totaly random mutation
			if(rand.nextInt(100) < RANDOM_MUTATION_CHANCE)
			{
				child.genome[mutatePosition[i]] = ((rand.nextDouble() * 10d) - 5d);
			}
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

