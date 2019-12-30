 

https://www.kancloud.cn/aollo/aolloopencv/260405



#### 读写图片

读：

```python
imread(img_path,flag) 读取图片，返回图片对象
    img_path: 图片的路径，即使路径错误也不会报错，但打印返回的图片对象为None
    flag：cv2.IMREAD_COLOR，读取彩色图片，图片透明性会被忽略，为默认参数，也可以传入1
          cv2.IMREAD_GRAYSCALE,按灰度模式读取图像，也可以传入0
          cv2.IMREAD_UNCHANGED,读取图像，包括其alpha通道，也可以传入-1
```



显示：

```python
imshow(window_name,img)：显示图片，窗口自适应图片大小
    window_name: 指定窗口的名字
    img：显示的图片对象
    可以指定多个窗口名称，显示多个图片
    
waitKey(millseconds)  键盘绑定事件，阻塞监听键盘按键，返回一个数字（不同按键对应的数字不同）
    millseconds: 传入时间毫秒数，在该时间内等待键盘事件；传入0时，会一直等待键盘事件
    
destroyAllWindows(window_name) 
    window_name: 需要关闭的窗口名字，不传入时关闭所有窗口
```



写：

```python
imwrite(img_path_name,img)
    img_path_name:保存的文件名
    img：文件对象
```





例：

```python 
# -*- coding: utf-8 -*-  
import cv2
 
# 载入图像
im = cv2.imread('./0.png')
 
# 打印图像尺寸
h,w = im.shape[:2]
print(h,w)
 
# 保存PNG格式图像为JPEG格式
cv2.imwrite('./0.jpg',im)
```



颜色空间转换



OpenCV中颜色模式的默认设置顺序是**BGR**，不同与Matplotlib。因此，要在RGB模式下查看图像，我们需要将它从BGR转换为RGB。

```python
import numpy as np
import matplotlib.pyplot as plt

%matplotlib inline

# Import the image
img = cv2.imread('burano.jpg')
# Convert the image into RGB
img_rgb = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)

plt.imshow(img_rgb)
```

灰度图

```python
# Convert the image into gray scale
img_gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

plt.imshow(img_gray, cmap = 'gray')
```





#### 像素获取和编辑

```python
img = cv2.imread(r"C:\Users\Administrator\Desktop\roi.jpg")

#获取和设置
pixel = img[100,100]  #[57 63 68],获取(100,100)处的像素值
img[100,100]=[57,63,99] #设置像素值
b = img[100,100,0]    #57, 获取(100,100)处，blue通道像素值
g = img[100,100,1]    #63
r = img[100,100,2]      #68
r = img[100,100,2]=99    #设置red通道值

#获取和设置
piexl = img.item(100,100,2)
img.itemset((100,100,2),99)
```



属性：

```python
import cv2
img = cv2.imread(r"C:\Users\Administrator\Desktop\roi.jpg")

#rows,cols,channels
img.shape   #返回(280, 450, 3), 宽280(rows)，长450(cols)，3通道(channels)
#size
img.size    #返回378000，所有像素数量，=280*450*3
#type
img.dtype   #dtype('uint8')
```



截取：

```python
roi = img[100:200,300:400]  #截取100行到200行，列为300到400列的整块区域
img[50:150,200:300] = roi   #将截取的roi移动到该区域 （50到100行，200到300列）
b = img[:,:,0]  #截取整个蓝色通道

b,g,r = cv2.split(img) #截取三个通道，比较耗时
img = cv2.merge((b,g,r))
```



添加边界：

```python
cv2.copyMakeBorder()
    参数：
        img:图像对象
        top,bottom,left,right: 上下左右边界宽度，单位为像素值
        borderType:
            cv2.BORDER_CONSTANT, 带颜色的边界，需要传入另外一个颜色值
            cv2.BORDER_REFLECT, 边缘元素的镜像反射做为边界
            cv2.BORDER_REFLECT_101/cv2.BORDER_DEFAULT
            cv2.BORDER_REPLICATE, 边缘元素的复制做为边界
            CV2.BORDER_WRAP
        value: borderType为cv2.BORDER_CONSTANT时，传入的边界颜色值，如[0,255,0]
```



从二进制读图片：

```python

```



导出二进制：

1. cv2.IMWRITE_JPEG_QUALITY 设置图片格式为.jpeg或者.jpg的图片质量，其值为0---100（数值越大质量越高），默认95
2.  cv2.IMWRITE_WEBP_QUALITY 设置图片的格式为.webp格式的图片质量，值为0--100
3.  cv2.IMWRITE_PNG_COMPRESSION 设置.png格式的压缩比，其值为0--9（数值越大，压缩比越大），默认为3

```python
cv2.imencode('.jpg', image, [cv2.IMWRITE_JPEG_QUALITY, 90])[1] 
```



