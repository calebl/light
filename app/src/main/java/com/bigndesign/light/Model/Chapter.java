package com.bigndesign.light.Model;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Chapter extends RealmObject {
    @PrimaryKey
    private String id;
    private String label;
    private String chapter;
    private String osisEnd;
    private Integer chapterOrder;
    private String display;
    private Book book;
    private Verses verses;

    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public String getChapter() {
        return chapter;
    }
    public void setChapter(String chapter) {
        this.chapter = chapter;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getOsisEnd() {
        return osisEnd;
    }
    public void setOsisEnd(String osisEnd) {
        this.osisEnd = osisEnd;
    }
    public Integer getChapterOrder() {
        return chapterOrder;
    }
    public void setChapterOrder(Integer chapterOrder) {
        this.chapterOrder = chapterOrder;
    }
    public String getDisplay() {
        return display;
    }
    public void setDisplay(String display) {
        this.display = display;
    }
    public Book getBook() {
        return book;
    }
    public void setBook(Book book) {
        this.book = book;
    }
    public Verses getVerses() {
        if(verses == null){
            return Realm.getDefaultInstance().where(Verses.class).equalTo("chapter.id", this.getId()).findFirst();
        }

        return verses;
    }
    public void setVerses(Verses verses) {
        this.verses = verses;
    }

    public Chapter(){
        super();
    }

    public static void truncate(){
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Chapter.class).findAll().deleteAllFromRealm();
            }
        });
    }
}
