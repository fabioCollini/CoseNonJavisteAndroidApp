package it.cosenonjaviste.mvp.category;

import java.util.List;

import javax.inject.Inject;

import it.cosenonjaviste.model.Category;
import it.cosenonjaviste.model.CategoryResponse;
import it.cosenonjaviste.model.WordPressService;
import it.cosenonjaviste.mvp.ListPresenter;
import it.cosenonjaviste.mvp.base.MvpConfig;
import it.cosenonjaviste.mvp.post.PostListPresenter;
import it.cosenonjaviste.mvp.post.PostListView;
import rx.Observable;

public class CategoryListPresenter extends ListPresenter<Category> {

    @Inject WordPressService wordPressService;

    @Inject MvpConfig<PostListView> postListMvpConfig;

    public void loadData() {
        Observable<List<Category>> observable = wordPressService
                .listCategories()
                .map(CategoryResponse::getCategories);

        subscribeListObservable(observable);
    }

    public void goToPosts(int position) {
        Category category = model.get(position);
        contextBinder.startNewActivity(postListMvpConfig, PostListPresenter.open(contextBinder, category));
    }
}
