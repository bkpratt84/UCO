package announcements.utility;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class EmailTemplate {
    public static String getEmailHTML(String url, String templateName, Map<String, String> map) {
        String message = null;
        
        try {
            Properties velocityProperties = new Properties();
            velocityProperties.put("file.resource.loader.path", url);

            VelocityEngine velocityEngine = new VelocityEngine();
            velocityEngine.init(velocityProperties);
            Template template = velocityEngine.getTemplate(templateName);

            VelocityContext velocityContext = new VelocityContext();

            for (Map.Entry<String, String> entry : map.entrySet()){
               velocityContext.put(entry.getKey(), entry.getValue());
            }

            StringWriter stringWriter = new StringWriter();
            template.merge(velocityContext, stringWriter);

            message = stringWriter.toString();
        } catch(Exception e) {
            System.out.println("Error with email template; " + e.toString());
        }
        
        return message == null ? null : message;
    }
}