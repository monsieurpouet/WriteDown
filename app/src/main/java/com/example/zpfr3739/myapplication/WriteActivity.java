package com.example.zpfr3739.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

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
    private ImageButton imageButton1;
    private ImageButton imageButton2;
    private ImageButton imageButton3;
    private ImageButton imageButton4;
    private ImageButton imageButton5;
    private ImageButton imageButton6;
    private ImageButton imageButton7;
    private ImageButton imageButton8;
    private ImageButton imageButton9;
    private ImageButton imageButton10;

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
                addTextInEditText(" ![ ]( ) ");
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
            myDbHandler.insertNote(newTitre,newContent,getDateNow(),getDateNow());
            ((MainActivity) getActivity()).toastMessage("Good");

        }
    }

    //effacer le titre et le contenu de la note
    public void onClickClear(){

        title.getEditText().setText("");
        content.getEditText().setText("");
    }



}
