package kr.hs.emirim.choi.firebaseStart.realtimedb;

public class MemoItem {
    private String user;
    private String title;
    private String memocontents;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMemocontents() {
        return memocontents;
    }

    public void setMemocontents(String memocontents) {
        this.memocontents = memocontents;
    }
}