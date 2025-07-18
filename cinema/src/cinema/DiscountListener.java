package cinema;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DiscountListener extends Remote {
    void notifyDiscount(int showId, String eventTitle, String showDate, String showTime) throws RemoteException;
}
