package partB;

import java.io.File;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class NaiveBayesianClassifier {
	public static void main(String[] args) throws Exception {
		// load data
		ArffLoader trainLoader = new ArffLoader();
		ArffLoader testLoader = new ArffLoader();
		trainLoader.setFile(new File("data/blogs/blogstrain_comp.arff"));
		testLoader.setFile(new File("data/blogs/blogstest_comp.arff"));
		Instances trainStructure = trainLoader.getStructure();
		Instances testStructure = testLoader.getDataSet();
		testStructure.setClassIndex(0);
		trainStructure.setClassIndex(0);
		// train NaiveBayes
		NaiveBayes nb = new NaiveBayes();
		nb.buildClassifier(trainStructure);
		Instance current;
		while ((current = trainLoader.getNextInstance(trainStructure)) != null){
			nb.updateClassifier(current);
		}
		Evaluation eval = new Evaluation(trainStructure);
		eval.evaluateModel(nb, testStructure);
		System.out.println(eval.toSummaryString("\nResults\n======\n", true));
		// output generated model
		//System.out.println(nb);
	}
}
