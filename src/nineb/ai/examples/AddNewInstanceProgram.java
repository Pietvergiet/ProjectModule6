package nineb.ai.examples;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class AddNewInstanceProgram {
	
	public static void main(String[] args) {
		FastVector atts = new FastVector();
		FastVector classVal = new FastVector();
		FastVector nullValue = null;
		classVal.addElement("A");
		classVal.addElement("B");
		atts.addElement(new Attribute("content", nullValue));
		atts.addElement(new Attribute("@@class@@", classVal));
		
		Instances dataRaw = new Instances("TestInstances", atts, 0);
		System.out.println("Before adding any instance");
		System.out.println("--------------------------");
		System.out.println(dataRaw);
		System.out.println("--------------------------");
		
		double[] instanceValue1 = new double[dataRaw.numAttributes()];
		
		instanceValue1[0] = dataRaw.attribute(0).addStringValue("This is a string!");
		instanceValue1[1] = 0;
		dataRaw.add(new Instance(1.0, instanceValue1));
		
		System.out.println("After adding a instance");
		System.out.println("--------------------------");
		System.out.println(dataRaw);
		System.out.println("--------------------------");
		
		double[] instanceValue2 = new double[dataRaw.numAttributes()];
		
		instanceValue2[0] = dataRaw.attribute(0).addStringValue("This is second string!");
		instanceValue2[1] = 1;
		
		dataRaw.add(new Instance(1.0, instanceValue2));
		
		System.out.println("After adding second instance");
		System.out.println("--------------------------");
		System.out.println(dataRaw);
		System.out.println("--------------------------");
		
	}
	
}