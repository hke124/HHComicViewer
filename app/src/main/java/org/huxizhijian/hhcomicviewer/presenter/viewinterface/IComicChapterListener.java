/*
 * Copyright 2017 huxizhijian
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.huxizhijian.hhcomicviewer.presenter.viewinterface;

import android.support.annotation.UiThread;

import org.huxizhijian.hhcomicviewer.model.ComicChapter;

/**
 * @date  2017/1/7.
 */
public interface IComicChapterListener {

    @UiThread
    void onSuccess(ComicChapter comicChapter);

    @UiThread
    void onException(Throwable e, ComicChapter comicChapter);

    @UiThread
    void onFail(int errorCode, String errorMsg, ComicChapter comicChapter);
}
