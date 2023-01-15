package viewer;

import controller.MovieController;
import model.MovieDTO;
import util.ScannerUtil;

import java.util.ArrayList;
import java.util.Scanner;

public class MovieViewer {
    private final int VIEWALL = 1; // 전체 관람가
    private final int VIEW12 = 2; // 12세 관람가
    private final int VIEW15 = 3; // 15세 관람가
    private final int VIEW18 = 4; // 청소년 관람 불가
    private final int TEST = 5; // 평가용
    private final Scanner SCANNER;

    private MovieController movieController;

    public MovieViewer(Scanner scanner) {
        SCANNER = scanner;
        movieController = new MovieController();
    }

    public void showMenu() {
        String message = "[1] 영화 목록 [2] 뒤로 가기";
        int userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 2);
        if (userChoice == 1) {
            printMovieList();
        }
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
            showMenu();
        }
    }

    public void printMovieInfo(int idx) {
        MovieDTO movieDTO = movieController.selectByIdx(idx);
        System.out.println("+===============================+");
        System.out.println(" < " + movieDTO.getTitle() + " > ");
        System.out.println("+-------------------------------+");
        System.out.println(" " + movieDTO.getContent());
        System.out.println("+===============================+");

        String message = "[1] 평점 [2] 영화 목록으로";
        int userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 2);
        // 평점 추가 후 수정

        printMovieList();
    }
}
