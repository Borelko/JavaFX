package com.example.testoviyfx;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class Controller {
    private Stage stage;
    private Scene scene;
    private Parent root;
    File[] files = new File("C:/Work/school/images").listFiles();
    int num=0;
    @FXML
    private ProgressBar AugmentationProgressBar;
    @FXML
    private Label AugmentationLabelOfPercents;
    @FXML
    private Label AugmentationPath;
    @FXML
    private ImageView imageView;
    @FXML
    private Button StartAug;
    @FXML
    CheckBox FlipCheckbox;
    @FXML
    CheckBox YUVCheckbox;
    @FXML
    CheckBox JPEGCheckbox;
    @FXML
    CheckBox HUECheckbox;
    private String DIR = null;

    //BigDecimal progress = new BigDecimal(String.format("%.2f", 0));
    @FXML
    protected void EXITButtonClick() {
        System.exit(1);
    }

    @FXML
    protected void HomeButtonClick(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Home-page.fxml")));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene=new Scene(root);
        stage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        stage.show();
    }

    @FXML
    protected void WatchLabelsButtonClick(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Interface2.fxml")));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        stage.show();
    }
    @FXML
    protected void AugmentationButtonClick(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Augment.fxml")));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        stage.show();

    }
    @FXML
    protected void NextImgButtonClick() {
        if (num<files.length) {
            num++;
            Image img = new Image(String.valueOf(files[num]));
            imageView.setImage(img);
        }
    }
    @FXML
    protected void PrevImgButtonClick(){
        if (num>0) {
            num--;
            Image img = new Image(String.valueOf(files[num]));
            imageView.setImage(img);
        }
    }
    @FXML
    protected void StartAugmentation(){
        StartAug.setDisable(true);
        HUECheckbox.setDisable(true);
        JPEGCheckbox.setDisable(true);
        YUVCheckbox.setDisable(true);
        FlipCheckbox.setDisable(true);
        AugmentationProgressBar.setVisible(true);
        System.out.println(Thread.currentThread().getName());
        System.out.println(DIR);
        NewThread NewTOfProgressBar = new NewThread(
                "ProgressBarThread",
                AugmentationProgressBar,
                AugmentationLabelOfPercents,
                DIR,
                HUECheckbox,
                JPEGCheckbox,
                YUVCheckbox,
                FlipCheckbox
                );
        NewTOfProgressBar.t.start();

        //System.out.println("Application Thread Complete");

    }
    @FXML
            protected void ChooseDir() {
        DirectoryChooser fil_chooser = new DirectoryChooser();
                        DIR = String.valueOf(fil_chooser.showDialog(stage));
        System.out.println(DIR);

        if (DIR != null && !DIR.equals("null")) {
            StartAug.setDisable(false);
            AugmentationPath.setText("Selected Directory:" + "\n" + DIR);
            HUECheckbox.setDisable(false);
            JPEGCheckbox.setDisable(false);
            YUVCheckbox.setDisable(false);
            FlipCheckbox.setDisable(false);
        }
        else
            AugmentationPath.setText("Selected Directory:" + "\n" + "No selected directory");
    }
}

class NewThread implements Runnable {
    @FXML
            private final ProgressBar AugmentationProgressBar;
    @FXML
            private final Label AugmentationLabelOfPercents;
    @FXML
    CheckBox FlipCheckbox;
    @FXML
    CheckBox YUVCheckbox;
    @FXML
    CheckBox JPEGCheckbox;
    @FXML
    CheckBox HUECheckbox;
    final String DIR;
    File[] Imgfiles;
    File[] Txtfiles;
    String Name;
    Thread t;
    double progress=0;
    int allCountOfFiles;
    BufferedImage img;
    NewThread(
            String ThreadName,
              ProgressBar AugmentationProgressBar,
              Label AugmentationLabelOfPercents,
              String DIR,
            CheckBox HUECheckbox,
            CheckBox JPEGCheckbox,
            CheckBox YUVCheckbox,
            CheckBox FlipCheckbox
    ){
        Name = ThreadName;
        t = new Thread(this, Name);
        this.AugmentationProgressBar = AugmentationProgressBar;
        this.AugmentationLabelOfPercents = AugmentationLabelOfPercents;
        this.DIR= DIR;
        this.HUECheckbox = HUECheckbox;
        this.JPEGCheckbox = JPEGCheckbox;
        this.YUVCheckbox = YUVCheckbox;
        this.FlipCheckbox = FlipCheckbox;

    }
    public void run(){
        Imgfiles = new File(DIR + "/images").listFiles();
        Txtfiles = new File(DIR + "/labels").listFiles();
            //System.out.println("I', in Thread" + t);
        allCountOfFiles = Imgfiles.length;
        Path source, destination;

        for (int i = 0; i < allCountOfFiles; i++) {
            File IMG_ORIGINAL = Imgfiles [i];
            File TXT_ORIGINAL = Txtfiles [i];
            if (!IMG_ORIGINAL.exists()) continue;
            if (!TXT_ORIGINAL.exists()) continue;

            String IMG_COMPRESSED = DIR + "/images/" + Imgfiles [i].getName().replace(".jpg","") + "_Compressed";
            String IMG_WITHNOISE  = DIR + "/images/" + Imgfiles [i].getName().replace(".jpg","") + "_WithNoise";
            String IMG_HUE  = DIR + "/images/" + Imgfiles [i].getName().replace(".jpg","") + "_ChangedHue";
            String IMG_FLIPPED = DIR + "/images/" + Imgfiles [i].getName().replace(".jpg","") + "_Flipped";


            source = Paths.get(String.valueOf(TXT_ORIGINAL));

            File f_compressed = new File(IMG_COMPRESSED + ".jpg");
            File f_WithNoise = new File(IMG_WITHNOISE + ".jpg");
            File f_changedHue = new File(IMG_HUE + ".jpg");
            File f_flipped = new File(IMG_FLIPPED + ".jpg");
            try {
                img = ImageIO.read(IMG_ORIGINAL);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (YUVCheckbox.isSelected()) {
                Noise.addNoise(img, 2);
                try {
                    ImageIO.write(img, "jpg", f_WithNoise);
                    destination = Paths.get(DIR + "/labels/" + Txtfiles[i].getName().replace(".txt","") + "_WithNoise.txt");
                        Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            if (HUECheckbox.isSelected()) {
                HueSaturationBrightness.changeHue(img);
                try {
                    ImageIO.write(img, "jpg", f_changedHue);
                    destination = Paths.get(DIR + "/labels/" + Txtfiles[i].getName().replace(".txt","") + "_ChangedHue.txt");
                        Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (FlipCheckbox.isSelected()) {
                Flipping.flipImage(img);
                try {
                    ImageIO.write(img, "jpg", f_flipped);
                    Flipping.writeFlippedAnn(
                            Txtfiles[i],
                            DIR + "/labels/" + Txtfiles[i].getName().replace(".txt","") + "_Flipped.txt"
                    );
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (JPEGCheckbox.isSelected()) {
                try {
                    Compress.compress(IMG_ORIGINAL, f_compressed, 0.5f);
                    destination = Paths.get(DIR + "/labels/" + Txtfiles[i].getName().replace(".txt","") + "_Compressed.txt");
                    Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            progress = 100.0*(i+1)/allCountOfFiles;
            Platform.runLater(()->
                    AugmentationLabelOfPercents.setText(
                            String.format("%.2f", progress)+"%")
            );

            AugmentationProgressBar.setProgress(progress/100.0);

            if ((i+1)>=allCountOfFiles)
                Platform.runLater(()->
                        AugmentationLabelOfPercents.setText(
                                "Augmentation Complete"
                        ));
        }
        }
}