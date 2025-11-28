<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <!-- 일반 반응형 메타는 유지하되, 프레임 폭은 CSS로 고정 -->
  <meta content="width=device-width, initial-scale=1.0" name="viewport">
  <title>Index - Dewi Bootstrap Template</title>
  <meta name="description" content="">
  <meta name="keywords" content="">

  <!-- Favicons -->
  <link href="/dewi/assets/img/favicon.png" rel="icon">
  <link href="/dewi/assets/img/apple-touch-icon.png" rel="apple-touch-icon">

  <!-- Fonts -->
  <link href="https://fonts.googleapis.com" rel="preconnect">
  <link href="https://fonts.gstatic.com" rel="preconnect" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@100..900&family=Raleway:wght@100..900&family=Inter:wght@100..900&display=swap" rel="stylesheet">

  <!-- Vendor CSS Files (Bootstrap 5 포함) -->
  <link href="/dewi/assets/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
  <link href="/dewi/assets/vendor/bootstrap-icons/bootstrap-icons.css" rel="stylesheet">
  <link href="/dewi/assets/vendor/aos/aos.css" rel="stylesheet">
  <link href="/dewi/assets/vendor/glightbox/css/glightbox.min.css" rel="stylesheet">
  <link href="/dewi/assets/vendor/swiper/swiper-bundle.min.css" rel="stylesheet">

  <!-- Main CSS File -->
  <link href="/dewi/assets/css/main.css" rel="stylesheet">

  <!-- (옵션) Summernote 등 AdminLTE 플러그인 CSS 필요 시 -->
  <link rel="stylesheet" href="/adminlte/plugins/summernote/summernote-bs4.min.css">

  <!-- ====== 폰화면 프레임 (얇게) ====== -->
  <style>
    :root{
      /* 폭 프리셋 변경 (예: 360, 390, 414 등) */
      --frame-width: 828px;
      --frame-radius: 18px;
    }
    /* 바깥 배경 & 가운데 정렬 */
    body.index-page{
      background:#fff;
      min-height:100dvh;
      display:flex;
      justify-content:center;
    }
    /* 폰 화면 비율 프레임 */
    .mobile-frame{
      padding-top: 64px;	/*헤더 높이만큼 패딩줘서 가려지지 않게*/
      width:var(--frame-width);
      min-height:100dvh; /* 푸터에서 닫힘 */
      background:#f0e9df;
      box-shadow:0 10px 30px rgba(0,0,0,.12);
      border-radius:var(--frame-radius);
      overflow-x:hidden;
      position:relative;
      
      height: 100dvh;          /* 프레임 높이를 화면 높이로 고정 */
	  overflow-y: auto;        /* 프레임 안에서 스크롤 되게 */
	  -webkit-overflow-scrolling: touch; /* iOS 부드러운 스크롤 */
    }
    
	/* safe-area 패딩이 64px을 덮어쓰지 않게 합산 */
	@supports(padding:max(2px)){
	  .mobile-frame{
	    padding-top: calc(64px + env(safe-area-inset-top));
	    padding-bottom: max(0px, env(safe-area-inset-bottom));
	  }
	}
    /* 이미지/표 넘침 방지 */
    img, video{ max-width:100%; height:auto; }
    .table-responsive{ overflow-x:auto; }

    /* notch 안전영역 */
    @supports(padding:max(2px)){
      .mobile-frame{
        padding-top:max(0px, env(safe-area-inset-top));
        padding-bottom:max(0px, env(safe-area-inset-bottom));
      }
    }

    /* Dewi 헤더가 프레임 안에서 가득 */  
     .mobile-frame .header{ 
/*         상단의 헤더 색  */
       background-color: #a47764; 
       top: 0; 
       z-index: 1030; 
       transition: background-color .25s, box-shadow .25s, backdrop-filter .25s, opacity .25s;
       box-shadow: 0 2px 8px rgba(0,0,0,.06);
       position: sticky; /*프레임 안에서 헤더가 고정 */
       border-top-left-radius:var(--frame-radius); 
       border-top-right-radius:var(--frame-radius); 
       overflow:hidden;  
     } 
     .mobile-frame .header.scrolled{
	  background: rgba(164,119,100,0.55);    /* 투명도 조절: 0.0 ~ 1.0 */
	  backdrop-filter: saturate(140%) blur(8px);
	  box-shadow: 0 4px 16px rgba(0,0,0,.08);
	}
	body.mobile-nav-active .mobile-frame .header,
	.mobile-frame .header.nav-open {
	  background-color:#a47764 !important;
	  backdrop-filter:none !important;
	}
    .mobile-frame .footer{
    background-color: rgba(164,119,100,0.75);
	}
    /* 컨텐츠 영역 높이를 적절히 확보하려면 필요에 따라 사용
       .content-wrapper{ min-height: calc(100dvh - 헤더높이 - 푸터높이); } */
  </style>
</head>

<!-- body는 푸터에서 닫습니다 -->
<body class="index-page">
  <!-- 모바일 프레임 시작: 이 DIV도 푸터에서 닫습니다 -->
  <div class="mobile-frame">

    <!-- =============== Dewi Header (원본 유지) =============== -->
	<!-- fixed는 상단화면에 꽉차게 고정  -->
<!--     <header id="header" class="header d-flex align-items-center fixed-top"> -->
     <header id="header" class="header d-flex align-items-center sticky-top">
      <div class="container-fluid container-xl position-relative d-flex align-items-center">

        <a href="index.html" class="logo d-flex align-items-center me-auto">
          <!-- <img src="/dewi/assets/img/logo.png" alt=""> -->
          <h1 class="sitename">DPYUNHAN</h1>
        </a>

        <nav id="navmenu" class="navmenu">
          <ul>
            <li><a href="#hero" class="active">Home</a></li>
            
            <li class="dropdown">
              <a href="#"><span>Dropdown</span> <i class="bi bi-chevron-down toggle-dropdown"></i></a>
              <ul>
                <li><a href="#">Dropdown 1</a></li>
                <li class="dropdown"><a href="#"><span>Deep Dropdown</span> <i class="bi bi-chevron-down toggle-dropdown"></i></a>
                  <ul>
                    <li><a href="#">Deep Dropdown 1</a></li>
                    <li><a href="#">Deep Dropdown 2</a></li>
                    <li><a href="#">Deep Dropdown 3</a></li>
                    <li><a href="#">Deep Dropdown 4</a></li>
                    <li><a href="#">Deep Dropdown 5</a></li>
                  </ul>
                </li>
                <li><a href="#">Dropdown 2</a></li>
                <li><a href="#">Dropdown 3</a></li>
                <li><a href="#">Dropdown 4</a></li>
              </ul>
            </li>
          </ul>
          <i class="mobile-nav-toggle d-xl-none bi bi-list"></i>
        </nav>
      </div>
    </header>
    <!-- =============== /Dewi Header =============== -->

    <!-- 여기서부터 각 페이지의 본문이 들어갑니다.
         푸터에서 </div> </body> </html> 을 닫아주세요. -->
