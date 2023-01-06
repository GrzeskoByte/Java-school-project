import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Set;
import java.io.BufferedReader;

import javax.swing.*;
import java.awt.*;

public class Database{
    public static void main(String[] args) {
        DatabaseManagement base = new DatabaseManagement("baza1");  
        DatabaseManagement base2 = new DatabaseManagement("baza2");  
        GUI menu = new GUI(base);
    
    }
}

class GUI {
    private JFrame frame = new JFrame();
    private JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    private JPanel databasesListPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    private JPanel mainView =new JPanel(new FlowLayout(FlowLayout.CENTER));
    private JPanel subView = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private JTextArea textArea = new JTextArea();

    private JButton clearTextArea = new JButton("reset");
    private JButton addToCollection = new JButton("---");

    private DatabaseManagement base;

    GUI(DatabaseManagement database){
        base = database;

        frame.setSize(500, 500);
        frame.setTitle("Symulacja bazy danych: Drużyny ligi angielskiej");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
        JButton clearTextArea = new JButton("wyczyść");
        JButton addToCollection = new JButton("---");

        JLabel listLabel  = new JLabel();
        listLabel.setText("Lista baz danych: ");

        databasesListPanel.add(listLabel);

        for(String filename : getDatabasesName()){
            if(!filename.equals(".git") && !filename.equals("README.md") && !filename.equals("Database.java") )
               {
                JButton button = new JButton(filename);
                button.addActionListener(e->{
                    base.switchDatabase(filename);
                    String data[] = base.collectionsName;

                    for(String el : data){   
                        JButton btn = new JButton(el);
                        btn.addActionListener(n->{
                            ArrayList<String> records = base.getCollectionRecords(el);
                            textArea.setText("");
                            textArea.append(el + ": \n");
                            for(String record : records){
                                textArea.append("   "+ record + "\n");
                            }

                            textArea.revalidate();
                            textArea.repaint();
                        });
                        mainView.add(btn);
                    }

                    addToCollection.setText("dodaj rekord");
                    
                    addToCollection.addActionListener(n->{
                        textArea.setText("");
                        mainView.removeAll();

                        frame.revalidate();
                        frame.repaint();
                    });

                    mainView.revalidate();
                    mainView.repaint();
                });
                databasesListPanel.add(button);
            }
        }
        
        clearTextArea.addActionListener(e->{
            textArea.setText("");
            addToCollection.setText("---");
            mainView.removeAll();
            frame.revalidate();
            frame.repaint();
        });

        buttonPanel.add(clearTextArea);
        buttonPanel.add(addToCollection);
     
        subView.add(textArea);
     
        frame.add(subView, BorderLayout.WEST);
        frame.add(mainView, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.add(databasesListPanel, BorderLayout.NORTH);
       
      
        frame.setVisible(true);
      
    }

   
    

    String[] getDatabasesName(){
        File folder = new File(System.getProperty("user.dir"));
        String[] listOfFiles = folder.list();
        return listOfFiles;
    }
}

class DatabaseManagement {
    private String containerName = "baza1";
    public String collectionsName[] = {"teams","players","trainers","matches"};
    
    DatabaseManagement(String container){
        this.createContainer(container);
        this.containerName = container;
    }

    DatabaseManagement(){
        this.createContainer(this.containerName);
       
    }

    void switchDatabase(String newContainerName){
        this.containerName = newContainerName;
    }

    private void createContainer(String containerName){
            File container = new File(containerName);
            if (!container.exists()) {
                Boolean result = container.mkdir();
                if(result){
                    System.out.println("Directory created: " + container.getName());
                    for(String filename : this.collectionsName)
                        this.createCollectionInDirectory(container.getName(), filename);
                    
                }else{
                    System.out.println("Error occurs");
                }
            }


    }

    
    void createCollectionInDirectory(String directoryName, String collectionName){
        try {
            File base = new File(directoryName + "/" + collectionName);
            if (base.createNewFile()) {
                System.out.println("File created: " + collectionName);
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    
    

    ArrayList<String> getCollectionRecords(String collectionName){
        ArrayList<String> records = new ArrayList<>();
        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader(this.containerName+"/"+collectionName))) {
            while ((line = reader.readLine()) != null) {
                records.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

      return records;
    }
   
    void writeToFile(String filename, String content){
        try{
            FileWriter writer = new FileWriter(this.containerName + "/" + filename,true);
            writer.write(content);
            writer.close();
            System.out.println("Successfully wrote to the file.");
        }catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
    }

   

    // void readFile(){
    //     try{
    //         int i;
    //         FileReader reader = new FileReader(this.filename);
    //         while ((i = reader.read()) != -1)
    //             System.out.print((char)i);
    //         reader.close();
    //     }catch (IOException e) {
    //         System.out.println("An error occurred.");
    //         e.printStackTrace();
    //       }
    // }
  
    
}


