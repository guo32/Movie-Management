package viewer;

import controller.UserController;
import model.UserDTO;
import util.ScannerUtil;

import java.util.Scanner;

public class UserViewer {
    private final Scanner SCANNER;

    private UserController userController;
    private UserDTO login;

    private MovieViewer movieViewer;
    private TheaterView theaterView;

    public UserViewer(Scanner scanner) {
        SCANNER = scanner;
        userController = new UserController();
    }

    public void setMovieViewer(MovieViewer movieViewer) {
        this.movieViewer = movieViewer;
    }

    public void setTheaterView(TheaterView theaterView) {
        this.theaterView = theaterView;
    }

    public void showMenu() {
        String message = "[1] 로그인 [2] 회원가입 [3] 시스템 종료";

        while (true) {
            int userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 3);

            if (userChoice == 1) { // 로그인
                loginUser();
            } else if (userChoice == 2) { // 회원가입
                registUser();
            } else if (userChoice == 3) { // 종료
                System.out.println("시스템을 종료합니다.");
                break;
            }
        }
    }

    private void loginUser() {
        while (true) {
            String message = "아이디를 입력해주세요.";
            String username = ScannerUtil.nextLine(SCANNER, message);

            message = "비밀번호를 입력해주세요.";
            String password = ScannerUtil.nextLine(SCANNER, message);

            login = userController.auth(username, password);
            if(login == null) {
                System.out.println("아이디 또는 비밀번호가 잘못되었습니다.");
                message = "[1] 다시 입력 [2] 시작 화면으로";
                int userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 2);
                if (userChoice == 2) {
                    break;
                }
            } else {
                showSystemMenu();
                break;
            }
        }
    }

    private void registUser() {
        String message = "사용할 아이디를 입력해주세요. (4~10자)";
        String username = ScannerUtil.nextLine(SCANNER, message);
        String exp = "\\w{4,10}";

        // 회원 아이디가 중복되는 경우 or 회원 아이디가 4자 미만 10자 초과인 경우
        while (userController.duplicateUsername(username) || !username.matches(exp)) {
            System.out.println("사용할 수 없는 아이디입니다.");
            username = ScannerUtil.nextLine(SCANNER, message);
        }

        message = "비밀번호를 입력해주세요. (4~10자)";
        String password = ScannerUtil.nextLine(SCANNER, message);
        while (!password.matches(exp)) {
            System.out.println("비밀번호는 4자 이상 10자 이하로 입력할 수 있습니다.");
            password = ScannerUtil.nextLine(SCANNER, message);
        }

        message = "사용할 닉네임을 입력해주세요.";
        String nickname = ScannerUtil.nextLine(SCANNER, message);

        UserDTO newUser = new UserDTO(username, password, nickname);
        userController.insert(newUser);
    }

    private void showSystemMenu() {
        String message = "[1] 영화 [2] 극장 [3] 회원정보 [4] 로그아웃";
        int userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 4);
        if (userChoice == 1) {
            // 영화 목록
            movieViewer.showMenu();
            showSystemMenu();
        } else if (userChoice == 2) {
            // 극장 목록
            theaterView.showMenu();
            showSystemMenu();
        } else if (userChoice == 3) {
            // 회원 정보 메뉴
            showUserMenu();
        } else if (userChoice == 4) {
            System.out.println("정상적으로 로그아웃되었습니다.");
            login = null;
        }
    }

    private void showUserMenu() {
        printUserInfo();
        String message = "[1] 수정 [2] 탈퇴 [3] 뒤로 가기";
        int userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 3);
        if (userChoice == 1) {
            updateUser();
        } else if (userChoice == 2) {
            deleteUser();
        } else if (userChoice == 3) {
            showSystemMenu();
        }
    }

    private void printUserInfo() {
        System.out.println("+---------------------+");
        System.out.println(" * 아이디: " + login.getUsername());
        System.out.println(" * 닉네임: " + login.getNickname());
        System.out.println("+---------------------+");
    }

    private void updateUser() {
        UserDTO userDTO = new UserDTO(login);
        String message = "새 비밀번호를 입력해주세요.";
        userDTO.setPassword(ScannerUtil.nextLine(SCANNER, message));

        message = "새 닉네임을 입력해주세요.";
        userDTO.setNickname(ScannerUtil.nextLine(SCANNER, message));

        message = "기존 비밀번호를 입력해주세요.";
        String password = ScannerUtil.nextLine(SCANNER, message);
        if (!password.equals(login.getPassword())) {
            System.out.println("회원 정보 변경에 실패했습니다.");
        } else {
            userController.update(userDTO);
            System.out.println("회원 정보를 수정했습니다.");
            login = new UserDTO(userDTO);
        }
        showUserMenu();
    }

    private void deleteUser() {
        String message = "정말로 탈퇴하시겠습니까?\n[Y] 예 [N] 아니오";
        String yesNo = ScannerUtil.nextLine(SCANNER, message);

        if (yesNo.equalsIgnoreCase("Y")) {
            userController.delete(login.getIdx());
            System.out.println("탈퇴되었습니다.");
            login = null;
        } else {
            System.out.println("취소되었습니다.");
            showUserMenu();
        }
    }
}