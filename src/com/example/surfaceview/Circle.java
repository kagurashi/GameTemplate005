package com.example.surfaceview;

import android.app.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.hardware.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import java.util.*;

public class Circle extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Titleを消す
//		requestWindowFeature(Window.FEATURE_NO_TITLE);

		MyCircleView mSurfaceView = new MyCircleView(this);
		setContentView(mSurfaceView);
	}

	/**
	 * バックキーを押した時にアプリを終了するようにした
	 */
	public void onPause() {
		super.onPause();
		finish();
	}
	
	
	
	
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate main_menu.xml 
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.mainMenuTop:
				Intent selectIntent = new Intent();
				selectIntent.setClassName("com.example.surfaceview", "com.example.surfaceview.MainActivity");
				startActivity(selectIntent);
				return true;
			case R.id.mainMenuAbout:
				Toast.makeText(this, "This is my app!!!", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.mainMenuExit:
				finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}

class MyCircleView extends SurfaceView implements SurfaceHolder.Callback, Runnable, SensorEventListener {

	/**
	 * 画面のグリッド(X軸)
	 */
	private int DX = 20;

	/**
	 * 画面のグリッド(Y軸)
	 */
	private int DY = 20;

	/**
	 * 描画するCount値
	 */
	private int count = 0;

	/**
	 * SensorManager
	 */
	private SensorManager mSensorManager;

	/**
	 * 画像を格納する変数
	 */
	private Bitmap myBitmap; 

	/**
	 * 床を格納する変数
	 */
	private Bitmap floorBitmap;
	/**
	 * 自分のx座標
	 */
	private int myX = DX*16;

	/**
	 * 自分の　y座標
	 */
	private int myY = DY*30;
	
	private int targetX = myX;
	private int targetY = myY;
	
	private int pointX;
	private int pointY;
	

	/**
	 * 床のx座標
	 */
//	private int floorX[] = {DX * 15, DX * 12, DX * 8, DX * 3, DX * 15, DX * 8, DX * 1, DX * 3};
//	private int floorX[] = {DX * 15, DX * 12, DX * 8};
	
	private Integer[] arrX = new Integer[] {DX * 15, DX * 12, DX * 8, DX * 3, DX * 15, DX * 8, DX * 1, DX * 3};
	// 配列をリストで削除し戻す
	private List<Integer> listX = new ArrayList<Integer>(Arrays.asList(arrX));
	//listX.remove(i);
	private Integer[] floorX = listX.toArray(new Integer[listX.size()]);
	
	
	
	
	/**
	 * 床y座標
	 */
//	private int floorY[] = {DY * 30, DY * 25, DY * 20, DY * 15, DY * 10, DY * 5, DY * 0, DY * -5};	
//	private int floorY[] = {DY * 30, DY * 25, DY * 20};
	//private int floorY[] = {DY * 30, DY * 20};
	
	private Integer[] arrY = new Integer[] {DX * 30, DX * 25, DX * 20, DY * 15, DY * 10, DY * 5, DY * 0, DY * -5};
	// 配列をリストで削除し戻す
	private List<Integer> listY = new ArrayList<Integer>(Arrays.asList(arrY));
	//listY.remove(i);
	private Integer[] floorY = listY.toArray(new Integer[listY.size()]);

		
	
	/**
	 * Thread
	 */
	private Thread mThread;

	/**
	 * Threadが動いているかどうかの判断
	 */
	private boolean isRunning = false;

	/**
	 * 速度
	 */
	private int v;

	/**
	 * 重力
	 */
	private float g = 9.8f;

	/**
	 * 画面の位置
	 */
	private float base = 0;

	/**
	 * 画面サイズ(x)
	 */
	private int displayX;

	/**
	 * 画面サイズ(y)
	 */
	private int displayY = 1000;

	/**
	 * Context
	 */
	private Context mContext;

	private int iDel;

	private int deltaY;

	private int deltaX;

	private int vX;

	private double deltaR;

	private int vY;

	private int deltaTargetX;

	private int deltaTargetY;

	private double deltaTargetR;

	private int iMin;

	private double deltaTargetRMin;

	private int targetOn = -1;

	private int deltaPointX;

	private int deltaPointY;

	private double deltaPointR = 99999;

	private int pointOn = -1;

	private int HP = 123;
	private int MHP = 123;

	private int NP = 78;
	private int MNP = 78;

	private int xBtnN = 273;

	private int yBtnN = 997;

	private int onBtnN = -1;

	private int inBtnN;

	
	public MyCircleView(Context context) {
		super(context);
		mContext = context;
		Log.i("SURFACE", "MySurfaceView()");

		// SensorManager
		mSensorManager = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);

		// Sensorの取得とリスナーへの登録
		List< Sensor > sensors = mSensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
		if (sensors.size() > 0) {
			Sensor sensor = sensors.get(0);
			mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
		}

		// Resourceインスタンスの生成 
		Resources res = this.getContext().getResources(); 
		// 画像の読み込み(res/drawable/cat_jump.png) 使用する素材の名前はわかりやすいものに変更してください  
		myBitmap = BitmapFactory.decodeResource(res, R.drawable.cat_jump);

		// 画像の読み込み(res/drawable/yuka.png) 使用する素材の名前はわかりやすいものに変更してください
		floorBitmap = BitmapFactory.decodeResource(res, R.drawable.yuka);

		// Callbackを登録する
		getHolder().addCallback(this);

		// Threadを起動する
		isRunning = true;
		mThread = new Thread(this);
		mThread.start();
		
							
		
	}

	
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
							   int height) {
		Log.i("SURFACE", "surfaceChanged()");
		displayY = height;
		displayX = width;
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.i("SURFACE", "surfaceDestroyed()");
		mSensorManager.unregisterListener(this);

		// Threadを破棄する
		isRunning = false;
		try {
			mThread.join();
		} catch (InterruptedException ex) {
		}
		mThread = null;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.i("SURFACE", "surfaceCreaded()");
	}

	@Override
	public void run() {
		Log.i("SURFACE", "run()");
		while (isRunning) {
			Log.i("SURFACE", "loop");

			// countに+1する
			count++;
			
			Random r = new Random();
			// 床を動かす
			for (int i = 0; i < floorX.length; i++) {
				floorX[i] = floorX[i] + r.nextInt(30) - 15;
				floorY[i] = floorY[i] + r.nextInt(30) - 0;
			}	
			
			
//			//マニュアルターゲットオンオフ
//			if(pointOn == 1)
//			{
//				deltaPointX = pointX - targetX;
//				deltaPointY = pointY - targetY;
//				deltaPointR = Math.hypot((double)deltaPointX, (double)deltaPointY);
//				if(targetOn == 1 && deltaPointR < 50)
//				{
//					targetOn = -1;			
//				}
//				else
//				{
//					targetOn = 1;
//					targetX = pointX;
//					targetY = pointY;
//				}
//				pointOn = -1;
//			}
			
			// ターゲットへ移動する		
			deltaX = targetX - myX - (int)(0.5*myBitmap.getWidth());
			deltaY = targetY - myY - (int)(0.5*myBitmap.getHeight());
			deltaR = Math.hypot((double)deltaX, (double)deltaY);
			if(deltaR == 0)
			{
				vX = 0;
				vY = 0;
			}
			else
			{
				v=1;
				if(onBtnN==1&&NP>0)v=10;			
				vX = v*(int)Math.signum(deltaX);
				vY = v*(int)Math.signum(deltaY);
				vX = vX*Math.min(Math.abs(deltaX),Math.abs(10*deltaX / (int)deltaR));
				vY = vY*Math.min(Math.abs(deltaY),Math.abs(10*deltaY / (int)deltaR));
			}			
			myX += vX;
			myY += vY;
			if(myY>700)myY= 700;
			
			//マニュアルターゲットオフ
//			if(deltaR == 0)
//			{
//				targetOn = -1;
//			}
			
			
			//オートターゲット、近い奴にする
			if(targetOn == -1 && (0 < floorX.length))
			{
				deltaTargetRMin = 99999;
				for (int i = 0; i < floorX.length; i++)
				{
					deltaTargetX = floorX[i] + (int)(0.5*floorBitmap.getWidth()) - myX - (int)(0.5*myBitmap.getWidth());
					deltaTargetY = floorY[i] + (int)(0.5*floorBitmap.getHeight()) - myY - (int)(0.5*myBitmap.getHeight());
					deltaTargetR = Math.hypot((double)deltaTargetX, (double)deltaTargetY);
					if(deltaTargetR < deltaTargetRMin)
					{
						deltaTargetRMin = deltaTargetR;
						iMin = i;
					}
				}
				targetX = floorX[iMin] + (int)(0.5*floorBitmap.getWidth());
				targetY = floorY[iMin] + (int)(0.5*floorBitmap.getHeight());
				
			}
			
			
			
			// キャラクターの位置に応じて画面を移動
//			if (v < 0) {
//				if (myY < 300) {
//					base -= v / 2;
//					myY += v / 2;
//				} else {
//					myY += v;					
//				}
//			} else {
//				myY += v;
//			}
			// 当たったかどうかの判定フラグ
			boolean isAtari = false;
			iDel = 0;

			for (int i = 0; i < floorX.length; i++) {
				if (v >= 0 && (((myY < base + floorY[i] + floorBitmap.getHeight()) 
					&& (myY + myBitmap.getHeight() > base + floorY[i]))
					&& ((myX + myBitmap.getWidth() - 30 > floorX[i]) 
					&& (myX + 30 < floorX[i] + floorBitmap.getWidth())))) {

					// 床にめり込まないようにする
					//myY = (int) (base + floorY[i] - myBitmap.getHeight());
			
					// 跳ね返りの速度
					//v = -30;
					
					iDel = i;
					
					// 当たった場合は、trueにする
					isAtari = true;

					// 当たったらforループをbreak
					break;
				}
			}

			/**
			 * 当たったら消す
			 */
			if (isAtari) 
			{		
				listX = new ArrayList<Integer>(Arrays.asList(floorX));
				listX.remove(iDel);
				floorX = listX.toArray(new Integer[listX.size()]);
				listY = new ArrayList<Integer>(Arrays.asList(floorY));
				listY.remove(iDel);
				floorY = listY.toArray(new Integer[listY.size()]);								
			}
			
			//境界にあたったか判定
			ArrayList<Integer> tempBoundaryI = new ArrayList<Integer>();
			
			for (int i = 0; i < floorX.length; i++)
			{
				if((floorY[i] + (int)(0.5*floorBitmap.getHeight())) > 700)
				{
					tempBoundaryI.add(i);
				}
			}
			
			//境界にあたったら消す
			if (tempBoundaryI.size() > 0) 
			{		
				for (int i = (tempBoundaryI.size() - 1); i >= 0; i--)
				{
					listX = new ArrayList<Integer>(Arrays.asList(floorX));
					listX.remove((int)tempBoundaryI.get(i));
					floorX = listX.toArray(new Integer[listX.size()]);
					listY = new ArrayList<Integer>(Arrays.asList(floorY));
					listY.remove((int)tempBoundaryI.get(i));
					floorY = listY.toArray(new Integer[listY.size()]);
					HP = HP - 10;
				}
												
			}
			
			//床を増やす
			if(r.nextInt(100)<10)
			{
				listX = new ArrayList<Integer>(Arrays.asList(floorX));
				listX.add(r.nextInt(700));
				floorX = listX.toArray(new Integer[listX.size()]);
				listY = new ArrayList<Integer>(Arrays.asList(floorY));
				listY.add(0);
				floorY = listY.toArray(new Integer[listY.size()]);
			}
			
			//NPの変更
			if(onBtnN == 1)
			{
				if(NP>0)NP--;
			}
			if(onBtnN == -1)
			{
				if(NP<MNP&&count%10==1)NP++;
			}
			
			// Canvasを取得する
			Canvas canvas = getHolder().lockCanvas();

			if (canvas != null) {

				// 背景を青くする
				canvas.drawColor(Color.BLUE);
				
				// 描画するための線の色を設定
				// Paintの設定
				Paint paint = new Paint();
				paint.setAntiAlias(true);		
				
				// ステータスバー
				paint.setARGB(255, 100, 100, 100);
				canvas.drawRect(0, 700, 800, 1100, paint);

				// 描画するための線の色を設定			
				Paint mainPaint = new Paint();
				mainPaint.setStyle(Paint.Style.FILL);
				mainPaint.setARGB(255, 255, 255, 100);

				// 床の描画
				for (int i = 0; i < floorX.length; i++) {
					canvas.drawBitmap(floorBitmap, floorX[i], base + floorY[i], mainPaint);
				}				
				
				// ネコ画像の描画
				canvas.drawBitmap(myBitmap, myX, myY, mainPaint);  

				// ポイントを半透明で描画		
				if(pointOn == 1)
				{
					paint.setARGB(228, 255, 255, 255);
					canvas.drawCircle(pointX, pointY, 50, paint);
				}
				
				// ターゲットを半透明で描画
				paint.setARGB(128, 255, 255, 100);
				if(targetOn == 1)
				{
					canvas.drawCircle(targetX, targetY, 50, paint);
				}
				
				// Nボタン表示		
				if(onBtnN == 1)
				{
					paint.setARGB(255, 200, 200, 200);
					canvas.drawCircle(290, 980, 50, paint);
					paint.setStyle(Paint.Style.FILL_AND_STROKE);
					paint.setStrokeWidth(3);
					paint.setTextSize(50);
					paint.setARGB(255, 255, 255, 255);
					canvas.drawText("N", xBtnN, yBtnN, paint);
				}
				if(onBtnN == -1)
				{
					paint.setARGB(55, 200, 200, 200);
					canvas.drawCircle(290, 980, 50, paint);
					paint.setStyle(Paint.Style.FILL_AND_STROKE);
					paint.setStrokeWidth(3);
					paint.setTextSize(50);
					paint.setARGB(55, 255, 255, 255);
					canvas.drawText("N", xBtnN, yBtnN, paint);
				}
				
				// 文字を描画
				canvas.drawText("count:" + count, 20, 20, mainPaint);
				canvas.drawText("iDel:" + iDel, 20, 40, mainPaint);
				canvas.drawText("isAtari:" + isAtari, 20, 60, mainPaint);				
				canvas.drawText("pointX:" + pointX, 20, 80, mainPaint);
				canvas.drawText("pointY:" + pointY, 20, 100, mainPaint);
				canvas.drawText("deltaX:" + deltaX, 20, 120, mainPaint);
				canvas.drawText("deltaY:" + deltaY, 20, 140, mainPaint);
				canvas.drawText("deltaR:" + deltaR, 20, 160, mainPaint);
				canvas.drawText("vX:" + vX, 20, 180, mainPaint);
				canvas.drawText("vY:" + vY, 20, 200, mainPaint);
				paint.setStyle(Paint.Style.FILL_AND_STROKE);
				paint.setStrokeWidth(3);
				paint.setTextSize(50);
				paint.setColor(Color.argb(255, 255, 255, 0));
				canvas.drawText("HP:" + HP + "/" +MHP, 200, 850, paint);
				canvas.drawText("NP:" + NP + "/" +MNP, 200, 900, paint);
				
				
				// 画面に描画をする
				getHolder().unlockCanvasAndPost(canvas);
			}

			try {
				Thread.sleep(100);
			} catch (Exception e) {
			}
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		Log.i("SURFACE", "onAccuracyChanged()");
	}

	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {
		Log.i("SURFACE", "SensorChanged()");
		if (sensorEvent.sensor.getType() == Sensor.TYPE_ORIENTATION) {
//			Log.i("SURFACE", "yaw:" + sensorEvent.values[0]);
//			Log.i("SURFACE", "picth:" + sensorEvent.values[1]);
//			Log.i("SURFACE", "roll:" + sensorEvent.values[2]);

			//myX -= sensorEvent.values[2]/10;
			//myY -= sensorEvent.values[1]/10;
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		switch (event.getAction()) 
		{
			case MotionEvent.ACTION_DOWN:
				//Log.d("TouchEvent", "getAction()" + "ACTION_DOWN");
				//X軸の取得
				pointX = (int)event.getX();
				//Y軸の取得
				pointY = (int)event.getY();	
								
				deltaPointX = pointX - xBtnN;
				deltaPointY = pointY - yBtnN;
				deltaPointR = Math.hypot((double)deltaPointX, (double)deltaPointY);
				if(deltaPointR < 75)
				{
					onBtnN = -1 * onBtnN;			
				}				
						
				pointOn = 1;
				break;
				
			case MotionEvent.ACTION_MOVE:
				//Log.d("TouchEvent", "getAction()" + "ACTION_MOVE");
				//X軸の取得
				pointX = (int)event.getX();
				//Y軸の取得
				pointY = (int)event.getY();	
				
				deltaPointX = pointX - xBtnN;
				deltaPointY = pointY - yBtnN;
				deltaPointR = Math.hypot((double)deltaPointX, (double)deltaPointY);
				if(inBtnN == -1 && deltaPointR < 75)
				{
					onBtnN = -1 * onBtnN;	
					inBtnN = 1;
				}			
				if(deltaPointR > 75)
				{
					inBtnN = -1;
				}
				
				break;
			
			case MotionEvent.ACTION_UP:
				//マニュアルターゲットオンオフ
				
				//Log.d("TouchEvent", "getAction()" + "ACTION_DOWN");
				//X軸の取得
				pointX = (int)event.getX();
				//Y軸の取得
				pointY = (int)event.getY();			
				
				if(pointY < 700)
				{
					deltaPointX = pointX - targetX;
					deltaPointY = pointY - targetY;
					deltaPointR = Math.hypot((double)deltaPointX, (double)deltaPointY);
					if(targetOn == 1 && deltaPointR < 50)
					{
						targetOn = -1;			
					}
					else
					{
						targetOn = 1;
						targetX = pointX;
						targetY = pointY;
					}
				}
				
				
				
				pointOn = -1;
				break;
		}
		//取得した内容をログに表示
		//Log.d("TouchEvent", "X:" + pointX + ",Y:" + pointY);

		return true;
	}
	
	
	
}
