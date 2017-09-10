package com.example.zpfr3739.myapplication;

/**
Activité principale contenant la toolbar et le Navigation drawer
 Les items du navigation drawer menent vers des fragments contenant les vues principales de l'application
 Gestion de l'export de la base de données SQLITE
 */

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import static java.lang.System.exit;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BackupData.OnBackupListener {


    public NavigationView navigationView;
    //code necessaires pour la demande de permission
    public static final int REQUEST_CODE_OPEN_DIRECTORY = 1;
    private static final int REQUEST_CODE_LOCATION = 2;
    private BackupData backupData;
    private Context context;
    private DBHandler db;



    //URL du server Web
    private static final String URL = "http://90.84.41.97/insertdata.php";

    //méthode principale
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        //instanciation de la BDD
        db = new DBHandler(context);
        backupData = new BackupData(context);
        backupData.setOnBackupListener(this);

        //toolbar du haut
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //instanciation du Drawer et de la toolbar
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInputFromInputMethod(getCurrentFocus().getWindowToken(), 0);
            }
            //methode pour fermer le clavier quand on ouvre le drawer
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        };



        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //affichage du menu par defaut
        displaySelectedScreen(R.id.item_write);


    }

    //methode en cas d'appui sur le bouton retour
    @Override
    public void onBackPressed() {
        //Ferme le drawer si il est ouvert
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // ajoute les items au menu.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //gestion des actions lorsqu'on clique sur un item du menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // recupération de l'ID de l'item cliqué
        int id = item.getItemId();

        //clique sur l'item "About"
        if (id == R.id.app_bar_about) {
            snackMessage("WriteDown V1.0 created by S.PAIRAULT");
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    //gestion du clique dans le menu du drawer
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //appel de la méthode displayselectedscreen en lui passant l'ID de l'item cliqué
        displaySelectedScreen(item.getItemId());
        //make this method blank
        return true;
    }

    //méthode qui affiche un le fragent correspondant à l'item cliqué
    private void displaySelectedScreen(int itemId) {

        //creation d'un objet fragment null
        Fragment fragment = null;

        //lancemet du fragment en fonction de l'ID de l'item
        switch (itemId) {
            //clique sur l'item "Write"
            case R.id.item_write:
                //affichage du fragment d'écriture d'une note
                fragment = new WriteActivity();
                break;
            //clique sur l'item "List"
            case R.id.item_list:
                //affichage du fragment pour le listing des notes
                fragment = new ListActivity();
                break;
            //clique sur l'item "Sync"
            case R.id.item_sync:
                //envoi des données vers le serveur web
                SendData();
                break;

            //clic sur le bouton "Export"
            case R.id.item_export:
                //opération à faire depuis android 6.0
                //verification des permission d'accès au stockage du téléphone.
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Demande de permission
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_CODE_LOCATION);
                } else {

                    //la permission est deja accordée
                    //ouverture d'un popup qui demande confirmation de la suppression de la note
                    //l'utilisateur à le choix de sauvegarder sur le stockage interne ou sur la carte SD
                    AlertDialog.Builder askLocationStorage = new AlertDialog.Builder(this,android.R.style.Theme_Material_Dialog_Alert);

                    askLocationStorage.setTitle("Exports").setMessage("Où voulez-vous exporter vos Notes ?");
                    askLocationStorage.setPositiveButton("SD", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //appel de la méthode pour faire le backup sur la carte SD
                            backupData.exportBdd("SD");
                            dialog.dismiss();
                        }
                    });

                    askLocationStorage.setNeutralButton("Internal", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //appel de la méthode pour faire le backup sur la mémoire interne
                            backupData.exportBdd("Internal");
                            dialog.dismiss();
                        }
                    });

                    askLocationStorage.show();

                }
                break;

            //clique sur le bouton "Quit"
            case R.id.item_quit:
                //sortie de l'application
                exit(0);
                break;
        }

        //remplacement du fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        //fermeture du drawer à la fin du clique
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    //méthode pour afficher un toast
    public void toastMessage(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }

    //méthode pour afficher une SnackBar
    public void snackMessage(String Message){
        Snackbar.make(findViewById(R.id.content_frame),Message,Snackbar.LENGTH_LONG).show();

    }

    //methode qui permet d'ouvrir un explorateur de fichier
    public void openFolder(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        startActivityForResult(intent, REQUEST_CODE_OPEN_DIRECTORY);

    }

    //methode pour action à la fin de l'export de la BDD
    @Override
    public void onFinishExport(String error) {
        String notify = error;
        //si pas d'erreur d'export affichage d'une notification contenant le nom du fichier enregistré
        if (error == null) {
            notify = "Export success : " + backupData.getbackupDBPath() + " on " + backupData.getTypeStorage();
        }
        snackMessage(notify);
    }

    //prise en compte de la réponse de demande de permission
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // la permission est acceptée!
                } else {
                    // la permission est refusée.
                    // Le bouton d'export ne sera plus disponible pour l'utilisateur
                    snackMessage("WRITE_BDD Denied");
                    Menu nav_menu = navigationView.getMenu();
                    nav_menu.findItem(R.id.item_export).setEnabled(false);
                }
                break;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    //méthode d'envoi des données de notes vers le serveur HTTP
    public void SendData(){


        //recuperer les données de la table note dans un curseur
        Cursor data = db.getDataNote();
        final int totalProgressTime = data.getCount();
        int jumpTime = 0;

        //affichage d'une barre de progression
        //N.B : la barre fonctionne mais on ne la voit pas car l'envoi se fait trop rapidement
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving Notes to Webserver...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(totalProgressTime);
        progressDialog.setProgress(0);
        progressDialog.show();

        //on parcours le curseur
        while (data.moveToNext()){

            //on recupère les données par colonne
            final String sync_id = data.getString(data.getColumnIndex(DBHandler.NOTE_ID));
            final String sync_titre = data.getString(data.getColumnIndex(DBHandler.NOTE_TITRE));
            final String sync_content = data.getString(data.getColumnIndex(DBHandler.NOTE_CONTENT));
            final String sync_create_date = data.getString(data.getColumnIndex(DBHandler.NOTE_CREATE_DATE));
            final String sync_modify_date = data.getString(data.getColumnIndex(DBHandler.NOTE_MODIFY_DATE));


            //création d'une requete via la librairie Volley
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        //ecoute de la réponse du serveur
                        public void onResponse(String response) {
                            System.out.println(response);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println(error);

                        }
                    }){
                //stockage des données récupérées dans une HashMap
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("id",sync_id);
                    params.put("titre",sync_titre);
                    params.put("content",sync_content);
                    params.put("create_date",sync_create_date);
                    params.put("modify_date",sync_modify_date);
                    return params;
                }

            };

            //envoi de la requete via Volley
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

            //avancement de la barre de progression
            jumpTime += 1;
            progressDialog.setProgress(jumpTime);

        }

        //si tout a été envoyé, on ferme la barre de progression
        if (progressDialog.getProgress() == totalProgressTime) {
            progressDialog.dismiss();
        }
        snackMessage("Sync done");
    }



}
