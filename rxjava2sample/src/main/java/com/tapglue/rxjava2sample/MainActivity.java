package com.tapglue.rxjava2sample;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.tapglue.android.Configuration;
import com.tapglue.android.RxTapglue;
import com.tapglue.android.entities.User;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Configuration configuration = new Configuration("https://api.tapglue.com", "2da2d79336c2773c630ce46f5d24cb76");
        configuration.setLogging(true);
        final RxTapglue tapglue;
        try {
            tapglue = new RxTapglue(configuration, this);
            User user = new User("pablo", "supersecret");
            tapglue.createUser(user).subscribeOn(Schedulers.io()).subscribe(new Observer<User>() {
                @Override
                public void onSubscribe(Disposable d) {
                    Log.d("MainActivity", "onSubscribe");
                }

                @Override
                public void onNext(User user) {
                    Log.d("MainActivity", "onNext");
                }

                @Override
                public void onError(Throwable e) {
                    Log.d("MainActivity", "onError");
                }

                @Override
                public void onComplete() {
                    Log.d("MainActivity", "onComplete");
                    tapglue.loginWithUsername("pablo", "supersecret").subscribe();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
