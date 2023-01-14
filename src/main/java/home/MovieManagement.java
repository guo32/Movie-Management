package home;

import util.ScannerUtil;

import java.util.Scanner;

public class MovieManagement {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String message = "테스트 중입니다.";
        int data = ScannerUtil.nextInt(scanner, message, 1, 3);
        System.out.println(data);
    }
}
