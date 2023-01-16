package viewer;

import controller.RatingController;
import model.RatingDTO;
import model.UserDTO;
import util.ScannerUtil;

import java.util.ArrayList;
import java.util.Scanner;

public class RatingViewer {
    private final Scanner SCANNER;
    private final RatingController ratingController;

    private UserDTO login;

    public RatingViewer(Scanner scanner) {
        SCANNER = scanner;
        ratingController = new RatingController();
    }

    public void showMenu(int movieIdx) {
        String message = "[1] 전체 평점 [2] 평론가 평점/평론 [3] 일반 관람객 평점 [4] 뒤로 가기";
        int userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 4);
        if (userChoice == 1) {
            printAllReviewList(movieIdx);
        } else if (userChoice == 2) {
            printReviewerReviewList(movieIdx);
        } else if (userChoice == 3) {

        }
    }

    private void printAllReviewList(int movieIdx) {
        // 0: 전체 1: 일반 2: 평론가
        ArrayList<RatingDTO> list = ratingController.selectByMovieIdx(movieIdx, 0);
        System.out.println("+-------------------------------------------+");
        if (list.isEmpty()) {
            System.out.println(" * 등록된 평점이 없습니다.");
            System.out.println("+-------------------------------------------+");
        } else {
            for (RatingDTO r : list) {
                // 등록자의 닉네임을 보여주기 위해서 UserViewer 연결해주기
                // 등록자의 등급을 확인하여 관리자인 경우 상세보기 활성화 시켜야 함
                System.out.println(" * [" + r.getIdx() + "] " + r.getRating() + "점 - " + r.getRegisterIdx());
                System.out.println("+-------------------------------------------+");
            }
        }
    }

    private void printReviewerReviewList(int movieIdx) {
        ArrayList<RatingDTO> list = ratingController.selectByMovieIdx(movieIdx, 2);
        System.out.println("+-------------------------------------------+");
        if (list.isEmpty()) {
            System.out.println(" * 등록된 평론가 평점/평론이 없습니다.");
            System.out.println("+-------------------------------------------+");
        } else {
            for (RatingDTO r : list) {
                System.out.println(" * [" + r.getIdx() + "] " + r.getRating() + "점 - " + r.getRegisterIdx());
                System.out.println("+-------------------------------------------+");
            }
        }
        String message = "상세 보기할 평론 번호를 입력해주세요.\n[입력] 상세 보기 [0] 뒤로 가기";
        int userChoice = ScannerUtil.nextInt(SCANNER, message);
        while (userChoice != 0 && !list.contains(ratingController.selectByIdx(userChoice))) {
            System.out.println("존재하지 않는 평론 번호입니다.");
            userChoice = ScannerUtil.nextInt(SCANNER, message);
        }
        if (userChoice != 0) {
            printReviewerReview(userChoice);
        }
    }

    private void printReviewerReview(int idx) {
        RatingDTO ratingDTO = ratingController.selectByIdx(idx);
        System.out.println("+===============================+");

    }
}
