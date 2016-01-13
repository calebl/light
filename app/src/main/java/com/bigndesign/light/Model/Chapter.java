package com.bigndesign.light.Model;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.TableInfo;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "chapter", id = "_id")
public class Chapter extends Model {
    @Column(name = "label")
    public String label;

    @Column(name= "chapter")
    public String chapter;

    @Column(name = "id")
    public String id;

    @Column(name="osisEnd")
    public String osis_end;

    @Column(name="chapterOrder")
    public Integer chapter_order;

    @Column(name="display")
    public String display;

    @Column(name="book")
    public Book book;

    @Column(name="verses")
    public Verses verses;

    public Chapter(){
        super();
    }

    public static void truncate(){
        TableInfo tableInfo = Cache.getTableInfo(Chapter.class);
        ActiveAndroid.execSQL(
                String.format("DELETE FROM %s;",
                        tableInfo.getTableName()));
        ActiveAndroid.execSQL(
                String.format("DELETE FROM sqlite_sequence WHERE name='%s';",
                        tableInfo.getTableName()));
    }
}
