
package net.rimoto.core.models;

import com.google.gson.annotations.Expose;

public class FAQ_Question {
    @Expose
    private String question;

    @Expose
    private String category;

    @Expose
    private String answer;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
