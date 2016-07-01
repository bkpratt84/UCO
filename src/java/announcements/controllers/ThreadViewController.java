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
import javax.persistence.NamedQuery;

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

        threads = postFacade.GetByParentId(null);

        if (threads == null) {
            threads = new ArrayList<>();

//           threads.add(new Post(1, 1, "Thread 1", "This is thread 1.", LocalDate.of(2016, 1, 1), LocalDate.of(2016, 1, 1), 2, "Course"));
//           threads.add(new Post(2, 1, "Thread 2", "This is thread 2.", LocalDate.of(2016, 2, 1), LocalDate.of(2016, 2, 2), 2, "News"));
//           threads.add(new Post(3, 1, "Thread 3", "This is thread 3.", LocalDate.of(2016, 3, 1), LocalDate.of(2016, 3, 2), 2, "Job Posting"));
//           threads.add(new Post(4, 1, "Thread 4", "This is thread 4.", LocalDate.of(2016, 4, 1), LocalDate.of(2016, 4, 2), 2, "Teacher"));
//           threads.add(new Post(5, 1, "Thread 5", "This is thread 5.", LocalDate.of(2016, 5, 1), LocalDate.of(2016, 5, 2), 2, "Club"));
        }
    }

    public List<Post> getThreads() {
        return threads;
    }

}
