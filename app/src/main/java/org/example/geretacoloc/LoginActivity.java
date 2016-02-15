package org.example.geretacoloc;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import it.sauronsoftware.ftp4j.FTPClient;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements LoaderCallbacks<Cursor> {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private EditText mColocationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();


        mColocationView = (EditText) findViewById(R.id.nomColocation);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.btnConnexion);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button nouveauCompte = (Button) findViewById(R.id.btnNewCompte);
        nouveauCompte.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                showProgress(true);
                UserRegistrationTask userRegistrationTask= new UserRegistrationTask(mEmailView.getText().toString(),mPasswordView.getText().toString(),mColocationView.getText().toString());
                File dir;
                dir = getDir("Test",MODE_PRIVATE);
                userRegistrationTask.execute(dir.getAbsolutePath());

            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

    }

    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String colocation = mColocationView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password, colocation);
            File dir;
            dir = getDir("Test",MODE_PRIVATE);
            mAuthTask.execute(dir.getAbsolutePath());
        }
    }

    //
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }
    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    /**
     * Represents an asynchronous login task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<String, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private final String mColocation;

        UserLoginTask(String email, String password, String colocation) {
            mEmail = email;
            mPassword = password;
            mColocation = colocation;
        }


        /*********  work only for Dedicated IP ***********/
        static final String FTP_HOST= "ftp.byethost33.com";

        /*********  FTP USERNAME ***********/
        static final String FTP_USER = "b33_16267513";

        /*********  FTP PASSWORD ***********/
        static final String FTP_PASS  ="240694h2vwcc7";

        @Override
        /*
        Connection au serveur ftp pour vérifier si l'user excite qu'il a le bn mdp et la bonne coloc
         */
        protected Boolean doInBackground(String... params) {

            FTPClient client = new FTPClient();
            try {
                // Simulate network access.
                client.connect(FTP_HOST,21);
                client.login(FTP_USER, FTP_PASS);
                client.setType(FTPClient.TYPE_BINARY);
                client.changeDirectory("/htdocs/");

                File file = new File(params[0],mColocation+".txt");
                client.download(mColocation+".txt",file);

            } catch (Exception e) {
                e.printStackTrace();
                try {
                    client.disconnect(true);
                    //Si impossibilité de trouver le fichier c'est que la Coloc n'existe pas.
                    //Ou pas de connection aux serveur
                    //TODO différencier les deux erreurs

                    return false;
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }

            //Convertir le fichier download en string pour pouvoir récupére le mdp et le mail et pouvoir le comparer

            try {
                String jsonContent = getStringFromFile(params[0]+"/"+mColocation+".txt");
                Log.d("Json",jsonContent);

                //stocker le Json download dans la Singleton
                SingletonColoc singletonColoc=SingletonColoc.getInstance().jsonToObject(jsonContent);

                Log.d("Json",singletonColoc.getMails().get(0));

                for (int i=0;i<singletonColoc.getMails().size();i++) {
                    if (singletonColoc.getMails().get(i).equals(mEmail)) {
                        if (singletonColoc.getMdps().get(i).equals(mPassword)) {
                            return true;
                        }
                    }
                }
                    } catch (Exception e) {
                e.printStackTrace();
                try {
                    client.disconnect(true);
                    return false;
                } catch (Exception e2){
                    e2.printStackTrace();
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                intent.putExtra("nomColoc",mColocation);
                startActivity(intent);
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    /**
     * Represents an asynchronous registration task used to authenticate
     * the user.
     */
    public class UserRegistrationTask extends AsyncTask<String, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private final String mColocation;

        UserRegistrationTask(String email, String password, String colocation) {
            mEmail = email;
            mPassword = password;
            mColocation = colocation;
        }
        /*********  work only for Dedicated IP ***********/
        static final String FTP_HOST= "ftp.byethost33.com";

        /*********  FTP USERNAME ***********/
        static final String FTP_USER = "b33_16267513";

        /*********  FTP PASSWORD ***********/
        static final String FTP_PASS  ="240694h2vwcc7";

        @Override
        protected Boolean doInBackground(String... params) {

            FTPClient client = new FTPClient();
            try {
                // Simulate network access.
                client.connect(FTP_HOST,21);
                client.login(FTP_USER, FTP_PASS);
                client.setType(FTPClient.TYPE_BINARY);
                client.changeDirectory("/htdocs/");

                //Est ce que la coloc existe ?
                File file = new File(params[0],mColocation+".txt");
                client.download(mColocation+".txt",file);

            } catch (Exception e) {
                e.printStackTrace();
                try {
                    //Si impossibilité de trouver le fichier c'est que la Coloc n'existe pas.
                    //Ou pas de connection aux serveur

                    SingletonColoc singletonColoc=SingletonColoc.getInstance();
                    singletonColoc.setNomColoc(mColocation);
                    //Log.d("mail", mEmail);
                    singletonColoc.getMails().add(mEmail);
                    singletonColoc.getMdps().add(mPassword);

                    String jsonContent= singletonColoc.objectToJson();
                    try {
                        File file = writeJsonInFile(jsonContent, mColocation);
                        client.upload(file);
                    }catch (Exception e3){
                        e3.printStackTrace();
                        try {
                            client.disconnect(true);
                        } catch (Exception e2){
                            e2.printStackTrace();
                        }
                    }

                } catch (Exception e4) {
                    e4.printStackTrace();
                }
            }
            try {
                String colocExistante = getStringFromFile(params[0]+"/"+mColocation+".txt");
                SingletonColoc singletonColoc=SingletonColoc.getInstance().jsonToObject(colocExistante);
                singletonColoc.getMails().add(mEmail);
                singletonColoc.getMdps().add(mPassword);

                String jsonContent = singletonColoc.objectToJson();

                try {
                    File file = writeJsonInFile(jsonContent,mColocation);
                    client.upload(file);
                }catch (Exception e1){
                    e1.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success == false) {
                return;

            } else {
                return;
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    public static String getStringFromFile (String filePath) throws Exception {
        File fl = new File(filePath);
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();
        return ret;
    }

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }


    public File writeJsonInFile(String jsonContent, String colocName) {
        BufferedWriter writer = null;
        File dir;
        dir = getDir("Test", MODE_PRIVATE);
        try {
            if (!dir.exists()) {
                dir.mkdir(); // On crée le répertoire (s'il n'existe
                // pas!!)
            }

            File newfile = new File(dir.getAbsolutePath() + File.separator
                    +colocName +".txt");
// Création du fichier
            newfile.createNewFile();
// Intégration du contenu dans un BufferedWriter
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(newfile)));
            writer.write(jsonContent);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return new File(dir.getAbsolutePath()+File.separator+colocName+".txt");
    }

}



