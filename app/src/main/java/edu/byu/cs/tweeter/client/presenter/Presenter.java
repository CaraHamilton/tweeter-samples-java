package edu.byu.cs.tweeter.client.presenter;

//import android.view.View;

public abstract class Presenter<K> {

    public final K view;
    //    private View view;
//
    protected Presenter(K view) { this.view = view; }

}
