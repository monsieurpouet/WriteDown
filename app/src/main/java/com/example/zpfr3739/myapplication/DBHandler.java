package com.example.zpfr3739.myapplication;

/**
 Gestion de la base SQlite
 handle database Create, Read, Update and Delete (CRUD)

 onCreate: It is called first time when database is created. We usually create tables and the initialize here.
 onUpgrade: Run when database is upgraded / changed, like drop tables, add tables etc.
 */

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import java.util.Date;


public class DBHandler extends SQLiteOpenHelper {

    //Database Name
    private static final String DATABASE_NAME = "writedown.db";
    //Database Version
    private static final int DATABASE_VERSION = 1;

    //nom de la table
    public static final String NOTE_TABLE_NAME = "Note_Table";

    //atributs de la table "Note_Table"
    public static final String NOTE_ID = "id_note" ;
    public static final String NOTE_TITRE = "titre_note";
    public static final String NOTE_CONTENT = "note";
    public static final String NOTE_CREATE_DATE = "date_creation";
    public static final String NOTE_MODIFY_DATE = "date_modification";


    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //requete de création de la table "Note_Table"
    public static final String NOTE_TABLE_CREATE =
            "CREATE TABLE " + NOTE_TABLE_NAME + " (" +
                    NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    NOTE_TITRE + " TEXT NOT NULL," +
                    NOTE_CONTENT + " TEXT NOT NULL," +
                    NOTE_CREATE_DATE + " TEXT NOT NULL," +
                    NOTE_MODIFY_DATE + " TEXT NOT NULL)";


    //requete de suppression de la table "Note_Table"
    public static final String SUPPRESSION_TABLE_NOTE =
            "DROP TABLE IF EXISTS " + NOTE_TABLE_NAME;



    // Création de la base de données
    // on exécute ici les requêtes de création des tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(NOTE_TABLE_CREATE);
    }

    // Mise à jour de la base de données
    // méthode appelée sur incrémentation de DATABASE_VERSION
    // on peut faire ce qu'on veut ici, comme recréer la base :
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,int newVersion) {
        // Drop older table if existed
        db.execSQL(SUPPRESSION_TABLE_NOTE);
        // Creating tables again
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    //insert new note
    public void insertNote(String titre, String note, String date_cre, String date_modif) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(NOTE_TITRE, titre);
        values.put(NOTE_CONTENT, note);
        values.put(NOTE_CREATE_DATE, date_cre);
        values.put(NOTE_MODIFY_DATE, date_modif);
        db.insert(NOTE_TABLE_NAME, null, values);
        db.close();
    }


    //retourne les info de la table
    public Cursor getDataNote(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT id_note _id,* FROM " + NOTE_TABLE_NAME;

        Cursor data = db.rawQuery(query, null);
        return data;
    }

    //retourne l'ID qui correspond au nom de la note
    public Cursor getIdNote(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT "+ NOTE_ID + " FROM " + NOTE_TABLE_NAME + " WHERE " + NOTE_TITRE + " = '" + name + "'";

        Cursor data = db.rawQuery(query, null);
        return data;
    }

    //methode pour supprimer une note
    public void removeNote(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        String string =String.valueOf(id);
        db.execSQL("DELETE FROM Note_Table WHERE id_note = '" + string + "'");

    }


    //methode pour updater une note en fonction de son ID + modifier date de dernière modif
    public Cursor getNoteById(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + NOTE_TITRE +"," + NOTE_CONTENT + " FROM " + NOTE_TABLE_NAME + " WHERE " + NOTE_ID + " = " + id + " ";

        Cursor data = db.rawQuery(query, null);
        return data;
    }



    //recherche de note par le titre qui contient les caracteres saisies
    public Cursor getNoteByTitle(String partialValue){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT id_note _id,* FROM " + NOTE_TABLE_NAME + " WHERE " + NOTE_TITRE + " like '%" + partialValue + "%'";

        Cursor data = db.rawQuery(query, null);
        return data;
    }

    //update d'une note par ID
    public void updateNoteById(long id, String newtitre, String newnote, String newdate_modif){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(NOTE_TITRE, newtitre);
        values.put(NOTE_CONTENT, newnote);
        values.put(NOTE_MODIFY_DATE, newdate_modif);
        db.update(NOTE_TABLE_NAME,values, "id_note=" + id, null);
        db.close();
    }


}
