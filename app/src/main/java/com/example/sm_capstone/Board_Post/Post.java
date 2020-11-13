package com.example.sm_capstone.Board_Post;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Post {

    private String documentId;//작성자의 uid
    private String title;
    private String contents;
    private String post_id;//게시글의 uid
    private String writer_name;
    @ServerTimestamp
    private Date date;
    private String post_photo;//게시글에 등록할 사진
   // private String user_photo;//게시글 작성자의 사진
    private String board_part;//동적게시판인지, 정적게시판인지



    public Post(String documentId, String title, String contents, String post_id, String writer_name, String post_photo,String board_part) {
        this.documentId = documentId;
        this.title = title;
        this.contents = contents;
        this.post_id = post_id;
        this.writer_name = writer_name;
        this.post_photo = post_photo;
        this.board_part=board_part;
    }


    public Post(){//빈생성자 생성

    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public String getBoard_part() {
        return board_part;
    }

    public void setBoard_part(String board_part) {
        this.board_part = board_part;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }


    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getWriter_name() {
        return writer_name;
    }

    public void setWriter_name(String writer_name) {
        this.writer_name = writer_name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPost_photo() {
        return post_photo;
    }

    public void setPost_photo(String post_photo) {
        this.post_photo = post_photo;
    }

    @Override
    public String toString() {
        return "Post{" +
                "documentId='" + documentId + '\'' +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", post_id='" + post_id + '\'' +
                ", writer_name='" + writer_name + '\'' +
                ", date=" + date +
                ", post_photo='" + post_photo + '\'' +
                ", board_part='" + board_part + '\'' +
                '}';
    }

}
