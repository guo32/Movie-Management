package model;

public class MovieDTO {
    private int idx; // 영화 번호
    private String title; // 영화 제목
    private String content; // 영화 줄거리
    private int grade; // 영화 등급

    // 생성자
    public MovieDTO() {}

    public MovieDTO(int idx) {
        this.idx = idx;
    }

    public MovieDTO(String title, String content, int grade) {
        this.title = title;
        this.content = content;
        this.grade = grade;
    }

    public MovieDTO(MovieDTO origin) {
        idx = origin.idx;
        title = origin.title;
        content = origin.content;
        grade = origin.grade;
    }

    // getter, setter
    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MovieDTO) {
            MovieDTO m = (MovieDTO) obj;
            return idx == m.getIdx();
        }
        return false;
    }

    @Override
    public String toString() {
        return "{" +
                "\"idx\": " + idx + ", " +
                "\"title\": " + title + ", " +
                "\"content\": " + content + ", " +
                "\"grade\": " + grade +
                "}";
    }
}
