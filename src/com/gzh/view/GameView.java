package com.gzh.view;

import com.gzh.core.GameService;

import Object.LinkInfo;
import android.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

public class GameView extends View {
	private GameService gameService;
	private Piece selectedPiece;
	private LinkInfo linkInfo;//������Ϣ����
	private Paint paint;
	private Bitmap selectImage;
	

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.paint=new Paint();
		//ʹ��λͼƽ����Ϊ��������
		this.paint.setShader(new BitmapShader(BitmapFactory.decodeResource(context.getResources(), R.drawable.heart),Shader.TileMode.REPEAT,Shader.TileMode.REPEAT));
		//����������ϸ
		this.paint.setStrokeWidth(9);
		this.selectImage=ImageUtil.getSelectImage(context);
	}
	
	public void setLinkInfo(LinkInfo linkInfo){
		this.linkInfo=linkInfo;
	}
	public void setGameService(GameService gameService){
		this.gameService=gameService;
	}
	
	@Override
	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		if(this.gameService==null){
			return;
		}
		Piece[][] pieces=gameService.getPieces();
		if(pieces!=null)
		{
			for(int i=0;i<pieces.length;i++)
			{
				for(int j=0;j<pieces[i].length;j++)
				{
					//�����λ���鲻Ϊ�գ����з��飬����������ͼƬ������
					if(pieces[i][j]!=null){
						Piece piece=pieces[i][j];
						//���ݷ�������Ͻ�X,Y������Ʒ���
						canvas.drawBitmap(piece.getImage().getImage(),piece.getBeginX(),piece.getBeginY() ,null);
					}
				}
			}
				
		}
		
		//�����ǰ������linkInfo���󣬼�������Ϣ
		if(this.linkInfo)!=null)
		{
			//����������
			
		}
		
		
	}


}
