package home;

import util.ScannerUtil;
import viewer.*;

import java.util.Scanner;

public class MovieManagement {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserViewer userViewer = new UserViewer(scanner);
        MovieViewer movieViewer = new MovieViewer(scanner);
        TheaterView theaterView = new TheaterView(scanner);
        RatingViewer ratingViewer = new RatingViewer(scanner);
        ScreenInfoViewer screenInfoViewer = new ScreenInfoViewer(scanner);

        userViewer.setMovieViewer(movieViewer);
        userViewer.setTheaterView(theaterView);

        movieViewer.setRatingViewer(ratingViewer);

        theaterView.setScreenInfoViewer(screenInfoViewer);

        screenInfoViewer.setTheaterView(theaterView);

        userViewer.showMenu();
    }
}
