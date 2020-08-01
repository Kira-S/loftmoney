package com.ks.loftmoney;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class AddItemActivity extends AppCompatActivity {
    private TextInputEditText etItem;
    private TextInputEditText etPrice;
    private Button btnAdd;
    private TextView loadingView;

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {

            if (etItem.getText() != null && etItem.getText().toString().trim().length() > 0 && etPrice.getText() != null && etPrice.getText().toString().trim().length() > 0) {
                btnAdd.setEnabled(true);
            } else {
                btnAdd.setEnabled(false);
            }
        }
    };

    private void setUI(boolean isLoading) {
        etItem.setEnabled(!isLoading);
        etPrice.setEnabled(!isLoading);
        btnAdd.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        loadingView.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }


    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        etItem = findViewById(R.id.etItem);
        etPrice = findViewById(R.id.etPrice);
        btnAdd = findViewById(R.id.add_button);
        loadingView = findViewById(R.id.loading_view);

        etItem.addTextChangedListener(textWatcher);
        etPrice.addTextChangedListener(textWatcher);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etItem.getText().toString();
                String price = etPrice.getText().toString();
                setUI(true);

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(price)) {
                    setResult(RESULT_OK,
                            new Intent().putExtra("name", name).putExtra("price", price));
                    finish();
                }
            }
        });
    }
}
