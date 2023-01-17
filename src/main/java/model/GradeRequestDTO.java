package model;

public class GradeRequestDTO {
    private int idx;
    private int userIdx;
    private int grade;

    public GradeRequestDTO() {}

    public GradeRequestDTO(int idx) {
        this.idx = idx;
    }

    public GradeRequestDTO(GradeRequestDTO origin) {
        idx = origin.idx;
        userIdx = origin.userIdx;
        grade = origin.grade;
    }

    public GradeRequestDTO(int userIdx, int grade) {
        this.userIdx = userIdx;
        this.grade = grade;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getUserIdx() {
        return userIdx;
    }

    public void setUserIdx(int userIdx) {
        this.userIdx = userIdx;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GradeRequestDTO) {
            GradeRequestDTO g = (GradeRequestDTO) obj;
            return idx == g.idx;
        }
        return false;
    }

    @Override
    public String toString() {
        return "{" +
                "\"idx\": " + idx + ", " +
                "\"userIdx\": " + userIdx + ", " +
                "\"grade\": " + grade +
                "}";
    }
}
