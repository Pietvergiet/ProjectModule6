package nineb.ai;

import java.util.Arrays;
import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class Learner {
	private Instances instances;
	private String[] classes;
	private FastVector attributes;
	private boolean hasData;
	private FilteredClassifier classifier;
	
	public Learner(String... classes) throws Exception {
		this.classes = classes;
		Arrays.sort(classes);
		
		attributes = new FastVector();
		FastVector classesVector = new FastVector();
		for (int i = 0; i < classes.length; i++) {
			classesVector.addElement(classes[i]);
		}
		
		attributes.addElement(new Attribute("content", (FastVector) null));
		attributes.addElement(new Attribute("@@class@@", classesVector));
		
		instances = new Instances("Instances", attributes, 0);
		instances.setClassIndex(1);
		
		StringToWordVector filter = new StringToWordVector();
		filter.setLowerCaseTokens(true);
		filter.setInputFormat(instances);
		
		classifier = new FilteredClassifier();
		classifier.setClassifier(new NaiveBayes());
		classifier.setFilter(filter);
	}
	
	public String getClass(String input) {
		try {
//			Classifier classifier = new NaiveBayes();
			classifier.buildClassifier(instances);
			
			Instance instance = new Instance(1.0, new double[] { instances.attribute(0).addStringValue(input), -1 });
			Instances testInstances = new Instances("", attributes, 0);
			testInstances.setClassIndex(1);
			testInstances.add(instance);
			
//			System.out.println(instances);
			
			String output = classes[(int) classifier.classifyInstance(testInstances.firstInstance())];
//			System.out.printf("getting class of %s, returning %s\n", input, output);
			
			return output;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 */
	public void classify(String input, String specifiedClass) {
//		System.out.printf("classifying %s as %s\n", input, specifiedClass);
		
		hasData = true;
		
		double[] data = new double[2];
		data[0] = instances.attribute(0).addStringValue(input);
		data[1] = Arrays.binarySearch(classes, specifiedClass);//index of the class
		instances.add(new Instance(1.0, data));
		instances.lastInstance().setValue(0, input);
	}
	
	public void eval() throws Exception {
		Evaluation evaluation = new Evaluation(instances);
		evaluation.crossValidateModel(classifier, instances, 10, new Random(1));
		System.out.println(evaluation.toSummaryString());
	}
	
	public boolean hasData() {
		return hasData;
	}
	
	public Instances getInstances() {
		return instances;
	}
}
