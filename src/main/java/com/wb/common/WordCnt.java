package com.wb.common;

public class WordCnt {
    private Integer id;
    private String word;
    private String filepath;
    private Integer cnt;

    public WordCnt() {
    }

    public WordCnt(Integer id, String word, String filepath, Integer cnt) {
        this.id = id;
        this.word = word;
        this.filepath = filepath;
        this.cnt = cnt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }
}
