package com.example.surfaceview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
public class OverActivity extends Activity {
	/** Called when the activity is first created **/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Titleを消す
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		MyOverView mView = new MyOverView(this);
		setContentView(mView);
	}
	/**
	 * バックキーを押した時にアプリを終了するようにした
	 */
	public void onPause() {
		super.onPause();
		finish();
	}
}

/**
*  描画用のクラス
*/
class MyOverView extends View {
	
	/**
	 * ゲームオーバーメッセージ
	 */
	private Bitmap mGameOver;
	/**
	 * ねこ
	 */
	private Bitmap mCat;
	
	/**
	 * Context
	 */
	private Context mContext;
	
	/**
	 * Twitter投稿画像データを保持
	 */
	private Bitmap mTwitter;
	
	/**
	 * Twitter投稿画像(x)
	 */
	private int twitterX = 180;
	
	/**
	 * Twitter投稿画像(y)
	 */
	private int twitterY = 600;
	
	/**
	 * コンストラクタ
	 * 
	 * @param c
	 */
	public MyOverView(Context c) {
		super(c);
		mContext = c;
		setFocusable(true);
		// Resourceインスタンスの生成 
		Resources res = this.getContext().getResources(); 
		// 画像の読み込み(res/drawable/gameover.png)   
		mGameOver = BitmapFactory.decodeResource(res, R.drawable.gameover);
		// 画像の読み込み(res/drawable/gameover_cat.png)   
		mCat = BitmapFactory.decodeResource(res, R.drawable.gameover_cat);
		//画像の読み込み(res/drawable/twitter.png)
		mTwitter = BitmapFactory.decodeResource(res, R.drawable.twitter);
	}
	
	/**
	 * 描画処理
	 */
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		/* 背景色を設定 */
		canvas.drawColor(Color.CYAN);
		
		/* 文字を描画 */
		canvas.drawBitmap(mGameOver, 100, 250, null);
		canvas.drawBitmap(mCat, 170, 400, null);
		//Twitterへの投稿イメージの描画
		canvas.drawBitmap(mTwitter,twitterX, twitterY, null);
	}
	
	/**
	 * タッチイベント
	 */
	public boolean onTouchEvent(MotionEvent event) {
		//タッチした時に実行
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			//指がクリックされたX,Y座標の取得
			int touchX = (int) event.getX();
			int touchY = (int) event.getY();
			
			// Twitterへの投稿の中心座標を計算
			int centerX = twitterX + mTwitter.getWidth() / 2;
			int centerY = twitterY + mTwitter.getHeight() / 2;
			
			// Twitterへの投稿と指の距離を計算
			double atariR = Math.sqrt(Math.abs(centerX - touchX) * Math.abs(centerX - touchX)
									+ Math.abs(centerY - touchY) * Math.abs(centerY - touchY));
			
			// 円の半径
			int twitterR = mTwitter.getWidth() / 2;
			
			// あたり判定
			if (atariR < twitterR) {
				// タイトルを起動
				Intent twitterIntent = new Intent();
				twitterIntent.setClassName("com.twitter.android", "com.twitter.android.PostActivity");
				twitterIntent.putExtra(Intent.EXTRA_TEXT, "飛びねこなう");
				mContext.startActivity(twitterIntent);
			} else {
				// タイトルを起動
				Intent selectIntent = new Intent();
				selectIntent.setClassName("com.example.surfaceview", "com.example.surfaceview.TitleActivity");
				mContext.startActivity(selectIntent);
			}
		}
		return true;
	}
}