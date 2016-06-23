package announcements.controllers;

import announcements.domain.Thread;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "threadViewController")
@ViewScoped
public class ThreadViewController implements Serializable {
    ArrayList<Thread> threads;
    
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
        System.out.println("DDF");
       if (threads == null) {
           threads = new ArrayList<>();
           
           threads.add(new Thread(1, 1, "Thread 1", "This is thread 1.", LocalDate.of(2016, 1, 1), LocalDate.of(2016, 1, 1), 2, "Course"));
           threads.add(new Thread(2, 1, "Thread 2", "This is thread 2.", LocalDate.of(2016, 2, 1), LocalDate.of(2016, 2, 2), 2, "News"));
           threads.add(new Thread(3, 1, "Thread 3", "This is thread 3.", LocalDate.of(2016, 3, 1), LocalDate.of(2016, 3, 2), 2, "Job Posting"));
           threads.add(new Thread(4, 1, "Thread 4", "This is thread 4.", LocalDate.of(2016, 4, 1), LocalDate.of(2016, 4, 2), 2, "Teacher"));
           threads.add(new Thread(5, 1, "Thread 5", "This is thread 5.", LocalDate.of(2016, 5, 1), LocalDate.of(2016, 5, 2), 2, "Club"));
       }
    }

    public ArrayList<Thread> getThreads() {
        return threads;
    }
    
}
