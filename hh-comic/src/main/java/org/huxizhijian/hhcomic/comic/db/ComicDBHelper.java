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

package org.huxizhijian.hhcomic.comic.db;

import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;
import org.huxizhijian.core.app.HHEngine;
import org.huxizhijian.hhcomic.comic.bean.Comic;
import org.huxizhijian.hhcomic.comic.bean.ComicDao;
import org.huxizhijian.hhcomic.comic.bean.DaoMaster;
import org.huxizhijian.hhcomic.comic.bean.DaoSession;

import java.util.List;

/**
 * 工具类
 *
 * @author huxizhijian 2017/10/10
 */
public class ComicDBHelper implements IComicDBHelper {

    private static ComicDao sComicDao;

    private ComicDBHelper() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(HHEngine.getApplicationContext(), "comic_db");
        Database db = helper.getWritableDb();
        DaoSession daoSession = new DaoMaster(db).newSession();
        sComicDao = daoSession.getComicDao();
    }

    public static ComicDBHelper getInstance() {
        return Holder.INSTANCE;
    }

    private static final class Holder {
        private static final ComicDBHelper INSTANCE = new ComicDBHelper();
    }

    @Override
    public synchronized void insert(Comic comic) {
        sComicDao.insert(comic);
    }

    @Override
    public synchronized void update(Comic comic) {
        sComicDao.update(comic);
    }

    @Override
    public synchronized void delete(Comic comic) {
        sComicDao.delete(comic);
    }

    @Override
    public synchronized Comic get(int source, String comicId) {
        QueryBuilder<Comic> qb = sComicDao.queryBuilder();
        qb.and(ComicDao.Properties.Source.eq(source), ComicDao.Properties.Cid.eq(comicId));
        return qb.list().get(0);
    }

    @Override
    public synchronized List<Comic> getAll() {
        return sComicDao.queryBuilder()
                .list();
    }

    @Override
    public List<Comic> getHistoryList(int order) {
        QueryBuilder<Comic> qb = sComicDao.queryBuilder();
        qb.where(ComicDao.Properties.History.notEq(null));
        order(order, qb, ComicDao.Properties.History);
        return qb.list();
    }

    @Override
    public List<Comic> getFavoriteList(int order, boolean highLight) {
        QueryBuilder<Comic> qb = sComicDao.queryBuilder();
        qb.where(ComicDao.Properties.Favorite.notEq(null));
        if (highLight) {
            // 将有高亮标记的排在前面
            qb.orderDesc(ComicDao.Properties.Highlight);
        }
        order(order, qb, ComicDao.Properties.Favorite);
        return qb.list();
    }

    @Override
    public List<Comic> getDownloadList(int order) {
        QueryBuilder<Comic> qb = sComicDao.queryBuilder();
        qb.where(ComicDao.Properties.Download.notEq(null));
        order(order, qb, ComicDao.Properties.Download);
        return qb.list();
    }

    private void order(int order, QueryBuilder<Comic> qb, Property property) {
        switch (order) {
            case ORDER_LAST_ASC:
                qb.orderAsc(ComicDao.Properties.Last);
                break;
            case ORDER_LAST_DESC:
                qb.orderDesc(ComicDao.Properties.Last);
                break;
            case ORDER_UPDATE_ASC:
                qb.orderAsc(ComicDao.Properties.Update);
                break;
            case ORDER_UPDATE_DESC:
                qb.orderDesc(ComicDao.Properties.Update);
                break;
            case ORDER_CORRESPOND_ASC:
                qb.orderAsc(property);
                break;
            case ORDER_CORRESPOND_DESC:
                qb.orderDesc(property);
            default:
                break;
        }
    }

}
