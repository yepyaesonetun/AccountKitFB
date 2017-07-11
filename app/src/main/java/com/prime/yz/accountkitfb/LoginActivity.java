package com.prime.yz.accountkitfb;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.accountkit.ui.SkinManager;
import com.facebook.accountkit.ui.SkinManager.Tint;
import com.facebook.accountkit.ui.UIManager;

import butterknife.BindView;

import static android.R.attr.drawable;
import static android.R.attr.tint;

public class LoginActivity extends AppCompatActivity {

    public static int APP_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // check for an existing access token
        AccessToken accessToken = AccountKit.getCurrentAccessToken();
        if (accessToken != null) {
            // if previously logged in, proceed to the account activity
            launchAccountActivity();
        }

//        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder;
//        UIManager uiManager;
//        final @ColorInt int color = ContextCompat.getColor(this, R.color.ak_buttonBackgorundColor);
//        Drawable drawable1= ContextCompat.getDrawable(this, R.drawable.bollon);
//        double d = 55;
//
//        uiManager = new SkinManager(
//                SkinManager.Skin.TRANSLUCENT,
//                color,
//                @DrawableRes int <R.drawable.bollon>,
//                Tint.BLACK,
//                d);
//
//        configurationBuilder.setUIManager(uiManager);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // confirm that this response matches your request
        if (requestCode == APP_REQUEST_CODE) {
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if (loginResult.getError() != null) {
                // display login error
                String toastMessage = loginResult.getError().getErrorType().getMessage();
                Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
            } else if (loginResult.getAccessToken() != null) {
                // on successful login, proceed to the account activity
                launchAccountActivity();
            }
        }
    }

    private void onLogin(final LoginType loginType) {
        // create intent for the Account Kit Activity
        final Intent intent = new Intent(this, AccountKitActivity.class);

        // configure login type and response type
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        loginType,
                        AccountKitActivity.ResponseType.TOKEN
                );

        final AccountKitConfiguration configuration = configurationBuilder.build();

        // launch AccountKit Activity
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configuration);
        startActivityForResult(intent, APP_REQUEST_CODE);
    }


    public void onPhoneLogin(View view) {
        onLogin(LoginType.PHONE);
    }

    public void onEmailLogin(View view) {
        onLogin(LoginType.EMAIL);
    }


    private void launchAccountActivity() {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
        finish();
    }
}
