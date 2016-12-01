package hello;

import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class HelloController {

    private Facebook facebook;
    private ConnectionRepository connectionRepository;

    public HelloController(Facebook facebook, ConnectionRepository connectionRepository) {
        this.facebook = facebook;
        this.connectionRepository = connectionRepository;
    }

    @GetMapping
    public String helloFacebook(Model model) {
        if (connectionRepository.findPrimaryConnection(Facebook.class) == null) {
            return "redirect:/connect/facebook";
        }

        String [] fields = { "id", "email",  "first_name", "last_name", "name" };
        User userProfile = facebook.fetchObject("me", User.class, fields);

        String [] postFields = { "message", "created_time", "description" };
        List<Post> posts = facebook.fetchConnections("me", "save.saves", Post.class, postFields);


        model.addAttribute("facebookProfile", userProfile);
        PagedList<Post> feed = facebook.feedOperations().getFeed();
        model.addAttribute("posts", posts);
        return "hello";
    }

}