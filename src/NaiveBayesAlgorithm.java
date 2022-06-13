import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

class Word {
    private String key;
    private int positiveCounter;
    private int negativeCounter;

    public Word(String key) {
        this.key = key;
        this.positiveCounter = 0;
        this.negativeCounter = 0;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getPositiveCounter() {
        return positiveCounter;
    }

    public void setPositiveCounter(int positiveCounter) {
        this.positiveCounter = positiveCounter;
    }

    public int getNegativeCounter() {
        return negativeCounter;
    }

    public void setNegativeCounter(int negativeCounter) {
        this.negativeCounter = negativeCounter;
    }

    public void addReviewPositivity(boolean positivity) {
        if (positivity)
            this.positiveCounter++;
        else
            this.negativeCounter++;
    }
}

public class NaiveBayesAlgorithm {
    public static void main(String[] args) {
        AITeacher aiTeacher = new AITeacher();
        aiTeacher.run();
    }
}

class AITeacher {
    private final ArrayList<Word> words = new ArrayList<>();
    private int positiveReviewCount = 0;
    private int negativeReviewCount = 0;

    public void run() {
        String line = "";
        HashMap<String, Boolean> reviews = new HashMap<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/DataSet.csv"));
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                StringBuilder sentence = new StringBuilder();
                for (int i = 0; i < tokens.length - 1; i++) {
                    String token = tokens[i];
                    sentence.append(token);
                }
                reviews.put(sentence.toString(), tokens[tokens.length - 1].equals("1"));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        setWords(reviews);
    }

    private void setWords(HashMap<String, Boolean> reviews) {
        for (String keySentence : reviews.keySet()) {
            ArrayList<String> keys = new ArrayList<>(Arrays.asList(keySentence.split("\\s+")));
            for (int i = 0; i < keys.size(); i++) {
                String key = keys.get(i);
                if (simplifyKey(key) == null) {
                    keys.remove(key);
                    i--;
                } else
                    key = simplifyKey(key);
                Word word;

                if ((word = doesWordsContain(key)) != null) {
                    word.addReviewPositivity(reviews.get(keySentence));
                }

            }
            if (reviews.get(keySentence))
                positiveReviewCount++;
            else negativeReviewCount++;

            System.out.println(positiveReviewCount + " , " + negativeReviewCount);
        }
    }

    private Word doesWordsContain(String key) {
        for (Word word : words) {
            if (word.getKey().equals(key))
                return word;
        }
        return null;
    }

    private String simplifyKey(String key) {
        int startIndex = 0;
        int stopIndex = key.length();
        for (int i = 0; i < key.length(); i++) {
            if (key.charAt(i) == '!' || key.charAt(i) == ' ' || key.charAt(i) == '.' || key.charAt(i) == ',' || key.charAt(i) == '(' || key.charAt(i) == ')' || key.charAt(i) == '/' || key.charAt(i) == '\\') {
                startIndex = i;
            } else break;
        }
        for (int i = key.length() - 1; i > -1; i--) {
            if (key.charAt(i) == '!' || key.charAt(i) == ' ' || key.charAt(i) == '.' || key.charAt(i) == '\"' || key.charAt(i) == ',' || key.charAt(i) == '(' || key.charAt(i) == ')' || key.charAt(i) == '/' || key.charAt(i) == '\\') {
                stopIndex = i;
            } else break;
        }
        if (startIndex >= stopIndex) {
            return null;
        }
        return key.substring(startIndex, stopIndex);
    }
}


