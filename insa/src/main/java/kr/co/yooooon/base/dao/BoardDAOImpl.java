package kr.co.yooooon.base.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import kr.co.yooooon.base.to.BoardTO;
import kr.co.yooooon.common.transaction.DataSourceTransactionManager;
import kr.co.yooooon.common.util.BoardFile;


public class BoardDAOImpl implements BoardDAO{
	 private DataSourceTransactionManager dataSourceTransactionManager ;
	   public void setDataSourceTransactionManager(DataSourceTransactionManager dataSourceTransactionManager) {
		      this.dataSourceTransactionManager = dataSourceTransactionManager;
		   }

	public ArrayList<BoardTO> selectBoardList(){
		ArrayList<BoardTO> v=new ArrayList<BoardTO>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs=null;
		try {
			StringBuffer selectQuery = new StringBuffer();
			selectQuery.append("select board_seq,ref_seq,reply_seq,level,name,title,content,hit,reg_date from newBoard2 start with reply_seq=0");
			selectQuery.append("connect by prior board_seq=reply_seq ");
			selectQuery.append("order siblings by ref_seq desc"); 
			con = dataSourceTransactionManager.getConnection();
			pstmt = con.prepareStatement(selectQuery.toString());
			rs=pstmt.executeQuery();
			while(rs.next()){
				BoardTO board=new BoardTO();
				board.setBoard_seq(rs.getInt("board_seq"));
				board.setRef_seq(rs.getInt("ref_seq"));
				board.setReply_seq(rs.getInt("reply_seq"));
				board.setReply_level(rs.getInt("level"));
				board.setName(rs.getString("name"));
				board.setTitle(rs.getString("title"));
				board.setContent(rs.getString("content"));
				board.setHit(rs.getInt("hit"));
				board.setReg_date(rs.getString("reg_date"));
				v.add(board);
			}
			
			return v;
		} catch(Exception sqle) {
			throw new RuntimeException(sqle.getMessage());			
		} finally {
			dataSourceTransactionManager.close(pstmt, rs);
		}
	}
	@SuppressWarnings("resource")
	public BoardTO selectBoard(int board_seq){
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs=null;
		try {
			StringBuffer query = new StringBuffer();
			query.append("select * from newBoard2 where board_seq = ? ");
			con = dataSourceTransactionManager.getConnection();
			pstmt = con.prepareStatement(query.toString());
			pstmt.setInt(1, board_seq);
			rs=pstmt.executeQuery();
			
			
			BoardTO board=null;
			if(rs.next()){
				board=new BoardTO();
				board.setBoard_seq(rs.getInt("board_seq"));
				board.setRef_seq(rs.getInt("ref_seq"));
				board.setReply_seq(rs.getInt("reply_seq"));
				board.setHit(rs.getInt("hit"));
				board.setTitle(rs.getString("title"));
				board.setName(rs.getString("name"));
				board.setContent(rs.getString("content"));
				board.setReg_date(rs.getString("reg_date"));
				
				query.setLength(0);
				query.append("SELECT ");
				query.append("file_seq, filename, tempfilename ");
				query.append("FROM BOARDFILE2 ");
				query.append("WHERE board_seq=? ");

				pstmt=null;
				pstmt = con.prepareStatement(query.toString());
				pstmt.setInt(1, board_seq);

				rs = pstmt.executeQuery();

				while (rs.next()) {
					BoardFile file = new BoardFile();
					file.setFile_seq(rs.getInt("file_seq"));
					file.setFileName(rs.getString("filename"));
					file.setTempFileName(rs.getString("tempfilename"));
					board.addBoardFile(file);
				}
			}
			return board;
		} catch(Exception sqle) {
			throw new RuntimeException(sqle.getMessage());			
		} finally {
			dataSourceTransactionManager.close(pstmt, rs);
		}
	}	
	public void insertBoard(BoardTO board){
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs=null;
		try{
			con = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();
			query.append("select newBoard_seq2.nextval from dual");
			pstmt = con.prepareStatement(query.toString());
			rs=pstmt.executeQuery();
			rs.next();
			int boardNo=rs.getInt(1);
			pstmt.close();
			
			query.setLength(0);
			query.append("insert into newBoard2 values (?,?,?,?,?,?,?,?)");	
			pstmt = con.prepareStatement(query.toString());
			
			pstmt.setInt(1, boardNo);			
			if(board.getReply_seq()==0) {	
				pstmt.setInt(2,boardNo);	
			}else {
				pstmt.setInt(2, board.getRef_seq());
			}
			pstmt.setInt(3, board.getReply_seq());
			pstmt.setString(4, board.getName());
			pstmt.setString(5, board.getTitle());
			pstmt.setString(6, board.getContent());
			pstmt.setInt(7,0);
			pstmt.setString(8, board.getReg_date());
			pstmt.executeUpdate();
			pstmt.close();
		
			query.setLength(0);
			query.append("INSERT INTO BOARDFILE2 ");
			query.append("VALUES(boardFile_seq2.nextval, newBoard_seq2.currval,?,?)");

			pstmt = con.prepareStatement(query.toString());
			List<BoardFile> files = board.getBoardFiles();
			for( BoardFile file : files) {
				pstmt.setString(1, file.getFileName());
				pstmt.setString(2, file.getTempFileName());
				pstmt.executeUpdate();
				System.out.println("boardfile 성공????/");
			}
			
		} catch(Exception sqle) {
			sqle.printStackTrace();
			throw new RuntimeException(sqle.getMessage());			
		} finally {
			dataSourceTransactionManager.close(pstmt, rs);
		}
	}
	public void updateHit(int board_seq){
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			StringBuffer selectQuery = new StringBuffer();
			selectQuery.append("update newBoard2 set hit=hit+1 where board_seq = ? ");
			con = dataSourceTransactionManager.getConnection();
			pstmt = con.prepareStatement(selectQuery.toString());
			pstmt.setInt(1, board_seq);
			pstmt.executeUpdate();
		} catch(Exception sqle) {
			throw new RuntimeException(sqle.getMessage());			
		} finally {
			dataSourceTransactionManager.close(pstmt);	
		}
	}
	
	@Override
	public int selectRowCount(){
		// TODO Auto-generated method stub		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs=null;
		try {
			StringBuffer query = new StringBuffer();
			query.append("SELECT COUNT(*) FROM NEWBOARD2");
			//query.append("START WITH REPLY_SEQ=0");
			//query.append("CONNECT BY PRIOR BOARD_SEQ=REPLY_SEQ");
			con = dataSourceTransactionManager.getConnection();
			pstmt = con.prepareStatement(query.toString());
			rs = pstmt.executeQuery();
			rs.next();
			return rs.getInt(1);
		}catch(Exception sqle) {
			throw new RuntimeException(sqle.getMessage());			
		}finally {
			dataSourceTransactionManager.close(pstmt);		
		}
	}
	
	@Override
	public ArrayList<BoardTO> selectBoardList(int sr, int er){
		ArrayList<BoardTO> v=new ArrayList<BoardTO>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs=null;
		try {
			StringBuffer selectQuery = new StringBuffer();
			selectQuery.append("SELECT * FROM (SELECT ROWNUM AS RN, BOARD_SEQ, REF_SEQ, REPLY_SEQ, LEVEL, NAME, TITLE, CONTENT, HIT, REG_DATE FROM (SELECT * FROM NEWBOARD2) START WITH REPLY_SEQ=0 CONNECT BY PRIOR BOARD_SEQ=REPLY_SEQ ORDER SIBLINGS BY REF_SEQ DESC)A WHERE A.RN BETWEEN ? AND ?");
			con = dataSourceTransactionManager.getConnection();
			pstmt = con.prepareStatement(selectQuery.toString());	
			pstmt.setInt(1, sr);
			pstmt.setInt(2, er);
			rs=pstmt.executeQuery();
			while(rs.next()){
				BoardTO board=new BoardTO();
				board.setBoard_seq(rs.getInt("board_seq"));
				board.setRef_seq(rs.getInt("ref_seq"));
				board.setReply_seq(rs.getInt("reply_seq"));
				board.setReply_level(rs.getInt("level"));
				board.setName(rs.getString("name"));
				board.setTitle(rs.getString("title"));
				board.setContent(rs.getString("content"));
				board.setHit(rs.getInt("hit"));
				board.setReg_date(rs.getString("reg_date"));
				v.add(board);
			}
			
			return v;
		} catch(Exception sqle) {
			throw new RuntimeException(sqle.getMessage());			
		} finally {
			dataSourceTransactionManager.close(pstmt, rs);
		}
	}
	@Override
	public void deleteBoard(int board_seq) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			StringBuffer selectQuery = new StringBuffer();
			selectQuery.append("delete from newboard2");
			selectQuery.append(" where board_seq in (");
			selectQuery.append(" select board_seq from newboard2 start with board_seq = ? ");
			selectQuery.append("connect by prior board_seq = reply_seq)");
			con = dataSourceTransactionManager.getConnection();
			pstmt = con.prepareStatement(selectQuery.toString());
			pstmt.setInt(1, board_seq);
			int n=pstmt.executeUpdate();
			System.out.println("�궘�젣以꾩닔:"+n);
		} catch(Exception sqle) {
			throw new RuntimeException(sqle.getMessage());			
		} finally {
			dataSourceTransactionManager.close(pstmt);	
		}
	}
}
	


