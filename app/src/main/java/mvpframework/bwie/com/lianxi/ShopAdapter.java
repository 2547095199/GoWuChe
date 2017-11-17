package mvpframework.bwie.com.lianxi;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import muhanxi.okhttpdemos.R;
//
//import static com.amap.api.col.s3.dc.G;

/**
 * Created by muhanxi on 17/11/15.
 */

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.IViewHolder> {
    private Context context;
    private List<ShopBean.OrderDataBean.CartlistBean> list ;
    public ShopAdapter(Context context) {
        this.context = context;
    }

    /**
     * 更新数据
     * @param list
     */
    public void add(List<ShopBean.OrderDataBean.CartlistBean> list){
        if(this.list == null){
            this.list = new ArrayList<>();
        }
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public ShopAdapter.IViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.shop_adapter, null);
        return new IViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ShopAdapter.IViewHolder holder, final int position) {


        //防止checkbox 滑动 错乱
        holder.checkBox.setChecked(list.get(position).isCheck());

        holder.danjia.setText(list.get(position).getPrice()+"");

        ImageLoader.getInstance().displayImage(list.get(position).getDefaultPic(),holder.shopface);

        /**
         * checkbox 点击事件
         */
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.get(position).setCheck(holder.checkBox.isChecked());
                notifyDataSetChanged();

                if(checkBoxListener != null){
                    checkBoxListener.check(position,holder.customviewid.getCurrentCount(),holder.checkBox.isChecked(),list);
                }
            }
        });


        /**
         * 加减监听
         */
        holder.customviewid.setListener(new CustomView.ClickListener() {
            @Override
            public void click(int count) {


                //更新数据源
                list.get(position).setCount(count);
                notifyDataSetChanged();

                if(listener != null){
                    listener.click(count,list);
                }


            }
        });

        /**
         * 删除点击事件
         */
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                list.remove(position);
                notifyDataSetChanged();

                if(delListener != null){
                    delListener.del(position,list);
                }


            }
        });




    }





    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    static class IViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBox;
        private ImageView shopface;
        private TextView danjia;
        private CustomView customviewid;
        private Button del;

        IViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }



    public List<ShopBean.OrderDataBean.CartlistBean> getList(){
        return list;
    }

    CheckBoxListener checkBoxListener;

    /**
     * checkbox 点击事件
     * @param listener
     */
    public void setCheckBoxListener(CheckBoxListener listener){
        this.checkBoxListener = listener;
    }
    interface CheckBoxListener {
        public void check(int position, int count, boolean check, List<ShopBean.OrderDataBean.CartlistBean> list);
    }




    CustomViewListener listener;

    /**
     * 加减号 点击事件
     * @param listener
     */
    public void setCustomViewListener(CustomViewListener listener){
        this.listener = listener;
    }
    interface CustomViewListener {
        public void click(int count, List<ShopBean.OrderDataBean.CartlistBean> list);
    }



    DelListener delListener ;
    /**
     * 加减号 删除按钮事件
     * @param listener
     */
    public void setDelListener(DelListener listener) {
        this.delListener = listener ;
    }
    interface DelListener {
        public void del(int position, List<ShopBean.OrderDataBean.CartlistBean> list);
    }


}
