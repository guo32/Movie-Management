package controller;

import model.RatingDTO;

import java.util.ArrayList;

public class RatingController {
    private ArrayList<RatingDTO> list;
    private int nextId;

    public RatingController() {
        list = new ArrayList<>();
        nextId = 1;
    }

    public void insert(RatingDTO ratingDTO) {
        ratingDTO.setIdx(nextId++);
        list.add(ratingDTO);
    }

    public void update(RatingDTO ratingDTO) {
        list.set(list.indexOf(ratingDTO), ratingDTO);
    }

    public void delete(int idx) {
        list.remove(new RatingDTO(idx));
    }

    // select 관련
    public RatingDTO selectByIdx(int idx) {
        for (RatingDTO r : list) {
            if (r.getIdx() == idx) {
                return new RatingDTO(r);
            }
        }
        return null;
    }

    public ArrayList<RatingDTO> selectAll() {
        ArrayList<RatingDTO> temp = new ArrayList<>();
        for (RatingDTO r : list) {
            temp.add(new RatingDTO(r));
        }
        return temp;
    }

    // movie idx 검색 후 review list 반환
    public ArrayList<RatingDTO> selectByMovieIdx(int movieIdx) {
        ArrayList<RatingDTO> temp = new ArrayList<>();
        for (RatingDTO r : list) {
            if (r.getMovieIdx() == movieIdx) {
                temp.add(new RatingDTO(r));
            }
        }
        return temp;
    }

    // movie idx 별 전체 평점 평균 계산
    public double calculateAverageRatingByMovie(int movieIdx) {
        ArrayList<RatingDTO> temp = selectByMovieIdx(movieIdx);
        int sum = 0;
        for (RatingDTO r : temp) {
            sum += r.getRating();
        }
        return (double)sum / temp.size();
    }
}
