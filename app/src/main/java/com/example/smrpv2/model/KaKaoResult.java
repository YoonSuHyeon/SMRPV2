package com.example.smrpv2.model;

import java.util.List;

public class KaKaoResult {
    List<int[]> boxes ;
    String[] recognition_words;

    public List<int[]> getBoxes() {
        return boxes;
    }

    public void setBoxes(List<int[]> boxes) {
        this.boxes = boxes;
    }

    public String[] getRecognition_words() {
        return recognition_words;
    }

    public void setRecognition_words(String[] recognition_words) {
        this.recognition_words = recognition_words;
    }
}
