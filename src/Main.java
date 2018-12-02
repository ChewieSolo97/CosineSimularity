import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) {

    ArrayList<HashMap<Integer, Double>> similarities = new ArrayList<>();
    ArrayList<MailVector> trainVectors = new ArrayList<>();
    ArrayList<MailVector> testVectors = new ArrayList<>();
    HashMap<String, Double> trainWords = new HashMap<>();
    String testPath = "/Users/Mitchell/Downloads/test";
    String trainPath = "/Users/Mitchell/Downloads/train";

    setUpWords(trainWords);
    setUpVectors(trainVectors, trainWords, trainPath);
    setUpVectors(testVectors, trainWords, testPath);
    cosineSimilarity(testVectors, trainVectors, similarities);

  }

  public static void setUpWords(HashMap<String, Double> trainWords) {
    File dir = new File("/Users/Mitchell/Downloads/train");
    Scanner s; //this is a test
    try {
      for (File file : dir.listFiles()) {
        s = new Scanner(file);
        while (s.hasNext()) {
          trainWords.put(s.next(), 0.0);
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public static void setUpVectors(ArrayList<MailVector> vectors, HashMap<String, Double> trainWords,
    String path) {
    HashMap<String, Double> emailWords = new HashMap<>();
    File dir = new File(path); // make this a param
    Scanner s;
    String next;
    try {
      for (File file : dir.listFiles()) {
        emailWords.putAll(trainWords);
        s = new Scanner(file);
        while (s.hasNext()) {
          next = s.next();
          if (emailWords.containsKey(next)) {
            emailWords.put(next, emailWords.get(next) + 1.0);
          }
        }
        if (file.getName().contains("spm")) {
          vectors.add(new MailVector(emailWords, true));
        } else {
          vectors.add(new MailVector(emailWords, false));
        }
        emailWords.clear();
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public static void cosineSimilarity(ArrayList<MailVector> trainVectors, ArrayList<MailVector> testVectors,
    ArrayList<HashMap<Integer, Double>> similarities) {

    // these were replaced by parameters

    // these will represent the emails' words w/ counts from each email in the testing and training set
    //ArrayList<HashMap<String, Double>> trainEmails = new ArrayList<>();
    //ArrayList<HashMap<String, Double>> testEmails = new ArrayList<>();
    // this will hold all of the similarities for an email, and keep them
    //ArrayList<ArrayList<HashMap<Integer, Double>>> similarities = new ArrayList<>();

    double cosineSimilarity = 0;
    double numerator = 0;
    double denominatorA = 0;
    double denominatorB = 0;
    // iterate over every training email to compare against one test email, don't like the N^3, will try to fix
    for (int i = 0; i < testVectors.size(); i++) {
      for (int j = 0; j < trainVectors.size(); j++) {
        // need to clear out these each time
        numerator = 0;
        denominatorA = 0;
        denominatorB = 0;
        //cosineSimilarity = 0;
        // you iterate over every word in the training set, which should be the keys for each email
        for (String word : trainVectors.get(0).getEmailWords().keySet()) {
          numerator += trainVectors.get(j).getEmailWords().get(word) * testVectors.get(i).getEmailWords().get(word);
          denominatorA += trainVectors.get(j).getEmailWords().get(word) * trainVectors.get(j).getEmailWords().get(word);
          denominatorB += testVectors.get(i).getEmailWords().get(word) * testVectors.get(i).getEmailWords().get(word);
        }
        cosineSimilarity = numerator / ((Math.sqrt(denominatorA)) * ((Math.sqrt(denominatorB))));
        // no idea why, but it complained about the double
        similarities.add(new HashMap<>(j, (float) cosineSimilarity));
      }
    }
  }

  public static void classifyEmails(ArrayList<MailVector> testVectors, ArrayList<MailVector> trainVectors,
    ArrayList<HashMap<Integer, Double>> similarities, int k) {

    for (MailVector email : testVectors) {

    }
  }
}
