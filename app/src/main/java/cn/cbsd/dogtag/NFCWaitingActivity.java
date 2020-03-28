package cn.cbsd.dogtag;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import com.blankj.utilcode.util.ActivityUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

import cn.cbsd.dogtag.Data.DogMessageBean;
import cn.cbsd.dogtag.UI.SearchIcon;
import cn.cbsd.dogtag.greendao.DaoSession;


public class NFCWaitingActivity extends BaseToolBarActivity {

    private static String TAG = NFCWaitingActivity.class.getSimpleName();

    DaoSession mdaosession = AppInit.getInstance().getDaoSession();

    @BindView(R.id.searchIcon)
    SearchIcon searchIcon;

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_waiting);
        ButterKnife.bind(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        searchIcon.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Toast.makeText(this, "该标签不符合规格", Toast.LENGTH_LONG).show();
        } else if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            byte[] myNFCID = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
            try{
                DogMessageBean dogMessageBean = mdaosession.queryRaw(DogMessageBean.class, "where DOG_TAG_NUM = '" + bytesToHexString(myNFCID).toUpperCase() + "'").get(0);
                Bundle bundle = new Bundle();
                bundle.putString("dogMessageType","NFC");
                bundle.putString("dogTagNum",bytesToHexString(myNFCID).toUpperCase());
                ActivityUtils.startActivity(bundle,getPackageName(), getPackageName() + ".MessagePageActivity");
            }catch (Exception e){
                Toast.makeText(this, "未能查到相关信息", Toast.LENGTH_LONG).show();
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
            Toast.makeText(this, "该标签不符合规格", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_nfc_waiting;
    }
}
