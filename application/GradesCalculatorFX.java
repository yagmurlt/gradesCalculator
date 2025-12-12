//imports for basic javaFX
package application;
import java.io.File;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.Scene;
//imports for layouts
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
//imports for contols
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
//imports for images
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
//imports for outputs
import java.io.FileWriter;
import java.io.BufferedWriter;

public class GradesCalculatorFX extends Application {
	// Declare components that require class scope
	MenuBar mbMain;
	Menu mnuFile, mnuHelp;
	MenuItem miFileQuit;
	MenuItem miHelpAbout, miHelpHowToUse;
	Label lblNumber;
	Label lblPath;
	Label lblWordCount;
	TextField txtfNumber;
	TextArea txtMain;
	Button btnChangePhoto, btnCheckGrades, btnExport, btnReset;
	//Profile Image
	ImageView imvProfilePhoto;
	Image img;
	Image defaultProfilePhoto;
	//Store chosen student number as a global variable
	String chosenNum = ""; 
	//keep track the last stundet number used. if it's the same, new module results appended
	String lastStudentNumber = "";
	//Your Details
	String StudentName = "your student name"; 
	String StudentNumber = "your student number"; 

	// Constructor() - instantiate 
	public GradesCalculatorFX() {
		//Instantiate components using keyword 'new'
		mbMain = new MenuBar();
		//Menu File and menu items
		mnuFile = new Menu("File");
		miFileQuit = new MenuItem("Quit");
		//Menu Help and menu items
		mnuHelp = new Menu ("Help");
		miHelpAbout = new MenuItem("About");
		miHelpHowToUse = new MenuItem("How to Use");
		//components
		lblNumber = new Label("Student Number:");
		lblPath=new Label("Default image");
		lblWordCount = new Label("Words: ");
		txtfNumber = new TextField();
		txtMain = new TextArea(); 
		btnChangePhoto = new Button("Change Photo");
		btnCheckGrades = new Button("Check Grades");
		btnExport = new Button("Export Results");
		btnReset = new Button("Reset Results");
		
		//text area to type in
		txtMain = new TextArea();
		txtMain.setDisable(true);
		//volatile actions go in try/catch!
		try {
			defaultProfilePhoto = new Image("Assets/profile.jpeg"); 
												  
			imvProfilePhoto = new ImageView(defaultProfilePhoto);
		}
		catch(Exception e) {
			System.err.println("Something went wrong loading initial image");
			e.printStackTrace();
		}
		
		//change buttons size
		//bind buttons izes to profile pfoto width and set min width for other buttons
		btnChangePhoto.maxWidthProperty().bind(imvProfilePhoto.fitWidthProperty());
		btnChangePhoto.minWidthProperty().bind(imvProfilePhoto.fitWidthProperty());
		btnCheckGrades.setMinWidth(40);
		btnExport.setMinWidth(40);
		btnReset.setMinWidth(40);
		
		//resize the TextArea
		txtMain.setPrefSize(400,500);
	} 

	// init () -Event handling
	@Override 
	public void init() {
		//handle events on the  miFileQuit - it closes javafx app 
		miFileQuit.setOnAction(event -> Platform.exit());
		
		//add functionality to miHelpHowToUse (pre-built dialog) (Alert)
		miHelpHowToUse.setOnAction(event -> showHowToUseDialog());
		
		//add functionality to miHelpAbout (pre-built dialog) (Alert)
		miHelpAbout.setOnAction(event -> showAboutDialog());
		
		//Handle events on check grades button (dialog)
		btnCheckGrades.setOnAction(event -> {
			if(txtfNumber.getText().matches("\\d{8,}")) {
				showCheckGradesDialog();
			}
			else {
				//use an alert for other than 8 or more digits input
				Alert myAlert = new Alert(AlertType.WARNING);
				//set the title for the alert
				myAlert.setTitle("Invalid Student Number");
				//customize header and content
				myAlert.setHeaderText("Student number must be at least 8 digits (numbers only).");
				myAlert.setContentText("Please enter a valid student number and try again.");
				//show the alert
				myAlert.showAndWait();
			}
		});
		
		//Handle events on the export button (save as txt)
		btnExport.setOnAction(event -> exportResults());
		
		// Handle events on the change photo button (system dialog)
		btnChangePhoto.setOnAction(event -> changePhoto());
		
		//handle events on the reset results button
		btnReset.setOnAction(event -> {
			//clear student number and text area
			txtfNumber.clear();
			txtMain.clear();
			//clear the profile photo with default photo which I setted
			imvProfilePhoto.setImage(defaultProfilePhoto);
		});
	}
	
	//show a built in dialog
	private void showAboutDialog() {
		//use an alert for the dialof
		Alert aboutAlert = new Alert(AlertType.INFORMATION);
		
		//set the title of the alert
		aboutAlert.setTitle("GradesCalculatorFX v1.0");
		
		//customize content and header
		aboutAlert.setHeaderText("Developed by yagmurlt");
		aboutAlert.setContentText("This application calculates module grades for Higher Diploma in Computing students"
				+ "and show whether the module is passed or failed.");
		//change icon of the alert
		try {
			Image img = new Image("Assets/calculator.png");
			ImageView imvIcon= new ImageView(img);
			//resize icon
			imvIcon.setFitWidth(50);
			imvIcon.setPreserveRatio(true);
			//set icon on the alert box
			aboutAlert.setGraphic(imvIcon);
			
		}
		catch(Exception e) {
			System.err.println("Error loading image for alert");
			e.printStackTrace();
		}
		//show the alert
		aboutAlert.showAndWait();
	}
	//shows a built in dialog (Alert)
	private void showHowToUseDialog() {
		//use an alert for the dialog
		Alert myAlert = new Alert(AlertType.INFORMATION);
		
		//set the title for the alert
		myAlert.setTitle("How to Use GradesCalculatorFX");
		
		//customize header and content
		myAlert.setHeaderText("You can follow the instructions below for use the GradesCalculatorFX");
		myAlert.setContentText("1. Enter your student number (at least 8 digit."
				+ "\n2. Click 'Check Grades' button to open the marks dialog"
				+ "\n3. Select a module and enter CA/Exam marks and weights."
				+ "\n4. Press 'Check Grades' button to calculate the result"
				+ "\n5. Results will be shown in the main text area"
				+ "\n6. Use 'Export Results' button to save your results to text file.");
		
		//change the icon of alert
		try {
			Image img = new Image("Assets/calculator.png");
			ImageView imvIcon= new ImageView(img);
			//resize icon
			imvIcon.setFitWidth(50);
			imvIcon.setPreserveRatio(true);
			//set icon on the alert bpx
			myAlert.setGraphic(imvIcon);
			
		}
		catch(Exception e) {
			System.err.println("Error loading image for alert");
			e.printStackTrace();
		}
		//show the alert
		myAlert.showAndWait();
	}
	//show a check grades dialog (stage) build and show it
	private void showCheckGradesDialog() {
		//create a secondary stage
		Stage dialogStage = new Stage();
		
		//set the title of the dialog
		dialogStage.setTitle("Check Grades");
		
		//create a layout for t he dialog (gridPane)
		GridPane gpDialog = new GridPane();
		
		//set attributes of the gridPAne#
		gpDialog.setPadding(new Insets(20));
		gpDialog.setHgap(10);
		gpDialog.setVgap(50);
		
		//create controls for the dialog (local variables)
		Label lblModule = new Label("Module:");
		MenuButton mbModule = new MenuButton("Click to Select Module");
		Label lblCA = new Label("CA Grade:");
		TextField txtfCA = new TextField();
		Label lblCAWeight = new Label("Weight:");
		TextField txtfCAWeight = new TextField();
		Label lblExam = new Label("Exam/Project Grade:");
		TextField txtfExam = new TextField();
		Label lblExamWeight = new Label("Weight:");
		TextField txtfExamWeight = new TextField();
		Button btnCancel = new Button("Cancel");
		Button btnCheck = new Button("Check Grades");
		
		//MenuButtom items
		MenuItem ood = new MenuItem("Object Oriented Development");
		MenuItem hgp = new MenuItem("HCI and GUI Programming");
		MenuItem dcn = new MenuItem("Data Communications & Networks");
		MenuItem pds = new MenuItem("Programming and Data Structures");
		MenuItem sewa = new MenuItem("Software Engineering for Web Applications");
		
		//change MenuButton size
		mbModule.setMinWidth(290);
		//add all modules items to module menu
		mbModule.getItems().addAll(ood, hgp, dcn, pds, sewa);
		//update the text after click the module
		for(int i=0; i<mbModule.getItems().size(); i++) {
			//get current menu item from the list
			MenuItem item = mbModule.getItems().get(i);
			//when clicked, set the menubutton text to that item's text
			item.setOnAction(event -> mbModule.setText(item.getText()));
		}
		
		//add controls to the layout (first col, second row)
		gpDialog.add(lblModule, 0, 0);
		gpDialog.add(lblCA, 0, 1);
		gpDialog.add(lblExam, 0, 2);
		gpDialog.add(lblCAWeight, 2, 1);
		gpDialog.add(lblExamWeight, 2, 2);
		gpDialog.add(txtfCAWeight, 3, 1);
		gpDialog.add(txtfExam, 1, 2);
		gpDialog.add(txtfExamWeight, 3, 2);
		gpDialog.add(txtfCA, 1, 1);
		gpDialog.add(mbModule, 1, 0, 2, 1);
		gpDialog.add(btnCancel, 3, 3);
		gpDialog.add(btnCheck, 4, 3);
		
		//Handle events on the cancel button
		btnCancel.setOnAction(event -> dialogStage.close());
		//Handle events on the check grades button (btnCheck)
		btnCheck.setOnAction(event -> {
			//check if the module is selected
			if (mbModule.getText().equals("Click to Select Module")) {
				//use an alert for the not selected module
				Alert moduleAlert = new Alert(AlertType.WARNING);
				//set the title of the moduleAlert
				moduleAlert.setTitle("No Module Selected");
				//customize header and content
				moduleAlert.setHeaderText("Module is not selected");
				moduleAlert.setContentText("Please select a module before check your grades.");
				//show the alert
				moduleAlert.showAndWait();
				return; 
			}
			//check if the text fields are empy
			if(txtfCA.getText().isEmpty() || txtfCAWeight.getText().isEmpty()
					|| txtfExamWeight.getText().isEmpty() || txtfExam.getText().isEmpty()) {
				//use an alert for the empty text fields
				Alert myAlert = new Alert(AlertType.ERROR);
				//set title of the alert
				myAlert.setTitle("Empty Input");
				//customize header and content
				myAlert.setHeaderText("Field cannot be empty.");
				myAlert.setContentText("Please enter a valid value and try again.");
				//show the alert
				myAlert.showAndWait();
				return;
			}
			try {
				//convert to double format
				double caGrade = Double.parseDouble(txtfCA.getText());
				double caWeight = Double.parseDouble(txtfCAWeight.getText());
				double examGrade = Double.parseDouble(txtfExam.getText());
				double examWeight = Double.parseDouble(txtfExamWeight.getText());
				//check the values of the marks between range 1-99
				if(caGrade<1 || caGrade>99 || caWeight<1 || caWeight>99 ||
						examGrade<1 || examGrade>99 || examWeight<1|| examWeight>99) {
					//use an alert for 
					Alert rangeAlert = new Alert(AlertType.ERROR);
					//set the title of the alert
					rangeAlert.setTitle("Invalid Range");
					//customize header and content
					rangeAlert.setHeaderText("Values must be between 1 and 99.");
					rangeAlert.setContentText("Please correct your input and try again");
					//show the alert
					rangeAlert.showAndWait();
					return;
				}
				//validation is successful, go on
				else {
					//overall grade
					double overall = (caGrade*caWeight + examGrade*examWeight)/(caWeight+examWeight);
					boolean caPass = caGrade>= 40;
					boolean examPass = examGrade>=40;
					String resultMessage;
					if(!caPass && examPass) {
						//Message = "** Module FAIL. Repeat Assignment at next sitting. **";
						resultMessage = "*** Module FAIL. Repeat Assignment at next sitting ***";
					}else if (caPass && !examPass) {
						resultMessage = "*** Module FAIL. Repeat Exam at next sitting ***";
					}else if(!caPass && !examPass) {
						resultMessage = "*** Module FAIL. Repeat both components at next sitting ***";
					}else {
						resultMessage = "*** MODULE PASS ***";
					}
					
					//module name from menubutton#
					String moduleName = mbModule.getText();
					
					//string of result
					String result = moduleName + "\n" + "CA: " + caGrade + "% Exam: " + examGrade + "% Overall Grade: " + overall + "\n" + resultMessage;
					
					//check the student number
					String currentStudentNumber = txtfNumber.getText();
					if(!lastStudentNumber.equals(currentStudentNumber)) {
						txtMain.clear();
					}
					//append to main text area
					txtMain.appendText(result + "\n\n");
					// remember the last entered student number
					lastStudentNumber = currentStudentNumber;
				}
			}
			//success: close the dialog and continue calculating
			catch(Exception e) {
				//use an alert for if there is missing or incorrect input
				Alert errorAlert = new Alert(AlertType.ERROR);
				//set the title of the alert
				errorAlert.setTitle("Invalid number");
				//customize header and content
				errorAlert.setHeaderText("Input is not a number!");
				errorAlert.setContentText("Please enter numeric value only.");
				//shoe the alert
				errorAlert.showAndWait();
			}
		});
	
		//create a scene for the dialog
		Scene s = new Scene(gpDialog);
		//Apply a Stylesheet (global style of UI)
		s.getStylesheets().add("Assets/calculator.css");
		//set the scene for the dialof
		dialogStage.setScene(s);
		//show the dialog stage
		dialogStage.show();
	}
	//allow the user to change the photo (only jpeg/jpg!)
	private void changePhoto() {
		//show a system dialog to choose new image
		FileChooser fc = new FileChooser();
		fc.setTitle("Choose a new image");
		
		//filter out only .jpeg/.jpg!
		ExtensionFilter onlyJPG = new ExtensionFilter("Only .jpeg/.jpg images!","*.jpg");
		
		//add filter to the file choser
		fc.getExtensionFilters().addAll(onlyJPG);
		
		//get the chosen file
		File chosenFile = fc.showOpenDialog(null);
		
		//make sure the chosen file isn't null
		if(chosenFile != null) {
			//get the path of the chosen file, update label
			lblPath.setText(chosenFile.getPath().toString());
			
			//show the chosen image (try/catch)
			try {
				img = new Image(chosenFile.toURI().toString());
				imvProfilePhoto.setImage(img);
			}
			catch(Exception e) {
				System.err.print("Something went wrong with the chosen user image");
			}
		}
	}
	//use a file choser to allow user to choose where to save the text
	private void exportResults() {
		//volitale action! (try/catch)
		try {
			String studentNo = txtfNumber.getText();
			//use an alert for if there is not any student number
			if(studentNo.isEmpty()) {
				//use an alert for null input
				Alert warningAlert = new Alert(Alert.AlertType.WARNING);
				//set the title of the alert
				warningAlert.setTitle("Missing Student Number");
				//customize header and content
				warningAlert.setHeaderText("Student number is required.");
				warningAlert.setContentText("Please enter a student number before export your results");
				//show the alert
				warningAlert.showAndWait();
				return;
			}
			//allow the user choose location/name of file to be safved
			FileChooser fc = new FileChooser();
			fc.setTitle("Choose where to save your results");
			//only allow.txt files
			ExtensionFilter onlyTXT = new ExtensionFilter("Only TXT files!","*.txt");
			fc.getExtensionFilters().add(onlyTXT);
			
			//set default file name
			String fileName = "ResultsFor" + studentNo + ".txt";
			//save the file with default file name
			fc.setInitialFileName(fileName);
			
			//get the file chosen by the user
			File chosenFile = fc.showSaveDialog(null);
			
			//check if file choosen is not null, then write it
			if(chosenFile != null) {
				//write the file to disk using a BufferedWriter
				BufferedWriter buf = new BufferedWriter(new FileWriter(chosenFile));
				//get the text from main text area
				buf.write(txtMain.getText());
				//close the writer
				buf.close();
				
				//inform user that export is succesfull
				Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
				//set the title of the alert
				successAlert.setTitle("Export Successful");
				//customize header and content
				successAlert.setHeaderText("Results exported successfully");
				successAlert.setContentText("Saved as: " + chosenFile.getName());
				//show the alert
				successAlert.showAndWait();
			}
		}
		catch(Exception e) {
			System.err.print("Error writing user choosen text file in exportResults()!");
			e.printStackTrace();
			
			//inform student that export failed
			Alert failAlert = new Alert(Alert.AlertType.ERROR);
			//set the title of the alert
			failAlert.setTitle("Export Failed");
			//customize header and content
			failAlert.setHeaderText("Couldn't export your results");
			failAlert.setContentText("Please try again or choose another file location");
			//shoe the alert
			failAlert.showAndWait();
		}
	}
	//count the words in the text area
	private void wordCount() {
		//get textt from the main area
		String text = txtMain.getText();
		//check the line if it's empty
		if(text==null || text.isEmpty()) {
			//if it's empty show 0 word adn stop
			lblWordCount.setText("Words: 0");
			return;
		}
		//split the text into words
		String[] words = text.split("\\s+");
		//display the total numbers of the words
		lblWordCount.setText("Words: " + words.length);
	}
	// Window setup and layouts
	@Override
	public void start(Stage primaryStage) throws Exception {
		//Manage window title and size
		primaryStage.setTitle("Student Grade Calculator");

		//Manage default window width and height
		primaryStage.setWidth(800);
		primaryStage.setHeight(700);
		
		//Create main layout
		BorderPane bpMain = new BorderPane();
		
		//Add file, student, help menus to MenuBar
		mbMain.getMenus().addAll(mnuFile, mnuHelp);
		
		//add menu items to File Menu
		mnuFile.getItems().addAll(miFileQuit);
		
		
		//add menu items to Help Menu
		mnuHelp.getItems().addAll(miHelpAbout, miHelpHowToUse);
		
		//TCreate sub-layouts
		VBox vbLeft  = new VBox();
		VBox vbRight = new VBox();
		HBox hbButton = new HBox();
		//Add components to sub-layouts
		vbLeft.getChildren().addAll(imvProfilePhoto,btnChangePhoto,lblNumber,txtfNumber, btnCheckGrades);
		hbButton.getChildren().addAll(btnExport, btnReset);
		vbRight.getChildren().addAll(txtMain, lblWordCount, hbButton);
		//update the word count when text changes
		txtMain.textProperty().addListener((observable, oldValue, newValue) -> {
		    wordCount();
		});
		
		//Add sub-layouts to main layout
		bpMain.setLeft(vbLeft);
		bpMain.setCenter(vbRight);
		//Manage Padding and Spacing for Layouts (VBox left and right padding/spacing)
		vbLeft.setPadding(new Insets(20));
		vbLeft.setSpacing(20);
		vbRight.setPadding(new Insets(20));
		vbRight.setSpacing(20);
		//Manage Alignment for Layouts (HBox spacing)
		hbButton.setSpacing(10);
		hbButton.setAlignment(Pos.CENTER_RIGHT);
		//Bind ImageView to 1/3rd of the width of the main window and preserve its ratio
		imvProfilePhoto.fitWidthProperty().bind(primaryStage.widthProperty().divide(3));
		//Bind width of changePhoto button to width of ImageView
		imvProfilePhoto.setPreserveRatio(true);
		//Create Scene (main layout as argument)
		Scene s = new Scene(bpMain);
		//Apply a Stylesheet (global style of UI)
		s.getStylesheets().add("Assets/calculator.css");
		//add components to main layout
		bpMain.setTop(mbMain);
		//change the icon of the primary stage
		primaryStage.getIcons().add(new Image("Assets/calculator.png"));
		//Set scene
		primaryStage.setScene(s);
		// Show stage
		primaryStage.show();
	}
	// Launch application
	public static void main(String[] args) {
		launch(args);
	}
}