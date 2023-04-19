package com.example0.datalibrary.listener;

import com.example0.datalibrary.model.User;

import java.util.List;

public interface DBLoadListener {

    void onStart(int successCount);

    void onLoad(int finishCount, int successCount, float progress);

    void onComplete(List<User> features , int successCount);

    void onFail(int finishCount, int successCount, List<User> features);
}
