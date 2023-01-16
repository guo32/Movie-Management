package viewer;

import controller.MovieController;
import controller.ScreenInfoController;
import model.MovieDTO;
import model.ScreenInfoDTO;
import model.UserDTO;
import util.ScannerUtil;

import java.util.ArrayList;
import java.util.Scanner;

public class ScreenInfoViewer {
    private final Scanner SCANNER;
    private final int MANAGER = 3;
    private ScreenInfoController screenInfoController;
    private MovieController movieController;
    private TheaterView theaterView;
    private UserDTO login;

    public ScreenInfoViewer(Scanner scanner) {
        SCANNER = scanner;
        screenInfoController = new ScreenInfoController();
    }

    public void setTheaterView(TheaterView theaterView) {
        this.theaterView = theaterView;
    }

    public void setMovieController(MovieController movieController) {
        this.movieController = movieController;
    }

    public void setLogin(UserDTO login) {
        this.login = login;
    }

    public void showScreenInfoListForTheater(int theaterIdx) {
        ArrayList<ScreenInfoDTO> list = screenInfoController.selectByTheaterIdx(theaterIdx);
        System.out.println("+-------------------------------------------+");
        if(list.isEmpty()) {
            System.out.println(" * 해당 극장에는 등록된 상영 정보가 없습니다.");
            System.out.println("+-------------------------------------------+");
        } else {
            for (ScreenInfoDTO s : list) {
                MovieDTO movie = movieController.selectByIdx(s.getMovieIdx());
                System.out.println(" [" + s.getIdx() + "] " + movie.getTitle() + "(" + s.getPlaytime() + ")");
                System.out.println("+-------------------------------------------+");
            }
        }
        if (login.getGrade() == MANAGER) {
            String message = "[1] 상영 정보 등록 [2] 상영 정보 수정 [3] 상영 정보 삭제 [0] 뒤로 가기";
            int userChoice = ScannerUtil.nextInt(SCANNER, message, 0, 3);
            if (userChoice == 1) {
                registerScreenInfo(theaterIdx);
            } else if (userChoice == 2) {
                // 수정
                message = "수정할 상영 정보 번호를 입력해주세요.\n[입력] 수정 [0] 뒤로 가기";
                int select = ScannerUtil.nextInt(SCANNER, message);
                while (select != 0 && screenInfoController.selectByIdx(select) == null) {
                    System.out.println("존재하지 않는 상영 정보 번호입니다.");
                    select = ScannerUtil.nextInt(SCANNER, message);
                }
                if (select != 0) {
                    updateScreenInfo(select);
                } else {
                    showScreenInfoListForTheater(theaterIdx);
                }
            } else if (userChoice == 3) {
                // 삭제
                message = "삭제할 상영 정보 번호를 입력해주세요.\n[입력] 삭제 [0] 뒤로 가기";
                int select = ScannerUtil.nextInt(SCANNER, message);
                while (select != 0 && screenInfoController.selectByIdx(select) == null) {
                    System.out.println("존재하지 않는 상영 정보 번호입니다.");
                    select = ScannerUtil.nextInt(SCANNER, message);
                }
                if (select != 0) {
                    deleteScreenInfo(select);
                    showScreenInfoListForTheater(theaterIdx);
                } else {
                    showScreenInfoListForTheater(theaterIdx);
                }
            }
        }
        theaterView.showMenu();
    }

    private void updateScreenInfo(int idx) {
        ScreenInfoDTO screenInfoDTO = screenInfoController.selectByIdx(idx);
        String regex = "([0-1][0-9]|2[0-4]):([0-5][0-9])";

        String message = "영화 시작 시간을 입력해주세요.(HH:mm)";
        String startTime = ScannerUtil.nextLine(SCANNER, message);
        while (!startTime.matches(regex)) {
            System.out.println("잘못된 입력입니다. 형식을 확인해주세요.");
            startTime = ScannerUtil.nextLine(SCANNER, message);
        }
        message = "영화 종료 시간을 입력해주세요. (HH:mm)";
        String endTime = ScannerUtil.nextLine(SCANNER, message);
        while (!endTime.matches(regex)) {
            System.out.println("잘못된 입력입니다. 형식을 확인해주세요.");
            endTime = ScannerUtil.nextLine(SCANNER, message);
        }
        String playtime = startTime + " ~ " + endTime;
        screenInfoDTO.setPlaytime(playtime);
        System.out.println("정상적으로 수정되었습니다.");

        showScreenInfoListForTheater(screenInfoDTO.getTheaterIdx());
    }

    private void deleteScreenInfo(int idx) {
        String message = "정말로 삭제하시겠습니까?\n[Y] 삭제 [N] 취소";
        String yesNo = ScannerUtil.nextLine(SCANNER, message);
        if (yesNo.equalsIgnoreCase("Y")) {
            screenInfoController.delete(idx);
            System.out.println("정상적으로 삭제되었습니다.");
        } else {
            System.out.println("취소되었습니다.");
        }
    }

    private void registerScreenInfo(int theaterIdx) {
        ArrayList<MovieDTO> movieList = movieController.selectAll();
        System.out.println("+-------------------------------------------+");
        if(movieList.isEmpty()) {
            System.out.println(" * 상영 정보를 등록할 수 있는 영화가 없습니다.");
            System.out.println("+-------------------------------------------+");
        } else {
            for (MovieDTO m : movieList) {
                System.out.println(" * [" + m.getIdx() + "] " + m.getTitle());
                System.out.println("+-------------------------------------------+");
            }
        }

        String message = "상영 정보를 등록할 영화 번호를 입력해주세요.\n[입력] 상영 정보 등록 [0] 뒤로 가기";
        int userChoice = ScannerUtil.nextInt(SCANNER, message);
        while (userChoice != 0 && movieController.selectByIdx(userChoice) == null) {
            System.out.println("존재하지 않는 영화 번호입니다.");
            userChoice = ScannerUtil.nextInt(SCANNER, message);
        }
        if (userChoice != 0) {
            String regex = "([0-1][0-9]|2[0-4]):([0-5][0-9])";

            message = "영화 시작 시간을 입력해주세요.(HH:mm)";
            String startTime = ScannerUtil.nextLine(SCANNER, message);
            while (!startTime.matches(regex)) {
                System.out.println("잘못된 입력입니다. 형식을 확인해주세요.");
                startTime = ScannerUtil.nextLine(SCANNER, message);
            }
            message = "영화 종료 시간을 입력해주세요. (HH:mm)";
            String endTime = ScannerUtil.nextLine(SCANNER, message);
            while (!endTime.matches(regex)) {
                System.out.println("잘못된 입력입니다. 형식을 확인해주세요.");
                endTime = ScannerUtil.nextLine(SCANNER, message);
            }
            String playtime = startTime + " ~ " + endTime;

            screenInfoController.insert(new ScreenInfoDTO(userChoice, theaterIdx, playtime));
            System.out.println("상영 정보가 정상적으로 등록되었습니다.");
            showScreenInfoListForTheater(theaterIdx);
        }
    }
}
