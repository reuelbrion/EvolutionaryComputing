package ec;

public class ec1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		EASubmission sub = new EASubmission();
		sub.setEvaluation(new SphereEvaluation());
		sub.run();
	}

}
