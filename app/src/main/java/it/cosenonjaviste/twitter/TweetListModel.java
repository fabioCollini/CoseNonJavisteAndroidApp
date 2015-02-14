package it.cosenonjaviste.twitter;

import org.parceler.Parcel;

import java.util.List;

import it.cosenonjaviste.lib.mvp.utils.OptionalItem;
import it.cosenonjaviste.lib.mvp.utils.OptionalList;
import it.cosenonjaviste.model.Tweet;
import rx.functions.Action1;

@Parcel
public class TweetListModel {
    OptionalList<Tweet> list = new OptionalList<>();

    boolean moreDataAvailable;

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        this.moreDataAvailable = moreDataAvailable;
    }

    public boolean isMoreDataAvailable() {
        return moreDataAvailable;
    }

    public OptionalItem<List<Tweet>> call(Action1<List<Tweet>> action) {
        return list.call(action);
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public void done(List<Tweet> object) {
        list.done(object);
    }

    public void error(Throwable throwable) {
        list.error(throwable);
    }

    public int size() {
        return list.size();
    }

    public boolean isError() {
        return list.isError();
    }

    public void append(List<Tweet> object) {
        list.append(object);
    }
}
