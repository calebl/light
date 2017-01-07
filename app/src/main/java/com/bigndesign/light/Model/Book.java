package com.bigndesign.light.Model;


import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class Book extends RealmObject {

    @PrimaryKey
    private String id;
    private String versionId;
    private String name;
    private String abbreviation;
    private Integer bookOrder;
    private Integer bookGroupId;
    private String testament;
    private String osisEnd;

    private RealmList<Chapter> chapters = new RealmList<>();
    private RealmList<Verses> verses = new RealmList<>();

    public String getVersionId() {
        return versionId;
    }
    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAbbreviation() {
        return abbreviation;
    }
    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }
    public Integer getBookOrder() {
        return bookOrder;
    }
    public void setBookOrder(Integer bookOrder) {
        this.bookOrder = bookOrder;
    }
    public Integer getBookGroupId() {
        return bookGroupId;
    }
    public void setBookGroupId(Integer bookGroupId) {
        this.bookGroupId = bookGroupId;
    }
    public String getTestament() {
        return testament;
    }
    public void setTestament(String testament) {
        this.testament = testament;
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
    public RealmList<Chapter> getChapters() {
        return chapters;
    }
    public void setChapters(RealmList<Chapter> chapters) {
        this.chapters = chapters;
    }
    public RealmList<Verses> getVerses() {
        return verses;
    }
    public void setVerses(RealmList<Verses> verses) {
        this.verses = verses;
    }

    public Book(){
        super();
    }

    public static void truncate(){
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Book.class).findAll().deleteAllFromRealm();
            }
        });
    }

    public static RealmResults<Book> all(){
        return Realm.getDefaultInstance().where(Book.class).findAllSorted("bookOrder");
    }

    public static ArrayList<String> bookTitles(){
        ArrayList<String> titles = new ArrayList<>();


        return titles;

    }
}
