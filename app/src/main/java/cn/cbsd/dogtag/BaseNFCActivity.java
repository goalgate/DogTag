package cn.cbsd.dogtag;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import cn.cbsd.dogtag.Tools.ActivityCollector;

public abstract class BaseNFCActivity extends AppCompatActivity {

    protected NfcAdapter mNfcAdapter = null;//1.声明一个nfc的Adapter

    protected PendingIntent mPendingIntent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NfcCheck();
        SetPendingIntent();
        ActivityCollector.addActivity(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mNfcAdapter != null) {
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);

    }

    private void NfcCheck() {
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);//3.获取nfc适配器
        if (mNfcAdapter == null) {
            return;
        } else {
            if (!mNfcAdapter.isEnabled()) {
                Intent setNfc = new Intent(Settings.ACTION_NFC_SETTINGS);
                startActivity(setNfc);
            }
        }
    }

    protected String getNDEFMessage(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        // only one message sent during the beam
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        // record 0 contains the MIME type, record 1 is the AAR, if present
        return new String(msg.getRecords()[0].getPayload());
    }


    protected String bytesToHexString(byte[] src) {

        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);

            stringBuilder.append(buffer);
//            stringBuilder.append("");
        }
        return stringBuilder.toString();
    }


    public void SetPendingIntent(){
        Intent intent = new Intent(this, getClass());
        mPendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

    }




}
