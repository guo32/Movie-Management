package viewer;

import controller.MovieController;
import controller.ScreenInfoController;
import controller.TheaterController;
import model.MovieDTO;
import model.ScreenInfoDTO;
import model.TheaterDTO;
import model.UserDTO;
import util.ScannerUtil;

import java.util.ArrayList;
import java.util.Scanner;

public class TheaterView {
    private final Scanner SCANNER;
    private final int MANAGER = 3;
    private final TheaterController theaterController;
    private MovieController movieController;
    private ScreenInfoController screenInfoController;
    private ScreenInfoViewer screenInfoViewer;
    private UserDTO login;

    public TheaterView(Scanner scanner) {
        SCANNER = scanner;
        theaterController = new TheaterController();

        for (int i = 1; i < 5; i++) {
            theaterController.insert(new TheaterDTO(i + "번째 극장", "서울시 " + i + "동", "123-1234-1234"));
        }
    }

    public void setMovieController(MovieController movieController) {
        this.movieController = movieController;
    }

    public void setScreenInfoController(ScreenInfoController screenInfoController) {
        this.screenInfoController = screenInfoController;
    }

    public void setScreenInfoViewer(ScreenInfoViewer screenInfoViewer) {
        this.screenInfoViewer = screenInfoViewer;
    }

    public void setLogin(UserDTO login) {
        this.login = login;
    }

    public void showMenu() {
        String message;
        int userChoice;
        if (login.getGrade() == MANAGER) {
            message = "[1] 극장 목록 [2] 극장 등록 [0] 뒤로 가기";
            userChoice = ScannerUtil.nextInt(SCANNER, message, 0, 2);
        } else {
            message = "[1] 극장 목록 [0] 뒤로 가기";
            userChoice = ScannerUtil.nextInt(SCANNER, message, 0, 1);
        }
        if (userChoice == 1) {
            printTheaterList();
        } else if (userChoice == 2) {
            registerTheater();
        }
    }

    private void registerTheater() {
        String message = "등록할 극장의 이름을 입력해주세요.";
        String name = ScannerUtil.nextLine(SCANNER, message);

        message = "등록할 극장의 주소를 입력해주세요.";
        String location = ScannerUtil.nextLine(SCANNER, message);

        message = "등록할 극장의 전화번호를 입력해주세요. ('-'포함)";
        String telephone = ScannerUtil.nextLine(SCANNER, message);
        while (!telephone.matches("\\d{2,3}-\\d{3,4}-\\d{4}")) {
            System.out.println("잘못된 입력입니다. 형식을 확인해주세요.");
            telephone = ScannerUtil.nextLine(SCANNER, message);
        }

        theaterController.insert(new TheaterDTO(name, location, telephone));
        System.out.println("정상적으로 등록되었습니다.");
        showMenu();
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

        String message;
        int userChoice;
        if (login.getGrade() == MANAGER) {
            message = "[1] 수정 [2] 삭제 [3] 상영 중인 영화 [4] 뒤로 가기";
            userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 4);
        } else {
            message = "[3] 상영 중인 영화 [4] 뒤로 가기";
            userChoice = ScannerUtil.nextInt(SCANNER, message, 3, 4);
        }

        if (userChoice == 1) {
            // 극장 정보 수정
            updateTheater(idx);
        } else if (userChoice == 2) {
            // 극장 삭제
            deleteTheater(idx);
        } else if (userChoice == 3) {
            // 상영 중인 영화
            screenInfoViewer.setLogin(login);
            screenInfoViewer.setMovieController(movieController);
            screenInfoViewer.showScreenInfoListForTheater(idx);
            printTheaterInfo(idx);
        } else if (userChoice == 4) {
            printTheaterList();
        }
    }

    private void updateTheater(int idx) {
        TheaterDTO theaterDTO = theaterController.selectByIdx(idx);
        String message = "수정할 극장 이름을 입력해주세요.";
        theaterDTO.setName(ScannerUtil.nextLine(SCANNER, message));

        message = "수정할 극장 주소를 입력해주세요.";
        theaterDTO.setLocation(ScannerUtil.nextLine(SCANNER, message));

        message = "수정할 극장 전화번호를 입력해주세요.";
        String telephone = ScannerUtil.nextLine(SCANNER, message);
        while (!telephone.matches("\\d{2,3}-\\d{3,4}-\\d{4}")) {
            System.out.println("잘못된 입력입니다. 형식을 확인해주세요.");
            telephone = ScannerUtil.nextLine(SCANNER, message);
        }
        theaterDTO.setTelephone(telephone);

        theaterController.update(theaterDTO);
        System.out.println("정상적으로 수정되었습니다.");
        printTheaterInfo(idx);
    }

    private void deleteTheater(int idx) {
        String message = "정말로 삭제하시겠습니까?\n[Y] 삭제 [N] 취소";
        String yesNo = ScannerUtil.nextLine(SCANNER, message);
        if (yesNo.equalsIgnoreCase("Y")) {
            // 극장 삭제 시 상영 정보 삭제
            if (screenInfoController != null) {
                ArrayList<ScreenInfoDTO> screenInfoList = screenInfoController.selectByTheaterIdx(idx);
                if (!screenInfoList.isEmpty()) {
                    for (ScreenInfoDTO s : screenInfoList) {
                        screenInfoController.delete(s.getIdx());
                    }
                }
            }

            theaterController.delete(idx);
            System.out.println("정상적으로 삭제되었습니다.");
            printTheaterList();
        } else {
            System.out.println("취소되었습니다.");
            printTheaterInfo(idx);
        }
    }
}
