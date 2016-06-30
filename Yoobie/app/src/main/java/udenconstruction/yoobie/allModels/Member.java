package udenconstruction.yoobie.allModels;

public class Member {

    public int id;
    public String username;
    public String password;
    public String email;
    public String gender;
    public String date_of_birth;
    public String location;
    public String device;
    public String mobile;

    public Member(int ii, String u, String p, String e, String g, String d, String l, String de, String mo){
        id = ii;
        username = u;
        password = p;
        email = e;
        gender = g;
        date_of_birth = d;
        location = l;
        device = de;
        mobile = mo;
    }
}
