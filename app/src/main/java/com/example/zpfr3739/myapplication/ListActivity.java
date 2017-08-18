package com.example.zpfr3739.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * Created by ZPFR3739 on 10/08/2017.
 */

public class ListActivity extends Fragment{

    private DBHandler myDbHandler;
    private ListView mlistView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.app_bar_search:
                ((MainActivity) getActivity()).toastMessage("Vous avez cliqué sur search");
                //
                return true;

            case R.id.app_bar_refresh:
                ((MainActivity) getActivity()).toastMessage("Vous avez cliqué sur refresh");
                populateListView();
                //
                return true;

        }
        return false;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Menu List");

        mlistView = (ListView) getView().findViewById(R.id.liste_note_view);
        myDbHandler = new DBHandler(getActivity());

        //insérer les notes dans une listview et les afficher
        populateListView();

        //faire une action lorsqu'on clique longtemps sur une note
        mlistView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {

                //
                AlertDialog.Builder alert = new AlertDialog.Builder(
                        getActivity());
                alert.setTitle("Attention!!");
                alert.setMessage("Are you sure to delete this record");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //action si confirmation de la suppression
                        myDbHandler.removeNote(id);
                        populateListView();
                        dialog.dismiss();

                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                alert.show();

                return true;

            }

        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        menu.clear();
        inflater.inflate(R.menu.list_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void populateListView(){

        //recuperer les données et les insérer dans la liste
        Cursor data = myDbHandler.getDataNote();


          /*
        ArrayList<String> listData = new ArrayList<>();
        while(data.moveToNext()){
            //recuperer la valeur à la colonne 1 puis l'ajouter à l'arrylist
            listData.add(data.getString(1));
        }
        //créer un listadapter et parametrer l'adapteur
        ListAdapter adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, listData);
        mlistView.setAdapter(adapter);
*/
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_2,
                data,
                new String[] { DBHandler.NOTE_TITRE, DBHandler.NOTE_CONTENT},
                new int[] { android.R.id.text1, android.R.id.text2 });

        mlistView.setAdapter(adapter);


    }


}
