package com.bigndesign.light.Model;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.TableInfo;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name="verses", id = "_id")
public class Verses extends Model {

    @Column(name="label")
    public String label;

    @Column(name="text")
    public String text;

    @Column(name="verseOrder")
    public Integer verse_order;

    @Column(name="id")
    public String id;

    @Column(name="display")
    public String display;

    @Column(name="prevChapterId")
    public String prevChapterId;

    @Column(name="nextChapterId")
    public String nextChapterId;

    @Column(name="book")
    public Book book;

    @Column(name="chapter")
    public Chapter chapter;

    public Verses(){
        super();
    }


    public static void truncate(){
        TableInfo tableInfo = Cache.getTableInfo(Verses.class);
        ActiveAndroid.execSQL(
                String.format("DELETE FROM %s;",
                        tableInfo.getTableName()));
        ActiveAndroid.execSQL(
                String.format("DELETE FROM sqlite_sequence WHERE name='%s';",
                        tableInfo.getTableName()));
    }
}
