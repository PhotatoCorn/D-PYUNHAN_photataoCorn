package kr.or.ddit.util.movingAvgStd;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class MovingAvgStdPoint {

	@JsonFormat(pattern = "yyyy/MM/dd")
		//localDate는 날짜만 저장, 시간없음 vs date는 시간까지 저장.
	private LocalDate date;
    private String hshldId;
    private String iemNm;
	private double usgqty;
	
	private boolean isAnomaly=false;
	private double anomalyScore=0.0;
	private String analysisStatus = "PENDING"; // 분석 상태

}
