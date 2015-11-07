package com.dotweblabs.friendscube.app.client.local.storage;

import com.dotweblabs.friendscube.app.client.shared.entity.User;
import com.seanchenxi.gwt.storage.client.StorageKey;
import com.seanchenxi.gwt.storage.client.StorageKeyProvider;
/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
public interface FriendsStorageKeyProvider extends StorageKeyProvider {
    StorageKey<User> getUserKey();
}
