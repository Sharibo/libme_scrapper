package com.gmail.alexejkrawez.libme_scrapper;

public class Chapter {
    private String chapterName;
    private String chapterLink;


    public Chapter(String chapterName, String chapterLink) {
        this.chapterName = chapterName;
        this.chapterLink = chapterLink;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getChapterLink() {
        return chapterLink;
    }

    public void setChapterLink(String chapterLink) {
        this.chapterLink = chapterLink;
    }

    @Override
    public String toString() {
        return "chapterName = " + chapterName + "\n";
    }
}
