package cn.tanghz17.bilivideodata.ui.VideoData;

import android.net.Uri;

import java.net.URL;

class FormatVideoData {
    //稿件bvID
    private String bvid;
    //稿件avID
    private int avid;
    //稿件分P总数
    private int videos;
    //稿件封面图片url
    private Uri pic;
    //稿件标题
    private String title;
    //稿件发布时间
    private String pubdate;
    //用户提交稿件的时间
    private String ctime;
    //视频简介
    private String desc;

    //get&set相关方法
    //bvid
    public String getBvid(){
        return bvid;
    }
    public void setBvid(String str){
        this.bvid=str;
    }
    //avid
    public int getAvid(){
        return avid;
    }
    public void setAvid(int i){
        this.avid=i;
    }
    //稿件分P总数
    public int getVideos(){
        return videos;
    }
    public void setVideos(int i){
        this.videos=i;
    }
    //稿件封面图片url
    public Uri getPic(){
        return pic;
    }
    public void setPic(Uri uri){
        this.pic=uri;
    }
    //稿件标题
    public String getTitle(){
        return title;
    }
    public void setTitle(String str){
        this.title=str;
    }
    //稿件简介
    public String getDesc(){
        return desc;
    }
    public void setDesc(String str){
        this.desc=str;
    }


    //作者信息
    public static class owner{
        private static int mid;        //作者uid
        public int getMid() {
            return mid;
        }
        public void setMid(int i) {
            mid = i;
        }

        private static String name;    //作者名字
        public String getName() {
            return name;
        }
        public void setName(String str) {
            name = str;
        }

        private static String face;    //作者头像
        public String getFace() {
            return face;
        }
        public void setFace(String str) {
            face = str;
        }

    }
    //视频数据信息
    public static class stat{
        private static int view;       //播放数
        public int getView(){
            return view;
        }
        public void setView(int view){
            stat.view=view;
        }

        private static int danmaku;    //弹幕数
        public int getDanmaku() {
            return danmaku;
        }
        public void setDanmaku(int danmaku) {
            stat.danmaku = danmaku;
        }

        private static int reply;      //评论数
        public int getReply() {
            return reply;
        }
        public void setReply(int reply) {
            stat.reply = reply;
        }

        private static int like;       //点赞数
        public int getLike() {
            return like;
        }
        public void setLike(int like) {
            stat.like = like;
        }

        private static int coin;       //硬币数
        public int getCoin() {
            return coin;
        }
        public void setCoin(int coin) {
            stat.coin = coin;
        }

        private static int favorite;   //收藏数
        public int getFavorite() {
            return favorite;
        }
        public void setFavorite(int favorite) {
            stat.favorite = favorite;
        }

        private static int share;      //分享数
        public int getShare() {
            return share;
        }
        public void setShare(int share) {
            stat.share = share;
        }
    }


    @Override
    public String toString() {
        return "VideoData [稿件bvID=" + bvid
                + ", 稿件avID=" + avid
                + ", 稿件标题=" + title
                + ", 作者名字=" + owner.name
                + ", 播放数=" + stat.view
                + ", 弹幕数=" + stat.danmaku
                + ", 评论数=" + stat.reply
                + ", 点赞数=" + stat.like
                + ", 硬币数=" + stat.coin
                + ", 收藏数=" + stat.favorite
                + ", 分享数=" + stat.share
                + "]";
    }
}
