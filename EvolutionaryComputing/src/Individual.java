import java.util.Random;

public class Individual 
{
	double[] genome;
	Double fitness;
	
	Individual()
	{
		genome = new double[10];
		fitness = Double.MIN_VALUE;
	}
	
	public Individual clone()
	{
		Individual output = new Individual();
		output.genome = new double[10];
		for(int i = 0; i < genome.length; i++)
		{
			output.genome[i] = this.genome[i];
		}
		output.fitness = this.fitness.doubleValue();
		return output;
	}
	
	public void initializeRandom()
	{
		double randDub;
		Random rand = new Random();
		double[] output = new double[10];
		for (int i = 0; i < output.length; i++)
		{
			randDub = rand.nextDouble();
			randDub *= 10d;
			randDub -= 5d;
			genome[i] = randDub;
		}		
	}
}
