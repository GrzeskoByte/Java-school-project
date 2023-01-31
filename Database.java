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

    private JTextField collectionNameField = new JTextField();
    private JTextField recordField = new JTextField();
    private JTextField newDatabaseField = new JTextField();

    
    private JLabel collectionLabel = new JLabel("Nazwa kolekcji");
    private JLabel recordLabel = new JLabel("Rekord");

    private JButton clear = new JButton("reset");
    private JButton addToCollection = new JButton("---");
    private JButton deleteRecordBtn = new JButton("---");
    
    private JButton confrimDataSend = new JButton("Zatwierdź");
    private JButton createDatabaseBtn = new JButton("Nowa baza");
    private JButton confrimCreateDatabaseBtn = new JButton("Stwórz nową baze danych");

    private DatabaseManagement base;

    GUI(DatabaseManagement database){
        base = database;

        frame.setSize(500, 500);
        frame.setTitle("Symulacja bazy danych: Drużyny ligi angielskiej");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
        JButton clearBtn = new JButton("wyczyść");
        JButton addToCollectionBtn = new JButton("---");
        JButton deleteRecordBtn = new JButton("---");

        JLabel listLabel  = new JLabel();
        listLabel.setText("Lista baz danych: ");

        databasesListPanel.add(listLabel);

      
        for(String filename : getDatabasesName()){
            if(!filename.equals(".git") && !filename.equals("README.md") && !filename.equals("Database.java") )
               {
                JButton button = new JButton(filename);
                

                button.addActionListener(e->{
                    mainView.removeAll();
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

                    addToCollectionBtn.setText("dodaj rekord");
                    deleteRecordBtn.setText("usuń rekord");

                    addToCollectionBtn.addActionListener(n->{
                        this.addToCollection();
                    });
                    
                    deleteRecordBtn.addActionListener(n->{
                        this.deleteFromCollection();
                    });



                    mainView.revalidate();
                    mainView.repaint();
                });
                
                databasesListPanel.add(button);
            }
        }
        
        createDatabaseBtn.addActionListener(l->{
            mainView.removeAll();
            mainView.add(newDatabaseField);
            mainView.add(confrimCreateDatabaseBtn);

            newDatabaseField.setColumns(20);

            confrimCreateDatabaseBtn.addActionListener(k->{
                new DatabaseManagement(newDatabaseField.getText());

                frame.revalidate();
                frame.repaint();
            });

            frame.revalidate();
            frame.repaint();
        });

        databasesListPanel.add(createDatabaseBtn);

        clearBtn.addActionListener(e->{
            textArea.setText("");
            addToCollectionBtn.setText("---");
            deleteRecordBtn.setText("---");
            mainView.removeAll();

            frame.revalidate();
            frame.repaint();
        });


        buttonPanel.add(clearBtn);
        buttonPanel.add(addToCollectionBtn);
        buttonPanel.add(deleteRecordBtn);

        subView.add(textArea);
     
        frame.add(subView, BorderLayout.WEST);
        frame.add(mainView, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.add(databasesListPanel, BorderLayout.NORTH);
       
      
        frame.setVisible(true);
      
    }
    
    void deleteFromCollection() {
        textArea.setText("");
        mainView.removeAll();
      
        mainView.add(collectionLabel);
        mainView.add(collectionNameField);
      
        collectionNameField.setColumns(20);
        recordField.setColumns(20);
        JButton confirmDataSend = new JButton("zatwierdz");
        mainView.add(confirmDataSend);
      
        confirmDataSend.addActionListener(e -> {
          ArrayList<String> records = base.getCollectionRecords(collectionNameField.getText());
          for (String record : records) {
            JButton deleteBtn = new JButton("Usuń");
            JLabel deleteLabel = new JLabel(record);
      
            deleteBtn.addActionListener(j -> {
              ArrayList<String> newRecords = new ArrayList<>();
              for (String line : records) {
                if (!line.equals(record)) {
                  newRecords.add(line);
                }
              }
      
              base.overRideCollection(collectionNameField.getText(), newRecords);
              deleteFromCollection();
            });
      
            mainView.add(deleteLabel);
            mainView.add(deleteBtn);
          }
      
          mainView.revalidate();
          mainView.repaint();
        });
      
        mainView.revalidate();
        mainView.repaint();
      }
      
  

    void addToCollection(){
        textArea.setText("");
        mainView.removeAll();

        mainView.add(collectionLabel);
        mainView.add(collectionNameField);

        mainView.add(recordField);
        mainView.add(recordLabel);

        collectionNameField.setColumns(20);
        recordField.setColumns(20);
        
        mainView.add(confrimDataSend);


        confrimDataSend.addActionListener(v->{
            String recordToAdd = recordField.getText();
            String collectionName = collectionNameField.getText();
            
            base.writeToFile(collectionName,recordToAdd);
            System.out.println("send");
            mainView.removeAll();

            mainView.revalidate();
            mainView.repaint();
         });

    
        frame.revalidate();
        frame.repaint();
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
            File base = new File(directoryName + "/" + collectionName + ".dat");
            if (base.createNewFile()) {
                System.out.println("File created: " + collectionName + ".dat");
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
        try (BufferedReader reader = new BufferedReader(new FileReader(this.containerName+"/"+collectionName+".dat"))) {
            while ((line = reader.readLine()) != null) {
                records.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

      return records;
    }
   
    void writeToFile(String collectionName, String content){
        try{
            FileWriter writer = new FileWriter(this.containerName + "/" + collectionName+".dat",true);
            writer.write("\n"+"  "+content);
            writer.close();
            System.out.println("Successfully wrote to the file.");
        }catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
    }


    void overRideCollection(String collectionName, ArrayList<String> newCollection){
        try{
            FileWriter writer = new FileWriter(this.containerName + "/" + collectionName+".dat");
            for(String record : newCollection)
                     writer.write(record);
            writer.close();
            System.out.println("Successfully wrote to the file.");
        }catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
    }
    
}


