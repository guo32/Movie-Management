package model;

public class ScreenInfoDTO {
    private int idx;
    private int movieIdx;
    private int theaterIdx;
    private String playtime;

    // 생성자
    public ScreenInfoDTO() {}

    public ScreenInfoDTO(int idx) {
        this.idx = idx;
    }

    public ScreenInfoDTO(int movieIdx, int theaterIdx, String playtime) {
        this.movieIdx = movieIdx;
        this.theaterIdx = theaterIdx;
        this.playtime = playtime;
    }

    public ScreenInfoDTO(ScreenInfoDTO origin) {
        idx = origin.idx;
        movieIdx = origin.movieIdx;
        theaterIdx = origin.theaterIdx;
        playtime = origin.playtime;
    }

    // getter, setter
    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getMovieIdx() {
        return movieIdx;
    }

    public void setMovieIdx(int movieIdx) {
        this.movieIdx = movieIdx;
    }

    public int getTheaterIdx() {
        return theaterIdx;
    }

    public void setTheaterIdx(int theaterIdx) {
        this.theaterIdx = theaterIdx;
    }

    public String getPlaytime() {
        return playtime;
    }

    public void setPlaytime(String playtime) {
        this.playtime = playtime;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ScreenInfoDTO) {
            ScreenInfoDTO s = (ScreenInfoDTO) obj;
            return idx == s.idx;
        }
        return false;
    }

    @Override
    public String toString() {
        return "{" +
                "\"idx\": " + idx + ", " +
                "\"movieIdx\": " + movieIdx + ", " +
                "\"theaterIdx\": " + theaterIdx + ", " +
                "\"playtime\": " + playtime +
                "}";
    }
}
