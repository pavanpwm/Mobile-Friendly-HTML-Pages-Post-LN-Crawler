package application;
	
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;



public class Main extends Application {
	
	

    @FXML
    private Label status;
    
    @FXML
    private Pane formPane;
    static String inital = "";
    static String downloadFolder = "";
	
	
	@Override
	public void start(Stage primaryStage) throws Exception{
		Stage newStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("Form.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		newStage.setScene(scene);
		newStage.setResizable(false);
		newStage.sizeToScene();			// to solve the extra margins caused by setResizable(false) method
		newStage.setTitle("Novel Mobilifier");
		newStage.show();
		newStage.setOnCloseRequest(w->{
			Platform.exit();
			System.exit(0);
		});
	}
	public static void main(String[] args) {
		launch(args);
	}
	public void stop(Stage primaryStage) throws Exception {
		Platform.exit();
	}
	
	
	
	public void onSubmit(ActionEvent event) throws Exception {
		status.setText("Please wait....");
		try {
			DirectoryChooser dir_chooser = new DirectoryChooser();
			dir_chooser.setTitle("Select the folder with HTML files");
	        File loc = dir_chooser.showDialog((Stage) status.getScene().getWindow());
	        downloadFolder = loc.getParent() + "/Mobile Friendly " + loc.getName() + "/";
	        startEditing(loc.getAbsolutePath());
	        status.setText("Done! Your files are saved in "+ downloadFolder +" folder");
	        Desktop.getDesktop().open(new File(downloadFolder));
		}catch (Exception e) {
		    status.setText("Something went wrong!");
	   }     
	}
	
	
	

	
	public void startEditing(String path) throws Exception {
		new File(downloadFolder).mkdirs();
		if (inital.isEmpty()) {
			getInitailString();
		}
		if(!inital.isEmpty()) {
			List<File> list = Arrays.asList(new File(path).listFiles());
			System.out.println(list);
			for (int i = 0; i < list.size(); i++) {
				BufferedReader br = Files.newBufferedReader(Paths.get(list.get(i).getPath()));
				String eachLine;
				String content = "";		
				while ((eachLine = br.readLine()) != null  ) {
					content += eachLine + System.lineSeparator();
				}
				br.close();
				content = content.substring(content.indexOf("<body>") + 6);
				content =  inital + content;
				createNewFile(list.get(i).getName(), content);
			}
		}
	}
	
	
	
	public static void createNewFile(String name, String content) throws Exception{
		File file = new File(downloadFolder + name);
        file.createNewFile();
        BufferedWriter bb = Files.newBufferedWriter(Paths.get(file.getPath()));
        bb.write(content);
        bb.close();
        
	}
	
	
	public static void getInitailString() throws Exception {
		BufferedReader br =  Files.newBufferedReader(Paths.get("src/application/initial.txt"));
		String eachLine;
		while ((eachLine = br.readLine()) != null  ) {
			inital += eachLine + System.lineSeparator();
		}
		br.close();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
