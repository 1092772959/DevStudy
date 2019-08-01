## Android 学习笔记

### 一、主要文件

#### 活动文件

```java
package com.example.helloworld;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

public class MainActivity extends Activity {

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.activity_main, menu);
      return true;
   }
}
```

这里，R.layout.activity_main引用自res/layout目录下的activity_main.xml文件。onCreate()是活动被加载之后众多被调用的方法之一。

#### Manifest文件

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
   package="com.example.helloworld"
   android:versionCode="1"
   android:versionName="1.0" >

   <uses-sdk
      android:minSdkVersion="8"
      android:targetSdkVersion="22" />

   <application
       android:icon="@drawable/ic_launcher"
       android:label="@string/app_name"
       android:theme="@style/AppTheme" >

       <activity
          android:name=".MainActivity"
          android:label="@string/title_activity_main" >

          <intent-filter>
             <action android:name="android.intent.action.MAIN" />
             <category android:name="android.intent.category.LAUNCHER"/>
          </intent-filter>

       </activity>
   </application>
</manifest>
```

标签之间是组件。

intent-filter：意图过滤器的action被命名为android.intent.action.MAIN，表明这个活动被用做应用程序的入口。意图过滤器的category被命名为android.intent.category.LAUNCHER，表明应用程序可以通过设备启动器的图标来启动。

#### Layout文件

activity_main.xml是一个在res/layout目录下的layout文件。当应用程序构建它的界面时被引用。修改这个文件来改变应用程序的布局。

```xml
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
   xmlns:tools="http://schemas.android.com/tools"
   android:layout_width="match_parent"
   android:layout_height="match_parent" >

   <TextView
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"
      android:layout_centerHorizontal="true"
      android:layout_centerVertical="true"
      android:padding="@dimen/padding_medium"
      android:text="@string/hello_world"
      tools:context=".MainActivity" />

</RelativeLayout>
```



### 二、资源访问

除了应用程序的编码，你需要关注各种各样的资源，诸如你用到的各种静态内容，如位图，颜色，布局定义，用户界面字符串，动画等等。这些资源一般放置在项目的 res/ 下独立子目录中。

| anim/     | 定义动画属性的XML文件。它们被保存在res/anim/文件夹下，通过R.anim类访问 |
| --------- | ------------------------------------------------------------ |
| color/    | 定义颜色状态列表的XML文件。它们被保存在res/color/文件夹下，通过R.color类访问 |
| drawable/ | 图片文件，如.png,.jpg,.gif或者XML文件，被编译为位图、状态列表、形状、动画图片。它们被保存在res/drawable/文件夹下，通过R.drawable类访问 |
| layout/   | 定义用户界面布局的XML文件。它们被保存在res/layout/文件夹下，通过R.layout类访问 |
| menu/     | 定义应用程序菜单的XML文件，如选项菜单，上下文菜单，子菜单等。它们被保存在res/menu/文件夹下，通过R.menu类访问 |
| raw/      | 任意的文件以它们的原始形式保存。需要根据名为R.raw.filename的资源ID，通过调用Resource.openRawResource()来打开raw文件 |
| values/   | 包含简单值(如字符串，整数，颜色等)的XML文件。这里有一些文件夹下的资源命名规范。arrays.xml代表数组资源，通过R.array类访问；integers.xml代表整数资源，通过R.integer类访问；bools.xml代表布尔值资源，通过R.bool类访问；colors.xml代表颜色资源，通过R.color类访问；dimens.xml代表维度值，通过R.dimen类访问；strings.xml代表字符串资源，通过R.string类访问；styles.xml代表样式资源，通过R.style类访问 |
| xml/      | 可以通过调用Resources.getXML()来在运行时读取任意的XML文件。可以在这里保存运行时使用的各种配置文件 |

#### Java中访问资源

访问资源：当 Android 应用程序被编译，生成一个 R 类，其中包含了所有 res/ 目录下资源的 ID。你可以使用 R 类，通过子类+资源名或者直接使用资源 ID 来访问资源。访问 res/drawable/myimage.png，并将其设置到 ImageView 上，你将使用以下代码：

```java
ImageView imageView = (ImageView) findViewById(R.id.myimageview);
imageView.setImageResource(R.drawable.myimage);
```

第一行代码用 R.id.myimageview 来在布局文件中获取定义为 myimageview 的 ImageView。第二行用 R.drawable.myimage 来获取在 res/ 的 drawable 子目录下名为 myimage 的图片。

为Activity加载布局，使用onCreate() 方法：

```java
public void onCreate(Bundle savedInstanceState) {
   super.onCreate(savedInstanceState);
   setContentView(R.layout.main_activity);
}
```

#### 在XML中访问资源

资源文件：

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
   <color name="opaque_red">#f00</color>
   <string name="hello">Hello!</string>
</resources>
```

布局文件中：

```xml
<?xml version="1.0" encoding="utf-8"?>
<EditText xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="fill_parent"
    android:layout_height="fill_parent"
    android:textColor="@color/opaque_red"
    android:text="@string/hello" />
```

### 三、布局

Android中有六大布局,分别是: LinearLayout(线性布局)，RelativeLayout(相对布局)，TableLayout(表格布局) FrameLayout(帧布局)，AbsoluteLayout(绝对布局)，GridLayout(网格布局) 。

#### 1. LinearLayout(线性布局)

![img](https://www.runoob.com/wp-content/uploads/2015/07/15116314.jpg)

##### weight属性

layout_weight:是布局文件的一个属性，它的值表示线性分割原本应有长度的权重，要和wrap_content和match_parent配合使用。wrap_content：表示和自身内容一样的长度。match_parent：表示和父组件一样的长度。

和0dp配合：将layout_weight或者layout_height设为0dp，将直接按照layout_weight权重的比例分配空间，且不会被内容撑大。

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"    
    xmlns:tools="http://schemas.android.com/tools"    
    android:id="@+id/LinearLayout1"    
    android:layout_width="match_parent"    
    android:layout_height="match_parent"    
    android:orientation="horizontal">   
        
    <LinearLayout    
        android:layout_width="0dp"    
        android:layout_height="fill_parent"    
        android:background="#ADFF2F"     
        android:layout_weight="1"/>    
       
        
    <LinearLayout    
        android:layout_width="0dp"    
        android:layout_height="fill_parent"    
        android:background="#DA70D6"     
        android:layout_weight="2"/>    
        
</LinearLayout>  
```

归纳: 按比例划分水平方向:将涉及到的View的android:width属性设置为**0dp**,然后设置为android weight属性设置比例即可;类推,竖直方向,只需设android:height为0dp,然后设weight属性即可。

如果我们不适用上述那种设置为0dp的方式,直接用wrap_content和match_parent的话, 则要接着解析weight属性了,分为两种情况,wrap_content与match_parent。

和wrap_content配合：先按照内容的多少去设定空间大小，然后按照权重的比例分配剩余控件。即当控件没有内容或内容未超出按照权重比例分配的空间时，就按照layout_weight设定的权重比例分配空间，当内容大小超过这样分配的空间时，控件就会扩张，其实就是按照wrap_content来占用空间了，剩下的空间仍然按照本段定理来分配。

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"    
    xmlns:tools="http://schemas.android.com/tools"    
    android:id="@+id/LinearLayout1"    
    android:layout_width="match_parent"    
    android:layout_height="match_parent"  
    android:orientation="horizontal" >    
    
    <TextView    
        android:layout_weight="1"    
        android:layout_width="wrap_content"    
        android:layout_height="fill_parent"    
        android:text="one"     
        android:background="#98FB98"    
     />    
     <TextView    
        android:layout_weight="2"    
        android:layout_width="wrap_content"    
        android:layout_height="fill_parent"    
        android:text="two"     
        android:background="#FFFF00"    
     />    
     <TextView    
        android:layout_weight="3"    
        android:layout_width="wrap_content"    
        android:layout_height="fill_parent"    
        android:text="three"     
        android:background="#FF00FF"    
     />    
    
</LinearLayout>  
```

![img](https://www.runoob.com/wp-content/uploads/2015/07/2036364.jpg)

*区别就在于，wrap_content在内容超过分配的比例后布局会继续扩张；而0dp的话即使超过也不会继续扩张。

##### orientation/gravity

orientation：排列方向 。vertical表上下方向堆，horizontal表左右方向堆。

gravity：对齐方式。

当 android:orientation="vertical" 时， gravity只有水平方向的设置才起作用，垂直方向的设置不起作用。 即：left，right，center_horizontal 是生效的。 当 android:orientation="horizontal" 时， 只有垂直方向的设置才起作用，水平方向的设置不起作用。 即：top，bottom，center_vertical 是生效的。

##### 分界线

1）原生线条

```xml
<View  
    android:layout_width="match_parent"  
    android:layout_height="1px"  
    android:background="#000000" />  
```

2）divider属性

使用LinearLayout的一个divider属性

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"  
    xmlns:tools="http://schemas.android.com/tools"  
    android:id="@+id/LinearLayout1"  
    android:layout_width="match_parent"  
    android:layout_height="match_parent"  
    android:divider="@drawable/ktv_line_div"  
    android:orientation="vertical"  
    android:showDividers="middle"  
    android:dividerPadding="10dp"  
    tools:context="com.jay.example.linearlayoutdemo.MainActivity" >  
  
    <Button  
        android:layout_width="wrap_content"  
        android:layout_height="wrap_content"  
        android:text="按钮1" />  
  
    <Button  
        android:layout_width="wrap_content"  
        android:layout_height="wrap_content"  
        android:text="按钮2" />  
  
    <Button  
        android:layout_width="wrap_content"  
        android:layout_height="wrap_content"  
        android:text="按钮3" />  
  
</LinearLayout>
```

#### 2. RelativeLayout(相对布局)

![img](https://www.runoob.com/wp-content/uploads/2015/07/797932661-1.png)

ex.

```xml
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"    
    xmlns:tools="http://schemas.android.com/tools"    
    android:id="@+id/RelativeLayout1"    
    android:layout_width="match_parent"    
    android:layout_height="match_parent" >    
    
    <!-- 这个是在容器中央的 -->    
    <ImageView    
        android:id="@+id/img1"     
        android:layout_width="80dp"    
        android:layout_height="80dp"    
        android:layout_centerInParent="true"    
        android:src="@drawable/pic1"/>    
        
    <!-- 在中间图片的左边 -->    
    <ImageView    
        android:id="@+id/img2"     
        android:layout_width="80dp"    
        android:layout_height="80dp"    
        android:layout_toLeftOf="@id/img1"    
        android:layout_centerVertical="true"    
        android:src="@drawable/pic2"/>    
        
    <!-- 在中间图片的右边 -->    
    <ImageView    
        android:id="@+id/img3"     
        android:layout_width="80dp"    
        android:layout_height="80dp"    
        android:layout_toRightOf="@id/img1"    
        android:layout_centerVertical="true"    
        android:src="@drawable/pic3"/>    
        
    <!-- 在中间图片的上面-->    
    <ImageView    
        android:id="@+id/img4"     
        android:layout_width="80dp"    
        android:layout_height="80dp"    
        android:layout_above="@id/img1"    
        android:layout_centerHorizontal="true"    
        android:src="@drawable/pic4"/>    
        
    <!-- 在中间图片的下面 参照：img1的下方-->    
    <ImageView    
        android:id="@+id/img5"     
        android:layout_width="80dp"    
        android:layout_height="80dp"    
        android:layout_below="@id/img1"    
        android:layout_centerHorizontal="true"    
        android:src="@drawable/pic5"/>    
    
</RelativeLayout>
```

效果：

![img](https://www.runoob.com/wp-content/uploads/2015/07/14556678.jpg)

##### margin和padding

margin用空白填充，而padding用元素本身填充

```xml
<Button    
        android:paddingLeft="100dp"     
        android:layout_height="wrap_content"    
        android:layout_width="wrap_content"    
        android:text="Button"    
        android:layout_toRightOf="@id/btn1"/>    

   <Button    
        android:layout_marginLeft="100dp"     
        android:layout_height="wrap_content"    
        android:layout_width="wrap_content"    
        android:text="Button"    
        android:layout_toRightOf="@id/btn2"     
        android:layout_alignParentBottom="true"/>    
```



#### *LayoutInflater

在实际开发中LayoutInflater这个类还是非常有用的，它的作用类似于findViewById()。不同点是LayoutInflater是用来找res/layout/下的xml布局文件，并且实例化；而findViewById()是找xml布局文件下的具体widget控件(如Button、TextView等)。

具体作用：

1、对于一个没有被载入或者想要动态载入的界面，都需要使用LayoutInflater.inflate()来载入；

2、对于一个已经载入的界面，就可以使用Activiyt.findViewById()方法来获得其中的界面元素。

LayoutInflater 是一个抽象类，在文档中如下声明：

public abstract class LayoutInflater extends Object

获得 LayoutInflater 实例的三种方式

1. LayoutInflater inflater = getLayoutInflater();//调用Activity的getLayoutInflater()
2. LayoutInflater inflater = LayoutInflater.from(context);
3. LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

### 四、UI控件

#### 1. TextView

```xml
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity"
    android:gravity="center"
    android:background="#8fffad">

    <TextView
        android:id="@+id/txtOne"
        android:layout_width="200dp"
        android:layout_height="200dp"
        android:gravity="center"
        android:text="TextView(显示框)"
        android:textColor="#EA5246"
        android:textStyle="bold|italic"
        android:background="#000000"
        android:textSize="18sp" />

</RelativeLayout>
```

- **id：**为TextView设置一个组件id，根据id，我们可以在Java代码中通过findViewById()的方法获取到该对象，然后进行相关属性的设置，又或者使用RelativeLayout时，参考组件用的也是id！
- **layout_width：**组件的宽度，一般写：**wrap_content**或者**match_parent(fill_parent)**，前者是控件显示的内容多大，控件就多大，而后者会填满该控件所在的父容器；当然也可以设置成特定的大小，比如我这里为了显示效果，设置成了200dp。
- **layout_height：**组件的宽度，内容同上。
- **gravity：**设置控件中内容的对齐方向，TextView中是文字，ImageView中是图片等等。
- **text：**设置显示的文本内容，一般我们是把字符串写到string.xml文件中，然后通过@String/xxx取得对应的字符串内容的，这里为了方便我直接就写到""里，不建议这样写！！！
- **textColor：**设置字体颜色，同上，通过colors.xml资源来引用，别直接这样写！
- **textStyle：**设置字体风格，三个可选值：**normal**(无效果)，**bold**(加粗)，**italic**(斜体)
- **textSize：**字体大小，单位一般是用sp！
- **background：**控件的背景颜色，可以理解为填充整个控件的颜色，可以是图片哦！

##### 带图片

事例：

![img](https://www.runoob.com/wp-content/uploads/2015/07/68693829.jpg)

```xml
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"  
    xmlns:tools="http://schemas.android.com/tools"  
    android:layout_width="match_parent"  
    android:layout_height="match_parent"  
    tools:context="com.jay.example.test.MainActivity" >  
  
    <TextView  
        android:layout_width="wrap_content"  
        android:layout_height="wrap_content"  
        android:layout_centerInParent="true"  
        android:drawableTop="@drawable/show1"  
        android:drawableLeft="@drawable/show1"  
        android:drawableRight="@drawable/show1"  
        android:drawableBottom="@drawable/show1"  
        android:drawablePadding="10dp"  
        android:text="张全蛋" />  
  
</RelativeLayout> 
```

效果：

![img](https://www.runoob.com/wp-content/uploads/2015/07/46436386.jpg)

##### autoLink

![img](https://www.runoob.com/wp-content/uploads/2015/07/59234627.jpg)

这样还不够，要在java代码中加：

```java
package jay.com.example.textviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView t1 = (TextView)findViewById(R.id.txtOne);
        String s1 = "<font color='blue'><b>百度一下，你就知道~：</b></font><br>";
        s1 += "<a href = 'http://www.baidu.com'>百度</a>";
        t1.setText(Html.fromHtml(s1));
        
        //此处需要手动set，才能enable超链接
        t1.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
```



#### 2. ListView

简单数据使用安卓提供的Adapter加载数据

```java
String[] data = {"LXW","SBN","YDC","GB","XZT","LJ","LZ","LLB","ZSY"};	//要加载的数据

ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,data);
        list = (ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
```



#### 3. RecycleView

引入依赖：

```txt
compile 'com.android.support:recyclerview-v7:24.2.1'
```





### 五、活动

#### 1. 生命周期

活动代表了一个具有用户界面的单一屏幕，如 Java 的窗口或者帧。Android 的活动是 ContextThemeWrapper 类的子类。Android 系统初始化它的程序是通过活动中的 onCreate() 回调的调用开始的。存在有一序列的回调方法来启动一个活动，同时有一序列的方法来关闭活动，如下面的活动声明周期图所示：

![è¿éåå¾çæè¿°](https://img-blog.csdn.net/20180820103933128?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3hpYW5LT0c=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

| 回调        | 描述                                                         |
| :---------- | :----------------------------------------------------------- |
| onCreate()  | 这是第一个回调，在活动第一次创建时调用                       |
| onStart()   | 这个回调在活动为用户可见时被调用                             |
| onResume()  | 这个回调在应用程序与用户开始可交互的时候调用                 |
| onPause()   | 被暂停的活动无法接受用户输入，不能执行任何代码。当当前活动将要被暂停，上一个活动将要被恢复是调用 |
| onStop()    | 当活动不在可见时调用                                         |
| onDestroy() | 当活动被系统销毁之前调用                                     |
| onRestart() | 当活动被停止以后重新打开时调用                               |

![img](https://www.runoob.com/wp-content/uploads/2015/08/48768883.jpg)

1. onPause()和onStop()被调用的前提是： 打开了一个新的Activity！而前者是旧Activity还可见的状态；后者是旧Activity已经不可见！

2. AlertDialog和PopWindow不会触发上述两个回调方法。

Activity/ActionBarActivity/AppCompatActivity的区别：Activity就不用说，后面这两个都是为了低版本兼容而提出的提出来的，他们都在v7包下， ActionBarActivity已被废弃，从名字就知道，ActionBar~，而在5.0后，被Google弃用了，现在用 ToolBar...而我们现在在Android Studio创建一个Activity默认继承的会是：AppCompatActivity! 当然你也可以只写Activity，不过AppCompatActivity给我们提供了一些新的东西而已！ 两个选一个。

**注意：Android中的四大组件，只要定义了，无论你用没用，都要在AndroidManifest.xml对这个组件进行声明，不然运行时程序会直接退出，报ClassNotFindException。**

#### 2. onCreate方法

有两种方法，第一种是正常操作，第二种加入持久化能力。

![img](https://www.runoob.com/wp-content/uploads/2015/08/28609433.jpg)

![img](https://www.runoob.com/wp-content/uploads/2015/08/18677320.jpg)

第二种onCreate方法一般搭配以下两种方法使用：

```java
public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState)
public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState)
```

第一种方法的使用场景

1. 点击home键回到主页或长按后选择运行其他程序
2. 按下电源键关闭屏幕
3. 启动新的Activity
4. 横竖屏切换时，肯定会执行，因为横竖屏切换的时候会先销毁Act，然后再重新创建。 
5. 重要原则：当系统"未经你许可"时销毁了你的activity，则onSaveInstanceState会被系统调用， 这是系统的责任，因为它必须要提供一个机会让你保存你的数据（你可以保存也可以不保存）。在onCreate方法中再从savedInstanceState中取出保存的临时数据

```java
if( saveInstanceState != null){
    String tempData = savedInstanceState.getString("data_key");
    Log.d(TAG, tempData);
}
```

而后一个方法，和onCreate同样可以从取出前者保存的数据： 一般是在onStart()和onResume()之间执行！ 之所以有两个可以获取到保存数据的方法，是为了避免Activity跳转而没有关闭， 然后不走onCreate()方法，而你又想取出保存数据。

在onCreate方法中使用setContentView方法为当前活动引入布局。	

#### 3. 启动Activity的方式

##### 1. 显示启动

1.1 最常见

```java
startActivity(new Intent(当前Act.this,要启动的Act.class));
```

1.2 通过Intent的ComponentName

```java
ComponentName cn = new ComponentName("当前Act的全限定类名","启动Act的全限定类名") ;
Intent intent = new Intent() ;
intent.setComponent(cn) ;
startActivity(intent) ;
```

1.3 初始化Intent时指定包名

```java
Intent intent = new Intent("android.intent.action.MAIN");
intent.setClassName("当前Act的全限定类名","启动Act的全限定类名");
startActivity(intent);
```

##### 2. 隐式启动

默认启动界面：

```xml
<intent-filter>
    <action android:name="android.intent.action.MAIN" />
    <category android:name="android.intent.category.LAUNCHER" />
</intent-filter>
```

指定跳转：

![img](https://www.runoob.com/wp-content/uploads/2015/08/291262381.jpg)

##### 3. 直接通过包名启动apk

```java
Intent intent = getPackageManager().getLaunchIntentForPackage("apk第一个启动的Activity的全限定类名") ;
if(intent != null) startActivity(intent) ;
```

#### 4. 数据传递(Intent)

通过Intent和Bundle传递数据。开启一个Activity时，把数据发送过去。

​	![img](https://www.runoob.com/wp-content/uploads/2015/08/7185831.jpg)

将子Activity的数据返回给父Activity。使用方法startActivityForResult().

![img](https://www.runoob.com/wp-content/uploads/2015/08/67124491.jpg)

显示Intent和隐式Intent的区别：

- **显式Intent**：通过组件名指定启动的目标组件,比如startActivity(new Intent(A.this,B.class)); 每次启动的组件只有一个
- **隐式Intent**：不指定组件名，而指定Intent的Action、Data或Category，当我们启动组件时, 会去匹配AndroidManifest.xml相关组件的Intent-filter，逐一匹配出满足属性的组件，当不止一个满足时, 会弹出一个让我们选择启动哪个的对话框。

Intent七个属性：

- ComponentName
- Action
- Category
- Data, Type
- Extras
- Flags

##### 显示Intent

```java
//写在Click监听器中
Intent intent = new Intent(MainActivity.this,SecondActivity.class);
startActivity(intent);
```

##### 隐式Intent

```java
//跳转至home界面
Intent it = new Intent();
it.setAction(Intent.ACTION_MAIN);
it.addCategory(Intent.CATEGORY_HOME);
startActivity(it);
```

```java
//访问外部网站
Intent it = new Intent();
it.setAction(Intent.ACTION_VIEW);
it.setData(Uri.parse("http://www.baidu.com"));
startActivity(it);
```

配置文件：

```xml
<activity android:name=".SecongActivity"
    android:launchMode="singleTask">
    <intent-filter>
        <action android:name="com.test.second"/>
        <category android:name="android.intent.category.DEFAULT" />
    </intent-filter>
</activity>
```

Activity调用：

```java
Intent i=new Intent();
i.setAction("com.test.second");
startActivity(i);
```

##### 复杂数据

数组：

```java
//写数组
Bundle bd = new Bundle();
bd.putStringArray("StringArray", new String[]{"呵呵","哈哈"});
//可把StringArray换成其他数据类型,比如int,float等等...
```

```java
//取数组
String[] str = bd.getStringArray("StringArray")
```

集合：

1、List<基本数据类型或String>

```java
//写集合
intent.putStringArrayListExtra(name, value)
intent.putIntegerArrayListExtra(name, value)
```

```java
//读集合
intent.getStringArrayListExtra(name)
intent.getIntegerArrayListExtra(name)
```

2、List< Object>

将list强转成Serializable类型,然后传入(可用Bundle做媒介)

```java
//写
intent.putExtras(key, (Serializable)list)
```

```java
//取，强转
(List<Object>) getIntent().getSerializable(key)
```

3、Map<String, Object>,或更复杂的

解决方法是：外层套个List

```java
//传递复杂些的参数 
Map<String, Object> map1 = new HashMap<String, Object>();  
map1.put("key1", "value1");  
map1.put("key2", "value2");  
List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();  
list.add(map1);  

Intent intent = new Intent();  
intent.setClass(MainActivity.this,ComplexActivity.class);  
Bundle bundle = new Bundle();  

//须定义一个list用于在budnle中传递需要传递的ArrayList<Object>,这个是必须要的  
ArrayList bundlelist = new ArrayList();   
bundlelist.add(list);   
bundle.putParcelableArrayList("list",bundlelist);  
intent.putExtras(bundle);                
startActivity(intent); 
```

##### 传递对象

传递对象的方式有两种：将对象转换为Json字符串或者通过Serializable,Parcelable序列化 不建议使用Android内置的抠脚Json解析器，可使用fastjson或者Gson第三方库。

1）将对象转换为Json字符串

Model类：

```java
public class Book{
    private int id;
    private String title;
    //...
}

public class Author{
    private int id;
    private String name;
    //...
}
```

写入数据：

```java
Book book=new Book();
book.setTitle("Java编程思想");
Author author=new Author();
author.setId(1);
author.setName("Bruce Eckel");
book.setAuthor(author);
Intent intent=new Intent(this,SecondActivity.class);
intent.putExtra("book",new Gson().toJson(book));			//转为字符串
startActivity(intent);
```

读取数据：

```java
String bookJson=getIntent().getStringExtra("book");
Book book=new Gson().fromJson(bookJson,Book.class);
Log.d(TAG,"book title->"+book.getTitle());
Log.d(TAG,"book author name->"+book.getAuthor().getName());
```

##### 返回数据

startActivityForResult()，接收两个参数，**第一个参数是intent，第二个参数是请求码**。请求码需要是唯一值。

```java
button1.setOnClickListener(new View.OnClickListener(){
   @Override
    public void onClick(View v){
        Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
        startActivityForResult(intent, 1);
    }
});
```

在第二个活动中给其按钮加上监听器，调用setResult方法，并销毁当前活动：

```java
public void onClick(View v){
    Intent intent = new Intent();
    intent.putExtra("data_return","Back First!");
    setResult(RESULT_OK, intent);
    finish();
}
```

setResult方法一般只使用RESULT_OK或RESULT_CANCELLED两个值。在SecondActivity被销毁后会回调上一个活动的onActivityResult()方法，因此需要重写第一个方法的该方法.

```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data){
    switch(requestCode){
        case 1:				//根据自己在第一个类传入的请求码判断
            if(resultCode == RESULT_OK){
                String returnedData = data.getStringExtra("data_return");
                Log.d("FirstActivity",returnedData);
            }
    }
}
```

但如果在活动B中不是点击该按钮而是按后退键返回的话，重写onBackPressed()方法。

```java
@Override
public void onBackPressed(){
    Intent intent = new Intent();
    intent.putExtra("data_return", "Hello FirstActivity");
    setResul(RESULT_OK, intent);
    finish();
}
```



#### 5. Toast

在onCreate方法中添加：

```java
protected void onCreate(Bundle savedInstanceState)｛
	setContentView(R.layout.first_layout);
	Button button1 = (Button) findViewById(R.id.button_1);
	button1.setOnClickListener(new View.OnClickListener(){
    	@Override
        public void onClick(View v){
            Toast.makeText(FirstActivity.this, "You click this button",Toast.LENGTH_SHORT);
        }
    });
｝
```

第一个参数表示Context，因为Activity本身就是一个Context对象，所以传自己即可。

第二个参数是显示的内容。

第三个参数是显示的时长，LENGTH_SHORT或LENGTH_LONG。

#### 6. Menu

```xml
<menu xmlns:android="http://schemas.android.com/apk/res/android">
	<item 
          android:id = "@+id/add_item"
          android:title="Add"/>
    <item 
          adnroid:id="@+id/remove_item"
          android:title="Remove"/>
</menu>      
```

<item>标签用来创建具体的偶一个菜单项，然后通过android:id给这个菜单指定一个唯一的标识符，通过title取名。

接着回到FirstActivity中重写onCreateOptionsMenu()方法。

```java
public boolean onCreateOptionsMenu(Menu menu){
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
}
```

返回true表示允许创建的菜单显示出来，false表示无法显示。

至此，菜单也只是显示了出来，但要让其可操控，需要在Activity中重写onOptionItemSelected(MenuItem item)

```java
public boolean onOptionsItemSelected(MenuItem item){
    switch(item.getItemId()){
        case R.id.add_item:
            ///
           break;
        case R.id.remove_item:
            ///
            break;
        default:
            
    }
    return true;
}
```

##### 

### 六、碎片

#### 底部导航栏切换

##### 编写Fragment类

```java
public class StepFragment extends Fragment {

    private String[] name = {"LXW","SBN","YDC","GB","XZT","LJ","LZ","LLB","ZSY"};

    private List<Item> itemList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.step_layout, container, false);

        initList();

        MainActivity main = (MainActivity)getActivity();

        ItemAdapter itemAdapter = new ItemAdapter(main, R.layout.my_item, itemList);

        ListView listView = (ListView) view.findViewById(R.id.list_view);

        //adapter添加数据
        listView.setAdapter(itemAdapter);

        //为其中的每一个项目加上点击响应事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Item item = itemList.get(position);
                Toast.makeText(getActivity(), item.getName(),Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void initList(){

        Log.d("LXW","init");

        for(int i=0;i<6;++i){
            Item item = new Item(name[i], R.drawable.port);
            itemList.add(item);
        }
        for(int i=0;i<6;++i){
            Item item = new Item(name[i], R.drawable.port);
            itemList.add(item);
        }
    }
}

```

##### Fragment xml页面

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical" android:layout_width="match_parent"
    android:layout_height="match_parent">

    <ListView
        android:id="@+id/list_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"></ListView>

</LinearLayout>
```

##### List item 页面

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation ="vertical">
                <ImageView
                    android:id = "@+id/item_image"
                    android:layout_width = "60dp"
                    android:layout_height = "60dp"
                    />

                <TextView
                    android:id = "@+id/item_name"
                    android:layout_width = "match_parent"
                    android:layout_height = "wrap_content"
                    android:layout_gravity = "center_vertical"
                    />
        </LinearLayout>

</LinearLayout>
```

##### Item类

```java
public class Item {
    int imageId;
    String name;

    public Item(String name, int imageId){
        this.name = name;
        this.imageId = imageId;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

```

##### 主活动的xml页面

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    >

    <FrameLayout
        android:id = "@+id/main_fragment"
        android:layout_width = "match_parent"
        android:layout_height = "0dp"
        android:layout_weight = "1"
        />

    <!--<android.support.v4.view.ViewPager-->
    <!--android:id="@+id/fragment_vp"-->
    <!--android:layout_width="match_parent"-->
    <!--android:layout_height="match_parent"-->
    <!--android:layout_above="@+id/tabs_rg" />-->

    <!--分割线-->
    <TextView
        android:layout_width="match_parent"
        android:layout_height="1dp"
        android:background="#6C7070"
        />

    <RadioGroup
        android:id="@+id/tabs_rg"
        android:layout_width="match_parent"
        android:layout_height= "60dp"
        android:layout_alignParentBottom="true"
        android:background="#EBE9E9"
        android:orientation="horizontal">

        <RadioButton
            android:id="@+id/step"
            android:layout_width="0dp"
            android:layout_weight="1"
            android:layout_height="match_parent"
            android:button="@null"
            android:layout_marginTop="5dp"
            android:drawableTop="@drawable/main_bottombar_icon_home_selector"
            android:textColor="@color/main_bottombar_text_selector"
            android:gravity="center"
            android:checked="true"
            android:text="今日" />

        <RadioButton
            android:id="@+id/news"
            android:layout_width="0dp"
            android:layout_weight="1"
            android:layout_height="match_parent"
            android:button="@null"
            android:layout_marginTop="5dp"
            android:drawableTop="@drawable/main_bottombar_icon_home_selector"
            android:textColor="@color/main_bottombar_text_selector"
            android:gravity="center"
            android:text="消息" />

        <RadioButton
            android:id="@+id/ticket"
            android:layout_width="0dp"
            android:layout_weight="1"
            android:layout_height="match_parent"
            android:button="@null"
            android:layout_marginTop="5dp"
            android:drawableTop="@drawable/main_bottombar_icon_home_selector"
            android:textColor="@color/main_bottombar_text_selector"
            android:gravity="center"
            android:text="通讯录" />

        <RadioButton
            android:id="@+id/self_info"
            android:layout_width="0dp"
            android:layout_weight="1"
            android:layout_height="match_parent"
            android:button="@null"
            android:layout_marginTop="5dp"
            android:drawableTop="@drawable/main_bottombar_icon_home_selector"
            android:textColor="@color/main_bottombar_text_selector"
            android:gravity="center"
            android:text="个人中心" />
    </RadioGroup>
</LinearLayout>
```

##### 自定义adapter

```java
package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.model.Item;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<Item> {

    private int resourceId;

    /**
     *
     * @param context
     * @param textViewResourceId    需要使用的layout Id
     * @param objects               需要加载的list数据
     */
    public ItemAdapter(Context context, int textViewResourceId, List<Item> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item item = getItem(position);

        //寻找布局文件并且实例化
        View view;
        if(convertView ==null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        }else{
            view = convertView;
        }

        ImageView itemView = (ImageView) view.findViewById(R.id.item_image);
        TextView textView = (TextView) view.findViewById(R.id.item_name);

        itemView.setImageResource(item.getImageId());
        textView.setText(item.getName());

        return view;
    }

    class ViewHolder{
        ImageView itemImage;
        TextView itemName;
    }
}

```



#### 利用ViewPager滑动切换

##### 主活动视图xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    >

    <!--<FrameLayout-->
        <!--android:id = "@+id/main_fragment"-->
        <!--android:layout_width = "match_parent"-->
        <!--android:layout_height = "0dp"-->
        <!--android:layout_weight = "1"-->
        <!--/>-->

    <androidx.viewpager.widget.ViewPager
        android:id="@+id/viewPager"
        android:layout_width="match_parent"
        android:layout_height="match_parent">
    </androidx.viewpager.widget.ViewPager>

    <!--分割线-->
    <TextView
        android:layout_width="match_parent"
        android:layout_height="1dp"
        android:background="#6C7070"
        />

    <RadioGroup
        android:id="@+id/tabs_rg"
        android:layout_width="match_parent"
        android:layout_height= "60dp"
        android:layout_alignParentBottom="true"
        android:background="#EBE9E9"
        android:orientation="horizontal">

        <RadioButton
            android:id="@+id/step"
            android:layout_width="0dp"
            android:layout_weight="1"
            android:layout_height="match_parent"
            android:button="@null"
            android:layout_marginTop="5dp"
            android:drawableTop="@drawable/main_bottombar_icon_home_selector"
            android:textColor="@color/main_bottombar_text_selector"
            android:gravity="center"
            android:checked="true"
            android:text="今日" />

        <RadioButton
            android:id="@+id/news"
            android:layout_width="0dp"
            android:layout_weight="1"
            android:layout_height="match_parent"
            android:button="@null"
            android:layout_marginTop="5dp"
            android:drawableTop="@drawable/main_bottombar_icon_home_selector"
            android:textColor="@color/main_bottombar_text_selector"
            android:gravity="center"
            android:text="消息" />

        <RadioButton
            android:id="@+id/self_info"
            android:layout_width="0dp"
            android:layout_weight="1"
            android:layout_height="match_parent"
            android:button="@null"
            android:layout_marginTop="5dp"
            android:drawableTop="@drawable/main_bottombar_icon_home_selector"
            android:textColor="@color/main_bottombar_text_selector"
            android:gravity="center"
            android:text="我" />
    </RadioGroup>
</RelativeLayout>
```

##### FragmentPagerAdapter 类

```java

//viewPager的adapter
public class MyPagerAdapter extends FragmentPagerAdapter {
    private FragmentManager mfragmentManager;
    private List<Fragment> mlist;

    public MyPagerAdapter(FragmentManager fm, List<Fragment> list){
        super(fm);
        this.mlist = list;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public Fragment getItem(int arg0) {
        return mlist.get(arg0);//显示第几个页面
    }

    @Override
    public int getCount() {
        return mlist.size();//有几个页面
    }
}

```

##### Activity类

```java
public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private ArrayList<Fragment> fragments;
    private ViewPager viewPager;
    private MyPagerAdapter myPagerAdapter;

    private RadioGroup radioGroup;
    private RadioButton rbNews;
    private RadioButton rbStep;
    private RadioButton rbSelf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        initView();
        initRadioButton();
        setListeners();
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i){
            case R.id.step:
                viewPager.setCurrentItem(0);
                break;
            case R.id.news:
                viewPager.setCurrentItem(1);
                break;
            case R.id.self_info:
                viewPager.setCurrentItem(2);
                break;
                default:
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    //设置viewPager
    private void initView(){
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        fragments = new ArrayList<>();
        fragments.add(new StepFragment());
        fragments.add(new NewsFragment());
        fragments.add(new SelfFragment());

        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(myPagerAdapter);
    }

    //设置底部按钮的属性和点击事件
    private void initRadioButton(){
        radioGroup = findViewById(R.id.tabs_rg);
        radioGroup.setOnCheckedChangeListener(this);

        //底部栏设置图片大小
        setDrawable(R.id.step, R.drawable.code);
        setDrawable(R.id.news, R.drawable.code);
        setDrawable(R.id.self_info, R.drawable.code);


        //设置监听器
        rbNews = (RadioButton)findViewById(R.id.news);
        //rbNews.setOnClickListener(this);
        rbStep = (RadioButton)findViewById(R.id.step);
        //rbStep.setOnClickListener(this);
        rbSelf = findViewById(R.id.self_info);
        //rbSelf.setOnClickListener(this);
    }

    //调整图片大小
    private void setDrawable(int buttonId, int imageId){
        RadioButton radioButton = (RadioButton)findViewById(buttonId);
        Drawable drawable = getResources().getDrawable(imageId);
        drawable.setBounds(0,0,65,65);
        radioButton.setCompoundDrawables(null,drawable,null,null);
    }

    //滑动的同时保持按钮同步
    private void setListeners() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        rbStep.setChecked(true);
                        break;
                    case 1:
                        rbNews.setChecked(true);
                        break;
                    case 2:
                        rbSelf.setChecked(true);
                        break;
                }
            }
            public void onPageScrolled(int position, float arg1, int arg2) { }

            public void onPageScrollStateChanged(int state) { }
        });
    }
}

```



### 七、广播

广播接收器可以自由地注册广播，然后可以接收到该广播，并做处理。注册广播有两种方式：静态和动态。

可以

#### 简单使用

1.创建Broadcast类

```java
public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"test broadcast", Toast.LENGTH_LONG).show();
    }
}
```

2.在Manifest.xml中注册receiver

```xml
<application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
        <activity android:name=".activity.MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <receiver
            android:name=".utils.MyBroadcastReceiver"
            android:enabled="true"
            android:exported="true">
            <intent-filter>
                <action android:name="com.example.myapplication.MY_BROADCAST"/>
            </intent-filter>
        </receiver>
    </application>
```

3.发送广播

```java
@Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn:
                Intent intent = new Intent("com.example.myapplication.MY_BROADCAST");
                activity.sendBroadcast(intent);
                break;
            default:
        }
    }


```

#### 本地广播

```java
private LocalReceiver localReceiver;
private LocalBroadcastManager localBroadcastManager;		//对广播进行管理

private IntentFilter intentFilter;

protected void onCreate(Bundle savedInstanceState){
    //...
    localBroadcastManager = LocalBroadcastManger.getInstance(this);		//获取实例
    //...
    intentFilter = new IntentFilter();
    intentFilter.addAction("com.example.myapplication.LOCAL_BROADCAST");
    localReceiver = new LocalReceiver();
    localBroadcastManager.registerReceiver(localReceiver, intentFilter);	//注册本地广播监听器
}
```

### 八、数据持久化

三种方法：文件存储、SharedPreference存储、数据库存储

文件存储：适合简单的文本数据或二进制数据

SharedPreference存储：键值对方式存储

#### 简单使用

```java
SharedPreferences.Editor editor = getSharedPreferences("data", MORE_PRIVATE).edit();

//put
editor.putString("name", "Tom");
editor.putInt("age",28);
editor.putBoolean("married", false);
editor.apply();


//get
SharedPreforences pref = getSharedPreferences("data", MODE_PRIVATE);
String name = pref.getString("name","");				//第二个参数为默认值
int age = pref.getInt("age", 0);
boolean married = pref.getBoolean("married", false);
```

#### 记住密码

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical" android:layout_width="match_parent"
    android:layout_height="match_parent">

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Welcome"
        android:textSize="70sp"
        />

    <View
        android:layout_width="match_parent"
        android:layout_height="1dp"
        android:background="#000000"/>

    <EditText
        android:id="@+id/username"
        android:layout_width="match_parent"
        android:layout_height="60dp"
        android:hint="Input username"/>

    <EditText
        android:id="@+id/pswd"
        android:inputType="textPassword"
        android:layout_width="match_parent"
        android:layout_height="60dp"
        android:hint="Input password"/>

    <LinearLayout
        android:orientation="horizontal"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">
        <CheckBox
            android:id="@+id/remember_pass"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content" />
        <TextView
            android:textSize="18sp"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Remember me"/>
    </LinearLayout>

    <Button
        android:id="@+id/login"
        android:layout_width="match_parent"
        android:layout_height="60dp"
        android:text="Login">
    </Button>

</LinearLayout>
```

LoginActivity

```java
package com.example.myapplication.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;


//实现记住账号密码
public class LoginActivity  extends AppCompatActivity implements View.OnClickListener {

    private Button submit;
    private EditText userName;
    private EditText pswd;
    private CheckBox remember;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        initView();
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        boolean isRemember = pref.getBoolean("remember_password",false);
        if(isRemember){
            String account = pref.getString("account","");
            String password = pref.getString("password","");
            userName.setText(account);
            pswd.setText(password);
            remember.setChecked(true);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login:
                submit();
                break;
            default:
        }
    }

    private void submit(){
        String account = userName.getText().toString();
        String password = pswd.getText().toString();
        if(account.equals("admin") && password.equals("123456")){
            editor = pref.edit();
            if(remember.isChecked()){           //保存账号密码
                editor.putBoolean("remember_password",true);
                editor.putString("account",account);
                editor.putString("password",password);
            }else{
                editor.clear();
            }
            editor.apply();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();           //结束本活动
        }
    }

    private void initView(){
        submit = findViewById(R.id.login);
        userName = findViewById(R.id.username);
        pswd = findViewById(R.id.pswd);
        remember = findViewById(R.id.remember_pass);

        submit.setOnClickListener(this);
    }
}
```

#### SQLite

留坑



### 九、网络编程

需要在Manifest中开启权限：

```xml
<uses-permission android:name="android.permission.INTERNET"></uses-permission>
```



#### WebView

xml中使用WebView控件；java代码中为其设置网址

```java
	private void initView(){
        webView = thisView.findViewById(R.id.web);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.cnblogs.com/xiuwenli/");
    }
```

#### HttpURLConnection

工具类：

```java
/**
 * 发送http的工具类
 */
public class HttpUtil {
    //封装的发送请求函数

    /**
     *
     * @param address   URL address
     * @param method    POST/GET
     * @param token     your token
     * @param params    请求中的参数
     * @param listener  回调监听器
     */
    public static void sendHttpRequest(final String address, String method, String token,
                                       HashMap<String,Object> params,
                                       final HttpCallbackListener listener) {
        if (!HttpUtil.isNetworkAvailable()){
            //这里写相应的网络设置处理
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try{
                    URL url = new URL(address);
                    //使用HttpURLConnection
                    connection = (HttpURLConnection) url.openConnection();
                    //请求方法
                    connection.setRequestMethod(method);
                    //连接超时时间
                    connection.setConnectTimeout(8000);
                    //读取超时异常
                    connection.setReadTimeout(8000);

                    connection.setUseCaches(false);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);

                    connection.setRequestProperty("Authorization", token);
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");//设置参数类型是json格式
                    connection.setRequestProperty("Connection", "Keep-Alive");
                    connection.setConnectTimeout(5 * 1000);


                    String paramString = getParams(params);
                    DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
                    dos.write(String.valueOf(paramString).getBytes());
                    dos.flush();
                    dos.close();
                    connection.connect();

//                    //获取返回结果
//                    InputStream inputStream = connection.getInputStream();
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//                    StringBuilder response = new StringBuilder();
//                    String line;
//                    while ((line = reader.readLine()) != null){
//                        response.append(line);
//                    }
                    int uid = connection.getResponseCode();


                    //成功则回调onFinish
                    if (uid ==200 && listener != null){
                        String response = null;
                        if(connection.getInputStream() != null){
                            BufferedReader in  = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));
                            response = URLDecoder.decode(in.readLine(),"UTF-8");
                        }
                        //回调
                        Log.d("Response",response);
                        listener.onFinish(response);
                    }
                }catch(SocketTimeoutException e){
                    e.printStackTrace();
                    if(listener != null){
                        listener.onTimeOut(e);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    //出现异常则回调onError
                    if (listener != null){
                        listener.onError(e);
                    }
                }finally {
                    if (connection != null){
                        try {
                            //主动关闭inputStream
                            //这里不需要进行判空操作
                            connection.getInputStream().close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    //构建Http请求参数
    public static String getParams(HashMap<String, Object> params){
        StringBuffer tmp = new StringBuffer();
        for(Map.Entry<String, Object> entry: params.entrySet()){
            tmp.append(entry.getKey()).append("=");
            tmp.append(entry.getValue());
            tmp.append("&");
        }
        tmp.deleteCharAt(tmp.length() - 1);
        return tmp.toString();
    }

    //组装出带参数的完整URL
    public static String getURLWithParams(String address,HashMap<String,String> params) throws UnsupportedEncodingException {
        //设置编码
        final String encode = "UTF-8";

        StringBuilder url = new StringBuilder(address);
        url.append("?");
        //将map中的key，value构造进入URL中
        for(Map.Entry<String, String> entry:params.entrySet())
        {
            url.append(entry.getKey()).append("=");
            url.append(URLEncoder.encode(entry.getValue(), encode));
            url.append("&");
        }
        //删掉最后一个&
        url.deleteCharAt(url.length() - 1);
        return url.toString();
    }

    //判断当前网络是否可用
    public static boolean isNetworkAvailable(){
        //这里检查网络，后续再添加
        return true;
    }
}
```

回调接口

```java
public interface HttpCallbackListener {
    void onFinish(String response);

    void onError(Exception e);

    void onTimeOut(Exception e);
}

```

### 十、服务

#### 修改UI

使用异步消息处理机制



