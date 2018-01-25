package net.doyouhike.app.wildbird.biz.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.doyouhike.app.wildbird.biz.model.DbContent;

/**
 * Created by zaitu on 15-11-17.
 */
public class WildBirdDbHelper extends SQLiteOpenHelper {

    public WildBirdDbHelper(Context context) {
        super(context, DbContent.DB_NM, null, DbContent.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table if not exists wildbird("
                + "speciesID text not null,"
                + "localName text not null,"
                + "engName text not null,"
                + "latinName text not null,"
                + "ordo text,"
                + "familia text,"
                + "genus text,"
                + "shape text,"
                + "color text,"
                + "locate text,"
                + "behavior text,"
                + "head text,"
                + "neck text,"
                + "belly text,"
                + "waist text,"
                + "wing text,"
                + "tail text,"
                + "leg text,"
                + "image text,"
                + "author text,"
                + "primary key(speciesID))");

        db.execSQL("create table if not exists comment("
                + "speciesID text not null,"
                + "commentID text not null,"
                + "userID text,"
                + "userName text,"
                + "isLike text,"
                + "avatar text,"
                + "likeNum text,"
                + "content text,"
                + "time text,"
                + "foreign key(speciesID) references wildbird(speciesID) "
                + "on delete cascade "
                + "on update cascade)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
