package com.example.selectametalsclad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ProductViewAdapter extends BaseAdapter {
    List<Product> list;
    LayoutInflater layoutInflater;

    public ProductViewAdapter(List<Product> list, Context context) {
        this.list = list;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if(view==null){
            view = layoutInflater.inflate(R.layout.product_view_item,viewGroup,false);
            TextView text = view.findViewById(R.id.txt_select_product);
            Product selectProduct = (Product) getItem(position);
            text.setText(selectProduct.getParam()
                    +", место : "+selectProduct.getLocation() + ", кол-во "+selectProduct.getTotal()+" "+selectProduct.getUnit());

        }

        return view;
    }
}
