package com.mytv365.zb.presenters;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.fhrj.library.third.tvlive.utils.Constants;
import com.mytv365.zb.avcontrollers.QavsdkControl;
import com.mytv365.zb.common.Constant;
import com.mytv365.zb.model.MySelfInfo;
import com.mytv365.zb.presenters.viewinface.LoginView;
import com.mytv365.zb.presenters.viewinface.LogoutView;
import com.mytv365.zb.utils.LogConstants;
import com.mytv365.zb.utils.SxbLog;
import com.tencent.TIMCallBack;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMManager;
import com.tencent.TIMUser;

import tencent.tls.platform.TLSErrInfo;
import tencent.tls.platform.TLSPwdLoginListener;
import tencent.tls.platform.TLSStrAccRegListener;
import tencent.tls.platform.TLSUserInfo;

/**
 * 登录的数据处理类
 */
public class LoginHelper extends Presenter {
    private Context mContext;
    private static final String TAG = LoginHelper.class.getSimpleName();
    private LoginView mLoginView;
    private LogoutView mLogoutView;
    private int RoomId = -1;
    private boolean isfirstJoin=false;


    public LoginHelper(Context context) {
        mContext = context;
    }

    public LoginHelper(Context context, LoginView loginView) {
        mContext = context;
        mLoginView = loginView;
    }

    public LoginHelper(Context context, LogoutView logoutView) {
        mContext = context;
        mLogoutView = logoutView;
    }





    /**
     * 登录imsdk
     *
     * @param identify 用户id
     * @param userSig  用户签名
     */
    public void imLogin(final String identify, String userSig) {
        TIMUser user = new TIMUser();
        user.setAccountType(String.valueOf(Constants.ACCOUNT_TYPE));
        user.setAppIdAt3rd(String.valueOf(Constants.SDK_APPID));
        user.setIdentifier(identify);
        //发起登录请求
        TIMManager.getInstance().login(
                Constants.SDK_APPID,
                user,
                userSig,                    //用户帐号签名，由私钥加密获得，具体请参考文档
                new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        SxbLog.e(TAG, "IMLogin fail ：" + i + " msg " + s);
                       // Toast.makeText(mContext, "IMLogin fail ：" + i + " msg " + s, Toast.LENGTH_SHORT).show();
                        if (mLoginView != null) {
                            mLoginView.loginFail();
                        }
                    }

                    @Override
                    public void onSuccess() {
                        SxbLog.i(TAG, "keypath IMLogin succ !");
                       // Toast.makeText(mContext, "IMLogin succ !", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(mContext, "登录成功", Toast.LENGTH_SHORT).show();
                        Log.i("login", "onSuccess:  登录成功");
                        SxbLog.d(TAG, LogConstants.ACTION_HOST_CREATE_ROOM + LogConstants.DIV + identify + LogConstants.DIV + "request room id");
                        getMyRoomNum();
                        startAVSDK();
                        if (!Constant.isfirstJoin){
                            setname();
                        }

                    }
                });
    }


    public void setname(){
        Constant.isfirstJoin=true;
        TIMFriendshipManager.getInstance().setNickName(Constant.getUser().getNickName(), new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
            }

            @Override
            public void onSuccess() {
//                Toast.makeText(mContext, "设置昵称", Toast.LENGTH_SHORT).show();
            }
        });


        TIMFriendshipManager.getInstance().setFaceUrl(Constant.getUser().getHeadImages(), new TIMCallBack() {
            @Override
            public void onError(int i, String s) {


            }

            @Override
            public void onSuccess() {
//                Toast.makeText(mContext, "设置头像成功", Toast.LENGTH_SHORT).show();
            }

        });

    }






    /**
     * 退出imsdk
     * <p>
     * 退出成功会调用退出AVSDK
     */
    public void imLogout() {
        TIMManager.getInstance().logout(new TIMCallBack() {

            @Override
            public void onError(int i, String s) {
                SxbLog.e(TAG, "IMLogout fail ：" + i + " msg " + s);
            }

            @Override
            public void onSuccess() {
                SxbLog.i(TAG, "IMLogout succ !");
                //清除本地缓存
                MySelfInfo.getInstance().clearCache(mContext);
                //反向初始化avsdk
                stopAVSDK();
            }
        });

    }

    /**
     * 登录TLS账号系统
     *
     * @param id
     * @param password
     */
    public void tlsLogin(String id, String password) {
        int ret = InitBusinessHelper.getmLoginHelper().TLSPwdLogin(id, password.getBytes(), new TLSPwdLoginListener() {
            @Override
            public void OnPwdLoginSuccess(TLSUserInfo tlsUserInfo) {//获取用户信息
//                Toast.makeText(mContext, "TLS login succ ! " + tlsUserInfo.identifier, Toast.LENGTH_SHORT).show();
//                SxbLog.i(TAG, "TLS OnPwdLoginSuccess " + tlsUserInfo.identifier);
                String userSig = InitBusinessHelper.getmLoginHelper().getUserSig(tlsUserInfo.identifier);
                MySelfInfo.getInstance().setId(tlsUserInfo.identifier);
                MySelfInfo.getInstance().setUserSig(userSig);
                imLogin(tlsUserInfo.identifier, userSig);
            }

            @Override
            public void OnPwdLoginReaskImgcodeSuccess(byte[] bytes) {

            }

            @Override
            public void OnPwdLoginNeedImgcode(byte[] bytes, TLSErrInfo tlsErrInfo) {

            }

            @Override
            public void OnPwdLoginFail(TLSErrInfo tlsErrInfo) {
                SxbLog.e(TAG, "OnPwdLoginFail " + tlsErrInfo.Msg);
                Toast.makeText(mContext, "OnPwdLoginFail：\n" + tlsErrInfo.Msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnPwdLoginTimeout(TLSErrInfo tlsErrInfo) {
                SxbLog.e(TAG, "OnPwdLoginTimeout " + tlsErrInfo.Msg);
                Toast.makeText(mContext, "OnPwdLoginTimeout：\n" + tlsErrInfo.Msg, Toast.LENGTH_SHORT).show();
            }
        });
        if (ret != -1001) {
            Toast.makeText(mContext, "input invalid !", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 在TLS模块注册一个账号
     *
     * @param id
     * @param psw
     */
    public void tlsRegister(final String id, final String psw) {
        int ret = InitBusinessHelper.getmAccountHelper().TLSStrAccReg(id, psw, new TLSStrAccRegListener() {
            @Override
            public void OnStrAccRegSuccess(TLSUserInfo tlsUserInfo) {
                Toast.makeText(mContext, tlsUserInfo.identifier + " register a user succ !  ", Toast.LENGTH_SHORT).show();
                //继续登录流程
                tlsLogin(id, psw);
            }

            @Override
            public void OnStrAccRegFail(TLSErrInfo tlsErrInfo) {
                Toast.makeText(mContext, " register a user fail ! " + tlsErrInfo.Msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnStrAccRegTimeout(TLSErrInfo tlsErrInfo) {
                Toast.makeText(mContext, " register timeout ! " + tlsErrInfo.Msg, Toast.LENGTH_SHORT).show();
            }
        });

        if (ret != -1001) {
            Toast.makeText(mContext, "input invalid !", Toast.LENGTH_SHORT).show();
        }

    }


    /**
     * 向用户服务器获取自己房间号
     */
    private void getMyRoomNum() {
        if (MySelfInfo.getInstance().getMyRoomNum() == -1) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    OKhttpHelper.getInstance().getMyRoomId(mContext);
                }
            }).start();
        }else{
            SxbLog.d(TAG, LogConstants.ACTION_HOST_CREATE_ROOM + LogConstants.DIV + MySelfInfo.getInstance().getId() + LogConstants.DIV + "request room id"
                    + LogConstants.DIV + LogConstants.STATUS.SUCCEED + LogConstants.DIV + "get room id from local " + MySelfInfo.getInstance().getMyRoomNum());
        }
    }


    /**
     * 初始化AVSDK
     */
    private void startAVSDK() {
        QavsdkControl.getInstance().setAvConfig(Constants.SDK_APPID, "" + Constants.ACCOUNT_TYPE, MySelfInfo.getInstance().getId(), MySelfInfo.getInstance().getUserSig());
        QavsdkControl.getInstance().startContext();
        if (mLoginView != null)
            mLoginView.loginSucc();
    }


    /**
     * 反初始化AVADK
     */
    public void stopAVSDK() {
        QavsdkControl.getInstance().stopContext();
        mLogoutView.logoutSucc();
    }


    @Override
    public void onDestory() {
        mLoginView = null;
        mLogoutView = null;
        mContext = null;
    }
}
