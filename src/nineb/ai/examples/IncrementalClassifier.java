package nineb.ai.examples;

import java.io.File;

import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

/**
 * This example trains NaiveBayes incrementally on data obtained from the
 * ArffLoader.
 *
 * usage: java -cp .;weka.jar IncrementalClassifier blogs.arff Note that in
 * blogs.arff the class attribute is the first attribute (index 0)! For a
 * Bayesian classifier the class attribute cannot be numeric
 */
public class IncrementalClassifier {
	
	/**
	 * Expects an ARFF file as first argument (class attribute is assumed to be
	 * the last attribute).
	 *
	 * @param args
	 *            the commandline arguments
	 * @throws Exception
	 *             if something goes wrong
	 */
	public static void main(String[] args) throws Exception {
		// load data
		ArffLoader loader = new ArffLoader();
		loader.setFile(new File("data/blogs_train_generated.arff"));
		Instances structure = loader.getStructure();
		//structure.setClassIndex(structure.numAttributes() - 1);
		// in blogs.arff the class attribute is the first attribute; not the last one!
		structure.setClassIndex(0);
		// train NaiveBayes
		NaiveBayesUpdateable nb = new NaiveBayesUpdateable();
		nb.buildClassifier(structure);
		Instance current;
		while ((current = loader.getNextInstance(structure)) != null)
			nb.updateClassifier(current);
		
		// output generated model
		System.out.println(nb);
	}
}
