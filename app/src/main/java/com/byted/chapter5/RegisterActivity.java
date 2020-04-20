package com.byted.chapter5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        EditText eName = findViewById(R.id.name);
        EditText ePassword = findViewById(R.id.password);
        EditText eRepassword = findViewById(R.id.repassword);
        Button register = findViewById(R.id.register);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://wanandroid.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = eName.getText().toString();
                String password = ePassword.getText().toString();
                String repassword = eRepassword.getText().toString();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(password) || TextUtils.isEmpty(repassword)) {
                    Toast.makeText(RegisterActivity.this, "参数不合法", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!TextUtils.equals(password, repassword)) {
                    Toast.makeText(RegisterActivity.this, "密码不相等", Toast.LENGTH_SHORT).show();
                    return;
                }
                // todo 做网络请求
                apiService.postRegist(name, password, repassword).enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        if(response.body() != null && response.body().user != null){
                            Toast.makeText(RegisterActivity.this, "注册成功!" + "\n"
                                    + "您的用户名为："+ response.body().user.username
                                    + "\n" + "ID为：" + response.body().user.id.toString(), Toast.LENGTH_SHORT).show();
                        }
                        else if(response.body() != null){
                            Toast.makeText(RegisterActivity.this, "注册失败：" + response.body().errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        Toast.makeText(RegisterActivity.this, "当前网络错误：" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
