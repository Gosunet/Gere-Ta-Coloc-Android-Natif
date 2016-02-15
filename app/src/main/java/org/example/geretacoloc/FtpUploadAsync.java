package org.example.geretacoloc;

import android.os.AsyncTask;

import java.io.File;

import it.sauronsoftware.ftp4j.FTPClient;

/**
 * Created by Gosunet on 17/05/2015.
 * Async Task for upload fie via Ftp
 */
public class FtpUploadAsync extends AsyncTask<File, Void, Void>
{
    //Pour le FTP

    /*********  work only for Dedicated IP ***********/
    static final String FTP_HOST= "ftp.byethost33.com";

    /*********  FTP USERNAME ***********/
    static final String FTP_USER = "b33_16267513";

    /*********  FTP PASSWORD ***********/
    static final String FTP_PASS  ="240694h2vwcc7";

    @Override
    protected Void doInBackground(File... params) {

        FTPClient client = new FTPClient();
        try {

                client.connect(FTP_HOST,21);
                client.login(FTP_USER, FTP_PASS);
                client.setType(FTPClient.TYPE_BINARY);
                client.changeDirectory("/htdocs");

                client.upload(params[0]);
            return null;

            } catch (Exception e) {
                e.printStackTrace();
                try {
                    client.disconnect(true);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        return null;
    }

}
