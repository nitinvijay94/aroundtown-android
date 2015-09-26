package hackgt.crowder.model;

import java.util.ArrayList;

public class Comment {
    public ArrayList<Comment> replies = new ArrayList<Comment>();
    public String timeStamp;
    public String content;

    public Comment(String content, String timeStamp){
        this.content = content;
        this.timeStamp = timeStamp;
    }

}
