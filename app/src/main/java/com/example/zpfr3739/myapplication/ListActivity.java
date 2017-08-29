package com.example.zpfr3739.myapplication;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FilterQueryProvider;
import android.widget.ListView;


/**
 * Fragment qui affiche les notes crées sous forme de liste (listview)
 * Le clique sur un élément de la liste permet de modifier ou supprimer une note
 */

public class ListActivity extends Fragment{

    DBHandler myDbHandler;
    ListView mlistView;
    SimpleCursorAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //on retourne le bon layout pour ce fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_list, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //modification du titre dans la toolbar
        getActivity().setTitle("List");

        //instanciation de la listview et de la BDD
        mlistView = (ListView) getView().findViewById(R.id.liste_note_view);
        myDbHandler = new DBHandler(getActivity());

        //insérer les notes dans une listview et les afficher
        populateListView();

        //faire une action lorsqu'on clique longtemps sur une note
        mlistView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {

                //ouverture d'un popup qui demande confirmation de la suppression de la note
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

        //faire une action lorsqu'on fait un clique court sur une note
        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {

                //passage vers le fragment d'écriture pour modification de la note
                Fragment newWriteFrag = new WriteActivity();

                //ajout d'un bundle contenant l'id de la note
                // pour pouvoir le renvoyer au fragment d'écriture pour modifier la note concernée
                Bundle arguments = new Bundle();
                arguments.putLong("ID_NOTE", id);

                newWriteFrag.setArguments(arguments);

                //renvoi vers le fragment d'écriture
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, newWriteFrag);
                ft.commit();

            }

        });

    }

    //creation de la barre de menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //on instancie le menu dédié au fragment de liste qui comprend essetiellement une icone de recherche
        menu.clear();
        inflater.inflate(R.menu.list_menu, menu);

        //mise en place d'une recherche de note à l'aide d'une "SearchView"
        MenuItem itemSearch = menu.findItem(R.id.app_bar_search);
        SearchView searchView = new SearchView(getActivity());

        //création d'un listener pour la recherche
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            //actualisation de la liste des notes en fonction des lettres tapés dans la barre de recherche
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        itemSearch.setActionView(searchView);
        super.onCreateOptionsMenu(menu, inflater);
    }


    //insertion des notes dans la listview
    public void populateListView(){

        //recuperer les données et les insérer dans la liste
        Cursor data = myDbHandler.getDataNote();

        //création d'un adapteur qui fait le lien entre la BDD et la view
        adapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_2,
                data,
                new String[] { DBHandler.NOTE_TITRE, DBHandler.NOTE_CREATE_DATE},
                new int[] { android.R.id.text1, android.R.id.text2 });

        mlistView.setAdapter(adapter);

        //ajout d'un filtre pour pouvoir effectuer des recherches
        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                String partialValue = constraint.toString();
                return myDbHandler.getNoteByTitle(partialValue);
            }
    });

    }

}
