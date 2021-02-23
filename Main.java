package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


public class Main extends Application {
    List<File> SelectedFiles = new ArrayList<>();
    @Override
    public void start(Stage primaryStage){

        primaryStage.setTitle("File Transfer Client");

        GridPane gp = new GridPane();

        Label IPAddress = new Label("IP Address");
        GridPane.setConstraints(IPAddress,0,0);

        TextField IP = new TextField();
        GridPane.setConstraints(IP,1,0);

        Button SelectFiles = new Button("Select Files");
        GridPane.setConstraints(SelectFiles, 1,2);
        SelectFiles.setOnAction(event->{
            FileChooser fc = new FileChooser();
           SelectedFiles =  fc.showOpenMultipleDialog(primaryStage);
        });


        Button SendFiles = new Button("Send Files");
        GridPane.setConstraints(SendFiles, 2,2);
        SendFiles.setOnAction(event->{
            try {
                Socket ss = new Socket(IP.getText(), 8080);
                for(int i = 0; i <= SelectedFiles.size()-1; i ++) {
                    byte[] byteArrayOfCurrentFile = Files.readAllBytes(SelectedFiles.get(i).toPath());
                    long fileSize = SelectedFiles.get(i).length();
                    String fileName = SelectedFiles.get(i).getName();
                    DataOutputStream dos = new DataOutputStream(ss.getOutputStream());
                    PrintWriter outRK = new PrintWriter(ss.getOutputStream(), true);
                    outRK.println(fileSize);

                    dos.write(byteArrayOfCurrentFile);
                    PrintWriter outFN = new PrintWriter(ss.getOutputStream(), true);
                    outFN.println(fileName);
                    outFN.println(fileName);
                    if(i == SelectedFiles.size()-1){
                        PrintWriter outE = new PrintWriter(ss.getOutputStream(), true);
                        outE.println("End");
                    }

                }
            }catch(Exception e){
                e.printStackTrace();
            }
        });

        gp.getChildren().addAll(SendFiles, SelectFiles, IP, IPAddress);
        primaryStage.setScene(new Scene(gp, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
