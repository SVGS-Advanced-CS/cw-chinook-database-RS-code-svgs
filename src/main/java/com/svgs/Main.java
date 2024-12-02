package com.svgs;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Main {

    private static Connection conn;
    private static Statement state;
    private static String loggedInUser;
    private static String loggedInRole;
    private static boolean loggedIn = false;
    public static void main(String[] args) {
        createDB();
        menu();
    }
        public static void menu(){
            if(!loggedIn){
                menu1();
            } else{
                menu2();
            }
        }

        public static void menu2(){
            System.out.println("Logged in as "+loggedInUser);
            if(loggedInRole.equals("admin")){
                System.out.println("0. Delete User");
            }
            System.out.println("1. Change Password");
            System.out.println("2. Log out");
            System.out.println("3. Exit");
            System.out.print("Make a choice: ");
            Scanner input = new Scanner(System.in);
            String choice = input.nextLine();
            if(choice.equals("1")){
                System.out.println("What is your new password?");
                String newPassword = input.nextLine();
                changePassword(newPassword);
            } else if(choice.equals("2")){
                loggedIn=false;
                loggedInRole=null;
                loggedInUser=null;
                
            }else if(choice.equals("3")){
                System.exit(0);
            } else if(choice.equals("0") && loggedInRole.equals("admin")){
                deleteUser();
            }
            menu();
        }

        public static void menu1(){
            System.out.println("1. Create a user");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Make a choice: ");
            Scanner input = new Scanner(System.in);
            String choice = input.nextLine();

            if(choice.equals("1")){
                System.out.print("Username: ");
                String username = input.nextLine();
                System.out.print("Password: ");
                String password = input.nextLine();
                System.out.print("Role: ");
                String role = input.nextLine();
                createUser(username, password, role);
            } 
            else if(choice.equals("2")){
                System.out.print("Username: ");
                String username = input.nextLine();
                System.out.print("Password: ");
                String password = input.nextLine();
                login(username,password);
            } else if(choice.equals("3")){
                System.exit(0);
            }

            menu();
        }

        public static void login(String u, String p){
            String query = "SELECT * FROM users WHERE userId='" + u+ "' AND password='"+p+"'";
            try{
                ResultSet results = state.executeQuery(query);
                while(results.next()){
                    System.out.println("Logged in!");
                    loggedInUser = results.getString("userId");
                    loggedInRole = results.getString("role");
                    loggedIn = true;
                }
            } catch(Exception e){

            }
        }
        public static void deleteUser() {
            try {
                String query = "SELECT userId FROM users";
                ResultSet results = state.executeQuery(query);
                while(results.next()){
                    System.out.println(results.getString("userId"));
                }
                System.out.println("Username of person you wish to delete:");
                Scanner input = new Scanner(System.in);
                String userToDelete = input.nextLine();
                query = "DELETE FROM users WHERE userId='"+userToDelete+"'";
                state.executeUpdate(query);
            } catch(SQLException e) {
                System.out.println(e);
            }
        }

        public static void changePassword(String newPassword){
            try {
                String query = "UPDATE users SET password='"+newPassword+"'WHERE userId='"+loggedInUser+"'";
                state.executeUpdate(query);
            } catch (Exception e) {
                System.out.println(e);
            }
        }


        public static void createUser(String u, String p, String r){
            String query = "INSERT INTO users(userId,password,role) VALUES('"+u+"','"+p+"','"+r+"')";
            try{
                state.executeUpdate(query);
            } catch(Exception e){
                System.out.println(e);
            }
        }

        public static void createDB(){
            String url = "jdbc:sqlite:./src/main/resources/users.db";
            try{
                conn = DriverManager.getConnection(url);
                state = conn.createStatement();
                String query = "CREATE TABLE IF NOT EXISTS users(userId TEXT, password TEXT, role TEXT)";
                state.executeUpdate(query);
            } catch(Exception e){
                System.out.println("Error");
                System.out.println(e);
            }
        }
        /*String url = "jdbc:sqlite:./src/main/resources/users.db";
        try {
            Connection conn = DriverManager.getConnection(url);
            Statement state = conn.createStatement();

            String query = "UPDATE employees SET FirstName='Garfield',LastName='Andrew' WHERE FirstName='Andrew' AND LastName='Adams'";
            state.executeUpdate(query);

            query = "DELETE FROM employees WHERE FirstName='R'";
            System.out.println(state.executeUpdate(query));

            query= "SELECT * FROM employees ORDER BY FirstName ASC";
            ResultSet results = state.executeQuery(query);
            while(results.next()){
                String first = results.getString("FirstName");
                String last = results.getString("LastName");
                int employeeId = results.getInt("EmployeeId");
                System.out.println(employeeId+": "+ first + " "+ last);
            }
            conn.close();
        } catch (SQLException e) { 
            System.out.println("Error!");
            System.out.println(e);

        }*/

    }