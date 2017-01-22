package in.mobileappdev.news.bus;

/**
 * Created by satyanarayana.avv on 23-12-2016.
 */

public class MainBus extends RxEventBus {

    private static MainBus instance;

    private MainBus() {
    }

    public static MainBus getInstance() {
        if (instance == null)
            instance = new MainBus();
        return instance;
    }
}
