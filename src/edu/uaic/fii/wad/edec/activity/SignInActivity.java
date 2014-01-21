package edu.uaic.fii.wad.edec.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.plus.PlusClient;
import edu.uaic.fii.wad.edec.R;
import edu.uaic.fii.wad.edec.service.util.Token;

import static com.google.android.gms.common.Scopes.PLUS_LOGIN;
import static com.google.android.gms.common.Scopes.PLUS_PROFILE;

/**
 * Catalin Dumitru
 * Date: 1/20/14
 * Time: 9:17 PM
 */
public class SignInActivity extends Activity implements View.OnClickListener, GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    public static final int REQUEST_CODE_RESOLVE_ERR = 9000;
    public static final String TAG = "EDEC";

    private ProgressDialog progressDialog;
    private PlusClient plusClient;
    private ConnectionResult result;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        plusClient = new PlusClient.Builder(this, this, this)
                .setActions("http://schemas.google.com/AddActivity", "http://schemas.google.com/BuyActivity")
                .setScopes(PLUS_LOGIN, PLUS_PROFILE)
                .build();

        findViewById(R.id.sign_in_button)
                .setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Hold your shit...");
    }

    @Override
    protected void onStart() {
        super.onStart();

        plusClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

        plusClient.disconnect();
    }

    @Override
    public void onClick(View view) {
        if (result == null) {
            progressDialog.show();
        } else {
            try {
                result.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
            } catch (IntentSender.SendIntentException e) {
                // Try connecting again.
                result = null;
                plusClient.connect();
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
        progressDialog.show();

        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    Token.CURRENT = GoogleAuthUtil.getToken(SignInActivity.this, plusClient.getAccountName(), "oauth2:" + PLUS_PROFILE + " " + PLUS_LOGIN);
                } catch (UserRecoverableAuthException transientEx) {
                    startActivity(transientEx.getIntent());
                } catch (Exception transientEx) {
                    Log.e(TAG, transientEx.toString());
                }

                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);

                return null;
            }

            @Override
            protected void onPostExecute(String token) {
                Log.i(TAG, "Access token retrieved:" + token);
            }

        };
        task.execute();


    }

    @Override
    public void onDisconnected() {
        //todo
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (progressDialog.isShowing()) {
            if (connectionResult.hasResolution()) {
                try {
                    result.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
                } catch (IntentSender.SendIntentException e) {
                    plusClient.connect();
                }
            }
        }
        result = connectionResult;
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == REQUEST_CODE_RESOLVE_ERR && responseCode == RESULT_OK) {
            result = null;
            plusClient.connect();
        }
    }

}