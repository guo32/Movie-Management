package model;

public class TheaterDTO {
    private int idx;
    private String name;
    private String location;
    private String telephone;

    // 생성자
    public TheaterDTO() {}

    public TheaterDTO(int idx) {
        this.idx = idx;
    }

    public TheaterDTO(String name, String location, String telephone) {
        this.name = name;
        this.location = location;
        this.telephone = telephone;
    }

    public TheaterDTO(TheaterDTO origin) {
        idx = origin.idx;
        name = origin.name;
        location = origin.location;
        telephone = origin.telephone;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TheaterDTO) {
            TheaterDTO t = (TheaterDTO) obj;
            return idx == t.idx;
        }
        return false;
    }

    @Override
    public String toString() {
        return "{" +
                "\"idx\": " + idx + ", " +
                "\"name\": " + name + ", " +
                "\"location\": " + location + ", " +
                "\"telephone\": " + telephone +
                "}";
    }
}
