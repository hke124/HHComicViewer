package org.huxizhijian.hhcomicviewer2.vo;

import org.huxizhijian.hhcomicviewer2.utils.ParsePicUrlList;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.ArrayList;

/**
 * 章节实体类，主要保存下载状态
 * Created by wei on 2016/8/23.
 */
@Table(name = "download")
public class ComicCapture {
    @Column(name = "id", isId = true)
    private int id; //id
    @Column(name = "capture_name")
    private String captureName; //章节名
    @Column(name = "capture_url")
    private String captureUrl; //章节的url
    @Column(name = "download_status")
    private int downloadStatus; //下载状态
    @Column(name = "page_count")
    private int pageCount; //章节的页数
    @Column(name = "download_progress")
    private int downloadProgress; //下载完成的页数
    @Column(name = "comic_title")
    private String comicTitle; //漫画名

    private ArrayList<String> picList;

    public ArrayList<String> getPicList() {
        return picList;
    }

    public void setPicList(ArrayList<String> picList) {
        this.picList = picList;
    }

    public ComicCapture() {
    }

    public ComicCapture(String url, String content) {
        this.picList = ParsePicUrlList.scanPicInPage(url, content);
        this.captureUrl = url;
        this.pageCount = picList.size();
    }

    public ComicCapture(String comicTitle, String captureName, String captureUrl) {
        this.comicTitle = comicTitle;
        this.captureName = captureName;
        this.captureUrl = captureUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCaptureName() {
        return captureName;
    }

    public void setCaptureName(String captureName) {
        this.captureName = captureName;
    }

    public String getCaptureUrl() {
        return captureUrl;
    }

    public void setCaptureUrl(String captureUrl) {
        this.captureUrl = captureUrl;
    }

    public int getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(int downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getDownloadProgress() {
        return downloadProgress;
    }

    public void setDownloadProgress(int downloadProgress) {
        this.downloadProgress = downloadProgress;
    }

    public String getComicTitle() {
        return comicTitle;
    }

    public void setComicTitle(String comicTitle) {
        this.comicTitle = comicTitle;
    }

    @Override
    public String toString() {
        return "ComicCapture{" +
                "id=" + id +
                ", captureName='" + captureName + '\'' +
                ", captureUrl='" + captureUrl + '\'' +
                ", downloadStatus=" + downloadStatus +
                ", pageCount=" + pageCount +
                ", downloadProgress=" + downloadProgress +
                ", comicTitle='" + comicTitle + '\'' +
                '}';
    }
}
