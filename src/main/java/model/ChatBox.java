package model;

public class ChatBox {
    private int id;
    private int user1;
    private int user2;
    
    public ChatBox() {}

    public ChatBox(int t_id, int t_user1, int t_user2) {
        setId(t_id);
        setUser1(t_user1);
        setUser2(t_user2);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUser1(int user1) {
        this.user1 = user1;
    }

    public void setUser2(int user2) {
        this.user2 = user2;
    }

    public int getId() {
        return id;
    }

    public int getUser1() {
        return user1;
    }

    public int getUser2() {
        return user2;
    }

    public static ChatBox fromResultSet(java.sql.ResultSet rs) throws java.sql.SQLException {
        return new ChatBox(rs.getInt("id"),
         rs.getInt("user1"),
         rs.getInt("user2"));
    }
}
