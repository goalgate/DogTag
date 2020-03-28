package cn.cbsd.dogtag;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuAdapter;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.cbsd.dogtag.Data.DogMessageBean;
import cn.cbsd.dogtag.UI.MessageUnitAdapter;
import cn.cbsd.dogtag.greendao.DaoSession;

public class MessageSearchActivity extends BaseNFCActivity {

    private String TAG = MessageSearchActivity.class.getSimpleName();

    Handler handler = new Handler();

    DaoSession mdaosession = AppInit.getInstance().getDaoSession();

    List<DogMessageBean> dogMessageBeanList = new ArrayList<>();

    Set<DogMessageBean> all = new HashSet<>();

    MessageUnitAdapter messageUnitAdapter;


    @BindView(R.id.id_recyclerview)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initToolBar();
        recycleViewInit();
    }

    private void recycleViewInit() {
        messageUnitAdapter = new MessageUnitAdapter(this, dogMessageBeanList);
        mRecyclerView.setAdapter(messageUnitAdapter);
        messageUnitAdapter.setOnItemClickListener(new MessageUnitAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                DogMessageBean bean = dogMessageBeanList.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("dogMessageType","HANDWRITING");
                bundle.putString("dogTagNum",bean.getDogTagNum());
                ActivityUtils.startActivity(bundle,getPackageName(), getPackageName() + ".MessagePageActivity");
                MessageSearchActivity.this.finish();
            }
        });
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
                Toast.makeText(MessageSearchActivity.this, "信息已全部搜索完毕", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                if (TextUtils.isEmpty(newText) || newText.startsWith("*")) {
                    all.clear();
                    dogMessageBeanList.clear();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            messageUnitAdapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    all.clear();
                    dogMessageBeanList.clear();
                    List<DogMessageBean> listByPersonName = mdaosession.queryRaw(DogMessageBean.class, "where PERSON_NAME like '%" + newText + "%'");
                    List<DogMessageBean> listByDogName = mdaosession.queryRaw(DogMessageBean.class, "where Dog_Name like '%" + newText + "%'");
                    Log.e("listByPersonName", String.valueOf(listByPersonName.size()));
                    Log.e("listByDogName", String.valueOf(listByDogName.size()));
                    all.addAll(listByPersonName);
                    all.addAll(listByDogName);
                    dogMessageBeanList.addAll(all);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            messageUnitAdapter.notifyDataSetChanged();
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
        super.onBackPressed();
    }
}
