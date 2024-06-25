package com.example.magicmirror.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.magicmirror.R;

public class PhotoFrameActivity extends AppCompatActivity
        implements View.OnClickListener, AdapterView.OnItemClickListener {
    private GridView gridView;  //镜框网格
    private TextView textView;  //返回键
    private int[] photo_styles; //图片的数组
    private String[] photo_name;    //图片的名称数组
    private Bitmap[] bitmaps; //镜框的集合

    private void initDatas(){
        //图片样式
        photo_styles = new int[]{R.mipmap.mag_0001,R.mipmap.mag_0003,R.mipmap.mag_0005,
                R.mipmap.mag_0006,R.mipmap.mag_0007,R.mipmap.mag_0008,R.mipmap.mag_0009,
                R.mipmap.mag_0011,R.mipmap.mag_0012,R.mipmap.mag_0014};

        //图片名称
        photo_name=new String[]{"Beautiful","Special","Wishes","Forever","Journey","Love","River","Wonderful","Birthday","Nice"};

        bitmaps = new Bitmap[photo_styles.length];  //新建图片对象
        for (int i = 0; i < photo_styles.length; i++) {
            //获取图片
            Bitmap bitmap= BitmapFactory.decodeResource(getResources(),photo_styles[i]);
            bitmaps[i] =bitmap;
        }

    }

    class PhotoFrameAdapter extends BaseAdapter{
        /**
         * 获取item数量
         * @return
         */
        @Override
        public int getCount() {
            return photo_name.length;
        }

        /**
         * 获取item
         * @param position Position of the item whose data we want within the adapter's
         * data set.
         * @return
         */
        @Override
        public Object getItem(int position) {
            return photo_name[position];    //返回position位置的图片对象
        }

        /**
         * 获取图片id
         * @param position The position of the item within the adapter's data set whose row id we want.
         * @return
         */
        @Override
        public long getItemId(int position) {
            return position;    //返回图片位置position
        }

        /**
         * 获取item对象
         * @param position The position of the item within the adapter's data set of the item whose view
         *        we want.
         * @param convertView The old view to reuse, if possible. Note: You should check that this view
         *        is non-null and of an appropriate type before using. If it is not possible to convert
         *        this view to display the correct data, this method can create a new view.
         *        Heterogeneous lists can specify their number of view types, so that this View is
         *        always of the right type (see {@link #getViewTypeCount()} and
         *        {@link #getItemViewType(int)}).
         * @param parent The parent that this view will eventually be attached to
         * @return
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
//            未确定
            RecyclerView.ViewHolder holder;
            if (convertView == null){
                holder =new RecyclerView.ViewHolder();
            }
            return null;
        }
    }




















}
