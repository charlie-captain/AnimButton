### 前言

以前有接触过这个,不过也只是看到github上面的,看起来很炫酷,最近有看动画这一方面,所以突然兴致大发, 简简单单实现一个自定义的View, 纯动画, 没有什么需要计算的地方.

### 效果图

![img](https://github.com/thatnight/AnimButton/raw/master/Animation.gif)


### 使用方法

1. 在app/build.gradle中添加下面一行代码: 
    ```
    dependencies {
        compile 'com.example.thatnight:animbutton:1.6'
    }
    ```

2. layout.xml中

    ```
    <com.example.animbutton.AnimButton
            android:id="@+id/rl"
            android:layout_width="match_parent"
            android:layout_height="@dimen/button_height"
            app:color_normal="@color/colorPrimary"		//正常button颜色
            app:color_pressed="@color/colorPrimaryDark"	//按下button颜色
            app:color_progress="@color/colorPrimaryDark"//进度条颜色
            app:color_text="@color/white"			   //字体颜色		
            app:button_radius="0"					  //button圆角
            app:duration="300"             //动画时长
            app:start_text="Login"         //默认字符串
            app:end_text="Error"           //错误字符串
            app:size_text="15sp"           //字体大小
            />
    ```

3.  activity中

    ```
    private AnimButton mButton;
    private Button mBtnChange;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = (AnimButton) findViewById(R.id.rl);
        mBtnChange = (Button) findViewById(R.id.btn_change);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	//do sth
            }
        });
        
        mBtnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButton.setNormalColor(Color.GREEN);
                mButton.setPressedColor(Color.BLUE);
                mButton.setPressedColor(Color.BLACK);
                mButton.setDuration(3000);
                mButton.setStartText("改变了");
                mButton.setEndText("改变错误");
            }
        });
    }
    ```

    就跟原先的Button一样的使用方法.很简单吧.


### 版本记录

- #### 1.6 
    - 开放更多设置（字体颜色，时长，控件颜色）
    - 向上兼容
- #### 1.5

  更新点击事件无需再调用startAnim()方法.
