import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Database{
    public static void main(String[] args) {
        DatabaseManagement base = new DatabaseManagement("database.txt");
        
        base.writeToFile( "pierwszy zapis do bazy danych");
        base.readFile();
    }
}

class DatabaseManagement {
    private String filename = "database.txt";
    
    DatabaseManagement(String container){
        this.createContainer(container);
        this.filename = container;
        this.initValues();
    }

    DatabaseManagement(){
        this.createContainer(this.filename);
        this.initValues();
    }

    private void createContainer(String filename){
        try {
            File container = new File(filename);
            if (container.createNewFile()) {
              System.out.println("File created: " + container.getName());
            } else {
              System.out.println("File already exists.");
            }
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
    }


    void writeToFile(String content){
        try{
            FileWriter writer = new FileWriter(this.filename);
            writer.write(content);
            writer.close();
            System.out.println("Successfully wrote to the file.");
        }catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
    }

    void readFile(){
        try{
            int i;
            FileReader reader = new FileReader(this.filename);
            while ((i = reader.read()) != -1)
                System.out.print((char)i);
            reader.close();
        }catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
    }
  
    private void initValues(){

    }
}


