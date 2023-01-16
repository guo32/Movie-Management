package home;

import util.ScannerUtil;
import viewer.MovieViewer;
import viewer.TheaterView;
import viewer.UserViewer;

import java.util.Scanner;

public class MovieManagement {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserViewer userViewer = new UserViewer(scanner);
        MovieViewer movieViewer = new MovieViewer(scanner);
        TheaterView theaterView = new TheaterView(scanner);

        userViewer.setMovieViewer(movieViewer);
        userViewer.setTheaterView(theaterView);
        userViewer.showMenu();
    }
}
