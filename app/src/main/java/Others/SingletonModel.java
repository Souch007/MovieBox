package Others;

/**
 * Created by Bir Al Sabia on 6/1/2016.
 */
public class SingletonModel {
    private static SingletonModel instance;

    public String drawercatid;



    public synchronized static SingletonModel getSingletonModel() {

        if (instance == null) {
            instance = new SingletonModel();
        }
        return instance;

    }

    private void SingletonModel() {
    }
}