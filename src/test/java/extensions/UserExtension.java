package extensions;

import api.ApiClient;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import utils.TestDataFactory;

public class UserExtension implements BeforeEachCallback, AfterEachCallback {
    private static final ThreadLocal<UserSession> SESSION_HOLDER = new ThreadLocal<>();

    public static UserSession getSession() {
        return SESSION_HOLDER.get();
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        ApiClient apiClient = new ApiClient();
        String login = TestDataFactory.uniqueLogin();
        String password = TestDataFactory.uniquePassword();

        apiClient.register(login, password);
        String token = apiClient.token(login, password);

        SESSION_HOLDER.set(new UserSession(apiClient, token, login, password));
    }

    @Override
    public void afterEach(ExtensionContext context) {
        UserSession session = SESSION_HOLDER.get();
        if (session != null) {
            session.getApiClient().deleteUser(session.getToken());
        }
        SESSION_HOLDER.remove();
    }
}