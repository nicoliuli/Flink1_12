package com.wb.common;

public class WC {
    private String word;
    private Long cnt;

    public WC() {
    }

    public WC(String word, Long cnt) {
        this.word = word;
        this.cnt = cnt;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Long getCnt() {
        return cnt;
    }

    public void setCnt(Long cnt) {
        this.cnt = cnt;
    }
}
