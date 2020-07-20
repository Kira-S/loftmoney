package com.ks.loftmoney;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class AddItemActivity extends AppCompatActivity {
    private TextInputEditText etItem;
    private TextInputEditText etPrice;
    private Button btnAdd;
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
                btnAdd.setTextColor(getApplicationContext().getResources().getColor(R.color.add_button_color_enable));
                Drawable img = getApplicationContext().getResources().getDrawable(R.drawable.ic_arrow_enable);
                btnAdd.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
            } else {
                btnAdd.setEnabled(false);
                btnAdd.setTextColor(getApplicationContext().getResources().getColor(R.color.add_button_color_disable));
                Drawable img = getApplicationContext().getResources().getDrawable(R.drawable.ic_arrow_disable);
                btnAdd.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        etItem = findViewById(R.id.etItem);
        etPrice = findViewById(R.id.etPrice);
        btnAdd = findViewById(R.id.add_button);

        etItem.addTextChangedListener(textWatcher);
        etPrice.addTextChangedListener(textWatcher);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getApplicationContext(), "Я могу нажать на кнопку", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}
