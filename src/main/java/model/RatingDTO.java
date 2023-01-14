package model;

public class RatingDTO {
    private int idx;
    private int registerIdx;
    private int movieIdx;
    private int rating;
    private String review;

    public RatingDTO() {}

    public RatingDTO(int idx) {
        this.idx = idx;
    }

    public RatingDTO(int registerIdx, int movieIdx, int rating, String review) {
        this.registerIdx = registerIdx;
        this.movieIdx = movieIdx;
        this.rating = rating;
        this.review = review;
    }

    public RatingDTO(RatingDTO origin) {
        idx = origin.idx;
        registerIdx = origin.registerIdx;
        movieIdx = origin.movieIdx;
        rating = origin.rating;
        review = origin.review;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getRegisterIdx() {
        return registerIdx;
    }

    public void setRegisterIdx(int registerIdx) {
        this.registerIdx = registerIdx;
    }

    public int getMovieIdx() {
        return movieIdx;
    }

    public void setMovieIdx(int movieIdx) {
        this.movieIdx = movieIdx;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RatingDTO) {
            RatingDTO r = (RatingDTO) obj;
            return idx == r.idx;
        }
        return false;
    }

    @Override
    public String toString() {
        return "{" +
                "\"idx\": " + idx + ", " +
                "\"registerIdx\": " + registerIdx + ", " +
                "\"movieIdx\": " + movieIdx + ", " +
                "\"rating\": " + rating + ", " +
                "\"review\": " + review +
                "}";
    }
}
