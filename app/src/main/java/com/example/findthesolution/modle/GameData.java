package com.example.findthesolution.modle;

import java.util.List;

public class GameData {
    int level_no;
    int unlock_points;
    List<Questions> questions;

    public GameData() {
    }

    public GameData(int level_no, int unlock_points) {
        this.level_no = level_no;
        this.unlock_points = unlock_points;
    }

    public GameData(int level_no, int unlock_points, List<Questions> questions) {
        this.level_no = level_no;
        this.unlock_points = unlock_points;
        this.questions = questions;
    }

    public int getLevel_no() {
        return level_no;
    }

    public void setLevel_no(int level_no) {
        this.level_no = level_no;
    }

    public int getUnlock_points() {
        return unlock_points;
    }

    public void setUnlock_points(int unlock_points) {
        this.unlock_points = unlock_points;
    }

    public List<Questions> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Questions> questions) {
        this.questions = questions;
    }
}
