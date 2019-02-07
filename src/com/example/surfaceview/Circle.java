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
		Intent selectIntent = new Intent();
		switch (item.getItemId())
		{
			case R.id.mainMenuTop:	
				selectIntent.setClassName("com.example.surfaceview", "com.example.surfaceview.MainActivity");
				startActivity(selectIntent);
				return true;
			case R.id.mainMenuGame:
				selectIntent.setClassName("com.example.surfaceview", "com.example.surfaceview.Circle");
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
	public static int DX = 20;

	/**
	 * 画面のグリッド(Y軸)
	 */
	public static int DY = 20;

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
	public static Integer[] myX = new Integer[] {DX * 25, DX * 30, DX * 30};

	/**
	 * 自分の　y座標
	 */
	public static Integer[] myY = new Integer[] {DY * 15, DY * 10, DY * 20};
	
	private Integer[] myTargetX = new Integer[] {DX * 25, DX * 30, DX * 30};
	private Integer[] myTargetY = new Integer[] {DY * 15, DY * 10, DY * 20};
	private Integer targetX;
	private Integer targetY;
	
	private int pointX;
	private int pointY;
	

	/**
	 * 床のx座標
	 */
//	private int floorX[] = {DX * 15, DX * 12, DX * 8, DX * 3, DX * 15, DX * 8, DX * 1, DX * 3};
//	private int floorX[] = {DX * 15, DX * 12, DX * 8};
	
	private Integer[] arrX = new Integer[] {DX * 3, DX * 10, DX * 10};
	// 配列をリストで削除し戻す
	private List<Integer> listX = new ArrayList<Integer>(Arrays.asList(arrX));
	//listX.remove(i);
	private Integer[] enX = listX.toArray(new Integer[listX.size()]);
	private Integer[] enTargetX = new Integer[] {DX * 3, DX * 10, DX * 10};
	
	
	
	/**
	 * 床y座標
	 */
//	private int floorY[] = {DY * 30, DY * 25, DY * 20, DY * 15, DY * 10, DY * 5, DY * 0, DY * -5};	
//	private int floorY[] = {DY * 30, DY * 25, DY * 20};
	//private int floorY[] = {DY * 30, DY * 20};
	
	private Integer[] arrY = new Integer[] {DY * 15, DY * 10, DY * 20};
	// 配列をリストで削除し戻す
	private List<Integer> listY = new ArrayList<Integer>(Arrays.asList(arrY));
	//listY.remove(i);
	private Integer[] enY = listY.toArray(new Integer[listY.size()]);
	private Integer[] enTargetY = new Integer[] {DY * 15, DY * 10, DY * 20};
		
	
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

	private Integer deltaY;

	private Integer deltaX;

	private Integer vX;

	private double deltaR;

	private Integer vY;

	private Integer deltaTargetX;

	private Integer deltaTargetY;

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

	private Bitmap myBitmapR;

	private Bitmap[] myBitmapList;
	
	private Bitmap[] enBitmapList;

	private int myBitmapI = 0;

	private Bitmap myBitmap1;
	
	private int enN = 3;

	private int myN = 3;

	private Integer[] myVX = new Integer[] {0, 0, 0};

	private Integer[] myVY = new Integer[] {0, 0, 0};

	private Integer[] myTargetI = new Integer[] {0, 0, 0};

	private Integer[] enVX = new Integer[] {0, 0, 0};

	private Integer[] enVY = new Integer[] {0, 0, 0};

	private Integer[] enTargetI = new Integer[] {0, 0, 0};

	private Integer[] enBitmapLR = new Integer[] {1, 1, 1};
	private Integer[] myBitmapLR = new Integer[] {-1, -1, -1};
	
	private Matrix matrix;

	private Integer[] myPerAtk = new Integer[] {0, 0, 0};
	private Integer[] myBaseX = new Integer[] {0, 0, 0};

	private Integer[] myBaseY = new Integer[] {0, 0, 0};

	private Integer[] myMoveX = new Integer[] {0, 0, 0};

	private Integer[] myMoveY = new Integer[] {0, 0, 0};

	private Integer[] myEfctX = new Integer[] {0, 0, 0};

	private Integer[] myEfctY = new Integer[] {0, 0, 0};

	private Integer[] myStAtk = new Integer[] {0, 0, 0};

	private Integer[] enBaseX = new Integer[] {0, 0, 0};

	private Integer[] enBaseY = new Integer[] {0, 0, 0};

	private Integer[] enMoveX = new Integer[] {0, 0, 0};

	private Integer[] enMoveY = new Integer[] {0, 0, 0};

	private Integer[] enEfctX = new Integer[] {0, 0, 0};

	private Integer[] enEfctY = new Integer[] {0, 0, 0};

	private Integer[] enStAtk = new Integer[] {0, 0, 0};
	
	
	
	private Bitmap efSwdBitmapListR;

	public static Bitmap[] efSwdBitmapList;
	
	private Integer[] efX = new Integer[] {};
	private Integer[] efY = new Integer[] {};

	private int rowN;

	private int colN;

	private Bitmap[] efBitmapList = new Bitmap[] {};

	private ArrayList<Bitmap> listB;

	private Integer[] efI = new Integer[] {};
	private Integer[] efMy = new Integer[] {};
	private Integer[] efEn = new Integer[] {};
	
	private Integer[] efVecX = new Integer[] {};

	private Integer[] efVecY = new Integer[] {};

	private Integer[] enStDmg = new Integer[] {0, 0, 0};
	private Integer[] enDmgVX = new Integer[] {0, 0, 0};
	private Integer[] enDmgVY = new Integer[] {0, 0, 0};
	
	private Integer[] myStDmg = new Integer[] {0, 0, 0};
	private Integer[] myDmgVX = new Integer[] {0, 0, 0};
	private Integer[] myDmgVY = new Integer[] {0, 0, 0};

	private Integer bfAtk = 30;

	private Integer[] myV = new Integer[] {0, 0, 0};

	private Integer[] enV = new Integer[] {0, 0, 0};

	private Integer[] enPerAtk = new Integer[] {0, 0, 0};

	private Bitmap bitmap;

	private Bitmap efGigaslashBitmapList;
	private Bitmap[] efGigaslashBitmapListR = new Bitmap[] {};

	private Bitmap[] efGigaslashBitmapListL = new Bitmap[] {};
	
	public static ArrayList<MkEf> ef = new ArrayList<MkEf>();
	public static ArrayList<Act> ac = new ArrayList<Act>();
	
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
		myBitmapList = new Bitmap[myN];

		// 画像の読み込み(res/drawable/yuka.png) 使用する素材の名前はわかりやすいものに変更してください
		myBitmapList[0] = BitmapFactory.decodeResource(res, R.drawable.valkyrie);
		myBitmapList[1] = BitmapFactory.decodeResource(res, R.drawable.leila);
		myBitmapList[2] = BitmapFactory.decodeResource(res, R.drawable.rota);

		for(int i = 0; i < myX.length; i++)
		{			
			myBitmapList[i] = Bitmap.createScaledBitmap( myBitmapList[i], myBitmapList[i].getWidth()/5, myBitmapList[i].getHeight()/5, false );
		}

		
		//myBitmap = myBitmapList[0];
		
		enBitmapList = new Bitmap[enN];
		
		// 敵画像の読み込み(res/drawable/yuka.png) 使用する素材の名前はわかりやすいものに変更してください
		enBitmapList[0] = BitmapFactory.decodeResource(res, R.drawable.penguin);
		enBitmapList[1] = BitmapFactory.decodeResource(res, R.drawable.kuma);
		enBitmapList[2] = BitmapFactory.decodeResource(res, R.drawable.petitmetal);

		matrix = new Matrix();
		// 左右反転
		matrix.preScale(-1, 1);
		for(int i = 0; i < enX.length; i++)
		{
			enBitmapList[i] = Bitmap.createBitmap(enBitmapList[i], 0, 0, enBitmapList[i].getWidth(), enBitmapList[i].getHeight(), matrix, false);	
			enBitmapList[i] = Bitmap.createScaledBitmap( enBitmapList[i], enBitmapList[i].getWidth()/5, enBitmapList[i].getHeight()/5, false );
		}
		
		// 画像読込ギガスラッシュ
		bitmap = BitmapFactory.decodeResource(res, R.drawable.efgigaslash);
		bitmap = Bitmap.createScaledBitmap( bitmap, bitmap.getWidth()/5, bitmap.getHeight()/5, false );
		efGigaslashBitmapList = mkBitmapListR(9,2,bitmap);
//		efGigaslashBitmapListL = mkBitmapListL(9,2,bitmap);
		
		// 技画像の読み込み(res/drawable/yuka.png) 使用する素材の名前はわかりやすいものに変更してください
		efSwdBitmapListR = BitmapFactory.decodeResource(res, R.drawable.efswd);	
		//- 分割する一つ一つの画像サイズを取得
		rowN = 1;
		colN = 9;
		int rowSize = efSwdBitmapListR.getHeight() / rowN;
		int colSize = efSwdBitmapListR.getWidth() / colN;
		//- 全画像数
		int totalImageNum = rowN * colN;
		//- X座標
		int x = 0;
		//- Y座標
		int y = 0;
		//- 分割イメージのリスト
		efSwdBitmapList = new Bitmap[totalImageNum];
		for( int i = 0; i < totalImageNum; i++ ) 
		{
			//- 3分割毎にx座標を0に戻す
			if ( i > 0 && i%colN == 0 ) 
			{
				x = 0;
				y += rowSize;
				//- 初回以外はx座標を1サイズ分足していく
			} else if ( i > 0 ) 
			{
				x += colSize;
			}
			efSwdBitmapList[i] = Bitmap.createBitmap(efSwdBitmapListR, x, y, colSize, rowSize );
		}
		
		// Callbackを登録する
		getHolder().addCallback(this);

		// Threadを起動する
		isRunning = true;
		mThread = new Thread(this);
		mThread.start();
		
							
		
	}

//	public Bitmap[] mkBitmapListL(int rowN,int colN, Bitmap bitmap)
//	{
//	 	Bitmap[] bitmapList = mkBitmapListR(rowN,colN,bitmap);
////		matrix = new Matrix();
////		// 左右反転
////		matrix.preScale(-1, 1);
////		for(int i = 0; i < bitmapList.length; i++)
////		{
////			bitmapList[i] = Bitmap.createBitmap(bitmapList[i], 0, 0, bitmapList[i].getWidth(), bitmapList[i].getHeight(), matrix, false);	
////		}
//		return bitmapList;
//	}
	
	public Bitmap[] tmp(int rowN, int colN, Bitmap bitmap){
//		rowN = 2;
//		colN = 9;
		//- 分割する一つ一つの画像サイズを取得
		int rowSize = bitmap.getHeight() / rowN;
		int colSize = bitmap.getWidth() / colN;
		//- 全画像数
		int totalImageNum = rowN * colN;
		//- X座標
		int x = 0;
		//- Y座標
		int y = 0;
		//- 分割イメージのリスト
		Bitmap[] bitmapList = new Bitmap[totalImageNum];
		for( int i = 0; i < totalImageNum; i++ ) 
		{
			//- 3分割毎にx座標を0に戻す
			if ( i > 0 && i%colN == 0 ) 
			{
				x = 0;
				y += rowSize;
				//- 初回以外はx座標を1サイズ分足していく
			} else if ( i > 0 ) 
			{
				x += colSize;
			}
			bitmapList[i] = Bitmap.createBitmap(bitmap, x, y, colSize, rowSize );
		}
		//bitmapList[0] = bitmap;
		
		return bitmapList;
	}
	
	
	
	public Bitmap mkBitmapListR(int rowN,int colN, Bitmap bitmap)
	{
//		//- 分割する一つ一つの画像サイズを取得
//		int rowSize = bitmap.getHeight() / rowN;
//		int colSize = bitmap.getWidth() / colN;
//		//- 全画像数
//		int totalImageNum = rowN * colN;
//		//- X座標
//		int x = 0;
//		//- Y座標
//		int y = 0;
//		//- 分割イメージのリスト
//		Bitmap[] bitmapList = new Bitmap[totalImageNum];
//		for( int i = 0; i < totalImageNum; i++ ) 
//		{
//			//- 3分割毎にx座標を0に戻す
//			if ( i > 0 && i%colN == 0 ) 
//			{
//				x = 0;
//				y += rowSize;
//				//- 初回以外はx座標を1サイズ分足していく
//			} else if ( i > 0 ) 
//			{
//				x += colSize;
//			}
//			bitmapList[i] = Bitmap.createBitmap(bitmap, x, y, colSize, rowSize );
//		}
//		return bitmapList;
		return bitmap;
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
			
			//アクションを消す		
			for(int k = ac.size()-1; k >= 0; k--){
				if(ac.get(k).del() == true){
					ac.remove(k);				
				}
			}
			
			//エフェクトを消す		
			for(int k = ef.size()-1; k >= 0; k--){
				if(ef.get(k).del() == true){
					ef.remove(k);				
				}
			}
			
//			// 床を動かす
//			for (int i = 0; i < floorX.length; i++) {
//				floorX[i] = floorX[i] + r.nextInt(30) - 15;
//				floorY[i] = floorY[i] + r.nextInt(30) - 0;
//			}	
			
			
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
			
			// 味方がターゲットへ移動する
			for(int i = 0; i < myX.length; i++)
			{
				deltaX = myTargetX[i] - myX[i];
				deltaY = myTargetY[i] - myY[i];
				deltaR = Math.hypot((double)deltaX, (double)deltaY);
				if(deltaR == 0)
				{
					myV[i] = 0;
					myVX[i] = 0;
					myVY[i] = 0;
				}
				else
				{
					myV[i] += r.nextInt(10);
					if(myV[i]>30)myV[i]=30;
					if(onBtnN==1&&NP>0)v=10;			
					vX = myV[i]*(int)Math.signum(deltaX);
					vY = myV[i]*(int)Math.signum(deltaY);
					vX = vX*Math.min(100*Math.abs(deltaX),Math.abs(2*deltaX / (int)deltaR));
					vY = vY*Math.min(100*Math.abs(deltaY),Math.abs(2*deltaY / (int)deltaR));				
					if(Math.abs(myVX[i]+vX)<31)myVX[i] += vX;
					if(Math.abs(myVY[i]+vY)<31)myVY[i] += vY;
					myVX[i] = vX + r.nextInt(10) - 5;
					myVY[i] = vY + r.nextInt(10) - 5;				
				}		
				if(myVX[i] != 0 && Math.signum(myVX[i]) != myBitmapLR[i])
				{				
					myBitmapLR[i] = -myBitmapLR[i];
					myBitmapList[i] = Bitmap.createBitmap(myBitmapList[i], 0, 0, myBitmapList[i].getWidth(), myBitmapList[i].getHeight(), matrix, false);				
				}
				myX[i] += myVX[i];
				myY[i] += myVY[i];
				if(myY[i]>700)myY[i]= 600;
				if(myY[i]<0)myY[i]= 100;
				if(myX[i]>700)myX[i]= 600;
				if(myX[i]<0)myX[i]= 100;
			}
			
			// 敵がターゲットへ移動する
			for(int i = 0; i < enX.length; i++)
			{
				deltaX = enTargetX[i] - enX[i];
				deltaY = enTargetY[i] - enY[i];
				deltaR = Math.hypot((double)deltaX, (double)deltaY);
				if(deltaR == 0)
				{
					enV[i] = 0;
					enVX[i] = 0;
					enVY[i] = 0;
				}
				else
				{
					enV[i] += r.nextInt(10);
					if(enV[i]>30)enV[i]=30;
					if(onBtnN==1&&NP>0)v=10;			
					vX = enV[i]*(int)Math.signum(deltaX);
					vY = enV[i]*(int)Math.signum(deltaY);
					vX = vX*Math.min(100*Math.abs(deltaX),Math.abs(2*deltaX / (int)deltaR));
					vY = vY*Math.min(100*Math.abs(deltaY),Math.abs(2*deltaY / (int)deltaR));				
					if(Math.abs(enVX[i]+vX)<31)enVX[i] += vX;
					if(Math.abs(enVY[i]+vY)<31)enVY[i] += vY;
					enVX[i] = vX + r.nextInt(10) - 5;
					enVY[i] = vY + r.nextInt(10) - 5;
				}			
				if(enVX[i] != 0 && Math.signum(enVX[i]) != enBitmapLR[i])
				{
					enBitmapLR[i] = -enBitmapLR[i];
					enBitmapList[i] = Bitmap.createBitmap(enBitmapList[i], 0, 0, enBitmapList[i].getWidth(), enBitmapList[i].getHeight(), matrix, false);
				}
				enX[i] += enVX[i];
				enY[i] += enVY[i];
				if(enY[i]>700)enY[i]= 600;
				if(enY[i]<0)enY[i]= 100;
				if(enX[i]>700)enX[i]= 600;
				if(enX[i]<0)enX[i]= 100;
			}
			
			//マニュアルターゲットオフ
//			if(deltaR == 0)
//			{
//				targetOn = -1;
//			}
			
			
			//味方オートターゲット、近い奴にする
			if(targetOn == -1 && (0 < enX.length))
			{
				for (int j = 0; j < myX.length; j++)
				{
					deltaTargetRMin = 99999;
					for (int i = 0; i < enX.length; i++)
					{
						deltaTargetX = enX[i] - myX[j];
						deltaTargetY = enY[i] - myY[j];
						deltaTargetR = Math.hypot((double)deltaTargetX, (double)deltaTargetY);
						if(deltaTargetR < deltaTargetRMin)
						{
							deltaTargetRMin = deltaTargetR;
							iMin = i;
						}
					}
					myTargetX[j] = enX[iMin];
					myTargetY[j] = enY[iMin];
					myTargetI[j] = iMin;
				}
				
			}
			
			//敵オートターゲット、近い奴にする
			if(0 < myX.length)
			{
				for (int j = 0; j < enX.length; j++)
				{
					deltaTargetRMin = 99999;
					for (int i = 0; i < myX.length; i++)
					{
						deltaTargetX = myX[i] - enX[j];
						deltaTargetY = myY[i] - enY[j];
						deltaTargetR = Math.hypot((double)deltaTargetX, (double)deltaTargetY);
						if(deltaTargetR < deltaTargetRMin)
						{
							deltaTargetRMin = deltaTargetR;
							iMin = i;
						}
					}
					enTargetX[j] = myX[iMin];
					enTargetY[j] = myY[iMin];
					enTargetI[j] = iMin;
				}

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
			
			//味方元の位置に戻る
			for (int j = 0; j < myX.length; j++)
			{
				if(myStAtk[j] == -1)
				{
					myStAtk[j] = 0;
				}
				if(myStAtk[j] == 1)
				{
					myX[j] = myBaseX[j];
					myY[j] = myBaseY[j];
					myV[j] = 0;
					myStAtk[j] = -1;
					for(int k = 0; k < efX.length; k++)
					{
						if(efI[k] == j)
						{
							listX = new ArrayList<Integer>(Arrays.asList(efI));
							listX.remove(k);
							efI = listX.toArray(new Integer[listX.size()]);
							listX = new ArrayList<Integer>(Arrays.asList(efX));
							listX.remove(k);
							efX = listX.toArray(new Integer[listX.size()]);
							listX = new ArrayList<Integer>(Arrays.asList(efY));
							listX.remove(k);
							efY = listX.toArray(new Integer[listX.size()]);
							listB = new ArrayList<Bitmap>(Arrays.asList(efBitmapList));
							listB.remove(k);
							efBitmapList = listB.toArray(new Bitmap[listB.size()]);				
						}
					}
				}
			}
				
			//敵元の位置に戻る
			for (int i = 0; i < enX.length; i++)
			{
				if(enStAtk[i] == -1)
				{
					enStAtk[i] = 0;
				}
				if(enStAtk[i] == 1)
				{
					enX[i] = enBaseX[i];
					enY[i] = enBaseY[i];
					enV[i] = 0;
					enStAtk[i] = -1;
					for(int k = 0; k < efX.length; k++)
					{
						if(efI[k] == i)
						{
							listX = new ArrayList<Integer>(Arrays.asList(efI));
							listX.remove(k);
							efI = listX.toArray(new Integer[listX.size()]);
							listX = new ArrayList<Integer>(Arrays.asList(efX));
							listX.remove(k);
							efX = listX.toArray(new Integer[listX.size()]);
							listX = new ArrayList<Integer>(Arrays.asList(efY));
							listX.remove(k);
							efY = listX.toArray(new Integer[listX.size()]);
							listB = new ArrayList<Bitmap>(Arrays.asList(efBitmapList));
							listB.remove(k);
							efBitmapList = listB.toArray(new Bitmap[listB.size()]);				
						}
					}
				}
			}
			
			//味方ダメージ移動
			for (int j = 0; j < myX.length; j++)
			{
				if(myStAtk[j] <= 0 && myStDmg[j] == 1)
				{
					myX[j] += myDmgVX[j];
					myY[j] += myDmgVY[j];
					if(myY[j]>700)myY[j]= 600;
					if(myY[j]<0)myY[j]= 100;
					if(myX[j]>700)myX[j]= 600;
					if(myX[j]<0)myX[j]= 100;
					myDmgVX[j] = 0;
					myDmgVY[j] = 0;
					myV[j] = 0;
					myStDmg[j] = 0;
				}
			}
			
			//敵ダメージ移動
			for (int i = 0; i < enX.length; i++)
			{
				if(enStAtk[i] <= 0 && enStDmg[i] == 1)
				{
					enX[i] += enDmgVX[i];
					enY[i] += enDmgVY[i];
					if(enY[i]>700)enY[i]= 600;
					if(enY[i]<0)enY[i]= 100;
					if(enX[i]>700)enX[i]= 600;
					if(enX[i]<0)enX[i]= 100;
					enDmgVX[i] = 0;
					enDmgVY[i] = 0;
					enV[i] = 0;
					enStDmg[i] = 0;
				}
			}
			
			// 味方の攻撃判定フラグ
			for (int j = 0; j < myX.length; j++)
			{		
				int i = myTargetI[j];
				if (myStAtk[j] == 0 
					&& (((myY[j] - Math.max(10,0.5*myBitmapList[j].getHeight()) - bfAtk < enY[i] + Math.max(10,0.5*enBitmapList[i].getHeight()) + bfAtk ) 
					&& (myY[j] + Math.max(10,0.5*myBitmapList[j].getHeight()) + bfAtk > enY[i] - Math.max(10,0.5*enBitmapList[i].getHeight()) - bfAtk ))
					&& ((myX[j] + Math.max(10,0.5*myBitmapList[j].getWidth()) + bfAtk > enX[i] - Math.max(10,0.5*enBitmapList[i].getWidth()) - bfAtk ) 
					&& (myX[j] - Math.max(10,0.5*myBitmapList[j].getWidth()) - bfAtk < enX[i] + Math.max(10,0.5*enBitmapList[i].getWidth()) + bfAtk )))) 
				{
					myPerAtk[j] = 50 + r.nextInt(100);
					if(myPerAtk[j] > 100)
					{
						myPerAtk[j] = 0;
						
						myBaseX[j] = myX[j] - 50*myBitmapLR[j];
						myBaseY[j] = myY[j] - r.nextInt(50) + 25;
						myStAtk[j] = 1;
						myMoveX[j] = (int)(0.6*myX[j] + 0.4*enX[i]);
						myMoveY[j] = (int)(0.6*myY[j] + 0.4*enY[i]);
						myEfctX[j] = (int)(0.4*myX[j] + 0.6*enX[i]);
						myEfctY[j] = (int)(0.4*myY[j] + 0.6*enY[i]);

						listX = new ArrayList<Integer>(Arrays.asList(efI));
						listX.add(j);
						efI = listX.toArray(new Integer[listX.size()]);
						listX = new ArrayList<Integer>(Arrays.asList(efMy));
						listX.add(0);
						efMy = listX.toArray(new Integer[listX.size()]);
						listX = new ArrayList<Integer>(Arrays.asList(efEn));
						listX.add(1);
						efEn = listX.toArray(new Integer[listX.size()]);
						listX = new ArrayList<Integer>(Arrays.asList(efX));
						listX.add(myEfctX[j]);
						efX = listX.toArray(new Integer[listX.size()]);
						listX = new ArrayList<Integer>(Arrays.asList(efY));
						listX.add(myEfctY[j]);
						efY = listX.toArray(new Integer[listX.size()]);
						listX = new ArrayList<Integer>(Arrays.asList(efVecX));
						listX.add(myBitmapLR[j]);
						efVecX = listX.toArray(new Integer[listX.size()]);
						listX = new ArrayList<Integer>(Arrays.asList(efVecY));
						listX.add((int)Math.signum(enY[i]-myY[j]));
						efVecY = listX.toArray(new Integer[listX.size()]);
						listB = new ArrayList<Bitmap>(Arrays.asList(efBitmapList));
						listB.add(efSwdBitmapList[3]);
						efBitmapList = listB.toArray(new Bitmap[listB.size()]);
						
					}
					
														
				}
			}
			
			// 敵の攻撃判定フラグ
			for (int i = 0; i < enX.length; i++)
			{		
				int j = enTargetI[i];
				if (enStAtk[i] == 0 
					&& (((myY[j] - Math.max(10,0.5*myBitmapList[j].getHeight()) - bfAtk < enY[i] + Math.max(10,0.5*enBitmapList[i].getHeight()) + bfAtk ) 
					&& (myY[j] + Math.max(10,0.5*myBitmapList[j].getHeight()) + bfAtk > enY[i] - Math.max(10,0.5*enBitmapList[i].getHeight()) - bfAtk ))
					&& ((myX[j] + Math.max(10,0.5*myBitmapList[j].getWidth()) + bfAtk > enX[i] - Math.max(10,0.5*enBitmapList[i].getWidth()) - bfAtk ) 
					&& (myX[j] - Math.max(10,0.5*myBitmapList[j].getWidth()) - bfAtk < enX[i] + Math.max(10,0.5*enBitmapList[i].getWidth()) + bfAtk)))) 
				{

					enPerAtk[i] = 50 + r.nextInt(100);
					if(enPerAtk[j] > 100)
					{
						enBaseX[i] = enX[i] - 50*enBitmapLR[i];
						enBaseY[i] = enY[i] - r.nextInt(50) + 25;
						enStAtk[i] = 1;
						enMoveX[i] = (int)(0.6*enX[i] + 0.4*myX[j]);
						enMoveY[i] = (int)(0.6*enY[i] + 0.4*myY[j]);
						enEfctX[i] = (int)(0.4*enX[i] + 0.6*myX[j]);
						enEfctY[i] = (int)(0.4*enY[i] + 0.6*myY[j]);

						listX = new ArrayList<Integer>(Arrays.asList(efI));
						listX.add(i);
						efI = listX.toArray(new Integer[listX.size()]);
						listX = new ArrayList<Integer>(Arrays.asList(efMy));
						listX.add(1);
						efMy = listX.toArray(new Integer[listX.size()]);
						listX = new ArrayList<Integer>(Arrays.asList(efEn));
						listX.add(0);
						efEn = listX.toArray(new Integer[listX.size()]);
						listX = new ArrayList<Integer>(Arrays.asList(efX));
						listX.add(enEfctX[i]);
						efX = listX.toArray(new Integer[listX.size()]);
						listX = new ArrayList<Integer>(Arrays.asList(efY));
						listX.add(enEfctY[i]);
						efY = listX.toArray(new Integer[listX.size()]);
						listX = new ArrayList<Integer>(Arrays.asList(efVecX));
						listX.add(enBitmapLR[i]);
						efVecX = listX.toArray(new Integer[listX.size()]);
						listX = new ArrayList<Integer>(Arrays.asList(efVecY));
						listX.add((int)Math.signum(myY[j]-enY[i]));
						efVecY = listX.toArray(new Integer[listX.size()]);
						listB = new ArrayList<Bitmap>(Arrays.asList(efBitmapList));
						listB.add(tmp(10,2,bitmap)[6]);
						efBitmapList = listB.toArray(new Bitmap[listB.size()]);
						
					}
					
					
				}
			}
			
			//味方攻撃移動と普通移動
			for (int j = 0; j < myX.length; j++)
			{
				if(myStAtk[j] == 0)
				{
					//ef.add(new MkEf(myX[j],myY[j],efSwdBitmapList));
				}
				if(myStAtk[j] == 1)
				{
					myX[j] = myMoveX[j];
					myY[j] = myMoveY[j];
					if(j == 0){
						ac.add(new Act(j));
					}
				}
			}
			
			//敵攻撃移動
			for (int i = 0; i < enX.length; i++)
			{
				if(enStAtk[i] == 1)
				{
					enX[i] = enMoveX[i];
					enY[i] = enMoveY[i];
				}
			}

			//アクションを進める
			for(int k = 0; k < ac.size(); k++){
				ac.get(k).run();
			}
			
			//エフェクトを進める
			for(int k = 0; k < ef.size(); k++){
				ef.get(k).run();
			}
			
			
			//味方ダメージ判定
			for (int k = 0; k < efX.length; k++)
			{		
				for (int j = 0; j < myX.length; j++)
				{
					if (efMy[k]==1 && (((efY[k] - Math.max(10,0.5*efBitmapList[k].getHeight()) < myY[j] + Math.max(10,0.5*myBitmapList[j].getHeight())) 
						&& (efY[k] + Math.max(10,0.5*efBitmapList[k].getHeight()) > myY[j] - Math.max(10,0.5*myBitmapList[j].getHeight())))
						&& ((efX[k] + Math.max(10,0.5*efBitmapList[k].getWidth() - 0) > myX[j] - Math.max(10,0.5*myBitmapList[j].getWidth())) 
						&& (efX[k] - Math.max(10,0.5*efBitmapList[k].getWidth() + 0) < myX[j] + Math.max(10,0.5*myBitmapList[j].getWidth()))))) 
					{
						myDmgVX[j] += 100*efVecX[k];
						myDmgVY[j] += 1*r.nextInt(100) - 50;//efVecY[k];
						myStDmg[j] = 1;				
					}					
				}
			}
			
			//敵ダメージ判定
			for (int k = 0; k < efX.length; k++)
			{		
				for (int i = 0; i < enX.length; i++)
				{
					if (efEn[k]==1 && (((efY[k] - Math.max(10,0.5*efBitmapList[k].getHeight()) < enY[i] + Math.max(10,0.5*enBitmapList[i].getHeight())) 
						&& (efY[k] + Math.max(10,0.5*efBitmapList[k].getHeight()) > enY[i] - Math.max(10,0.5*enBitmapList[i].getHeight())))
						&& ((efX[k] + Math.max(10,0.5*efBitmapList[k].getWidth() - 0) > enX[i] - Math.max(10,0.5*enBitmapList[i].getWidth())) 
						&& (efX[k] - Math.max(10,0.5*efBitmapList[k].getWidth() + 0) < enX[i] + Math.max(10,0.5*enBitmapList[i].getWidth()))))) 
					{
						enDmgVX[i] += 100*efVecX[k];
						enDmgVY[i] += 1*r.nextInt(100) - 50;//efVecY[k];
						enStDmg[i] = 1;				
					}					
				}
			}
			
			/**
			 * 当たったら消す
			 */
//			if (isAtari) 
//			{		
//				listX = new ArrayList<Integer>(Arrays.asList(floorX));
//				listX.remove(iDel);
//				floorX = listX.toArray(new Integer[listX.size()]);
//				listY = new ArrayList<Integer>(Arrays.asList(floorY));
//				listY.remove(iDel);
//				floorY = listY.toArray(new Integer[listY.size()]);								
//			}
			
//			Matrix matrix = new Matrix();
//			// 左右反転
//			matrix.preScale(-1, 1);
//			myBitmap = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix, false);
//			
			
//			//境界にあたったか判定
//			ArrayList<Integer> tempBoundaryI = new ArrayList<Integer>();
//			
//			for (int i = 0; i < floorX.length; i++)
//			{
//				if((floorY[i] + (int)(0.5*floorBitmap.getHeight())) > 700)
//				{
//					tempBoundaryI.add(i);
//				}
//			}
			
//			//境界にあたったら消す
//			if (tempBoundaryI.size() > 0) 
//			{		
//				for (int i = (tempBoundaryI.size() - 1); i >= 0; i--)
//				{
//					listX = new ArrayList<Integer>(Arrays.asList(floorX));
//					listX.remove((int)tempBoundaryI.get(i));
//					floorX = listX.toArray(new Integer[listX.size()]);
//					listY = new ArrayList<Integer>(Arrays.asList(floorY));
//					listY.remove((int)tempBoundaryI.get(i));
//					floorY = listY.toArray(new Integer[listY.size()]);
//					HP = HP - 10;
//				}
//												
//			}
			
//			//床を増やす
//			if(r.nextInt(100)<10)
//			{
//				listX = new ArrayList<Integer>(Arrays.asList(floorX));
//				listX.add(r.nextInt(700));
//				floorX = listX.toArray(new Integer[listX.size()]);
//				listY = new ArrayList<Integer>(Arrays.asList(floorY));
//				listY.add(0);
//				floorY = listY.toArray(new Integer[listY.size()]);
//			}
			
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

				// 敵の描画
				for (int i = 0; i < enX.length; i++) {
					canvas.drawBitmap(enBitmapList[i], enX[i] - (int)(0.5*enBitmapList[i].getWidth()), enY[i] - (int)(0.5*enBitmapList[i].getHeight()), mainPaint);
				}				
				
				// 味方の描画
				for (int i = 0; i < myX.length; i++) {				
					canvas.drawBitmap(myBitmapList[i], myX[i] - (int)(0.5*myBitmapList[i].getWidth()), myY[i] - (int)(0.5*myBitmapList[i].getHeight()), mainPaint);
				}				
				  
				// エフェクトの描画
				for (int i = 0; i < efX.length; i++) {
					canvas.drawBitmap(efBitmapList[i], efX[i] - (int)(0.5*efBitmapList[i].getWidth()), efY[i] - (int)(0.5*efBitmapList[i].getHeight()), mainPaint);
				}			
				//エフェクトを進める
				for(int k = 0; k < ef.size(); k++){
					canvas.drawBitmap(ef.get(k).bitmap(), ef.get(k).x(), ef.get(k).y(), mainPaint);				
				}
				
				
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
//				canvas.drawText("isAtari:" + isAtari, 20, 60, mainPaint);				
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

class Act {
	private Integer x ;
	private Integer y ;
	private int j ;
	private Integer t ;
	private Integer tEnd = 2;
	
	public Act(int j){
		this.t = -1;	
		//this.x = x;
		//this.y = y;
		this.j = j;
	}

	public void run(){
		this.t += 1;	
		switch(this.t){
			case 0:
				MyCircleView.myX[this.j] = 400;
				MyCircleView.myY[this.j] = 300;
				MyCircleView.ef.add(new MkEf(MyCircleView.myX[this.j],MyCircleView.myY[this.j],MyCircleView.efSwdBitmapList));
				break;
			case 1:
				MyCircleView.myX[this.j] = 450;
				MyCircleView.myY[this.j] = 300;
				MyCircleView.ef.add(new MkEf(MyCircleView.myX[this.j],MyCircleView.myY[this.j],MyCircleView.efSwdBitmapList));
				break;
			case 2:
				MyCircleView.myX[this.j] = 500;
				MyCircleView.myY[this.j] = 300;
				MyCircleView.ef.add(new MkEf(MyCircleView.myX[this.j],MyCircleView.myY[this.j],MyCircleView.efSwdBitmapList));
				break;
		}
	}

	public Boolean del(){	
		if(this.tEnd <= t){
			return true;
		}else{
			return false;	
		}	
	}

	
}

class MkEf {
	private Integer x ;
	private Integer y ;
	private Integer t ;
	private Bitmap[] bitmap;
	
	public MkEf(Integer x, Integer y, Bitmap[] bitmap){
		this.x = x;
		this.y = y;
		this.t = -1;
		this.bitmap = bitmap;
	}
	
	public void run(){
		this.t += 1;	
	}
		
	public Boolean del(){	
		if(this.bitmap.length <= t+1){
			return true;
		}else{
			return false;	
		}	
	}
	
	public Bitmap bitmap(){	
		return this.bitmap[t];	
	}
	
	public Integer x(){	
		return this.x;	
	}
	
	public Integer y(){	
		return this.y;	
	}
}
