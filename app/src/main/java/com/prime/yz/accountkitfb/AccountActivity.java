package com.prime.yz.accountkitfb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.PhoneNumber;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountActivity extends AppCompatActivity {

    @BindView(R.id.id)
    TextView id;
    @BindView(R.id.info_label)
    TextView info_label;
    @BindView(R.id.info)
    TextView info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        ButterKnife.bind(this);

        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(Account account) {
                // get Account Kit ID
                String accountkitId = account.getId();
                id.setText(accountkitId);

                PhoneNumber phoneNumber = account.getPhoneNumber();
                if (account.getPhoneNumber() != null){
                    // if the phone number is available, display it is
                    String formattedPhoneNumber = formatPhoneNumber(phoneNumber.toString());
                    info.setText(formattedPhoneNumber);
                    info_label.setText(R.string.phone_label);
                }else {
                    // if the email address is available, display it is
                    String emailString = account.getEmail();
                    info.setText(emailString);
                    info_label.setText(R.string.email_label);
                }
            }

            @Override
            public void onError(AccountKitError accountKitError) {
                // display error
                String toastMessage = accountKitError.getErrorType().getMessage();
                Toast.makeText(AccountActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void onLogout(View view){
        AccountKit.logOut();
        launchLoginActivity();
    }

    private void launchLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    private String formatPhoneNumber(String phoneNumber){
        // helper method to format the phone number for display
        try {
            PhoneNumberUtil pnu = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber pn = pnu.parse(phoneNumber, Locale.getDefault().getCountry());
            phoneNumber = pnu.format(pn, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
        } catch (NumberParseException e) {
            e.printStackTrace();
        }
        return phoneNumber;
    }
}
