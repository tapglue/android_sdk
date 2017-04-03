package com.tapglue.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tapglue.android.Configuration;
import com.tapglue.android.rx2.RxTapglue;
import com.tapglue.android.entities.User;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by gauravvashisth on 03/04/17.
 */

public class RxJava2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Configuration configuration = new Configuration("https://api.tapglue.com", "2da2d79336c2773c630ce46f5d24cb76");
        configuration.setLogging(true);
        final RxTapglue tapglue;
        try {
            tapglue = new RxTapglue(configuration, this);
            User user = new User("pablo", "supersecret");
            tapglue.createUser(user).subscribeOn(Schedulers.io()).subscribe(new Observer<User>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(User user) {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {
                    tapglue.loginWithUsername("pablo", "supersecret").subscribe();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
