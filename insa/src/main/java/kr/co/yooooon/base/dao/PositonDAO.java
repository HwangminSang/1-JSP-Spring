package kr.co.yooooon.base.dao;

import java.util.ArrayList;

import kr.co.yooooon.base.to.PositionTO;

public interface PositonDAO {
	public void updatePosition(PositionTO position);
	public void insertPosition(PositionTO position);
	public void deletePosition(PositionTO position);
	public ArrayList<PositionTO> selectPositonList();
}
