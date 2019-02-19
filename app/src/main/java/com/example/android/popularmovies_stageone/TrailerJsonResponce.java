package com.example.android.popularmovies_stageone;

public class TrailerJsonResponce {
    private String name;
    private String source;

    public TrailerJsonResponce(String name, String source) {
        this.name = name;
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
