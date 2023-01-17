package controller;

import model.GradeRequestDTO;

import java.util.ArrayList;

public class GradeRequestController {
    private ArrayList<GradeRequestDTO> list;
    private int nextId;

    public GradeRequestController() {
        list = new ArrayList<>();
        nextId = 1;
    }

    public void insert(GradeRequestDTO gradeRequestDTO) {
        gradeRequestDTO.setIdx(nextId++);
        list.add(gradeRequestDTO);
    }

    public void update(GradeRequestDTO gradeRequestDTO) {
        list.set(list.indexOf(gradeRequestDTO), gradeRequestDTO);
    }

    public void delete(int idx) {
        list.remove(new GradeRequestDTO(idx));
    }

    public GradeRequestDTO selectByIdx(int idx) {
        for (GradeRequestDTO g : list) {
            if (g.getIdx() == idx) {
                return new GradeRequestDTO(g);
            }
        }
        return null;
    }

    public ArrayList<GradeRequestDTO> selectAll() {
        ArrayList<GradeRequestDTO> temp = new ArrayList<>();
        for (GradeRequestDTO g : list) {
            temp.add(new GradeRequestDTO(g));
        }
        return temp;
    }

    public GradeRequestDTO selectByUserIdx(int userIdx) {
        for (GradeRequestDTO g : list) {
            if (g.getUserIdx() == userIdx) {
                return new GradeRequestDTO(g);
            }
        }
        return null;
    }
}
