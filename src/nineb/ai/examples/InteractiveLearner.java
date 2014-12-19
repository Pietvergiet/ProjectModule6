package nineb.ai.examples;

import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * This class shows how to classify new data using the weka api
 */

public class InteractiveLearner {
	
	public static void main(String[] args) {
		
// We start creating some labeled data 
		FastVector atts = new FastVector();
		FastVector classVal = new FastVector();
		classVal.addElement("C1");
		classVal.addElement("C2");
		atts.addElement(new Attribute("a"));
		atts.addElement(new Attribute("b"));
		atts.addElement(new Attribute("c"));
		atts.addElement(new Attribute("d"));
		atts.addElement(new Attribute("@@class@@", classVal));
		
		Instances dataRaw = new Instances("TestInstances", atts, 0);
		dataRaw.setClassIndex(dataRaw.numAttributes() - 1);
		dataRaw.add(new Instance(1.0, new double[] { 3, 0, 1, 0, 0 }));
		
		dataRaw.add(new Instance(1.0, new double[] { 2, 1, 1, 0, 0 }));
		
		dataRaw.add(new Instance(1.0, new double[] { 2, 0, 2, 0, 0 }));
		
		dataRaw.add(new Instance(1.0, new double[] { 1, 3, 0, 0, 1 }));
		
		dataRaw.add(new Instance(1.0, new double[] { 0, 3, 1, 0, 1 }));
		
		dataRaw.add(new Instance(1.0, new double[] { 0, 2, 1, 1, 1 }));
		
// Then we build up the classifier:
		J48 ibk = new J48();
		try {
			ibk.buildClassifier(dataRaw);
			
			// create a new Instance
			double[] values = new double[] { 3, 1, 0, 0, -1 };
			Instance newInst = new Instance(1.0, values);
			// Create a new Instances object similar to the dataRaw, 
			// add your unlabeled instance to it, 
			// set class index, 
			// then try classifying it, e.g.:
			Instances dataUnlabeled = new Instances("TestInstances", atts, 0);
			dataUnlabeled.add(newInst);
			dataUnlabeled.setClassIndex(dataUnlabeled.numAttributes() - 1);
			double classif = ibk.classifyInstance(dataUnlabeled.firstInstance());
			System.out.println(classif);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}