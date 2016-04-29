package ip.theia2.networking.interfaces;

/**
 * Interface for login results
 *
 * @author Zachary Shannon
 */
public interface LoginHandler {

    void loginSuccess();

    void loginFail();

    void loginReject();

}
