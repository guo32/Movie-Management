package controller;

import model.ScreenInfoDTO;

import java.util.ArrayList;

public class ScreenInfoController {
    private ArrayList<ScreenInfoDTO> list;
    private int nextId;

    public ScreenInfoController() {
        list = new ArrayList<>();
        nextId = 1;
    }

    public void insert(ScreenInfoDTO screenInfoDTO) {
        screenInfoDTO.setIdx(nextId++);
        list.add(screenInfoDTO);
    }

    public void update(ScreenInfoDTO screenInfoDTO) {
        list.set(list.indexOf(screenInfoDTO), screenInfoDTO);
    }

    public void delete(int idx) {
        list.remove(new ScreenInfoDTO(idx));
    }

    // select 관련
    public ScreenInfoDTO selectByIdx(int idx) {
        for (ScreenInfoDTO s : list) {
            if (s.getIdx() == idx) {
                return new ScreenInfoDTO(s);
            }
        }
        return null;
    }

    public ArrayList<ScreenInfoDTO> selectByTheaterIdx(int theaterIdx) {
        ArrayList<ScreenInfoDTO> temp = new ArrayList<>();
        for (ScreenInfoDTO s : list) {
            if (s.getTheaterIdx() == theaterIdx) {
                temp.add(new ScreenInfoDTO(s));
            }
        }
        return temp;
    }

    public ArrayList<ScreenInfoDTO> selectByMovieIdx(int movieIdx, int theaterIdx) {
        ArrayList<ScreenInfoDTO> temp = new ArrayList<>();
        for (ScreenInfoDTO s : selectByTheaterIdx(theaterIdx)) {
            if (s.getMovieIdx() == movieIdx) {
                temp.add(new ScreenInfoDTO(s));
            }
        }
        return temp;
    }

    public ArrayList<ScreenInfoDTO> selectByMovieIdx(int movieIdx) {
        ArrayList<ScreenInfoDTO> temp = new ArrayList<>();
        for (ScreenInfoDTO s : list) {
            if (s.getMovieIdx() == movieIdx) {
                temp.add(new ScreenInfoDTO(s));
            }
        }
        return temp;
    }
}
