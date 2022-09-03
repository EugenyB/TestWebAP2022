
package ua.mk.berkut.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Eugeny
 */
public class Question implements Serializable {
    private String question;
    private String picture;
    private String addition;
    private String[] answers;
    private int correct;

    public Question(String question, String picture, String addition, String[] answers, int correct) {
        this.question = question;
        this.picture = picture;
        this.addition = addition;
        this.answers = answers;
        this.correct = correct;
    }


    public int getCorrect() {
        return correct;
    }

    public String getPicture() {
        return picture;
    }

    public String[] getAnswers() {
        return answers;
    }

    public String getAddition() {
        return addition;
    }

    public void setAddition(String addition) {
        this.addition = addition;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }


    public void shuffle() {
        answers[correct] = "+" + answers[correct];
        List<String> ans = new ArrayList<>(Arrays.asList(answers));
        Collections.shuffle(ans);
        for (int i=0; i < answers.length; i++) {
            String s = ans.get(i);
            if (s.charAt(0)=='+') {
                correct = i;
                s = s.substring(1);
            }
            answers[i] = s;
        }
    }

    @Override
    public String toString() {
        return "Question{" + "question=" + question + ", picture=" + picture + ", addition=" + addition + ", answers=" + Arrays.toString(answers) + ", correct=" + correct + '}';
    }
    
    
}
