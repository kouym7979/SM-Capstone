package com.example.sm_capstone.Board_Post;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Post {

    private String documentId;
    private String title;
    private String contents;
    private String post_id;
    private String writer_name;
    @ServerTimestamp
    private Date date;

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

    public Post(String documentId, String title, String contents,  String post_id, String writer_name) {
        this.documentId = documentId;
        this.title = title;
        this.contents = contents;
        this.post_id = post_id;
        this.writer_name = writer_name;
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
                '}';
    }
}
