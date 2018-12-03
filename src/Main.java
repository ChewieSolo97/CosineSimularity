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
    //ArrayList<Integer> topIndexes = findLargestSimilarities(5, similarities.get(0));
//    for (Integer num : topIndexes) {
//      System.out.println("the top sim. are: " + num);
//    }
    classifyEmails(testVectors, trainVectors, similarities, 1); // using 5 for now, change to user input
    checkAccuracy(testVectors);
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
    for (int i = 0; i < trainVectors.size(); i++) {
      similarities.add(new HashMap<>());
      for (int j = 0; j < testVectors.size(); j++) {
        // need to clear out these each time
        numerator = 0;
        denominatorA = 0;
        denominatorB = 0;
        //cosineSimilarity = 0;
        // you iterate over every word in the training set, which should be the keys for each email
        for (String word : trainVectors.get(0).getEmailWords().keySet()) {
          numerator += trainVectors.get(i).getEmailWords().get(word) * testVectors.get(j).getEmailWords().get(word);
          denominatorA += trainVectors.get(i).getEmailWords().get(word) * trainVectors.get(i).getEmailWords().get(word);
          denominatorB += testVectors.get(j).getEmailWords().get(word) * testVectors.get(j).getEmailWords().get(word);
        }
        cosineSimilarity = numerator / ((Math.sqrt(denominatorA)) * ((Math.sqrt(denominatorB))));
        // no idea why, but it complained about the double


        //System.out.printf("%f, %f, %f \n", numerator, denominatorA, denominatorB);

        similarities.get(i).put(j, cosineSimilarity);
        //System.out.println(similarities.get(i).get(j));
      }
    }
  }

  public static void classifyEmails(ArrayList<MailVector> testVectors, ArrayList<MailVector> trainVectors,
    ArrayList<HashMap<Integer, Double>> similarities, int k) {

    ArrayList<Integer> emailSim = new ArrayList<>();
    int i = 0;
    for (HashMap<Integer, Double> email : similarities) {
      emailSim.addAll(findLargestSimilarities(k, email));
      testVectors.get(i).setSpam(pickTestLabel(emailSim, trainVectors));
      emailSim.clear();
      i++;
    }

  }

  public static ArrayList<Integer> findLargestSimilarities(int k, HashMap<Integer, Double> similarities) {

    ArrayList<Integer> topIndexes = new ArrayList<>();
    double top = 0;
    int topIndex = -1;

    for (int i = 0; i < k; i++) {
      for (int j = 0; j < similarities.size(); j++) {
        if (similarities.get(j) > top && !topIndexes.contains(j)) {
          top = similarities.get(j);
          topIndex = j;
        }
      }
      top = 0;
      topIndexes.add(topIndex);
    }

    return topIndexes;
  }

  public static boolean pickTestLabel(ArrayList<Integer> emailSim, ArrayList<MailVector> trainVectors) {

    int spam = 0;
    int notSpam = 0;
    for (Integer index : emailSim) {
      if (trainVectors.get(index).getLabel()) {
        spam++;
      } else {
        notSpam++;
      }
    }
    return (spam > notSpam ? true : false);
  }

  public static void checkAccuracy(ArrayList<MailVector> testVectors) {

    double correctClassification = 0;
    for (MailVector email : testVectors) {
      if (email.getLabel() == email.getIsSpam()) {
        correctClassification++;
      }
    }
    System.out.println("The accuracy is : " + correctClassification / testVectors.size());
  }
}
