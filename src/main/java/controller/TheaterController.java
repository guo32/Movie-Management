package controller;

import model.TheaterDTO;

import java.util.ArrayList;

public class TheaterController {
    private ArrayList<TheaterDTO> list;
    private int nextId;

    public TheaterController() {
        list = new ArrayList<>();
        nextId = 1;
    }

    public void insert(TheaterDTO theaterDTO) {
        theaterDTO.setIdx(nextId++);
        list.add(theaterDTO);
    }

    public void update(TheaterDTO theaterDTO) {
        list.set(list.indexOf(theaterDTO), theaterDTO);
    }

    public void delete(int idx) {
        list.remove(new TheaterDTO(idx));
    }

    // select 관련
    public TheaterDTO selectByIdx(int idx) {
        for (TheaterDTO t : list) {
            if (idx == t.getIdx()) {
                return new TheaterDTO(t);
            }
        }
        return null;
    }

    public ArrayList<TheaterDTO> selectAll() {
        ArrayList<TheaterDTO> temp = new ArrayList<>();
        for (TheaterDTO t : list) {
            temp.add(new TheaterDTO(t));
        }
        return temp;
    }
}
