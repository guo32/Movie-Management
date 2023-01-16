package controller;

import model.MovieDTO;

import java.util.ArrayList;

public class MovieController {
    private ArrayList<MovieDTO> list;
    private int nextId; // auto increment

    public MovieController() {
        list = new ArrayList<>();
        nextId = 1;
    }

    public void insert(MovieDTO movieDTO) {
        movieDTO.setIdx(nextId);
        list.add(movieDTO);
    }

    public void update(MovieDTO movieDTO) {
        list.set(list.indexOf(movieDTO), movieDTO);
    }

    public void delete(int idx) {
        list.remove(new MovieDTO(idx));
    }

    public MovieDTO selectByIdx(int idx) {
        for (MovieDTO m : list) {
            if (m.getIdx() == idx) {
                return new MovieDTO(m);
            }
        }
        return null;
    }

    public ArrayList<MovieDTO> selectAll() {
        ArrayList<MovieDTO> temp = new ArrayList<>();
        for (MovieDTO m : list) {
            temp.add(new MovieDTO(m));
        }
        return temp;
    }
}
