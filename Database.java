import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;

import javax.swing.*;
import java.awt.*;

public class Database{
    public static void main(String[] args) {
        GUI menu = new GUI();
        DatabaseManagement base = new DatabaseManagement("baza1");  
    
    }
}

class GUI {
    private JFrame frame = new JFrame();
    private JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    private JPanel databasesListPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    
    GUI(){
        frame.setSize(400, 400);
        frame.setTitle("Symulacja bazy danych: Drużyny ligi angielskiej");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
        JButton prev = new JButton("---");
        JButton next = new JButton("---");

        JLabel listLabel  = new JLabel();
        listLabel.setText("Lista baz danych: ");

        databasesListPanel.add(listLabel);

        for(String filename : getDatabasesName()){
            if(!filename.equals(".git") && !filename.equals("README.md") && !filename.equals("Database.java") )
               {
                JButton button = new JButton(filename);
                button.addActionListener(e->showDatabase());
                databasesListPanel.add(button);
            }
        }
        

        buttonPanel.add(prev);
        buttonPanel.add(next);
    
        // Stwórz okienko do wyświetlania danych
        JTextArea textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
    
        textArea.setText("Wybierz bazę danych z listy");
        // Dodaj elementy do okna
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.add(databasesListPanel, BorderLayout.NORTH);
    
        // Wyświetl okno
        frame.setVisible(true);
        this.getDatabasesName();
    }

    void showDatabase(){
        System.out.println("hello");
    }

    String[] getDatabasesName(){
        File folder = new File(System.getProperty("user.dir"));
        String[] listOfFiles = folder.list();

        
        return listOfFiles;
    }
}

class DatabaseManagement {
    private String containerName = "baza1";
   
    
    DatabaseManagement(String container){
        this.createContainer(container);
        this.containerName = container;
        this.initValues();
        
    }

    DatabaseManagement(){
        this.createContainer(this.containerName);
        this.initValues();
    }

    private void createContainer(String containerName){
            File container = new File(containerName);
            if (!container.exists()) {
                Boolean result = container.mkdir();
                if(result){
                    System.out.println("Directory created: " + container.getName());
                    this.createCollectionInDirectory(container.getName(),"team");
                    this.createCollectionInDirectory(container.getName(),"players");
                    this.createCollectionInDirectory(container.getName(),"trainers");
                    this.createCollectionInDirectory(container.getName(),"matches");
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


    // void writeToFile(String content){
    //     try{
    //         FileWriter writer = new FileWriter(this.filename);
    //         writer.write(content);
    //         writer.close();
    //         System.out.println("Successfully wrote to the file.");
    //     }catch (IOException e) {
    //         System.out.println("An error occurred.");
    //         e.printStackTrace();
    //       }
    // }

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
  
    private void initValues(){

    }
}


