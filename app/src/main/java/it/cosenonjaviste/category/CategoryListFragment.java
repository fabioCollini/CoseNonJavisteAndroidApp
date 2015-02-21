package it.cosenonjaviste.category;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.quentindommerc.superlistview.SuperGridview;

import butterknife.InjectView;
import butterknife.OnClick;
import it.cosenonjaviste.CnjFragment;
import it.cosenonjaviste.R;
import rx.functions.Actions;

public class CategoryListFragment extends CnjFragment<CategoryListPresenter, CategoryListModel> {

    @InjectView(R.id.grid) SuperGridview grid;

    private CategoryAdapter adapter;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getComponent().inject(this);
    }

    @Override protected CategoryListPresenter createPresenter() {
        return getComponent().getCategoryListPresenter();
    }

    @Override protected int getLayoutId() {
        return R.layout.super_grid;
    }

    @SuppressLint("ResourceAsColor") @Override protected void initView(View view) {
        super.initView(view);
        adapter = new CategoryAdapter(getActivity());
        grid.setAdapter(adapter);
        grid.getList().setNumColumns(2);
        grid.setRefreshingColor(android.R.color.holo_orange_light, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
        grid.setOnItemClickListener((parent, v, position, id) -> presenter.goToPosts(position));
    }

    @OnClick(R.id.error_retry) void retry() {
        presenter.loadData();
    }


    @Override public void update(CategoryListModel model) {
        model.call(
                categories -> {
                    grid.showList();
                    adapter.reloadData(categories);
                }
        ).whenError(
                t -> grid.showError()
        ).whenEmpty(
                Actions.empty()
        );
    }

    public void startLoading() {
        grid.showProgress();
    }
}
