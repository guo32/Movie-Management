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
        GradeRequestViewer gradeRequestViewer = new GradeRequestViewer(scanner);

        userViewer.setMovieViewer(movieViewer);
        userViewer.setTheaterView(theaterView);
        userViewer.setGradeRequestViewer(gradeRequestViewer);

        movieViewer.setRatingViewer(ratingViewer);

        theaterView.setScreenInfoViewer(screenInfoViewer);

        screenInfoViewer.setTheaterView(theaterView);

        ratingViewer.setUserViewer(userViewer);

        gradeRequestViewer.setUserViewer(userViewer);

        userViewer.showMenu();
    }
}
