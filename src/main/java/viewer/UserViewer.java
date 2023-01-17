package viewer;

import controller.GradeRequestController;
import controller.RatingController;
import controller.UserController;
import model.GradeRequestDTO;
import model.RatingDTO;
import model.UserDTO;
import util.ScannerUtil;

import java.util.ArrayList;
import java.util.Scanner;

public class UserViewer {
    // 사용자 등급 상수
    private final int GENERAL = 1;
    private final int REVIEWER = 2;
    private final int MANAGER = 3;
    private final Scanner SCANNER;

    private UserController userController;
    private GradeRequestController gradeRequestController;
    private UserDTO login;

    private MovieViewer movieViewer;
    private TheaterView theaterView;
    private GradeRequestViewer gradeRequestViewer;

    public UserViewer(Scanner scanner) {
        SCANNER = scanner;
        userController = new UserController();
        userController.insert(new UserDTO("admin", "1111", "admin", MANAGER));
    }

    public void setMovieViewer(MovieViewer movieViewer) {
        this.movieViewer = movieViewer;
    }

    public void setTheaterView(TheaterView theaterView) {
        this.theaterView = theaterView;
    }

    public void setGradeRequestViewer(GradeRequestViewer gradeRequestViewer) {
        this.gradeRequestViewer = gradeRequestViewer;
    }

    public void setGradeRequestController(GradeRequestController gradeRequestController) {
        this.gradeRequestController = gradeRequestController;
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

        UserDTO newUser = new UserDTO(username, password, nickname, GENERAL);
        userController.insert(newUser);
    }

    private void showSystemMenu() {
        String message;
        int userChoice = 0;
        if (login.getGrade() == MANAGER) {
            message = "[1] 영화 [2] 극장 [3] 회원정보 [4] 회원 관리 [0] 로그아웃";
            userChoice = ScannerUtil.nextInt(SCANNER, message, 0, 4);
        } else {
            message = "[1] 영화 [2] 극장 [3] 회원정보 [0] 로그아웃";
            userChoice = ScannerUtil.nextInt(SCANNER, message, 0, 3);
        }

        if (userChoice == 1) {
            // 영화 목록
            movieViewer.setUserController(userController);
            movieViewer.showMenu(login);
            showSystemMenu();
        } else if (userChoice == 2) {
            // 극장 목록
            theaterView.setMovieController(movieViewer.getMovieController());
            theaterView.setLogin(login);
            theaterView.showMenu();

            showSystemMenu();
        } else if (userChoice == 3) {
            // 회원 정보 메뉴
            showUserMenu();
        } else if (userChoice == 4) {
            // 회원 관리 메뉴
            showManageUserMenu();
        } else if (userChoice == 0) {
            System.out.println("정상적으로 로그아웃되었습니다.");
            login = null;
        }
    }

    private void showUserMenu() {
        printUserInfo(login);
        String message;
        int userChoice = 0;
        if (login.getGrade() != MANAGER) {
            message = "[1] 수정 [2] 탈퇴 [3] 등업 [0] 뒤로 가기";
            userChoice = ScannerUtil.nextInt(SCANNER, message, 0, 3);
        } else {
            message = "[1] 수정 [2] 탈퇴 [0] 뒤로 가기";
            userChoice = ScannerUtil.nextInt(SCANNER, message, 0, 2);
        }

        if (userChoice == 1) {
            updateUser();
        } else if (userChoice == 2) {
            deleteUser();
        } else if (userChoice == 3) {
            // 등업
            gradeRequestViewer.setLogin(login);
            gradeRequestViewer.setUserController(userController);
            gradeRequestViewer.showMenu();
            showUserMenu();
        } else if (userChoice == 0) {
            showSystemMenu();
        }
    }

    private void showManageUserMenu() {
        String message = "[1] 회원 목록 [2] 등업 관리 [0] 뒤로 가기";
        int userChoice = ScannerUtil.nextInt(SCANNER, message, 0, 2);
        if (userChoice == 1) {
            printUserList();
        } else if (userChoice == 2) {
            gradeRequestViewer.setLogin(login);
            gradeRequestViewer.setUserController(userController);
            gradeRequestViewer.showMenu();
            showManageUserMenu();
        } else {
            showSystemMenu();
        }
    }

    private void printUserList() {
        ArrayList<UserDTO> list = userController.selectAll();
        System.out.println("+---------------------+");
        for (UserDTO u : list) {
            System.out.println(" [" + u.getIdx() + "] " + u.getUsername());
            System.out.println("+---------------------+");
        }
        String message = "상세 보기할 회원의 번호를 입력해주세요.\n[입력] 상세 보기 [0] 뒤로 가기";
        int userChoice = ScannerUtil.nextInt(SCANNER, message);
        while (userChoice != 0 && userController.selectByIdx(userChoice) == null) {
            System.out.println("존재하지 않는 회원 번호입니다.");
            userChoice = ScannerUtil.nextInt(SCANNER, message);
        }
        if (userChoice != 0) {
            printUserInfo(userController.selectByIdx(userChoice));
            message = "[1] 등급 변경 [0] 뒤로 가기";
            int select = ScannerUtil.nextInt(SCANNER, message, 0, 1);
            if (select == 1) {
                updateUserGrade(userChoice);
                showManageUserMenu();
            } else {
                showManageUserMenu();
            }
        } else {
            showManageUserMenu();
        }

    }

    private void printUserInfo(UserDTO userDTO) {
        System.out.println("+---------------------+");
        System.out.println(" * 아이디: " + userDTO.getUsername());
        System.out.println(" * 닉네임: " + userDTO.getNickname());
        if (userDTO.getGrade() == GENERAL) {
            System.out.println(" * 등급: 일반");
        } else if (userDTO.getGrade() == REVIEWER) {
            System.out.println(" * 등급: 전문 평론가");
        } else if (userDTO.getGrade() == MANAGER) {
            System.out.println(" * 등급: 관리자");
        }
        System.out.println("+---------------------+");
    }

    private void updateUserGrade(int idx) {
        String message = "등급을 입력해주세요.\n[1] 일반 [2] 전문 평론가 [3] 관리자";
        int userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 3);
        UserDTO userDTO = userController.selectByIdx(idx);
        userDTO.setGrade(userChoice);

        userController.update(userDTO);
        System.out.println("성공적으로 변경되었습니다.");
        printUserInfo(userDTO);
    }

    private void updateUser() {
        UserDTO userDTO = new UserDTO(login);
        String message = "새 비밀번호를 입력해주세요. (4~10자)";
        String newPassword = ScannerUtil.nextLine(SCANNER, message);
        while (!newPassword.matches("\\w{4,10}")) {
            System.out.println("비밀번호는 4자 이상 10자 이하로 입력할 수 있습니다.");
            newPassword = ScannerUtil.nextLine(SCANNER, message);
        }
        userDTO.setPassword(newPassword);

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
            // 기본 관리자 탈퇴 방지
            if (login.getUsername().equals("admin")) {
                System.out.println("기본 관리자는 탈퇴할 수 없습니다.");
                showUserMenu();
            } else {
                // 탈퇴한 회원의 평점 처리
                RatingController ratingController = new RatingController();
                ArrayList<RatingDTO> temp = ratingController.selectAll();
                if (temp != null) {
                    for (RatingDTO r : temp) {
                        if (r.getRegisterIdx() == login.getIdx()) {
                            ratingController.delete(r.getIdx());
                        }
                    }
                }

                // 탈퇴한 회원의 등업 신청 목록 처리
                if (gradeRequestController != null) {
                    GradeRequestDTO gradeRequestDTO = gradeRequestController.selectByUserIdx(login.getIdx());
                    if (gradeRequestDTO != null) {
                        gradeRequestController.delete(gradeRequestDTO.getIdx());
                    }
                }

                userController.delete(login.getIdx());

                System.out.println("탈퇴되었습니다.");
                login = null;
            }
        } else {
            System.out.println("취소되었습니다.");
            showUserMenu();
        }
    }
}