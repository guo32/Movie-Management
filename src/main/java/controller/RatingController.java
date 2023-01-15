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
    public ArrayList<RatingDTO> selectByMovieIdx(int movieIdx, int userGrade) {
        ArrayList<RatingDTO> temp = new ArrayList<>();
        for (RatingDTO r : list) {
            if (r.getMovieIdx() == movieIdx) {
                if (userGrade == 1 && r.getReview() == null) { // 일반
                    temp.add(new RatingDTO(r));
                } else if (userGrade == 2 && r.getReview() != null) { // 평론가
                    temp.add(new RatingDTO(r));
                } else if (userGrade == 0) { // 전체
                    temp.add(new RatingDTO(r));
                }
            }
        }
        return temp;
    }
}
