package com.example.magicmirror.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
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
//
            ViewHolder holder;
            if (convertView == null){
                holder =new ViewHolder();
                //将布局填充成控件
                convertView = getLayoutInflater().inflate(R.layout.item_gridview,null);
                //获取展示图片的控件对象
                holder.image =(ImageView) convertView.findViewById(R.id.item_pic);
                //获取展示文本的控件对象
                holder.text = (TextView) convertView.findViewById(R.id.item_text);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag(); //根据tag获取holder对象
            }
            setData(holder,position);   //设置控件显示
            return convertView;
        }

        /**
         * 设置数据
         */
        private void setData(ViewHolder holder,int position){
            holder.image.setImageBitmap(bitmaps[position]); //设置图片
            holder.text.setText((photo_name[position]));
        }

        class ViewHolder{
            ImageView image;    //声明图片控件
            TextView text;

        }

    }
    /**
     * item单击事件
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra("POSITION",position);   //设置图片位置数据进行传输
        setResult(RESULT_OK,intent);    //将选取的结果返回给主窗体
        finish();   //关闭窗口
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back_to_main)
            finish();   //关闭窗口
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);   //满屏显示
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_frame);  //获取布局

        //初始化控件
        textView = (TextView) findViewById(R.id.back_to_main);
        gridView = (GridView) findViewById(R.id.photo_frame_list);

        initDatas();    //初始化数据

        textView.setOnClickListener(this);  //设置控件

        PhotoFrameAdapter adapter = new PhotoFrameAdapter();  //创建适配器
        gridView.setAdapter(adapter);   //绑定适配器
        gridView.setOnItemClickListener(this);  //执行子项单击监听事件




    }
}
