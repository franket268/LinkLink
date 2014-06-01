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
	private LinkInfo linkInfo;//链接信息对象
	private Paint paint;
	private Bitmap selectImage;
	

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.paint=new Paint();
		//使用位图平铺作为连接线条
		this.paint.setShader(new BitmapShader(BitmapFactory.decodeResource(context.getResources(), R.drawable.heart),Shader.TileMode.REPEAT,Shader.TileMode.REPEAT));
		//设置线条粗细
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
					//如果二位数组不为空，即有方块，讲这个方块的图片画出来
					if(pieces[i][j]!=null){
						Piece piece=pieces[i][j];
						//根据方块的左上角X,Y坐标绘制方块
						canvas.drawBitmap(piece.getImage().getImage(),piece.getBeginX(),piece.getBeginY() ,null);
					}
				}
			}
				
		}
		
		//如果当前对象有linkInfo对象，即链接信息
		if(this.linkInfo)!=null)
		{
			//绘制连接线
			
		}
		
		
	}


}
