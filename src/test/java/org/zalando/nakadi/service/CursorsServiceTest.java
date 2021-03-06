package org.zalando.nakadi.service;

import org.junit.Before;
import org.junit.Test;
import org.zalando.nakadi.domain.SubscriptionResource;
import org.zalando.nakadi.exceptions.runtime.AccessDeniedException;
import org.zalando.nakadi.plugin.api.authz.AuthorizationService;
import org.zalando.nakadi.repository.db.SubscriptionDbRepository;

import java.util.Collections;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class CursorsServiceTest {

    private AuthorizationValidator authorizationValidator;
    private CursorsService service;

    @Before
    public void setup() {
        authorizationValidator = mock(AuthorizationValidator.class);
        service = new CursorsService(mock(SubscriptionDbRepository.class), mock(SubscriptionCache.class), null, null,
                null, null, null, null, authorizationValidator);
    }

    @Test(expected = AccessDeniedException.class)
    public void whenResetCursorsThenAdminAccessChecked() throws Exception {
        doThrow(new AccessDeniedException(AuthorizationService.Operation.ADMIN, new SubscriptionResource("", null)))
                .when(authorizationValidator).authorizeSubscriptionAdmin(any());
        service.resetCursors("test", Collections.emptyList());
    }

    @Test(expected = AccessDeniedException.class)
    public void whenCommitCursorsAccessDenied() throws Exception {
        doThrow(new AccessDeniedException(AuthorizationService.Operation.ADMIN, new SubscriptionResource("", null)))
                .when(authorizationValidator).authorizeSubscriptionCommit(any());
        service.commitCursors("test", "test", Collections.emptyList());
    }
}
