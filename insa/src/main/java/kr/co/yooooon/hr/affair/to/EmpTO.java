package kr.co.yooooon.hr.affair.to;


import java.util.ArrayList;

import kr.co.yooooon.base.to.BaseTO;

public class EmpTO extends BaseTO{
   private String empCode,empName,id,pw,birthdate,gender,mobileNumber,address,
   detailAddress,postNumber,email,lastSchool,imgExtend,position,deptName
   ,hobong,occupation,employment,authority;
   
   private ArrayList<WorkInfoTO> workInfoList;  // 일 대 다 관계에서 자식 테이블   //재직
   private ArrayList<CareerInfoTO> careerInfoList; //경력
   private ArrayList<EducationInfoTO> educationInfoList; //학력
   private ArrayList<FamilyInfoTO> familyInfoList;  // 가족
   private ArrayList<LicenseInfoTO> licenseInfoList; //자격증  

   

   public String getAuthority() {
	   return authority;
   }

   public void setAuthority(String authority) {
	this.authority = authority;
   }

	public String getDeptName() {
      return deptName;
   }

   public void setDeptName(String deptName) {
      this.deptName = deptName;
   }
 
   public String getEmpCode() {
      return empCode;
   }

   public void setEmpCode(String empCode) {
      this.empCode = empCode;
   }

   public String getEmpName() {
      return empName;
   }

   public void setEmpName(String empName) {
      this.empName = empName;
   }

   public String getBirthdate() {
      return birthdate;
   }

   public void setBirthdate(String birthdate) {
      this.birthdate = birthdate;
   }

   public String getGender() {
      return gender;
   }

   public void setGender(String gender) {
      this.gender = gender;
   }

   public String getMobileNumber() {
      return mobileNumber;
   }

   public void setMobileNumber(String mobileNumber) {
      this.mobileNumber = mobileNumber;
   }

   public String getAddress() {
      return address;
   }

   public void setAddress(String address) {
      this.address = address;
   }

   public String getDetailAddress() {
      return detailAddress;
   }

   public void setDetailAddress(String detailAddress) {
      this.detailAddress = detailAddress;
   }

   public String getPostNumber() {
      return postNumber;
   }

   public void setPostNumber(String postNumber) {
      this.postNumber = postNumber;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getLastSchool() {
      return lastSchool;
   }

   public void setLastSchool(String lastSchool) {
      this.lastSchool = lastSchool;
   }

   public String getImgExtend() {
      return imgExtend;
   }

   public void setImgExtend(String imgExtend) {
      this.imgExtend = imgExtend;
   }

   public String getPosition() {
      return position;
   }

   public void setPosition(String position) {
      this.position = position;
   }
   
   public String getHobong() {
      return hobong;
   }

   public void setHobong(String hobong) {
      this.hobong = hobong;
   }
   
   public String getOccupation() {
      return occupation;
   }

   public void setOccupation(String occupation) {
      this.occupation = occupation;
   }
   
   public String getEmployment() {
      return employment;
   }

   public void setEmployment(String employment) {
      this.employment = employment;
   }
   
   
   public ArrayList<WorkInfoTO> getWorkInfoList() {
      return workInfoList;
   }
   public void setWorkInfoList(ArrayList<WorkInfoTO> workInfoList) {
      this.workInfoList = workInfoList;
   }
   public ArrayList<CareerInfoTO> getCareerInfoList() {
      return careerInfoList;
   }
   public void setCareerInfoList(ArrayList<CareerInfoTO> careerInfoList) {
      this.careerInfoList = careerInfoList;
   }
   public ArrayList<EducationInfoTO> getEducationInfoList() {
      return educationInfoList;
   }
   public void setEducationInfoList(ArrayList<EducationInfoTO> educationInfoList) {
      this.educationInfoList = educationInfoList;
   }
   public ArrayList<LicenseInfoTO> getLicenseInfoList() {
      return licenseInfoList;
   }
   public void setLicenseInfoList(ArrayList<LicenseInfoTO> licenseInfoList) {
      this.licenseInfoList = licenseInfoList;
   }
   public ArrayList<FamilyInfoTO> getFamilyInfoList() {
      return familyInfoList;
   }
   public void setFamilyInfoList(ArrayList<FamilyInfoTO> familyInfoList) {
      this.familyInfoList = familyInfoList;
   }

public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
}

public String getPw() {
	return pw;
}

public void setPw(String pw) {
	this.pw = pw;
}

}