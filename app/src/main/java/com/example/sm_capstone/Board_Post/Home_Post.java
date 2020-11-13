package com.example.sm_capstone.Board_Post;

public class Home_Post {

    private String board_part;
    private String title;

    public Home_Post(){

    }

    public Home_Post(String board_part, String title) {
        this.board_part = board_part;
        this.title = title;
    }

    public String getBoard_part() {
        return board_part;
    }

    public void setBoard_part(String board_part) {
        this.board_part = board_part;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Home_Post{" +
                "board_part='" + board_part + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
