package com.example.zpfr3739.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Created by ZPFR3739 on 10/08/2017.
 */

public class WriteActivity extends Fragment{

    private DBHandler myDbHandler;
    private TextInputLayout title;
    private TextInputLayout content;

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
        getActivity().setTitle("Menu Write");
        title = (TextInputLayout) getView().findViewById(R.id.input_layout_title);
        content = (TextInputLayout) getView().findViewById(R.id.input_layout_note);
        myDbHandler = new DBHandler(getActivity());


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
        }
        return false;
    }

    public String getDateNow(){
        Date dateNow = new Date();
        DateFormat shortDateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT,new Locale("FR","fr"));
        return shortDateFormat.format(dateNow);
    }


    //ajouter les données
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


}
