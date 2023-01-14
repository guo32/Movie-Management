package controller;

import model.UserDTO;

import java.util.ArrayList;

public class UserController {
    // 사용자 등급 상수
    private final int GENERAL = 1;
    private final int REVIEWER = 2;
    private final int MANAGER = 3;

    private ArrayList<UserDTO> list; // DB: 회원 객체 저장
    private int nextId; // auto increment

    // 생성자
    public UserController() {
        list = new ArrayList<>();
        nextId = 1;
    }

    // 회원 삽입
    public void insert(UserDTO userDTO) {
        userDTO.setIdx(nextId++);
        userDTO.setGrade(GENERAL);
        list.add(userDTO);
    }

    // 회원 수정
    public void update(UserDTO userDTO) {
        list.set(list.indexOf(userDTO), userDTO);
    }

    // 회원 삭제
    public void delete(int idx) {
        list.remove(new UserDTO(idx));
    }

    // 회원 번호로 회원 찾기
    public UserDTO selectByIdx(int idx) {
        for (UserDTO u : list) {
            if (u.getIdx() == idx) {
                return new UserDTO(u);
            }
        }
        return null;
    }

    // 전체 회원 리스트
    public ArrayList<UserDTO> selectAll() {
        ArrayList<UserDTO> temp = new ArrayList<>();
        for (UserDTO u : list) {
            temp.add(new UserDTO(u));
        }
        return temp;
    }

    // 회원 인증
    public UserDTO auth(String username, String password) {
        for (UserDTO u : list) {
            if (username.equals(u.getUsername()) && password.equals(u.getPassword())) {
                return new UserDTO(u);
            }
        }
        return null;
    }

    // 회원 아이디(username) 중복 여부 체크
    // true: 중복O
    // false: 중복X
    public boolean duplicateUsername(String username) {
        for (UserDTO u : list) {
            if (username.equals(u.getUsername())) {
                return true;
            }
        }
        return false;
    }

}
