package announcements.controllers;

import announcements.domain.Post;
import announcements.domain.PostsFacade;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "threadViewController")
@ViewScoped
public class ThreadViewController implements Serializable {
    @EJB
    PostsFacade postFacade;

    List<Post> threads;
    
    @PostConstruct
    public void init() {
        refresh();

//        String pw = "password";
//        try {
//            MessageDigest md = MessageDigest.getInstance("SHA-256");
//            md.update(pw.getBytes("UTF-8")); // Change this to "UTF-16" if needed
//            byte[] digest = md.digest();
//            BigInteger bigInt = new BigInteger(1, digest);
//            System.out.println(bigInt.toString(16));
//        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
//        }
    }

    public void refresh() {
        threads = postFacade.GetByParentId(0);

        if (threads == null) {
            threads = new ArrayList<>();
        }
    }

    public List<Post> getThreads() {
        return threads;
    }

    public void delete(int postId) {
        Post post = postFacade.GetByPostId(postId);
        postFacade.remove(post);
        refresh();
    }
    
}
