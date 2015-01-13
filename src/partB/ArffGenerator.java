package partB;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.TextDirectoryLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Standardize;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class ArffGenerator {
	public static void main(String[] args) throws Exception {
	    TextDirectoryLoader loader = new TextDirectoryLoader();
	    
	    String dataTrain = "data/blogs/blogstrain";
	    loader.setDirectory(new File(dataTrain));
	    Instances dataRaw = loader.getDataSet();
	    printInstances(dataTrain+"_raw.arff", dataRaw);
	    
	    String dataTest = "data/blogs/blogstest";
	    loader.setDirectory(new File(dataTest));
	    Instances dataTRaw = loader.getDataSet();
	    printInstances(dataTest+"_raw.arff", dataTRaw);
	    
	    StringToWordVector filterStwv = new StringToWordVector();
	    
	    //Without batch filtering
	    filterStwv.setInputFormat(dataRaw);
	    Instances dataTrainF = Filter.useFilter(dataRaw, filterStwv);
	    printInstances(dataTrain+"_filtered.arff", dataTrainF);
	    

	    filterStwv.setInputFormat(dataTRaw);
	    Instances dataTrainTF = Filter.useFilter(dataTRaw, filterStwv);
	    printInstances(dataTest+"_filtered.arff", dataTrainTF);
		
	    //With batch filtering
		Instances trainComp = dataRaw;   
		Instances testComp = dataTRaw;    
		filterStwv.setInputFormat(trainComp);  // initializing the filter once with training set
		Instances newTrain = Filter.useFilter(trainComp, filterStwv);  // configures the Filter based on train instances and returns filtered instances
		Instances newTest = Filter.useFilter(testComp, filterStwv);    // create new test set
		printInstances(dataTrain+"_comp.arff", newTrain);
		printInstances(dataTest + "_comp.arff", newTest);
	  }
	  
	  
	  public static void printInstances(String filename, Instances data){
		try{
			PrintWriter pw = new PrintWriter(new FileOutputStream(filename,false),true);
			pw.println(""+data);
			pw.close();
		}catch(IOException exc){
			System.out.println("could not open output file");	
		}
		
	  }
}
