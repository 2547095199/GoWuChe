package mvpframework.bwie.com.shop;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by CZ on 2017/11/17.
 */
public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.IViewHolder> {


    private Context context;
    private List<ShopBean.OrderDataBean.CartlistBean> list;


    public ShopAdapter(Context context) {
        this.context = context;
    }

    public void add(List<ShopBean.OrderDataBean.CartlistBean> list) {
        if (this.list == null) {
            this.list = new ArrayList<>();
        }
        this.list.addAll(list);
        notifyDataSetChanged();
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
    }

    @Override
    public IViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.shop_adapter, null);
        return new IViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final IViewHolder holder, final int position) {
        holder.checkbox.setChecked(list.get(position).isCheck());
        holder.danjia.setText(list.get(position).getPrice() + "");
        holder.customviewid.setEditText(list.get(position).getCount());
        ImageLoader.getInstance().displayImage(list.get(position).getDefaultPic(), holder.shopface, getOption());

        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.get(position).setCheck(holder.checkbox.isChecked());
                notifyDataSetChanged();

                if (checkBoxListener != null) {
                    checkBoxListener.check(position, holder.customviewid.getCurrentCount(), holder.checkbox.isChecked(), list);
                }
            }
        });
        holder.customviewid.setListener(new CustomView.ClickListener() {
            @Override
            public void click(int count) {
                list.get(position).setCount(count);
                notifyDataSetChanged();
                if (listener != null) {
                    listener.click(count, list);
                }
            }
        });
        holder.shopBtnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                list.remove(position);
                notifyDataSetChanged();


                if (delListener != null) {
                    delListener.del(position, list);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    public class IViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.checkbox)
        CheckBox checkbox;
        @Bind(R.id.shopface)
        ImageView shopface;
        @Bind(R.id.danjia)
        TextView danjia;
        @Bind(R.id.customviewid)
        CustomView customviewid;
        @Bind(R.id.shop_btn_del)
        Button shopBtnDel;

        public IViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public List<ShopBean.OrderDataBean.CartlistBean> getList() {
        return list;
    }

    CheckBoxListener checkBoxListener;

    public void setCheckBoxListener(CheckBoxListener listener) {
        this.checkBoxListener = listener;
    }

    interface CheckBoxListener {
        public void check(int position, int count, boolean check, List<ShopBean.OrderDataBean.CartlistBean> list);
    }

    CustomViewListener listener;

    public void setCustomViewListener(CustomViewListener listener) {
        this.listener = listener;
    }

    interface CustomViewListener {
        public void click(int count, List<ShopBean.OrderDataBean.CartlistBean> list);
    }

    DelListener delListener;

    /**
     * 加减号 删除按钮事件
     *
     * @param listener
     */
    public void setDelListener(DelListener listener) {
        this.delListener = listener;
    }

    interface DelListener {
        public void del(int position, List<ShopBean.OrderDataBean.CartlistBean> list);
    }

    public DisplayImageOptions getOption() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.ic_launcher)//设置图的时片Uri为空或是错误候显示的图片
                .showImageOnFail(R.mipmap.ic_launcher)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
                //.decodingOptions(BitmapFactory.Options decodingOptions)//设置图片的解码配置
                .delayBeforeLoading(0)//int delayInMillis为你设置的下载前的延迟时间
                //设置图片加入缓存前，对bitmap进行设置
                //.preProcessor(BitmapProcessor preProcessor)
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .displayer(new RoundedBitmapDisplayer(20))//不推荐用！！！！是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间，可能会出现闪动
                .build();//构建完成
        return options;

    }
}
