import java.util.HashMap;

// An object from this class will represent an email with its words and their counts, or an average email
public class MailVector {
  private boolean label;
  private HashMap<String, Double> emailWords = new HashMap<>();
  private boolean isSpam; // if the email is spam or not

  public MailVector(HashMap<String, Double> words, boolean label) {
    emailWords.putAll(words);
    this.label = label;
    // add the other two to the constructor
  }

  public MailVector(boolean label) {
    this.label = label;
  }

  public void setLabel(boolean emailName) {
    label = emailName;
  }

  public boolean getLabel() {
    return label;
  }

  public void setSpam(boolean spam) {
    isSpam = spam;
  }

  public boolean getIsSpam() {
    return isSpam;
  }

  public void setEmailWords(HashMap<String, Double> emailWords) {
    this.emailWords.putAll(emailWords);
  }
  public HashMap<String, Double> getEmailWords() {
    return emailWords;
  }
}
