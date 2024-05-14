package dbmanager;

public class Twit {
    public int id;
    public String name;
    public String content;
    public String createdAt;

    public Twit(String id, String name, String content, String createAt) {
        this.id = Integer.parseInt(id);
        this.name = name;
        this.content = content;
        this.createdAt = createAt;
    } 
}
