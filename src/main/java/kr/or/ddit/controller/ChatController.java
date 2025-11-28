package kr.or.ddit.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatController {

	@GetMapping("/chat")
	public String chatPage(Model model, Principal principal) {
		//스프링 시큐리티 로그인한 사용자 정보 가져오기
		String userName = principal.getName();
		model.addAttribute("userName",userName);
		return "chat/chat";   //chat.html로 리렌더링, 만약 로그인 안되어있으면 시큐리티에서 설정된 페이지로 이동
	}
}
