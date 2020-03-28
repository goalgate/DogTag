package cn.cbsd.dogtag;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.widget.EditText;

import com.blankj.utilcode.util.ActivityUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cbsd.dogtag.Data.DogMessageBean;
import cn.cbsd.dogtag.UI.DogPicAdapter;
import cn.cbsd.dogtag.UI.DogPicListView;
import cn.cbsd.dogtag.greendao.DaoSession;

public class MessagePageActivity extends BaseToolBarActivity {

    private static String TAG = MessagePageActivity.class.getSimpleName();

    DaoSession mdaosession = AppInit.getInstance().getDaoSession();

    DogMessageBean dogMessageBean = null;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//注意月份是MM


    @BindView(R.id.et_personName)
    EditText et_personName;

    @BindView(R.id.et_personId)
    EditText et_personId;

    @BindView(R.id.et_address)
    EditText et_address;

    @BindView(R.id.et_dogName)
    EditText et_dogName;

    @BindView(R.id.et_dogType)
    EditText et_dogType;

    @BindView(R.id.et_dogTagNum)
    EditText et_dogTagNum;

    @BindView(R.id.et_startDate)
    EditText et_startDate;

    @BindView(R.id.et_stopDate)
    EditText et_stopDate;

    @BindView(R.id.lv_dog_Pic)
    DogPicListView lv_dog_Pic;

    @OnClick(R.id.tv_violations) void violations(){
        Bundle bundle = new Bundle();
        bundle.putString("violationMessageType","add");
        bundle.putString("dogTagNum",dogMessageBean.getDogTagNum());
        ActivityUtils.startActivity(bundle,getPackageName(), getPackageName() + ".FillViolationsActivity");

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_page);
        ButterKnife.bind(this);
        DataPrepare();
    }

    private void DataPrepare() {
        Intent intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                if (bundle.get("dogMessageType").equals("NFC")) {
                    dogMessageBean = mdaosession.queryRaw(DogMessageBean.class, "where DOG_TAG_NUM = '" + bundle.get("dogTagNum") + "'").get(0);
                } else if (bundle.get("dogMessageType").equals("QRCODE")) {
                    dogMessageBean = mdaosession.queryRaw(DogMessageBean.class, "where DOG_TAG_QRCODE = '" + bundle.get("result") + "'").get(0);
                }else if(bundle.get("dogMessageType").equals("HANDWRITING")){
                    dogMessageBean = mdaosession.queryRaw(DogMessageBean.class, "where DOG_TAG_NUM = '" + bundle.get("dogTagNum") + "'").get(0);
                }
                et_personName.setText(dogMessageBean.getPersonName());
                et_personName.setEnabled(false);
                et_personId.setText(dogMessageBean.getPersonId());
                et_personId.setEnabled(false);
                et_address.setText(dogMessageBean.getAddress());
                et_address.setEnabled(false);
                et_dogName.setText(dogMessageBean.getDogName());
                et_dogName.setEnabled(false);
                et_dogType.setText(dogMessageBean.getDogType());
                et_dogType.setEnabled(false);
                et_dogTagNum.setText(dogMessageBean.getDogTagNum());
                et_dogTagNum.setEnabled(false);
                et_startDate.setText(dogMessageBean.getStartDate());
                et_startDate.setEnabled(false);


                Integer intColor = et_startDate.getCurrentTextColor();
                String hexColor = "#" + Integer.toHexString(intColor).substring(2);
                Log.e("color",hexColor);
                DogPicAdapter dogPicAdapter = new DogPicAdapter(this, R.layout.adapter_dog_pic, dogMessageBean.getBitmaps());
                lv_dog_Pic.setAdapter(dogPicAdapter);
                try {
                    Date stopDate = simpleDateFormat.parse(dogMessageBean.getStopDate());
                    if (stopDate.getTime() - System.currentTimeMillis() > 0) {
                        String content = "<font color=\"#808080\"> " + dogMessageBean.getStopDate() + "</font>" + "<font color=\"#008B00\"> (该证未过有效期)</font>";
                        et_stopDate.setText(Html.fromHtml(content));
                    } else {
                        String content = "<font color=\"#808080\"> " + dogMessageBean.getStopDate() + "</font>" + "<font color=\"#FF0000\"> (该证已过有效期)</font>";
                        et_stopDate.setText(Html.fromHtml(content));
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                et_stopDate.setEnabled(false);

            }

        }
    }



    @Override
    protected int getLayoutId() {
        return R.layout.activity_message_page;
    }
}
