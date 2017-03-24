package com.pondthaitay.deeplink_example;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;

public class SchemeActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(AppInvite.API)
                .build();

        AppInvite.AppInviteApi.getInvitation(mGoogleApiClient, this, false)
                .setResultCallback(
                        new ResultCallback<AppInviteInvitationResult>() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onResult(@NonNull AppInviteInvitationResult result) {
                                if (result.getStatus().isSuccess()) {
                                    Intent intent = result.getInvitationIntent();
                                    Uri uri = intent.getData();
                                    String valueKeyUtmSource = uri.getQueryParameter("utm_source");
                                    String valueKeyUtmMedium = uri.getQueryParameter("utm_medium");
                                    String valueKeyUtmCampaign = uri.getQueryParameter("utm_campaign");
                                    String deepLink = AppInviteReferral.getDeepLink(intent);
                                    Log.d(TAG, "getInvitation: " + deepLink);
                                    Log.d(TAG, "valueKeyUtmSource: " + valueKeyUtmSource);
                                    Log.d(TAG, "valueKeyUtmMedium: " + valueKeyUtmMedium);
                                    Log.d(TAG, "valueKeyUtmCampaign: " + valueKeyUtmCampaign);
                                    if (valueKeyUtmCampaign.equals("main_activity")) {
                                        startActivity(new Intent(SchemeActivity.this, MainActivity.class));
                                        finish();
                                    } else if (valueKeyUtmCampaign.equals("detail_activity")) {
                                        startActivity(new Intent(SchemeActivity.this, DetailActivity.class));
                                        finish();
                                    }
                                } else {
                                    Log.d(TAG, "getInvitation: no deep link found.");
                                }
                            }
                        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, connectionResult.getErrorMessage());
    }
}