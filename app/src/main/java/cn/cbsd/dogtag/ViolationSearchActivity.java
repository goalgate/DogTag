package cn.cbsd.dogtag;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.ActivityUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.cbsd.dogtag.Data.DogViolationBean;
import cn.cbsd.dogtag.UI.ViolationUnitAdapter;
import cn.cbsd.dogtag.greendao.DaoSession;

public class ViolationSearchActivity extends BaseNFCActivity {

    private String TAG = MessageSearchActivity.class.getSimpleName();

    Handler handler = new Handler();

    DaoSession mdaosession = AppInit.getInstance().getDaoSession();

    List<DogViolationBean> dogViolationBeans = new ArrayList<>();

    Set<DogViolationBean> all = new HashSet<>();

    ViolationUnitAdapter violationUnitAdapter;

    @BindView(R.id.id_recyclerview)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initToolBar();
        DataPrepare();
        recycleViewInit();
    }

    private void DataPrepare() {
        Intent intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Bundle bundle = intent.getExtras();
            if (bundle.get("violationID") != null) {
                try {
                    DogViolationBean bean = mdaosession.queryRaw(DogViolationBean.class, "where _id = " + bundle.get("violationID")).get(0);
                    dogViolationBeans.add(bean);
                }catch (Exception e){
                    Log.e(TAG,e.toString());
                }

            }
        }
    }

    private void recycleViewInit() {
        violationUnitAdapter = new ViolationUnitAdapter(this, dogViolationBeans);
        mRecyclerView.setAdapter(violationUnitAdapter);
        violationUnitAdapter.setOnItemClickListener(new ViolationUnitAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {

            }
        });
        violationUnitAdapter.notifyDataSetChanged();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconified(false);
//        searchView.setIconifiedByDefault(false);
        searchView.onActionViewExpanded();
        searchView.setQueryHint("请输入关键信息");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(ViolationSearchActivity.this, "信息已全部搜索完毕", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                if (TextUtils.isEmpty(newText) || newText.startsWith("*")) {
                    all.clear();
                    dogViolationBeans.clear();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            violationUnitAdapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    all.clear();
                    dogViolationBeans.clear();
                    List<DogViolationBean> listByPersonName = mdaosession.queryRaw(DogViolationBean.class, "where PERSON_NAME like '%" + newText + "%'");
                    List<DogViolationBean> listByDogName = mdaosession.queryRaw(DogViolationBean.class, "where Dog_Name like '%" + newText + "%'");
                    Log.e("listByPersonName", String.valueOf(listByPersonName.size()));
                    Log.e("listByDogName", String.valueOf(listByDogName.size()));
                    all.addAll(listByPersonName);
                    all.addAll(listByDogName);
                    dogViolationBeans.addAll(all);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            violationUnitAdapter.notifyDataSetChanged();
                        }
                    });
                }
                return false;
            }
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        this.finish();
        ActivityUtils.startActivity(getPackageName(), getPackageName() + ".MainActivity");
    }
}
