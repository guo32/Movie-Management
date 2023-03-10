package viewer;

import controller.RatingController;
import controller.UserController;
import model.RatingDTO;
import model.UserDTO;
import util.ScannerUtil;

import java.util.ArrayList;
import java.util.Scanner;

public class RatingViewer {
    // 사용자 등급 상수
    private final int GENERAL = 1;
    private final int REVIEWER = 2;
    private final int MANAGER = 3;
    private final Scanner SCANNER;
    private final RatingController ratingController;
    private UserController userController;
    private UserViewer userViewer;

    private UserDTO login;

    public RatingViewer(Scanner scanner) {
        SCANNER = scanner;
        ratingController = new RatingController();
    }

    public void setUserController(UserController userController) {
        this.userController = userController;
    }

    public void setUserViewer(UserViewer userViewer) {
        this.userViewer = userViewer;
    }

    public void showMenu(int movieIdx, UserDTO login) {
        this.login = login;
        userViewer.setRatingController(ratingController);
        String message;
        int userChoice;
        if (login.getGrade() == MANAGER) {
            message = "[2] 전체 평점 [3] 평론가 평점/평론 [4] 일반 관람객 평점 [5] 뒤로 가기";
            userChoice = ScannerUtil.nextInt(SCANNER, message, 2, 5);
        } else {
            message = "[1] 평점 등록 [2] 전체 평점 [3] 평론가 평점/평론 [4] 일반 관람객 평점 [5] 뒤로 가기";
            userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 5);
        }

        if(userChoice == 1) {
            registerReview(movieIdx);
        } else if (userChoice == 2) {
            printAllReviewList(movieIdx);
        } else if (userChoice == 3) {
            printReviewerReviewList(movieIdx);
        } else if (userChoice == 4) {
            printGeneralRatingList(movieIdx);
        }
    }

    public void registerReview(int movieIdx) {
        boolean flag = false;
        ArrayList<RatingDTO> temp = ratingController.selectByMovieIdx(movieIdx);
        if (!temp.isEmpty()) {
            for (RatingDTO r : temp) {
                if (r.getRegisterIdx() == login.getIdx()) {
                    flag = true;
                }
            }
        }

        if (!flag) {
            String message = "해당 영화의 평점을 입력해주세요. (1~10점)";
            int rating = ScannerUtil.nextInt(SCANNER, message, 1, 10);
            String review = "";
            if (login.getGrade() == REVIEWER) {
                message = "해당 영화의 평론을 입력해주세요.";
                review = ScannerUtil.nextLine(SCANNER, message);
            }

            ratingController.insert(new RatingDTO(movieIdx, login.getIdx(), rating, review));
            System.out.println("정상적으로 등록되었습니다.");
        } else {
            System.out.println("이미 해당 영화에 평점을 입력하셨습니다.");
        }

        showMenu(movieIdx, login);
    }

    private void printAllReviewList(int movieIdx) {
        // 0: 전체 1: 일반 2: 평론가
        ArrayList<RatingDTO> list = ratingController.selectByMovieIdx(movieIdx);
        System.out.println("+===========================================+");
        System.out.println(" < 평균 " + ratingController.calculateAverageRatingByMovie(movieIdx) + "점 >");
        System.out.println("+-------------------------------------------+");
        if (list.isEmpty()) {
            System.out.println(" * 등록된 평점이 없습니다.");
            System.out.println("+-------------------------------------------+");
        } else {
            for (RatingDTO r : list) {
                // 등록자의 닉네임을 보여주기 위해서 UserViewer 연결해주기
                // 등록자의 등급을 확인하여 관리자인 경우 상세보기 활성화 시켜야 함
                // String ratingInfo = " * [" + r.getIdx() + "] " + r.getRating() + "점 - " + userController.selectByIdx(r.getRegisterIdx()).getUsername();
                String ratingInfo = " * [" + r.getIdx() + "] " + r.getRating() + "점 - " + userController.selectByIdx(r.getRegisterIdx()).getNickname();
                if (r.getRegisterIdx() == login.getIdx() || login.getGrade() == MANAGER) {
                    ratingInfo += " (수정/삭제)";
                }
                System.out.println(ratingInfo);
                System.out.println("+-------------------------------------------+");
            }
        }
        // 관리자 권한 추가함
        String message = "수정하거나 삭제할 평점의 번호를 입력해주세요.\n[입력] 수정/삭제 [0] 뒤로 가기";
        int userChoice = ScannerUtil.nextInt(SCANNER, message);
        RatingDTO ratingDTO = ratingController.selectByIdx(userChoice);
        while (userChoice != 0 && (ratingDTO == null || ratingDTO.getMovieIdx() != movieIdx ||
                        (ratingDTO.getRegisterIdx() != login.getIdx()) && login.getGrade() != MANAGER)) {
            System.out.println("권한이 없는 평점의 번호입니다.");
            userChoice = ScannerUtil.nextInt(SCANNER, message);
            ratingDTO = ratingController.selectByIdx(userChoice);
        }
        if (userChoice != 0) {
            message = "[1] 수정 [2] 삭제 [0] 뒤로 가기";
            int select = ScannerUtil.nextInt(SCANNER, message, 0, 2);
            if (select == 1) {
                updateReview(userChoice);
            } else if (select == 2) {
                deleteRating(userChoice);
            }
            printAllReviewList(movieIdx);
        } else {
            showMenu(movieIdx, login);
        }
    }

    private void printReviewerReviewList(int movieIdx) {
        ArrayList<RatingDTO> list = ratingController.selectByMovieIdx(movieIdx);
        double average = calculateAverageRatingByUserGrade(list, REVIEWER);
        int num = 0;
        System.out.println("+===========================================+");
        System.out.println(" < 평균 " + average + "점 >");
        System.out.println("+-------------------------------------------+");
        for (RatingDTO r : list) {
            if (userController.selectByIdx(r.getRegisterIdx()).getGrade() == REVIEWER) {
                System.out.println(" * [" + r.getIdx() + "] " + r.getRating() + "점 - " + userController.selectByIdx(r.getRegisterIdx()).getNickname());
                System.out.println("+-------------------------------------------+");
                num++;
            }
        }
        if (num == 0) {
            System.out.println(" * 등록된 평론가 평점/평론이 없습니다.");
            System.out.println("+-------------------------------------------+");
        }

        String message = "상세 보기할 평론 번호를 입력해주세요.\n[입력] 상세 보기 [0] 뒤로 가기";
        int userChoice = ScannerUtil.nextInt(SCANNER, message);
        RatingDTO ratingDTO = ratingController.selectByIdx(userChoice);
        while (userChoice != 0 && (ratingDTO == null || ratingDTO.getMovieIdx() != movieIdx ||
                userController.selectByIdx(ratingDTO.getRegisterIdx()).getGrade() != REVIEWER)) {
            System.out.println("권한이 없는 평론의 번호입니다.");
            userChoice = ScannerUtil.nextInt(SCANNER, message);
            ratingDTO = ratingController.selectByIdx(userChoice);
        }
        if (userChoice != 0) {
            printReviewerReview(userChoice);
        } else {
            showMenu(movieIdx, login);
        }
    }

    // 평론가 평점, 평론 개별 보기
    private void printReviewerReview(int idx) {
        RatingDTO ratingDTO = ratingController.selectByIdx(idx);
        System.out.println("+===============================+");
        System.out.println(" [작성자] " + userController.selectByIdx(ratingDTO.getRegisterIdx()).getNickname());
        System.out.println(" [평점] " + ratingDTO.getRating() + "점");
        System.out.println("+-------------------------------+");
        System.out.println(" [평론]");
        System.out.println(" " + ratingDTO.getReview());
        System.out.println("+===============================+");
        // 로그인한 사람으로 바꾸기
        if ((ratingDTO.getRegisterIdx() == login.getIdx() && login.getGrade() == REVIEWER) || login.getGrade() == MANAGER) {
            String message = "[1] 수정 [2] 삭제 [0] 뒤로 가기";
            int userChoice = ScannerUtil.nextInt(SCANNER, message, 0, 2);
            if (userChoice == 1) {
                // 수정
                updateReview(idx);
                printReviewerReview(idx);
            } else if (userChoice == 2) {
                // 삭제
                deleteReview(idx);
                printReviewerReviewList(ratingDTO.getMovieIdx());
            } else {
                printReviewerReviewList(ratingDTO.getMovieIdx());
            }
        }
    }

    private void updateReview(int idx) {
        RatingDTO ratingDTO = ratingController.selectByIdx(idx);
        String message = "수정할 평점을 입력해주세요. (1~10점)";
        ratingDTO.setRating(ScannerUtil.nextInt(SCANNER, message, 1, 10));

        if (login.getGrade() == REVIEWER) {
            message = "수정할 평론을 입력해주세요.";
            ratingDTO.setReview(ScannerUtil.nextLine(SCANNER, message));
        }

        ratingController.update(ratingDTO);
        System.out.println("정상적으로 수정되었습니다.");
    }

    private void deleteReview(int idx) {
        String message = "정말로 삭제하시겠습니까?\n[Y] 삭제 [N] 취소";
        String yesNo = ScannerUtil.nextLine(SCANNER, message);
        if (yesNo.equalsIgnoreCase("Y")) {
            ratingController.delete(idx);
            System.out.println("정상적으로 삭제되었습니다.");
        } else {
            System.out.println("취소되었습니다.");
            printReviewerReview(idx);
        }
    }

    private void printGeneralRatingList(int movieIdx) {
        // 0: 전체 1: 일반 2: 평론가
        ArrayList<RatingDTO> list = ratingController.selectByMovieIdx(movieIdx);
        double average = calculateAverageRatingByUserGrade(list, GENERAL);
        int num = 0;
        System.out.println("+===========================================+");
        System.out.println(" < 평균 " + average + "점 >");
        System.out.println("+-------------------------------------------+");
        for (RatingDTO r : list) {
            // 등록자의 닉네임을 보여주기 위해서 UserViewer 연결해주기
            // 등록자의 등급을 확인하여 관리자인 경우 상세보기 활성화 시켜야 함
            if (userController.selectByIdx(r.getRegisterIdx()).getGrade() == GENERAL) {
                String ratingInfo = " * [" + r.getIdx() + "] " + r.getRating() + "점 - " + userController.selectByIdx(r.getRegisterIdx()).getNickname();
                if (r.getRegisterIdx() == login.getIdx() || login.getGrade() == MANAGER) {
                    ratingInfo += " (수정/삭제)";
                }
                System.out.println(ratingInfo);
                System.out.println("+-------------------------------------------+");
                num++;
            }
        }
        if (num == 0) {
            System.out.println(" * 등록된 평점이 없습니다.");
            System.out.println("+-------------------------------------------+");
        }
        // 관리자 권한 추가함
        String message = "수정하거나 삭제할 평점의 번호를 입력해주세요.\n[입력] 수정/삭제 [0] 뒤로 가기";
        int userChoice = ScannerUtil.nextInt(SCANNER, message);
        RatingDTO ratingDTO = ratingController.selectByIdx(userChoice);
        while (userChoice != 0 && (ratingDTO == null || ratingDTO.getMovieIdx() != movieIdx ||
                (ratingDTO.getRegisterIdx() != login.getIdx()) && login.getGrade() != MANAGER)) {
            System.out.println("권한이 없는 평점의 번호입니다.");
            userChoice = ScannerUtil.nextInt(SCANNER, message);
            ratingDTO = ratingController.selectByIdx(userChoice);
        }
        if (userChoice != 0) {
            message = "[1] 수정 [2] 삭제 [0] 뒤로 가기";
            int select = ScannerUtil.nextInt(SCANNER, message, 0, 2);
            if (select == 1) {
                updateReview(userChoice);
            } else if (select == 2) {
                deleteRating(userChoice);
            }
            printGeneralRatingList(movieIdx);
        } else {
            showMenu(movieIdx, login);
        }
    }

    private double calculateAverageRatingByUserGrade(ArrayList<RatingDTO> list, int userGrade) {
        int sum = 0, count = 0;
        for (RatingDTO r : list) {
            if(userGrade == userController.selectByIdx(r.getRegisterIdx()).getGrade()) {
                sum += r.getRating();
                count++;
            }
        }

        return (double)sum / count;
    }

    private void deleteRating(int idx) {
        String message = "정말로 삭제하시겠습니까?\n[Y] 삭제 [N] 취소";
        String yesNo = ScannerUtil.nextLine(SCANNER, message);
        if (yesNo.equalsIgnoreCase("Y")) {
            ratingController.delete(idx);
            System.out.println("정상적으로 삭제되었습니다.");
        } else {
            System.out.println("취소되었습니다.");
        }
    }
}
