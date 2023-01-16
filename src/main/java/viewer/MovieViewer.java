package viewer;

import controller.MovieController;
import controller.UserController;
import model.MovieDTO;
import model.UserDTO;
import util.ScannerUtil;

import java.util.ArrayList;
import java.util.Scanner;

public class MovieViewer {
    // 사용자 등급 상수
    private final int GENERAL = 1;
    private final int REVIEWER = 2;
    private final int MANAGER = 3;

    // 영화 등급 상수
    private final int VIEWALL = 1; // 전체 관람가
    private final int VIEW12 = 2; // 12세 관람가
    private final int VIEW15 = 3; // 15세 관람가
    private final int VIEW18 = 4; // 청소년 관람 불가
    private final int TEST = 5; // 평가용
    private final Scanner SCANNER;

    private MovieController movieController;
    private UserController userController;
    private RatingViewer ratingViewer;
    private UserDTO login;

    public MovieViewer(Scanner scanner) {
        SCANNER = scanner;
        movieController = new MovieController();
    }

    public void setRatingViewer(RatingViewer ratingViewer) {
        this.ratingViewer = ratingViewer;
    }

    public void setUserController(UserController userController) {
        this.userController = userController;
    }

    public MovieController getMovieController() {
        return movieController;
    }

    public void showMenu(UserDTO login) {
        this.login = login;
        String message;
        int userChoice = 0;
        if (login.getGrade() != MANAGER) {
            message = "[1] 영화 목록 [0] 뒤로 가기";
            userChoice = ScannerUtil.nextInt(SCANNER, message, 0, 1);
        } else {
            message = "[1] 영화 목록 [2] 영화 등록 [0] 뒤로 가기";
            userChoice = ScannerUtil.nextInt(SCANNER, message, 0, 2);
        }
        if (userChoice == 1) {
            printMovieList();
        } else if (userChoice == 2) {
            registerMovie();
        }
    }

    private void registerMovie() {
        String message = "등록할 영화의 제목을 입력해주세요.";
        String title = ScannerUtil.nextLine(SCANNER, message);

        message = "등록할 영화의 줄거리를 입력해주세요.";
        String content = ScannerUtil.nextLine(SCANNER, message);

        message = "등록할 영화의 등급을 입력해주세요.\n[1] 전체 관람가 [2] 12세 관람가 [3] 15세 관람가 [4] 청소년 관람 불가 [5] 평가용";
        int grade = ScannerUtil.nextInt(SCANNER, message, 1, 5);

        movieController.insert(new MovieDTO(title, content, grade));
        System.out.println("정상적으로 등록되었습니다.");
    }

    private void printMovieList() {
        ArrayList<MovieDTO> list = movieController.selectAll();
        System.out.println("+-------------------------------------------+");
        if(list.isEmpty()) {
            System.out.println(" * 등록된 영화가 없습니다.");
            System.out.println("+-------------------------------------------+");
        } else {
            for (MovieDTO m : list) {
                System.out.println(" * [" + m.getIdx() + "] " + m.getTitle());
                System.out.println("+-------------------------------------------+");
            }
        }

        String message = "상세 보기할 영화 번호를 입력해주세요.\n[입력] 상세 보기 [0] 뒤로 가기";
        int userChoice = ScannerUtil.nextInt(SCANNER, message);
        while (userChoice != 0 && movieController.selectByIdx(userChoice) == null) {
            System.out.println("존재하지 않는 영화 번호입니다.");
            userChoice = ScannerUtil.nextInt(SCANNER, message);
        }
        if (userChoice != 0) {
            printMovieInfo(userChoice);
        } else {
            showMenu(login);
        }
    }

    public void printMovieInfo(int idx) {
        MovieDTO movieDTO = movieController.selectByIdx(idx);
        System.out.println("+===============================+");
        System.out.println(" < " + movieDTO.getTitle() + " > ");
        System.out.println("+-------------------------------+");
        if (movieDTO.getGrade() == VIEWALL) {
            System.out.println(" 전체 관람가");
        } else if (movieDTO.getGrade() == VIEW12) {
            System.out.println(" 12세 관람가");
        } else if (movieDTO.getGrade() == VIEW15) {
            System.out.println(" 15세 관람가");
        } else if (movieDTO.getGrade() == VIEW18) {
            System.out.println(" 청소년 관람 불가");
        } else if (movieDTO.getGrade() == TEST) {
            System.out.println(" 평가용");
        }
        System.out.println("+-------------------------------+");
        System.out.println(" " + movieDTO.getContent());
        System.out.println("+===============================+");

        String message;
        int userChoice = 0;
        if (login.getGrade() != MANAGER) {
            message = "[3] 평점 [4] 영화 목록으로";
            userChoice = ScannerUtil.nextInt(SCANNER, message, 3, 4);
        } else {
            message = "[1] 수정 [2] 삭제 [3] 평점 [4] 영화 목록으로";
            userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 4);
        }

        if (userChoice == 1) {
            updateMovie(idx);
        } else if (userChoice == 2) {
            deleteMovie(idx);
        } else if (userChoice == 3) {
            // 평점
            ratingViewer.setUserController(userController);
            ratingViewer.showMenu(idx, login);
        }

        printMovieList();
    }

    private void updateMovie(int idx) {
        MovieDTO temp = movieController.selectByIdx(idx);

        String message = "수정할 영화의 제목을 입력해주세요.";
        temp.setTitle(ScannerUtil.nextLine(SCANNER, message));

        message = "수정할 영화의 줄거리를 입력해주세요.";
        temp.setContent(ScannerUtil.nextLine(SCANNER, message));

        message = "수정할 영화의 등급을 입력해주세요.\n[1] 전체 관람가 [2] 12세 관람가 [3] 15세 관람가 [4] 청소년 관람 불가 [5] 평가용";
        temp.setGrade(ScannerUtil.nextInt(SCANNER, message, 1, 5));

        movieController.update(temp);
        System.out.println("정상적으로 수정되었습니다.");
        printMovieInfo(idx);
    }

    private void deleteMovie(int idx) {
        String message = "정말로 삭제하시겠습니까?\n[Y] 삭제 [N] 취소";
        String yesNo = ScannerUtil.nextLine(SCANNER, message);
        if (yesNo.equalsIgnoreCase("Y")) {
            movieController.delete(idx);
            System.out.println("정상적으로 삭제되었습니다.");
            printMovieList();
        } else {
            System.out.println("취소되었습니다.");
            printMovieInfo(idx);
        }
    }
}
