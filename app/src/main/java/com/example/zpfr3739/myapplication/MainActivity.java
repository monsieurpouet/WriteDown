package com.example.zpfr3739.myapplication;

/**
Activité principale contenant la toolbar et le Navigation drawer
 Les items du navigation drawer menent vers des fragments contenant les vues principales de l'application
 Gestion de l'export de la base de données SQLITE
 */

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //affichage du menu par defaut
        displaySelectedScreen(R.id.item_write);

        //URL du serveur web
        String serverweb_url = "http://192.168.43.87/myfiles/index.php";


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
                    // la permission est deja accordée
                    backupData.exportToSD();
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
            notify = "Export success : " + backupData.getbackupDBPath();
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



}
