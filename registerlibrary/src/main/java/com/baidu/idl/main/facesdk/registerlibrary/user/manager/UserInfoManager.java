package com.baidu.idl.main.facesdk.registerlibrary.user.manager;


import com.example0.datalibrary.api.FaceApi;
import com.baidu.idl.main.facesdk.registerlibrary.user.utils.LogUtils;
import com.example0.datalibrary.listener.DBLoadListener;
import com.example0.datalibrary.model.User;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 用户管理
 * Created by v_liujialu01 on 2018/12/14.
 */

public class UserInfoManager {
    private static final String TAG = UserInfoManager.class.getSimpleName();
    private ExecutorService mExecutorService = null;
    private Future future;
    // 私有构造
    private UserInfoManager() {
        if (mExecutorService == null) {
            mExecutorService = Executors.newSingleThreadExecutor();
        }
    }

    private static class HolderClass {
        private static final UserInfoManager instance = new UserInfoManager();
    }

    public static UserInfoManager getInstance() {
        return HolderClass.instance;
    }

    /**
     * 释放
     */
    public void release() {
        FaceApi.getInstance().isDelete = false;
        if (future != null && !future.isDone()) {
            future.cancel(true);
            return;
        }

        LogUtils.i(TAG, "release");
    }

    /**
     * 删除用户列表信息
     */
    public void deleteUserListInfo(final List<User> list , final String userName,
                                   final UserInfoListener listener, final DBLoadListener dbLoadListener) {
        future = mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                if (listener == null) {
                    return;
                }

                if (list == null) {
                    listener.userListDeleteFailure("参数异常");
                    return;
                }
                FaceApi.getInstance().isDelete = true;
                FaceApi.getInstance().userDeletes(list , userName != null && !"".equals(userName) , dbLoadListener);
                FaceApi.getInstance().isDelete = false;
                listener.userListDeleteSuccess();
            }
        });
    }

    /**
     * 获取用户列表信息
     */
    public void getUserListInfo(final String userName, final UserInfoListener listener) {
        future = mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                if (listener == null) {
                    return;
                }

                // 如果关键字为null，则全局查找
                if (userName == null) {
                    listener.userListQuerySuccess(null, FaceApi.getInstance().getAllUserList());
                } else {
                    listener.userListQuerySuccess(userName,
                            FaceApi.getInstance().getUserListByUserNameVag(userName));
                }
            }
        });
    }

    public static class UserInfoListener {

        public void userListQuerySuccess(String userName, List<User> listUserInfo) {
            // 用户列表查询成功
        }

        public void userListQueryFailure(String message) {
            // 用户列表查询失败
        }

        public void userListDeleteSuccess() {
            // 用户列表删除成功
        }

        public void userListDeleteFailure(String message) {
            // 用户列表删除失败
        }
    }
}
