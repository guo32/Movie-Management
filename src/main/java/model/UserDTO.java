package model;

public class UserDTO {
    private int idx; // 회원 번호(auto)
    private String username; // 회원 아이디
    private String password; // 회원 비밀번호
    private String nickname; // 회원 닉네임
    private int grade; // 회원 등급 (1. 일반 | 2. 전문 평론가 | 3. 관리자)

    // 생성자
    public UserDTO() {}

    public UserDTO(int idx) {
        this.idx = idx;
    }

    public UserDTO(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }

    public UserDTO(UserDTO origin) {
        idx = origin.idx;
        username = origin.username;
        password = origin.password;
        nickname = origin.nickname;
        grade = origin.grade;
    }

    // Getter, Setter
    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    // equals()
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UserDTO) {
            UserDTO u = (UserDTO) obj;
            return idx == u.idx;
        }
        return false;
    }

    // toString()
    @Override
    public String toString() {
        return "{" +
                "\"idx\": " + idx + ", " +
                "\"username\": " + username + ", " +
                "\"password\": " + password + ", " +
                "\"nickname\": " + nickname + ", " +
                "\"grade\": " + grade +
                "}";
    }
}