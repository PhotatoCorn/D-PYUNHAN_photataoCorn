package kr.or.ddit.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.or.ddit.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.ddit.mapper.ElctrncsanctnMapper;
import kr.or.ddit.service.ElctrncsanctnService;
import kr.or.ddit.util.UploadService;
import kr.or.ddit.vo.BkmkSanctnlnDataVO;
import kr.or.ddit.vo.BkmkSanctnlnDetailVO;
import kr.or.ddit.vo.BkmkSanctnlnVO;
import kr.or.ddit.vo.DeptVO;
import kr.or.ddit.vo.DrftDocVO;
import kr.or.ddit.vo.DrftRefrnVO;
import kr.or.ddit.vo.ElctrnsanctnVO;
import kr.or.ddit.vo.EmpVO;
import kr.or.ddit.vo.FileDetailVO;
import kr.or.ddit.vo.SanctnlnVO;
import kr.or.ddit.vo.SignVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ElctrncsanctnServiceImpl implements ElctrncsanctnService {

	@Autowired
	UploadService uploadService;

	@Autowired
	ElctrncsanctnMapper elctrncsanctnMapper;

	@Autowired
	PdfStampingServiceImpl pdfStampingServiceImpl;

	@Autowired
	FileMapper fileMapper;

	@Override
	public List<DrftDocVO> getDrftDocList() {
		return this.elctrncsanctnMapper.getDrftDocList();
	}

	@Override
	public DrftDocVO getDrftDoc(DrftDocVO drftDocVO) {
		return this.elctrncsanctnMapper.getDrftDoc(drftDocVO);
	}

	@Override
	public List<DeptVO> getDeptList(EmpVO empVO) {
		return this.elctrncsanctnMapper.getDeptList(empVO);
	}

	@Transactional
	@Override
	public int postElctrnsanctn(ElctrnsanctnVO elctrnsanctnVO, MultipartFile[] inputFiles) {
		// 1. VO에 담겨 온 파일 그룹 SN (PDF 생성 시 넘어온 ID, 또는 0)
		long finalFileGroupSn = elctrnsanctnVO.getFileGroupSn();

		// --- A. 파일 그룹 SN이 0일 경우에만 파일 처리/그룹 생성 ---
		if (finalFileGroupSn == 0) {

			if (inputFiles != null && inputFiles.length > 0) {
				// Case 1: 새로운 파일(PDF 없음)이 업로드된 경우. 새 그룹 생성 및 파일 저장.
				finalFileGroupSn = this.uploadService.multiImageUpload(inputFiles);
			}
			// Case 2: 새 파일도 없는 경우 (ID는 0으로 유지됨).
		}
		// 🚨 파일 그룹 SN이 0보다 큰 경우(PDF 존재), 이 블록을 건너뛰고 finalFileGroupSn 값을 유지합니다. 🚨


		// --- B. 재상신/복사 로직 처리 (파일 활동이 전혀 없었을 때만) ---
		// finalFileGroupSn이 0인 상태에서만 상위 문서를 참조합니다.
		if (finalFileGroupSn == 0 && elctrnsanctnVO.getUpperElctrnsanctnId() != 0) {

			// Case 3: PDF도 없고, 새 파일도 없지만, 기존 문서 복사본으로 제출하는 경우.
			ElctrnsanctnVO upperElctrnsanctnVO = new ElctrnsanctnVO();
			upperElctrnsanctnVO.setElctrnsanctnSn(elctrnsanctnVO.getUpperElctrnsanctnId());

			// 상위 문서의 FileGroupSn을 재사용합니다.
			finalFileGroupSn = this.elctrncsanctnMapper.getElctrnsanctn(upperElctrnsanctnVO).getFileGroupSn();
		}

		// --- C. 최종 검증 및 DB 커밋 ---

		// 1. VO에 최종 그룹 ID 설정
		elctrnsanctnVO.setFileGroupSn(finalFileGroupSn);

		// 2. 🚨 VO에 Committed File List는 로컬 변수로만 처리 (필드 추가 없음) 🚨
		if (finalFileGroupSn > 0) {
			// 이 로직은 파일 목록이 존재함을 확인하고 검증하는 용도로만 사용됩니다.
			List<FileDetailVO> committedFiles = this.uploadService.getFileDetailVOList(finalFileGroupSn);
			// (VO에 list를 set하는 코드는 삭제됨)
		}

		// 3. 전자결재 문서 최종 DB INSERT/UPDATE
		return this.elctrncsanctnMapper.postElctrnsanctn(elctrnsanctnVO);
	}

	@Transactional
	@Override
	public int postBkmkSanctnln(BkmkSanctnlnDataVO bkmkSanctnlnDataVO, CustomUser customUser) {
		String bkmkSanctnlnNm = bkmkSanctnlnDataVO.getBkmkSanctnlnNm();

		BkmkSanctnlnVO bkmkSanctnlnVO = new BkmkSanctnlnVO();
		bkmkSanctnlnVO.setBkmkSanctnlnNm(bkmkSanctnlnNm);
		bkmkSanctnlnVO.setEmpId(customUser.getEmpVO().getEmpId());

		int result = this.elctrncsanctnMapper.postBkmkSanctnln(bkmkSanctnlnVO);

		for (BkmkSanctnlnDetailVO bkmkSanctnlnDetailVO : bkmkSanctnlnDataVO.getSanctnlnList()) {
			bkmkSanctnlnDetailVO.setBkmkSanctnlnId(bkmkSanctnlnVO.getBkmkSanctnlnId());
			this.elctrncsanctnMapper.postBkmkSanctnlnDetail(bkmkSanctnlnDetailVO);
		}

		for (BkmkSanctnlnDetailVO bkmkSanctnlnDetailVO : bkmkSanctnlnDataVO.getDrftRefrnList()) {
			bkmkSanctnlnDetailVO.setBkmkSanctnlnId(bkmkSanctnlnVO.getBkmkSanctnlnId());
			bkmkSanctnlnDetailVO.setDrftRefrnAt(1);
			this.elctrncsanctnMapper.postBkmkSanctnlnDetail(bkmkSanctnlnDetailVO);
		}

		return result;
	}

	@Override
	public List<BkmkSanctnlnVO> getBkmkSanctnlnList(EmpVO empVO) {
		return this.elctrncsanctnMapper.getBkmkSanctnlnList(empVO);
	}

	@Override
	public BkmkSanctnlnVO getBkmkSanctnln(BkmkSanctnlnVO bkmkSanctnlnVO) {
		return this.elctrncsanctnMapper.getBkmkSanctnln(bkmkSanctnlnVO);
	}

	@Transactional
	@Override
	public int postSanctnlnDrftRefrn(String sanctnlnEmpList, String drftRefrnEmpList, ElctrnsanctnVO elctrnsanctnVO) {
		int result = 0;
		ObjectMapper mapper = new ObjectMapper();
		try {
			if (sanctnlnEmpList != null && !sanctnlnEmpList.isEmpty() && !sanctnlnEmpList.equals("[]")) {
				List<SanctnlnVO> sanctnlnVOList = mapper.readValue(sanctnlnEmpList,
						new TypeReference<List<SanctnlnVO>>() {
						});
				for (int i = 0; i < sanctnlnVOList.size(); i++) {
					SanctnlnVO sanctnlnVO = sanctnlnVOList.get(i);
					sanctnlnVO.setElctrnsanctnSn(elctrnsanctnVO.getElctrnsanctnSn());
					if (sanctnlnVO.getSanctnOrdr() == sanctnlnVOList.size()) {
						sanctnlnVO.setDcrbmanAt(1);
					} else {
						sanctnlnVO.setDcrbmanAt(0);
					}
					this.elctrncsanctnMapper.postSanctnln(sanctnlnVO);
				}
			}
			if (drftRefrnEmpList != null && !drftRefrnEmpList.isEmpty() && !drftRefrnEmpList.equals("[]")) {
				List<DrftRefrnVO> drftRefrnVOList = mapper.readValue(drftRefrnEmpList,
						new TypeReference<List<DrftRefrnVO>>() {
						});
				for (DrftRefrnVO drftRefrnVO : drftRefrnVOList) {
					drftRefrnVO.setElctrnsanctnSn(elctrnsanctnVO.getElctrnsanctnSn());
					this.elctrncsanctnMapper.postDrftRefrn(drftRefrnVO);
				}
			}
			result = 1;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return result;
	}

	// 페이지네이션
	@Override
	public List<ElctrnsanctnVO> getElctrnsanctnList(Map<String, Object> map) {
		return this.elctrncsanctnMapper.getElctrnsanctnList(map);
	}

	@Override
	public int getTotal(Map<String, Object> map) {
		return this.elctrncsanctnMapper.getTotal(map);
	}

	// 상세페이지
	@Transactional
	@Override
	public ElctrnsanctnVO getElctrnsanctn(ElctrnsanctnVO elctrnsanctnVO) {
		elctrnsanctnVO = this.elctrncsanctnMapper.getElctrnsanctn(elctrnsanctnVO);

		DrftDocVO drftDocVO = new DrftDocVO();
		drftDocVO.setDrftDocId(elctrnsanctnVO.getDrftDocId());
		drftDocVO = this.elctrncsanctnMapper.getDrftDoc(drftDocVO);
		elctrnsanctnVO.setDrftDocVO(drftDocVO);

		List<SanctnlnVO> sanctnlnVOList = elctrnsanctnVO.getSanctnlnVOList();
		for (SanctnlnVO sanctnlnVO : sanctnlnVOList) {
			DeptVO deptVO = this.elctrncsanctnMapper.getDept(sanctnlnVO.getEmpVO());
			sanctnlnVO.getEmpVO().setDeptVO(deptVO);
			SignVO signVO = this.elctrncsanctnMapper.getSign(sanctnlnVO.getEmpVO());
			if (signVO != null) {
				List<FileDetailVO> fileDetailVOList = this.uploadService
						.getFileDetailVOList(signVO.getFileGroupSn());
				signVO.setFileDetailVO(fileDetailVOList.getFirst());
				sanctnlnVO.getEmpVO().setSignVO(signVO);
			}
		}

		elctrnsanctnVO.setSanctnlnVOList(sanctnlnVOList);

		List<DrftRefrnVO> drftRefrnVOList = elctrnsanctnVO.getDrftRefrnVOList();
		for (DrftRefrnVO drftRefrnVO : drftRefrnVOList) {
			DeptVO deptVO = this.elctrncsanctnMapper.getDept(drftRefrnVO.getEmpVO());
			drftRefrnVO.getEmpVO().setDeptVO(deptVO);
		}

		elctrnsanctnVO.setDrftRefrnVOList(drftRefrnVOList);

		EmpVO empVO = this.elctrncsanctnMapper.getEmp(elctrnsanctnVO.getEmpId());
		elctrnsanctnVO.setEmpVO(empVO);

		return elctrnsanctnVO;
	}

	@Override
	public List<ElctrnsanctnVO> getElctrnsanctnRcptList(Map<String, Object> mapRcpt) {
		List<ElctrnsanctnVO> elctrnsanctnVOList = this.elctrncsanctnMapper.getElctrnsanctnRcptList(mapRcpt);
		for (ElctrnsanctnVO elctrnsanctnVO : elctrnsanctnVOList) {
			EmpVO empVO = this.elctrncsanctnMapper.getEmp(elctrnsanctnVO.getEmpId());
			elctrnsanctnVO.setEmpVO(empVO);
		}

		return elctrnsanctnVOList;
	}

	@Override
	public int getTotalRcpt(Map<String, Object> mapRcpt) {

		return this.elctrncsanctnMapper.getTotalRcpt(mapRcpt);
	}

	@Override
	public int rejectElctrnsanctn(SanctnlnVO sanctnlnVO, EmpVO empVO) {
		Map<String, Object> map = new HashMap<>();
		map.put("empId", empVO.getEmpId());
		map.put("elctrnsanctnSn", sanctnlnVO.getElctrnsanctnSn());
		map.put("returnPrvonsh", sanctnlnVO.getReturnPrvonsh());

		int result = this.elctrncsanctnMapper.rejectElctrnsanctn(map);
		return result;
	}

	@Override
	public int consentElctrnsanctn(SanctnlnVO sanctnlnVO, EmpVO empVO) throws IOException {
		Map<String, Object> map = new HashMap<>();
		map.put("empId", empVO.getEmpId());
		map.put("elctrnsanctnSn", sanctnlnVO.getElctrnsanctnSn());

		int stamping=sanctnlnVO.getDcrbmanAt();
		System.out.println("나나나나"+stamping);
		if(stamping==1){
			ElctrnsanctnVO elctrnsanctnVO = new ElctrnsanctnVO();
			elctrnsanctnVO.setElctrnsanctnSn(sanctnlnVO.getElctrnsanctnSn());
			elctrnsanctnVO=this.elctrncsanctnMapper.getElctrnsanctn(elctrnsanctnVO);

			Map<String, Object> filemap = new HashMap<>();
			filemap.put("fileGroupSn",elctrnsanctnVO.getFileGroupSn() );
			filemap.put("fileNo", 1);
			FileDetailVO originalFileVO = fileMapper.getFileDetail(filemap);

			pdfStampingServiceImpl.createStampedPdfCopy(originalFileVO);
			
			System.out.println("나나나나");
		}

		int result = this.elctrncsanctnMapper.consentElctrnsanctn(map);
		return result;
	}
	
//	여기서부터 수정
	
	//KBH
	@Override
	public List<ElctrnsanctnVO> getElctrnsanctnVOListSm(Map<String, Object> map) {
		return this.elctrncsanctnMapper.getElctrnsanctnVOListSm(map);
	}	
}
