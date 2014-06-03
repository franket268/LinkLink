package com.gzh.core;

import java.util.ArrayList;
import java.util.List;

import com.gzh.object.GameConf;
import com.gzh.view.Piece;


// ��������ֲ�����Ϸ����

public class FullBoard extends AbstractBoard
{
	@Override
	protected List<Piece> createPieces(GameConf config,
		Piece[][] pieces)
	{
		// ����һ��Piece����, �ü��������ų�ʼ����Ϸʱ�����Piece����
		List<Piece> notNullPieces = new ArrayList<Piece>();
		for (int i = 1; i < pieces.length - 1; i++)
		{
			for (int j = 1; j < pieces[i].length - 1; j++)
			{
				// �ȹ���һ��Piece����, ֻ��������Piece[][]�����е�����ֵ��
				// ����Ҫ��PieceImage���丸�ฺ�����á�
				Piece piece = new Piece(i, j);
				// ��ӵ�Piece������
				notNullPieces.add(piece);
			}
		}
		return notNullPieces;
	}
}
