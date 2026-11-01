package model;
public class User {
    private int id;
    private String username;
    private String email;
    private int xp;
    private int level;

    public User() {}

    public User(int id, String username, String email, int xp, int level) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.xp = xp;
        this.level = level;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public int getXp() { return xp; }
    public void setXp(int xp) { this.xp = xp; }
    public int getLevel1() { return level; }
    public void setLevel(int level) { this.level = level; }

	public Object getLevel() {
		return null;
	}
}