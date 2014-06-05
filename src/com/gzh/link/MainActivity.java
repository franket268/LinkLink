package com.gzh.link;

import java.util.Timer;
import java.util.TimerTask;

import com.gzh.R;
import com.gzh.core.GameService;
import com.gzh.core.GameServiceImpl;
import com.gzh.object.GameConf;
import com.gzh.object.LinkInfo;
import com.gzh.view.GameView;
import com.gzh.view.Piece;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.os.Build;

public class MainActivity extends Activity {

		// ��Ϸ���ö���
		private GameConf config;
		// ��Ϸҵ���߼��ӿ�
		private GameService gameService;
		// ��Ϸ����
		private GameView gameView;
		// ��ʼ��ť
		private Button startButton;

		// ��¼ʣ��ʱ���TextView
		private TextView timeTextView;
		// ʧ�ܺ󵯳��ĶԻ���
		private AlertDialog.Builder lostDialog;
		// ��Ϸʤ����ĶԻ���
		private AlertDialog.Builder successDialog;
		// ��ʱ��
		private Timer timer = new Timer();
		// ��¼��Ϸ��ʣ��ʱ��
		private int gameTime=100;
		// ��¼�Ƿ�����Ϸ״̬
		private boolean isPlaying=false;
		// �񶯴�����
		private Vibrator vibrator;
		// ��¼�Ѿ�ѡ�еķ���
		private Piece selected = null;
		private MediaPlayer mPlayer;
		private Handler handler = new Handler()
		{
			public void handleMessage(Message msg)
			{
				switch (msg.what)
				{
					case 0x123:
						timeTextView.setText("ʣ��ʱ�䣺 " + gameTime);
						gameTime--;
						// ʱ��С��0, ��Ϸʧ��
						if (gameTime < 0)
						{
							stopTimer();
							// ������Ϸ��״̬
							isPlaying = false;
							startButton.setBackgroundResource(R.drawable.play);
							lostDialog.show();
							return;
						}
						break;
				}
			}
		};

		@Override
		public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);
			// ��ʼ������
			init();
		}

		// ��ʼ����Ϸ�ķ���
		private void init()
		{
			config = new GameConf(8, 9, 2, 10 , 100000, this);
			// �õ���Ϸ�������
			gameView = (GameView) findViewById(R.id.gameView);
			// ��ȡ��ʾʣ��ʱ����ı���
			timeTextView = (TextView) findViewById(R.id.timeText);
			// ��ȡ��ʼ��ť
			startButton = (Button) this.findViewById(R.id.startButton);
			
			// ��ȡ����
			vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			gameService = new GameServiceImpl(this.config);
			gameView.setGameService(gameService);
			//��������
			mPlayer=MediaPlayer.create(this, R.raw.song);
			mPlayer.start();
			mPlayer.setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					mPlayer.start();
				}
			});
			
			// Ϊ��ʼ��ť�ĵ����¼����¼�������
			startButton.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View source)
				{ 
					if(isPlaying==false){
						startGame(gameTime);
					}
					else{
						pauseGame();
					}
					
					
				}
			});
			// Ϊ��Ϸ����Ĵ����¼��󶨼�����
			this.gameView.setOnTouchListener(new View.OnTouchListener()
			{
				public boolean onTouch(View view, MotionEvent e)
				{
					if(isPlaying==true)
					{
						if (e.getAction() == MotionEvent.ACTION_DOWN)
						{
							gameViewTouchDown(e);
						}
						if (e.getAction() == MotionEvent.ACTION_UP)
						{
							gameViewTouchUp(e);
						}
					}
					return true;
				}
			});
			// ��ʼ����Ϸʧ�ܵĶԻ���
			lostDialog = createDialog("Lost", "��Ϸʧ�ܣ� ���¿�ʼ", R.drawable.lost)
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						startGame(GameConf.DEFAULT_TIME);
					}
				});
			// ��ʼ����Ϸʤ���ĶԻ���
			successDialog = createDialog("Success", "��Ϸʤ���� ���¿�ʼ",
				R.drawable.success).setPositiveButton("ȷ��",
				new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						
						startGame(GameConf.DEFAULT_TIME);
					}
				});
		}
		@Override
		protected void onPause()
		{
			// ��ͣ��Ϸ
			stopTimer();
			mPlayer.stop();
			super.onPause();
		}
		@Override
		protected void onResume()
		{
			// ���������Ϸ״̬��
			if (isPlaying)
			{
				// ��ʣ��ʱ����д��ʼ��Ϸ
				startGame(gameTime);
			}
			super.onResume();
		}

		// ������Ϸ����Ĵ�����
		private void gameViewTouchDown(MotionEvent event)
		{
			// ��ȡGameServiceImpl�е�Piece[][]����
			Piece[][] pieces = gameService.getPieces();
			// ��ȡ�û������x����
			float touchX = event.getX();
			// ��ȡ�û������y����
			float touchY = event.getY();
			// �����û�����������õ���Ӧ��Piece����
			Piece currentPiece = gameService.findPiece(touchX, touchY);
			// ���û��ѡ���κ�Piece����(��������ĵط�û��ͼƬ), ��������ִ��
			if (currentPiece == null)
				return;
			// ��gameView�е�ѡ�з�����Ϊ��ǰ����
			this.gameView.setSelectedPiece(currentPiece);
			// ��ʾ֮ǰû��ѡ���κ�һ��Piece
			if (this.selected == null)
			{
				// ����ǰ������Ϊ��ѡ�еķ���, ���½�GamePanel����, ����������ִ��
				this.selected = currentPiece;
				this.gameView.postInvalidate();
				return;
			}
			// ��ʾ֮ǰ�Ѿ�ѡ����һ��
			if (this.selected != null)
			{
				// �������Ҫ��currentPiece��prePiece�����жϲ���������
				LinkInfo linkInfo = this.gameService.link(this.selected,
					currentPiece);
				// ����Piece������, linkInfoΪnull
				if (linkInfo == null)
				{
					// ������Ӳ��ɹ�, ����ǰ������Ϊѡ�з���
					this.selected = currentPiece;
					this.gameView.postInvalidate();
				}
				else
				{
					// ����ɹ�����
					handleSuccessLink(linkInfo, this.selected
						, currentPiece, pieces);
				}
			}
		}
		// ������Ϸ����Ĵ�����
		private void gameViewTouchUp(MotionEvent e)
		{
			this.gameView.postInvalidate();
		}
		
		// ��gameTime��Ϊʣ��ʱ�俪ʼ��ָ���Ϸ
		private void startGame(int gameTime)
		{
			startButton.setBackgroundResource(R.drawable.pause);
//			// ���֮ǰ��timer��δȡ����ȡ��timer
//			if (this.timer != null)
//			{
//				stopTimer();
//			}
			// ����������Ϸʱ��
			this.gameTime = gameTime;		
			// �����Ϸʣ��ʱ��С������Ϸʱ����ȣ���Ϊ������Ϸ
			if(gameTime < GameConf.DEFAULT_TIME)
			{
				isPlaying=true;
				
			}
			else{
				// ��ʼ�µ���Ϸ��Ϸ
				gameTime=100;
				gameView.startGame();
				isPlaying = true;
			}
			this.timer = new Timer();
			// ������ʱ�� �� ÿ��1�뷢��һ����Ϣ
			this.timer.schedule(new TimerTask()
			{
				public void run()
				{
					handler.sendEmptyMessage(0x123);
				}
			}, 0, 1000);
			// ��ѡ�з�����Ϊnull��
			this.selected = null;
			
		}	
		
		//��ͣ��Ϸ
		public void pauseGame(){
			startButton.setBackgroundResource(R.drawable.play);
			isPlaying=false;
			stopTimer();
		
		}
		

		/**
		 * �ɹ����Ӻ���
		 * 
		 * @param linkInfo ������Ϣ
		 * @param prePiece ǰһ��ѡ�з���
		 * @param currentPiece ��ǰѡ�񷽿�
		 * @param pieces ϵͳ�л�ʣ��ȫ������
		 */
		private void handleSuccessLink(LinkInfo linkInfo, Piece prePiece,
			Piece currentPiece, Piece[][] pieces)
		{
			// ���ǿ�������, ��GamePanel����LinkInfo
			this.gameView.setLinkInfo(linkInfo);
			// ��gameView�е�ѡ�з�����Ϊnull
			this.gameView.setSelectedPiece(null);
			this.gameView.postInvalidate();
			// ������Piece�����������ɾ��
			pieces[prePiece.getIndexX()][prePiece.getIndexY()] = null;
			pieces[currentPiece.getIndexX()][currentPiece.getIndexY()] = null;
			// ��ѡ�еķ�������null��
			this.selected = null;
			// �ֻ���(100����)
			this.vibrator.vibrate(100);
			// �ж��Ƿ���ʣ�µķ���, ���û��, ��Ϸʤ��
			if (!this.gameService.hasPieces())
			{
				// ��Ϸʤ��
				this.successDialog.show();
				// ֹͣ��ʱ��
				stopTimer();
				// ������Ϸ״̬
				isPlaying = false;
			}
		}

		// �����Ի���Ĺ��߷���
		private AlertDialog.Builder createDialog(String title, String message,
			int imageResource)
		{
			return new AlertDialog.Builder(this).setTitle(title)
				.setMessage(message).setIcon(imageResource);
		}
		private void stopTimer()
		{
			// ֹͣ��ʱ��
			this.timer.cancel();
		//	this.timer = null;
		}
		private void startTimer()
		{
			
		}
	}