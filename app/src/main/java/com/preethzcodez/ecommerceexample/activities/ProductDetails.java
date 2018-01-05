package com.preethzcodez.ecommerceexample.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.preethzcodez.ecommerceexample.R;
import com.preethzcodez.ecommerceexample.database.DB_Handler;
import com.preethzcodez.ecommerceexample.pojo.Product;

import org.apmem.tools.layouts.FlowLayout;

import java.util.List;

/**
 * Created by Preeth on 1/3/2018.
 */

public class ProductDetails extends AppCompatActivity {

    Product product;
    DB_Handler db_handler;

    String selectedSize = null;
    String selectedColor = null;
    String selectedItemPrice = null;

    LinearLayout colorParentLay;
    FlowLayout colorsLay;
    TextView Price;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);

        // Get Product Id
        int id = getIntent().getIntExtra("ProductId", 0);

        // Get Product Details By Id
        db_handler = new DB_Handler(this);
        product = db_handler.getProductDetailsById(id);

        setIds();
        setValues();
    }

    // Set Ids
    private void setIds() {
        colorParentLay = (LinearLayout) findViewById(R.id.colorParentLay);
        colorsLay = (FlowLayout) findViewById(R.id.colorsLay);
        Price = (TextView) findViewById(R.id.price);
    }

    // Set Values
    private void setValues() {
        // Title
        TextView Title = (TextView) findViewById(R.id.title);
        Title.setText(product.getName());

        // Size
        LinearLayout SizeParentLay = (LinearLayout) findViewById(R.id.sizeParentLay);
        FlowLayout SizeLay = (FlowLayout) findViewById(R.id.sizesLay);
        try {
            List<String> sizeList = db_handler.getSizeByProductId(product.getId());
            if (sizeList.size() > 0) {
                for (int i = 0; i < sizeList.size(); i++) {
                    final TextView size = new TextView(this);
                    size.setText(sizeList.get(i));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        size.setBackground(getResources().getDrawable(R.drawable.border_grey));
                    } else {
                        size.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_grey));
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        size.setTextColor(getResources().getColor(R.color.black, null));
                    } else {
                        size.setTextColor(getResources().getColor(R.color.black));
                    }
                    size.setFocusableInTouchMode(false);
                    size.setFocusable(true);
                    size.setClickable(true);
                    size.setTextSize(16);

                    int dpValue = 8; // margin in dips
                    float d = getResources().getDisplayMetrics().density;
                    int margin = (int) (dpValue * d); // margin in pixels
                    FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT,
                            FlowLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(margin, margin, 0, 0);
                    size.setLayoutParams(params);
                    SizeLay.addView(size);

                    // Size Click Listener
                    size.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            TextView textView = (TextView) view;
                            selectedSize = textView.getText().toString();
                            selectedColor = null;
                            selectedItemPrice = null;

                            // Get Color of Selected Size & Set Color Layout
                            List<String> colorList = db_handler.getColorBySelectedSize(product.getId(), selectedSize);
                            setColorLayout(colorList);
                        }
                    });
                }
            } else {
                SizeParentLay.setVisibility(View.GONE);
                selectedSize = "-";
            }
        } catch (NullPointerException e) {
            SizeParentLay.setVisibility(View.GONE);
            selectedSize = "-";
        }

        // Color
        List<String> colorList = db_handler.getProductColorsById(product.getId());
        setColorLayout(colorList);

        // Price Range
        Price.setText(db_handler.getProductPriceRangeById(product.getId()));
    }

    private void setColorLayout(List<String> colorList) {
        colorsLay.removeAllViews();
        try {
            if (colorList.size() > 0) {
                for (int i = 0; i < colorList.size(); i++) {
                    final TextView color = new TextView(this);
                    color.setText(colorList.get(i));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        color.setBackground(getResources().getDrawable(R.drawable.border_grey));
                    } else {
                        color.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_grey));
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        color.setTextColor(getResources().getColor(R.color.black, null));
                    } else {
                        color.setTextColor(getResources().getColor(R.color.black));
                    }
                    color.setFocusableInTouchMode(false);
                    color.setFocusable(true);
                    color.setClickable(true);
                    color.setTextSize(16);

                    int dpValue = 8; // margin in dips
                    float d = getResources().getDisplayMetrics().density;
                    int margin = (int) (dpValue * d); // margin in pixels
                    FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT,
                            FlowLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(margin, margin, 0, 0);
                    color.setLayoutParams(params);
                    colorsLay.addView(color);

                    // Size Click Listener
                    color.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            TextView textView = (TextView) view;
                            selectedColor = textView.getText().toString();

                            // Get Selected Item Price
                            if (selectedSize.equals("-")) { // no size for product
                                selectedItemPrice = db_handler.getProductPrice(product.getId(), null, selectedColor);
                                Price.setText(selectedItemPrice);
                            } else if (selectedSize != null) {
                                selectedItemPrice = db_handler.getProductPrice(product.getId(), selectedSize, selectedColor);
                                Price.setText(selectedItemPrice);
                            } else {
                                Toast.makeText(getApplicationContext(), "Please Select Size", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            } else {
                colorParentLay.setVisibility(View.GONE);
            }
        } catch (NullPointerException e) {
            colorParentLay.setVisibility(View.GONE);
        }
    }
}