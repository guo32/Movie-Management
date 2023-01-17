package viewer;

import controller.GradeRequestController;
import controller.UserController;
import model.GradeRequestDTO;
import model.UserDTO;
import util.ScannerUtil;

import java.util.ArrayList;
import java.util.Scanner;

public class GradeRequestViewer {
    // 사용자 등급 상수
    private final int REVIEWER = 2;
    private final int MANAGER = 3;
    private final Scanner SCANNER;
    private UserViewer userViewer;
    private GradeRequestController gradeRequestController;
    private UserController userController;
    private UserDTO login;

    public GradeRequestViewer(Scanner scanner) {
        SCANNER = scanner;
        gradeRequestController = new GradeRequestController();
    }

    public void setUserController(UserController userController) {
        this.userController = userController;
    }

    public void setLogin(UserDTO login) {
        this.login = login;
    }

    public void setUserViewer(UserViewer userViewer) {
        this.userViewer = userViewer;
    }

    public void showMenu() {
        userViewer.setGradeRequestController(gradeRequestController);
        String message;
        int userChoice;
        if (login.getGrade() == MANAGER) {
            message = "[2] 신청 현황 [3] 뒤로 가기";
            userChoice = ScannerUtil.nextInt(SCANNER, message, 2, 3);
        } else {
            message = "[1] 등업 신청 [2] 신청 현황 [3] 뒤로 가기";
            userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 3);
        }

        if (userChoice == 1) {
            registerGradeRequest();
        } else if (userChoice == 2) {
            if (login.getGrade() == MANAGER) {
                printGradeRequestList();
            } else {
                printGradeRequest();
            }
        }
    }

    private void registerGradeRequest() {
        if (gradeRequestController.selectByUserIdx(login.getIdx()) != null) {
            System.out.println("이미 등업 신청을 하셨습니다.");
        } else {
            String message = "등급을 선택해주세요.\n[2] 전문 평론가 [3] 관리자";
            int userChoice = ScannerUtil.nextInt(SCANNER, message, 2, 3);
            while (login.getGrade() == userChoice) {
                System.out.println("같은 등급은 신청할 수 없습니다.");
                userChoice = ScannerUtil.nextInt(SCANNER, message, 2, 3);
            }
            GradeRequestDTO newGradeRequest = new GradeRequestDTO(login.getIdx(), userChoice);
            gradeRequestController.insert(newGradeRequest);
            System.out.println("신청되었습니다.");
        }
        showMenu();
    }

    private void printGradeRequestList() {
        ArrayList<GradeRequestDTO> list = gradeRequestController.selectAll();
        System.out.println("+-----------------------------------+");
        if (list.isEmpty()) {
            System.out.println(" * 신청 내역이 없습니다.");
            System.out.println("+-----------------------------------+");
        } else {
            for (GradeRequestDTO g : list) {
                String requestInfo = " [" + g.getIdx() + "] " + userController.selectByIdx(g.getUserIdx()).getNickname() + " - ";
                if (g.getGrade() == REVIEWER) {
                    requestInfo += "전문 평론가";
                } else if (g.getGrade() == MANAGER) {
                    requestInfo += "관리자";
                }
                requestInfo += " 등업 신청";
                System.out.println(requestInfo);
                System.out.println("+-----------------------------------+");
            }
        }
        String message = "수락할 등업 신청의 번호를 입력해주세요.\n[입력] 수락 [0] 뒤로 가기";
        int userChoice = ScannerUtil.nextInt(SCANNER, message);
        GradeRequestDTO gradeRequestDTO = gradeRequestController.selectByIdx(userChoice);
        while (userChoice != 0 && gradeRequestDTO == null) {
            System.out.println("유효하지 않은 번호입니다.");
            userChoice = ScannerUtil.nextInt(SCANNER, message);
            gradeRequestDTO = gradeRequestController.selectByIdx(userChoice);
        }
        if (userChoice != 0) {
            UserDTO userDTO = userController.selectByIdx(gradeRequestDTO.getUserIdx());
            message = userDTO.getNickname() + "님을 ";
            if (gradeRequestDTO.getGrade() == REVIEWER) {
                message += "전문 평론가로 ";
            } else if (gradeRequestDTO.getGrade() == MANAGER) {
                message += "관리자로 ";
            }
            message += "변경하시겠습니까?\n[Y] 예 [N] 취소";
            String yesNo = ScannerUtil.nextLine(SCANNER, message);

            if (yesNo.equalsIgnoreCase("Y")) {
                userDTO.setGrade(gradeRequestDTO.getGrade());
                userController.update(userDTO);
                gradeRequestController.delete(userChoice);
                System.out.println("정상적으로 처리되었습니다.");
                printGradeRequestList();
            } else {
                System.out.println("취소되었습니다.");
                printGradeRequestList();
            }
        }
    }

    private void printGradeRequest() {
        GradeRequestDTO gradeRequestDTO = gradeRequestController.selectByUserIdx(login.getIdx());
        System.out.println("+===================================+");
        if (gradeRequestDTO == null) {
            System.out.println(" * 신청 내역이 없습니다.");
            System.out.println("+===================================+");
        } else {
            String requestInfo = " * " + login.getNickname() + ": ";
            if (gradeRequestDTO.getGrade() == REVIEWER) {
                requestInfo += "전문 평론가";
            } else if (gradeRequestDTO.getGrade() == MANAGER) {
                requestInfo += "관리자";
            }
            requestInfo += " 등업 신청";
            System.out.println(requestInfo);
            System.out.println("+===================================+");
        }
        if (gradeRequestDTO != null) {
            String message = "[1] 신청 내역 수정 [2] 신청 내역 삭제 [0] 뒤로 가기";
            int userChoice = ScannerUtil.nextInt(SCANNER, message, 0, 2);
            if (userChoice == 1) {
                updateGradeRequest(gradeRequestDTO.getIdx());
            } else if (userChoice == 2) {
                deleteGradeRequest(gradeRequestDTO.getIdx());
            } else {
                showMenu();
            }
        } else {
            showMenu();
        }
    }

    private void updateGradeRequest(int idx) {
        GradeRequestDTO gradeRequestDTO = gradeRequestController.selectByIdx(idx);
        String message = "등급을 선택해주세요.\n[2] 전문 평론가 [3] 관리자";
        int userChoice = ScannerUtil.nextInt(SCANNER, message, 2, 3);
        while (login.getGrade() == userChoice) {
            System.out.println("같은 등급은 신청할 수 없습니다.");
            userChoice = ScannerUtil.nextInt(SCANNER, message, 2, 3);
        }
        gradeRequestDTO.setGrade(userChoice);
        gradeRequestController.update(gradeRequestDTO);
        System.out.println("수정되었습니다.");

        printGradeRequest();
    }

    private void deleteGradeRequest(int idx) {
        String message = "신청 내역을 삭제하시겠습니까?\n[Y] 삭제 [N] 취소";
        String yesNo = ScannerUtil.nextLine(SCANNER, message);
        if (yesNo.equalsIgnoreCase("Y")) {
            gradeRequestController.delete(idx);
            System.out.println("정상적으로 삭제되었습니다.");
            printGradeRequest();
        } else {
            System.out.println("취소되었습니다.");
            printGradeRequest();
        }
    }
}