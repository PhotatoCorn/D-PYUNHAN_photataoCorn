package kr.or.ddit.util.scheduler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import kr.or.ddit.mapper.EmailQueueMapper;
import kr.or.ddit.service.EmailService;
import kr.or.ddit.vo.EmailQueueVO;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EmailScheduler {

    @Autowired
    private EmailQueueMapper emailQueueMapper;
    @Autowired
    private EmailService emailService;

    @Scheduled(cron = "0 */1 * * * *")  // 1분마다
    public void sendPendingEmails() {
        List<EmailQueueVO> pendingEmails = emailQueueMapper.selectPendingEmails();
        for (EmailQueueVO emailQueue : pendingEmails) {
            emailService.sendEmailFromQueue(emailQueue);
            try { Thread.sleep(100); } catch (InterruptedException ignored) {}
        }
    }
}
