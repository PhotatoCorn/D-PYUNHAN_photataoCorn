package kr.or.ddit.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.or.ddit.mapper.EmpMyPageMapper;
import kr.or.ddit.mapper.SignMapper;
import kr.or.ddit.service.EmpMyPageService;
import kr.or.ddit.util.UploadService;
import kr.or.ddit.vo.EmpVO;
import lombok.extern.slf4j.Slf4j;
@Transactional
@Slf4j
@Service
public class EmpMyPageServiceImpl implements EmpMyPageService {
	@Autowired
	EmpMyPageMapper empMyPageMapper;
	@Autowired
	SignMapper signMapper; 
	

	//emp+file+filedetail+auth
	@Override
	public EmpVO selectEmpDetailById(String empId) {
		return this.empMyPageMapper.selectEmpDetailById(empId);
	}
	
	
	@Autowired
	UploadService uploadService;
       @Transactional
	   @Override
	   public int empEdit(EmpVO empVO ) {
	      
	      MultipartFile[] uploadFiles = empVO.getUploadFiles();
	        if (uploadFiles != null && uploadFiles[0].getOriginalFilename().length() > 0) {
	            long fileGroupSn = uploadService.multiImageUpload(uploadFiles);
	            empVO.setFileGroupSn(fileGroupSn);
	        }
	        int result = this.empMyPageMapper.empEdit(empVO);
	        log.info("empEdit -> result :{}" , result);
	        
	      return result;
	   }
	   

}
