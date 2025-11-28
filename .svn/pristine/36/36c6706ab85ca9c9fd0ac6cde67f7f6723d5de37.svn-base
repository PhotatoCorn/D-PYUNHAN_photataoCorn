<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@ include file="../include/headerContents.jsp"%>

<link rel="stylesheet" href="https://leeoniya.github.io/uPlot/dist/uPlot.min.css">
<script src="https://leeoniya.github.io/uPlot/dist/uPlot.iife.js"></script>

<section class="content">
  <div class="container-fluid">

    <div class="card card-info">
      <div class="card-header">
        <h3 class="card-title">30일 예상 사용량 예측</h3>
        <div class="card-tools">
          <button type="button" class="btn btn-tool" data-card-widget="collapse">
            <i class="fas fa-minus"></i>
          </button>
        </div>
      </div>

      <div class="card-body">
        <div id="result"
             style="font-size:18px;font-weight:bold;margin-bottom:15px;padding:15px;background:#f0f0f0;border-radius:5px;">
        </div>

        <div class="row mb-3">
          <div class="col-4">
            <label style="font-weight:bold;">조회할 년/월 선택</label>
            <input type="month" id="yearMonth" class="form-control" value="${yearMonth}">
          </div>
        </div>

        <button id="predict" class="btn btn-primary mb-3">
          <i class="fas fa-chart-line"></i> 예측하기
        </button>

        <div id="lineChart" style="width:100%;height:400px;"></div>
      </div>

    </div>
  </div>
</section>

<script>
document.addEventListener("DOMContentLoaded", function () {

  let chart = null;

  function fetchData(yearMonth) {
    return fetch("/levy/predict", {
      method: "post",
      headers: { "Content-Type": "application/json;charset=UTF-8" },
      body: JSON.stringify({ yearMonth })
    }).then(r => r.json());
  }

  document.querySelector("#predict").addEventListener("click", () => {
    const ym = document.querySelector("#yearMonth").value.replace("-", "");
    if (!ym) return alert("년월을 선택하세요.");
    runPredict(ym);
  });

  function runPredict(yearMonth) {

    fetchData(yearMonth).then(data => {

      const days = data.days;
      const actualValues = data.values;
      const predictedValues = data.predicted;
      const totalMonth = data.amt;

      // ------ 오차율 계산 제거됨 ------

      var futurePredict = new Array(days.length).fill(null);

      for (var i = 0; i < days.length; i++) {
        if (actualValues[i] == null) {
          futurePredict[i] = predictedValues[i];
        }
      }

      document.querySelector("#result").innerHTML =
        "선택한 월: " + yearMonth.substr(0,4) + "년 " + yearMonth.substr(4,5) + "월<br>" +
        "<span style='color:blue;'>" + yearMonth.substr(4,5) + "월 예상 관리비: " + totalMonth + "원</span><br>";

      const opts = {
        title: yearMonth.substr(4,5) + "월 예상 관리비 추이",
        width: document.querySelector("#lineChart").offsetWidth,
        height: 400,
        scales: {
          x: { time: false }
        },
        plugins: [ tooltipPlugin() ],
        series: [
          { label:"Day" },
          { label:"Actual", stroke:"rgb(75,192,192)", width:2, points:{show:true,size:6}},
          { label:"Predict(전체직선)", stroke:"rgba(255,99,132,0.3)", width:1, dash:[5,5] },
          { label:"Future Predict", stroke:"red", width:2, points:{show:true,size:10,fill:"yellow"} }
        ]
      };

      const plotData = [
        days,
        actualValues,
        predictedValues,
        futurePredict
      ];

      if (chart) chart.destroy();
      chart = new uPlot(opts, plotData, document.querySelector("#lineChart"));
    });
  }

  function tooltipPlugin() {
    var el;
    return {
      hooks: {
        init: function(u) {
          el = document.createElement("div");
          el.style.position = "absolute";
          el.style.background = "rgba(0,0,0,0.8)";
          el.style.color = "white";
          el.style.padding = "6px 10px";
          el.style.borderRadius = "4px";
          el.style.fontSize = "13px";
          el.style.pointerEvents = "none";
          el.style.display = "none";
          document.querySelector("#lineChart").appendChild(el);
        },
        setCursor: function(u) {
          var idx = u.cursor.idx;
          var left = u.cursor.left;
          var top = u.cursor.top;

          if (idx == null) {
            el.style.display = "none";
            return;
          }

          var actual = u.data[1][idx];
          var predict = u.data[2][idx];

          el.innerHTML =
            "<b>" + u.data[0][idx] + "일</b><br>" +
            "실제: " + (actual != null ? actual.toFixed(2) : '-') + "<br>" +
            "예측: " + (predict != null ? predict.toFixed(2) : '-');

          el.style.left = (left + 30) + "px";
          el.style.top = (top + 40) + "px";
          el.style.display = "block";
        }
      }
    };
  }

});
</script>

<%@ include file="../include/footerContents.jsp"%>
