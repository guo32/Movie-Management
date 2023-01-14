package viewer;

import controller.UserController;
import model.UserDTO;
import util.ScannerUtil;

import java.util.Scanner;

public class UserViewer {
    private final Scanner SCANNER;

    private UserController userController;
    private UserDTO login;

    public UserViewer(Scanner scanner) {
        SCANNER = scanner;
        userController = new UserController();
    }

    public void showMenu() {
        String message = "[1] 로그인 [2] 회원가입 [3] 시스템 종료";

        while (true) {
            int userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 3);

            if (userChoice == 1) { // 로그인
                userLogin();
            } else if (userChoice == 2) { // 회원가입
                userRegister();
            } else if (userChoice == 3) { // 종료
                System.out.println("시스템을 종료합니다.");
                break;
            }
        }
    }

    private void userLogin() {
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
                showUserMenu();
                break;
            }
        }
    }

    private void userRegister() {
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

    private void showUserMenu() {
        String message = "[1] 영화 [2] 극장 [3] 회원정보수정 [4] 로그아웃";
    }
}