
package net.rimoto.core.models;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class FAQ_Category {
    @Expose
    private String name;

    private List<FAQ_Question> questions = new ArrayList<>();

    public FAQ_Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FAQ_Question> getQuestions() {
        return questions;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof FAQ_Category) && (((FAQ_Category) o).getName().equals(this.getName()));
    }
}
