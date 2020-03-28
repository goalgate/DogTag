package cn.cbsd.dogtag;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cbsd.dogtag.Data.DogMessageBean;
import cn.cbsd.dogtag.Helper.PermissionHelper;
import cn.cbsd.dogtag.Tools.ActivityCollector;
import cn.cbsd.dogtag.Tools.FileUtils;
import cn.cbsd.dogtag.greendao.DaoSession;

import static cn.cbsd.dogtag.Helper.PermissionHelper.PERMISSION_REQUESTCODE;


public class MainActivity extends BaseToolBarActivity {

    private static String TAG = MainActivity.class.getSimpleName();

    DaoSession mdaosession = AppInit.getInstance().getDaoSession();

    private SPUtils config = SPUtils.getInstance("config");

    String[] permissions = new String[]{
            Manifest.permission.CAMERA
    };

    @BindView(R.id.iv_nfc)
    ImageView iv_nfc;

    @BindView(R.id.iv_QRCode)
    ImageView iv_qrcode;

    @BindView(R.id.iv_handWrite)
    ImageView iv_handwrite;

    @OnClick(R.id.iv_QRCode)
    void qrcode() {
        PermissionHelper.getInstance().requestRunPermisssion(this, permissions, new PermissionHelper.PermissionListener() {
            @Override
            public void onGranted() {
                Intent openCameraIntent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
            }

            @Override
            public void onDenied(List<String> deniedPermission) {

            }
        });

    }

    @OnClick(R.id.iv_handWrite)
    void handWrite() {
        ActivityUtils.startActivity(getPackageName(), getPackageName() + ".MessageSearchActivity");
    }


    @OnClick(R.id.iv_nfc)
    void nfc() {
        ActivityUtils.startActivity(getPackageName(), getPackageName() + ".NFCWaitingActivity");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (config.getBoolean("firstStart", true)) {
            Bitmap labuladuo1 = BitmapFactory.decodeResource(getResources(), R.drawable.labuladuo1);
            Bitmap labuladuo2 = BitmapFactory.decodeResource(getResources(), R.drawable.labuladuo2);
            Bitmap labuladuo3 = BitmapFactory.decodeResource(getResources(), R.drawable.labuladuo3);

            String string_labuladuo1 = FileUtils.bitmapToBase64(labuladuo1);
            String string_labuladuo2 = FileUtils.bitmapToBase64(labuladuo2);
            String string_labuladuo3 = FileUtils.bitmapToBase64(labuladuo3);

            List<String> bitmaps = new ArrayList<>();
            bitmaps.add(string_labuladuo1);
            bitmaps.add(string_labuladuo2);
            bitmaps.add(string_labuladuo3);

            DogMessageBean dogMessageBean = new DogMessageBean();
            dogMessageBean.setPersonName("王振文");
            dogMessageBean.setPersonId("441302199308100538");
            dogMessageBean.setAddress("广东省广州市黄埔区");
            dogMessageBean.setDogName("皮皮");
            dogMessageBean.setDogType("拉布拉多");
            dogMessageBean.setDogTagNum("04537792FC6780");
            dogMessageBean.setStartDate("2019-03-05");
            dogMessageBean.setStopDate("2020-12-05");
            dogMessageBean.setDogTagQRCode("https://4legs.co/lnf/qrid=0c396a9f-802f-45f2-aaff-5b61a9e4870a");
            dogMessageBean.setBitmaps(bitmaps);
            AppInit.getInstance().getDaoSession().insertOrReplace(dogMessageBean);
            config.put("firstStart", false);

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                Bundle bundle = data.getExtras();
                DogMessageBean dogMessageBean = mdaosession.queryRaw(DogMessageBean.class, "where DOG_TAG_QRCODE = '" + bundle.get("result") + "'").get(0);
                bundle.putString("dogMessageType", "QRCODE");
                ActivityUtils.startActivity(bundle, getPackageName(), getPackageName() + ".MessagePageActivity");
            } catch (IndexOutOfBoundsException e) {
                ToastUtils.showLong("未能找到相关狗牌信息");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUESTCODE:
                PermissionHelper.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
            default:
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void setBackIcon() {
    }

    @Override
    public void onBackPressed() {
        ActivityCollector.finishAll();
    }
}
