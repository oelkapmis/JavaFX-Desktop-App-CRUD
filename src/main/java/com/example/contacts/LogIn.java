package com.example.contacts;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.effect.DropShadow;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class LogIn {

    @FXML
    private Button btnLogin;

    @FXML
    private Label lblPassword;

    @FXML
    private Label lblUserId;

    @FXML
    private Label lblDontHaveAnAccount;

    @FXML
    private Label lblLoginMessage;

    @FXML
    private PasswordField pfPassword;

    @FXML
    private TextField tfUserId;

    @FXML
    private Label lblSignUp;


    @FXML
    void onLoginButtonClick(ActionEvent event) throws IOException, SQLException {
        checkLogin();
    }

    @FXML
    void onMouseEntered(MouseEvent event) {
        Launcher m = new Launcher();
        m.setCursorTo(Cursor.HAND);

        switch (((Control) event.getSource()).getId()) {
            case "btnLogin" -> btnLogin.setEffect(new DropShadow());
            case "lblSignUp" -> lblSignUp.setEffect(new DropShadow());
        }
    }

    @FXML
    void onMouseExited(MouseEvent event) {
        Launcher m = new Launcher();
        m.setCursorTo(Cursor.DEFAULT);

        switch (((Control) event.getSource()).getId()) {
            case "btnLogin" -> btnLogin.setEffect(null);
            case "lblSignUp" -> lblSignUp.setEffect(null);
        }
    }

    @FXML
    void onKeyPressed(KeyEvent event) throws SQLException, IOException {
        if(event.getCode() == KeyCode.ENTER)
            checkLogin();
    }

    @FXML
    void onGoToSignUp(MouseEvent event) throws IOException {
        Launcher main = new Launcher();
        main.changeSceneTo("SignUp.fxml");
    }

    private void checkLogin() throws IOException, SQLException {
        boolean validation = false;

        if(Objects.equals(tfUserId.getText(), "") || Objects.equals(pfPassword.getText(), "")){
            lblLoginMessage.setText("All fields are required.");
        }
        else{
            try{
                User user = getUserFromDB(Integer.parseInt(tfUserId.getText()));

                if(user == null){
                    lblLoginMessage.setText("Incorrect user ID or password.");
                }
                else if(user.getId() == Integer.parseInt(tfUserId.getText()) &&
                        user.getPassword().equals(pfPassword.getText())){
                    validation = true;
                }
                else{
                    lblLoginMessage.setText("Incorrect user ID or password.");
                }
            } catch (NumberFormatException e) {
                lblLoginMessage.setText("User ID must be an integer.");
            }
        }

        if(validation){
            Launcher main = new Launcher();
            main.changeSceneTo("Main.fxml");
        }
    }

    private User getUserFromDB(int id){
        Statement st;
        ResultSet rs;
        User user = null;

        try {
            st = getConnection().createStatement();
            rs = st.executeQuery("SELECT * FROM public.\"Users\" " +
                    "WHERE id = " + id);

            while(rs.next()){
                user = new User(
                        rs.getInt("id"),
                        rs.getString("password"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }

    public Connection getConnection(){
        DbFunctions dbFunctions = new DbFunctions();
        return dbFunctions.connect_to_db("JavaDesktopAppDB", "postgres", "oelkapmis");
    }

    private void executeQuery(String query) {
        Connection conn = getConnection();
        Statement st;
        try{
            st = conn.createStatement();
            st.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

