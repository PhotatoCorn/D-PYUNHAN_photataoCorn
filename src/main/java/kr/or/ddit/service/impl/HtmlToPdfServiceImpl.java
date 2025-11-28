package kr.or.ddit.service.impl;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import kr.or.ddit.util.UploadService;
import kr.or.ddit.vo.FileDetailVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.util.UUID; // 임시 파일명 생성용

@Service
@RequiredArgsConstructor
@Slf4j
public class HtmlToPdfServiceImpl {


    private final ResourceLoader resourceLoader;

    private final UploadService uploadController;

    public FileDetailVO generateAndSavePdf(
            String htmlContent,
            Long initialFileGroupSn,
            String originalFilename
    ) throws IOException, DocumentException {

        // 1. PDF 생성 (byte[] 데이터)
        byte[] pdfBytes = createPdfFromHtmlSanctr(htmlContent);

        // 2. 임시 파일 생성 및 byte[] 쓰기
        File tempFile = File.createTempFile("pdf_contract_temp_", ".pdf");

        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(pdfBytes);
            log.info("임시 파일 생성 성공: {}", tempFile.getAbsolutePath());
        } catch (IOException e) {
            log.error("임시 파일 쓰기 실패", e);
            if (tempFile.exists()) {
                tempFile.delete();
            }
            throw e;
        }

        // 3. 영구 저장소로 이동 및 DB 등록 (FileService 호출)
        // 🚨 이 fileService.commitTempFile()은 FileDetailVO를 반환해야 합니다. 🚨
        FileDetailVO savedFileVO = uploadController.commitTempFile(initialFileGroupSn, tempFile, originalFilename);

        // 4. 결과 DTO 반환
        return savedFileVO;
    }

    /**
     * CKEditor에서 받은 HTML 내용을 PDF 파일로 생성합니다.
     * @param htmlContent CKEditor의 HTML 문자열
     * @return 1단계로 생성된 PDF의 '파일 시스템 경로'
     * @throws IOException
     * @throws DocumentException
     */
    public String createPdfFromHtml(String htmlContent) throws IOException, DocumentException {
        // ... (1. 폰트, 2. CSS 로드 부분은 동일) ...
        Resource fontResource = resourceLoader.getResource("classpath:fonts/NanumGothic.ttf");
        if (!fontResource.exists()) {
            throw new IOException("한글 폰트 파일을 찾을 수 없습니다.");
        }
        String fontPath = fontResource.getFile().getAbsolutePath();

        Resource cssResource = resourceLoader.getResource("classpath:static/css/contract.css");
        String cssContent = "";
        if (cssResource.exists()) {
            try (InputStream cssStream = cssResource.getInputStream()) {
                cssContent = new String(cssStream.readAllBytes());
            }
        }

        // 3. (★핵심★) HTML '강력 소독'
        String bodyContentOnly = htmlContent;

        // (?s)는 '줄 바꿈' 무시, (?i)는 '대소문자' 무시

        // <head>...</head> 블록이 있다면 제거
        bodyContentOnly = bodyContentOnly.replaceAll("(?s)(?i)<head>.*?</head>", "");

        // <style>...</style> 블록이 있다면 제거 (우리가 Java에서 새로 주입할 것임)
        bodyContentOnly = bodyContentOnly.replaceAll("(?s)(?i)<style.*?</style>", "");

        // <title>...</title>이 있다면 제거
        bodyContentOnly = bodyContentOnly.replaceAll("(?s)(?i)<title>.*?</title>", "");

        // (이번 오류의 주범) <meta ...> 태그 제거
        bodyContentOnly = bodyContentOnly.replaceAll("(?i)<meta[^>]*>", "");

        // (미래의 오류 방지) 껍데기 태그들 제거
        bodyContentOnly = bodyContentOnly.replaceAll("(?i)<!DOCTYPE[^>]*>", "");
        bodyContentOnly = bodyContentOnly.replaceAll("(?i)<html[^>]*>", "");
        bodyContentOnly = bodyContentOnly.replaceAll("(?i)</html>", "");
        bodyContentOnly = bodyContentOnly.replaceAll("(?i)<body[^>]*>", "");
        bodyContentOnly = bodyContentOnly.replaceAll("(?i)</body>", "");

        // ✅ 추가: font-family 속성 제거
        bodyContentOnly = bodyContentOnly.replaceAll("(?i)font-family:\\s*[^;\"'>]+;?", "");

// ✅ 추가: style 속성이 비어있으면 아예 제거
        bodyContentOnly = bodyContentOnly.replaceAll("(?i)\\s+style=\"\\s*\"", "");

// ✅ 추가: <font> 태그 제거 (태그는 제거하되 내용은 유지)
        bodyContentOnly = bodyContentOnly.replaceAll("(?i)<font[^>]*>", "");
        bodyContentOnly = bodyContentOnly.replaceAll("(?i)</font>", "");

        // 4. '소독된 알맹이'로 XHTML 문서를 조립 (이하 동일)
        StringBuilder xhtmlBuilder = new StringBuilder();
        xhtmlBuilder.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
        xhtmlBuilder.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        xhtmlBuilder.append("<head>");
        xhtmlBuilder.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
        xhtmlBuilder.append("<style type=\"text/css\">");
        xhtmlBuilder.append("body { font-family: 'Nanum Gothic'; }");
        xhtmlBuilder.append(cssContent); // CSS 주입
        xhtmlBuilder.append("</style>");
        xhtmlBuilder.append("</head>");
        xhtmlBuilder.append("<body>");

        // 여기에 '소독된' 알맹이만 삽입
        xhtmlBuilder.append(bodyContentOnly);

        xhtmlBuilder.append("</body>");
        xhtmlBuilder.append("</html>");

        String finalHtml = xhtmlBuilder.toString();

        // 5. 렌더러 생성 및 폰트 등록 (동일)
        // 5. 렌더러 생성 및 폰트 등록 (수정!)
        ITextRenderer renderer = new ITextRenderer();

        // ⛔️ 기존 코드 (CP1252 인코딩으로 강제되어 한글 깨짐)
        // renderer.getFontResolver().addFont(fontPath, true);

        // ✅ 수정 코드 (IDENTITY_H 인코딩으로 한글 완벽 지원)
        renderer.getFontResolver().addFont(
                fontPath,                 // 1. 폰트 경로 (String)
                "Nanum Gothic",
                BaseFont.IDENTITY_H,      // 2. 인코딩 (String) - 한글/유니코드 필수!
                BaseFont.EMBEDDED,        // 3. 폰트 임베드 여부 (boolean)
                null
        );
        renderer.setDocumentFromString(finalHtml);
        renderer.layout();

        // 6. 임시 파일 저장 (동일)
        Resource staticResource = resourceLoader.getResource("classpath:static/");
        File outputDir = new File(staticResource.getFile().getAbsolutePath() + File.separator + "output");
        if (!outputDir.exists()) { outputDir.mkdirs(); }
        String tempPdfFilename = "temp_" + UUID.randomUUID().toString() + ".pdf";
        String savePath = outputDir.getAbsolutePath() + File.separator + tempPdfFilename;

        // 7. PDF 파일로 '인쇄' (동일)
        try (OutputStream os = new FileOutputStream(savePath)) {
            renderer.createPDF(os);
        }

        // 8. 파일 경로 반환 (동일)
//        return savePath;

        // "static/output" 폴더에 저장했으므로,
        // 웹 브라우저에서는 "/output/" URL로 접근할 수 있습니다.
        return "/output/" + tempPdfFilename;
    }

    public byte[] createPdfFromHtmlSanctr(String htmlContent) throws IOException, DocumentException {

        // --- 1. 폰트 및 CSS 로드 (기존과 동일) ---
        Resource fontResource = resourceLoader.getResource("classpath:fonts/NanumGothic.ttf");
        if (!fontResource.exists()) {
            throw new IOException("한글 폰트 파일을 찾을 수 없습니다: classpath:fonts/NanumGothic.ttf");
        }
        String fontPath = fontResource.getFile().getAbsolutePath();

        Resource cssResource = resourceLoader.getResource("classpath:static/css/contract.css");
        String cssContent = "";
        if (cssResource.exists()) {
            try (InputStream cssStream = cssResource.getInputStream()) {
                cssContent = new String(cssStream.readAllBytes());
            }
        }

        // --- 2. (★수정★) Jsoup Cleaner가 'style'과 'class' 속성을 허용하도록 변경 ---

        // 2-1. (소독) Safelist(화이트리스트)를 사용하여 유해 태그 제거
        Safelist safelist = Safelist.relaxed();

        // (★CSS 적용 핵심★)
        // 'relaxed' 기본값에 더해, ":all"(모든 태그)에
        // 1. 'style' 속성 (인라인 스타일)
        // 2. 'class' 속성 (CSS 파일 연동)
        // 을 허용합니다.
        safelist.addAttributes(":all", "style", "class");

        // 2-2. Jsoup.clean()은 <body> 태그 내부의 안전한 HTML만 반환합니다.
        String safeHtml = Jsoup.clean(htmlContent, safelist);

        // 2-3. (XHTML 변환) "소독된" HTML을 Jsoup Document로 파싱
        Document document = Jsoup.parseBodyFragment(safeHtml);

        // 2-4. (XHTML 오류 수정) Jsoup의 출력 설정을 XHTML로 강제
        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        document.outputSettings().charset("UTF-8");

        // 2-5. <body> 태그 내부의 HTML만 "XHTML 문자열"로 추출
        String bodyContentOnly = document.body().html();


        // --- 3. XHTML 문서 조립 (껍데기 씌우기) ---
        // (CSS가 class="contract-container" 등을 사용하므로 씌워줍니다)
        StringBuilder xhtmlBuilder = new StringBuilder();
        xhtmlBuilder.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
        xhtmlBuilder.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        xhtmlBuilder.append("<head>");
        xhtmlBuilder.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
        xhtmlBuilder.append("<style type=\"text/css\">");
        xhtmlBuilder.append("body { font-family: 'Nanum Gothic'; }");
        xhtmlBuilder.append(cssContent); // 외부 CSS 주입
        xhtmlBuilder.append("</style>");
        xhtmlBuilder.append("</head>");

        // (★껍데기 추가★)
        xhtmlBuilder.append("<body>");
        xhtmlBuilder.append("<div class='contract-container'>"); // CSS 파일이 요구하는 래퍼

        xhtmlBuilder.append(bodyContentOnly); // "style과 class가 살아있는" HTML 삽입

        xhtmlBuilder.append("</div>");
        xhtmlBuilder.append("</body>");

        xhtmlBuilder.append("</html>");

        String finalHtml = xhtmlBuilder.toString();

        // --- 4. 렌더러 생성 및 폰트 등록 (기존과 동일) ---
        ITextRenderer renderer = new ITextRenderer();
        renderer.getFontResolver().addFont(
                fontPath,
                "Nanum Gothic",
                BaseFont.IDENTITY_H,      // 한글/유니코드 필수
                BaseFont.EMBEDDED,        // PDF에 폰트 포함
                null
        );
        renderer.setDocumentFromString(finalHtml);
        renderer.layout();

        // --- 5. PDF를 메모리(byte[])로 생성 (파일 저장 -> 메모리) ---
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        renderer.createPDF(outputStream);

        // --- 6. byte[] 반환 ---
        byte[] pdfBytes = outputStream.toByteArray();
        outputStream.close();
        return pdfBytes;
    }
}