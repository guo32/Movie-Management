package viewer;

import controller.TheaterController;
import model.TheaterDTO;
import util.ScannerUtil;

import java.util.ArrayList;
import java.util.Scanner;

public class TheaterView {
    private final Scanner SCANNER;
    private final TheaterController theaterController;

    public TheaterView(Scanner scanner) {
        SCANNER = scanner;
        theaterController = new TheaterController();
    }

    public void showMenu() {
        String message = "[1] 극장 목록 [2] 뒤로 가기";
        int userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 2);
        if (userChoice == 1) {
            printTheaterList();
        }
    }

    private void printTheaterList() {
        ArrayList<TheaterDTO> list = theaterController.selectAll();
        System.out.println("+-------------------------------------------+");
        if(list.isEmpty()) {
            System.out.println(" * 등록된 극장이 없습니다.");
            System.out.println("+-------------------------------------------+");
        } else {
            for (TheaterDTO t : list) {
                System.out.println(" * [" + t.getIdx() + "] " + t.getName());
                System.out.println("+-------------------------------------------+");
            }
        }
        String message = "상세 보기할 극장 번호를 입력해주세요.\n[입력] 상세 보기 [0] 뒤로 가기";
        int userChoice = ScannerUtil.nextInt(SCANNER, message);
        while (userChoice != 0 && theaterController.selectByIdx(userChoice) == null) {
            System.out.println("존재하지 않는 극장 번호입니다.");
            userChoice = ScannerUtil.nextInt(SCANNER, message);
        }
        if (userChoice != 0) {
            printTheaterInfo(userChoice);
        } else {
            showMenu();
        }
    }

    public void printTheaterInfo(int idx) {
        TheaterDTO theaterDTO = theaterController.selectByIdx(idx);
        System.out.println("+===============================+");
        System.out.println(" < " + theaterDTO.getName() + " > ");
        System.out.println("+-------------------------------+");
        System.out.println(" [주소] " + theaterDTO.getLocation());
        System.out.println(" [번호] " + theaterDTO.getTelephone());
        System.out.println("+===============================+");

        String message = "[1] 상영 중인 영화 목록 [2] 뒤로 가기";
        int userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 2);
        if (userChoice == 1) {
            // 상영 중인 영화 목록

        } else if (userChoice == 2) {
            printTheaterList();
        }
    }
}
