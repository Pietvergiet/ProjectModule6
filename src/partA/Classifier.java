package partA;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

public class Classifier {
	//returns the words in this file, sanitized in a arraylist, returns an empty list if the file was not found
	public static ArrayList<String> tokenize(File file) {
		ArrayList<String> result = new ArrayList<String>();
		try {
			Scanner s = new Scanner(file);
			s.useDelimiter("[^A-Za-z]");//match anything thats not a letter
//			s.useDelimiter(",|\\.|\\?|\\!| |\\*|\r\n|\\:|\\\t|\\-|\\(|\\)|\\@|\\\n|\\\"");
			while (s.hasNext()) {
				String token = s.next();
				if (token.length() != 0) result.add(token.toLowerCase());
			}
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static HashMap<String, Integer> calculateCountSet(File dir) {
		HashMap<String, Integer> countMap = new HashMap<String, Integer>();
		int n = 0;
		for (File f : dir.listFiles()) {
			for (String s : tokenize(f)) {
				n++;
				if (!countMap.containsKey(s)) {
					countMap.put(s, 1);
				} else {
					countMap.put(s, countMap.get(s) + 1);
				}
			}
		}
		// our sincere apologies for this solution.
		countMap.put("COUNT", n);
		return countMap;
	}
	
	//get the probability of a word, in log space
	public static float getLogProbability(String token, HashMap<String, Integer> counts, float k) {
		int n = counts.get("COUNT");
		int count = counts.containsKey(token) ? counts.get(token) : 0;
		
		int v = counts.size() - 1;//minus for to fix for COUNT key
		
		float p = (float) (count + k) / (n + k * v);
		return (float) Math.log(p);
	}
	
	//get the prob of a set of tokens, ie the blog post, in log space
	public static float getLogProbabilty(ArrayList<String> tokens, HashMap<String, Integer> counts, float k) {
		float total = 0f;
		
		for (String token : tokens) {
			total += getLogProbability(token, counts, k);
		}
		
		return total;
	}
	
	public static void printAccuracy(float k, HashMap<String, Integer> countMale, HashMap<String, Integer> countFemale) {
		int maleN = 0, femaleN = 0;
		int maleCorrect = 0, femaleCorrect = 0;
		
//		File testFilesM = new File("data/blogs/blogstrain/M");
//		File testFilesF = new File("data/blogs/blogstrain/F");
		File testFilesM = new File("data/blogs/blogstest/M");
		File testFilesF = new File("data/blogs/blogstest/F");
		
		for (File f : testFilesM.listFiles()) {
			maleN++;
			ArrayList<String> tokens = tokenize(f);
			float maleLogProb = getLogProbabilty(tokens, countMale, k);
			float femaleLogProb = getLogProbabilty(tokens, countFemale, k);
			if (maleLogProb > femaleLogProb) {
				maleCorrect++;
			}
			
		}
		
		for (File f : testFilesF.listFiles()) {
			femaleN++;
			ArrayList<String> tokens = tokenize(f);
			float maleLogProb = getLogProbabilty(tokens, countMale, k);
			float femaleLogProb = getLogProbabilty(tokens, countFemale, k);
			if (femaleLogProb > maleLogProb) {
				femaleCorrect++;
			}
		}
		
//		printData(k, maleN, maleCorrect, "Male");
//		printData(k, femaleN, femaleCorrect, "Female");
		printData(k, maleN + femaleN, maleCorrect + femaleCorrect, "Both");
	}
	
	private static void printData(float k, int n, int correct, String gender) {
		System.out.printf("%10s|%10s|%10s|%10s|\n", String.format("%.2f", k),
				String.format("%.2f", ((float) correct / n) * 100), String.format("%d/%d", correct, n), gender);
	}
	
	//prints the 40 most common words
	public static void printCountData(HashMap<String, Integer> countMap) {
		ArrayList<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(countMap.entrySet());
		Collections.sort(list, new Comparator<Entry<String, Integer>>() {
			@Override
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				return -Integer.compare(o1.getValue(), o2.getValue());
			}
		});
		
		for (int i = 0; i < 40 && i < list.size(); i++) {
			System.out.printf("%s : %d\n", list.get(i).getKey(), list.get(i).getValue());
		}
	}
	
	//filters the data, only lets entries with a frequency between the specified values in the map
	public static void filterCountData(HashMap<String, Integer> countMap, int minimalCount, int maximumCount) {
		ArrayList<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(countMap.entrySet());
		countMap.clear();
		
		for (Entry<String, Integer> entry : list) {
			if ((entry.getValue() >= minimalCount && entry.getValue() <= maximumCount)
					|| entry.getKey().equals("COUNT")) {
				countMap.put(entry.getKey(), entry.getValue());
			}
		}
	}
	
	public static void main(String[] args) {
		System.out.print("training data... ");
		HashMap<String, Integer> countMale = calculateCountSet(new File("data/blogs/blogstrain/M"));
		HashMap<String, Integer> countFemale = calculateCountSet(new File("data/blogs/blogstrain/F"));
		System.out.println("done");
		
		int min = 0, max = 100000;
		System.out.printf("filtering words with a frequency lower than %d, or higher than %d\n", min, max);
		filterCountData(countMale, min, max);
		filterCountData(countFemale, min, max);
		
//		System.out.println(countMale.get("COUNT"));
//		System.out.println(countFemale.get("COUNT"));
		
//		printCountData(countMale);
//		printCountData(countFemale);
		
		System.out.printf("%10s|%10s|%10s|%10s|\n", "K-factor", "Accuracy", "N correct", "M/F");
		System.out.println("----------+----------+----------+----------+");
		
		for (int i = 1; i <= 10; i++) {
			printAccuracy(i, countMale, countFemale);
		}
		System.out.println("----------+----------+----------+----------+");
		for (float i = 1f; i <= 3.1f; i += 0.2f) {
			printAccuracy(i, countMale, countFemale);
		}
	}
}
