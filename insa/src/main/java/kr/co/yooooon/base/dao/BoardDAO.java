package kr.co.yooooon.base.dao;

import java.util.ArrayList;

import kr.co.yooooon.base.to.BoardTO;

public interface BoardDAO {
	public ArrayList<BoardTO> selectBoardList();
	public BoardTO selectBoard(int board_seq);
	public void insertBoard(BoardTO board);
	public void updateHit(int board_seq);
	public int selectRowCount();
	public ArrayList<BoardTO> selectBoardList(int sr, int er);
	public void deleteBoard(int board_seq);
}
