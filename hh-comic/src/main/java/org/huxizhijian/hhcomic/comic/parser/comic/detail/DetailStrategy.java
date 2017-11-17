package org.huxizhijian.hhcomic.comic.parser.comic.detail;

import org.huxizhijian.core.app.ConfigKeys;
import org.huxizhijian.core.app.HHEngine;
import org.huxizhijian.hhcomic.comic.bean.Chapter;
import org.huxizhijian.hhcomic.comic.bean.Comic;
import org.huxizhijian.hhcomic.comic.parser.comic.BaseComicParseStrategy;
import org.huxizhijian.hhcomic.comic.type.RequestFieldType;
import org.huxizhijian.hhcomic.comic.type.ResponseFieldType;
import org.huxizhijian.hhcomic.comic.value.IComicRequest;
import org.huxizhijian.hhcomic.comic.value.IComicResponse;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author huxizhijian
 * @date 2017/10/9.
 */

public abstract class DetailStrategy extends BaseComicParseStrategy {

    /**
     * Comic id
     */
    protected String mComicId;

    /**
     * 获取请求的网址
     *
     * @param comicId Comic来源网站标识Id
     * @return 网址
     */
    protected abstract String getDetailUrl(String comicId);

    /**
     * 解析内容
     *
     * @param data    网页请求返回的内容
     * @param comicId comic网站标识
     * @return Comic
     * @throws UnsupportedEncodingException 解析时可能会抛出的异常
     */
    protected abstract Comic parseComic(byte[] data, String comicId) throws UnsupportedEncodingException;

    /**
     * 是否需要再进行一次网络请求才能获取到Chapter列表
     *
     * @return false不需要，true需要进行网络请求
     */
    protected abstract boolean needMoreRequestGetChapterList();

    /**
     * 当shouldNotRequestToParseChapter()返回true时才调用，返回章节列表网络请求的Request
     *
     * @param comicId ComicId
     * @return request
     */
    protected abstract Request buildChapterListRequest(String comicId);

    /**
     * Chapter网址列表解析
     *
     * @param data 获取到的网页内容
     * @return result
     * @throws UnsupportedEncodingException 异常
     */
    protected abstract List<Chapter> parseChapter(byte[] data) throws UnsupportedEncodingException;

    @Override
    public Request buildRequest(IComicRequest comicRequest) throws NullPointerException {
        // 取出cid，该值为必要
        if (comicRequest.getField(RequestFieldType.COMIC_ID) == null) {
            throw new NullPointerException("comic_id should not be null!");
        }
        mComicId = comicRequest.getField(RequestFieldType.COMIC_ID);
        return getRequestGetAndWithUrl(getDetailUrl(mComicId));
    }

    @Override
    public IComicResponse parseData(IComicResponse comicResponse, byte[] data) throws IOException, NullPointerException {
        Comic comic = parseComic(data, mComicId);
        // 添加返回结果
        comicResponse.setComicResponse(comic);
        List<Chapter> chapters = null;
        if (needMoreRequestGetChapterList()) {
            chapters = parseChapter(data);
        } else {
            Request request = buildChapterListRequest(mComicId);
            OkHttpClient client = HHEngine.getConfiguration(ConfigKeys.OKHTTP_CLIENT);
            Response response = client.newCall(request).execute();
            if (response.isSuccessful() && response.code() > 199 && response.code() < 300) {
                byte[] html = response.body().bytes();
                chapters = parseChapter(html);
            } else {
                throw new IOException("OKHttp connect no successful");
            }
        }
        if (chapters != null) {
            // 添加结果
            comicResponse.addField(ResponseFieldType.CHAPTER_LIST, chapters);
            comic.setChapterCount(chapters.size());
        }
        return comicResponse;
    }

}
