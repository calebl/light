package com.bigndesign.light.Model;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.TableInfo;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

@Table(name="books", id = "_id")
public class Book extends Model {

    @Column(name="versionId")
    public String version_id;

    @Column(name="name")
    public String name;

    @Column(name="abbreviation")
    public String abbreviation;

    @Column(name="bookOrder")
    public Integer book_order;

    @Column(name="bookGroupId")
    public Integer book_group_id;

    @Column(name="testament")
    public String testament;

    @Column(name="id")
    public String id;

    @Column(name="osisEnd")
    public String osis_end;

    public List<Chapter> chapters() {
        return getMany(Chapter.class, "book");
    }

    public List<Verses> verses() {
        return getMany(Verses.class, "book");
    }

    public Book(){
        super();
    }

    public static void truncate(){
        TableInfo tableInfo = Cache.getTableInfo(Book.class);
        ActiveAndroid.execSQL(
                String.format("DELETE FROM %s;",
                        tableInfo.getTableName()));
        ActiveAndroid.execSQL(
                String.format("DELETE FROM sqlite_sequence WHERE name='%s';",
                        tableInfo.getTableName()));
    }
}
