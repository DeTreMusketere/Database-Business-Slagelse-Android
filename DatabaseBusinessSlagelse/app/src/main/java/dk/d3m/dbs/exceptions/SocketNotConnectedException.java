package dk.d3m.dbs.exceptions;

/**
 * Created by Patrick on 22-09-2014.
 */
public class SocketNotConnectedException extends Exception {
    public SocketNotConnectedException() {
    }

    public SocketNotConnectedException(String detailMessage) {
        super(detailMessage);
    }
}
