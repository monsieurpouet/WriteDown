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
 * Created by ZPFR3739 on 25/08/2017.
 */

public class BackupData {


    DBHandler myDbHandler;

        // url for database
        private final String dataPath = "//data//com.example.zpfr3739.myapplication//databases//";

        // name of main data
        private final String dataName = myDbHandler.DATABASE_NAME;

        // data main
        private final String data = dataPath + dataName;

        // name of temp data
        private final String dataTempName = myDbHandler.DATABASE_NAME + "_temp";

        // temp data for copy data from sd then copy data temp into main data
        private final String dataTemp = dataPath + dataTempName;

        // folder on sd to backup data
        private final String folderSD = Environment.getExternalStorageDirectory() + "/WriteDownDbDump";

        private Context context;
        public String backupDBPath;

        public BackupData(Context context) {
            this.context = context;
        }

        // create folder if it not exist
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
         * Copy database to sd card
         * name of file = database name + time when copy
         * When finish, we call onFinishExport method to send notify for activity
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
                    System.out.println("peux pas ecrire");
                }

            } catch (Exception e) {
                e.printStackTrace();
                error = "Error backup";
            }
            onBackupListener.onFinishExport(error);
        }



        private OnBackupListener onBackupListener;

        public void setOnBackupListener(OnBackupListener onBackupListener) {
            this.onBackupListener = onBackupListener;
        }

        public interface OnBackupListener {
            public void onFinishExport(String error);

        }

        public String getbackupDBPath(){
            return backupDBPath;
        }

    }


