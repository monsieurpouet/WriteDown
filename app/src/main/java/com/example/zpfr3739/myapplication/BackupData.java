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

        // répertoire ou sera déposé l'export de la BDD
        private final String folderSD = Environment.getExternalStorageDirectory() + "/WriteDownDbDump";

        private Context context;
        public String backupDBPath;
        private OnBackupListener onBackupListener;

        public BackupData(Context context) {
            this.context = context;
        }

        // creation du dossier de destination si il n'existe pas
        private void createFolder() {
            File sd = new File(folderSD);
            if (!sd.exists()) {
                sd.mkdirs();
                System.out.println(folderSD);
                System.out.println("create folder");
            } else {
                System.out.println("exits");
            }
        }

        /**
         * Copie de la BDD vers le répertoire de destination
         * nom du fichier = nom de l'application + date de copie
         * Quand la copie est terminée, on fait appel à la méthode "onFinishExport" pour envoyer une notification à l'activité principale
         */
        public void exportToSD() {

            String error = null;
            try {

                createFolder();

                File sd = new File(folderSD);

                if (sd.canWrite()) {

                    SimpleDateFormat formatTime = new SimpleDateFormat("yyyyMMdd_HHmmss");
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
                    System.out.println("on ne peux pas ecrire");
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

    }


