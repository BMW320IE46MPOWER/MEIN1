package com.fhrj.library.tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.fhrj.library.MApplication;
import com.fhrj.library.data.message.NotificationMessage;
import com.fhrj.library.view.ProgressDialog;

/***
 * 对话框工具类
 * @author ZhangGuoHao
 * @date 2016年4月7日 下午8:24:14
 */
public class ToolAlert {

  /** 日志输出标志 **/
  protected static final String TAG = ToolAlert.class.getSimpleName();

  private static ProgressDialog mProgressDialog;

  /**
   * 显示ProgressDialog
   * 
   * @param context 上下文
   * @param message 消息
   */
  public static void loading(Activity context, String message) {

    loading(context, message, true);
  }

  /**
   * 显示ProgressDialog
   * 
   * @param context 上下文
   * @param message 消息
   */
  public static void loading(Activity context, String message, final ILoadingOnKeyListener listener) {

    loading(context, message, true, listener);
  }

  /**
   * 显示ProgressDialog
   * 
   * @param context 上下文
   * @param message 消息
   * @param cancelable 是否可以取消
   */
  public static void loading(Activity context, String message, boolean cancelable) {

    if (mProgressDialog == null || mProgressDialog.getOwnerActivity() != context) {
      mProgressDialog = new ProgressDialog(context, message);
      mProgressDialog.setCancelable(cancelable);
      mProgressDialog.setOwnerActivity(context);
    }
    
    if (mProgressDialog.isShowing()) {
      mProgressDialog.cancel();
      mProgressDialog.dismiss();
    }
    mProgressDialog.show();
  }

  /**
   * 显示ProgressDialog
   * 
   * @param context 上下文
   * @param message 消息
   */
  public static void loading(Activity context, String message, boolean cancelable,
      final ILoadingOnKeyListener listener) {
    
    if (mProgressDialog == null || mProgressDialog.getOwnerActivity() != context) {
      mProgressDialog = new ProgressDialog(context, message);
      mProgressDialog.setCancelable(cancelable);
      mProgressDialog.setOwnerActivity(context);
    }
    
    if (mProgressDialog.isShowing()) {
      mProgressDialog.cancel();
    }

    if (null != listener) {
      mProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
          listener.onKey(dialog, keyCode, event);
          return false;
        }
      });
    } else {
      mProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
          if (keyCode == KeyEvent.KEYCODE_BACK) {
            mProgressDialog.dismiss();
          }
          return false;
        }
      });
    }

    try {
      if (context instanceof Activity) {
        if (!context.isFinishing()) {
          mProgressDialog.show();
        }
      } else {
        mProgressDialog.show();
      }
    } catch (Exception e) {
      e.printStackTrace();
      ToolLog.e(TAG, "显示等待对话框失败，原因："+e.getMessage());
    }
  }

  /**
   * 判断加载对话框是否正在加载
   * 
   * @return 是否
   */
  public static boolean isLoading() {

    if (null != mProgressDialog) {
      return mProgressDialog.isShowing();
    } else {
      return false;
    }
  }

  /**
   * 关闭ProgressDialog
   */
  public static void closeLoading() {
    if (mProgressDialog != null) {
      mProgressDialog.dismiss();
      mProgressDialog = null;
    }
  }

  /**
   * 更新ProgressDialog进度消息
   * 
   * @param message 消息
   */
  public static void updateProgressText(String message) {
    if (mProgressDialog == null) return;

    if (mProgressDialog.isShowing()) {
      mProgressDialog.setMessage(message);
    }
  }

  /**
   * 弹出对话框
   *
   * @param msg 对话框内容
   */
  public static AlertDialog dialog(Context context, String msg) {
    return dialog(context, "", msg);
  }

  /**
   * 弹出对话框
   * 
   * @param title 对话框标题
   * @param msg 对话框内容
   */
  public static AlertDialog dialog(Context context, String title, String msg) {
    return dialog(context, title, msg, null);
  }

  /**
   * 弹出对话框
   * 
   * @param title 对话框标题
   * @param msg 对话框内容
   * @param okBtnListenner 确定按钮点击事件
   */
  public static AlertDialog dialog(Context context, String title, String msg,
      OnClickListener okBtnListenner) {
    return dialog(context, title, msg, okBtnListenner, null);
  }

  /**
   * 弹出对话框
   * 
   * @param title 对话框标题
   * @param msg 对话框内容
   * @param okBtnListenner 确定按钮点击事件
   * @param cancelBtnListenner 取消按钮点击事件
   */
  public static AlertDialog dialog(Context context, String title, String msg,
      OnClickListener okBtnListenner, OnClickListener cancelBtnListenner) {
    return dialog(context, null, title, msg, okBtnListenner, cancelBtnListenner);
  }

  /**
   * 弹出对话框
   * 
   * @param title 对话框标题
   * @param msg 对话框内容
   */
  public static AlertDialog dialog(Context context, Drawable icon, String title, String msg) {
    return dialog(context, icon, title, msg, null);
  }

  /**
   * 弹出对话框
   * 
   * @param title 对话框标题
   * @param msg 对话框内容
   * @param okBtnListenner 确定按钮点击事件
   */
  public static AlertDialog dialog(Context context, Drawable icon, String title, String msg,
      OnClickListener okBtnListenner) {
    return dialog(context, icon, title, msg, okBtnListenner, null);
  }

  /**
   * 弹出对话框
   * 
   * @param icon 标题图标
   * @param title 对话框标题
   * @param msg 对话框内容
   * @param okBtnListenner 确定按钮点击事件
   * @param cancelBtnListenner 取消按钮点击事件
   */
  public static AlertDialog dialog(Context context, Drawable icon, String title, String msg,
      OnClickListener okBtnListenner, OnClickListener cancelBtnListenner) {
    Builder dialogBuilder = new AlertDialog.Builder(context);
    if (null != icon) {
      dialogBuilder.setIcon(icon);
    }
    if (ToolString.isNoBlankAndNoNull(title)) {
      dialogBuilder.setTitle(title);
    }
    dialogBuilder.setMessage(msg);
    if (null != okBtnListenner) {
      dialogBuilder.setPositiveButton(android.R.string.ok, okBtnListenner);
    }
    if (null != cancelBtnListenner) {
      dialogBuilder.setNegativeButton(android.R.string.cancel, cancelBtnListenner);
    }
    dialogBuilder.create();
    return dialogBuilder.show();
  }

  /**
   * 弹出一个自定义布局对话框
   * 
   * @param context 上下文
   * @param view 自定义布局View
   */
  public static AlertDialog dialog(Context context, View view) {
    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
    AlertDialog alertDialog = builder.create();
    WindowManager.LayoutParams  lp= alertDialog.getWindow().getAttributes();
    lp.width=350;//定义宽度
    lp.height=300;//定义高度
    alertDialog.getWindow().setAttributes(lp);
    alertDialog.addContentView(view,lp);

    return builder.show();
  }

  /**
   * 弹出一个自定义布局对话框
   * 
   * @param context 上下文
   * @param resId 自定义布局View对应的layout id
   */
  public static AlertDialog dialog(Context context, int resId) {
    LayoutInflater inflater = LayoutInflater.from(context);
    View view = inflater.inflate(resId, null);
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    AlertDialog alertDialog=builder.create();
    //调用这个方法时，按对话框以外的地方不起作用。按返回键还起作用
    alertDialog.setCanceledOnTouchOutside(false);
    alertDialog.setCancelable(false);//调用这个方法时，按对话框以外的地方不起作用。按返回键也不起作用
    alertDialog.setView(view,0,0,0,0);
    return alertDialog;
  }

  /**
   * 弹出较短的Toast消息
   * 
   * @param msg 消息内容
   */
  public static void toastShort(String msg) {
    Toast.makeText(MApplication.gainContext(), msg, Toast.LENGTH_SHORT).show();
  }

  /**
   * 弹出较短的Toast消息
   * 
   * @param msg 消息内容
   */
  public static void toastShort(Context context, String msg) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
  }

  /**
   * 弹出较长的Toast消息
   * 
   * @param msg 消息内容
   */
  public static void toastLong(String msg) {
    Toast.makeText(MApplication.gainContext(), msg, Toast.LENGTH_LONG).show();
  }

  /**
   * 弹出较长的Toast消息
   * 
   * @param msg 消息内容
   */
  public static void toastLong(Context context, String msg) {
    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
  }

  /**
   * 弹出自定义时长的Toast消息
   * 
   * @param msg 消息内容
   */
  public static void toast(String msg, int duration) {
    Toast.makeText(MApplication.gainContext(), msg, duration).show();
  }

  /**
   * 弹出自定义时长的Toast消息
   * 
   * @param msg 消息内容
   */
  public static void toast(Context context, String msg, int duration) {
    Toast.makeText(context, msg, duration).show();
  }

  /**
   * 显示居中黑色的Toast
   * 
   * @param context
   * @param msg
   */
  public static void showCenterText(Context context, String msg) {
    ToolToast.showShort(context, msg);
  }

  /**
   * 显示居中黑色的Toast
   * 
   * @param context
   * @param msg
   */
  public static void showCenterText(Context context, String msg, int duration) {
    ToolToast.buildToast(context, msg, duration).show();
  }

  /**
   * 弹出Pop窗口
   * 
   * @param context 依赖界面上下文
   * @param anchor 触发pop界面的控件
   * @param viewId pop窗口界面layout
   * @param xoff 窗口X偏移量
   * @param yoff 窗口Y偏移量
   */
  public static PopupWindow popwindow(Context context, View anchor, int viewId, int xoff, int yoff) {
    ViewGroup menuView = (ViewGroup) LayoutInflater.from(context).inflate(viewId, null);
    PopupWindow pw =
        new PopupWindow(menuView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
    pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    pw.setTouchable(true);
    pw.setFocusable(true);
    pw.setOutsideTouchable(true);
    pw.showAsDropDown(anchor, xoff, yoff);
    pw.update();
    return pw;
  }

  /**
   * 弹出Pop窗口
   * 
   * @param anchor 触发pop界面的控件
   * @param popView pop窗口界面
   * @param xoff 窗口X偏移量
   * @param yoff 窗口Y偏移量
   */
  public static PopupWindow popwindow(View anchor, View popView, int xoff, int yoff) {
    PopupWindow pw =
        new PopupWindow(popView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
    pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    pw.setOutsideTouchable(true);
    pw.showAsDropDown(anchor, xoff, yoff);
    pw.update();
    return pw;
  }

  /**
   * 弹出Pop窗口（可设置是否点击其他地方关闭窗口）
   * 
   * @param context 依赖界面上下文
   * @param anchor 触发pop界面的控件
   * @param viewId pop窗口界面layout
   * @param xoff 窗口X偏移量
   * @param yoff 窗口Y偏移量
   * @param outSideTouchable 点击其他地方是否关闭窗口
   */
  public static PopupWindow popwindow(Context context, View anchor, int viewId, int xoff, int yoff,
      boolean outSideTouchable) {
    ViewGroup menuView = (ViewGroup) LayoutInflater.from(context).inflate(viewId, null);
    PopupWindow pw =
        new PopupWindow(menuView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
    pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    pw.setTouchable(outSideTouchable);
    pw.setFocusable(outSideTouchable);
    pw.setOutsideTouchable(outSideTouchable);
    pw.showAsDropDown(anchor, xoff, yoff);
    pw.update();
    return pw;
  }

  /**
   * 弹出Pop窗口（可设置是否点击其他地方关闭窗口）
   * 
   * @param anchor 触发pop界面的控件
   * @param popView pop窗口界面
   * @param xoff 窗口X偏移量
   * @param yoff 窗口Y偏移量
   * @param outSideTouchable 点击其他地方是否关闭窗口
   */
  public static PopupWindow popwindow(View anchor, View popView, int xoff, int yoff,
      boolean outSideTouchable) {
    PopupWindow pw =
        new PopupWindow(popView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
    pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    pw.setOutsideTouchable(outSideTouchable);
    pw.showAsDropDown(anchor, xoff, yoff);
    pw.update();

    return pw;
  }

  /**
   * 指定坐标弹出Pop窗口
   * 
   * @param pw pop窗口对象
   * @param anchor 触发pop界面的控件
   * @param popView pop窗口界面
   * @param x 窗口X
   * @param y 窗口Y
   */
  public static PopupWindow popwindowLoction(PopupWindow pw, View anchor, View popView, int x, int y) {
    if (pw == null) {
      pw = new PopupWindow(popView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
      pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
      pw.setOutsideTouchable(false);
    }

    if (pw.isShowing()) {
      pw.update(x, y, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    } else {
      pw.showAtLocation(anchor, Gravity.NO_GRAVITY, x, y);
    }

    return pw;
  }


  /**
   * 方向枚举
   * 
   */
  public enum Direction {
    UP, DOWN, LEFT, RIGHT
  }

  /**
   * 在控件的上方打开popwindow
   * 
   * @param pw PopupWindow
   * @param anchor 打开popw的目标控件
   * @param popView popview布局view
   * @param direction
   *        在目标控件的方向：上(Direction.UP)、右(Direction.RIGHT)、下(Direction.DOWN)、左(Direction.LEFT)
   * @return
   */
  public static PopupWindow popwindowLoction(PopupWindow pw, View anchor, View popView,
      Direction direction) {

    int[] location = new int[2];
    anchor.getLocationOnScreen(location);
    if (pw == null) {
      pw = new PopupWindow(popView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
      pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
      pw.setOutsideTouchable(false);
    }

    switch (direction) {
      case UP:
        if (pw.isShowing()) {
          pw.update(location[0], location[1] - pw.getHeight(), LayoutParams.WRAP_CONTENT,
              LayoutParams.WRAP_CONTENT);
        } else {
          pw.showAtLocation(anchor, Gravity.NO_GRAVITY, location[0], location[1] - pw.getHeight());
        }
        break;
      case DOWN:
        if (pw.isShowing()) {
          pw.update(location[0], location[1] + pw.getHeight(), LayoutParams.WRAP_CONTENT,
              LayoutParams.WRAP_CONTENT);
        } else {
          pw.showAtLocation(anchor, Gravity.NO_GRAVITY, location[0], location[1] + pw.getHeight());
        }
        break;
      case LEFT:
        if (pw.isShowing()) {
          pw.update(location[0] - pw.getWidth(), location[1], LayoutParams.WRAP_CONTENT,
              LayoutParams.WRAP_CONTENT);
        } else {
          pw.showAtLocation(anchor, Gravity.NO_GRAVITY, location[0] - pw.getWidth(), location[1]);
        }
        break;
      case RIGHT:
        if (pw.isShowing()) {
          pw.update(location[0] + anchor.getWidth(), location[1], LayoutParams.WRAP_CONTENT,
              LayoutParams.WRAP_CONTENT);
        } else {
          pw.showAtLocation(anchor, Gravity.NO_GRAVITY, location[0] + anchor.getWidth(),
              location[1]);
        }
        break;

      default:
        if (pw.isShowing()) {
          pw.update(location[0], location[1] - pw.getHeight(), LayoutParams.WRAP_CONTENT,
              LayoutParams.WRAP_CONTENT);
        } else {
          pw.showAtLocation(anchor, Gravity.NO_GRAVITY, location[0], location[1] - pw.getHeight());
        }
        break;
    }

    return pw;
  }

  /**
   * 往状态栏发送一条通知消息
   * 
   * @param mContext 上下文
   * @param message 消息Bean
   */
  @SuppressWarnings("deprecation")
public static void notification(Context mContext, NotificationMessage message) {

    // 消息管理器
    NotificationManager mNoticeManager =
        (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    // 构造Notification
    Notification notice = new Notification();
    notice.icon = message.getIconResId();
    notice.tickerText = message.getStatusBarText();
    notice.when = System.currentTimeMillis();
    /**
     * notification.defaults = Notification.DEFAULT_SOUND; // 调用系统自带声音 notification.defaults =
     * Notification.DEFAULT_VIBRATE;// 设置默认震动 notification.defaults = Notification.DEFAULT_ALL; //
     * 设置铃声震动 notification.defaults = Notification.DEFAULT_ALL; // 把所有的属性设置成默认
     */
    notice.defaults = Notification.DEFAULT_SOUND;// 通知时发出的默认声音
    /**
     * notification.flags = Notification.FLAG_NO_CLEAR; // 点击清除按钮时就会清除消息通知,但是点击通知栏的通知时不会消失
     * notification.flags = Notification.FLAG_ONGOING_EVENT; // 点击清除按钮不会清除消息通知,可以用来表示在正在运行
     * notification.flags |= Notification.FLAG_AUTO_CANCEL; // 点击清除按钮或点击通知后会自动消失 notification.flags
     * |= Notification.FLAG_INSISTENT; // 一直进行，比如音乐一直播放，知道用户响应
     */
    notice.flags |= Notification.FLAG_AUTO_CANCEL; // 通知点击清除

    // 设置通知显示的参数
    Intent mIntent = new Intent(mContext, message.getForwardComponent());
    mIntent.setAction(ToolString.gainUUID());
    mIntent.putExtras(message.getExtras());
    mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
    // 自动更新PendingIntent的Extra数据
    PendingIntent pIntent =
        PendingIntent.getActivity(mContext, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);


    // 发送通知
    mNoticeManager.notify(message.getId(), notice);
  }


  /**
   * 发送自定义布局通知消息
   * 
   * @param mContext 上下文
   * @param message 消息Bean
   */
  public static void notificationCustomView(Context mContext, NotificationMessage message) {

    // 消息管理器
    NotificationManager mNoticeManager =
        (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    // 构造Notification
    Notification mNotify = new Notification();
    mNotify.icon = message.getIconResId();
    mNotify.tickerText = message.getStatusBarText();
    mNotify.when = System.currentTimeMillis();
    mNotify.flags |= Notification.FLAG_AUTO_CANCEL; // 通知点击清除
    mNotify.contentView = message.getmRemoteViews();

    // 设置通知显示的参数
    Intent mIntent = new Intent(mContext, message.getForwardComponent());
    mIntent.setAction(ToolString.gainUUID());
    mIntent.putExtras(message.getExtras());
    mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
    PendingIntent contentIntent =
        PendingIntent.getActivity(mContext, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    mNotify.contentIntent = contentIntent;

    // 发送通知
    mNoticeManager.notify(message.getId(), mNotify);
  }

  /**
   * Loading监听对话框
   */
  public interface ILoadingOnKeyListener {
    boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event);

  }
}
