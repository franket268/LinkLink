package com.gzh.core;

import java.util.ArrayList;
import java.util.List;

import com.gzh.object.GameConf;
import com.gzh.view.Piece;


//����������Ϸ����
public class VerticalBoard extends AbstractBoard
{
	protected List<Piece> createPieces(GameConf config,
		Piece[][] pieces)
	{
		// ����һ��Piece����, �ü��������ų�ʼ����Ϸʱ�����Piece����
		List<Piece> notNullPieces = new ArrayList<Piece>();
		for (int i = 0; i < pieces.length; i++)
		{
			for (int j = 0; j < pieces[i].length; j++)
			{
				// �����ж�, ����һ��������ȥ����Piece����, ���ӵ�������
				if (i % 2 == 0)
				{
					// ���x�ܱ�2����, �������в��ᴴ������
					// �ȹ���һ��Piece����, ֻ��������Piece[][]�����е�����ֵ��
					// ����Ҫ��PieceImage���丸�ฺ�����á�
					Piece piece = new Piece(i, j);
					// ��ӵ�Piece������
					notNullPieces.add(piece);
				}
			}
		}
		return notNullPieces;
	}
}
