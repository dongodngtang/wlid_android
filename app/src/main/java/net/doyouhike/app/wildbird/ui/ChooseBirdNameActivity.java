package net.doyouhike.app.wildbird.ui;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;

import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.service.database.WildbirdDbService;
import net.doyouhike.app.wildbird.ui.adapter.AutoEditAdapter;
import net.doyouhike.app.wildbird.biz.model.bean.SpeciesInfo;
import net.doyouhike.app.wildbird.ui.base.BaseAppActivity;
import net.doyouhike.app.wildbird.ui.view.LineEditText;
import net.doyouhike.app.wildbird.ui.view.LineEditText.SearchListener;

import java.util.List;

public class ChooseBirdNameActivity extends BaseAppActivity implements SearchListener {

    private ImageView name_back;
    private LineEditText bird_name;
    private GridView autoedit;
    private List<SpeciesInfo> list;
    private AutoEditAdapter adapter;
    private boolean moreAuto = false;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.birdname_view;
    }

    @Override
    protected void initViewsAndEvents() {
        super.initViewsAndEvents();

        name_back = (ImageView) super.findViewById(R.id.name_back);
        bird_name = (LineEditText) super.findViewById(R.id.bird_name);
        autoedit = (GridView) super.findViewById(R.id.autoedit);

        bird_name.setListener(this);
        bird_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (!TextUtils.isEmpty(s)) {
                    bird_name.setClear();
                    autoEdit();
                } else {
                    bird_name.setGone();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        list = WildbirdDbService.getInstance().autoEdit("%", 0, 20);
        adapter = new AutoEditAdapter(getApplicationContext(), list);
        autoedit.setAdapter(adapter);
        autoedit.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub
                if (!moreAuto && totalItemCount > 0 && firstVisibleItem + visibleItemCount == totalItemCount) {
                    moreAuto = true;
                    String sel_name = "%" + bird_name.getText().toString().trim() + "%";

                    List<SpeciesInfo> list2 = WildbirdDbService.getInstance().autoEdit(sel_name, list.size(), 20);
                    list.addAll(list2);
                    adapter.notifyDataSetChanged();
                    moreAuto = false;
                    if (list2.size() < 20) {
                        moreAuto = true;
                    }
                }
            }
        });
        autoedit.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(bird_name.getWindowToken(), 0);
                Intent intent = new Intent();
                intent.putExtra("name", list.get(position).getLocalName());
                intent.putExtra("speciesID", list.get(position).getSpeciesID());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        name_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(bird_name.getWindowToken(), 0);
                finish();
            }
        });
    }


    @Override
    public void search() {
        // TODO Auto-generated method stub
    }

    @Override
    public void showPW() {
        // TODO Auto-generated method stub
    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub
        moreAuto = false;
        autoEdit();
    }

    @Override
    public void autoEdit() {
        // TODO Auto-generated method stub
        String sel_name = "%" + bird_name.getText().toString().trim() + "%";

        List<SpeciesInfo> list = WildbirdDbService.getInstance().autoEdit(sel_name, 0, 20);
        if (list.size() < 20) moreAuto = true;
        this.list.clear();
        this.list.addAll(list);
        adapter.notifyDataSetChanged();
    }
}
