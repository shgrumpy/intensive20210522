package main;

import com.fasterxml.jackson.databind.util.JSONPObject;
import main.model.Message;
import main.model.User;
import main.repos.MessageRepository;
import main.repos.UserRepository;
import main.response.MessageResponse;
import main.response.UserResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import javax.persistence.Entity;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
public class ChatController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @GetMapping(path = "/api/auth")
    public HashMap<String, Boolean> auth() {
        HashMap<String, Boolean> response = new HashMap<>();
        String sessionId = getSessionId();
        User user = userRepository.getBySessionId(sessionId);
        response.put("result", user != null);
        return response;
    }

    @PostMapping(path = "/api/users")
    public HashMap<String, Boolean> addUser(HttpServletRequest request) {
        String name = request.getParameter("name");
        String sessionId = getSessionId();
        User user = new User();
        user.setName(name);
        user.setRegTime(new Date());
        user.setSessionId(sessionId);
        userRepository.save(user);
        HashMap<String, Boolean> response = new HashMap<>();
        response.put("result", true);
        return response;
    }

    private String getSessionId() {
        return RequestContextHolder.getRequestAttributes().getSessionId();
    }

    @GetMapping(path = "/api/users")
    public HashMap<String, List> getUsers() {
        ArrayList<UserResponse> usersList =
                new ArrayList<>();
        Iterable<User> users = userRepository.findAll();
        for(User user: users) {
            UserResponse userItem = new UserResponse();
            userItem.setId(user.getId());
            userItem.setName(user.getName());
            usersList.add(userItem);
        }

        HashMap<String, List> response = new HashMap<>();

        response.put("users", usersList);

        return response;
    }


//    @GetMapping(path = "/api/messages")
//    public HashMap<String, List> getMessages() {
//        ArrayList<MessageResponse> messagesList =
//                new ArrayList<>();
//        Iterable<Message> messages = messageRepository.findAll();
//        for(Message message : messages) {
//            MessageResponse messageItem = new MessageResponse();
//            messageItem.setName(message.getUser().getName());
//            messageItem.setTime(
//                    formatter.format(message.getSendTime())
//            );
//            messageItem.setText(message.getText());
//            messagesList.add(messageItem);
//        }
//
//        HashMap<String, List> response = new HashMap<>();
//
//        response.put("messages", messagesList);
//
//        return response;
//    }
}
