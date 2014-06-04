package com.gzh.core;

import com.gzh.object.LinkInfo;
import com.gzh.view.Piece;


public interface GameService
{
	/**
	 * ������Ϸ��ʼ�ķ���
	 */
	void start();

	/**
	 * ����һ���ӿڷ���, ���ڷ���һ����ά����
	 * 
	 * @return ��ŷ������Ķ�ά����
	 */
	Piece[][] getPieces();
	
	/**
	 * �жϲ���Piece[][]�������Ƿ񻹴��ڷǿյ�Piece����
	 * 
	 * @return �����ʣPiece���󷵻�true, û�з���false
	 */
	boolean hasPieces();
	
	/**
	 * ��������x�����y����, ���ҳ�һ��Piece����
	 * 
	 * @param touchX �������x����
	 * @param touchY �������y����
	 * @return ���ض�Ӧ��Piece����, û�з���null
	 */
	Piece findPiece(float touchX, float touchY);

	/**
	 * �ж�����Piece�Ƿ��������, ��������, ����LinkInfo����
	 * 
	 * @param p1 ��һ��Piece����
	 * @param p2 �ڶ���Piece����
	 * @return �����������������LinkInfo����, �������Piece����������, ����null
	 */
	LinkInfo link(Piece p1, Piece p2);
}
