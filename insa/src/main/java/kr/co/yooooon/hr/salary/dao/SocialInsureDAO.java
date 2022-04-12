package kr.co.yooooon.hr.salary.dao;

import java.util.ArrayList;
import kr.co.yooooon.hr.salary.to.SocialInsureTO;

public interface SocialInsureDAO {
	public ArrayList<SocialInsureTO> selectBaseInsureList(String yearBox);
	public void updateInsureData(SocialInsureTO baseInsure);
	public void deleteInsureData(SocialInsureTO baseInsure);
}