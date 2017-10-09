package org.huxizhijian.hhcomic.comic.parser.detail;

import org.huxizhijian.core.app.ConfigKeys;
import org.huxizhijian.core.app.HHEngine;
import org.huxizhijian.hhcomic.comic.bean.Chapter;
import org.huxizhijian.hhcomic.comic.bean.Comic;
import org.huxizhijian.hhcomic.comic.parser.BaseParseStrategy;
import org.huxizhijian.hhcomic.comic.value.IHHComicRequest;
import org.huxizhijian.hhcomic.comic.value.IHHComicResponse;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Author huxizhijian on 2017/10/9.
 */

public abstract class DetailStrategy extends BaseParseStrategy {

    // Comic id
    private String mComicId;

    /**
     * 获取请求的网址
     *
     * @param comicId Comic来源网站标识Id
     */
    protected abstract String getDetailUrl(String comicId);

    /**
     * 解析内容
     *
     * @param data 网页请求返回的内容
     * @return Comic
     */
    protected abstract Comic parseComic(byte[] data, String comicId) throws UnsupportedEncodingException;

    /**
     * 是否需要再进行一次网络请求才能获取到Chapter列表
     *
     * @return false不需要，true需要进行网络请求
     */
    protected abstract boolean shouldNotRequestToParseChapter();

    /**
     * 当shouldNotRequestToParseChapter()返回true时才调用，返回章节列表网络请求的Request
     */
    protected abstract Request buildChapterRequest(String comicId);

    /**
     * Chapter网址列表解析
     *
     * @param data 获取到的网页内容
     */
    protected abstract List<Chapter> parseChapter(byte[] data);

    @Override
    public Request buildRequest(IHHComicRequest comicRequest) {
        // 省略，从request中取出cid的操作
        return getRequestGetAndWithUrl(getDetailUrl(mComicId));
    }

    @Override
    public IHHComicResponse parseData(IHHComicResponse comicResponse, byte[] data) throws IOException {
        Comic comic = parseComic(data, mComicId);
        comicResponse.setResponse(comic);
        List<Chapter> chapters = null;
        if (shouldNotRequestToParseChapter()) {
            chapters = parseChapter(data);
        } else {
            Request request = buildChapterRequest(mComicId);
            OkHttpClient client = HHEngine.getConfiguration(ConfigKeys.OKHTTP_CLIENT);
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                byte[] html = response.body().bytes();
                chapters = parseChapter(html);
            }
        }
        if (chapters != null) {
            // 添加
        }
        return comicResponse;
    }

}