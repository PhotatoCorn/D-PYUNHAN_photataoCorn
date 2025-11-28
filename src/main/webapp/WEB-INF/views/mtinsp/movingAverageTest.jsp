<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="../include/header.jsp" %>
<script type="text/javascript" src="/js/jquery-3.6.0.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<h2>📊 검침 데이터</h2>
<style>
    body { font-family: sans-serif; margin: 20px; }
    table { width: 80%; border-collapse: collapse; margin-top: 20px; }
    th, td { border: 1px solid #ddd; padding: 10px; text-align: center; }
    th { background-color: #f2f2f2; }
    h2 { color: #333; }
    .search-form {
        background-color: #f9f9f9;
        border: 1px solid #eee;
        padding: 15px;
        margin-bottom: 20px;
        border-radius: 5px;
    }
    .search-form input[type="text"],
    .search-form input[type="month"] {
        padding: 8px;
        border: 1px solid #ccc;
        border-radius: 4px;
    }
    .search-form button {
        padding: 8px 12px;
        background-color: #007bff;
        color: white;
        border: none;
        border-radius: 4px;
        cursor: pointer;
    }
    .search-form button:hover { background-color: #0056b3; }
    .search-form button.reset { background-color: #6c757d; }
    .search-form button.reset:hover { background-color: #5a6268; }

    /* 탭 스타일 */
    .tab-container {
        margin: 20px 0;
    }
    .tab-buttons {
        display: flex;
        border-bottom: 2px solid #ddd;
        gap: 5px;
    }
    .tab-btn {
        padding: 12px 24px;
        background-color: #f5f5f5;
        border: none;
        border-top: 2px solid transparent;
        border-left: 1px solid #ddd;
        border-right: 1px solid #ddd;
        cursor: pointer;
        font-size: 15px;
        font-weight: 500;
        transition: all 0.3s;
        position: relative;
        top: 2px;
    }
    .tab-btn:hover {
        background-color: #e9e9e9;
    }
    .tab-btn.active {
        background-color: white;
        border-bottom: 2px solid white;
        border-top: 2px solid #007bff;
        font-weight: bold;
    }
    .tab-btn.electric.active { border-top-color: #ff6384; }
    .tab-btn.water.active { border-top-color: #36a2eb; }
    .tab-btn.gas.active { border-top-color: #4bc0c0; }
    .tab-btn.all.active { border-top-color: #9966ff; }

    .tab-btn.electric { color: #ff6384; }
    .tab-btn.water { color: #36a2eb; }
    .tab-btn.gas { color: #4bc0c0; }
    .tab-btn.all { color: #9966ff; }
</style>
</head>
<body>

<div class="search-form">
    <form action="${pageContext.request.contextPath}/mtinsp/movingAverageTest" method="get">
        세대 ID:
        <input type="text"
               name="hshldId"
               value="${hshldId}"
               placeholder="예: 11011001" />

        &nbsp;&nbsp; 검색 월:
        <input type="month"
               name="searchMonth"
               value="${searchMonth}" />

        &nbsp;&nbsp;

        <button type="submit">조회</button>

        <button type="button" class="reset"
                onclick="location.href='${pageContext.request.contextPath}/mtinsp/movingAverageTest'">
            총 사용량 보기
        </button>
    </form>

    <!-- 탭 버튼 -->
    <div class="tab-container">
        <div class="tab-buttons">
            <button class="tab-btn all active" onclick="switchTab('all')">
                📊 전체 보기
            </button>
            <button class="tab-btn electric " onclick="switchTab('electric')">
                ⚡ 전기 사용량
            </button>
            <button class="tab-btn water" onclick="switchTab('water')">
                💧 수도 사용량
            </button>
            <button class="tab-btn gas" onclick="switchTab('gas')">
                🔥 가스 사용량
            </button>
        </div>
    </div>

    <div style="width: 100%;margin-top: 20px;">
        <canvas id="myChart"></canvas>
    </div>
</div>

<table>
    <thead>
    <tr>
        <th>검침 시간</th>
        <th>전기 사용량 (kWh)</th>
        <th>수도 사용량 (t)</th>
        <th>가스 사용량 (m³)</th>
    </tr>
    </thead>
    <tbody>
    <c:choose>
        <c:when test="${not empty dataList}">
            <c:forEach items="${dataList}" var="item">
                <tr>
                    <td>
                        <fmt:formatDate value="${item.mtinspDt}" pattern="MM-dd HH:mm" />
                    </td>
                    <td><fmt:formatNumber value="${item.electricUsage}" pattern="#,##0.0#" /></td>
                    <td><fmt:formatNumber value="${item.waterUsage}" pattern="#,##0.0#" /></td>
                    <td><fmt:formatNumber value="${item.gasUsage}" pattern="#,##0.0#" /></td>
                </tr>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <tr>
                <td colspan="4">조회된 데이터가 없습니다.</td>
            </tr>
        </c:otherwise>
    </c:choose>
    </tbody>
</table>





<!-- 이상징후 시작 -->
<h3>🚨 이상 사용 패턴</h3>
<table class="tight-table">
    <thead>
    <tr>
        <th>날짜</th>
        <th>항목</th>
        <th>사용량</th>
        <th>상태</th>
        <th>점수</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${anomalyList}" var="a">
        <c:if test="${a.anomaly}"> <!-- 이상치만 -->
            <tr>
                <td>${a.date}</td>
                <td>${a.iemNm}</td>
                <td><fmt:formatNumber value="${a.usgqty}" pattern="#,##0.0#"/></td>
                <td>${a.analysisStatus}</td>
                <td><fmt:formatNumber value="${a.anomalyScore}" pattern="#,##0.00"/></td>
            </tr>
        </c:if>
    </c:forEach>
    </tbody>
</table>
<!-- 이상징후 끝 -->









<script>
    // 컨트롤러에서 넘긴 JSON 데이터
    var chartLabels = ${chartLabels};
    var electricData = ${chartElectric};
    var waterData = ${chartWater};
    var gasData = ${chartGas};

    // 디버깅: 데이터 확인
    console.log('chartLabels:', chartLabels);
    console.log('electricData:', electricData);
    console.log('waterData:', waterData);
    console.log('gasData:', gasData);

    // Chart 객체를 전역 변수로 저장
    let myChart;

    // 전체 데이터셋 정의
    const allDatasets = {
        electric: {
            label: '전기 사용량 (kWh)',
            data: electricData || [],
            borderColor: 'rgba(255, 99, 132, 1)',
            backgroundColor: 'rgba(255, 99, 132, 0.2)',
            tension: 0.1,
            fill: true,
            borderWidth: 2,
            pointRadius: 3
        },
        water: {
            label: '수도 사용량 (t)',
            data: waterData || [],
            borderColor: 'rgba(54, 162, 235, 1)',
            backgroundColor: 'rgba(54, 162, 235, 0.2)',
            tension: 0.1,
            fill: true,
            borderWidth: 2,
            pointRadius: 3
        },
        gas: {
            label: '가스 사용량 (m³)',
            data: gasData || [],
            borderColor: 'rgba(75, 192, 192, 1)',
            backgroundColor: 'rgba(75, 192, 192, 0.2)',
            tension: 0.1,
            fill: true,
            borderWidth: 2,
            pointRadius: 3
        }
    };

    console.log('전기 데이터 개수:', electricData ? electricData.length : 0);
    console.log('수도 데이터 개수:', waterData ? waterData.length : 0);
    console.log('가스 데이터 개수:', gasData ? gasData.length : 0);

    // 초기 차트 생성 (모든 데이터셋을 미리 준비)
    function initChart() {
        const ctx = document.getElementById('myChart').getContext('2d');

        console.log('차트 초기화 시작');

        myChart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: chartLabels,
                datasets: [
                    { ...allDatasets.electric },
                    { ...allDatasets.water },
                    { ...allDatasets.gas }
                ]
            },
            options: {
                responsive: true,
                maintainAspectRatio: true,
                animation: false,
                scales: {
                    y: {
                        beginAtZero: true,
                        min: 0, // 최소값을 0으로 강제 설정
                        ticks: {
                            font: { size: 12 }
                        }
                    },
                    x: {
                        ticks: {
                            font: { size: 11 },
                            maxRotation: 45,
                            minRotation: 45
                        }
                    }
                },
                plugins: {
                    legend: {
                        display: false
                    }
                }
            }
        });

        // 초기에는 전기만 표시
        myChart.data.datasets[0].hidden = false;
        myChart.data.datasets[1].hidden = false;
        myChart.data.datasets[2].hidden = false;
        myChart.update('none');

        console.log('차트 생성 완료');
    }

    // 탭 전환 함수 (hidden 속성만 토글)
    function switchTab(tabName) {
        console.log('switchTab 호출:', tabName);
        console.log('차트 존재 여부:', myChart ? '있음' : '없음');

        // 모든 탭 버튼의 active 클래스 제거
        document.querySelectorAll('.tab-btn').forEach(btn => {
            btn.classList.remove('active');
        });

        // 클릭한 탭에 active 클래스 추가
        const clickedBtn = document.querySelector('.tab-btn.' + tabName);
        if (clickedBtn) {
            clickedBtn.classList.add('active');
        }

        // hidden 속성만 변경 (데이터셋은 그대로 유지)
        switch(tabName) {
            case 'electric':
                myChart.data.datasets[0].hidden = false; // 전기 보이기
                myChart.data.datasets[1].hidden = true;  // 수도 숨기기
                myChart.data.datasets[2].hidden = true;  // 가스 숨기기
                break;
            case 'water':
                myChart.data.datasets[0].hidden = true;  // 전기 숨기기
                myChart.data.datasets[1].hidden = false; // 수도 보이기
                myChart.data.datasets[2].hidden = true;  // 가스 숨기기
                break;
            case 'gas':
                myChart.data.datasets[0].hidden = true;  // 전기 숨기기
                myChart.data.datasets[1].hidden = true;  // 수도 숨기기
                myChart.data.datasets[2].hidden = false; // 가스 보이기
                break;
            case 'all':
                myChart.data.datasets[0].hidden = false; // 전기 보이기
                myChart.data.datasets[1].hidden = false; // 수도 보이기
                myChart.data.datasets[2].hidden = false; // 가스 보이기
                break;
        }
        // update 호출 (hidden 속성만 변경하므로 빠름)
        console.log('차트 업데이트 전 datasets:', myChart.data.datasets.map(d => ({label: d.label, hidden: d.hidden})));
        myChart.update('none');
        console.log('차트 업데이트 완료');
    }
    // 페이지 로드 시 차트 초기화
    window.addEventListener('load', function() {
        initChart();
    });
</script>
<%@ include file="../include/footer.jsp" %>