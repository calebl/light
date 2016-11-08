package com.bigndesign.light.Model;


import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Verses extends RealmObject {

    @PrimaryKey
    private String id;
    private String label;
    private String text;
    private Integer verseOrder;
    private String display;
    private String prevChapterId;
    private String nextChapterId;
    private Book book;
    private Chapter chapter;

    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public Integer getVerseOrder() {
        return verseOrder;
    }
    public void setVerseOrder(Integer verseOrder) {
        this.verseOrder = verseOrder;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getDisplay() {
        return display;
    }
    public void setDisplay(String display) {
        this.display = display;
    }
    public String getPrevChapterId() {
        return prevChapterId;
    }
    public void setPrevChapterId(String prevChapterId) {
        this.prevChapterId = prevChapterId;
    }
    public String getNextChapterId() {
        return nextChapterId;
    }
    public void setNextChapterId(String nextChapterId) {
        this.nextChapterId = nextChapterId;
    }
    public Book getBook() {
        return book;
    }
    public void setBook(Book book) {
        this.book = book;
    }
    public Chapter getChapter() {
        return chapter;
    }
    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }

    public Verses(){
        super();
    }


    public static void truncate(){
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Verses.class).findAll().deleteAllFromRealm();
            }
        });
    }
}
