
package com.example.administrator.mylifelogger;

        import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2016-12-15.
 */
public class LIstViewGoal {
    private Drawable iconDrawable ;
    private String titleStr ;
    private String descStr ;
    private int Listid;

    public void setIcon(Drawable icon) {
        iconDrawable = icon ;
    }
    public void setTitle(String title) {
        titleStr = title ;
    }
    public void setDesc(String desc) {
        descStr = desc ;
    }
    public void setId(int Id) {
        Listid = Id ;
    }

    public Drawable getIcon() {
        return this.iconDrawable ;
    }
    public  String getTitle() {
        return this.titleStr ;
    }
    public  String getDesc() {
        return this.descStr ;
    }
    public int getListid() { return this.Listid; }
}
