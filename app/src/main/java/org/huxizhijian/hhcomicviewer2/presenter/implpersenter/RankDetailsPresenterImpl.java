package org.huxizhijian.hhcomicviewer2.presenter.implpersenter;

import org.huxizhijian.hhcomicviewer2.app.HHApplication;
import org.huxizhijian.hhcomicviewer2.model.Comic;
import org.huxizhijian.hhcomicviewer2.presenter.IRankDetailsPresenter;
import org.huxizhijian.hhcomicviewer2.presenter.viewinterface.IRankDetailsFragment;
import org.huxizhijian.hhcomicviewer2.utils.HHApiProvider;
import org.huxizhijian.sdk.network.service.NormalRequest;
import org.huxizhijian.sdk.network.service.NormalResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wei on 2017/1/20.
 */

public class RankDetailsPresenterImpl implements IRankDetailsPresenter {

    private IRankDetailsFragment mFragment;

    public RankDetailsPresenterImpl(IRankDetailsFragment fragment) {
        this.mFragment = fragment;
    }

    @Override
    public void getRankList(String url) {
        HHApiProvider.getInstance().getWebContentAsyn(HHApplication.getInstance()
                        .getHHWebVariable().getCsite() + url,
                new NormalResponse<byte[]>() {
                    @Override
                    public void success(NormalRequest request, byte[] data) {
                        String content = null;
                        try {
                            content = new String(data, "utf-8");
                            Document doc = Jsoup.parse(content);
                            Elements comicSrcs = doc.select("div[class=cComicItem]");
                            List<Comic> comics = new ArrayList<>();
                            for (Element comicSrc : comicSrcs) {
                                Comic comic = new Comic();
                                String comicUrl = comicSrc.select("a").first().attr("href");
                                String end = comicUrl.substring(HHApplication.getInstance()
                                        .getHHWebVariable().getPre().length());
                                comic.setCid(Integer.parseInt(end.split("\\.")[0]));
                                comic.setThumbnailUrl(comicSrc.select("img").first().attr("src"));
                                comic.setTitle(comicSrc.select("span[class=cComicTitle]").first().text());
                                comic.setAuthor(comicSrc.select("span[class=cComicAuthor").first().text());
                                comic.setComicStatus(comicSrc.select("span[class=cComicRating").first().text());
                                comics.add(comic);
                            }
                            if (mFragment != null)
                                mFragment.onSuccess(comics);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            if (mFragment != null)
                                mFragment.onException(e);
                        }
                    }

                    @Override
                    public void fail(int errorCode, String errorMsg) {
                        if (mFragment != null)
                            mFragment.onFailure(errorCode, errorMsg);
                    }
                });
    }

    @Override
    public void removeListener() {
        mFragment = null;
    }
}