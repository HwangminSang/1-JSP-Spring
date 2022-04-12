package kr.co.yooooon.hr.salary.dao;

import java.util.ArrayList;

import kr.co.yooooon.hr.salary.to.BasePymntItmNameCodeTO;
import kr.co.yooooon.hr.salary.to.AmnclFrmlRsTO;
import kr.co.yooooon.hr.salary.to.AmnclFrmlTO;
import kr.co.yooooon.hr.salary.to.DissectionRsTO;
import kr.co.yooooon.hr.salary.to.DissectionTO;
import kr.co.yooooon.hr.salary.to.JobCodeRsTO;
import kr.co.yooooon.hr.salary.to.JobCodeTO;
import kr.co.yooooon.hr.salary.to.PaymentItmOptionRsTO;
import kr.co.yooooon.hr.salary.to.PaymentItmOptionTO;
import kr.co.yooooon.hr.salary.to.PymntDditmTO;

public interface PymntDditmDAO {
	public ArrayList<BasePymntItmNameCodeTO> findPymntDditmList(PymntDditmTO pymntData);
	public ArrayList<PaymentItmOptionRsTO> setPaymentItmNodeOption(PaymentItmOptionTO paymntOtionTO);
	public ArrayList<JobCodeRsTO> setJobCode(JobCodeTO priorityCode);
	public ArrayList<DissectionRsTO> setDissection(DissectionTO dissectionData);
	public ArrayList<AmnclFrmlRsTO> setAmnclFrml(AmnclFrmlTO AFTTO);

}