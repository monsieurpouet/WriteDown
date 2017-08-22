package com.example.zpfr3739.myapplication;


/**
 *Objet correspondant à la table NOTE de la BDD WriteDown
 gérer l’interface entre notre application et la base de données
 *Colonnes: Id_note, Titre_note, Note, Date_creation, Date_modif
 */

public class DefNoteTable {

    private long id_note;       // Identifiant de la note
    private String titre_note;  // Titre de la note
    private String note;	//Contenu de la note
    private String date_creation;   // Date de creation de la note
    private String date_modification;   // Date de dernière modification de la note


    public DefNoteTable(){

    }


    //Constructeur
    public DefNoteTable(long id_note,String titre_note,String note,String date_creation,String date_modification){
        this.id_note=id_note;
        this.titre_note=titre_note;
        this.note=note;
        this.date_creation=date_creation;
        this.date_modification=date_modification;

    }

//creation des getter

    public long getId_note() {
        return id_note;
    }

    public String getTitre_note() {
        return titre_note;
    }

    public String getNote() {
        return note;
    }

    public String getDate_creation() {
        return date_creation;
    }

    public String getDate_modif() {
        return date_modification;
    }

//creation des setter

    public void setId(long id_note) {
        this.id_note = id_note;
    }

    public void setTitre(String titre_note) {
        this.titre_note = titre_note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setDate_creation(String date_creation) {
        this.date_creation = date_creation;
    }

    public void setDate_modif(String date_modification) {
        this.date_modification = date_modification;
    }


}