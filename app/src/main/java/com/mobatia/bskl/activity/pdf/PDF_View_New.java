package com.mobatia.bskl.activity.pdf;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.mobatia.bskl.R;
import com.mobatia.bskl.manager.AppUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PDF_View_New extends AppCompatActivity {

    PDFView pdf;
    ImageView ShareBtn,DownloadBtn,BackBtn;
    Bundle Extras;
    String Url;
    Context mContext;
    ProgressDialog mProgressDialog;
    String name = "BSKL";
    String fileData="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf__view__new);
        getSupportActionBar().hide();

        mContext = this;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); StrictMode.setVmPolicy(builder.build());
        Extras = getIntent().getExtras();
        if (Extras != null){
            Url = Extras.getString("pdf_url");
            fileData = Extras.getString("fileName");
        }
        Log.d("PDF_URL",Url);

        InitUi();

    }

    private void InitUi() {
        pdf = findViewById(R.id.Csutom_pdfView);
        ShareBtn = findViewById(R.id.PDFshared);
        DownloadBtn = findViewById(R.id.PDFdownload);
        BackBtn = findViewById(R.id.PDF_GoBack);

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        ShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (AppUtils.isNetworkConnected(mContext)) {
                    Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                    File fileWithinMyDir = new File(getFilepath(fileData+".pdf"));
                    if(fileWithinMyDir.exists()) {
                        intentShareFile.setType("application/pdf");
                        intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+getFilepath(fileData+".pdf")));
                        intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "Sharing File...");
                        intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");
                        startActivity(Intent.createChooser(intentShareFile, "Share File"));
                    }
                } else {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
                }
            }
        });

        DownloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppUtils.isNetworkConnected(mContext)) {
                    mProgressDialog = new ProgressDialog(PDF_View_New.this);
                    mProgressDialog.setMessage("Downloading..");
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mProgressDialog.setCancelable(true);
                    new DownloadPDF().execute();
                    mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            new DownloadPDF().cancel(true); //cancel the task
                        }
                    });
                } else {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
                }
            }
        });

        PermissionListener permissionListenerGallery = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                if (AppUtils.isNetworkConnected(mContext)) {
                    new loadPDF().execute();
                } else {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
                }

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(mContext, "Permission Denied\n", Toast.LENGTH_SHORT).show();
            }


        };
        TedPermission.with(mContext)
                .setPermissionListener(permissionListenerGallery)
                .setDeniedMessage("If you reject permission,you cannot use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();

    }

    public class loadPDF extends AsyncTask<String, Void, Void> {

        private Exception exception;
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(PDF_View_New.this);
            //dialog.setMessage(getResources().getString(R.string.pleasewait));//Please wait...
            //
            // dialog.show();
        }

        protected Void doInBackground(String... urls) {
            URL u = null;
            try {
                String fileName = fileData+".pdf";
                u = new URL(Url);
                HttpURLConnection c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod("GET");
                // c.setDoOutput(true);


                String auth = "SGHCXFTPUser" + ":" + "cXFTPu$3r";
                String encodedAuth = new String(Base64.encodeToString(auth.getBytes(), Base64.DEFAULT));
                encodedAuth = encodedAuth.replace("\n", "");

                c.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                c.addRequestProperty("Authorization", "Basic " + encodedAuth);
                //c.setRequestProperty("Accept", "application/json");
                // c.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                c.connect();

                int response = c.getResponseCode();
                String PATH = Environment.getExternalStorageDirectory()
                        + "/download/";
                // Log.d("Abhan", "PATH: " + PATH);
                File file = new File(PATH);
                if (!file.exists()) {
                    file.mkdirs();
                }
                File outputFile = new File(file, fileName);
                FileOutputStream fos = new FileOutputStream(outputFile);
                InputStream is = c.getInputStream();
                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                }
                fos.flush();
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/" + fileData+".pdf");
            Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/" + fileData+".pdf");
            // System.out.println("file.exists() = " + file.exists());
            // pdf.fromUri(uri);

            pdf.fromFile(file).defaultPage(0).enableSwipe(true).load();

            //web.loadUrl(Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/" + "test.pdf");
            // Toast.makeText(MainActivity.this, "Completed", Toast.LENGTH_LONG).show();
        }

    }

    public class DownloadPDF extends AsyncTask<String, Void, Void> {

        private Exception exception;
        private ProgressDialog dialog;
        String filename = name.replace(" ", "_");
        String fileName = filename + ".pdf";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(PDF_View_New.this);
            dialog.setMessage(getResources().getString(R.string.pleasewait));//Please wait...
            dialog.show();
        }

        protected Void doInBackground(String... urls) {
            URL u = null;
            try {

                u = new URL(Url);
                HttpURLConnection c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod("GET");
                // c.setDoOutput(true);


                String auth = "SGHCXFTPUser" + ":" + "cXFTPu$3r";
                String encodedAuth = new String(Base64.encodeToString(auth.getBytes(), Base64.DEFAULT));
                encodedAuth = encodedAuth.replace("\n", "");

                c.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                c.addRequestProperty("Authorization", "Basic " + encodedAuth);
                //c.setRequestProperty("Accept", "application/json");
                // c.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                c.connect();

                int response = c.getResponseCode();
                String PATH = Environment.getExternalStorageDirectory()
                        + "/download/";
                // Log.d("Abhan", "PATH: " + PATH);
                File file = new File(PATH);
                if (!file.exists()) {
                    file.mkdirs();
                }
                File outputFile = new File(file, fileName);
                FileOutputStream fos = new FileOutputStream(outputFile);
                InputStream is = c.getInputStream();
                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                }
                fos.flush();
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/" + fileName);
            Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/" + fileName);
            System.out.println("file.exists() = " + file.exists());
            if (file.exists()) {
                Toast.makeText(mContext, "File Downloaded to Downloads folder", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "Something Went Wrong. Download failed", Toast.LENGTH_SHORT).show();
            }
            // pdf.fromUri(uri);

            // pdf.fromFile(file).defaultPage(1).enableSwipe(true).load();

            //web.loadUrl(Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/" + "test.pdf");
            // Toast.makeText(MainActivity.this, "Completed", Toast.LENGTH_LONG).show();
        }


    }

    private String getFilepath(String filename) {

        return new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Download/" + filename).getPath();

    }

}
