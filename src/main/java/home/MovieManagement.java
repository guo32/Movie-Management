package home;

import util.ScannerUtil;
import viewer.UserViewer;

import java.util.Scanner;

public class MovieManagement {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserViewer userViewer = new UserViewer(scanner);
        userViewer.showMenu();
    }
}
