package cn.cbsd.dogtag;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cbsd.dogtag.Data.DogMessageBean;
import cn.cbsd.dogtag.Data.DogViolationBean;
import cn.cbsd.dogtag.Helper.PermissionHelper;
import cn.cbsd.dogtag.Tools.CropImageUtils;
import cn.cbsd.dogtag.Tools.FileUtils;
import cn.cbsd.dogtag.greendao.DaoSession;

public class FillViolationsActivity extends BaseToolBarActivity {

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private enum Operation {
        add, update, view
    }

    private Operation mOperation;

    DaoSession mdaosession = AppInit.getInstance().getDaoSession();


    String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    private CropImageUtils cropImageUtils;

    DogMessageBean dogMessageBean = null;

    DogViolationBean dogViolationBean = null;

    @BindView(R.id.et_personName)
    EditText et_personName;

    @BindView(R.id.scrollView)
    ScrollView mScrollView;

    @BindView(R.id.et_personId)
    EditText et_personId;

    @BindView(R.id.et_dogName)
    EditText et_dogName;

    @BindView(R.id.et_violation_message)
    EditText et_violation_message;

    @BindView(R.id.et_result)
    EditText et_result;

    @BindView(R.id.btn_camera_add)
    Button btn_camera_add;

    @BindView(R.id.btn_camera_left)
    Button btn_camera_left;

    @BindView(R.id.btn_camera_right)
    Button btn_camera_right;

    @BindView(R.id.btn_camera_delete)
    Button btn_camera_delete;

    @BindView(R.id.iv_violation)
    ImageView iv_violation;

    private List<String> violationBitmap = new ArrayList<>();

    private int pic_index = -1;

    @BindView(R.id.tv_submit)
    TextView tv_sumbit;

    @OnClick(R.id.tv_submit)
    void submit() {
        if (mOperation == Operation.add) {
            if (TextUtils.isEmpty(et_violation_message.getText().toString())) {
                ToastUtils.showLong("违规情况尚未填写,该条记录无法上传");
            } else {
                dogViolationBean = new DogViolationBean();
                dogViolationBean.setPersonId(dogMessageBean.getPersonId());
                dogViolationBean.setPersonName(dogMessageBean.getPersonName());
                dogViolationBean.setDogName(dogMessageBean.getDogName());
                dogViolationBean.setDogTag(dogMessageBean.getDogTagNum());
                dogViolationBean.setViolation_message(et_violation_message.getText().toString());
                dogViolationBean.setBitmaps(violationBitmap);
                if (TextUtils.isEmpty(et_result.getText().toString())) {
                    dogViolationBean.setDealStatus("待处理");
                } else {
                    dogViolationBean.setDealStatus("已处理");
                    dogViolationBean.setDealContent(et_result.getText().toString());
                }
                dogViolationBean.setDate(formatter.format(new Date(System.currentTimeMillis())));
                mdaosession.insert(dogViolationBean);
                Bundle bundle = new Bundle();
                bundle.putString("violationID", String.valueOf(dogViolationBean.getId()));
                ActivityUtils.startActivity(bundle, getPackageName(), getPackageName() + ".ViolationSearchActivity");
                this.finish();
            }
        } else if (mOperation == Operation.update) {
            if (TextUtils.isEmpty(et_result.getText().toString())) {
                ToastUtils.showLong("处理结果尚未填写,该条记录无法上传");
            } else {
                dogViolationBean.setPersonId(dogViolationBean.getPersonId());
                dogViolationBean.setPersonName(dogViolationBean.getPersonName());
                dogViolationBean.setDogName(dogViolationBean.getDogName());
                dogViolationBean.setDogTag(dogViolationBean.getDogTag());
                dogViolationBean.setViolation_message(et_violation_message.getText().toString());
                dogViolationBean.setDealStatus("已处理");
                dogViolationBean.setDealContent(et_result.getText().toString());
                mdaosession.update(dogViolationBean);
                Bundle bundle = new Bundle();
                bundle.putString("violationID", String.valueOf(dogViolationBean.getId()));
                ActivityUtils.startActivity(bundle, getPackageName(), getPackageName() + ".ViolationSearchActivity");
                this.finish();

            }
        }
    }

    @OnClick(R.id.btn_camera_add)
    void camera_add() {
        cropImageUtils.takePhoto();
    }

    @OnClick(R.id.btn_camera_left)
    void camera_left() {
        if (pic_index - 1 >= 0) {
            Glide.with(FillViolationsActivity.this)
                    .load(Base64.decode(violationBitmap.get(pic_index - 1), Base64.DEFAULT))
                    .into(iv_violation);
            pic_index -= 1;
        }
    }

    @OnClick(R.id.btn_camera_right)
    void camera_right() {
        if (pic_index + 1 < violationBitmap.size()) {
            Glide.with(FillViolationsActivity.this)
                    .load(Base64.decode(violationBitmap.get(pic_index + 1), Base64.DEFAULT))
                    .into(iv_violation);
            pic_index += 1;
        }

    }

    @OnClick(R.id.btn_camera_delete)
    void camera_delete() {
        File file = new File(violationBitmap.get(pic_index));
        if (file.exists()) {
            file.delete();
        }
        violationBitmap.remove(pic_index);
        if (pic_index == 0) {
            if (violationBitmap.size() != 0) {
                Glide.with(FillViolationsActivity.this)
                        .load(Base64.decode(violationBitmap.get(pic_index), Base64.DEFAULT))
                        .into(iv_violation);
            } else {
                violationBitmap.clear();
                pic_index -= 1;
                Glide.with(FillViolationsActivity.this)
                        .load("")
                        .into(iv_violation);
                btn_camera_left.setEnabled(false);
                btn_camera_right.setEnabled(false);
                btn_camera_delete.setEnabled(false);
            }
        } else {
            pic_index -= 1;
            Glide.with(FillViolationsActivity.this)
                    .load(Base64.decode(violationBitmap.get(pic_index), Base64.DEFAULT))
                    .into(iv_violation);
        }


    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_violations);
        ButterKnife.bind(this);
        cropImageUtils = new CropImageUtils(this);
        DataPrepare();
        PermissionHelper.getInstance().requestRunPermisssion(this, permissions, new PermissionHelper.PermissionListener() {
            @Override
            public void onGranted() {
            }

            @Override
            public void onDenied(List<String> deniedPermission) {

            }
        });
    }

    private void DataPrepare() {
        Intent intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                if (bundle.get("violationMessageType").equals("add")) {
                    mOperation = Operation.add;
                    dogMessageBean = mdaosession.queryRaw(DogMessageBean.class, "where DOG_TAG_NUM = '" + bundle.get("dogTagNum") + "'").get(0);
                    et_personName.setText(dogMessageBean.getPersonName());
                    et_personId.setText(dogMessageBean.getPersonId());
                    et_dogName.setText(dogMessageBean.getDogName());
                    btn_camera_left.setEnabled(false);
                    btn_camera_right.setEnabled(false);
                    btn_camera_delete.setEnabled(false);
                } else if (bundle.get("violationMessageType").equals("update")) {
                    mOperation = Operation.update;
                    dogViolationBean = mdaosession.queryRaw(DogViolationBean.class, "where _id = '" + bundle.get("violationID") + "'").get(0);
                    et_personName.setText(dogViolationBean.getPersonName());
                    et_personId.setText(dogViolationBean.getPersonId());
                    et_dogName.setText(dogViolationBean.getDogName());
                    et_violation_message.setText(dogViolationBean.getViolation_message());
                    violationBitmap = dogViolationBean.getBitmaps();
                    if (violationBitmap.size() <= 0) {
                        btn_camera_left.setVisibility(View.GONE);
                        btn_camera_right.setVisibility(View.GONE);
                    } else {
                        pic_index++;
                        Glide.with(FillViolationsActivity.this)
                                .load(Base64.decode(violationBitmap.get(pic_index), Base64.DEFAULT))
                                .into(iv_violation);

                    }
                    et_violation_message.setEnabled(false);
                    btn_camera_add.setVisibility(View.GONE);
                    btn_camera_delete.setVisibility(View.GONE);
                } else if (bundle.get("violationMessageType").equals("view")) {
                    mOperation = Operation.view;
                    dogViolationBean = mdaosession.queryRaw(DogViolationBean.class, "where _id = '" + bundle.get("violationID") + "'").get(0);
                    et_personName.setText(dogViolationBean.getPersonName());
                    et_personId.setText(dogViolationBean.getPersonId());
                    et_dogName.setText(dogViolationBean.getDogName());
                    et_violation_message.setText(dogViolationBean.getViolation_message());
                    et_result.setText(dogViolationBean.getDealContent());
                    violationBitmap = dogViolationBean.getBitmaps();

                    if (violationBitmap.size() <= 0) {
                        btn_camera_left.setVisibility(View.GONE);
                        btn_camera_right.setVisibility(View.GONE);
                    } else {
                        pic_index++;
                        Glide.with(FillViolationsActivity.this)
                                .load(Base64.decode(violationBitmap.get(pic_index), Base64.DEFAULT))
                                .into(iv_violation);
                    }
                    et_violation_message.setEnabled(false);
                    et_result.setEnabled(false);
                    btn_camera_add.setVisibility(View.GONE);
                    btn_camera_delete.setVisibility(View.GONE);
                    tv_sumbit.setVisibility(View.GONE);
                }
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        cropImageUtils.onActivityResult(requestCode, resultCode, data, new CropImageUtils.OnResultListener() {
            @Override
            public void takePhotoFinish(String bitmapBase64) {
                violationBitmap.add(bitmapBase64);
                pic_index = violationBitmap.size() - 1;
                Glide.with(FillViolationsActivity.this)
                        .load(Base64.decode(violationBitmap.get(pic_index), Base64.DEFAULT))
                        .into(iv_violation);
                ScrollViewDown();

                btn_camera_left.setEnabled(true);
                btn_camera_right.setEnabled(true);
                btn_camera_delete.setEnabled(true);
            }

            @Override
            public void selectPictureFinish(String path) {
            }

            @Override
            public void cropPictureFinish(String path) {

            }
        });
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_fill_violations;
    }


    private void ScrollViewDown() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

    }

    @Override
    public void onBackPressed() {
        this.finish();
        if(mOperation !=Operation.add){
            ActivityUtils.startActivity(getPackageName(), getPackageName() + ".ViolationSearchActivity");
        }else{
            ActivityUtils.startActivity(getPackageName(), getPackageName() + ".MainActivity");

        }

    }
}
