/*
* Copyright 2015 Dotweblabs Web Technologies
*
*   Licensed under the Apache License, Version 2.0 (the "License");
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*   limitations under the License.
*/
package com.dotweblabs.friendscube.rest.services;

import com.dotweblabs.friendscube.app.client.shared.entity.actions.Like;

import java.util.List;
/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
public interface LikeService {
    public Like create(Like like);
    public Like read(Long id);
    public Like update(Like like);
    public void delete(Long id);
    public List<Like> findLikeByCommentId(Long commentId);
    public List<Like> findLikeByStatusId(Long statusId);
    public Like readLikeByCommentId(Long commentId, Long userId);
    public Like readLikeByStatusId(Long statusId, Long userId);
}
