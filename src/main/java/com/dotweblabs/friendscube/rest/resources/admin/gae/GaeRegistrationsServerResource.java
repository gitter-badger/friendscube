package com.dotweblabs.friendscube.rest.resources.admin.gae;

import com.dotweblabs.friendscube.app.client.shared.entity.Registration;
import com.dotweblabs.friendscube.rest.guice.SelfInjectingServerResource;
import com.dotweblabs.friendscube.rest.resources.admin.RegistrationsResource;
import com.dotweblabs.friendscube.rest.services.RegistrationService;
import com.google.inject.Inject;

import java.util.List;

/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
public class GaeRegistrationsServerResource extends SelfInjectingServerResource
    implements RegistrationsResource {

    @Inject
    RegistrationService registrationService;

    @Override
    public List<Registration> list() {
        // TODO Add admin check
        List<Registration> registrations = registrationService.list();
        return registrations;
    }
}
