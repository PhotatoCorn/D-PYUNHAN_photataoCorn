package kr.or.ddit.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler{  //websocket 기본 동작 상속받기위해 extends함
   
   private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();  //채팅을 하기 위해 클라이언트가 여러개이므로 접속하는 모든 세션을 저장해둬야함 즉, 여기서 sessions는 모든 클라이언트를 의미
   private final ObjectMapper mapper = new ObjectMapper();  //chat.html에서 던지는 json 파싱용
   
   //클라이언트 접속 시 세션 저장
   @Override
   public void afterConnectionEstablished(WebSocketSession session) throws Exception{  //afterConnectionEstablished은 sping 웹소캣에서 정한 콜백 메서드
      sessions.add(session);  //여기서 session은 1명의 클라이언트의 접속 정보(세션id,로그인 유저 등)
   }
   
   //클라이언트 메세지 수신 시
   
   @Override
   protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception{  //handleTextMessage는 웹소캣 프레임워크가 자동으로 호출하는 콜백 메서드, 클라이언트로 부터 메시지가 오면 자동으로 내부에서 실행하므로 public으로 열어둘 필요 없음
   
      Map<String,Object> msg = mapper.readValue(message.getPayload(),Map.class);  //클라이언트가 보낸 json 내용
      String type=(String) msg.get("type");
      
      //클라이언트 입장 시 입장 알림
      if("join".equals(type)) {
         String name = (String) msg.get("name");
         session.getAttributes().put("name", name);  //세션에 클라이언트 이름 꺼내기
         Map<String,Object> payload = Map.of(
           "type", "notice",
           "text", "📢 " + name + "님이 입장했습니다."
          );
         broadcast(payload);
         return;
      }
      
      
      // 일반 채팅 시
      if("chat".equals(type)) {
         String name = (String) msg.get("name");
         String text = (String) msg.get("text");
         
         Map<String,Object> payload = new HashMap<>();
         payload.put("type", "chat");
         payload.put("name", name);
         payload.put("text", text);
         broadcast(payload);         
         return;
      }
      
            
       //클라이언트 입력 중인 경우
      if("typing".equals(type)) {
         String name= (String) session.getAttributes().get("name");  
         
         Map<String,Object> payload = new HashMap<>();
         payload.put("type", "typing");
         payload.put("name", name);
         broadcast(payload);   
      }
     }   
         
      //클라이언트 퇴장 시
      @Override
      public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception{
          sessions.remove(session);
          String name = (String) session.getAttributes().get("name");
          if(name != null) {
            Map<String,Object> payload = Map.of(
                 "type", "notice",
               "text", "📢 " + name + "님이 퇴장했습니다."
             );
            broadcast(payload); 
      }      
   }
    
   //broadcast 선언   
   private void broadcast(Map<String,Object> payload) throws Exception{
      
      String json=mapper.writeValueAsString(payload);
      
      for(WebSocketSession s: sessions) {
         if(s.isOpen()) {    //세션이 연결된 클라이언트에게만 메세지보내기
            s.sendMessage(new TextMessage(json));
         }
      }
   }
   
}
