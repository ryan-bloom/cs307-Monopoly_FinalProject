package Controller;
import java.io.*;

public class GameSaver<T> {

    public void saveGame(T game) {
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("Game.ser"));
            os.writeObject(game);
            os.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public T reload(File file) {
        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(file));
            T game = (T) is.readObject();
            return game;
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
