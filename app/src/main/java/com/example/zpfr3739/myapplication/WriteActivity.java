package com.example.zpfr3739.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import static com.example.zpfr3739.myapplication.R.id.imageButton;


/**
 * Activité de création d'une note
 */

public class WriteActivity extends Fragment{

    private DBHandler myDbHandler;
    private TextInputLayout title;
    private TextInputLayout content;
    private ImageButton imageButton1;//bold
    private ImageButton imageButton2;//italique
    private ImageButton imageButton3;//link
    private ImageButton imageButton4;//list with no number
    private ImageButton imageButton5;//list with number
    private ImageButton imageButton6;//images
    private ImageButton imageButton7;//code
    private ImageButton imageButton8;//quote
    private ImageButton imageButton9;//texte barré
    private ImageButton imageButton10;//title
    private long key_id_note;
    private static final int PICK_IMAGE_REQUEST= 99;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        setHasOptionsMenu(true);



        return inflater.inflate(R.layout.fragment_write, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Write");
        title = (TextInputLayout) getView().findViewById(R.id.input_layout_title);
        content = (TextInputLayout) getView().findViewById(R.id.input_layout_note);
        myDbHandler = new DBHandler(getActivity());

        //initialisation de l'id d'une note à zero
        //si l'ID est modifié cela signifie qu'il s'agit d'une modification de note; sinon c'est qu'il s'agit d'une nouvelle note
        key_id_note = 0;

        //recupération du bundle si envoyé par fragment de liste, puis insertion de la note concernée dans les edittext afin de les modifier
        Bundle bundle_list = getArguments();
        if (bundle_list != null) {
            key_id_note = bundle_list.getLong("ID_NOTE");
            Cursor curs_note = myDbHandler.getNoteById(key_id_note);
            if (curs_note.moveToFirst()) {
                //str = cursor.getString(cursor.getColumnIndex("content"));
                title.getEditText().setText(curs_note.getString(curs_note.getColumnIndex("titre_note")));
                content.getEditText().setText(curs_note.getString(curs_note.getColumnIndex("note")));
            }

        }

        imageButton1 = (ImageButton) getView().findViewById(imageButton);
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTextInEditText(" ** ** ");
            }
        });
        imageButton2 = (ImageButton) getView().findViewById(R.id.imageButton2);
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTextInEditText(" * * ");
            }
        });
        imageButton3 = (ImageButton) getView().findViewById(R.id.imageButton3);
        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTextInEditText(" [ ]( ) ");
            }
        });
        imageButton4 = (ImageButton) getView().findViewById(R.id.imageButton4);
        imageButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTextInEditText(" \n");
                addTextInEditText("- ");
            }
        });
        imageButton5 = (ImageButton) getView().findViewById(R.id.imageButton5);
        imageButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTextInEditText(" \n");
                addTextInEditText("1. ");
            }
        });
        imageButton6 = (ImageButton) getView().findViewById(R.id.imageButton6);
        imageButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
                //addTextInEditText(" ![ ]( ) ");
            }
        });
        imageButton7 = (ImageButton) getView().findViewById(R.id.imageButton7);
        imageButton7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTextInEditText("    ");
            }
        });
        imageButton8 = (ImageButton) getView().findViewById(R.id.imageButton8);
        imageButton8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTextInEditText(" \n");
                addTextInEditText("> ");
            }
        });
        imageButton9 = (ImageButton) getView().findViewById(R.id.imageButton9);
        imageButton9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTextInEditText(" ~~ ~~ ");
            }
        });
        imageButton10 = (ImageButton) getView().findViewById(R.id.imageButton10);
        imageButton10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTextInEditText("#");
            }
        });

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        menu.clear();
        inflater.inflate(R.menu.write_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.app_bar_save:
                ((MainActivity) getActivity()).toastMessage("Vous avez cliqué sur save");
                onClickSave();
                return true;

            case R.id.app_bar_clear:
                ((MainActivity) getActivity()).toastMessage("Vous avez cliqué sur clear");
                onClickClear();
                return true;

        }
        return false;
    }

    public String getDateNow(){
        Date dateNow = new Date();
        DateFormat shortDateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT,Locale.FRANCE);
        return shortDateFormat.format(dateNow);
    }


    public void addTextInEditText(String add){
        content.getEditText().append(add);

    }

    //vérification du contenu de la note et sauvegarde d'une note
    public void onClickSave(){
        String newTitre = title.getEditText().getText().toString();
        String newContent = content.getEditText().getText().toString();
        title.setError(null);
        if (TextUtils.isEmpty(newTitre)){
            title.setError("Titre incorrect");
        }else if (TextUtils.isEmpty(newContent)){
            content.setError("contenu incorrect");
        }else {
            if (key_id_note == 0){
                myDbHandler.insertNote(newTitre,newContent,getDateNow(),getDateNow());
            }else{
                myDbHandler.updateNoteById(key_id_note,newTitre,newContent,getDateNow());
            }
            ((MainActivity) getActivity()).toastMessage("Good");

        }
    }

    //effacer le titre et le contenu de la note
    public void onClickClear(){

        title.getEditText().setText("");
        content.getEditText().setText("");
    }


    //ouverture de la galerie
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


}
