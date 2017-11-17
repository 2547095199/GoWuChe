package mvpframework.bwie.com.shop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShopActivity extends AppCompatActivity {

    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.allselect)
    CheckBox allselect;
    @Bind(R.id.totalprice)
    TextView totalprice;
    @Bind(R.id.totalnum)
    TextView totalnum;
    @Bind(R.id.submit)
    TextView submit;
    @Bind(R.id.pay_linear)
    LinearLayout payLinear;
    private List<ShopBean.OrderDataBean.CartlistBean> mAllOrderList = new ArrayList<>();
    private ShopAdapter adapter;
    private LinearLayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        ButterKnife.bind(this);
        getData();
        allselect.setTag(1);
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new ShopAdapter(this);
        recyclerview.setLayoutManager(manager);
        recyclerview.setAdapter(adapter);
        adapter.add(mAllOrderList);

        adapter.setCheckBoxListener(new ShopAdapter.CheckBoxListener() {
            @Override
            public void check(int position, int count, boolean check, List<ShopBean.OrderDataBean.CartlistBean> list) {
                sum(list);
            }
        });
        adapter.setCustomViewListener(new ShopAdapter.CustomViewListener() {
            @Override
            public void click(int count, List<ShopBean.OrderDataBean.CartlistBean> list) {
                sum(list);
            }
        });
        adapter.setDelListener(new ShopAdapter.DelListener() {
            @Override
            public void del(int position, List<ShopBean.OrderDataBean.CartlistBean> list) {
                sum(list);
            }
        });

    }

    float price = 0;
    int count;

    private void sum(List<ShopBean.OrderDataBean.CartlistBean> mAllOrderList) {
        //刚开始把数据化为0
        price = 0;
        count = 0;
        boolean allCheck = true;
        for (ShopBean.OrderDataBean.CartlistBean bean : mAllOrderList) {
            if (bean.isCheck()) {
                price += bean.getPrice() * bean.getCount();
                count += bean.getCount();
            } else {
                allCheck = false;
            }
        }
        //总价价格
        totalprice.setText("总价：" + price);
        //总数量
        totalnum.setText("共:" + count + "件商品");
        //判断点击
        if (allCheck) {
            allselect.setTag(2);
            allselect.setChecked(true);
        } else {
            allselect.setTag(1);
            allselect.setChecked(false);
        }

    }
    //gson解析
    private void getData() {
        try {
            InputStream inputStream = getAssets().open("shop.json");
            String data = convertStreamToString(inputStream, "utf-8");
            Gson gson = new Gson();
            ShopBean shopBean = gson.fromJson(data, ShopBean.class);
            for (int i = 0; i < shopBean.getOrderData().size(); i++) {
                int length = shopBean.getOrderData().get(i).getCartlist().size();
                for (int j = 0; j < length; j++) {
                    mAllOrderList.add(shopBean.getOrderData().get(i).getCartlist().get(j));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String convertStreamToString(InputStream inputStream, String s) {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder builder = new StringBuilder();
            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }
            bufferedReader.close();
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //默认为没选中
    boolean select = false;
    //全选点击事件
    @OnClick(R.id.allselect)
    public void onClick() {
        int tag = (Integer) allselect.getTag();
        //未点击为1     点击为2
        if (tag == 1) {
            allselect.setTag(2);
            select = true;
        } else {
            allselect.setTag(1);
            select = false;
        }
        //全选把数据全都加起来
        for (ShopBean.OrderDataBean.CartlistBean bean : mAllOrderList) {
            bean.setCheck(select);
        }
        //适配器刷新
        adapter.notifyDataSetChanged();
        sum(adapter.getList());
    }
}
