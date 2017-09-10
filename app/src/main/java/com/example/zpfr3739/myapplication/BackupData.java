package com.example.zpfr3739.myapplication;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Export de la base de données SQlite contenant les notes
 */

public class BackupData {


        DBHandler myDbHandler;

        // emplacement de la BDD
        private final String dataPath = "//data//com.example.zpfr3739.myapplication//databases//";

        // nom de la BDD
        private final String dataName = myDbHandler.DATABASE_NAME;

        // chemin complet vers BDD
        private final String data = dataPath + dataName;

        // répertoire ou sera déposé l'export de la BDD sur la carte SD
        private final String folderSD = Environment.getExternalStorageDirectory() + "/WriteDownDbDump";

        // répertoire ou sera déposé l'export de la BDD sur le stockage interne
        private final String folderInternal;

        private Context context;
        public String backupDBPath;
        public String type_storage;
        private OnBackupListener onBackupListener;

        public BackupData(Context context) {
            this.context = context;
            //destination de l'export sur stockage sur la mémoire Interne
            folderInternal  = this.context.getFilesDir() + "/bdd/WriteDownDbDump";
        }

        // creation du dossier de destination si il n'existe pas
        private void createFolder(String folder) {
            File filefolder = new File(folder);
            if (!filefolder.exists()) {
                filefolder.mkdirs();
                System.out.println("create folder");
            } else {
                System.out.println("exits");
            }
        }

        /**
         * Copie de la BDD vers le répertoire de destination Interne ou externe
         * nom du fichier = nom de l'application + date de copie
         * Quand la copie est terminée, on fait appel à la méthode "onFinishExport" pour envoyer une notification à l'activité principale
         */
        public void exportBdd(String type_stor) {

            String error = null;
            try {
                //creation des répertoires
                createFolder(folderSD);
                createFolder(folderInternal);
                File sd = new File(folderSD);
                File internal = new File(folderSD);
                SimpleDateFormat formatTime = new SimpleDateFormat("yyyyMMdd_HHmmss");
                type_storage = type_stor;

                //vérification de la destination de l'export
                if (type_stor == "SD"){

                    //tentative d'écriture sur la carte SD
                    if (sd.canWrite()) {

                        backupDBPath = "WriteDown_" + formatTime.format(new Date()) + ".db";

                        File currentDB = new File(Environment.getDataDirectory(), data);
                        File backupDB = new File(sd, backupDBPath);

                        if (currentDB.exists()) {

                            FileChannel src = new FileInputStream(currentDB).getChannel();
                            FileChannel dst = new FileOutputStream(backupDB).getChannel();
                            dst.transferFrom(src, 0, src.size());
                            src.close();
                            dst.close();
                        } else {
                            System.out.println("db not exist");
                        }
                    }else{
                        System.out.println("on ne peux pas ecrire sur la carte SD");
                    }

                } else if (type_storage == "Internal"){
                    //tentative d'écriture sur la mémoire interne
                    if (internal.canWrite()) {

                        backupDBPath = "WriteDown_" + formatTime.format(new Date()) + ".db";

                        File currentDB = new File(Environment.getDataDirectory(), data);
                        File backupDB = new File(internal, backupDBPath);

                        if (currentDB.exists()) {

                            FileChannel src = new FileInputStream(currentDB).getChannel();
                            FileChannel dst = new FileOutputStream(backupDB).getChannel();
                            dst.transferFrom(src, 0, src.size());
                            src.close();
                            dst.close();
                        } else {
                            System.out.println("db not exist");
                        }
                    }else{
                        System.out.println("on ne peux pas ecrire sur la mémoire interne");
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
                error = "Error backup";
            }
            onBackupListener.onFinishExport(error);
        }


        public void setOnBackupListener(OnBackupListener onBackupListener) {
            this.onBackupListener = onBackupListener;
        }

        public interface OnBackupListener {
            public void onFinishExport(String error);
        }

        //récupération du chemin de l'export
        public String getbackupDBPath(){
            return backupDBPath;
        }

        //récupération du type de stockage (interne ou externe)
        public String getTypeStorage(){ return type_storage;}

    }


